package com.codingdrama.hrsystem.service;


import com.codingdrama.hrsystem.service.dto.CreateUserResponse;
import com.codingdrama.hrsystem.service.dto.UserDto;

import java.util.List;

public interface UserService extends BaseService<UserDto> {
    CreateUserResponse register(UserDto user, String role);


    UserDto findByUsername(String username);

    List<UserDto> getByDepartment(Long departmentId);

    UserDto update(Long id, UserDto userDTO);
    
}
