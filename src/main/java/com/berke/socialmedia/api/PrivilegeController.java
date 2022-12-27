package com.berke.socialmedia.api;


import com.berke.socialmedia.dto.privilege.PrivilegeDto;
import com.berke.socialmedia.dto.privilege.PrivilegeRequestDto;
import com.berke.socialmedia.service.PrivilegeService;
import com.berke.socialmedia.util.ApiPaths;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin

@RestController
@RequestMapping(ApiPaths.PrivilegeCtrl.CTRL)
@SecurityRequirement(name = "Bearer Authentication")
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
    public ResponseEntity<PrivilegeDto> save(@RequestBody PrivilegeRequestDto privilegeRequestDto){
        return new ResponseEntity<>(privilegeService.save(privilegeRequestDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public  ResponseEntity<PrivilegeDto> update(@PathVariable Long id, @RequestBody PrivilegeRequestDto privilegeRequestDto){
        privilegeService.update(id, privilegeRequestDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id){
        privilegeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
