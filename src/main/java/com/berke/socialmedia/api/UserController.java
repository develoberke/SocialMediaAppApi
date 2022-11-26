package com.berke.socialmedia.api;


import com.berke.socialmedia.dto.UserDto;
import com.berke.socialmedia.service.UserService;
import com.berke.socialmedia.util.ApiPaths;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin

@RestController
@RequestMapping(ApiPaths.UserCtrl.CTRL)
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll(){
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(userService.getById(id));
    }

    @PostMapping
    public ResponseEntity<UserDto> save(@RequestBody UserDto userDto){
        return ResponseEntity.ok(userService.save(userDto));
    }


    @PutMapping("/{id}")
    public ResponseEntity<UserDto> addRoleById(@PathVariable(name = "id") Long userId,
                                               @RequestParam(name = "roleId") Long roleId){
        return ResponseEntity.ok(userService.addRoleById(userId,roleId));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable(name = "id") Long id){
        if(id == null)
            return ResponseEntity.badRequest().build();
        if(userService.delete(id))
            return ResponseEntity.noContent().build();

        return ResponseEntity.notFound().build();

    }
}
