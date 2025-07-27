# HDDS CH HAPPS Batch - 푸시 알림 배치 시스템

실시간 푸시 알림을 위한 Spring Batch 시스템입니다. Android는 FCM, iOS는 APNS를 통해 푸시를 발송합니다.

## 🚀 주요 기능

- **2단계 배치 시스템**: 데이터 적재 → 실제 발송
- **실시간 처리**: 10초마다 자동 실행
- **멀티 플랫폼 지원**: Android (FCM) + iOS (APNS)
- **완전한 추적**: 성공/실패 결과 기록

## 🏗️ 시스템 아키텍처

```
[외부 시스템] → [마스터/타겟 테이블] → [대기 테이블] → [실제 발송] → [결과 테이블]
     ↓              ↓                    ↓              ↓              ↓
   데이터 입력    Batch 1 (적재)        Batch 2 (발송)    FCM/APNS      성공/실패
```

## 📋 데이터베이스 설정

### Oracle Database 설정

1. **Docker로 Oracle DB 실행**
```bash
docker run --restart unless-stopped --name oracle -e ORACLE_PASSWORD=1234 -p 1521:1521 -d gvenzl/oracle-xe
```

2. **SC_PT 계정 생성 (필요시)**
```bash
# Oracle DB에 연결
docker exec -it oracle sqlplus system/1234@//localhost:1521/XE

# SC_PT 계정 생성 (이미 존재한다면 생략)
CREATE USER SC_PT IDENTIFIED BY 1234;
GRANT CONNECT, RESOURCE, DBA TO SC_PT;
GRANT CREATE SESSION TO SC_PT;
GRANT UNLIMITED TABLESPACE TO SC_PT;
```

3. **Spring Batch 메타데이터 테이블 생성**
```bash
# SC_PT 계정으로 연결
docker exec -it oracle sqlplus SC_PT/1234@//localhost:1521/XE

# Spring Batch 메타데이터 테이블 생성
@src/main/resources/sql/spring-batch-schema-oracle.sql
```

4. **푸시 관련 테이블 생성**
```bash
# 푸시 관련 테이블 생성
@src/main/resources/sql/init-tables.sql
```

5. **애플리케이션 설정**
- `application.properties`에 Oracle 연결 정보가 이미 설정되어 있습니다.
- 기본 연결 정보:
  - URL: `jdbc:oracle:thin:@localhost:1521:XE`
  - Username: `SC_PT`
  - Password: `1234`

## 🗄️ 데이터베이스 테이블

### Spring Batch 메타데이터 테이블 (SC_PT 스키마)
- **BATCH_JOB_INSTANCE**: 배치 작업 인스턴스 정보
- **BATCH_JOB_EXECUTION**: 배치 작업 실행 정보
- **BATCH_STEP_EXECUTION**: 배치 스텝 실행 정보
- **BATCH_JOB_EXECUTION_PARAMS**: 배치 작업 파라미터
- **BATCH_JOB_EXECUTION_CONTEXT**: 배치 작업 컨텍스트
- **BATCH_STEP_EXECUTION_CONTEXT**: 배치 스텝 컨텍스트

### 핵심 테이블 (SC_PT 스키마)
- **PUSH_MSG_MASTER**: 푸시 메시지 마스터 정보
- **PUSH_SEND_TARGET_INFO**: 푸시 발송 대상 정보
- **PUSH_SEND_STBY**: 푸시 발송 대기 정보
- **USER_DEVICE_INFO**: 사용자 디바이스 정보
- **PUSH_SUCCESS_RESULT**: 푸시 발송 성공 결과
- **PUSH_FAIL_RESULT**: 푸시 발송 실패 결과

## 🔧 배치 프로세스

### Batch 1: 데이터 적재 (10초마다)
1. **Reader**: `PRC_FLAG='P'`인 마스터 데이터 조회
2. **Processor**: 사번별 디바이스 정보 조회 및 대기 데이터 생성
3. **Writer**: `PUSH_SEND_STBY` 테이블에 데이터 삽입

### Batch 2: 실제 발송 (10초마다)
1. **Reader**: `PRC_FLAG='P'`인 대기 데이터 조회
2. **Processor**: OS 타입에 따라 FCM/APNS 발송
3. **Writer**: 성공/실패 결과 저장 및 처리 완료 표시

## 🚀 실행 방법

### 1. 환경 설정
```bash
# Oracle DB 실행 확인
docker ps | grep oracle

# 애플리케이션 빌드
./gradlew build
```

### 2. 애플리케이션 실행
```bash
./gradlew bootRun
```

### 3. 배치 수동 실행 (선택사항)
```bash
# 데이터 적재 배치
curl -X POST http://localhost:8080/api/batch/push-stby-insert

# 실제 발송 배치
curl -X POST http://localhost:8080/api/batch/push-send
```

## 📊 모니터링

### 로그 확인
```bash
# 애플리케이션 로그
tail -f logs/application.log

# 배치 실행 로그
grep "배치" logs/application.log
```

### 데이터베이스 확인
```sql
-- 대기 데이터 확인
SELECT COUNT(*) FROM SC_PT.PUSH_SEND_STBY WHERE PRC_FLAG = 'P';

-- 성공/실패 결과 확인
SELECT COUNT(*) FROM SC_PT.PUSH_SUCCESS_RESULT;
SELECT COUNT(*) FROM SC_PT.PUSH_FAIL_RESULT;

-- 배치 실행 상태 확인
SELECT JOB_NAME, STATUS, START_TIME, END_TIME 
FROM SC_PT.BATCH_JOB_EXECUTION 
ORDER BY START_TIME DESC;
```

## 🔑 설정 파일

### 필수 설정 파일
- `src/main/resources/firebase-service-account.json`: FCM 서비스 계정 키
- `src/main/resources/apns-certificate.p12`: APNS 인증서

### 설정 속성
- `fcm.project-id`: Firebase 프로젝트 ID
- `apns.certificate-password`: APNS 인증서 비밀번호
- `apns.environment`: APNS 환경 (sandbox/production)

## 🛠️ 개발 환경

- **Java**: 17+
- **Spring Boot**: 3.5.3
- **Spring Batch**: 5.x
- **Database**: Oracle 21c
- **Build Tool**: Gradle

## 📝 API 엔드포인트

- `POST /api/batch/push-stby-insert`: 데이터 적재 배치 수동 실행
- `POST /api/batch/push-send`: 실제 발송 배치 수동 실행

## 🔍 문제 해결

### 일반적인 문제
1. **Oracle 연결 실패**: Docker 컨테이너 상태 확인
2. **SC_PT 계정 권한 부족**: DBA 권한 확인
3. **Spring Batch 테이블 없음**: `spring-batch-schema-oracle.sql` 실행
4. **FCM 발송 실패**: Firebase 서비스 계정 키 확인
5. **APNS 발송 실패**: 인증서 파일 및 비밀번호 확인

### 로그 레벨 조정
```properties
# application.properties
logging.level.com.hyundai.happsbtch=DEBUG
logging.level.org.springframework.batch=DEBUG
```

## 📞 지원

문제가 발생하면 로그를 확인하고 필요한 정보를 수집하여 문의해주세요. 