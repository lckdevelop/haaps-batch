package com.hyundai.happsbtch.config;

import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.ApnsClientBuilder;
import com.eatthepath.pushy.apns.auth.ApnsSigningKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Duration;

@Slf4j
@Configuration
public class ApnsConfig {
    
    @Value("${apns.key-path}")
    private String keyPath;
    
    @Value("${apns.environment}")
    private String environment;
    
    @Value("${apns.team-id:23693X6DFL}")
    private String teamId;
    
    @Value("${apns.key-id:854L67FYCT}")
    private String keyId;
    
    @Value("${apns.bundle-id:com.hyundai.happs}")
    private String bundleId;
    
    @Value("${connection.timeout:5000}")
    private int connectionTimeout;
    
    @Value("${read.timeout:5000}")
    private int readTimeout;
    
    @Bean(destroyMethod = "close")
    public ApnsClient apnsClient() throws Exception {
        try {
            Resource keyResource = getKeyResource();
            
            if (!keyResource.exists()) {
                log.warn("APNS 푸시 키 파일이 존재하지 않습니다: {}. APNS 기능을 비활성화합니다.", keyPath);
                return null;
            }
            
            // .p8 파일에서 PrivateKey 읽기
            PrivateKey privateKey = loadPrivateKey(keyResource);
            
            // ECPrivateKey로 캐스팅
            if (!(privateKey instanceof ECPrivateKey)) {
                throw new IllegalArgumentException("APNS 푸시 키는 EC(타원곡선) 키여야 합니다.");
            }
            ECPrivateKey ecPrivateKey = (ECPrivateKey) privateKey;
            
            // ApnsSigningKey 생성
            ApnsSigningKey signingKey = new ApnsSigningKey(keyId, teamId, ecPrivateKey);
            
            ApnsClientBuilder builder = new ApnsClientBuilder()
                    .setSigningKey(signingKey)
                    .setConnectionTimeout(Duration.ofMillis(connectionTimeout));
            
            if ("production".equalsIgnoreCase(environment)) {
                builder.setApnsServer(ApnsClientBuilder.PRODUCTION_APNS_HOST);
            } else {
                builder.setApnsServer(ApnsClientBuilder.DEVELOPMENT_APNS_HOST);
            }
            
            ApnsClient client = builder.build();
            log.info("APNS(Pushy) 클라이언트 초기화 완료 - Environment: {}, TeamId: {}, KeyId: {}, BundleId: {}, ConnectionTimeout: {}ms", 
                    environment, teamId, keyId, bundleId, connectionTimeout);
            return client;
        } catch (Exception e) {
            log.warn("APNS 클라이언트 초기화 실패 - APNS 기능을 비활성화합니다: {}", e.getMessage());
            return null;
        }
    }
    
    private Resource getKeyResource() {
        // classpath:로 시작하면 프로젝트 내 파일, 그렇지 않으면 서버 파일 시스템 경로
        if (keyPath.startsWith("classpath:")) {
            return new ClassPathResource(keyPath.replace("classpath:", ""));
        } else {
            return new FileSystemResource(keyPath);
        }
    }
    
    private PrivateKey loadPrivateKey(Resource keyResource) throws Exception {
        try (InputStream keyStream = keyResource.getInputStream()) {
            // Java 8 호환 코드
            byte[] keyBytes = new byte[8192];
            int bytesRead;
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            
            while ((bytesRead = keyStream.read(keyBytes)) != -1) {
                baos.write(keyBytes, 0, bytesRead);
            }
            
            byte[] allBytes = baos.toByteArray();
            
            // PKCS8 형식의 .p8 파일 파싱
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(allBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            
            return keyFactory.generatePrivate(keySpec);
        }
    }
    
    // Bundle ID getter (다른 빈에서 사용)
    public String getBundleId() {
        return bundleId;
    }
} 