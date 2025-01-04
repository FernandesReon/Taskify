package com.reonfernandes.Taskify.helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Objects;

public class LoggedUser {
    public static String getEmail(Authentication authentication){

        if (authentication instanceof OAuth2AuthenticationToken aOAuth2AuthenticationToken){

            var clientId = aOAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
            var oauth2User = (OAuth2User) authentication.getPrincipal();

            String username = "";

            if (clientId.equalsIgnoreCase("google")){
                System.out.println("Fetching data from Google.");

                username = Objects.requireNonNull(oauth2User.getAttribute("email")).toString();
            }
            else if (clientId.equalsIgnoreCase("github")){
                System.out.println("Fetching data from GitHub.");

                username = oauth2User.getAttribute("email") != null ?
                        Objects.requireNonNull(oauth2User.getAttribute("email")).toString()
                        : Objects.requireNonNull(oauth2User.getAttribute("login")).toString() + "@gmail.com";
            }

            return username;
        }
        else {
            System.out.println("Fetching data from local database.");
            return authentication.getName();
        }
    }
}
