package com.reonfernandes.Taskify.controllers;

import com.reonfernandes.Taskify.forms.TaskForm;
import com.reonfernandes.Taskify.models.Category;
import com.reonfernandes.Taskify.models.Priority;
import com.reonfernandes.Taskify.models.Status;
import com.reonfernandes.Taskify.models.Task;
import com.reonfernandes.Taskify.services.impl.TaskServicesImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    private final List<String> category = Arrays.asList(
            "FOOD_DRINKS", "SHOPPING", "HOUSING", "BILLS", "TRANSPORT", "VEHICLE", "ELECTRONICS", "LEISURE", "OTHERS");
    private final List<String> status = Arrays.asList(
            "NOT_STARTED", "IN_PROGRESS", "COMPLETED", "CANCELED", "PENDING", "BLOCKED", "ARCHIVED"
    );
    private final List<String> priority = Arrays.asList("CRITICAL", "HIGH", "MEDIUM", "LOW");

    private final TaskServicesImpl taskServices;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public UserController(TaskServicesImpl taskServices) {
        this.taskServices = taskServices;
    }

    @GetMapping("/dashboard")
    public String dashboardPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model
    ){
        Page<Task> taskPage = taskServices.getAllTask(PageRequest.of(page, size));
        model.addAttribute("taskPage", taskPage);

        // Add pagination details to the model
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("totalItems", taskPage.getTotalElements());

        return "user/dashboard";
    }

    @GetMapping("/profile")
    public String profilePage(){
        return "user/profile";
    }

    @GetMapping("/addTask")
    public String addTaskPage(Model model){
        logger.info("(Controller) Add task page");

        TaskForm taskForm = new TaskForm();
        taskForm.setPriority(Priority.MEDIUM);
        taskForm.setStatus(Status.NOT_STARTED);
        taskForm.setCategory(Category.OTHERS);

        model.addAttribute("taskForm", taskForm);
        model.addAttribute("category", category);
        model.addAttribute("status", status);
        model.addAttribute("priority", priority);

        return "user/addTask";
    }

    @PostMapping("/processTaskForm")
    public String processTaskForm(){
        return "redirect:/user/dashboard";
    }

    @GetMapping("/updateTask")
    public String updateTaskPage(
            @Valid @ModelAttribute("taskForm") TaskForm taskForm,
            BindingResult result, Model model)
    {

        return "user/updateTask";
    }

    @ModelAttribute
    public void commonAttribute(Model model){
        model.addAttribute("category", category);
        model.addAttribute("status", status);
        model.addAttribute("priority", priority);
    }
}
