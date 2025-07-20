package com.hyundai.happsbtch.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "PUSH_MESSAGE_TARGET", schema = "SC_PT")
public class PushMessageTargetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TARGET_ID")
    private Long targetId; // 타겟 ID (PK)

    @Column(name = "MESSAGE_ID")
    private Long messageId; // 메시지 ID (FK)

    @Column(name = "PLATFORM", length = 10)
    private String platform; // 플랫폼 (AOS, IOS)

    @Column(name = "DEVICE_TOKEN", length = 500)
    private String deviceToken; // 디바이스 토큰

    @Column(name = "USER_ID", length = 50)
    private String userId; // 사용자 ID

    @Column(name = "STATUS", length = 20)
    private String status; // 상태 (PENDING, SENT, FAILED)

    @Column(name = "RETRY_COUNT")
    private Integer retryCount; // 재시도 횟수

    @Column(name = "SENT_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime sentAt; // 발송 완료 시간

    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt; // 생성일

    @Column(name = "UPDATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt; // 수정일
}