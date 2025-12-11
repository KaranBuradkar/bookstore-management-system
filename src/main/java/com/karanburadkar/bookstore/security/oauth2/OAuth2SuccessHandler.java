package com.karanburadkar.bookstore.security.oauth2;

import com.karanburadkar.bookstore.dto.SignupRequestDto;
import com.karanburadkar.bookstore.entity.type.AuthProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(OAuth2SuccessHandler.class);

    @Autowired
    private OAuth2Service oAuth2Service;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // All user attributes returned by the OAuth2 provider (Google, GitHub, etc.)
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // Extract details (these vary by provider)
        String email = null;
        String name = null;
        String picture = null;
        AuthProvider provider = null;

        String registrationId = token.getAuthorizedClientRegistrationId(); // "google", "github", etc.

        if ("google".equalsIgnoreCase(registrationId)) {
            email = (String) attributes.get("email");
            name = (String) attributes.get("name");
            picture = (String) attributes.get("picture");
            provider = AuthProvider.GOOGLE;
        } else if ("github".equalsIgnoreCase(registrationId)) {
            email = (String) attributes.get("email");
            name = (String) attributes.get("name");
            picture = (String) attributes.get("avatar_url");
            provider = AuthProvider.GITHUB;
        } else if ("facebook".equalsIgnoreCase(registrationId)) {
            email = (String) attributes.get("email");
            name = (String) attributes.get("name");
//            picture = ((Map<String, Object>) ((Map<String, Object>) attributes.get("picture")).get("data")).get("url").toString();
        }

        log.info("OAuth2 Login Success! Provider: {} -> {}", registrationId, provider);

        SignupRequestDto signupRequestDto = new SignupRequestDto(email, "", "", provider);
        signupRequestDto.setFirstname(name);

        oAuth2Service.resolveOAuth2(signupRequestDto);

//         Example: Redirect to frontend or home page
        response.sendRedirect("/api/public");
    }
}
