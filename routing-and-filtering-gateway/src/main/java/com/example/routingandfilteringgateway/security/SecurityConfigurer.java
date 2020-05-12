package com.example.routingandfilteringgateway.security;

import com.example.routingandfilteringgateway.ErrorHandling.RestAccessDeniedHandler;
import com.example.routingandfilteringgateway.ErrorHandling.RestAuthenticationFailureHandler;
import com.example.routingandfilteringgateway.filter.JwtRequestFilter;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService myUserDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/authenticate").permitAll()
                .antMatchers("/user/roles").hasAuthority("ADMIN")
                .antMatchers("/user/roles/*").hasAuthority("ADMIN")
                .antMatchers("/user/role").hasAuthority("ADMIN")
                .antMatchers("/user/deleteRole/*").hasAuthority("ADMIN")
                .antMatchers("/user/updateRole/*").hasAuthority("ADMIN")

                .antMatchers("/user/users").hasAuthority("ADMIN")
                .antMatchers("/user/users/role/*").hasAuthority("ADMIN")
                .antMatchers("/user/users/*").hasAnyAuthority("ADMIN","USER")
                .antMatchers("/user").hasAnyAuthority("ADMIN","USER")
                .antMatchers("/user/deleteUser/*").hasAnyAuthority("ADMIN","USER")
                .antMatchers("/user/updateUser/*").hasAnyAuthority("ADMIN","USER")

                .antMatchers("/online-testing/servers").hasAnyAuthority("ADMIN", "USER")
                .antMatchers(HttpMethod.GET, "/online-testing/server/*").hasAnyAuthority("ADMIN", "USER")
                .antMatchers("/online-testing/addServer").hasAuthority("ADMIN")
                .antMatchers("/online-testing/updateServer/*").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/online-testing/server/*").hasAuthority("ADMIN")
                .antMatchers("/online-testing/userServers/*").hasAuthority("ADMIN")
                .antMatchers("/online-testing/browsers").hasAuthority("ADMIN")
                .antMatchers("/online-testing/browser/*").hasAuthority("ADMIN")
                .antMatchers("/online-testing/browsersServer/*").hasAuthority("ADMIN")
                .antMatchers("/online-testing/addBrowser").hasAuthority("ADMIN")
                .antMatchers("/online-testing/updateBrowser/*").hasAuthority("ADMIN")
                .antMatchers("/online-testing/onlineTests").hasAnyAuthority("ADMIN", "USER")
                .antMatchers("/online-testing/onlineTest/*").hasAnyAuthority("ADMIN", "USER")
                .antMatchers("/online-testing/onlineTestsServer/*").hasAnyAuthority("ADMIN", "USER")
                .antMatchers("/online-testing/onlineTestsUser/*").hasAnyAuthority("ADMIN", "USER")
                .antMatchers("/online-testing/onlineTestsGSPECDocument/*").hasAnyAuthority("ADMIN", "USER")
                .antMatchers("/online-testing/addOnlineTest").hasAnyAuthority("ADMIN", "USER")
                .antMatchers("/online-testing/updateOnlineTest/*").hasAnyAuthority("ADMIN", "USER")
                .antMatchers("/online-testing/deleteUser/*").denyAll()
                .antMatchers("/online-testing/updateUser/*").denyAll()
                .antMatchers("/online-testing/user").denyAll()
                .antMatchers("/online-testing/GSPECDocument/*").denyAll()
                .antMatchers("/online-testing/updateGSPECDocument/*").denyAll()
                .antMatchers("/online-testing/addGSPECDocument").denyAll()
                .antMatchers("/online-testing/role").denyAll()
                .antMatchers("/online-testing/deleteRole/*").denyAll()
                .antMatchers("/online-testing/updateRole/*").denyAll()
                .anyRequest().authenticated();

    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new RestAuthenticationFailureHandler();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new RestAccessDeniedHandler();
    }
}
