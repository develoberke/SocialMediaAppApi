package com.berke.socialmedia.api;

import com.berke.socialmedia.dto.like.LikeDto;
import com.berke.socialmedia.service.LikeService;
import com.berke.socialmedia.util.ApiPaths;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(ApiPaths.LikeCtrl.CTRL)
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @GetMapping
    public ResponseEntity<List<LikeDto>> getAll(@RequestParam Optional<Long> profileId, @RequestParam Optional<Long> postId){
        return ResponseEntity.ok(likeService.getAll(profileId, postId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LikeDto> getById(@PathVariable Long id){
        return ResponseEntity.ok(likeService.getById(id));
    }

    @PostMapping
    public ResponseEntity<LikeDto> create(@RequestParam Long postId){
        return new ResponseEntity<>(likeService.create( postId), HttpStatus.CREATED);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(@PathVariable Long postId){
        likeService.delete(postId);
        return ResponseEntity.noContent().build();
    }
}
