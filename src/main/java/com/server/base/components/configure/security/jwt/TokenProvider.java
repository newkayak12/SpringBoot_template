package com.server.base.components.configure.security.jwt;

import com.server.base.components.constants.Constants;
import com.server.base.repository.dto.reference.AccountDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component(value = "TokenProvider")
@DependsOn(value = {"constants"})
@Slf4j
@RequiredArgsConstructor
public class TokenProvider implements InitializingBean {
    private Key key;
    private String BODY = "BODY";
    private String BEARER_PREFIX = "Bearer ";
    private final ModelMapper mapper;

    //기존 TokenManager와 비슷하다.

    @Override
    public void afterPropertiesSet() throws Exception {
        key = Keys.hmacShaKeyFor(Base64.getEncoder().encode(getSecretKey().getBytes(StandardCharsets.UTF_8)));
    }

    public String createToken(Authentication authentication) {
        return BEARER_PREFIX + Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuer(Constants.PROJECT_NAME)
                .claim(BODY, authentication.getPrincipal())
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        AccountDto principal = mapper.map(claims.get(BODY), AccountDto.class);
        return new UsernamePasswordAuthenticationToken(principal, token, principal.getAuthorities());
    }


    public boolean validateToken( String token ) {
         Claims claims = Jwts.parserBuilder()
                 .setSigningKey(key).build()
                 .parseClaimsJws(token).getBody();
         AccountDto authorities = mapper.map(claims.get(BODY), AccountDto.class);
         String issuer = claims.getIssuer();
        return true;
    }


    private String getSecretKey() {
        Path path = Paths.get(Constants.SALT);
        String privateKey = null;
        try {
//            if(!Files.exists(path)) privateKey =  this.readDefault();
            if(!Files.exists(path)) privateKey =  "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ";
            else privateKey = Files.readAllLines(path).stream().collect(Collectors.joining());
            String head = "-----BEGIN OPENSSH PRIVATE KEY-----";
            String lf = "\n";
            String tail = "-----END OPENSSH PRIVATE KEY-----";
            privateKey = privateKey
                    .replaceAll(head, "")
                    .replaceAll(lf, "")
                    .replaceAll(tail,"");
        } catch (IOException e) {
            e.printStackTrace();
            privateKey = this.readDefault();
        }


        return  privateKey;
    }
    private String readDefault() {
        List<Character> upperCase = IntStream.rangeClosed(65, 90)
                .mapToObj(unicode -> (char) unicode)
                .unordered()
                .collect(Collectors.toList());
        List<Character> lowerCase = IntStream.rangeClosed(97, 122)
                .mapToObj(unicode -> (char) unicode)
                .unordered()
                .collect(Collectors.toList());
        List<Character> number = IntStream.rangeClosed(48, 57)
                .mapToObj(unicode -> (char) unicode)
                .unordered()
                .collect(Collectors.toList());

        List<Character> reference = new ArrayList<>();

        reference.addAll(upperCase);
        reference.addAll(lowerCase);
        reference.addAll(number);

        Random random = new Random();
        return  IntStream.rangeClosed(0, 2500)
                .mapToObj( intValue -> reference.get(random.nextInt(reference.size() - 1)))
                .map(String::valueOf)
                .collect(Collectors.joining());
    }
}
