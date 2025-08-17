package com.hyundai.happsbtch.repository;

import com.hyundai.happsbtch.entity.UserDeviceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDeviceInfoRepository extends JpaRepository<UserDeviceInfo, String> {
    // Native Query로 캐시 우회
    @Query(value = "SELECT * FROM USER_DEVICE_INFO u WHERE u.EMP_ID = :empId AND u.PUSH_AGR_YN = :pushAgrYn", nativeQuery = true)
    List<UserDeviceInfo> findByEmpIdAndPushAgrYnNative(@Param("empId") String empId, @Param("pushAgrYn") String pushAgrYn);
    
    // 기존 메서드 유지 (하위 호환성)
    List<UserDeviceInfo> findByEmpIdAndPushAgrYn(String empId, String pushAgrYn);
}