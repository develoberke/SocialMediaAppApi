package com.berke.socialmedia.dao;

import com.berke.socialmedia.dao.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
