package com.berke.socialmedia.service;

import com.berke.socialmedia.repository.LikeRepository;
import com.berke.socialmedia.entity.Like;
import com.berke.socialmedia.entity.Post;
import com.berke.socialmedia.entity.Profile;
import com.berke.socialmedia.dto.like.LikeDto;
import com.berke.socialmedia.exception.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;

    private final PostService postService;

    private final ProfileService profileService;

    private final UserService userService;

    private final ModelMapper modelMapper;

    public LikeService(LikeRepository likeRepository, PostService postService, ProfileService profileService, UserService userService, ModelMapper modelMapper) {
        this.likeRepository = likeRepository;
        this.postService = postService;
        this.profileService = profileService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }


    public List<LikeDto> getAll(Optional<Long> profileId, Optional<Long> postId) {
        if(profileId.isPresent() && postId.isPresent())
            return Arrays.asList(modelMapper.map(likeRepository.getByProfileIdAndPostId(profileId.get(), postId.get()), LikeDto.class));
        else if (profileId.isPresent())
            return Arrays.asList(modelMapper.map(likeRepository.getAllByProfileId(profileId.get()), LikeDto[].class));
        else if(postId.isPresent())
            return Arrays.asList(modelMapper.map(likeRepository.getAllByPostId(postId.get()), LikeDto[].class));

        return Arrays.asList(modelMapper.map(likeRepository.findAll(), LikeDto[].class));
    }

    public LikeDto getById(Long id) {
        return modelMapper.map(checkAndGetLikeById(id), LikeDto.class);
    }


    public LikeDto create(Long postId) {
        Profile profile = profileService.checkAndGetProfileById(userService.getCurrentUser().getId());
        Post post = postService.checkAndGetPostById(postId);

        Like like = likeRepository.getByProfileIdAndPostId(profile.getId(), postId);
        if(like !=null){
            throw new IllegalArgumentException("Already liked");
        }
        like = Like.builder()
                .profile(profile)
                .post(post)
                .build();

        return modelMapper.map(likeRepository.save(like), LikeDto.class);
    }


    public void delete(Long postId) {
        //kaldırılabilir sadece post olmadığı zaman error atmaya yarıyor
        Post post = postService.checkAndGetPostById(postId);

        Like like = likeRepository.getByProfileIdAndPostId(userService.getCurrentUser().getId(), postId);
        if(like != null){
            likeRepository.delete(like);
        }
    }

    protected Like checkAndGetLikeById(Long id){
        Optional<Like> like = likeRepository.findById(id);
        if(!like.isPresent())
            throw new NotFoundException("Like", "No like found with this id");
        return like.get();
    }
}
