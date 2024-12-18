package com.reonfernandes.Taskify.controllers;

import com.reonfernandes.Taskify.services.impl.UserServicesImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserServicesImpl userServices;

    public PageController(UserServicesImpl userServices) {
        this.userServices = userServices;
    }

    @GetMapping("/")
    public String homePage(){
        logger.info("(Controller) Home page");
        return "home";
    }
    @GetMapping("/contact")
    public String contactPage(){
        logger.info("(Controller) Contact page");
        return "contact";
    }
    @GetMapping("/login")
    public String loginPage(){
        logger.info("(Controller) Login page");
        return "/forms/login";
    }
    @GetMapping("/register")
    public String registerPage(Model model){
        logger.info("(Controller) Register page");
        return "/forms/register";
    }
}
