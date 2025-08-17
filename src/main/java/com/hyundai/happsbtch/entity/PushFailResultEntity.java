package com.hyundai.happsbtch.entity;

import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(name = "PUSH_FAIL_RESULT", schema = "SC_HAPP")
public class PushFailResultEntity { // 푸시실패결과

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "push_fail_result_seq_gen")
    @SequenceGenerator(
            name = "push_fail_result_seq_gen",
            sequenceName = "PUSH_FAIL_RESULT_SEQ",
            allocationSize = 1
    )
    @Column(name = "PUSH_MSG_SEQ")
    private Long pushMsgSeq;

    @Column(name = "PUSH_SEND_STBY_SEQ")
    private Long pushSendStbySeq;

    @Column(name = "DEVI_TOKN", length = 1000)
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
