package com.groyouth.backend.controller;

import com.groyouth.backend.model.Application;
import com.groyouth.backend.model.ApplicationStatus;
import com.groyouth.backend.service.ApplicationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {
    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService){
        this.applicationService = applicationService;
    }

    @PostMapping("/{jobId}/{candidateId}")
    public Application apply(@PathVariable Long jobId,
                             @PathVariable Long candidateId){
        return applicationService.apply(jobId, candidateId);
    }

    @PutMapping("/{applicationId}/{status}")
    public Application updateStatus(@PathVariable Long applicationId,
                                    @PathVariable ApplicationStatus status){
        return applicationService.updateStatus(applicationId, status);
    }
}
