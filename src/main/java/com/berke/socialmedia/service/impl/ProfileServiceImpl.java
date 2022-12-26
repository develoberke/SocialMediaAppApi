package com.berke.socialmedia.service.impl;

import com.berke.socialmedia.dao.ProfileRepository;
import com.berke.socialmedia.dao.UserRepository;
import com.berke.socialmedia.dao.entity.Profile;
import com.berke.socialmedia.dao.entity.User;
import com.berke.socialmedia.dto.ProfileDto;
import com.berke.socialmedia.exception.AlreadyExistsException;
import com.berke.socialmedia.exception.NotFoundException;
import com.berke.socialmedia.service.ProfileService;
import com.berke.socialmedia.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

    private final UserRepository userRepository;

    private final UserService userService;

    private final ModelMapper modelMapper;

    public ProfileServiceImpl(ProfileRepository profileRepository, UserRepository userRepository, UserService userService, ModelMapper modelMapper) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ProfileDto> getAll(){
        return Arrays.asList(modelMapper.map(profileRepository.findAll(), ProfileDto[].class));
    }

    @Override
    public ProfileDto getById(Long id){
        Profile profile = checkAndGetProfileById(id);
        return modelMapper.map(profile, ProfileDto.class);
    }

    @Override
    public ProfileDto update(Long id, ProfileDto profileDto) {
        Profile profile = checkAndGetProfileById(id);

        if(profileDto.getFirstName() != null)
            profile.setFirstName(profileDto.getFirstName());
        if(profileDto.getLastName() != null)
            profile.setLastName(profileDto.getLastName());
        if(profileDto.getPhoneNumber() != null)
            profile.setPhoneNumber(profileDto.getPhoneNumber());
        if(profileDto.getPicture() != null)
            profile.setPicture(profileDto.getPicture());
        return modelMapper.map(profileRepository.save(profile), ProfileDto.class);
    }


    @Override
    public List<ProfileDto> getFollowers(Long id) {
        return Arrays.asList(modelMapper.map(profileRepository.getProfilesByFollowingId(id), ProfileDto[].class));
    }

    @Override
    public Boolean removeFollower(Long profileId) {
        Profile profile = checkAndGetProfileById(userService.getCurrentUser().getId());
        Profile profile2 = checkAndGetProfileById(profileId);

        profile2.removeFollowing(profile);
        profileRepository.save(profile2);
        return Boolean.TRUE;
    }

    @Override
    public List<ProfileDto> getFollowings(Long id){
        Profile profile = checkAndGetProfileById(id);
        return Arrays.asList(modelMapper.map(profile.getFollowing(), ProfileDto[].class));
    }

    @Override
    public Boolean follow(Long followUserId) {
        Profile profile = checkAndGetProfileById(userService.getCurrentUser().getId());
        Profile profile2 = checkAndGetProfileById(followUserId);

        profile.addFollowing(profile2);
        profileRepository.save(profile);
        return Boolean.TRUE;
    }

    @Override
    public Boolean unfollow(Long unfollowUserId) {
        Profile profile = checkAndGetProfileById(userService.getCurrentUser().getId());
        Profile profile2 = checkAndGetProfileById(unfollowUserId);

        profile.removeFollowing(profile2);
        profileRepository.save(profile);
        return Boolean.TRUE;
    }


    private Profile checkAndGetProfileById(Long id){
        Optional<Profile> profile = profileRepository.findById(id);
        if(!profile.isPresent())
            throw new NotFoundException("Profile", "No profile found with this id");
        return profile.get();
    }

    private void checkAndThrowErrorIfProfileExists(Long id){
        Optional<Profile> profile = profileRepository.findById(id);
        if(profile.isPresent())
            throw new AlreadyExistsException("Profile", "A profile with this id already exists");
    }
}
