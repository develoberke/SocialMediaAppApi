package com.berke.socialmedia.repository;

import com.berke.socialmedia.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Like getByProfileIdAndPostId(Long profileId, Long postId);

    List<Like> getAllByProfileId(Long profileId);

    List<Like> getAllByPostId(Long postId);
}
