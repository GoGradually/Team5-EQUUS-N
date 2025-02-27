package com.feedhanjum.back_end.core.repository;

import com.feedhanjum.back_end.core.domain.JobRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRecordRepository extends JpaRepository<JobRecord, JobRecord.JobName> {
}