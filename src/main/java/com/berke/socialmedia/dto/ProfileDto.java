package com.berke.socialmedia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private byte[] picture;

    private UserDto user;

    private Long level;

    private Set<ProfileDto> following;

    private Set<ProfileDto> followers;

    private Set<PostDto> posts;

}
