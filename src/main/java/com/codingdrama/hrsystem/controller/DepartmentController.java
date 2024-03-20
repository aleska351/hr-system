package com.codingdrama.hrsystem.controller;

import com.codingdrama.hrsystem.service.DepartmentService;
import com.codingdrama.hrsystem.service.dto.CreateDepartmentRequest;
import com.codingdrama.hrsystem.service.dto.DepartmentDto;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/department")
public class DepartmentController {
    
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/")
    public List<DepartmentDto> getDepartments() {
        return departmentService.getAll();
    }

    @GetMapping("/{id}")
    public DepartmentDto getDepartmentById(@PathVariable Long id) {
        return departmentService.getById(id);
    }

    @PostMapping("/")
    public DepartmentDto createDepartment(@RequestBody CreateDepartmentRequest request) {
        return departmentService.createDepartment(request);
    }

    @PutMapping("/{id}")
    public DepartmentDto updateDepartment(@PathVariable Long id, @RequestBody DepartmentDto departmentDto) {
        return departmentService.updateDepartment(id, departmentDto);
    }

    @DeleteMapping("/{id}")
    public void deleteDepartment(@PathVariable Long id) {
        departmentService.delete(id);
    }
}
