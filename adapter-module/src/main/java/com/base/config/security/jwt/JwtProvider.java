package com.base.config.security.jwt;

import com.base.authenticate.dto.SecurityRole;
import com.base.config.security.properties.jwt.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;

public class JwtProvider implements AbstractJwtProvider<AuthenticationDetails> {

    private final String PREFIX;
    private final Long EXPIRE_TIME;
    private final SecretKey SIGNING_KEY;
    private final String ISSUER;

    private final String ID = "id";
    private final String ROLE = "role";

    public JwtProvider(JwtProperties properties) {
        this.PREFIX = "Bearer";
        this.SIGNING_KEY = Keys.hmacShaKeyFor(
            Base64.getEncoder().encode(properties.secret().getBytes(StandardCharsets.UTF_8)));
        this.EXPIRE_TIME = properties.expireTime();
        this.ISSUER = properties.issuer();
    }

    @Override
    public String tokenize(AuthenticationDetails tokenable) {
        LocalDateTime now = LocalDateTime.now();
        String token = Jwts.builder()
            .id(tokenable.identity())
            .issuer(ISSUER)
            .issuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
            .expiration(Date.from(now.plus(EXPIRE_TIME, ChronoUnit.MILLIS).atZone(ZoneId.systemDefault()).toInstant()))
            .claims(
                Map.of(
                    ID, tokenable.getUsername(),
                    ROLE, tokenable.role()
                )
            )
            .signWith(this.SIGNING_KEY)
            .compact();
        return String.format("%s %s", PREFIX, token);
    }

    private String removeBearer(String bearerToken) {
        return bearerToken.replace(PREFIX, "").trim();
    }

    private Claims parse(String bearerToken) {
        String token = this.removeBearer(bearerToken);
        JwtParser parser = Jwts.parser().verifyWith(this.SIGNING_KEY).build();
        Jws<Claims> jws = parser.parseSignedClaims(token);
        Claims claims = jws.getPayload();

        return claims;
    }


    @Override
    public Boolean validate(String bearerToken) {
        Claims claims = this.parse(bearerToken);
        Date expireDate = claims.getExpiration();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireLocalDateTime = LocalDateTime.from(expireDate.toInstant());
        return now.isBefore(expireLocalDateTime);
    }

    @Override
    public AuthenticationDetails decrypt(String bearerToken) {
        Claims claims = this.parse(bearerToken);
        String id = claims.getId();
        String loginId = claims.get(ID, String.class);
        SecurityRole role = claims.get(ROLE, SecurityRole.class);

        return new AuthenticationDetails(id, loginId, "", role);
    }
}
