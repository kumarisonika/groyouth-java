package com.groyouth.backend.service;

import com.groyouth.backend.event.JobAppliedEvent;
import com.groyouth.backend.kafka.JobEventProducer;
import com.groyouth.backend.model.Application;
import com.groyouth.backend.model.ApplicationStatus;
import com.groyouth.backend.model.Job;
import com.groyouth.backend.model.User;
import com.groyouth.backend.repository.ApplicationRepository;
import com.groyouth.backend.repository.JobRepository;
import com.groyouth.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final JobEventProducer jobEventProducer;

    public ApplicationService(ApplicationRepository applicationRepository,
                              JobRepository jobRepository, UserRepository userRepository, JobEventProducer jobEventProducer){
        this.applicationRepository= applicationRepository;
        this.jobRepository= jobRepository;
        this.userRepository= userRepository;
        this.jobEventProducer= jobEventProducer;
    }

    public Application apply(Long jobId, Long candidateId){
        Job job = jobRepository.findById(jobId).orElseThrow(()->
                new RuntimeException("Job not found"));

        User candidate = userRepository.findById(candidateId).orElseThrow(()->
                new RuntimeException("Candidate not found"));

        Application app = new Application();
        app.setJob(job);
        app.setCandidate(candidate);
        app.setStatus(ApplicationStatus.APPLIED);
        app.setAppliedAt(LocalDateTime.now());
        applicationRepository.save(app);

        JobAppliedEvent event = new JobAppliedEvent();
        event.jobId = job.getId();
        event.candidateId = candidate.getId();
        event.candidateEmail = candidate.getEmail();
        jobEventProducer.publishJobApplied(event);

        return app;
    }

    public Application updateStatus(Long applicationId, ApplicationStatus status){
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(()-> new RuntimeException("Application not found"));

        app.setStatus(status);
        return applicationRepository.save(app);
    }


}
