package com.hyundai.happsbtch.util;

import com.eatthepath.pushy.apns.util.ApnsPayloadBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class ApnsPayloadBuilderImpl extends ApnsPayloadBuilder {

    private String title;
    private String subtitle;
    private String body;
    private String pushMagicValue; // MDM용

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public ApnsPayloadBuilderImpl() {
        super();
    }

    public ApnsPayloadBuilderImpl setTitle(String title) {
        this.title = title;
        return this;
    }

    public ApnsPayloadBuilderImpl setSubtitle(String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public ApnsPayloadBuilderImpl setBody(String body) {
        this.body = body;
        return this;
    }

    public ApnsPayloadBuilderImpl setPushMagicValue(String pushMagicValue) {
        this.pushMagicValue = pushMagicValue;
        return this;
    }

    @Override
    public String build() {
        try {
            Map<String, Object> payload = new HashMap<>();
            Map<String, Object> aps = new HashMap<>();
            
            // APNS alert 구조 (title, subtitle, body 포함)
            Map<String, String> alert = new HashMap<>();
            if (title != null && !title.trim().isEmpty()) {
                alert.put("title", title);
            }
            if (subtitle != null && !subtitle.trim().isEmpty()) {
                alert.put("subtitle", subtitle);
            }
            if (body != null && !body.trim().isEmpty()) {
                alert.put("body", body);
            }
            
            // alert가 비어있지 않으면 설정
            if (!alert.isEmpty()) {
                aps.put("alert", alert);
            }
            
            aps.put("sound", "default");
            payload.put("aps", aps);
            return OBJECT_MAPPER.writeValueAsString(payload);
        } catch (Exception e) {
            throw new RuntimeException("Payload JSON 변환 실패", e);
        }
    }

    @Override
    public String buildMdmPayload(String pushMagicValue) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("mdm", pushMagicValue);
            return OBJECT_MAPPER.writeValueAsString(payload);
        } catch (Exception e) {
            throw new RuntimeException("MDM payload JSON 변환 실패", e);
        }
    }

    public static void main(String[] args) {
        ApnsPayloadBuilderImpl builder = new ApnsPayloadBuilderImpl()
                .setTitle("테스트 제목")
                .setSubtitle("테스트 부제목")
                .setBody("테스트 본문");

        System.out.println("일반 알림 payload:");
        System.out.println(builder.build());

        System.out.println("MDM payload:");
        System.out.println(builder.buildMdmPayload("pushMagicToken123"));
    }
}