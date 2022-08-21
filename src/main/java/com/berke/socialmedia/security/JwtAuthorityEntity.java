package com.berke.socialmedia.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthorityEntity {

    private String role;
    private List<String> privileges;
}
