package com.etstur.filemanager.other_services;


import com.etstur.filemanager.dto.request.LoginRequestDTO;
import com.etstur.filemanager.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;


@Service
@RequiredArgsConstructor
public class JwtAuthenticationService {
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    /***
     * Jwt validate and set authentication
     * @param email
     * @param jwt
     * @param request
     */
    public void jwtValidate(String email, String jwt, HttpServletRequest request) {

        if (jwt.startsWith(JwtUtil.TOKEN_PREFIX)) {
            jwt = jwt.replaceAll(JwtUtil.TOKEN_PREFIX, "");
        }
        if (email == null) {
            email = jwtUtil.getUsernameFromToken(jwt);
        }
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

            if (jwtUtil.validateToken(jwt, email)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }

        }

    }

    /***
     * Create auth and return jwt
     * @param loginRequestDTO
     * @param request
     * @return jwt
     * @throws AuthenticationException
     */
    public String jwtAuthenticateUser(LoginRequestDTO loginRequestDTO, HttpServletRequest request) throws AuthenticationException {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequestDTO.getEmail());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        return jwtUtil.generateToken(userDetails);
    }

}
