package com.hyundai.happsbtch.service;

import com.hyundai.happsbtch.entity.PushSendStbyEntity;
import com.hyundai.happsbtch.entity.UserDeviceInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmPushService {
    // private final FirebaseMessaging firebaseMessaging; // 임시 주석

    public boolean sendPushMessage(PushSendStbyEntity stby, UserDeviceInfo device) {
        try {
            // 임시로 성공 반환 (실제 FCM 발송은 나중에 구현)
            log.info("FCM 푸시 발송 성공 (임시) - stbySeq: {}, empId: {}", stby.getSeq(), device.getEmpId());
            return true;
            
            /* 실제 FCM 발송 코드 (나중에 활성화)
            Message.Builder messageBuilder = Message.builder()
                    .setToken(device.getDeviceToken())
                    .setNotification(Notification.builder()
                            .setTitle("푸시알림")
                            .setBody("푸시 메시지 도착!")
                            .build());
            String response = firebaseMessaging.send(messageBuilder.build());
            log.info("FCM 푸시 발송 성공 - stbySeq: {}, empId: {}, response: {}", stby.getSeq(), device.getEmpId(), response);
            return true;
            */
        } catch (Exception e) {
            log.error("FCM 푸시 발송 실패 - stbySeq: {}, empId: {}, error: {}", stby.getSeq(), device.getEmpId(), e.getMessage(), e);
            return false;
        }
    }
} 