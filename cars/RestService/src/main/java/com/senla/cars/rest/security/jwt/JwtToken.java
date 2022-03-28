package com.senla.cars.rest.security.jwt;

import com.senla.cars.serviceImpl.service.security.SecurityUserDetailsService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtToken {
    @Value("${spring.auth.jwt.secure_key}")
    private String secret;
    @Value("${spring.auth.jwt.validity-in-milliseconds}")
    private long validityInMilliseconds;

    private final SecurityUserDetailsService securityUserDetailsService;

    public String createToken(String email) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        return Jwts
                .builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(signingKey, signatureAlgorithm)
                .compact();
    }

    public String getEmail(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(DatatypeConverter.parseBase64Binary(secret))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(DatatypeConverter.parseBase64Binary(secret)).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expiredJwtException) {
            log.error("Token expired");
        } catch (UnsupportedJwtException unsupportedJwtException) {
            log.error("Unsupported app.jwt");
        } catch (MalformedJwtException malformedJwtException) {
            log.error("Malformed app.jwt");
        } catch (Exception exception) {
            log.error("Invalid token");
        }
        return false;
    }

    public Authentication getAuthentication(String token){
        UserDetails userDetails = this.securityUserDetailsService.loadUserByUsername(getEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails,"", userDetails.getAuthorities());
    }
}

