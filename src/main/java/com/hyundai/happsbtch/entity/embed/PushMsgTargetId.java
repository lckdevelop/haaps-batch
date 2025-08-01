package com.hyundai.happsbtch.entity.embed;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
public class PushMsgTargetId implements Serializable {

    private Long pushMsgSeq;
    private String targetEmpid;

    public PushMsgTargetId() {}

    public PushMsgTargetId(Long pushMsgSeq, String targetEmpid) {
        this.pushMsgSeq = pushMsgSeq;
        this.targetEmpid = targetEmpid;
    }

    // equals & hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PushMsgTargetId)) return false;
        PushMsgTargetId that = (PushMsgTargetId) o;
        return Objects.equals(pushMsgSeq, that.pushMsgSeq) &&
                Objects.equals(targetEmpid, that.targetEmpid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pushMsgSeq, targetEmpid);
    }

}
