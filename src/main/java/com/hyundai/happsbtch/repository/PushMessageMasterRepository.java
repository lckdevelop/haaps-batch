package com.hyundai.happsbtch.repository;

import com.hyundai.happsbtch.entity.PushMessageMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PushMessageMasterRepository extends JpaRepository<PushMessageMasterEntity, Long> {

    @Query("SELECT p FROM PushMessageMasterEntity p WHERE p.status = :status AND p.scheduledTime <= :currentTime")
    List<PushMessageMasterEntity> findByStatusAndScheduledTimeBefore(
            @Param("status") String status, 
            @Param("currentTime") LocalDateTime currentTime);

    List<PushMessageMasterEntity> findByStatus(String status);
} 