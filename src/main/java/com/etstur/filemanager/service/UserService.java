package com.etstur.filemanager.service;

import com.etstur.filemanager.dto.request.LoginRequestDTO;
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
     * @param loginRequestDTO email and password
     * @return User
     * @throws AuthenticationException
     */
    public User login(LoginRequestDTO loginRequestDTO) throws AuthenticationException {
        User user = userRepository.getUserByEmail(loginRequestDTO.getEmail());
        if(user == null){
            throw new AuthenticationException("Email or password is invalid.");
        }
        if(!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())){
            throw new AuthenticationException("Email or password is invalid.");
        }
        return user;
    }

    /***
     * Find user by email
     * @param email User email
     * @return User
     */
    public User findUserByEmail(String email) {
        User user =userRepository.getUserByEmail(email);

        if (user == null ) {
            throw new UsernameNotFoundException("Email or password is invalid.");
        }
        return user;
    }
}
