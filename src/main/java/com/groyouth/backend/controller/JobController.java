package com.groyouth.backend.controller;

import com.groyouth.backend.dto.JobRequest;
import com.groyouth.backend.model.Job;
import com.groyouth.backend.repository.JobRepository;
import com.groyouth.backend.service.JobService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService){
        this.jobService= jobService;
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @PostMapping("/{companyId}")
    public Job createJob(@PathVariable Long companyId, @RequestBody JobRequest request){
        String recruiterEmail = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return jobService.createJob(companyId, recruiterEmail, request);
    }

    @GetMapping("/search")
    public List<Job> search(@RequestParam(required = false) String keyword,
                            @RequestParam(required = false) String location,
                            @RequestParam(required = false) Integer exp){
        return jobService.search(keyword,location,exp);
    }
}
