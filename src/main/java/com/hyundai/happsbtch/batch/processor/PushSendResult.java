package com.hyundai.happsbtch.batch.processor;

import com.hyundai.happsbtch.entity.PushSendStbyEntity;
import com.hyundai.happsbtch.entity.UserDeviceInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushSendResult {
    private PushSendStbyEntity stby;
    private UserDeviceInfo device;
    private boolean success;
    private String failCode;
    private String failMsg;
    private String appId; // PUSH_MSG_MASTER의 APP_ID 전달용

    public static PushSendResult success(PushSendStbyEntity stby, UserDeviceInfo device, String appId) {
        return PushSendResult.builder()
                .stby(stby)
                .device(device)
                .success(true)
                .appId(appId)
                .build();
    }
    public static PushSendResult fail(PushSendStbyEntity stby, UserDeviceInfo device, String failCode, String failMsg, String appId) {
        return PushSendResult.builder()
                .stby(stby)
                .device(device)
                .success(false)
                .failCode(failCode)
                .failMsg(failMsg)
                .appId(appId)
                .build();
    }
} 