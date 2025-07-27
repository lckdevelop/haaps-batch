package com.hyundai.happsbtch.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;

@Entity
@Table(name = "PUSH_SEND_TARGET_INFO", schema = "SC_PT")
@Data
public class PushSendTargetInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEQ")
    private Long seq;

    @Column(name = "PUSH_MSG_SEQ", nullable = false)
    private Long pushMsgSeq;

    @Column(name = "TARGET_EMPID", length = 50, nullable = false)
    private String targetEmpid;

    @Column(name = "REG_DTM", length = 14)
    private String regDtm;

    @Column(name = "RGST_ID", length = 100)
    private String rgstId;

//    @Column(name = "REG_PRG_ID", length = 100)
//    private String regPrgId;
//
//    @Column(name = "CHG_DTM", length = 14)
//    private String chgDtm;
//
//    @Column(name = "CHG_ID", length = 100)
//    private String chgId;
//
//    @Column(name = "CHG_PRG_ID", length = 100)
//    private String chgPrgId;

}
