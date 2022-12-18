package com.berke.socialmedia.service;

import com.berke.socialmedia.dto.PostDto;

import java.util.List;
import java.util.Optional;

public interface PostService {

    List<PostDto> getAll(Optional<Long> userId);

    PostDto getById(Long id);

    PostDto create(PostDto postDto);

    PostDto update(Long id, PostDto postDto);

    Boolean delete(Long id);


}
