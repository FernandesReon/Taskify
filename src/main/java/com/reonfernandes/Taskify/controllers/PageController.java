package com.reonfernandes.Taskify.controllers;

import com.reonfernandes.Taskify.forms.RegisterForm;
import com.reonfernandes.Taskify.models.User;
import com.reonfernandes.Taskify.services.impl.UserServicesImpl;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String registerPage(Model model, HttpSession session){
        logger.info("(Controller) Register page");

        RegisterForm registerForm = new RegisterForm();
        model.addAttribute("registerForm", registerForm);

        if (session.getAttribute("success") != null){
            model.addAttribute("success", session.getAttribute("success"));
            session.removeAttribute("success");
        }

        if (session.getAttribute("failure") != null){
            model.addAttribute("failure", session.getAttribute("failure"));
            session.removeAttribute("failure");
        }

        return "/forms/register";
    }

    @PostMapping("/process-form")
    public String processRegisterForm(@Valid @ModelAttribute("registerForm") RegisterForm registerForm,
                                      BindingResult bindingResult, HttpSession session) {
        logger.info("Processing register form");

        if (bindingResult.hasErrors()) {
            return "/forms/register";
        }

        try {
            User user = new User();
            user.setName(registerForm.getName());
            user.setUsername(registerForm.getUsername());
            user.setEmail(registerForm.getEmail());
            user.setPhoneNumber(registerForm.getPhoneNumber());
            user.setPassword(registerForm.getPassword());

            userServices.saveUser(user);
            logger.info("User: " + user);

            session.setAttribute("success", "Success! Your account has been created.");

        } catch (Exception e) {
            logger.error("Error while creating user: " + e);
            session.setAttribute("failure", "Error! Occurred while creating your account. Please try again.");
        }

        logger.info("Form submitted.");
        return "redirect:/register";
    }
}
