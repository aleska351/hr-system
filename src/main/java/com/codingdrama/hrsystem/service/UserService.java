package com.codingdrama.hrsystem.service;


import com.codingdrama.hrsystem.service.dto.CreateUserRequest;
import com.codingdrama.hrsystem.service.dto.UserDto;

import java.util.List;

public interface UserService extends BaseService<UserDto> {
    UserDto createUser(CreateUserRequest user);
    
    UserDto updateUser(Long id, UserDto user);


    UserDto findByEmail(String email);

    List<UserDto> getByDepartment(Long departmentId);

    UserDto update(Long id, UserDto userDTO);
    
}
