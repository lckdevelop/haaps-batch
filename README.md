# HDDS CH HAPPS 배치 애플리케이션

현대자동차 푸시 메시지 배치 발송 시스템입니다. AOS는 FCM(Firebase Cloud Messaging)을, iOS는 APNS(Apple Push Notification Service)를 통해 푸시 메시지를 발송합니다.

## 주요 기능

- **플랫폼별 푸시 발송**: AOS(FCM), iOS(APNS) 지원
- **스케줄링**: 크론탭 기반 자동 배치 실행 (5초마다)
- **수동 실행**: REST API를 통한 배치 작업 수동 실행
- **모니터링**: 상세한 로깅 및 상태 추적

## 기술 스택

- **Spring Boot 3.5.3**
- **Spring Batch**
- **Spring Data JPA**
- **Firebase Admin SDK** (FCM)
- **APNS Java Library** (APNS)
- **Oracle Database**
- **Redis**

## 프로젝트 구조

```
src/main/java/com/hyundai/happsbtch/
├── batch/
│   ├── PushBatch.java                      # 메인 배치 설정 (processPushMessageJob)
│   ├── reader/
│   │   └── PushMessageReader.java          # 푸시 메시지 Reader
│   ├── processor/
│   │   └── PushMessageProcessor.java       # 푸시 메시지 처리
│   └── writer/
│       └── PushMessageWriter.java          # 발송 결과 업데이트
├── config/
│   ├── FcmConfig.java                      # FCM 설정
│   └── ApnsConfig.java                     # APNS 설정
├── controller/
│   └── PushBatchController.java            # 배치 작업 수동 실행 API
├── dto/
│   └── PushMessageDto.java                 # 푸시 메시지 DTO
├── entity/
│   ├── PushMessageMasterEntity.java        # 푸시 메시지 마스터 엔티티
│   └── PushMessageTargetEntity.java        # 푸시 메시지 타겟 엔티티
├── repository/
│   ├── PushMessageMasterRepository.java    # 마스터 Repository
│   └── PushMessageTargetRepository.java    # 타겟 Repository
├── schedule/
│   └── PushMessageScheduler.java           # 크론탭 스케줄러 (5초마다)
└── service/
    ├── FcmPushService.java                 # FCM 푸시 발송 서비스
    └── ApnsPushService.java                # APNS 푸시 발송 서비스
```

## 설정

### 1. application.properties 설정

```properties
# FCM 설정
fcm.service-account-key-path=classpath:firebase-service-account.json
fcm.project-id=your-firebase-project-id

# APNS 설정
apns.certificate-path=classpath:apns-certificate.p12
apns.certificate-password=your-certificate-password
apns.environment=sandbox

# 배치 설정
batch.push.chunk-size=100
```

### 2. 인증 파일 설정

#### FCM 설정
- `src/main/resources/firebase-service-account.json` 파일을 Firebase Console에서 다운로드하여 추가

#### APNS 설정
- `src/main/resources/apns-certificate.p12` 파일을 Apple Developer Console에서 다운로드하여 추가

## 스케줄링

### 자동 실행 스케줄

**푸시 메시지 배치**: 5초마다 실행
```cron
*/5 * * * * *
```

### 수동 실행 API

#### 푸시 메시지 배치 실행
```bash
POST /api/batch/push-message
```

## 배치 작업 흐름

### 푸시 메시지 배치 (processPushMessageJob)
1. **Start Step**: 배치 작업 시작 로그
2. **Process Step**: 
   - **Reader**: 발송 대기 중인 푸시 메시지 조회
   - **Processor**: 플랫폼별 푸시 발송 (FCM/APNS)
   - **Writer**: 발송 결과 상태 업데이트
3. **End Step**: 배치 작업 완료 로그

## 메시지 상태

- **PENDING**: 발송 대기
- **SENT**: 발송 성공
- **FAILED**: 발송 실패

## 로깅

모든 배치 작업은 상세한 로그를 남깁니다:
- 배치 작업 시작/완료
- 푸시 발송 성공/실패
- 오류 발생 시 상세 정보

## 실행 방법

```bash
# 애플리케이션 실행
./gradlew bootRun

# 또는 JAR 파일로 실행
./gradlew build
java -jar build/libs/hdds-ch-happs-btch-0.0.1-SNAPSHOT.jar
```

## 주의사항

1. **인증 파일**: FCM과 APNS 인증 파일이 올바르게 설정되어야 합니다.
2. **데이터베이스**: Oracle Database 연결이 필요합니다.
3. **네트워크**: FCM과 APNS 서버에 대한 네트워크 접근이 필요합니다.
4. **모니터링**: 배치 작업 실행 상태를 정기적으로 확인해야 합니다.
5. **성능**: 5초마다 실행되므로 시스템 리소스 사용량을 모니터링해야 합니다. 