package com.reonfernandes.Taskify.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {
    @GetMapping("/dashboard")
    public String dashboardPage(){
        return "user/dashboard";
    }

    @GetMapping("/profile")
    public String profilePage(){
        return "user/profile";
    }

    @GetMapping("/addTask")
    public String addTaskPage(){
        return "user/addTask";
    }
}
