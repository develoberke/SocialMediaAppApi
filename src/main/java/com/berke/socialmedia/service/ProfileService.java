package com.berke.socialmedia.service;

import com.berke.socialmedia.dao.entity.Profile;
import com.berke.socialmedia.dto.ProfileDto;

import java.util.List;

public interface ProfileService {

    List<ProfileDto> getAll();

    ProfileDto update(Long id, ProfileDto profileDto);
}
