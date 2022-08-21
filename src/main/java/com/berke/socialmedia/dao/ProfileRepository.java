package com.berke.socialmedia.dao;

import com.berke.socialmedia.dao.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface ProfileRepository extends JpaRepository<Profile, Long> {

}
