package com.hyundai.happsbtch.util;

import com.eatthepath.pushy.apns.util.ApnsPayloadBuilder;

public class ApnsPayloadBuilderImpl extends ApnsPayloadBuilder {

    public ApnsPayloadBuilderImpl() {
        super();
    }

    // 필수로 추상 메서드 구현
    @Override
    public String build() {
        // 기본 build 호출 (기본 최대 길이 제한 없음)
        return "";
    }

    @Override
    public String buildMdmPayload(String pushMagicValue) {
        return "";
    }

}
