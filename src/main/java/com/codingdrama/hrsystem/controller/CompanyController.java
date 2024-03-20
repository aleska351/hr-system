package com.codingdrama.hrsystem.controller;

import com.codingdrama.hrsystem.service.CompanyService;
import com.codingdrama.hrsystem.service.dto.CompanyDto;
import com.codingdrama.hrsystem.service.dto.CreateCompanyRequest;
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
@RequestMapping("/v1/company")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/")
    public List<CompanyDto> getCompanies() {
        return companyService.getAll();
    }

    @GetMapping("/{id}")
    public CompanyDto getCompanyById(@PathVariable Long id) {
        return companyService.getById(id);
    }

    @PostMapping("/")
    public CompanyDto createCompany(@RequestBody CreateCompanyRequest request) {
        return companyService.createCompany(request);
    }

    @PutMapping("/{id}")
    public CompanyDto updateCompany(@PathVariable Long id, @RequestBody CompanyDto companyDto) {
        return companyService.updateCompany(id, companyDto);
    }

    @DeleteMapping("/{id}")
    public void deleteCompany(@PathVariable Long id) {
        companyService.delete(id);
    }
}
