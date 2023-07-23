package com.etstur.filemanager.controller;


import com.etstur.filemanager.dto.request.LoginRequestDTO;
import com.etstur.filemanager.dto.response.LoginResponseDTO;
import com.etstur.filemanager.dto.response.MessageResponseDTO;
import com.etstur.filemanager.model.User;
import com.etstur.filemanager.other_services.JwtAuthenticationService;
import com.etstur.filemanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class LoginController {
    private final UserService userService;
    private final JwtAuthenticationService jwtAuthenticationService;

    /***
     * Find user by email,password and return user info and jwt
     * @param loginRequestDTO
     * @param request
     * @return ResponseEntity<LoginResponseDTO>
     */
    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletRequest request) {
        try {
            User user = userService.login(loginRequestDTO);
            String token = jwtAuthenticationService.jwtAuthenticateUser(loginRequestDTO,request);
            return ResponseEntity.ok(LoginResponseDTO.builder().firstName(user.getFirstName()).lastName(user.getLastName()).jwt(token).build());

        } catch (AuthenticationException exception) {
            return ResponseEntity.badRequest().body(MessageResponseDTO.builder().message(exception.getMessage()).build());
        }
    }
}
