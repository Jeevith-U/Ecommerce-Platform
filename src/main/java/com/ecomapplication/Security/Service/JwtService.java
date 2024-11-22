package com.ecomapplication.Security.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private Logger logger = LoggerFactory.getLogger(JwtService.class) ;

    @Value("${spring.ecom.app.jwt.secret}")
    private String secretKey ;

    @Value("${spring.ecom.app.jwtcookieName}")
    private String jwtCookie ;

    public JwtService(){

        //secretKey = genarateSecretKey() ;
    }

    private String genarateSecretKey()  {

        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            SecretKey key = keyGenerator.generateKey() ;
            return Base64.getEncoder().encodeToString(key.getEncoded()) ;
        } catch (NoSuchAlgorithmException e) {
            logger.error("Error generating secret key: Algorithm not found", e);
            throw new IllegalStateException("Unable to generate secret key", e);
        }

    }

    public String getJwtFromCookeies(HttpServletRequest request){
        Cookie cookie = WebUtils.getCookie(request, jwtCookie) ;

        if (cookie != null) return cookie.getValue() ;

        else return null ;
    }

    public ResponseCookie generateJwtCookie(String name){

        String jwt = genarateToken(name) ;

        ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt).path("/student")
                .maxAge(3*60*60)
                .httpOnly(true)
                .build() ;

        return cookie ;
    }

    public String genarateToken(String name) {

        Map<String, Object> claims = new HashMap<>() ;

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(name)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
                .signWith(generateKey(), SignatureAlgorithm.HS256)
                .compact() ;
    }

    private Key generateKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public String extractUserName(String token) {
        // extract the username from jwt token
        String name = extractClaim(token, Claims::getSubject);
        logger.info(name+ " Extracted the name from the Token");
        return name ;
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(generateKey())
                .build().parseClaimsJws(token).getBody();
    }


    public boolean validateToken(String token, UserDetails userDetails) {
        logger.info("Validating the Authentication Token");
        final String userName = extractUserName(token);
        logger.info(userDetails.getUsername()+" UserName From the User Details Object");
        boolean result = (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
        logger.info("Validating the Authentication Token is : "+result);
        return result ;
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
