package com.codingdrama.hrsystem.service.impl;


import com.codingdrama.hrsystem.exceptions.LocalizedResponseStatusException;
import com.codingdrama.hrsystem.model.User;
import com.codingdrama.hrsystem.repository.CompanyRepository;
import com.codingdrama.hrsystem.repository.DepartmentRepository;
import com.codingdrama.hrsystem.repository.ProjectRepository;
import com.codingdrama.hrsystem.repository.RoleRepository;
import com.codingdrama.hrsystem.repository.UserRepository;
import com.codingdrama.hrsystem.service.UserService;
import com.codingdrama.hrsystem.service.dto.CreateUserResponse;
import com.codingdrama.hrsystem.service.dto.RoleDto;
import com.codingdrama.hrsystem.service.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final CompanyRepository companyRepository;

    private final ProjectRepository projectRepository;
//    private final BCryptPasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, DepartmentRepository departmentRepository, CompanyRepository companyRepository, ProjectRepository projectRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.departmentRepository = departmentRepository;
        this.companyRepository = companyRepository;
        this.projectRepository = projectRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CreateUserResponse register(UserDto user, String role) {
        departmentRepository.findById(user.getDepartmentId()).orElseThrow(() -> new LocalizedResponseStatusException(HttpStatus.NOT_FOUND, "department.not.found"));
        RoleDto roleUser = modelMapper.map(roleRepository.findByName(role), RoleDto.class);
        List<RoleDto> userRoles = new ArrayList<>(1);
        userRoles.add(roleUser);
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoleIds(userRoles.stream().map(RoleDto::getId).collect(Collectors.toList()));

        User registeredUser = userRepository.save(modelMapper.map(user, User.class));
        log.info("User: {} successfully registered", registeredUser);
        return modelMapper.map(registeredUser, CreateUserResponse.class);
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
            .map(user -> modelMapper.map(user, UserDto.class))
            .collect(Collectors.toList());
    }

    @Override
    public UserDto getById(Long id) {
        return modelMapper.map(userRepository.findById(id).orElseThrow(() -> new LocalizedResponseStatusException(HttpStatus.NOT_FOUND, "user.not.found")), UserDto.class);
    }

    @Override
    public UserDto findByUsername(String email) {
        return modelMapper.map(userRepository.findByEmail(email).orElseThrow(() -> new LocalizedResponseStatusException(HttpStatus.NOT_FOUND, "user.not.found")), UserDto.class);
    }

    @Override
    public List<UserDto> getByDepartment(Long departmentId) {
        departmentRepository.findById(departmentId).orElseThrow(() -> new LocalizedResponseStatusException(HttpStatus.NOT_FOUND, "department.not.found"));
        return userRepository.findAllByDepartmentId(departmentId).stream()
            .map(user -> modelMapper.map(user, UserDto.class))
            .collect(Collectors.toList());
    }

    @Override
    public UserDto update(Long id, UserDto user) {
        getOrThrowNotFound(id);

        user.setId(id);
//        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User updatedUser = userRepository.save(modelMapper.map(user, User.class));
        log.info("User: {} successfully updated", updatedUser);
        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public void delete(Long id) {
        getOrThrowNotFound(id);

        userRepository.deleteById(id);
        log.info("User with id: {} successfully updated", id);
    }


    private User getOrThrowNotFound(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new LocalizedResponseStatusException(HttpStatus.NOT_FOUND, "user.not.found"));
    }
}
