package com.berke.socialmedia.service;

import com.berke.socialmedia.dto.RoleDto;

import java.util.List;

public interface RoleService {

    List<RoleDto> getAll();

    RoleDto getById(Long id);

    RoleDto save(RoleDto roleDto);

    RoleDto addPrivilegeById(Long roleId, Long privilegeId);
}
