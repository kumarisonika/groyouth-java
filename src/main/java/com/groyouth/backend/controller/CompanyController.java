package com.groyouth.backend.controller;

import com.groyouth.backend.dto.CompanyRequest;
import com.groyouth.backend.model.Company;
import com.groyouth.backend.service.CompanyService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService){
        this.companyService= companyService;
    }

    @PostMapping("/{recruiterId}")
    public Company createCompany(@PathVariable Long recruiterId,
                                 @RequestBody CompanyRequest request){
        return companyService.createCompany(recruiterId, request);
    }

}
