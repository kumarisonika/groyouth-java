package com.groyouth.backend.controller;

import com.groyouth.backend.model.Application;
import com.groyouth.backend.model.ApplicationStatus;
import com.groyouth.backend.service.ApplicationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {
    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService){
        this.applicationService = applicationService;
    }

    @PreAuthorize("hasRole('CANDIDATE')")
    @PostMapping("/{jobId}")
    public Application apply(@PathVariable Long jobId){
        String candidateEmail =
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName();
        return applicationService.apply(jobId, candidateEmail);
    }

    @PreAuthorize("hasRole('CANDIDATE')")
    @PutMapping("/{applicationId}/{status}")
    public Application updateStatus(@PathVariable Long applicationId,
                                    @PathVariable ApplicationStatus status){
        String recruiterEmail =
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName();
        return applicationService.updateStatus(applicationId, status, recruiterEmail);
    }
}
