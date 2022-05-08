package com.example.authserver.security;

import com.example.authserver.constant.JwtConstant;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.util.Date;

public class JwtUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);

    public static String generateToken(String subject, int validDuration, String userRole, int id) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        JwtBuilder builder = Jwts.builder()
                .setSubject(subject)
                .claim("id",id)
                .claim("role", userRole)
                .setIssuedAt(now)
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(validDuration).toInstant()))
                .signWith(SignatureAlgorithm.HS256, JwtConstant.JWT_SIGNING_KEY);

        return builder.compact();
    }

    public static String getSubjectFromJwt(String token) {
        try {
            String subject = Jwts.parser().setSigningKey(JwtConstant.JWT_SIGNING_KEY)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();

            return subject;
        } catch(SignatureException e) {
            LOGGER.warn("Invalid Jwt Signature");
            return null;
        } catch(ExpiredJwtException e) {
            LOGGER.warn("Expired Jwt");
            return null;
        } catch(Exception e) {
            LOGGER.warn("Exception Parsing Jwt");
            return null;
        }
    }
}
