package com.berke.socialmedia.service;

import com.berke.socialmedia.repository.PostRepository;
import com.berke.socialmedia.entity.Post;
import com.berke.socialmedia.entity.Profile;
import com.berke.socialmedia.dto.post.PostDto;
import com.berke.socialmedia.dto.post.PostRequestDto;
import com.berke.socialmedia.exception.AlreadyExistsException;
import com.berke.socialmedia.exception.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    private final ProfileService profileService;

    private final UserService userService;

    private final ModelMapper modelMapper;

    public PostService(PostRepository postRepository, ProfileService profileService, UserService userService, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.profileService = profileService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }


    public List<PostDto> getAll(Optional<Long> profileId) {
        if(!profileId.isPresent())
        return Arrays.asList(modelMapper.map(postRepository.findAll(), PostDto[].class));

        return Arrays.asList(modelMapper.map(postRepository.getAllByProfileId(profileId.get()), PostDto[].class));
    }


    public PostDto getById(Long id) {
        Post post = checkAndGetPostById(id);
        return modelMapper.map(post, PostDto.class);
    }


    public PostDto create(PostRequestDto postRequestDto) {
        Profile profile = profileService.checkAndGetProfileById(userService.getCurrentUser().getId());

        Post post = modelMapper.map(postRequestDto, Post.class);
        post.setProfile(profile);
        profile.addPost(post);
        //Gerek yok şimdilik zaten kaydediliyor
        //profileRepository.save(profile);

        return modelMapper.map(postRepository.save(post), PostDto.class);
    }


    public PostDto update(Long id, PostRequestDto postRequestDto) {
        Post post = checkAndGetPostById(id);
        if(postRequestDto.getTitle() != null)
            post.setTitle(postRequestDto.getTitle());
        if(postRequestDto.getText() != null)
            post.setText(postRequestDto.getText());

        return modelMapper.map(postRepository.save(post), PostDto.class);
    }


    public Boolean delete(Long id) {
        Post post = checkAndGetPostById(id);
        postRepository.delete(post);

        return Boolean.TRUE;
    }

    protected Post checkAndGetPostById(Long id){
        Optional<Post> post = postRepository.findById(id);
        if(!post.isPresent())
            throw new NotFoundException("Post", "No post found with this id");
        return post.get();
    }

    protected void checkAndThrowErrorIfPostExists(Long id){
        Optional<Post> post = postRepository.findById(id);
        if(post.isPresent())
            throw new AlreadyExistsException("Post", "A post with this id already exists");
    }


}
