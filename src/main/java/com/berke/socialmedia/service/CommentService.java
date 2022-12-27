package com.berke.socialmedia.service;

import com.berke.socialmedia.repository.CommentRepository;
import com.berke.socialmedia.entity.Comment;
import com.berke.socialmedia.entity.Post;
import com.berke.socialmedia.entity.Profile;
import com.berke.socialmedia.dto.comment.CommentDto;
import com.berke.socialmedia.dto.comment.CommentRequestDto;
import com.berke.socialmedia.exception.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private final ModelMapper modelMapper;

    private final UserService userService;

    private final PostService postService;

    private final ProfileService profileService;



    public CommentService(CommentRepository commentRepository, ModelMapper modelMapper, UserService userService, PostService postService, ProfileService profileService) {
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
        this.postService = postService;
        this.profileService = profileService;
        this.userService = userService;
    }


    public List<CommentDto> getAll(Optional<Long> profileId, Optional<Long> postId) {
        if(profileId.isPresent() && postId.isPresent())
            return Arrays.asList(modelMapper.map(commentRepository.getByProfileIdAndPostId(profileId.get(), postId.get()), CommentDto[].class));
        else if (profileId.isPresent())
            return Arrays.asList(modelMapper.map(commentRepository.getAllByProfileId(profileId.get()), CommentDto[].class));
        else if (postId.isPresent())
            return Arrays.asList(modelMapper.map(commentRepository.getAllByPostId(postId.get()), CommentDto[].class));

        return Arrays.asList(modelMapper.map(commentRepository.findAll(), CommentDto[].class));
    }

    public CommentDto getById(Long id) {
        Comment comment = checkAndGetCommentById(id);
        return modelMapper.map(comment, CommentDto.class);
    }


    public CommentDto create(CommentRequestDto commentRequestDto) {
        Comment comment = modelMapper.map(commentRequestDto, Comment.class);
        comment.setId(null);

        Profile profile = profileService.checkAndGetProfileById(userService.getCurrentUser().getId());
        Post post = postService.checkAndGetPostById(commentRequestDto.getPostId());

        comment.setProfile(profile);
        comment.setPost(post);

        comment = commentRepository.save(comment);
        return modelMapper.map(comment, CommentDto.class);
    }


    public void update(Long id, CommentRequestDto commentRequestDto) {
        Comment comment = checkAndGetCommentById(id);

        if(commentRequestDto.getContent() != null)
            comment.setContent(commentRequestDto.getContent());

        commentRepository.save(comment);
    }


    public void delete(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        if(!comment.isPresent())
            throw new NotFoundException("Comment", "No comment found with this id");

        commentRepository.delete(comment.get());
    }

    protected Comment checkAndGetCommentById(Long id){
        Optional<Comment> comment = commentRepository.findById(id);
        if(!comment.isPresent())
            throw new NotFoundException("Comment", "No comment found with this id");
        return comment.get();
    }
}
