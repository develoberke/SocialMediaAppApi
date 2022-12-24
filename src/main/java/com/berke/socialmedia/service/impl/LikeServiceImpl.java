package com.berke.socialmedia.service.impl;

import com.berke.socialmedia.dao.LikeRepository;
import com.berke.socialmedia.dao.PostRepository;
import com.berke.socialmedia.dao.ProfileRepository;
import com.berke.socialmedia.dao.entity.Like;
import com.berke.socialmedia.dao.entity.Post;
import com.berke.socialmedia.dao.entity.Profile;
import com.berke.socialmedia.dto.LikeDto;
import com.berke.socialmedia.exception.NotFoundException;
import com.berke.socialmedia.service.LikeService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;

    private final PostRepository postRepository;

    private final ProfileRepository profileRepository;

    private final ModelMapper modelMapper;

    public LikeServiceImpl(LikeRepository likeRepository, PostRepository postRepository, ProfileRepository profileRepository, ModelMapper modelMapper) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.profileRepository = profileRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public List<LikeDto> getAll(Optional<Long> profileId, Optional<Long> postId) {
        if(profileId.isPresent() && postId.isPresent())
            return Arrays.asList(modelMapper.map(likeRepository.getByProfileIdAndPostId(profileId.get(), postId.get()), LikeDto.class));
        else if (profileId.isPresent())
            return Arrays.asList(modelMapper.map(likeRepository.getAllByProfileId(profileId.get()), LikeDto[].class));
        else if(postId.isPresent())
            return Arrays.asList(modelMapper.map(likeRepository.getAllByPostId(postId.get()), LikeDto[].class));

        return Arrays.asList(modelMapper.map(likeRepository.findAll(), LikeDto[].class));
    }

    @Override
    public LikeDto getById(Long id) {
        return modelMapper.map(checkAndGetLikeById(id), LikeDto.class);
    }

    @Override
    public LikeDto create(Long profileId, Long postId) {
        Optional<Profile> profile = profileRepository.findById(profileId);
        if(!profile.isPresent())
            throw new NotFoundException("Profile", "No profile found with this id");
        Optional<Post> post = postRepository.findById(postId);
        if(!post.isPresent())
            throw new NotFoundException("Post", "No post found with this id");

        Like like = Like.builder()
                .profile(profile.get())
                .post(post.get())
                .build();

        return modelMapper.map(likeRepository.save(like), LikeDto.class);
    }

    @Override
    public void delete(Long profileId, Long postId) {
        Optional<Profile> profile = profileRepository.findById(profileId);
        if(!profile.isPresent())
            throw new NotFoundException("Profile", "No profile found with this id");
        Optional<Post> post = postRepository.findById(postId);
        if(!post.isPresent())
            throw new NotFoundException("Post", "No post found with this id");
        Like like = likeRepository.getByProfileIdAndPostId(profileId, postId);
        likeRepository.delete(like);
    }

    private Like checkAndGetLikeById(Long id){
        Optional<Like> like = likeRepository.findById(id);
        if(!like.isPresent())
            throw new NotFoundException("Like", "No like found with this id");
        return like.get();
    }
}
