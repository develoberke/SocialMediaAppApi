package com.berke.socialmedia.dao;

import com.berke.socialmedia.dao.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> getAllByProfileId(Long id);
}
