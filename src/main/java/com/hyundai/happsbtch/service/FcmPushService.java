package com.hyundai.happsbtch.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.hyundai.happsbtch.dto.PushMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmPushService {

    private final FirebaseMessaging firebaseMessaging;

    public boolean sendPushMessage(PushMessageDto pushMessage) {
        try {
            Message.Builder messageBuilder = Message.builder()
                    .setToken(pushMessage.getDeviceToken())
                    .setNotification(Notification.builder()
                            .setTitle(pushMessage.getTitle())
                            .setBody(pushMessage.getBody())
                            .build());

            // 데이터 페이로드 추가
            if (pushMessage.getData() != null && !pushMessage.getData().isEmpty()) {
                for (Map.Entry<String, Object> entry : pushMessage.getData().entrySet()) {
                    messageBuilder.putData(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }

            String response = firebaseMessaging.send(messageBuilder.build());
            log.info("FCM 푸시 발송 성공 - MessageId: {}, Response: {}", pushMessage.getMessageId(), response);
            return true;

        } catch (FirebaseMessagingException e) {
            log.error("FCM 푸시 발송 실패 - MessageId: {}, Error: {}", pushMessage.getMessageId(), e.getMessage(), e);
            return false;
        }
    }
} 