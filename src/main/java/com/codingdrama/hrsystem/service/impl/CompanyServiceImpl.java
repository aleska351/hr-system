package com.codingdrama.hrsystem.service.impl;

import com.codingdrama.hrsystem.exceptions.LocalizedResponseStatusException;
import com.codingdrama.hrsystem.model.Address;
import com.codingdrama.hrsystem.model.Company;
import com.codingdrama.hrsystem.model.CompanyStatus;
import com.codingdrama.hrsystem.repository.AddressRepository;
import com.codingdrama.hrsystem.repository.CompanyRepository;
import com.codingdrama.hrsystem.service.CompanyService;
import com.codingdrama.hrsystem.service.dto.CompanyDto;
import com.codingdrama.hrsystem.service.dto.CreateCompanyRequest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final AddressRepository addressRepository;

    private final ModelMapper modelMapper;

    public CompanyServiceImpl(CompanyRepository companyRepository, AddressRepository addressRepository, ModelMapper modelMapper) {
        this.companyRepository = companyRepository;
        this.addressRepository = addressRepository;
        this.modelMapper = modelMapper;
    }
    
    @Override
    public CompanyDto findCompanyByCode(String companyCode) {
        return modelMapper.map(companyRepository.findByCompanyCode(companyCode).orElseThrow(() -> new LocalizedResponseStatusException(HttpStatus.NOT_FOUND, "company.not.found")), CompanyDto.class);
    }

    @Override
    public CompanyDto createCompany(CreateCompanyRequest createCompanyRequest) {
        if (companyRepository.existsByCompanyCode(createCompanyRequest.getCompanyCode())) {
            throw new LocalizedResponseStatusException(HttpStatus.BAD_REQUEST, "company.already.exist");
        }
        
        Company company = modelMapper.map(createCompanyRequest, Company.class);
        company.setCompanyStatus(CompanyStatus.CREATED);
        
        Address address = modelMapper.map(company.getAddress(), Address.class);
        addressRepository.save(address);

        Company createdCompany = companyRepository.save(company);

        return modelMapper.map(createdCompany, CompanyDto.class);
    }

    @Override
    public CompanyDto updateCompany(Long id, CompanyDto company) {
        return null;
    }

    @Override
    public List<CompanyDto> getAll() {
        return companyRepository.findAll().stream().map(company -> modelMapper.map(company, CompanyDto.class)).collect(Collectors.toList());
    }

    @Override
    public CompanyDto getById(Long id) {
          return modelMapper.map(companyRepository.findById(id).orElseThrow(() -> new LocalizedResponseStatusException(HttpStatus.NOT_FOUND, "company.not.found")), CompanyDto.class);
    }

    @Override
    public void delete(Long id) {
        companyRepository.findById(id).orElseThrow(() -> new LocalizedResponseStatusException(HttpStatus.NOT_FOUND, "company.not.found"));
        companyRepository.deleteById(id);
    }
}
