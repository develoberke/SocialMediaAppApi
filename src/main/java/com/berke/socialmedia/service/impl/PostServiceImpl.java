package com.berke.socialmedia.service.impl;

import com.berke.socialmedia.dao.PostRepository;
import com.berke.socialmedia.dao.ProfileRepository;
import com.berke.socialmedia.dao.entity.Post;
import com.berke.socialmedia.dao.entity.Profile;
import com.berke.socialmedia.dto.PostDto;
import com.berke.socialmedia.exception.AlreadyExistsException;
import com.berke.socialmedia.exception.NotFoundException;
import com.berke.socialmedia.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final ProfileRepository profileRepository;

    private final ModelMapper modelMapper;

    public PostServiceImpl(PostRepository postRepository, ProfileRepository profileRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.profileRepository = profileRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<PostDto> getAll(Optional<Long> profileId) {
        if(!profileId.isPresent())
        return Arrays.asList(modelMapper.map(postRepository.findAll(), PostDto[].class));

        return Arrays.asList(modelMapper.map(postRepository.getAllByProfileId(profileId.get()), PostDto[].class));
    }

    @Override
    public PostDto getById(Long id) {
        Post post = checkAndGetPostById(id);
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public PostDto create(PostDto postDto) {
        Profile profile = checkAndGetProfileById(postDto.getProfileId());

        Post post = modelMapper.map(postDto, Post.class);
        post.setId(0L);
        post.setProfile(profile);
        profile.addPost(post);
        profileRepository.save(profile);

        return modelMapper.map(postRepository.save(post), PostDto.class);
    }


    @Override
    public PostDto update(Long id, PostDto postDto) {
        Post post = checkAndGetPostById(id);
        if(postDto.getTitle() != null)
            post.setTitle(postDto.getTitle());
        if(postDto.getText() != null)
            post.setText(postDto.getText());

        return modelMapper.map(postRepository.save(post), PostDto.class);
    }

    @Override
    public Boolean delete(Long id) {
        Post post = checkAndGetPostById(id);
        postRepository.delete(post);

        return Boolean.TRUE;
    }

    private Post checkAndGetPostById(Long id){
        Optional<Post> post = postRepository.findById(id);
        if(!post.isPresent())
            throw new NotFoundException("Post", "No post found with this id");
        return post.get();
    }

    private void checkAndThrowErrorIfPostExists(Long id){
        Optional<Post> post = postRepository.findById(id);
        if(post.isPresent())
            throw new AlreadyExistsException("Post", "A post with this id already exists");
    }

    private Profile checkAndGetProfileById(Long id){
        Optional<Profile> profile = profileRepository.findById(id);
        if(!profile.isPresent())
            throw new NotFoundException("Profile", "No profile found with this id");
        return profile.get();
    }

}
