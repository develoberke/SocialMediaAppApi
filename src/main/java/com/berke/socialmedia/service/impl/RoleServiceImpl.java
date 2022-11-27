package com.berke.socialmedia.service.impl;

import com.berke.socialmedia.dao.PrivilegeRepository;
import com.berke.socialmedia.dao.RoleRepository;
import com.berke.socialmedia.dao.entity.Privilege;
import com.berke.socialmedia.dao.entity.Role;
import com.berke.socialmedia.dto.RoleDto;
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
        Optional<Role> role = roleRepository.findById(id);
        if(role.isPresent()){
            return modelMapper.map(role.get(),RoleDto.class);
        }
        throw new EntityNotFoundException("There is no role with this id");
    }

    @Override
    public RoleDto save(RoleDto roleDto) {
        Optional<Role> control = roleRepository.findById(roleDto.getId());
        if (control.isPresent()){
            throw new IllegalArgumentException("Id already exist");
        }
        Role role = modelMapper.map(roleDto, Role.class);
        role = roleRepository.save(role);
        roleDto.setId(role.getId());
        return roleDto;
    }


    @Override
    public RoleDto update(Long id, RoleDto roleDto) {
        Optional<Role> role = roleRepository.findById(id);
        if(!role.isPresent())
            throw new IllegalArgumentException("Role not found");

        if(roleDto.getName() != null)
            role.get().setName(roleDto.getName());
        if(roleDto.getDescription() != null)
            role.get().setDescription(roleDto.getDescription());

        return modelMapper.map(roleRepository.save(role.get()), RoleDto.class);
    }


    @Override
    public void delete(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        if(!role.isPresent())
            throw new IllegalArgumentException("Role not found");

        roleRepository.delete(role.get());
    }

    @Override
    public RoleDto addPrivilegeById(Long roleId, Long privilegeId) {
        Optional<Role> role = roleRepository.findById(roleId);
        Optional<Privilege> privilege = privilegeRepository.findById(privilegeId);
        if(!role.isPresent() || !privilege.isPresent()){
            throw new IllegalArgumentException("Role or Privilege not found");
        }
        role.get().addPrivilege(privilege.get());
        roleRepository.save(role.get());
        return modelMapper.map(role.get(), RoleDto.class);

    }

    @Override
    public Boolean removePrivilegeById(Long id, Long privilegeId) {
        Optional<Role> role = roleRepository.findById(id);
        Optional<Privilege> privilege = privilegeRepository.findById(privilegeId);
        if(!role.isPresent() || !privilege.isPresent()){
            throw new IllegalArgumentException("Role or Privilege not found");
        }

        role.get().removePrivilege(privilege.get());
        roleRepository.save(role.get());
        return Boolean.TRUE;
    }

}
