package com.berke.socialmedia.api;


import com.berke.socialmedia.dto.ProfileDto;
import com.berke.socialmedia.service.ProfileService;
import com.berke.socialmedia.util.ApiPaths;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.ProfileCtrl.CTRL)
@SecurityRequirement(name = "Bearer Authentication")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public List<ProfileDto> getAll(){
        return profileService.getAll();
    }

    @GetMapping("/{id}")
    public ProfileDto getById(@PathVariable(value = "id") Long id){
        return profileService.getById(id);
    }
    @PutMapping("/{id}")
    public ProfileDto update(@PathVariable Long id, @RequestBody ProfileDto profileDto){
        return profileService.update(id, profileDto);
    }
}
