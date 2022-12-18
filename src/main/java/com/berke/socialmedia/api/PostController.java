package com.berke.socialmedia.api;

import com.berke.socialmedia.dto.PostDto;
import com.berke.socialmedia.service.PostService;
import com.berke.socialmedia.util.ApiPaths;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(ApiPaths.PostCtrl.CTRL)
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<PostDto>> getAll(@RequestParam Optional<Long> userId){
       return ResponseEntity.ok(postService.getAll(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getById(@PathVariable Long id){
        return ResponseEntity.ok(postService.getById(id));
    }

    @PostMapping
    public ResponseEntity<PostDto> create(@RequestBody PostDto postDto){
        return new ResponseEntity<PostDto>(postService.create(postDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDto> update(@PathVariable Long id, @RequestBody PostDto postDto){
        postService.update(id, postDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id){
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }

   /* @PutMapping("/{id}/likes")
    public ResponseEntity<Void> like(@PathVariable Long postId, @RequestParam Long userId){
        postService.like(postId, userId);
        return ResponseEntity.noContent().build();
   }

    */
}
