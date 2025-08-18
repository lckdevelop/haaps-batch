package com.hyundai.happsbtch.batch.processor;

import com.hyundai.happsbtch.batch.reader.PushStbyInsertReader.PushStbyInsertDto;
import com.hyundai.happsbtch.entity.PushSendStbyEntity;
import com.hyundai.happsbtch.entity.UserDeviceInfo;
import com.hyundai.happsbtch.repository.PushMsgMasterRepository;
import com.hyundai.happsbtch.repository.PushSendStbyRepository;
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
    private final PushSendStbyRepository pushSendStbyRepository;
    private final PushMsgMasterRepository pushMsgMasterRepository;

    @Override
    public PushSendStbyEntity process(PushStbyInsertDto dto) {
        // 중복 체크: 이미 push_send_stby에 존재하는지 확인
        if (isDuplicateStby(dto.getMaster().getSeq(), dto.getTarget().getId().getTargetEmpid())) {
            log.info("[대기적재] 중복 데이터 스킵 - PUSH_MSG_SEQ: {}, EMP_ID: {}", 
                    dto.getMaster().getSeq(), dto.getTarget().getId().getTargetEmpid());
            return null;
        }
        
        // Native Query를 사용하여 Hibernate 캐시 우회
        List<UserDeviceInfo> deviceList = userDeviceInfoRepository.findByEmpIdAndPushAgrYnNative(dto.getTarget().getId().getTargetEmpid(), "Y");
        if (deviceList.isEmpty()) {
            log.warn("[대기적재] 디바이스 정보 없음 OR 푸시 알림 미동의: {}", dto.getTarget().getId().getTargetEmpid());
            return null;
        }
        
        // PUSH_MSG_MASTER의 prc_flag를 'C'로 업데이트 (처리 완료)
        try {
            pushMsgMasterRepository.updatePrcFlag(dto.getMaster().getSeq());
        } catch (Exception e) {
            log.error("[대기적재] PUSH_MSG_MASTER prc_flag 업데이트 중 오류 - SEQ: {}", dto.getMaster().getSeq(), e);
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
        stby.setRegPrgId("batch");
        stby.setChgDtm(now);
        stby.setChgId("batch");
        stby.setChgPrgId("batch");
        
        return stby;
    }
    
    /**
     * 중복 체크: 이미 push_send_stby에 존재하는지 확인
     */
    private boolean isDuplicateStby(Long pushMsgSeq, String empId) {
        try {
            List<PushSendStbyEntity> existing = pushSendStbyRepository.findByPushMsgSeqAndTargetEmpidNative(pushMsgSeq, empId);
            return !existing.isEmpty();
        } catch (Exception e) {
            log.warn("[대기적재] 중복 체크 중 오류 발생 - PUSH_MSG_SEQ: {}, EMP_ID: {}", pushMsgSeq, empId, e);
            return false;
        }
    }
} 