package com.hyundai.happsbtch.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "USER_DEVICE_INFO", schema = "SC_HAPP")
@IdClass(UserDeviceInfo.PK.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDeviceInfo {

    @Id
    @Column(name = "EMP_ID", nullable = false, length = 50)
    private String empId;

    @Id
    @Column(name = "DEVI_TOKN", nullable = false, length = 500)
    private String deviceToken;

    @Column(name = "TE_OPSY_GBCD", length = 1)
    private String teOpsyGbcd;

    @Column(name = "DEVICE_MODEL", length = 100)
    private String deviceModel;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Column(name = "PUSH_SVR_TYPE", nullable = false, length = 20)
    private String pushSvrType;

    @Column(name = "PUSH_AGR_YN", length = 1)
    private String pushAgrYn;

    @Column(name = "PUSH_OS_AUTH_STATUS", nullable = false, length = 20)
    private String pushOsAuthStatus;

    @Column(name = "APP_VER", length = 20)
    private String appVer;

    @Column(name = "SVR_ENV", nullable = false, length = 10)
    private String svrEnv = "REAL";

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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PK implements Serializable {
        private String empId;
        private String deviceToken;
    }
}
