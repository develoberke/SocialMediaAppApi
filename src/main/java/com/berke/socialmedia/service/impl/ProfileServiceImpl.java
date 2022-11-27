package com.berke.socialmedia.service.impl;

import com.berke.socialmedia.dao.PostRepository;
import com.berke.socialmedia.dao.ProfileRepository;
import com.berke.socialmedia.dao.UserRepository;
import com.berke.socialmedia.dao.entity.Post;
import com.berke.socialmedia.dao.entity.Profile;
import com.berke.socialmedia.dto.PostDto;
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

    private final PostRepository postRepository;

    private final ModelMapper modelMapper;

    public ProfileServiceImpl(ProfileRepository profileRepository, UserRepository userRepository, PostRepository postRepository, ModelMapper modelMapper) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
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


    @Override
    public List<PostDto> getPosts(Long id) {
        Optional<Profile> profile = profileRepository.findById(id);
        if(!profile.isPresent())
            throw new IllegalArgumentException("Profile not found");

        return Arrays.asList(modelMapper.map(postRepository.getAllByProfileId(id), PostDto[].class));
    }

    @Override
    public PostDto addPost(Long id, PostDto postDto) {
        Optional<Profile> profile = profileRepository.findById(id);
        Optional<Post> post = postRepository.findById(postDto.getId());
        if(!profile.isPresent())
            throw new IllegalArgumentException("Profile not found");
        if(post.isPresent())
            throw new IllegalArgumentException("Post id is already used");
        Post postToSave = modelMapper.map(postDto, Post.class);
        profile.get().addPost(postToSave);
        postToSave = postRepository.save(postToSave);
        profileRepository.save(profile.get());

        return modelMapper.map(postToSave, PostDto.class);

    }



    @Override
    public List<ProfileDto> getFollowers(Long id) {
        return Arrays.asList(modelMapper.map(profileRepository.getProfilesByFollowingId(id), ProfileDto[].class));
    }

    @Override
    public Boolean removeFollower(Long id, Long profileId) {
        Optional<Profile> profile = profileRepository.findById(id);
        Optional<Profile> profile2 = profileRepository.findById(profileId);
        if(!profile.isPresent() || !profile2.isPresent())
            throw new IllegalArgumentException("Profile not found");
        profile2.get().removeFollowing(profile.get());
        profileRepository.save(profile2.get());
        return Boolean.TRUE;
    }

    @Override
    public List<ProfileDto> getFollowings(Long id){
        Optional<Profile> profile = profileRepository.findById(id);
        if(!profile.isPresent())
            throw new IllegalArgumentException("Profile not found");
        return Arrays.asList(modelMapper.map(profile.get().getFollowing(), ProfileDto[].class));
    }

    @Override
    public Boolean follow(Long id, Long followUserId) {
        Optional<Profile> profile = profileRepository.findById(id);
        Optional<Profile> profile2 = profileRepository.findById(followUserId);
        if(!profile.isPresent() || !profile2.isPresent())
            throw new IllegalArgumentException("Profile not found");

        profile.get().addFollowing(profile2.get());
        profileRepository.save(profile.get());
        return Boolean.TRUE;
    }

    @Override
    public Boolean unfollow(Long id, Long unfollowUserId) {
        Optional<Profile> profile = profileRepository.findById(id);
        Optional<Profile> profile2 = profileRepository.findById(unfollowUserId);
        if(!profile.isPresent() || !profile2.isPresent())
            throw new IllegalArgumentException("Profile not found");

        profile.get().removeFollowing(profile2.get());
        profileRepository.save(profile.get());
        return Boolean.TRUE;
    }
}
