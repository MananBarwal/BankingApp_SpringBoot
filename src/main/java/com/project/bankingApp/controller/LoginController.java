package com.project.bankingApp.controller;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.interfaces.Claim;
import com.project.bankingApp.Security.Service.CustomUserDetailsService;
import com.project.bankingApp.Security.jwt.JwtUtil;
import com.project.bankingApp.dto.AccountDto;
import com.project.bankingApp.dto.LoginDto;
import com.project.bankingApp.service.AccountService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;

@RestController
@Data
public class LoginController {

	private final JwtUtil jwtUtil;
	private final AuthenticationManager authenticationManager;
	private final AccountService a;
	private final CustomUserDetailsService cds;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginDto login, HttpServletResponse response) {
		// Authenticate user
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(login.getAccNumber(), login.getPassword()));

		long l = login.getAccNumber();

		String s = Long.toString(l);
		// Load user details
		UserDetails userDetails = cds.loadUserByUsername(s);

		// Generate tokens
		String accessToken = jwtUtil.generateAccessToken(userDetails);
		String refreshToken = jwtUtil.generateRefreshToken(userDetails);

		int accessTokenMaxAge = (5 * 60);
		int refreshTokenMaxAge = (10 * 60*60);

		// Set HTTP-only Secure Cookies
		Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
		accessTokenCookie.setHttpOnly(true);
		accessTokenCookie.setSecure(true); // Ensure it's sent over HTTPS
		accessTokenCookie.setPath("/");
		accessTokenCookie.setMaxAge(accessTokenMaxAge);

		Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
		refreshTokenCookie.setHttpOnly(true);
		refreshTokenCookie.setSecure(true);
		refreshTokenCookie.setPath("/refresh");
		refreshTokenCookie.setMaxAge(refreshTokenMaxAge);

		response.addCookie(accessTokenCookie);
		response.addCookie(refreshTokenCookie);

		// Return only user information, not tokens
		return ResponseEntity.ok("Login successful");
	}

	@PostMapping("/signup")
	public ResponseEntity<AccountDto> createAccount (@RequestBody AccountDto dto) {
		AccountDto createdAccount =a.saveAccount(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
	}

@PostMapping("/refresh")
public ResponseEntity<?> refreshToken(HttpServletResponse response, @CookieValue(name = "refreshToken", required = false) String refreshToken) {
    if (refreshToken == null || jwtUtil.isExpired(refreshToken)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token is missing or expired");
    }

    String username = jwtUtil.getUsernamefromToken(refreshToken);
    UserDetails userDetails = cds.loadUserByUsername(username);

    if (!jwtUtil.isValidToken(refreshToken, userDetails)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
    }

    //Generate a new access token
    String newAccessToken = jwtUtil.generateAccessToken(userDetails);

    //generate a new refresh token
    String newRefreshToken = jwtUtil.generateRefreshToken(userDetails);

    int accessTokenMaxAge = (1 * 60); // in seconds
    Cookie newAccessTokenCookie = new Cookie("accessToken", newAccessToken);
    newAccessTokenCookie.setHttpOnly(true);
    newAccessTokenCookie.setSecure(true);
    newAccessTokenCookie.setPath("/");
    newAccessTokenCookie.setMaxAge(accessTokenMaxAge); 
    
    Cookie refreshTokenCookie = new Cookie("refreshToken", newRefreshToken);
    refreshTokenCookie.setHttpOnly(true);
    refreshTokenCookie.setSecure(true);
    refreshTokenCookie.setPath("/refresh");
    refreshTokenCookie.setMaxAge(24 * 60 *60); 

    response.addCookie(newAccessTokenCookie);
    response.addCookie(refreshTokenCookie);

    return ResponseEntity.ok("Tokens refreshed");
    }}
