package com.reservationbroker.reservation.jwt.filters;


import com.reservationbroker.reservation.authentication.TokenBlackListService;
import com.reservationbroker.reservation.entities.User;
import com.reservationbroker.reservation.jwt.JwtService;
import com.reservationbroker.reservation.services.UserService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService adminService;
    private final TokenBlackListService tokenBlackListService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws JwtException, IOException {

        try {
            String authHeader = request.getHeader("Authorization"); // Bearer jwt

            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                filterChain.doFilter(request, response);
                //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Please login - No token provided");
                //extracted(response, "Please login - No token provided");
                return;
            }
            String jwt = authHeader.split(" ")[1];

            if (tokenBlackListService.isTokenBlacklisted(jwt)) {
                //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Please login - Token blacklisted");
                extracted(response, "Please login - Token blacklisted");
                return;
            }

            String username = jwtService.extractUsername(jwt);

            User user = adminService.findByUsername(username).get();

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    username, null, user.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authToken);
            filterChain.doFilter(request, response);

        } catch (JwtException e) {
            System.err.println(e.getMessage());
            //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT signature, please verify your JWT token. Please login.");
            extracted(response, e.getMessage());
        }catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    private static void extracted(HttpServletResponse response , String s) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \""+s+"\"}");
        response.getWriter().flush();
        response.getWriter().close();
    }
}
