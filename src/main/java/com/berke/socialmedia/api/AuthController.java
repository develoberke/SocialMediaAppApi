package com.berke.socialmedia.api;

import com.berke.socialmedia.dto.JwtResponse;
import com.berke.socialmedia.dto.LoginUserDto;
import com.berke.socialmedia.dto.UserDto;
import com.berke.socialmedia.dto.UserRegisterDto;
import com.berke.socialmedia.security.JwtTokenUtil;
import com.berke.socialmedia.service.UserService;
import com.berke.socialmedia.util.ApiPaths;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPaths.AuthCtrl.CTRL)
public class AuthController {

    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserService userService;

    public AuthController(JwtTokenUtil jwtTokenUtil, AuthenticationManager authenticationManager, UserDetailsService userDetailsService, UserService userService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginUserDto loginUserDto){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUserDto.getUsername(),loginUserDto.getPassword()));

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginUserDto.getUsername());
        final String jwtToken = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(jwtToken, userService.getByUsername(loginUserDto.getUsername())));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserRegisterDto userRegisterDto){
        UserDto userDto = userService.register(userRegisterDto);
        return ResponseEntity.ok(userDto);
    }
}
