package com.berke.socialmedia.dao;

import com.berke.socialmedia.dao.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    List<Profile> getProfilesByFollowingId(Long profileId);

    List<Profile> getProfilesByFollowersId(Long profileId);
}
