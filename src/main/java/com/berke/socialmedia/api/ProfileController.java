package com.berke.socialmedia.api;


import com.berke.socialmedia.dto.PostDto;
import com.berke.socialmedia.dto.ProfileDto;
import com.berke.socialmedia.service.ProfileService;
import com.berke.socialmedia.util.ApiPaths;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ProfileDto> update(@PathVariable Long id, @RequestBody ProfileDto profileDto){
        profileService.update(id, profileDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/posts")
    public ResponseEntity<List<PostDto>> getPosts(@PathVariable Long id){
        return ResponseEntity.ok(profileService.getPosts(id));
    }

    @PostMapping("/{id}/posts")
    public ResponseEntity<PostDto> addPost(@PathVariable Long id, @RequestBody PostDto postDto){
        return new ResponseEntity<>(profileService.addPost(id, postDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}/followers")
    public ResponseEntity<List<ProfileDto>> getFollowers(@PathVariable Long id){
        return ResponseEntity.ok(profileService.getFollowers(id));
    }

    @DeleteMapping("/{id}/followers")
    public ResponseEntity<Boolean> removeFollower(@PathVariable Long id, @RequestParam Long profileId){
        profileService.removeFollower(id, profileId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/followings")
    public ResponseEntity<List<ProfileDto>> getFollowings(@PathVariable Long id){
        return ResponseEntity.ok(profileService.getFollowings(id));
    }

    @PutMapping("/{id}/followings")
    public ResponseEntity<Boolean> follow(@PathVariable Long id, @RequestParam Long profileId) {
        profileService.follow(id, profileId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/followings")
    public ResponseEntity<Boolean> unfollow(@PathVariable Long id, @RequestParam Long profileId){
        profileService.unfollow(id, profileId);
        return ResponseEntity.noContent().build();
    }
}
