package com.berke.socialmedia.service;

import com.berke.socialmedia.repository.RoleRepository;
import com.berke.socialmedia.entity.Privilege;
import com.berke.socialmedia.entity.Role;
import com.berke.socialmedia.dto.role.RoleDto;
import com.berke.socialmedia.dto.role.RoleRequestDto;
import com.berke.socialmedia.exception.AlreadyExistsException;
import com.berke.socialmedia.exception.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final PrivilegeService privilegeService;
    private final ModelMapper modelMapper;

    public RoleService(RoleRepository roleRepository, PrivilegeService privilegeService, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.privilegeService = privilegeService;
        this.modelMapper = modelMapper;
    }


    public List<RoleDto> getAll() {
        return Arrays.asList(modelMapper.map(roleRepository.findAll(),RoleDto[].class));
    }


    public RoleDto getById(Long id) {
        Role role = checkAndGetRoleById(id);

        return modelMapper.map(role,RoleDto.class);
    }


    public RoleDto save(RoleRequestDto roleRequestDto) {
        Role role = modelMapper.map(roleRequestDto, Role.class);
        role = roleRepository.save(role);
        return modelMapper.map(role, RoleDto.class);
    }



    public RoleDto update(Long id, RoleRequestDto roleRequestDto) {
        Role role = checkAndGetRoleById(id);

        if(roleRequestDto.getName() != null)
            role.setName(roleRequestDto.getName());
        if(roleRequestDto.getDescription() != null)
            role.setDescription(roleRequestDto.getDescription());

        return modelMapper.map(roleRepository.save(role), RoleDto.class);
    }



    public void delete(Long id) {
        Role role = checkAndGetRoleById(id);
        roleRepository.delete(role);
    }


    public RoleDto addPrivilegeById(Long roleId, Long privilegeId) {
        Role role = checkAndGetRoleById(roleId);
        Privilege privilege = privilegeService.checkAndGetPrivilegeById(privilegeId);

        role.addPrivilege(privilege);
        roleRepository.save(role);
        return modelMapper.map(role, RoleDto.class);

    }


    public Boolean removePrivilegeById(Long id, Long privilegeId) {
        Role role = checkAndGetRoleById(id);
        Privilege privilege = privilegeService.checkAndGetPrivilegeById(privilegeId);
        role.removePrivilege(privilege);
        roleRepository.save(role);
        return Boolean.TRUE;
    }


    protected Role checkAndGetRoleById(Long id){
        Optional<Role> role = roleRepository.findById(id);
        if(!role.isPresent())
            throw new NotFoundException("Role", "No role found with this id");
        return role.get();
    }

    protected void checkAndThrowErrorIfRoleExists(Long id){
        Optional<Role> role = roleRepository.findById(id);
        if(role.isPresent())
            throw new AlreadyExistsException("Role", "Role with this id already exists");
    }

    protected Role findByNameFromRepository(String name){
        return roleRepository.findByName(name);
    }

}
