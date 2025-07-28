package com.hyundai.happsbtch.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "PUSH_SUCCESS_RESULT", schema = "SC_PT")
public class PushSuccessResultEntity { // 푸시성공결과

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "push_success_result_seq_gen")
    @SequenceGenerator(
            name = "push_success_result_seq_gen",
            sequenceName = "PUSH_SUCCESS_RESULT_SEQ",
            allocationSize = 1 // 100으로 변경하는걸로 고려
    )
    @Column(name = "SEQ")
    private Long seq;

    @Column(name = "PUSH_SEND_STBY_SEQ")
    private Long pushSendStbySeq;

    @Column(name = "PUSH_MSG_SEQ")
    private Long pushMsgSeq;

    @Column(name = "DEVICE_TOKEN", length = 1000)
    private String deviceToken;

    @Column(name = "EMP_ID", length = 50)
    private String empId;

    @Column(name = "APP_ID", length = 100)
    private String appId;

    @Column(name = "PUSH_SVR_TYPE", length = 20)
    private String pushSvrType;

    @Column(name = "SEND_STATUS_CODE", columnDefinition = "CHAR(1)")
    private String sendStatusCode;

    @Column(name = "SENT_DTM", length = 14)
    private String sentDtm;

    @Column(name = "RCV_DTM", length = 14)
    private String rcvDtm;

    @Column(name = "READ_DTM", length = 14)
    private String readDtm;

    @Column(name = "REG_DTM", length = 14)
    private String regDtm;

    @Column(name = "RGST_ID", length = 50)
    private String rgstId;
}
