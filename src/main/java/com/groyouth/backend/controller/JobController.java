package com.groyouth.backend.controller;

import com.groyouth.backend.dto.JobRequest;
import com.groyouth.backend.model.Job;
import com.groyouth.backend.repository.JobRepository;
import com.groyouth.backend.service.JobService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService){
        this.jobService= jobService;
    }

    @PostMapping("/{companyId}")
    public Job createJob(@PathVariable Long companyId, @RequestBody JobRequest request){
        return jobService.createJob(companyId, request);
    }
}
