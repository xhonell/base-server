package com.xhonell.common.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * program: BaseServer
 * ClassName EmalProperties
 * description:
 * author: xhonell
 * create: 2025年10月17日23时52分
 * Version 1.0
 **/
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.mail")
public class EmailProperties {

    /**
     * 邮件服务器地址
     */
    private String host;

    /**
     * 端口号
     */
    private Integer port;

    /**
     * 发件人邮箱
     */
    private String username;

    /**
     * 授权码（不是邮箱密码）
     */
    private String password;

    /**
     * 协议
     */
    private String protocol;

    @Data
    public static class MailProperties {
        private Smtp smtp = new Smtp();

        @Data
        public static class Smtp {
            private Ssl ssl = new Ssl();

            @Data
            public static class Ssl {
                private boolean enable;
            }
        }
    }

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(this.getHost());
        mailSender.setPort(this.getPort());
        mailSender.setUsername(this.getUsername());
        mailSender.setPassword(this.getPassword());
        mailSender.setDefaultEncoding("UTF-8");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", this.getProtocol());
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");

        return mailSender;
    }
}