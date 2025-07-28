package com.hyundai.happsbtch.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "PUSH_FAIL_RESULT", schema = "SC_PT")
public class PushFailResultEntity { // 푸시실패결과

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "push_fail_result_seq_gen")
    @SequenceGenerator(
            name = "push_fail_result_seq_gen",
            sequenceName = "PUSH_FAIL_RESULT_SEQ",
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

    @Column(name = "PUSH_SVR_RST_CODE", length = 50)
    private String pushSvrRstCode;

    @Column(name = "PUSH_SVR_RST_MSG", length = 200)
    private String pushSvrRstMsg;

    @Column(name = "REG_DTM", length = 14)
    private String regDtm;

    @Column(name = "RGST_ID", length = 50)
    private String rgstId;
}
