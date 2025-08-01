package com.hyundai.happsbtch.repository;

import com.hyundai.happsbtch.entity.UserDeviceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDeviceInfoRepository extends JpaRepository<UserDeviceInfo, String> {
    List<UserDeviceInfo> findByEmpIdAndPushAgrYn(String empId, String pushAgrYn);
}