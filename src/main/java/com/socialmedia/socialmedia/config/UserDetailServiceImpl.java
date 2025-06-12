package com.socialmedia.socialmedia.config;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.socialmedia.socialmedia.service.UserService;

import org.springframework.security.core.userdetails.User;

import java.util.Collections;
public class UserDetailServiceImpl implements UserDetailsService{
    private UserService userService;
    public UserDetailServiceImpl(UserService userService){
        this.userService = userService;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.socialmedia.socialmedia.domain.User user = this.userService.getUserByUserName(username);
        if(user == null){
            throw new UsernameNotFoundException("Username/Password not valid");
        }
        User user_detail = new User(
            user.getEmail(),
            user.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));
        return user_detail;
    }
}
