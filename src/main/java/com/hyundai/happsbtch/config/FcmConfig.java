package com.hyundai.happsbtch.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Configuration
public class FcmConfig {
    
    @Value("${fcm.service-account-key-path}")
    private String serviceAccountKeyPath;
    
    @Value("${fcm.project-id}")
    private String projectId;
    
    @Value("${connection.timeout:5000}")
    private int connectionTimeout;
    
    @Value("${read.timeout:5000}")
    private int readTimeout;
    
    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        try {
            Resource serviceAccountResource = getServiceAccountResource();
            
            if (!serviceAccountResource.exists()) {
                log.warn("Firebase 서비스 계정 키 파일이 존재하지 않습니다: {}. Firebase 기능을 비활성화합니다.", serviceAccountKeyPath);
                return null;
            }
            
            InputStream serviceAccount = serviceAccountResource.getInputStream();
            
            // 파일이 비어있는지 확인
            if (serviceAccount.available() == 0) {
                log.warn("Firebase 서비스 계정 키 파일이 비어있습니다. Firebase 기능을 비활성화합니다.");
                return null;
            }
            
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setProjectId(projectId)
                    .build();

            FirebaseApp app = FirebaseApp.initializeApp(options);
            log.info("Firebase 앱 초기화 완료 - ProjectId: {}, ServiceAccountPath: {}", projectId, serviceAccountKeyPath);
            return app;
        } catch (Exception e) {
            log.warn("Firebase 앱 초기화 실패 - Firebase 기능을 비활성화합니다: {}", e.getMessage());
            return null;
        }
    }

    @Bean
    public FirebaseMessaging firebaseMessaging() {
        try {
            FirebaseApp firebaseApp = firebaseApp();
            if (firebaseApp == null) {
                log.warn("FirebaseApp이 초기화되지 않았습니다. Firebase 기능을 비활성화합니다.");
                return null;
            }
            return FirebaseMessaging.getInstance(firebaseApp);
        } catch (Exception e) {
            log.warn("FirebaseMessaging 빈 생성 실패 - Firebase 기능을 비활성화합니다: {}", e.getMessage());
            return null;
        }
    }
    
    private Resource getServiceAccountResource() {
        // classpath:로 시작하면 프로젝트 내 파일, 그렇지 않으면 서버 파일 시스템 경로
        if (serviceAccountKeyPath.startsWith("classpath:")) {
            return new ClassPathResource(serviceAccountKeyPath.replace("classpath:", ""));
        } else {
            return new FileSystemResource(serviceAccountKeyPath);
        }
    }

} 