package com.berke.socialmedia.service;

import com.berke.socialmedia.repository.PrivilegeRepository;
import com.berke.socialmedia.entity.Privilege;
import com.berke.socialmedia.dto.privilege.PrivilegeDto;
import com.berke.socialmedia.dto.privilege.PrivilegeRequestDto;
import com.berke.socialmedia.exception.AlreadyExistsException;
import com.berke.socialmedia.exception.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
public class PrivilegeService {

    private final PrivilegeRepository privilegeRepository;

    private final ModelMapper modelMapper;

    public PrivilegeService(PrivilegeRepository privilegeRepository, ModelMapper modelMapper) {
        this.privilegeRepository = privilegeRepository;
        this.modelMapper = modelMapper;
    }

    public List<PrivilegeDto> getAll() {
        return Arrays.asList(modelMapper.map(privilegeRepository.findAll(),PrivilegeDto[].class));
    }

    public PrivilegeDto getById(Long id) {
        Privilege privilege = checkAndGetPrivilegeById(id);

        return modelMapper.map(privilege,PrivilegeDto.class);
    }



    public PrivilegeDto save(PrivilegeRequestDto privilegeRequestDto) {
        Privilege privilege = modelMapper.map(privilegeRequestDto, Privilege.class);
        privilege = privilegeRepository.save(privilege);
        return modelMapper.map(privilege, PrivilegeDto.class);
    }


    public PrivilegeDto update(Long id, PrivilegeRequestDto privilegeRequestDto) {
        Privilege privilege = checkAndGetPrivilegeById(id);

        if(privilegeRequestDto.getName() != null)
            privilege.setName(privilegeRequestDto.getName());
        if(privilegeRequestDto.getDescription() != null)
            privilege.setDescription(privilegeRequestDto.getDescription());
        privilege = privilegeRepository.save(privilege);
        return modelMapper.map(privilege, PrivilegeDto.class);
    }


    public Boolean delete(Long id) {
        Privilege privilege = checkAndGetPrivilegeById(id);
        privilegeRepository.delete(privilege);
        return true;
    }

    protected Privilege checkAndGetPrivilegeById(Long id){
        Optional<Privilege> privilege = privilegeRepository.findById(id);
        if(!privilege.isPresent())
            throw new NotFoundException("Privilege", "No privilege found with this id");
        return privilege.get();
    }

    protected void checkAndThrowErrorIfPrivilegeExists(Long id){
        Optional<Privilege> privilege = privilegeRepository.findById(id);
        if(privilege.isPresent())
            throw new AlreadyExistsException("Privilege", "A privilege with this id already exists");
    }
}
