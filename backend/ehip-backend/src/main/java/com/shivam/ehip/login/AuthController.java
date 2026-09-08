package com.shivam.ehip.login;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shivam.ehip.login.AuthRequestDTO;
import com.shivam.ehip.login.AuthResponseDTO;
import com.shivam.ehip.security.JwtUtil;
import com.shivam.ehip.login.CustomUserDetailsService;
import com.shivam.ehip.login.User;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;

    public AuthController(CustomUserDetailsService customUserDetailsService, JwtUtil jwtUtil) {
        this.customUserDetailsService=customUserDetailsService;
        this.jwtUtil=jwtUtil;
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody AuthRequestDTO request) {

        User user= customUserDetailsService.loadUserByLoginName(request.getLoginName());

        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        if( !user.getPassword().equals(request.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getLoginName(), user.getRole());

        return new AuthResponseDTO(token);
    }
}
