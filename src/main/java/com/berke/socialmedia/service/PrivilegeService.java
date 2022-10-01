package com.berke.socialmedia.service;

import com.berke.socialmedia.dto.PrivilegeDto;

import java.util.List;

public interface PrivilegeService {

    List<PrivilegeDto> getAll();

    PrivilegeDto getById(Long id);

    PrivilegeDto save(PrivilegeDto privilegeDto);

    PrivilegeDto update(PrivilegeDto privilegeDto);

    Boolean delete(Long id);
}
