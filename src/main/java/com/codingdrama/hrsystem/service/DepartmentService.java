package com.codingdrama.hrsystem.service;

import com.codingdrama.hrsystem.service.dto.DepartmentDto;
import com.codingdrama.hrsystem.service.dto.CreateDepartmentRequest;
import org.springframework.stereotype.Service;

import java.util.List;

public interface DepartmentService extends BaseService<DepartmentDto>{
    DepartmentDto createDepartment(CreateDepartmentRequest createDepartmentRequest);
    DepartmentDto updateDepartment(Long id, DepartmentDto company);
}
