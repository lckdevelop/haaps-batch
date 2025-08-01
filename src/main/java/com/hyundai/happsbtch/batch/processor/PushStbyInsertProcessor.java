package com.hyundai.happsbtch.batch.processor;

import com.hyundai.happsbtch.batch.reader.PushStbyInsertReader.PushStbyInsertDto;
import com.hyundai.happsbtch.entity.PushSendStbyEntity;
import com.hyundai.happsbtch.entity.UserDeviceInfo;
import com.hyundai.happsbtch.repository.UserDeviceInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PushStbyInsertProcessor implements ItemProcessor<PushStbyInsertDto, PushSendStbyEntity> {
    private final UserDeviceInfoRepository userDeviceInfoRepository;

    @Override
    public PushSendStbyEntity process(PushStbyInsertDto dto) {
        List<UserDeviceInfo> deviceList = userDeviceInfoRepository.findByEmpIdAndPushAgrYn(dto.getTarget().getId().getTargetEmpid(), "Y");
        if (deviceList.isEmpty()) {
            log.warn("[대기적재] 디바이스 정보 없음: {}", dto.getTarget().getId().getTargetEmpid());
            return null;
        }
        UserDeviceInfo device = deviceList.get(0);
        String osType = device.getTeOpsyGbcd();
        String pushSvrType = "AOS".equalsIgnoreCase(osType) ? "AOS" : ("IOS".equalsIgnoreCase(osType) ? "IOS" : "ETC");
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        PushSendStbyEntity stby = new PushSendStbyEntity();
        stby.setPushMsgSeq(dto.getMaster().getSeq());
        stby.setTargetEmpid(dto.getTarget().getId().getTargetEmpid());
        stby.setPushSvrType(pushSvrType);
        stby.setPrcFlag("P");
        stby.setRegDtm(now);
        stby.setRgstId("batch");
        return stby;
    }
} 