package com.xhonell.common.utils;

import com.xhonell.common.exception.BizException;
import com.xhonell.common.properties.CosProperties;
import com.xhonell.common.properties.EmailProperties;
import com.xhonell.common.properties.RedisPrefixProperties;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;
import java.util.Random;

/**
 * program: BaseServer
 * ClassName EmailUtil
 * description:
 * author: xhonell
 * create: 2025年10月17日23时52分
 * Version 1.0
 **/
@Component
@RequiredArgsConstructor
@Slf4j
public class EmailUtil {


    private final JavaMailSender mailSender;

    private final EmailProperties emailProperties;

    private final RedisUtil redisUtil;

    // 验证码有效期，单位分钟
    private static final long CODE_EXPIRE_MINUTES = 5;


    // 注册邮件主题
    private static final String REGISTER_SUBJECT = "龙腾华夏·注册验证码";

    /**
     * 给指定邮箱发送注册验证码，一步完成
     *
     * @param email 收件人邮箱
     */
    @Async
    public void sendRegisterCode(String email) {
        AssertUtil.isTrue(!redisUtil.hasKey(String.format(RedisPrefixProperties.EMAIL_REGISTER_CODE_PREFIX, email)), "请稍等一会吧~操作频繁");;

        String code = generateCode();

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // 发件人必须和 SMTP 授权码对应
            helper.setFrom(emailProperties.getUsername());
            helper.setTo(email);
            helper.setSubject(REGISTER_SUBJECT);
            helper.setText(buildHtmlContent(code), true); // HTML 邮件

            mailSender.send(message);


            // 保存验证码到 Redis
            redisUtil.set(String.format(RedisPrefixProperties.EMAIL_REGISTER_CODE_PREFIX, email),  code, RedisPrefixProperties.EXPIRE_TIME_SHORT);

            log.info("验证码已发送到 {}，验证码为 {}", email, code);

        }  catch (Exception e) {
            log.error("邮件发送失败：{}", e.getMessage(), e);
            throw new BizException("邮件发送异常");
        }
    }

    /**
     * 验证注册验证码
     *
     * @param email 收件人邮箱
     * @param code  用户输入验证码
     * @return true 验证成功，false 验证失败
     */
    public boolean verifyRegisterCode(String email, String code) {
        String cacheCode = redisUtil.get(String.format(RedisPrefixProperties.EMAIL_REGISTER_CODE_PREFIX, email));
        if (Objects.isNull(cacheCode)) return false;
        boolean result = cacheCode.equals(code);
        if (result) redisUtil.del(String.format(RedisPrefixProperties.EMAIL_REGISTER_CODE_PREFIX, email));
        return result;
    }

    /**
     * 构建 HTML 邮件内容
     */
    private String buildHtmlContent(String code) {
        return """
                 <table cellpadding="0" cellspacing="0" border="0" width="100%%" style="min-height: 100vh;">
                                    <tr>
                                        <td align="center" style="padding: 40px 20px;">
                                            <table cellpadding="0" cellspacing="0" border="0" width="600" style="max-width: 600px; background-color: #ffffff; border-radius: 24px; box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3); border: 4px solid #d4af37; overflow: hidden;">
                
                                                <!-- 顶部装饰带 -->
                                                <tr>
                                                    <td style="background: linear-gradient(90deg, #d4af37 0%%, #ffd700 50%%, #d4af37 100%%); height: 6px;"></td>
                                                </tr>
                
                                                <!-- 头部 -->
                                                <tr>
                                                    <td style="background: linear-gradient(135deg, #c8102e 0%%, #8b0000 50%%, #660000 100%%); padding: 40px 30px; text-align: center; position: relative;">
                                                        <div style="font-size: 64px; margin-bottom: 15px; position: relative; z-index: 1;">🐉</div>
                                                        <h1 style="color: #ffffff; font-size: 28px; margin: 0 0 10px 0; letter-spacing: 4px; font-weight: bold; text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3); position: relative; z-index: 1;">青少年爱国教育平台</h1>
                                                        <p style="color: #ffd700; font-size: 16px; margin: 0; letter-spacing: 3px; font-weight: bold; position: relative; z-index: 1;">龙腾华夏 · 爱我中华</p>
                                                    </td>
                                                </tr>
                
                                                <!-- 装饰分割线 -->
                                                <tr>
                                                    <td style="padding: 0 30px;">
                                                        <table cellpadding="0" cellspacing="0" border="0" width="100%%" style="margin-top: -15px;">
                                                            <tr>
                                                                <td style="background: linear-gradient(90deg, #c8102e, #d4af37, #c8102e); height: 3px; border-radius: 3px;"></td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                
                                                <!-- 主要内容 -->
                                                <tr>
                                                    <td style="padding: 40px 40px 20px 40px;">
                                                        <h2 style="color: #c8102e; font-size: 24px; margin: 0 0 20px 0; letter-spacing: 2px; font-weight: bold;">尊敬的用户，您好！</h2>
                                                        <p style="color: #333; font-size: 16px; line-height: 1.8; margin: 0 0 30px 0; letter-spacing: 1px;">感谢您使用<span style="color: #c8102e; font-weight: bold;"> 
                                                        <a href="https://patriotic.xhongwong.cn/#/register" target="_blank" style="color: #c8102e; font-weight: bold; text-decoration: underline;">青少年爱国教育平台</a>
                                                        </span>。您正在进行邮箱验证操作，请使用以下验证码完成验证：</p>
                
                                                        
                                                        <!-- 验证码容器 -->
                                                        <table cellpadding="0" cellspacing="0" border="0" width="100%%">
                                                            <tr>
                                                                <td align="center" style="padding: 30px 0;">
                                                                    <div style="margin-bottom: 15px;">
                                                                        <span style="font-size: 24px; margin: 0 8px;">🏮</span>
                                                                        <span style="font-size: 28px;">🐉</span>
                                                                        <span style="font-size: 24px; margin: 0 8px;">🏮</span>
                                                                    </div>
                                                                    <div style="display: inline-block; background: linear-gradient(135deg, #c8102e 0%%, #8b0000 100%%); padding: 4px; border-radius: 12px; box-shadow: 0 8px 24px rgba(200, 16, 46, 0.3);">
                                                                        <div style="background: white; padding: 20px 40px; border-radius: 10px; border: 3px solid #d4af37;">
                                                                            <span style="font-size: 36px; font-weight: bold; letter-spacing: 8px; color: #c8102e; font-family: 'Courier New', monospace;">%s</span>
                                                                        </div>
                                                                    </div>
                                                                    <div style="margin-top: 15px;">
                                                                        <span style="font-size: 24px; margin: 0 8px;">🏮</span>
                                                                        <span style="font-size: 28px;">🐉</span>
                                                                        <span style="font-size: 24px; margin: 0 8px;">🏮</span>
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                        </table>
                
                                                        <!-- 提示信息框 -->
                                                        <table cellpadding="0" cellspacing="0" border="0" width="100%%" style="margin-top: 30px;">
                                                            <tr>
                                                                <td style="background: linear-gradient(135deg, #fff5f5 0%%, #ffe6e6 100%%); border-left: 4px solid #c8102e; border-radius: 8px; padding: 20px;">
                                                                    <p style="margin: 0 0 10px 0; color: #c8102e; font-size: 15px; font-weight: bold;">⚠️ 重要提示</p>
                                                                    <ul style="margin: 0; padding-left: 20px; color: #666; font-size: 14px; line-height: 1.8;">
                                                                        <li>验证码有效期为 <strong style="color: #c8102e;">5分钟</strong>，请尽快使用</li>
                                                                        <li>请勿将验证码泄露给他人，以保护账户安全</li>
                                                                        <li>如非本人操作，请忽略此邮件</li>
                                                                    </ul>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                
                                                <!-- 特色功能介绍 -->
                                                <tr>
                                                    <td style="padding: 20px 40px;">
                                                        <table cellpadding="0" cellspacing="0" border="0" width="100%%">
                                                            <tr>
                                                                <td style="background: linear-gradient(135deg, #fff8e6 0%%, #fff4d9 100%%); border-radius: 12px; padding: 25px; border: 2px solid #d4af37;">
                                                                    <h3 style="color: #c8102e; font-size: 18px; margin: 0 0 15px 0; text-align: center; letter-spacing: 2px;">✨ 平台特色功能 ✨</h3>
                                                                    <table cellpadding="0" cellspacing="0" border="0" width="100%%">
                                                                        <tr>
                                                                            <td width="50%%" style="padding: 8px;">
                                                                                <div style="font-size: 14px; color: #333;">
                                                                                    <span style="color: #c8102e; font-weight: bold;">📚</span> 海量学习资源
                                                                                </div>
                                                                            </td>
                                                                            <td width="50%%" style="padding: 8px;">
                                                                                <div style="font-size: 14px; color: #333;">
                                                                                    <span style="color: #c8102e; font-weight: bold;">🎯</span> 个性化推荐
                                                                                </div>
                                                                            </td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td width="50%%" style="padding: 8px;">
                                                                                <div style="font-size: 14px; color: #333;">
                                                                                    <span style="color: #c8102e; font-weight: bold;">🏆</span> 积分奖励系统
                                                                                </div>
                                                                            </td>
                                                                            <td width="50%%" style="padding: 8px;">
                                                                                <div style="font-size: 14px; color: #333;">
                                                                                    <span style="color: #c8102e; font-weight: bold;">👥</span> 社区交流互动
                                                                                </div>
                                                                            </td>
                                                                        </tr>
                                                                    </table>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                
                                                <!-- 底部 -->
                                                <tr>
                                                    <td style="background: linear-gradient(135deg, #1a1a1a 0%%, #2c2c2c 100%%); padding: 30px; text-align: center; border-top: 4px solid #d4af37;">
                                                        <div style="margin-bottom: 15px; font-size: 32px;">🏮</div>
                                                        <p style="color: #ffd700; font-size: 16px; margin: 0 0 15px 0; font-weight: bold; letter-spacing: 2px;">传承红色基因 · 培育时代新人</p>
                                                        <div style="width: 100px; height: 2px; background: linear-gradient(90deg, transparent, #d4af37, transparent); margin: 20px auto;"></div>
                                                        <p style="color: rgba(255, 255, 255, 0.7); font-size: 13px; margin: 5px 0; letter-spacing: 1px;">青少年爱国教育平台</p>
                                                        <p style="color: rgba(255, 255, 255, 0.6); font-size: 12px; margin: 5px 0;">邮箱: contact@patriotic-edu.com | 电话: 400-123-4567</p>
                                                        <p style="color: rgba(255, 255, 255, 0.5); font-size: 11px; margin: 15px 0 0 0;">© 2025 青少年爱国教育平台. All rights reserved.</p>
                                                        <div style="margin-top: 15px; font-size: 20px; opacity: 0.5;">🐉 ⚡ 🐉</div>
                                                    </td>
                                                </tr>
                
                                                <!-- 底部装饰带 -->
                                                <tr>
                                                    <td style="background: linear-gradient(90deg, #d4af37 0%%, #ffd700 50%%, #d4af37 100%%); height: 6px;"></td>
                                                </tr>
                
                                            </table>
                
                                            <!-- 额外提示 -->
                                            <table cellpadding="0" cellspacing="0" border="0" width="600" style="max-width: 600px; margin-top: 20px;">
                                                <tr>
                                                    <td style="text-align: center; color: rgba(255, 255, 255, 0.8); font-size: 12px; line-height: 1.6;">
                                                        <p style="margin: 5px 0;">此邮件由系统自动发送，请勿直接回复</p>
                                                        <p style="margin: 5px 0;">如有疑问，请联系客服：400-123-4567</p>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                """.formatted(code);
    }

    /**
     * 生成 6 位随机验证码
     */
    private String generateCode() {
        Random random = new Random();
        return String.valueOf(random.nextInt(900000) + 100000);
    }
}

