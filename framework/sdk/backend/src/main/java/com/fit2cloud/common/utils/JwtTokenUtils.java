package com.fit2cloud.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fit2cloud.dto.User;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.time.DateUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JwtTokenUtils {

    public static final String TOKEN_NAME = "CE-TOKEN";

    private static int JWT_EXPIRE_MINUTES;

    public static void setJwtExpireMinutes(int jwtExpireMinutes) {
        if (jwtExpireMinutes <= 10) {
            jwtExpireMinutes = 10;
        }
        JwtTokenUtils.JWT_EXPIRE_MINUTES = jwtExpireMinutes;
    }

    private static SecretKey JWT_KEY = null;

    public static final String JWT_LOCK = "JWT_LOCK";

    public static final String JWT_BUCKET = "JWT_BUCKET";

    private static final String KEY_CLAIMS = "user";
    private static final String KEY_SUBJECT = "CE-USER";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void setJwtKey(String key) {
        JwtTokenUtils.JWT_KEY = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(key));
    }

    public static String initJwtKey() {
        return Encoders.BASE64URL.encode(Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded());
    }


    public static String createJwtToken(User user) {

        Date now = new Date();
        Date expirationDate = DateUtils.addMinutes(now, JWT_EXPIRE_MINUTES);

        Map<String, Object> claims = new HashMap<>();
        try {
            claims.put(KEY_CLAIMS, objectMapper.writeValueAsString(user));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setId(UUID.randomUUID().toString())
                .setSubject(KEY_SUBJECT)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(JWT_KEY)
                .compressWith(CompressionCodecs.GZIP);

        return builder.compact();
    }

    public static User parseJwtToken(String token) {

        String userStr = (String) Jwts.parserBuilder()
                .setSigningKey(JWT_KEY)
                .requireSubject(KEY_SUBJECT)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get(KEY_CLAIMS);
        try {
            return objectMapper.readValue(userStr, User.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }


}
