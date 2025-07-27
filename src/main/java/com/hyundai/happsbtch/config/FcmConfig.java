package com.hyundai.happsbtch.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class FcmConfig {
    // 임시로 비활성화 (Firebase 서비스 계정 키 파일이 없어서)
    
    /*
    @Value("${fcm.service-account-key-path}")
    private String serviceAccountKeyPath;
    
    @Value("${fcm.project-id}")
    private String projectId;
    
    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        InputStream serviceAccount = new ClassPathResource(serviceAccountKeyPath.replace("classpath:", "")).getInputStream();
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setProjectId(projectId)
                .build();
        
        FirebaseApp app = FirebaseApp.initializeApp(options);
        log.info("Firebase 앱 초기화 완료 - ProjectId: {}", projectId);
        return app;
    }
    
    @Bean
    public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        FirebaseMessaging messaging = FirebaseMessaging.getInstance(firebaseApp);
        log.info("Firebase Cloud Messaging 초기화 완료");
        return messaging;
    }
    */
} 