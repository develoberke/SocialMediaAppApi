package com.berke.socialmedia.repository;

import com.berke.socialmedia.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface UserRepository extends JpaRepository<User,Long> {

    public User findByUsername(String username);
}
