package com.hyundai.happsbtch.config;

import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.ApnsClientBuilder;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.InputStream;

@Slf4j
@Configuration
public class ApnsConfig {

    @Value("${apns.certificate-path}")
    private String certificatePath;

    @Value("${apns.certificate-password}")
    private String certificatePassword;

    @Value("${apns.environment}")
    private String environment;

    @Bean(destroyMethod = "close")
    public ApnsClient apnsClient() throws Exception {
        File certFile = new ClassPathResource(certificatePath.replace("classpath:", "")).getFile();
        ApnsClientBuilder builder = new ApnsClientBuilder()
                .setClientCredentials(certFile, certificatePassword);

        if ("production".equalsIgnoreCase(environment)) {
            builder.setApnsServer(ApnsClientBuilder.PRODUCTION_APNS_HOST);
        } else {
            builder.setApnsServer(ApnsClientBuilder.DEVELOPMENT_APNS_HOST);
        }

        ApnsClient client = builder.build();
        log.info("APNS(Pushy) 클라이언트 초기화 완료");
        return client;
    }
} 