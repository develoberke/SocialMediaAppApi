package com.berke.socialmedia.api;


import com.berke.socialmedia.dto.RoleDto;
import com.berke.socialmedia.service.RoleService;
import com.berke.socialmedia.util.ApiPaths;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin

@RestController
@RequestMapping(ApiPaths.RoleCtrl.CTRL)
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
        return ResponseEntity.ok(roleService.save(roleDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDto> addPrivilegeById(@PathVariable(name = "id") Long roleId,
                                                    @RequestParam(name = "privilegeId") Long privilegeId){
        return ResponseEntity.ok(roleService.addPrivilegeById(roleId,privilegeId));
    }

}
