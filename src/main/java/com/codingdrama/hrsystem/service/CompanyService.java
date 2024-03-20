package com.codingdrama.hrsystem.service;

import com.codingdrama.hrsystem.service.dto.CompanyDto;
import com.codingdrama.hrsystem.service.dto.CreateCompanyRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CompanyService extends BaseService<CompanyDto>{
 
    CompanyDto findCompanyByCode(String companyCode);
    CompanyDto createCompany(CreateCompanyRequest createCompanyRequest);
    CompanyDto updateCompany(Long id, CompanyDto company);
  
}
