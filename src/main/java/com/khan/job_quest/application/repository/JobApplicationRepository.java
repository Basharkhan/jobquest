package com.khan.job_quest.application.repository;

import com.khan.job_quest.application.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByJobId(Long jobId);
    List<JobApplication> findByApplicantId(Long applicantId);
}
