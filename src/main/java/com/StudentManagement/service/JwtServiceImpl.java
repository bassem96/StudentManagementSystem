package com.StudentManagement.service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
/*
 * responsible for all the methods for jwt such generate jwt
 * extract any info from token or get some checks from the token*/

@Service
public class JwtServiceImpl implements JwtService {
	@Value("${jwt.secret}")
	private String jwtSecret;

	@Value("${jwt.expiration}")
	private long jwtExpiration;

	// build token
	public String generateToken(UserDetails userDetails) {

		return Jwts.builder().setSubject(userDetails.getUsername()).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
				.signWith(getSignatureKey(), SignatureAlgorithm.HS512).compact();
	}
	// build refresh token
	public String generateRefreshToken(Map<String,Object>Claims,UserDetails userDetails) {

		return Jwts.builder().setClaims(Claims).setSubject(userDetails.getUsername()).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 400000))
				.signWith(getSignatureKey(), SignatureAlgorithm.HS512).compact();
	}

	public String extractUserName(String token) {

		return extractClaim(token, Claims::getSubject);

	}

	// method to extract the username from the token
	// to do so we need to extract claims from the token
	private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
		final Claims claims = extractAllClaims(token);

		return claimsResolvers.apply(claims);

	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignatureKey()).build().parseClaimsJws(token).getBody();
	}

	private Key getSignatureKey() {

		byte[] key = Decoders.BASE64.decode(jwtSecret);// SecretKey as an input
		return Keys.hmacShaKeyFor(key);
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {

		final String username = extractUserName(token);

		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));

	}

	private boolean isTokenExpired(String token) {
		
		return extractClaim(token,Claims::getExpiration).before(new Date());
	}

}
