package com.reonfernandes.Taskify.controllers;

import com.reonfernandes.Taskify.forms.RegisterForm;
import com.reonfernandes.Taskify.helper.LoggedUser;
import com.reonfernandes.Taskify.models.Email;
import com.reonfernandes.Taskify.models.User;
import com.reonfernandes.Taskify.services.impl.EmailServiceImpl;
import com.reonfernandes.Taskify.services.impl.UserServicesImpl;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
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
    private final EmailServiceImpl emailService;

    @Value("${mail.receiver}")
    private String emailReceiver;

    public PageController(UserServicesImpl userServices, EmailServiceImpl emailService) {
        this.userServices = userServices;
        this.emailService = emailService;
    }

    @GetMapping("/")
    public String homePage(){
        logger.info("(Controller) Home page");
        return "home";
    }

//    @GetMapping("/about")
//    public String aboutPage(){
//        logger.info("(Controller) About page");
//        return "about";
//    }

    @GetMapping("/contact")
    public String contactPage(Model model){
        logger.info("(Controller) Contact page loaded");

        Email email = new Email();
        model.addAttribute("email", email);
        return "contact";
    }

    @PostMapping("/sendMail")
    public String sendEmail(@Valid @ModelAttribute("email") Email email, BindingResult result, Model model){
        logger.info("(Controller) Sending Email");

        if (result.hasErrors()){
            return "/contact";
        }

        emailService.sendEmail(emailReceiver, email.getSubject(),
                "From: " + email.getRecipient() + "\n\n" + email.getMessage());

        logger.info("Email has been sent successfully from {}", email.getRecipient());
        model.addAttribute("successMessage", "Your message has been sent successfully!");
        model.addAttribute("email", new Email());

        return "/contact";
    }

    @GetMapping("/login")
    public String loginPage(){
        logger.info("(Controller) Login page");
        return "/forms/login";
    }

    @GetMapping("/success")
    public String successPage(){
        logger.info("(Controller) Success page");
        return "success";
    }

//    @GetMapping("/error")
//    public String errorPage(){
//        logger.info("(Controller) Error page");
//        return "error";
//    }


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

            if (registerForm.getPhoneNumber() != null && !registerForm.getPhoneNumber().isEmpty()){
                user.setPhoneNumber(registerForm.getPhoneNumber());
                logger.info("Phone number provided: {}", registerForm.getPhoneNumber());
            }
            else {
                user.setPhoneNumber(null);
                logger.info("No phone number provided.");
            }
            user.setPassword(registerForm.getPassword());
            userServices.saveUser(user);
            logger.info("User: " + user);

            session.setAttribute("success", "Success! Your account has been created.");

        } catch (Exception e) {
            logger.error("Error while creating user: " + e);
            session.setAttribute("failure", "Error! Occurred while creating your account. Please try again.");
        }

        logger.info("Form submitted.");
        return "redirect:/success";
    }
}
