package com.reonfernandes.Taskify.controllers;

import com.reonfernandes.Taskify.helper.LoggedUser;
import com.reonfernandes.Taskify.models.User;
import com.reonfernandes.Taskify.services.impl.UserServicesImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class RootController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserServicesImpl userServices;

    public RootController(UserServicesImpl userServices) {
        this.userServices = userServices;
    }

    @ModelAttribute
    public void loggedInUser(Authentication authentication, Model model){
        if (authentication == null){
            return;
        }

        logger.info("(Controller) In the root controller");
        String username = LoggedUser.getEmail(authentication);
        logger.info("Dashboard accessed by user: {}", username);

        User user = userServices.getUserByEmail(username);
        logger.info("Username: {}", user.getName());
        model.addAttribute("loggedUser", user);
    }
}
