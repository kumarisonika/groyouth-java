package com.groyouth.backend.service;

import com.groyouth.backend.dto.JobRequest;
import com.groyouth.backend.model.Company;
import com.groyouth.backend.model.Job;
import com.groyouth.backend.repository.CompanyRepository;
import com.groyouth.backend.repository.JobRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;

    public JobService(JobRepository jobRepository, CompanyRepository companyRepository){
        this.jobRepository= jobRepository;
        this.companyRepository= companyRepository;
    }

    public Job createJob(Long companyId,String recruiterEmail, JobRequest request){

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        Job job = new Job();
        job.setTitle(request.title);
        job.setDescription(request.description);
        job.setLocation(request.location);
        job.setExperienceRequired(request.experienceRequired);
        job.setCompany(company);
        return jobRepository.save(job);
    }

    @Cacheable(value= "jobSearch", key="#keyword + '-' + #location+ '-'+ #exp")
    public List<Job> search(String keyword, String location, Integer exp){
        return jobRepository.search(keyword,location,exp);
    }
}
