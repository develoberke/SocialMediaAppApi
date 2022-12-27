package com.berke.socialmedia.dto.profile;

import com.berke.socialmedia.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileRequestDto {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private byte[] picture;
    private Long level;
}
