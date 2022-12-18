package com.berke.socialmedia.service;

import com.berke.socialmedia.dto.PostDto;
import com.berke.socialmedia.dto.ProfileDto;

import java.util.List;

public interface ProfileService {

    List<ProfileDto> getAll();

    ProfileDto getById(Long id);
    ProfileDto update(Long id, ProfileDto profileDto);

    List<ProfileDto> getFollowers(Long id);

    Boolean removeFollower(Long id, Long profileId);

    List<ProfileDto> getFollowings(Long id);

    Boolean follow(Long id, Long followUserId);

    Boolean unfollow(Long id, Long unfollowUserId);
}
