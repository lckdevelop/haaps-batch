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
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PushSendResultWriter implements ItemWriter<PushSendResult> {
    private final PushSuccessResultRepository successResultRepository;
    private final PushFailResultRepository failResultRepository;
    private final PushSendStbyRepository stbyRepository;

    @Override
    public void write(List<? extends PushSendResult> items) {
        for (PushSendResult result : items) {
            PushSendStbyEntity stby = result.getStby();
            UserDeviceInfo device = result.getDevice();
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String appId = result.getAppId();
            
            if (result.isSuccess()) {
                PushSuccessResultEntity success = new PushSuccessResultEntity();
                success.setPushSendStbySeq(stby.getSeq());
                success.setPushMsgSeq(stby.getPushMsgSeq());
                success.setDeviceToken(device != null ? device.getDeviceToken() : null);
                success.setEmpId(stby.getTargetEmpid());
                success.setAppId(appId);
                success.setPushSvrType(stby.getPushSvrType());
                success.setSendStatusCode("S");
                success.setSentDtm(now);
                success.setRegDtm(now);
                success.setRgstId("batch");
                success.setRegPrgId("batch");
                success.setChgDtm(now);
                success.setChgId("batch");
                success.setChgPrgId("batch");
                successResultRepository.save(success);
            } else {
                PushFailResultEntity fail = new PushFailResultEntity();
                fail.setPushSendStbySeq(stby.getSeq());
                fail.setPushMsgSeq(stby.getPushMsgSeq());
                fail.setDeviceToken(device != null ? device.getDeviceToken() : null);
                fail.setEmpId(stby.getTargetEmpid());
                fail.setAppId(appId);
                fail.setPushSvrType(stby.getPushSvrType());
                fail.setPushSvrRstCode(result.getFailCode());
                fail.setPushSvrRstMsg(result.getFailMsg());
                fail.setRegDtm(now);
                fail.setRgstId("batch");
                fail.setRegPrgId("batch");
                fail.setChgDtm(now);
                fail.setChgId("batch");
                fail.setChgPrgId("batch");
                failResultRepository.save(fail);
            }
            
            stby.setPrcFlag("C");
            stbyRepository.save(stby);
        }
    }
} 