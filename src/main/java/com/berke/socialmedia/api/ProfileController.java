package com.berke.socialmedia.api;


import com.berke.socialmedia.dao.entity.Profile;
import com.berke.socialmedia.dto.ProfileDto;
import com.berke.socialmedia.service.ProfileService;
import com.berke.socialmedia.util.ApiPaths;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.ProfileCtrl.CTRL)
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public List<ProfileDto> getAll(){
        return profileService.getAll();
    }

    @PutMapping("/{id}")
    public ProfileDto update(@PathVariable Long id, @RequestBody ProfileDto profileDto){
        return profileService.update(id, profileDto);
    }
}
