package com.berke.socialmedia.api;


import com.berke.socialmedia.dto.RoleDto;
import com.berke.socialmedia.service.RoleService;
import com.berke.socialmedia.util.ApiPaths;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin

@RestController
@RequestMapping(ApiPaths.RoleCtrl.CTRL)
@SecurityRequirement(name = "Bearer Authentication")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<RoleDto>> getAll(){
        return ResponseEntity.ok(roleService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> getById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(roleService.getById(id));
    }

    @PostMapping
    public ResponseEntity<RoleDto> save(@RequestBody RoleDto roleDto){
        return new ResponseEntity<>(roleService.save(roleDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDto> update(@PathVariable Long id, @RequestBody RoleDto roleDto){
        roleService.update(id,roleDto);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{id}/privileges")
    public ResponseEntity<RoleDto> addPrivilegeById(@PathVariable(name = "id") Long roleId,
                                                    @RequestParam(name = "privilegeId") Long privilegeId){
        roleService.addPrivilegeById(roleId,privilegeId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}/privileges")
    public ResponseEntity<RoleDto> removePrivilegeById(@PathVariable Long id, @RequestParam Long privilegeId){
        roleService.removePrivilegeById(id, privilegeId);
        return ResponseEntity.noContent().build();
    }


}
