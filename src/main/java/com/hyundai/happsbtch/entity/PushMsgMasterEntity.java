package com.hyundai.happsbtch.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "PUSH_MSG_MASTER", schema = "SC_HAPP")
public class PushMsgMasterEntity { // 푸시메시지마스터

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEQ")
    private Long seq;

    @Column(name = "SYS_TYPE_CODE", length = 50)
    private String sysTypeCode;

    @Column(name = "APP_ID", length = 100)
    private String appId;

    @Column(name = "TITLE", length = 200)
    private String title;

    @Column(name = "SUBTITLE", length = 200)
    private String subtitle;

    @Column(name = "MSG_BODY", length = 4000)
    private String msgBody;

    @Column(name = "RSV_YN", length = 1)
    private String rsvYn;

    @Column(name = "RSV_DTM", length = 14)
    private String rsvDtm;

    @Column(name = "PUSH_PRTY_VAL")
    private Integer pushPrtyVal;

    @Column(name = "PRC_FLAG", columnDefinition = "CHAR(1)")
    private String prcFlag; // 처리상태: 'P'(대기), 'C'(완료)

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

