package com.dwbh.backend.common.util;

import com.dwbh.backend.service.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class JwtUtil {

    private final Key key;
    private final UserService userService;

    public JwtUtil(
            @Value("${token.secret}") String secretKey,
            UserService userSerivce
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.userService = userSerivce;
    }

    public boolean validateToken(String token) {

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token {}", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token {}", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token {}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT Token claims empty {}", e);
        }

        return false;
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userService.loadUserByUsername(this.getUserId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String getUserId(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * 매개변수를 받아 토큰을 반환합니다.
     * response의 header에 추가하여 반환해야 할 수 있습니다.
     *
     * @param subject 주제
     * @param claims 값
     * @param exp 만료 시간(ms)
     * @return 완성한 토큰
     */
    public String createCustomToken(String subject, Map<String, String> claims, long exp) {

        Claims newClaims = Jwts.claims().setSubject(subject);
        if (claims != null) newClaims.putAll(claims);

        return Jwts.builder().setClaims(newClaims).setExpiration(
                        new Date(System.currentTimeMillis() + exp)
                ).signWith(key, SignatureAlgorithm.HS512).compact();
    }
}
