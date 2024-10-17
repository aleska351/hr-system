package com.codingdrama.hrsystem.service.impl;


import com.codingdrama.hrsystem.exceptions.LocalizedResponseStatusException;
import com.codingdrama.hrsystem.model.Role;
import com.codingdrama.hrsystem.model.User;
import com.codingdrama.hrsystem.repository.DepartmentRepository;
import com.codingdrama.hrsystem.repository.RoleRepository;
import com.codingdrama.hrsystem.repository.UserRepository;
import com.codingdrama.hrsystem.service.UserService;
import com.codingdrama.hrsystem.service.dto.CreateUserRequest;
import com.codingdrama.hrsystem.service.dto.UserDto;
import com.codingdrama.hrsystem.service.email.context.WelcomeEmailContext;
import com.codingdrama.hrsystem.service.email.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final EmailService emailService;
    
    private final BCryptPasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, DepartmentRepository departmentRepository, EmailService emailService, BCryptPasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.departmentRepository = departmentRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDto createUser(CreateUserRequest request) {
        departmentRepository.findById(request.getDepartmentId()).orElseThrow(() -> new LocalizedResponseStatusException(HttpStatus.NOT_FOUND, "department.not.found"));
        List<Role> roleUser = roleRepository.findByNameIn(request.getRoles());
        User user = modelMapper.map(request, User.class);
        user.setRoles(roleUser);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User registeredUser = userRepository.save(modelMapper.map(user, User.class));
        sendWelcomeEmail(user);
        log.info("User: {} successfully registered", registeredUser);
        return modelMapper.map(registeredUser, UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto user) {
        return null;
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
    public UserDto findByEmail(String email) {
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
        user.setPassword(passwordEncoder.encode(user.getPassword()));

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
    
    private void sendWelcomeEmail(User user) {
        WelcomeEmailContext emailContext = new WelcomeEmailContext();
        emailContext.init(user);
        emailService.sendMail(emailContext);
        log.info("Welcome email was sent to user {}", user.getEmail());
    }
}
