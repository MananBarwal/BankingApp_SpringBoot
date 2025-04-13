package com.project.bankingApp.Security.jwt;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private String Secret_key_static="This_is_my_secret_key_9March2025_1:02PM";
	private SecretKey Secret_key = Keys.hmacShaKeyFor(Secret_key_static.getBytes(StandardCharsets.UTF_8)); 

	private long AccessTokenTime = 5 * 60 * 1000; // 5 minutes. Milliseconds is used
	private long RefreshTokenTime = 10 * 60 * 60 * 1000; // 10 mins
	
	public String generateToken(UserDetails userDetails, long expiration) {
		List <String> Roles = new ArrayList<>();
		for (GrantedAuthority ga : userDetails.getAuthorities()) {
			Roles.add(ga.getAuthority()); //Get roles in string instead of GrantedAuthority
		}
		
		return Jwts.builder()
				.setSubject(userDetails.getUsername())
				.claim("role", Roles)
				.setIssuer("Banking App Manan")
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(Secret_key, SignatureAlgorithm.HS256)
				.compact(); //Creates the Token
	}

	public String generateAccessToken(UserDetails userDetails) {
		return generateToken(userDetails, AccessTokenTime);
	}

	public String generateRefreshToken(UserDetails userDetails) {
		return generateToken(userDetails, RefreshTokenTime);
	}
	
	public String getUsernamefromToken(String Token) {
		return Jwts.parserBuilder()
				.setSigningKey(Secret_key)
				.build()
				.parseClaimsJws(Token)
				.getBody()
				.getSubject();
	}
	
	public boolean isExpired(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(Secret_key)
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getExpiration().before(new Date());
	}
	
	/*
	 * public Date extractExpiration(String token) { return Jwts.parserBuilder()
	 * .setSigningKey(Secret_key) .build() .parseClaimsJws(token) .getBody()
	 * .getExpiration(); }
	 */
	public boolean isValidToken(String token, UserDetails user){
		return (getUsernamefromToken(token).equals(user.getUsername())&& !isExpired(token));
	}

}
