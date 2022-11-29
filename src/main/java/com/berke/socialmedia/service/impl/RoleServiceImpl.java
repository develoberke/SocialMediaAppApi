package com.berke.socialmedia.service.impl;

import com.berke.socialmedia.dao.PrivilegeRepository;
import com.berke.socialmedia.dao.RoleRepository;
import com.berke.socialmedia.dao.entity.Privilege;
import com.berke.socialmedia.dao.entity.Role;
import com.berke.socialmedia.dto.RoleDto;
import com.berke.socialmedia.exception.AlreadyExistsException;
import com.berke.socialmedia.exception.NotFoundException;
import com.berke.socialmedia.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final PrivilegeRepository privilegeRepository;
    private final ModelMapper modelMapper;

    public RoleServiceImpl(RoleRepository roleRepository, PrivilegeRepository privilegeRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<RoleDto> getAll() {
        return Arrays.asList(modelMapper.map(roleRepository.findAll(),RoleDto[].class));
    }

    @Override
    public RoleDto getById(Long id) {
        Role role = checkAndGetRoleById(id);

        return modelMapper.map(role,RoleDto.class);
    }

    @Override
    public RoleDto save(RoleDto roleDto) {
        checkAndThrowErrorIfRoleExists(roleDto.getId());

        Role role = modelMapper.map(roleDto, Role.class);
        role = roleRepository.save(role);
        roleDto.setId(role.getId());
        return roleDto;
    }


    @Override
    public RoleDto update(Long id, RoleDto roleDto) {
        Role role = checkAndGetRoleById(id);

        if(roleDto.getName() != null)
            role.setName(roleDto.getName());
        if(roleDto.getDescription() != null)
            role.setDescription(roleDto.getDescription());

        return modelMapper.map(roleRepository.save(role), RoleDto.class);
    }


    @Override
    public void delete(Long id) {
        Role role = checkAndGetRoleById(id);
        roleRepository.delete(role);
    }

    @Override
    public RoleDto addPrivilegeById(Long roleId, Long privilegeId) {
        Role role = checkAndGetRoleById(roleId);
        Privilege privilege = checkAndGetPrivilegeById(privilegeId);

        role.addPrivilege(privilege);
        roleRepository.save(role);
        return modelMapper.map(role, RoleDto.class);

    }

    @Override
    public Boolean removePrivilegeById(Long id, Long privilegeId) {
        Role role = checkAndGetRoleById(id);
        Privilege privilege = checkAndGetPrivilegeById(privilegeId);

        role.removePrivilege(privilege);
        roleRepository.save(role);
        return Boolean.TRUE;
    }


    private Role checkAndGetRoleById(Long id){
        Optional<Role> role = roleRepository.findById(id);
        if(!role.isPresent())
            throw new NotFoundException("Role", "No role found with this id");
        return role.get();
    }

    private void checkAndThrowErrorIfRoleExists(Long id){
        Optional<Role> role = roleRepository.findById(id);
        if(role.isPresent())
            throw new AlreadyExistsException("Role", "Role with this id already exists");
    }

    private Privilege checkAndGetPrivilegeById(Long id){
        Optional<Privilege> privilege = privilegeRepository.findById(id);
        if(!privilege.isPresent())
            throw new NotFoundException("Privilege", "No privilege found with this id");
        return privilege.get();
    }
}
