package com.berke.socialmedia.api;

import com.berke.socialmedia.dto.PostDto;
import com.berke.socialmedia.service.PostService;
import com.berke.socialmedia.util.ApiPaths;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.PostCtrl.CTRL)
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public List<PostDto> getAll(){
       return postService.getAll();
    }

    @GetMapping("/{id}")
    public PostDto getById(@PathVariable Long id){
        return postService.getById(id);
    }

    @PostMapping
    public PostDto create(@RequestBody PostDto postDto){
        return postService.create(postDto);
    }

    @PutMapping("/{id}")
    public PostDto update(@PathVariable Long id, @RequestBody PostDto postDto){
        return postService.update(id, postDto);
    }

    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Long id){
        return postService.delete(id);
    }
}
