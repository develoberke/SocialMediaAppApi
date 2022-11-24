package com.berke.socialmedia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostDto {
    private Long id;
    private ProfileDto profileDto;
    private String title;
    private String text;
}
