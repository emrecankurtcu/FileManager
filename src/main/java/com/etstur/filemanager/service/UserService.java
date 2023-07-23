package com.etstur.filemanager.service;

import com.etstur.filemanager.dto.request.LoginRequest;
import com.etstur.filemanager.model.User;
import com.etstur.filemanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;

@Service("userService")
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /***
     * Find user by email and password
     * @param loginRequest LoginRequest
     * @return User
     * @throws AuthenticationException authenticationException
     */
    public User login(LoginRequest loginRequest) throws AuthenticationException {
        User user = userRepository.findByEmail(loginRequest.getEmail());
        if(user == null){
            throw new AuthenticationException("Email or password is invalid.");
        }
        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new AuthenticationException("Email or password is invalid.");
        }
        return user;
    }

    /***
     * Find user by email
     * @param email String
     * @return User
     */
    public User findUserByEmail(String email) {
        User user =userRepository.findByEmail(email);
        if (user == null ) {
            throw new UsernameNotFoundException("Email or password is invalid.");
        }
        return user;
    }

    /***
     * Return authenticated user
     * @return User
     * @throws AuthenticationException authenticationException
     */
    public User getAuthenticatedUser() throws AuthenticationException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new AuthenticationException("User not found.");
        }
        org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) auth.getPrincipal();
        return userRepository.findByEmail(userDetails.getUsername());
    }
}
