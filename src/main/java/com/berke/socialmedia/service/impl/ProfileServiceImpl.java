package com.berke.socialmedia.service.impl;

import com.berke.socialmedia.dao.ProfileRepository;
import com.berke.socialmedia.dao.UserRepository;
import com.berke.socialmedia.dao.entity.Profile;
import com.berke.socialmedia.dao.entity.User;
import com.berke.socialmedia.dto.ProfileDto;
import com.berke.socialmedia.service.ProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public ProfileServiceImpl(ProfileRepository profileRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ProfileDto> getAll(){
        return Arrays.asList(modelMapper.map(profileRepository.findAll(), ProfileDto[].class));
    }

    @Override
    public ProfileDto getById(Long id){
        Optional<Profile> profile = profileRepository.findById(id);
        if(!profile.isPresent()){
            throw new IllegalArgumentException("Profile not found");
        }
        return modelMapper.map(profile.get(), ProfileDto.class);
    }

    @Override
    public ProfileDto update(Long id, ProfileDto profileDto) {
        Optional<Profile> profile = profileRepository.findById(id);
        if(!profile.isPresent())
            throw new IllegalArgumentException("Profile not found");

        if(profileDto.getFirstName() != null)
            profile.get().setFirstName(profileDto.getFirstName());
        if(profileDto.getLastName() != null)
            profile.get().setLastName(profileDto.getLastName());
        if(profileDto.getPhoneNumber() != null)
            profile.get().setPhoneNumber(profileDto.getPhoneNumber());
        if(profileDto.getPicture() != null)
            profile.get().setPicture(profileDto.getPicture());
        return modelMapper.map(profileRepository.save(profile.get()), ProfileDto.class);
    }

}
