package com.etstur.filemanager.other_services;

import com.etstur.filemanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;

    /***
     * Load user by username
     * @param username String
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.etstur.filemanager.model.User user =userService.findUserByEmail(username);

        if (user == null ) {
            throw new UsernameNotFoundException("Email or password is invalid.");
        }

        return User.withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities("USER").build();
    }
}