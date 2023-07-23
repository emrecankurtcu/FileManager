package com.etstur.filemanager.controller;


import com.etstur.filemanager.dto.request.LoginRequest;
import com.etstur.filemanager.dto.response.LoginResponse;
import com.etstur.filemanager.model.User;
import com.etstur.filemanager.other_services.JwtAuthenticationService;
import com.etstur.filemanager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * @param loginRequest LoginRequest
     * @param request HttpServletRequest
     * @return ResponseEntity<LoginResponse>
     */
    @Operation(summary = "Login with email and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Email or password is invalid",
                    content = @Content) })
    @PostMapping(value = "/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) throws AuthenticationException {
        User user = userService.login(loginRequest);
        String token = jwtAuthenticationService.jwtAuthenticateUser(loginRequest,request);
        return ResponseEntity.ok(LoginResponse.builder().firstName(user.getFirstName()).lastName(user.getLastName()).jwt(token).build());
    }
}
