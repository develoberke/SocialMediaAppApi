package com.berke.socialmedia.dao;

import com.berke.socialmedia.dao.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
