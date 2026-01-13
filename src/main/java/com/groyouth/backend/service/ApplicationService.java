package com.groyouth.backend.service;

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

    public ApplicationService(ApplicationRepository applicationRepository,
                              JobRepository jobRepository, UserRepository userRepository){
        this.applicationRepository= applicationRepository;
        this.jobRepository= jobRepository;
        this.userRepository= userRepository;
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
        return applicationRepository.save(app);
    }

    public Application updateStatus(Long applicationId, ApplicationStatus status){
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(()-> new RuntimeException("Application not found"));

        app.setStatus(status);
        return applicationRepository.save(app);
    }


}
