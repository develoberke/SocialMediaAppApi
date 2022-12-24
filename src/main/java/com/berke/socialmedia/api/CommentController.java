package com.berke.socialmedia.api;

import com.berke.socialmedia.dto.CommentDto;
import com.berke.socialmedia.service.CommentService;
import com.berke.socialmedia.util.ApiPaths;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(ApiPaths.CommentCtrl.CTRL)
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @GetMapping
    public ResponseEntity<List<CommentDto>> getAll(@RequestParam Optional<Long> profileId, @RequestParam Optional<Long> postId){
        return ResponseEntity.ok(commentService.getAll(profileId, postId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getById(@PathVariable Long id){
        return ResponseEntity.ok(commentService.getById(id));
    }

    @PostMapping
    public ResponseEntity<CommentDto> create(@RequestBody CommentDto commentDto){
        return new ResponseEntity<>(commentService.create(commentDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentDto> update(@PathVariable Long id, @RequestBody CommentDto commentDto){
        commentService.update(id, commentDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam Long id){
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
