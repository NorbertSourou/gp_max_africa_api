package io.maxafrica.gpserver.security;

import io.jsonwebtoken.*;
import io.maxafrica.gpserver.dto.CategoryDTO;
import io.maxafrica.gpserver.dto.JwtAuthenticationResponse;
import io.maxafrica.gpserver.dto.UserResponse;
import io.maxafrica.gpserver.entities.Category;
import io.maxafrica.gpserver.services.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class TokenProvider {

    @Value("${jwt.jwtSecret}")
    private String jwtSecret;

    @Value("${jwt.jwtRefreshSecret}")
    private String jwtRefreshSecret;

    @Value("${jwt.jwtExpirationInMs}")
    private long jwtExpirationInMs;

    @Value("${jwt.jwtRefreshExpirationInMs}")
    private long jwtRefreshExpirationInMs;

    private final BaseService baseService;
    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);


    public TokenProvider(BaseService baseService) {

        this.baseService = baseService;
    }


    public JwtAuthenticationResponse generateToken(Authentication authentication) {
        return generateToken((UserPrincipal) authentication.getPrincipal());
    }

    public JwtAuthenticationResponse generateToken(UserPrincipal userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
        Date refreshExpiryDate = new Date(now.getTime() + jwtRefreshExpirationInMs);

        Map<String, Object> claims = new HashMap<>();
        List<String> roles = new ArrayList<>();
        userDetails.getAuthorities().forEach(role -> {
            roles.add(role.getAuthority());
        });


        claims.put("permissions", roles.toArray(new String[0]));

        // JWT for permission
        String tokenPermission = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
               // .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret.getBytes())
                .compact();

        String token = Jwts.builder()
                .setSubject(userDetails.getId().toString())
                .setIssuedAt(new Date())
                // .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret.getBytes())
                .compact();


        // JWT for refresh token
        Map<String, Object> claimsRefresh = new HashMap<>();
        String accessToken = Jwts.builder()
                .setClaims(claimsRefresh)
                .setSubject(userDetails.getId().toString())
                .setIssuedAt(new Date())
              //  .setExpiration(refreshExpiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtRefreshSecret.getBytes())
                .compact();


        List<CategoryDTO> categoryDTOS = baseService.getCategories().stream()
                .map(this::convertToDto)
                .toList();


        return new JwtAuthenticationResponse(token, accessToken, expiryDate, tokenPermission, new UserResponse(userDetails.getUsername(), userDetails.getEmail()), categoryDTOS);
    }

    private CategoryDTO convertToDto(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setPosition(category.getPosition());
        categoryDTO.setName(category.getName());
        return categoryDTO;
    }

    public String getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret.getBytes())
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        return validateJWT(authToken, jwtSecret);
    }

    private boolean validateJWT(String authToken, String jwtSecret) {
//        try {
        Jwts.parser().setSigningKey(jwtSecret.getBytes()).parseClaimsJws(authToken);
        return true;
//        } catch (SignatureException ex) {
//            logger.error("Invalid JWT signature");
//            throw ex;
//        } catch (MalformedJwtException ex) {
//            logger.error("Invalid JWT token");
//            throw ex;
//        } catch (ExpiredJwtException ex) {
//            logger.error("Expired JWT token");
//            throw ex;
//        } catch (UnsupportedJwtException ex) {
//            logger.error("Unsupported JWT token");
//            throw ex;
//        } catch (IllegalArgumentException ex) {
//            logger.error("JWT claims string is empty.");
//            throw ex;
//        }
    }


}
