package com.groyouth.backend.service;

import com.groyouth.backend.dto.CompanyRequest;
import com.groyouth.backend.model.Company;
import com.groyouth.backend.model.User;
import com.groyouth.backend.repository.CompanyRepository;
import com.groyouth.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository){
        this.companyRepository= companyRepository;
        this.userRepository= userRepository;
    }

     public Company createCompany(Long recruiterId, CompanyRequest request){
         User recruiter = userRepository.findById(recruiterId).orElseThrow(() -> new RuntimeException("Recruiter not found"));
         Company company= new Company();
         company.setName(request.name);
         company.setDescription(request.description);
         company.setRecruiter(recruiter);
         return companyRepository.save(company);
     }
}
