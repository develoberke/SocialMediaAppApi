package com.berke.socialmedia.api;


import com.berke.socialmedia.dto.PrivilegeDto;
import com.berke.socialmedia.service.PrivilegeService;
import com.berke.socialmedia.util.ApiPaths;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin

@RestController
@RequestMapping(ApiPaths.PrivilegeCtrl.CTRL)
public class PrivilegeController {

    private final PrivilegeService privilegeService;

    public PrivilegeController(PrivilegeService privilegeService) {
        this.privilegeService = privilegeService;
    }

    @GetMapping
    public ResponseEntity<List<PrivilegeDto>> getAll(){
        return ResponseEntity.ok(privilegeService.getAll());
    }

    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    @GetMapping("/{id}")
    public ResponseEntity<PrivilegeDto> getById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(privilegeService.getById(id));
    }

    @PostMapping
    public ResponseEntity<PrivilegeDto> save(@RequestBody PrivilegeDto privilegeDto){
        return ResponseEntity.ok(privilegeService.save(privilegeDto));
    }
}
