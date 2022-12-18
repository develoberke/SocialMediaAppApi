package com.berke.socialmedia.service.impl;

import com.berke.socialmedia.dao.PrivilegeRepository;
import com.berke.socialmedia.dao.entity.Privilege;
import com.berke.socialmedia.dto.PrivilegeDto;
import com.berke.socialmedia.exception.AlreadyExistsException;
import com.berke.socialmedia.exception.NotFoundException;
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
        Privilege privilege = checkAndGetPrivilegeById(id);

        return modelMapper.map(privilege,PrivilegeDto.class);
    }


    @Override
    public PrivilegeDto save(PrivilegeDto privilegeDto) {
        Privilege privilege = modelMapper.map(privilegeDto, Privilege.class);
        privilege.setId(0L);
        privilege = privilegeRepository.save(privilege);
        privilegeDto.setId(privilege.getId());
        return privilegeDto;
    }

    @Override
    public PrivilegeDto update(Long id, PrivilegeDto privilegeDto) {
        Privilege privilege = checkAndGetPrivilegeById(id);

        if(privilegeDto.getName() != null)
            privilege.setName(privilegeDto.getName());
        if(privilegeDto.getDescription() != null)
            privilege.setDescription(privilegeDto.getDescription());
        privilegeRepository.save(privilege);
        return privilegeDto;
    }

    @Override
    public Boolean delete(Long id) {
        Privilege privilege = checkAndGetPrivilegeById(id);
        privilegeRepository.delete(privilege);
        return true;
    }

    private Privilege checkAndGetPrivilegeById(Long id){
        Optional<Privilege> privilege = privilegeRepository.findById(id);
        if(!privilege.isPresent())
            throw new NotFoundException("Privilege", "No privilege found with this id");
        return privilege.get();
    }

    private void checkAndThrowErrorIfPrivilegeExists(Long id){
        Optional<Privilege> privilege = privilegeRepository.findById(id);
        if(privilege.isPresent())
            throw new AlreadyExistsException("Privilege", "A privilege with this id already exists");
    }
}
