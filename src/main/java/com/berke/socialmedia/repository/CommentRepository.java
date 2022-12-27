package com.berke.socialmedia.repository;

import com.berke.socialmedia.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> getByProfileIdAndPostId(Long profileId, Long postId);

    List<Comment> getAllByProfileId(Long profileId);

    List<Comment> getAllByPostId(Long postId);

}
