package com.hyundai.happsbtch.entity;

import lombok.Data;
import javax.persistence.*;

@Entity
@Table(name = "PUSH_SEND_STBY", schema = "SC_HAPP")
@Data
public class PushSendStbyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "push_send_stby_seq_gen")
    @SequenceGenerator(
            name = "push_send_stby_seq_gen",
            sequenceName = "PUSH_SEND_STBY_SEQ",
            allocationSize = 1
    )
    @Column(name = "SEQ")
    private Long seq;

    @Column(name = "PUSH_MSG_SEQ", nullable = false)
    private Long pushMsgSeq;

    @Column(name = "TARGET_EMPID", length = 50, nullable = false)
    private String targetEmpid;

    @Column(name = "PUSH_SVR_TYPE", length = 20, nullable = false)
    private String pushSvrType;

//    @Column(name = "PUSH_SEND_TYPE", length = 1)
//    private String pushSendType;

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
