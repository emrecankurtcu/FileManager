package com.etstur.filemanager.filter;


import com.etstur.filemanager.other_services.JwtAuthenticationService;
import com.etstur.filemanager.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
	private final JwtAuthenticationService jwtAuthenticationService;
	private final JwtUtil jwtUtil;
	private final Logger logger = LogManager.getLogger(this.getClass());

	/***
	 * Handle all http requests and check Authorization header
	 * @param request
	 * @param response
	 * @param filterChain
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String uri=request.getRequestURI();
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("utf-8");
		if (!uri.startsWith("/api/")){
			filterChain.doFilter(request, response);
			return;
		}
		else if (uri.equals("/api/v1/login")) {
			filterChain.doFilter(request, response);
			return;
		}

		String requestTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		String username;
		String jwt;

		if (requestTokenHeader != null) {
			if(requestTokenHeader.startsWith(JwtUtil.TOKEN_PREFIX)){
				jwt = requestTokenHeader.substring(7);
				try {
					username = jwtUtil.getUsernameFromToken(jwt);
					if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
						jwtAuthenticationService.jwtValidate(username,jwt,request);
					}
				}
				catch (IllegalArgumentException | SignatureException | ExpiredJwtException e) {
					logger.warn("Token expired - "+uri);
					response.setStatus(HttpStatus.UNAUTHORIZED.value());
					return;
				}  catch (Exception e) {
					logger.error(e.getMessage()+" - "+uri);
					response.setStatus(HttpStatus.UNAUTHORIZED.value());
					return;
				}
			}
			else{
				logger.error("Token does not start with Bearer String - "+uri);
				response.setStatus(HttpStatus.UNAUTHORIZED.value());
				return;
			}
		} else {
			logger.error("Token cannot be empty - "+uri);
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			return;
		}
		filterChain.doFilter(request, response);
	}


}
