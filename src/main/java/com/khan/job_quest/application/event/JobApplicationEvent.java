package com.khan.job_quest.application.event;

import com.khan.job_quest.application.entity.JobApplication;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

@Getter
public class JobApplicationEvent extends ApplicationEvent {
    private final JobApplication jobApplication;

    public JobApplicationEvent(Object source, JobApplication jobApplication) {
        super(source);
        this.jobApplication = jobApplication;
    }
}
