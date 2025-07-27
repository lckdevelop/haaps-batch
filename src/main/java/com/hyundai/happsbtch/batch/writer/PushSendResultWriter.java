package com.hyundai.happsbtch.batch.writer;

import com.hyundai.happsbtch.batch.processor.PushSendResult;
import com.hyundai.happsbtch.entity.PushFailResultEntity;
import com.hyundai.happsbtch.entity.PushSendStbyEntity;
import com.hyundai.happsbtch.entity.PushSuccessResultEntity;
import com.hyundai.happsbtch.entity.UserDeviceInfo;
import com.hyundai.happsbtch.repository.PushFailResultRepository;
import com.hyundai.happsbtch.repository.PushSendStbyRepository;
import com.hyundai.happsbtch.repository.PushSuccessResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class PushSendResultWriter implements ItemWriter<PushSendResult> {
    private final PushSuccessResultRepository successResultRepository;
    private final PushFailResultRepository failResultRepository;
    private final PushSendStbyRepository stbyRepository;

    @Override
    public void write(Chunk<? extends PushSendResult> items) {
        for (PushSendResult result : items) {
            PushSendStbyEntity stby = result.getStby();
            UserDeviceInfo device = result.getDevice();
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            if (result.isSuccess()) {
                PushSuccessResultEntity success = new PushSuccessResultEntity();
                success.setPushSendStbySeq(stby.getSeq());
                success.setPushMsgSeq(stby.getPushMsgSeq());
                success.setDeviceToken(device != null ? device.getDeviceToken() : null);
                success.setEmpId(stby.getTargetEmpid());
                success.setAppId(null); // 필요시 채우기
                success.setPushSvrType(stby.getPushSvrType());
                success.setSendStatusCode("S");
                success.setSentDtm(now);
                success.setRegDtm(now);
                success.setRgstId("batch");
                successResultRepository.save(success);
                log.info("성공 결과 저장: {}", success);
            } else {
                PushFailResultEntity fail = new PushFailResultEntity();
                fail.setPushSendStbySeq(stby.getSeq());
                fail.setPushMsgSeq(stby.getPushMsgSeq());
                fail.setDeviceToken(device != null ? device.getDeviceToken() : null);
                fail.setEmpId(stby.getTargetEmpid());
                fail.setAppId(null); // 필요시 채우기
                fail.setPushSvrType(stby.getPushSvrType());
                fail.setPushSvrRstCode(result.getFailCode());
                fail.setPushSvrRstMsg(result.getFailMsg());
                fail.setRegDtm(now);
                fail.setRgstId("batch");
                failResultRepository.save(fail);
                log.info("실패 결과 저장: {}", fail);
            }
            // 대기 테이블 상태값 변경
            stby.setPrcFlag("C");
            stbyRepository.save(stby);
        }
    }
} 