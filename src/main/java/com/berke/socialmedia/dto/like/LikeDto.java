package com.berke.socialmedia.dto.like;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LikeDto {
    private Long id;

    private Long postId;

    private Long profileId;
}
