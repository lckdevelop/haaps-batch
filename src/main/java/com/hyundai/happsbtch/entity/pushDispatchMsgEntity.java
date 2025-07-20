package com.hyundai.happsbtch.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Entity
@Data
@Table(name = "PUSH_DISPATCH_MSG", schema = "SC_PT")
public class pushDispatchMsgEntity {
    @Id
    @Column(name = "SEQ", nullable = false)
    private Long seq; // 순번 (PK)

    @Column(name = "CUID", length = 50)
    private String cuid; // 수신자 아이디

    @Column(name = "MESSAGE", length = 4000)
    private String message; // 푸시 메시지

    @Column(name = "REG_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDate; // 등록일자

    @Column(name = "SENDER_ID", length = 50)
    private String senderId; // 발송자 아이디

    @Column(name = "SENDER_USER", length = 50)
    private String senderUser; // 발송자 명

    @Column(name = "EXT", length = 50)
    private String ext; // 부가정보

    @Column(name = "PRIORITY", length = 1)
    private String priority; // 우선순위

    @Column(name = "SEND_YN", length = 1)
    private String sendYn; // 발송여부

    @Column(name = "SMS_YN", length = 1)
    private String smsYn; // SMS 부달처리 여부

    @Column(name = "SMS_SENDER_NUM", length = 20)
    private String smsSenderNum; // 부담 발신자 번호

    @Column(name = "SMS_RECEIVER_NUM", length = 50)
    private String smsReceiverNum; // 부담 수신자 번호

    @Column(name = "RESERVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reserveDate; // 예약 발송 일자

    @Column(name = "SENDER_USER_NAME", length = 50)
    private String senderUserName; // 발송자 이름

    @Column(name = "REQ_SEQ")
    private Long reqSeq; // 로그 마스터 순번

    @Column(name = "SMS_BILL_CODE", length = 50)
    private String smsBillCode; // 청구 코드

    @Column(name = "SMS_SEND_TYPE", length = 50)
    private String smsSendType; // 발송 타입

    @Column(name = "ETC1", length = 50)
    private String etc1; // 기타
}
