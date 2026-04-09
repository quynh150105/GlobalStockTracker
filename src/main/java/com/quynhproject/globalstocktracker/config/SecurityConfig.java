package com.quynhproject.globalstocktracker.config;

import com.quynhproject.globalstocktracker.service.impl.CustomOAuth2Service;
import com.quynhproject.globalstocktracker.service.impl.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final String[] publicEndpoint = {"/users/**", "/auth/**","/watch-list/**","/stocks/**"};

    @Value("${jwt.signerKey}")
    private String signerKey;

    private final CustomOAuth2Service oAuth2Service;

    private final OAuth2SuccessHandler successHandler;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {

        http.authorizeHttpRequests(request ->
                request.requestMatchers(publicEndpoint).permitAll()
                        .anyRequest().authenticated()
        );

        http.cors(Customizer.withDefaults()
        );

        http.csrf(AbstractHttpConfigurer::disable);

        http.oauth2Login(oauth ->
                oauth.userInfoEndpoint(user -> user.userService(oAuth2Service)
                ).successHandler(successHandler)
        );
        return http.build();
    }



    @Bean
    public org.springframework.web.filter.CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

}
