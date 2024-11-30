package com.ecomapplication.Filter;

import com.ecomapplication.Security.Service.JwtService;
import com.ecomapplication.Security.Service.MyUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService ;

    @Autowired
    private ApplicationContext context ;

    private final Logger logger = LoggerFactory.getLogger(JwtFilter.class) ;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            logger.info("Authenticating the Request");

            String jwt = parseJwt(request);

            String username = jwtService.extractUserName(jwt);

            UserDetails userDetails = context.getBean(MyUserDetailService.class).loadUserByUsername(username);

            logger.info("Loaded the UserDetails ");

            if (jwt != null && jwtService.validateToken(jwt, userDetails)) {

                UsernamePasswordAuthenticationToken authentication =

                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                logger.debug("Roles from JWT: {}", userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch (Exception e){
            logger.error("Cannot set user authentication: {}", e);
        }
            filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String jwt = jwtService.getJwtFromCookeies(request);
        logger.debug("AuthTokenFilter.java: {}", jwt);
        return jwt;
    }

}
