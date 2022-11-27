package com.berke.socialmedia.api;


import com.berke.socialmedia.dto.PostDto;
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

    @GetMapping("/{id}/posts")
    public List<PostDto> getPosts(@PathVariable Long id){
        return profileService.getPosts(id);
    }

    @PostMapping("/{id}/posts")
    public PostDto addPost(@PathVariable Long id, @RequestBody PostDto postDto){
        return profileService.addPost(id, postDto);
    }

    @GetMapping("/{id}/followers")
    public List<ProfileDto> getFollowers(@PathVariable Long id){
        return profileService.getFollowers(id);
    }

    @DeleteMapping("/{id}/followers")
    public Boolean removeFollower(@PathVariable Long id, @RequestParam Long profileId){
        return profileService.removeFollower(id, profileId);
    }

    @GetMapping("/{id}/followings")
    public List<ProfileDto> getFollowings(@PathVariable Long id){
        return profileService.getFollowings(id);
    }

    @PostMapping("/{id}/followings")
    public Boolean follow(@PathVariable Long id, @RequestParam Long profileId) {
        return profileService.follow(id, profileId);
    }

    @DeleteMapping("/{id}/followings")
    public Boolean unfollow(@PathVariable Long id, @RequestParam Long profileId){
        return profileService.unfollow(id, profileId);
    }
}
