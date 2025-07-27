package com.hyundai.happsbtch.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ApnsConfig {
    // 임시로 비활성화 (APNS 인증서 파일이 없어서)
    
    /*
    @Value("${apns.certificate-path}")
    private String certificatePath;
    
    @Value("${apns.certificate-password}")
    private String certificatePassword;
    
    @Value("${apns.environment}")
    private String environment;
    
    @Bean(destroyMethod = "close")
    public ApnsClient apnsClient() throws Exception {
        File certFile = new ClassPathResource(certificatePath.replace("classpath:", "")).getFile();
        ApnsClientBuilder builder = new ApnsClientBuilder().setClientCredentials(certFile, certificatePassword);
        
        if ("production".equalsIgnoreCase(environment)) {
            builder.setApnsServer(ApnsClientBuilder.PRODUCTION_APNS_HOST);
        } else {
            builder.setApnsServer(ApnsClientBuilder.DEVELOPMENT_APNS_HOST);
        }
        
        ApnsClient client = builder.build();
        log.info("APNS(Pushy) 클라이언트 초기화 완료");
        return client;
    }
    */
} 