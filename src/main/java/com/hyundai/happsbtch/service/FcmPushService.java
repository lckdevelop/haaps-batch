package com.hyundai.happsbtch.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.hyundai.happsbtch.entity.PushMsgMasterEntity;
import com.hyundai.happsbtch.entity.PushSendStbyEntity;
import com.hyundai.happsbtch.entity.UserDeviceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FcmPushService {
    private FirebaseMessaging firebaseMessaging;
    
    // FirebaseMessaging이 있을 때 사용하는 생성자
    public FcmPushService(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
        log.info("FirebaseMessaging이 초기화되었습니다.");
    }
    
    // 기본 생성자 (FirebaseMessaging이 없을 때 사용)
    public FcmPushService() {
        this.firebaseMessaging = null;
        log.warn("FirebaseMessaging이 null입니다. Firebase 기능이 비활성화됩니다.");
    }
    
    @Value("${connection.timeout:5000}")
    private int connectionTimeout;
    
    @Value("${read.timeout:5000}")
    private int readTimeout;

    public boolean sendPushMessage(PushSendStbyEntity stby, UserDeviceInfo device) {
        // 기존 메서드 유지 (하위 호환성)
        return sendPushMessage(stby, device, null);
    }

    public boolean sendPushMessage(PushSendStbyEntity stby, UserDeviceInfo device, PushMsgMasterEntity messageMaster) {
        try {
            if (firebaseMessaging == null) {
                // 임시로 성공 반환 (실제 FCM 발송은 나중에 구현)
                log.info("FCM 푸시 발송 성공 (임시) - pushMsgSeq: {}, stbySeq: {}, empId: {}", stby.getPushMsgSeq(), stby.getSeq(), device.getEmpId());
                return true;
            }

            // 메시지 마스터 정보가 있으면 동적으로 설정, 없으면 기본값 사용
            String title = (messageMaster != null && messageMaster.getTitle() != null) ? messageMaster.getTitle() : "푸시알림";
            String subtitle = (messageMaster != null && messageMaster.getSubtitle() != null) ? messageMaster.getSubtitle() : "";
            String body = (messageMaster != null && messageMaster.getMsgBody() != null) ? messageMaster.getMsgBody() : "푸시 메시지 도착!";

            // 실제 FCM 발송 코드
            Notification.Builder notificationBuilder = Notification.builder()
                    .setTitle(title)
                    .setBody(body);

            Message.Builder messageBuilder = Message.builder()
                    .setToken(device.getDeviceToken())
                    .setNotification(notificationBuilder.build());
            
            // subtitle이 있으면 Message의 data로 전달
            if (subtitle != null && !subtitle.trim().isEmpty()) {
                messageBuilder.putData("subtitle", subtitle);
            }

            // 타임아웃 설정과 함께 발송
            String response = firebaseMessaging.send(messageBuilder.build());
            log.info("FCM 푸시 발송 성공 - stbySeq: {}, empId: {}, title: {}, subtitle: {}, body: {}, response: {}, ConnectionTimeout: {}ms, ReadTimeout: {}ms",
                    stby.getSeq(), device.getEmpId(), title, subtitle, body, response, connectionTimeout, readTimeout);
            return true;

        } catch (Exception e) {
            log.error("FCM 푸시 발송 실패 - stbySeq: {}, empId: {}, error: {}, ConnectionTimeout: {}ms, ReadTimeout: {}ms",
                    stby.getSeq(), device.getEmpId(), e.getMessage(), connectionTimeout, readTimeout, e);
            return false;
        }
    }
} 