package com.berke.socialmedia.service;

import com.berke.socialmedia.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    public List<CommentDto> getAll(Optional<Long> profileId, Optional<Long> postId);

    public CommentDto getById(Long id);

    public CommentDto create(CommentDto commentDto);

    public void update(Long id, CommentDto commentDto);

    public void delete(Long id);
}
