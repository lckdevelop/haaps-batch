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
        // 사번으로 디바이스 정보 조회 (여러개면 첫번째)
        List<UserDeviceInfo> deviceList = userDeviceInfoRepository.findByEmpId(dto.getTarget().getTargetEmpid());
        if (deviceList.isEmpty()) {
            log.warn("[대기적재] 디바이스 정보 없음: {}", dto.getTarget().getTargetEmpid());
            return null;
        }
        UserDeviceInfo device = deviceList.get(0);
        String osType = device.getTeOpsyGbcd();
        String pushSvrType = "AOS".equalsIgnoreCase(osType) ? "AOS" : ("IOS".equalsIgnoreCase(osType) ? "IOS" : "ETC");
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        PushSendStbyEntity stby = new PushSendStbyEntity();
        stby.setPushMsgSeq(dto.getMaster().getSeq());
        stby.setTargetEmpid(dto.getTarget().getTargetEmpid());
        stby.setPushSvrType(pushSvrType);
        stby.setPrcFlag("P");
        stby.setRegDtm(now);
        stby.setRgstId("batch");
        return stby;
    }
} 