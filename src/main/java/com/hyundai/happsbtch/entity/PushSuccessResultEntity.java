package com.hyundai.happsbtch.entity;

import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(name = "PUSH_SUCCESS_RESULT", schema = "SC_HAPP")
public class PushSuccessResultEntity { // 푸시성공결과

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "push_success_result_seq_gen")
    @SequenceGenerator(
            name = "push_success_result_seq_gen",
            sequenceName = "PUSH_SUCCESS_RESULT_SEQ",
            allocationSize = 1
    )
    @Column(name = "PUSH_SEND_STBY_SEQ")
    private Long pushSendStbySeq;

    @Column(name = "PUSH_MSG_SEQ")
    private Long pushMsgSeq;

    @Column(name = "DEVI_TOKN", length = 1000)
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

    @Column(name = "REG_PRG_ID", length = 100)
    private String regPrgId;

    @Column(name = "CHG_DTM", length = 14)
    private String chgDtm;

    @Column(name = "CHG_ID", length = 100)
    private String chgId;

    @Column(name = "CHG_PRG_ID", length = 100)
    private String chgPrgId;
}
