package com.berke.socialmedia.service.impl;

import com.berke.socialmedia.dao.PostRepository;
import com.berke.socialmedia.dao.ProfileRepository;
import com.berke.socialmedia.dao.entity.Post;
import com.berke.socialmedia.dao.entity.Profile;
import com.berke.socialmedia.dto.PostDto;
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
    public List<PostDto> getAll() {
        return Arrays.asList(modelMapper.map(postRepository.findAll(), PostDto[].class));
    }

    @Override
    public PostDto getById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if (!post.isPresent())
            throw new IllegalArgumentException("Post not found");
        return modelMapper.map(post.get(), PostDto.class);
    }

    @Override
    public PostDto create(PostDto postDto) {
        Optional<Post> post = postRepository.findById(postDto.getId());
        Optional<Profile> profile = profileRepository.findById(postDto.getProfile().getId());
        if (!profile.isPresent())
            throw new IllegalArgumentException("User not found");
        if(post.isPresent())
            throw new IllegalArgumentException("Id is already used");

        Post postToSave = modelMapper.map(postDto, Post.class);
        postToSave.setProfile(profile.get());
        profile.get().addPost(postToSave);
        profileRepository.save(profile.get());
        return modelMapper.map(postRepository.save(postToSave), PostDto.class);
    }

    @Override
    public PostDto update(Long id, PostDto postDto) {
        Optional<Post> post = postRepository.findById(id);
        if(!post.isPresent())
            throw new IllegalArgumentException("Post not found");
        if(postDto.getTitle() != null)
            post.get().setTitle(postDto.getTitle());
        if(postDto.getText() != null)
            post.get().setText(postDto.getText());


        return modelMapper.map(postRepository.save(post.get()), PostDto.class);

    }

    @Override
    public Boolean delete(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if(!post.isPresent())
            throw new IllegalArgumentException("Post not found");
        postRepository.delete(post.get());
        return Boolean.TRUE;
    }
}
