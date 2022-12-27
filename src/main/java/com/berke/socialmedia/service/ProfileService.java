package com.berke.socialmedia.service;

import com.berke.socialmedia.repository.ProfileRepository;
import com.berke.socialmedia.entity.Profile;
import com.berke.socialmedia.dto.profile.ProfileDto;
import com.berke.socialmedia.dto.profile.ProfileRequestDto;
import com.berke.socialmedia.exception.AlreadyExistsException;
import com.berke.socialmedia.exception.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserService userService;

    private final ModelMapper modelMapper;

    public ProfileService(ProfileRepository profileRepository, UserService userService, ModelMapper modelMapper) {
        this.profileRepository = profileRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }


    public List<ProfileDto> getAll(){
        return Arrays.asList(modelMapper.map(profileRepository.findAll(), ProfileDto[].class));
    }


    public ProfileDto getById(Long id){
        Profile profile = checkAndGetProfileById(id);
        return modelMapper.map(profile, ProfileDto.class);
    }


    public ProfileDto update(Long id, ProfileRequestDto profileRequestDto) {
        Profile profile = checkAndGetProfileById(id);

        if(profileRequestDto.getFirstName() != null)
            profile.setFirstName(profileRequestDto.getFirstName());
        if(profileRequestDto.getLastName() != null)
            profile.setLastName(profileRequestDto.getLastName());
        if(profileRequestDto.getPhoneNumber() != null)
            profile.setPhoneNumber(profileRequestDto.getPhoneNumber());
        if(profileRequestDto.getPicture() != null)
            profile.setPicture(profileRequestDto.getPicture());
        return modelMapper.map(profileRepository.save(profile), ProfileDto.class);
    }


    public List<ProfileDto> getFollowers(Long id) {
        return Arrays.asList(modelMapper.map(profileRepository.getProfilesByFollowingId(id), ProfileDto[].class));
    }


    public Boolean removeFollower(Long profileId) {
        Profile profile = checkAndGetProfileById(userService.getCurrentUser().getId());
        Profile profile2 = checkAndGetProfileById(profileId);

        profile2.removeFollowing(profile);
        profileRepository.save(profile2);
        return Boolean.TRUE;
    }


    public List<ProfileDto> getFollowings(Long id){
        Profile profile = checkAndGetProfileById(id);
        return Arrays.asList(modelMapper.map(profile.getFollowing(), ProfileDto[].class));
    }


    public Boolean follow(Long followUserId) {
        Profile profile = checkAndGetProfileById(userService.getCurrentUser().getId());
        Profile profile2 = checkAndGetProfileById(followUserId);

        profile.addFollowing(profile2);
        profileRepository.save(profile);
        return Boolean.TRUE;
    }


    public Boolean unfollow(Long unfollowUserId) {
        Profile profile = checkAndGetProfileById(userService.getCurrentUser().getId());
        Profile profile2 = checkAndGetProfileById(unfollowUserId);

        profile.removeFollowing(profile2);
        profileRepository.save(profile);
        return Boolean.TRUE;
    }


    protected Profile checkAndGetProfileById(Long id){
        Optional<Profile> profile = profileRepository.findById(id);
        if(!profile.isPresent())
            throw new NotFoundException("Profile", "No profile found with this id");
        return profile.get();
    }

    protected void checkAndThrowErrorIfProfileExists(Long id){
        Optional<Profile> profile = profileRepository.findById(id);
        if(profile.isPresent())
            throw new AlreadyExistsException("Profile", "A profile with this id already exists");
    }
}
