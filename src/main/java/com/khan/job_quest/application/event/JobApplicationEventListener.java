package com.khan.job_quest.application.event;

import com.khan.job_quest.notofication.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobApplicationEventListener {
    private final EmailService emailService;

    @EventListener
    public void handleJobApplication(JobApplicationEvent event) {
        var application = event.getJobApplication();
        var employerEmail = application.getJob().getEmployer().getEmail();

        String subject = "New Job Application for " + application.getJob().getTitle();
        String body = "Hello,\n\nYou have a new application from " +
                application.getApplicant().getName() +
                " for your job post: " + application.getJob().getTitle() + ".\n\n" +
                "Cover Letter:\n" + application.getCoverLetter() + "\n\n" +
                "Regards,\nJob Portal";

        emailService.sendEmail(employerEmail, subject, body);
    }
}
