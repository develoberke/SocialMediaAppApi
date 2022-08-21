package com.berke.socialmedia.service;

import com.berke.socialmedia.dao.entity.Profile;
import com.berke.socialmedia.dto.ProfileDto;

public interface ProfileService {

    ProfileDto save(ProfileDto profileDto);
}
