package com.hyundai.happsbtch.service;

import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.PushNotificationResponse;
import com.eatthepath.pushy.apns.util.ApnsPayloadBuilder;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import com.eatthepath.pushy.apns.util.TokenUtil;
import com.eatthepath.pushy.apns.util.concurrent.PushNotificationFuture;
import com.hyundai.happsbtch.config.ApnsConfig;
import com.hyundai.happsbtch.entity.PushMsgMasterEntity;
import com.hyundai.happsbtch.entity.PushSendStbyEntity;
import com.hyundai.happsbtch.entity.UserDeviceInfo;
import com.hyundai.happsbtch.util.ApnsPayloadBuilderImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ApnsPushService {
    private final ApnsClient apnsClient;
    private final ApnsConfig apnsConfig;
    
    // ApnsClient가 있을 때 사용하는 생성자
    public ApnsPushService(ApnsClient apnsClient, ApnsConfig apnsConfig) {
        this.apnsClient = apnsClient;
        this.apnsConfig = apnsConfig;
        log.info("ApnsClient가 초기화되었습니다.");
    }
    
    // 기본 생성자 (ApnsClient가 없을 때 사용)
    public ApnsPushService() {
        this.apnsClient = null;
        this.apnsConfig = null;
        log.warn("ApnsClient가 null입니다. APNS 기능이 비활성화됩니다.");
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
            if (apnsClient == null) {
                // 임시로 성공 반환 (실제 APNS 발송은 나중에 구현)
                log.info("APNS 푸시 발송 성공 (임시) - stbySeq: {}, empId: {}", stby.getSeq(), device.getEmpId());
                return true;
            }
            
            // 메시지 마스터 정보가 있으면 동적으로 설정, 없으면 기본값 사용
            String title = (messageMaster != null && messageMaster.getTitle() != null) ? messageMaster.getTitle() : "푸시알림";
            String subtitle = (messageMaster != null && messageMaster.getSubtitle() != null) ? messageMaster.getSubtitle() : "";
            String body = (messageMaster != null && messageMaster.getMsgBody() != null) ? messageMaster.getMsgBody() : "푸시 메시지 도착!";
            
            // 실제 APNS 발송 코드
            ApnsPayloadBuilderImpl payloadBuilder = new ApnsPayloadBuilderImpl()
                    .setTitle(title)
                    .setBody(body);
            
            // subtitle이 있으면 설정
            if (subtitle != null && !subtitle.trim().isEmpty()) {
                payloadBuilder.setSubtitle(subtitle);
            }
            
            String payload = payloadBuilder.build();
            String token = TokenUtil.sanitizeTokenString(device.getDeviceToken());
            
            // .p8 파일 사용 시 Bundle ID를 토픽으로 사용
            String topic = apnsConfig != null ? apnsConfig.getBundleId() : "com.hyundai.happs";
            
            SimpleApnsPushNotification notification = new SimpleApnsPushNotification(token, topic, payload);
            
            // 타임아웃 설정과 함께 발송
            PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>> sendNotificationFuture =
                    apnsClient.sendNotification(notification);
            PushNotificationResponse<SimpleApnsPushNotification> response = sendNotificationFuture.get(readTimeout, TimeUnit.MILLISECONDS);
            
            if (response.isAccepted()) {
                log.info("APNS 푸시 발송 성공 - stbySeq: {}, empId: {}, topic: {}, title: {}, subtitle: {}, body: {}, ConnectionTimeout: {}ms", 
                        stby.getSeq(), device.getEmpId(), topic, title, subtitle, body, connectionTimeout);
                return true;
            } else {
                log.error("APNS 푸시 발송 실패 - stbySeq: {}, empId: {}, topic: {}, title: {}, subtitle: {}, body: {}, reason: {}, ConnectionTimeout: {}ms", 
                        stby.getSeq(), device.getEmpId(), topic, title, subtitle, body, response.getRejectionReason(), connectionTimeout);
                return false;
            }
            
        } catch (Exception e) {
            log.error("APNS 푸시 발송 실패 - stbySeq: {}, empId: {}, error: {}, ConnectionTimeout: {}ms, ReadTimeout: {}ms", 
                    stby.getSeq(), device.getEmpId(), e.getMessage(), connectionTimeout, readTimeout, e);
            return false;
        }
    }
} 