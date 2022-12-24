package com.berke.socialmedia.service.impl;

import com.berke.socialmedia.dao.CommentRepository;
import com.berke.socialmedia.dao.PostRepository;
import com.berke.socialmedia.dao.ProfileRepository;
import com.berke.socialmedia.dao.entity.Comment;
import com.berke.socialmedia.dao.entity.Post;
import com.berke.socialmedia.dao.entity.Profile;
import com.berke.socialmedia.dto.CommentDto;
import com.berke.socialmedia.exception.NotFoundException;
import com.berke.socialmedia.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final ModelMapper modelMapper;

    private final PostRepository postRepository;

    private final ProfileRepository profileRepository;

    public CommentServiceImpl(CommentRepository commentRepository, ModelMapper modelMapper, PostRepository postRepository, ProfileRepository profileRepository) {
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
        this.postRepository = postRepository;
        this.profileRepository = profileRepository;
    }


    @Override
    public List<CommentDto> getAll(Optional<Long> profileId, Optional<Long> postId) {
        if(profileId.isPresent() && postId.isPresent())
            return Arrays.asList(modelMapper.map(commentRepository.getByProfileIdAndPostId(profileId.get(), postId.get()), CommentDto[].class));
        else if (profileId.isPresent())
            return Arrays.asList(modelMapper.map(commentRepository.getAllByProfileId(profileId.get()), CommentDto[].class));
        else if (postId.isPresent())
            return Arrays.asList(modelMapper.map(commentRepository.getAllByPostId(postId.get()), CommentDto[].class));

        return Arrays.asList(modelMapper.map(commentRepository.findAll(), CommentDto[].class));
    }

    @Override
    public CommentDto getById(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        if(!comment.isPresent())
            throw new NotFoundException("Comment", "No comment found with this id");
        return modelMapper.map(comment.get(), CommentDto.class);
    }

    @Override
    public CommentDto create(CommentDto commentDto) {
        Comment comment = modelMapper.map(commentDto, Comment.class);
        comment.setId(0L);

        Optional<Profile> profile = profileRepository.findById(commentDto.getProfileId());
        if(!profile.isPresent())
            throw new NotFoundException("Profile", "No profile found with this id");
        Optional<Post> post = postRepository.findById(commentDto.getPostId());
        if(!post.isPresent())
            throw new NotFoundException("Post", "No post found with this id");

        comment.setProfile(profile.get());
        comment.setPost(post.get());

        comment = commentRepository.save(comment);
        commentDto.setId(comment.getId());
        return commentDto;
    }

    @Override
    public void update(Long id, CommentDto commentDto) {
        Optional<Comment> comment = commentRepository.findById(id);
        if(!comment.isPresent())
            throw new NotFoundException("Comment", "No comment found with this id");

        if(commentDto.getContent() != null)
            comment.get().setContent(commentDto.getContent());

        commentRepository.save(comment.get());
    }

    @Override
    public void delete(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        if(!comment.isPresent())
            throw new NotFoundException("Comment", "No comment found with this id");

        commentRepository.delete(comment.get());
    }
}
