package com.quynhproject.globalstocktracker.service.impl;

import com.quynhproject.globalstocktracker.constant.AuthProvider;
import com.quynhproject.globalstocktracker.domain.entity.User;
import com.quynhproject.globalstocktracker.excetion.AppException;
import com.quynhproject.globalstocktracker.repository.UserRepository;
import com.quynhproject.globalstocktracker.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthService authService;
    private final UserRepository userRepository;

    @Value("${app.oauth2.success-redirect-url:http://localhost:5173/oauth2/success}")
    private String successRedirectUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String providerId = oAuth2User.getAttribute("sub");

        if (email == null) {
            throw new AppException("Không lấy được email từ Google");
        }

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(email)
                            .username(name != null ? name : email)
                            .authProvider(AuthProvider.GOOGLE)
                            .providerId(providerId)
                            .password(null)
                            .role("ROLE_USER")
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();

                    return userRepository.save(newUser);
                });

        // tạo JWT
        String token = authService.generateToken(user);

        // redirect về frontend
        String redirectUrl = successRedirectUrl + "?token=" + token;

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
