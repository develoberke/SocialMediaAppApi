package com.berke.socialmedia.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleDto {

    private Long id;

    private String name;

    private String description;
}
