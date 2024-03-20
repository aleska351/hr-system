package com.codingdrama.hrsystem.service.impl;

import com.codingdrama.hrsystem.exceptions.LocalizedResponseStatusException;
import com.codingdrama.hrsystem.model.Company;
import com.codingdrama.hrsystem.model.Department;
import com.codingdrama.hrsystem.repository.CompanyRepository;
import com.codingdrama.hrsystem.repository.DepartmentRepository;
import com.codingdrama.hrsystem.service.DepartmentService;
import com.codingdrama.hrsystem.service.dto.CreateDepartmentRequest;
import com.codingdrama.hrsystem.service.dto.DepartmentDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {
    private final CompanyRepository companyRepository;

    private final DepartmentRepository departmentRepository;

    private final ModelMapper modelMapper;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, ModelMapper modelMapper,
                                 CompanyRepository companyRepository) {
        this.departmentRepository = departmentRepository;
        this.modelMapper = modelMapper;
        this.companyRepository = companyRepository;
    }


    @Override
    public DepartmentDto createDepartment(CreateDepartmentRequest createDepartmentRequest) {
        Company company = companyRepository.findById(createDepartmentRequest.getCompanyId()).orElseThrow(() -> new LocalizedResponseStatusException(HttpStatus.NOT_FOUND, "company.not.found"));
        if (departmentRepository.existsByNameAndCompany(createDepartmentRequest.getName(), company)) {
            throw new LocalizedResponseStatusException(HttpStatus.BAD_REQUEST, "department.already.exist");
        }

        Department department = modelMapper.map(createDepartmentRequest, Department.class);

        Department createdDepartment = departmentRepository.save(department);

        return null;
    }

    @Override
    public DepartmentDto updateDepartment(Long id, DepartmentDto company) {
        return null;
    }
   

    @Override
    public List<DepartmentDto> getAll() {
        return departmentRepository.findAll().stream().map(company -> modelMapper.map(company, DepartmentDto.class)).collect(Collectors.toList());
    }

    @Override
    public DepartmentDto getById(Long id) {
       return modelMapper.map(departmentRepository.findById(id).orElseThrow(() -> new LocalizedResponseStatusException(HttpStatus.NOT_FOUND, "company.not.found")), DepartmentDto.class);
    }

    @Override
    public void delete(Long id) {

    }
}
