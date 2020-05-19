package com.example.routingandfilteringgateway.filter;

import com.example.routingandfilteringgateway.ErrorHandling.RestAccessDeniedHandler;
import com.example.routingandfilteringgateway.util.JwtUtil;
import com.example.routingandfilteringgateway.MyUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    /*@Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        System.out.println("request je: ");
        StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
        String queryString = request.getRequestURI();
        System.out.println(request.getRequestURI());
        String[] splitovano=queryString.split("/");
        String metoda=request.getMethod();
        if(metoda!=null && metoda.toUpperCase().equals("POST") && splitovano!=null && splitovano.length>2 && splitovano[1].equals("user") && splitovano[2].equals("user")){
            System.out.println("TRUE JE!");
            return true;
        }
        return super.shouldNotFilter(request);
    }*/

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        String zahtjev=request.getRequestURI();
        String[] splitano=zahtjev.split("/");

        //poseban slucaj POST za Usera
        /*if(splitano!=null && splitano.length>2 && splitano[1].equals("user") && splitano[2].equals("user") && request.getMethod()!=null && request.getMethod().toUpperCase().equals("POST")){
            System.out.println("USLOV ISPUNJEN!");
            chain.doFilter(request,response);
        }*/


        String parametarId;
        String parametarUsername=null;
        if(splitano!=null && splitano.length>4 && splitano[3].equals("username")){
            parametarUsername=splitano[4];
        }
        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
            }
            catch(ExpiredJwtException e){

            }
            catch(MalformedJwtException e){

            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if(parametarUsername!=null && !parametarUsername.equals(userDetails.getUsername())){
                chain.doFilter(request,response);
            }

            if (jwtUtil.validateToken(jwt, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }
}
