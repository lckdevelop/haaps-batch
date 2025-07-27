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

    public static PushSendResult success(PushSendStbyEntity stby, UserDeviceInfo device) {
        return PushSendResult.builder()
                .stby(stby)
                .device(device)
                .success(true)
                .build();
    }
    public static PushSendResult fail(PushSendStbyEntity stby, UserDeviceInfo device, String failCode, String failMsg) {
        return PushSendResult.builder()
                .stby(stby)
                .device(device)
                .success(false)
                .failCode(failCode)
                .failMsg(failMsg)
                .build();
    }
} 