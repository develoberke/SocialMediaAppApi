package com.berke.socialmedia.service.impl;

import com.berke.socialmedia.dao.PrivilegeRepository;
import com.berke.socialmedia.dao.entity.Privilege;
import com.berke.socialmedia.dto.PrivilegeDto;
import com.berke.socialmedia.service.PrivilegeService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
public class PrivilegeServiceImpl implements PrivilegeService {

    private final PrivilegeRepository privilegeRepository;

    private final ModelMapper modelMapper;

    public PrivilegeServiceImpl(PrivilegeRepository privilegeRepository, ModelMapper modelMapper) {
        this.privilegeRepository = privilegeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<PrivilegeDto> getAll() {
        return Arrays.asList(modelMapper.map(privilegeRepository.findAll(),PrivilegeDto[].class));
    }

    @Override
    public PrivilegeDto getById(Long id) {
        Optional<Privilege> privilege = privilegeRepository.findById(id);
        if(privilege.isPresent()){
            return modelMapper.map(privilege.get(),PrivilegeDto.class);
        }
        throw new EntityNotFoundException("There is no privilege with this id");
    }


    @Override
    public PrivilegeDto save(PrivilegeDto privilegeDto) {
        Optional<Privilege> control = privilegeRepository.findById(privilegeDto.getId());
        if(control.isPresent()){
            throw new IllegalArgumentException("Id already exist");
        }
        Privilege privilege = modelMapper.map(privilegeDto, Privilege.class);
        privilege = privilegeRepository.save(privilege);
        privilegeDto.setId(privilege.getId());
        return privilegeDto;
    }

    @Override
    public PrivilegeDto update(PrivilegeDto privilegeDto) {
        Optional<Privilege> privilege = privilegeRepository.findById(privilegeDto.getId());
        if(!privilege.isPresent()){
            throw new IllegalArgumentException("Privilege not found");
        }
        privilege.get().setName(privilegeDto.getName());
        privilege.get().setDescription(privilegeDto.getDescription());
        privilegeRepository.save(privilege.get());
        return privilegeDto;
    }

    @Override
    public Boolean delete(Long id) {
        Optional<Privilege> privilege = privilegeRepository.findById(id);
        if(!privilege.isPresent()){
            throw new IllegalArgumentException("Privilege not found");
        }
        privilegeRepository.delete(privilege.get());
        return true;
    }
}
