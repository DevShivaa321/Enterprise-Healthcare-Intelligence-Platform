package com.shivam.ehip.login;

import org.springframework.stereotype.Service;

import com.shivam.ehip.login.User;
import com.shivam.ehip.login.UserRepository;

@Service
public class CustomUserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User loadUserByLoginName(String loginName) {
        return userRepository.getUserByLoginName(loginName);
    }
}
