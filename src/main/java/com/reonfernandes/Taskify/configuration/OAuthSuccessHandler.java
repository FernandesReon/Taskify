package com.reonfernandes.Taskify.configuration;

import com.reonfernandes.Taskify.helper.PasswordGenerator;
import com.reonfernandes.Taskify.helper.UniqueUsernameAssigner;
import com.reonfernandes.Taskify.models.Providers;
import com.reonfernandes.Taskify.models.User;
import com.reonfernandes.Taskify.repositories.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Component
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public OAuthSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        logger.info("onAuthSuccessHandler");

        var oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        String authorizedClientRegistrationId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();
        logger.info(authorizedClientRegistrationId);

        var oauthUser = (DefaultOAuth2User) authentication.getPrincipal();
        oauthUser.getAttributes().forEach((key, value) -> {
            logger.info(key + " : " + value);
        });

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setEmailVerified(true);
        user.setAccountEnabled(true);
        user.setPassword(PasswordGenerator.randomPassword());

        if (authorizedClientRegistrationId.equalsIgnoreCase("google")){
            user.setName(Objects.requireNonNull(oauthUser.getAttribute("name")).toString());
            user.setUsername(UniqueUsernameAssigner.assignUniqueUsername(user.getName()));
            user.setEmail(Objects.requireNonNull(oauthUser.getAttribute("email")).toString());
            user.setProfilePic(Objects.requireNonNull(oauthUser.getAttribute("picture")).toString());
            user.setProviders(Providers.GOOGLE);
            user.setProviderId(oauthUser.getName());
        }
        else if (authorizedClientRegistrationId.equalsIgnoreCase("github")){
            String email = oauthUser.getAttribute("email") != null ?
                    oauthUser.getAttribute("email").toString() :
                    oauthUser.getAttribute("login").toString() + "@gmail.com";
            String picture = Objects.requireNonNull(oauthUser.getAttribute("avatar_url")).toString();
            String name = oauthUser.getAttribute("login").toString();
            String providerUserId = oauthUser.getName();
            user.setEmail(email);
            user.setProfilePic(picture);
            user.setName(name);
            user.setUsername(UniqueUsernameAssigner.assignUniqueUsername(name));
            user.setProviders(Providers.GITHUB);
            user.setProviderId(providerUserId);
        }
        else {
            logger.info("OAuthenticationSuccessHandler: Unknown provider");
        }

        User checkUser = userRepository.findByEmail(user.getEmail()).orElse(null);
        if (checkUser == null) {
            userRepository.save(user); // Save the new user object
            logger.info("New user saved with email: " + user.getEmail());
        } else {
            logger.info("User already exists with email: " + user.getEmail());
        }


        response.sendRedirect("/user/dashboard");
    }
}
