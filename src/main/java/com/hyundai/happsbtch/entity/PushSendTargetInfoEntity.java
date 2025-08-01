package com.hyundai.happsbtch.entity;

import com.hyundai.happsbtch.entity.embed.PushMsgTargetId;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PUSH_SEND_TARGET_INFO", schema = "SC_HAPP")
@Data
public class PushSendTargetInfoEntity {
    @EmbeddedId
    private PushMsgTargetId id;

    @Column(name = "REG_DTM", length = 14)
    private String regDtm;

    @Column(name = "RGST_ID", length = 100)
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
