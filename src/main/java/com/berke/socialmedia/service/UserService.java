package com.berke.socialmedia.service;

import com.berke.socialmedia.dto.UserDto;
import com.berke.socialmedia.dto.UserRegisterDto;

import java.util.List;

public interface UserService {

    List<UserDto> getAll();

    UserDto getById(Long id);

    UserDto getByUsername(String username);

    UserDto save(UserDto userDto);

    UserDto register(UserRegisterDto userRegisterDto);

    UserDto addRoleById(Long userId, Long roleId);

    Boolean delete(Long id);
}
