package com.hzp.test.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(JwtInfo jwtInfo) {
        String token = Jwts.builder()
                .claim("userId", jwtInfo.getUserId())
                .claim("username", jwtInfo.getUsername())
                .setId(UUID.randomUUID().toString())
                .setSubject(jwtInfo.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(null)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
        return token;
    }

    public JwtInfo validateToken(String token) {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
        Claims body = claimsJws.getBody();
        JwtInfo jwtInfo = new JwtInfo();
        jwtInfo.setUserId(Long.parseLong(String.valueOf(body.get("userId"))));
        jwtInfo.setUsername(String.valueOf(body.get("username")));
        return jwtInfo;
    }
}
