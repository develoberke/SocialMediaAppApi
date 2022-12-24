package com.berke.socialmedia.service;

import com.berke.socialmedia.dto.LikeDto;

import java.util.List;
import java.util.Optional;

public interface LikeService {

    List<LikeDto> getAll(Optional<Long> profileId, Optional<Long> postId);

    LikeDto getById(Long id);

    LikeDto create(Long profileId, Long postId);

    void delete(Long profileId, Long postId);
}
