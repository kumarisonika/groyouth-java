package com.groyouth.backend.controller;

import com.groyouth.backend.dto.CompanyRequest;
import com.groyouth.backend.model.Company;
import com.groyouth.backend.service.CompanyService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService){
        this.companyService= companyService;
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @PostMapping
    public Company createCompany(@RequestBody CompanyRequest request){
        String recruiterEmail =
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName();
        return companyService.createCompany(recruiterEmail, request);
    }

}
