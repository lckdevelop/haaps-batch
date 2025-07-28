package com.hyundai.happsbtch.batch.processor;

import com.hyundai.happsbtch.entity.PushSendStbyEntity;
import com.hyundai.happsbtch.entity.UserDeviceInfo;
import com.hyundai.happsbtch.service.FcmPushService;
import com.hyundai.happsbtch.service.ApnsPushService;
import com.hyundai.happsbtch.repository.UserDeviceInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PushSendProcessor implements ItemProcessor<PushSendStbyEntity, PushSendResult> {
    private final UserDeviceInfoRepository userDeviceInfoRepository;
    private final FcmPushService fcmPushService;
    private final ApnsPushService apnsPushService;

    @Override
    public PushSendResult process(PushSendStbyEntity stby) {
        // 사번으로 디바이스 정보 조회
        List<UserDeviceInfo> deviceList = userDeviceInfoRepository.findByEmpId(stby.getTargetEmpid());
        if (deviceList.isEmpty()) {
            log.warn("사용자 디바이스 정보 없음: {}", stby.getTargetEmpid());
            return PushSendResult.fail(stby, null, "NO_DEVICE", "No device info");
        }
        // 여러 디바이스가 있을 경우 모두 발송 (여기선 첫 번째만 예시)
        UserDeviceInfo device = deviceList.get(0);
        boolean success;
        String failMsg = null;
        String failCode = null;
        if ("1".equalsIgnoreCase(device.getTeOpsyGbcd())) {
            success = fcmPushService.sendPushMessage(stby, device);
            if (!success) failMsg = "FCM_FAIL";
        } else if ("2".equalsIgnoreCase(device.getTeOpsyGbcd())) {
            success = apnsPushService.sendPushMessage(stby, device);
            if (!success) failMsg = "APNS_FAIL";
        } else {
            log.warn("지원하지 않는 OS: {}", device.getTeOpsyGbcd());
            return PushSendResult.fail(stby, device, "UNSUPPORTED_OS", "Not supported OS");
        }
        if (success) {
            return PushSendResult.success(stby, device);
        } else {
            return PushSendResult.fail(stby, device, failCode, failMsg);
        }
    }
} 