package com.xhonell.common.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.StorageClass;
import com.qcloud.cos.model.UploadResult;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.TransferManagerConfiguration;
import com.qcloud.cos.transfer.Upload;
import com.xhonell.common.exception.BizException;
import com.xhonell.common.enums.common.SystemErrorEnum;
import com.xhonell.common.properties.CosProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * program: BaseServer
 * ClassName CosUploadUtil
 * description:
 * author: xhonell
 * create: 2025年10月19日00时34分
 * Version 1.0
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class CosUploadUtil {

    // 默认配置，可从配置文件加载
    private final CosProperties cosProperties;

    private static final StorageClass DEFAULT_STORAGE_CLASS = StorageClass.Standard_IA;

    private COSClient createCOSClient() {
        COSCredentials cred = new BasicCOSCredentials(cosProperties.getSecretId(), cosProperties.getSecretKey());
        ClientConfig config = new ClientConfig(new Region(cosProperties.getRegion()));
        return new COSClient(cred, config);
    }

    private TransferManager createTransferManager() {
        // 创建一个 COSClient 实例，这是访问 COS 服务的基础实例。
        // 详细代码参见本页: 简单操作 -> 创建 COSClient
        COSClient cosClient = createCOSClient();
        // 自定义线程池大小，建议在客户端与 COS 网络充足（例如使用腾讯云的 CVM，同地域上传 COS）的情况下，设置成16或32即可，可较充分的利用网络资源
        // 对于使用公网传输且网络带宽质量不高的情况，建议减小该值，避免因网速过慢，造成请求超时。
        ExecutorService threadPool = Executors.newFixedThreadPool(32);

        // 传入一个 threadpool, 若不传入线程池，默认 TransferManager 中会生成一个单线程的线程池。
        return new TransferManager(cosClient, threadPool);
    }

    /**
     * 智能上传 InputStream（自动识别文件名）
     *
     * @param inputStream 文件流
     * @param source      可为 File、URL、MultipartFile、null
     */
    public com.xhonell.common.domain.entity.File uploadAuto(Object source, InputStream inputStream) {
        COSClient cosClient = null;
        TransferManager transferManager = null;

        try {
            // 获取文件名
            String fileName = extractFileName(source);

            // 猜测 Content-Type
            String contentType = detectContentType(inputStream, fileName);

            // 分类路径
            String typeDir = classifyByType(contentType);
            String suffix = extractSuffix(fileName);
            String key = typeDir + "/" + UUID.randomUUID().toString().replace("-", "") + suffix;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);

            // 初始化客户端
            cosClient = createCOSClient();
            PutObjectRequest request = new PutObjectRequest(cosProperties.getBucket(), key, inputStream, metadata);
            request.setStorageClass(DEFAULT_STORAGE_CLASS);

            // 判断是否使用分块
            long size = tryGetStreamSize(inputStream);
            boolean useTransfer = size == -1 || size > 5 * 1024 * 1024L;
            transferManager = createTransferManager();
            if (useTransfer) {

                TransferManagerConfiguration cfg = new TransferManagerConfiguration();
                cfg.setMultipartUploadThreshold(5 * 1024 * 1024L);
                cfg.setMinimumUploadPartSize(1024 * 1024L);
                transferManager.setConfiguration(cfg);
            }
            Upload upload = transferManager.upload(request);
            UploadResult uploadResult = upload.waitForUploadResult();
            com.xhonell.common.domain.entity.File file = new com.xhonell.common.domain.entity.File();
            String fileUrl = uploadResult.getKey();
            return file.setFileUrl(fileUrl).setFilePath(cosProperties.getBasePath());
        } catch (Exception e) {
            log.error("文件上传失败：{}",e.getMessage(), e);
            throw new BizException(SystemErrorEnum.UPLOAD_FAILED);
        } finally {
            try {
                if (Objects.nonNull(inputStream)) inputStream.close();
            } catch (IOException ignored) {
            }
            if (Objects.nonNull(transferManager)) transferManager.shutdownNow(true);
            if (Objects.nonNull(cosClient)) cosClient.shutdown();
        }
    }

    // 根据来源智能提取文件名
    private String extractFileName(Object source) {
        if (source == null) return "unknown-" + System.currentTimeMillis();

        if (source instanceof File file) {
            return file.getName();
        }
        if (source instanceof URL url) {
            return Paths.get(url.getPath()).getFileName().toString();
        }
        // Spring MultipartFile 兼容
        if (source instanceof MultipartFile file) {
            return file.getOriginalFilename();
        }

        return "unknown-" + System.currentTimeMillis();
    }

    private String detectContentType(InputStream in, String fileName) {
        try {
            if (fileName != null && fileName.contains(".")) {
                String type = URLConnection.guessContentTypeFromName(fileName);
                if (type != null) return type;
            }
            String type = URLConnection.guessContentTypeFromStream(in);
            if (type != null) return type;
        } catch (IOException ignored) {
        }
        return "application/octet-stream";
    }

    private String classifyByType(String contentType) {
        if (contentType == null) return "others";
        if (contentType.startsWith("image")) return "images";
        if (contentType.startsWith("video")) return "videos";
        if (contentType.startsWith("audio")) return "audios";
        if (contentType.startsWith("text")) return "docs";
        return "others";
    }

    private String extractSuffix(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return "";
    }

    private long tryGetStreamSize(InputStream inputStream) {
        try {
            return inputStream.available();
        } catch (IOException e) {
            return -1;
        }
    }
}
