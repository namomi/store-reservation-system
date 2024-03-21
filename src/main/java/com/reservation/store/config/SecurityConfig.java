package com.reservation.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;


@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF 설정
            .csrf(csrf -> csrf.ignoringRequestMatchers("/user/register/**","/h2-console/**"))
            // 권한 설정
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/user/register/**", "/h2-console/**").permitAll() // 경로 패턴 수정
                    .anyRequest().authenticated()
            )
            // Headers 설정
            .headers(headers -> headers
                    .addHeaderWriter(new XFrameOptionsHeaderWriter(
                            XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
            );

        return http.build();
    }

}
