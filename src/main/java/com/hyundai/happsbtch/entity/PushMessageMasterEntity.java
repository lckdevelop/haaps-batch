package com.hyundai.happsbtch.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Data
@Table(name = "PUSH_MESSAGE_MASTER", schema = "SC_PT")
public class PushMessageMasterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MESSAGE_ID")
    private Long messageId; // 메시지 ID (PK)

    @Column(name = "TITLE", length = 200)
    private String title; // 타이틀

    @Column(name = "BODY", length = 4000)
    private String body; // 메시지 내용

    @Column(name = "STATUS", length = 20)
    private String status; // 상태 (PENDING, SENT, FAILED)

    @Column(name = "SCHEDULED_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime scheduledTime; // 예약 발송 시간

    @Column(name = "DATA", columnDefinition = "CLOB")
    private String data; // JSON 형태의 추가 데이터

    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt; // 생성일

    @Column(name = "UPDATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt; // 수정일
}
