package com.berke.socialmedia.api;


import com.berke.socialmedia.dto.profile.ProfileDto;
import com.berke.socialmedia.dto.profile.ProfileRequestDto;
import com.berke.socialmedia.service.ProfileService;
import com.berke.socialmedia.util.ApiPaths;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<ProfileDto>> getAll(){
        return ResponseEntity.ok(profileService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileDto> getById(@PathVariable(value = "id") Long id){
        return ResponseEntity.ok(profileService.getById(id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProfileDto> update(@PathVariable Long id, @RequestBody ProfileRequestDto profileRequestDto){
        profileService.update(id, profileRequestDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/followers")
    public ResponseEntity<List<ProfileDto>> getFollowers(@PathVariable Long id){
        return ResponseEntity.ok(profileService.getFollowers(id));
    }

    @DeleteMapping("/followers")
    public ResponseEntity<Boolean> removeFollower(@RequestParam Long profileId){
        profileService.removeFollower(profileId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/followings")
    public ResponseEntity<List<ProfileDto>> getFollowings(@PathVariable Long id){
        return ResponseEntity.ok(profileService.getFollowings(id));
    }

    @PutMapping("/followings")
    public ResponseEntity<Boolean> follow(@RequestParam Long profileId) {
        profileService.follow(profileId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/followings")
    public ResponseEntity<Boolean> unfollow(@RequestParam Long profileId){
        profileService.unfollow(profileId);
        return ResponseEntity.noContent().build();
    }
}
