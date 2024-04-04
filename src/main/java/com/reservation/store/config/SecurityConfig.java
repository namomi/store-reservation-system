package com.reservation.store.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig{

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationConfiguration authenticationConfiguration) throws Exception {
        AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
        JwtTokenFilter jwtTokenFilter = new JwtTokenFilter(jwtTokenProvider, userDetailsService);
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(jwtTokenProvider);
        customAuthenticationFilter.setCustomAuthenticationManager(authenticationManager);
        customAuthenticationFilter.setFilterProcessesUrl("/user/login");


        http
                // CSRF 설정
                .csrf(csrf -> csrf.ignoringRequestMatchers( "/user/register/**", "/h2-console/**", "/user/login", "/store/register/**", "/user/logout",
                        "/reservation/**","/reviews/**" ))
                // 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/register/**", "/h2-console/**", "/user/login").permitAll() // 경로 패턴 수정
                        .requestMatchers("/reservation/**", "/store/**", "/reservation/**", "/reviews/**").authenticated()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/user/logout")
                        .logoutSuccessHandler((HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                        })
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID", "jwt")
                        .clearAuthentication(true)
                )

                // Headers 설정
                .headers(headers -> headers
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
                );

        return http.build();
    }
}
