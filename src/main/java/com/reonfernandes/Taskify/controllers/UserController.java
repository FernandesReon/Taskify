package com.reonfernandes.Taskify.controllers;

import com.reonfernandes.Taskify.forms.TaskForm;
import com.reonfernandes.Taskify.models.*;
import com.reonfernandes.Taskify.services.impl.TaskServicesImpl;
import com.reonfernandes.Taskify.services.impl.UserServicesImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    private final UserServicesImpl userServices;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public UserController(TaskServicesImpl taskServices, UserServicesImpl userServices) {
        this.taskServices = taskServices;
        this.userServices = userServices;
    }

    @GetMapping("/dashboard")
    public String dashboardPage(@RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "6") int size,
                                Model model) {
        // Fetch the logged-in user
        User loggedInUser = userServices.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        // Get tasks for the logged-in user
        Page<Task> paginated = taskServices.getTasksForUser(loggedInUser, page, size);
        List<Task> taskList = paginated.getContent();

        // Add attributes to the model
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paginated.getTotalPages());
        model.addAttribute("totalItems", paginated.getTotalElements());
        model.addAttribute("taskList", taskList);

        return "user/dashboard";
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
    public String processTaskForm(@Valid @ModelAttribute("taskForm") TaskForm taskForm,
                                  BindingResult result, Model model) {
        User loggedInUser = userServices.getUserByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
        logger.info("Processing form (create task)");

        if (result.hasErrors()) {
            logger.info("Validation errors: {}", result.getAllErrors());
            return "/user/addTask";
        }

        try {
            Task task = new Task();
            task.setTitle(taskForm.getTitle());
            task.setDescription(taskForm.getDescription()); // Fixed this line
            task.setLocalDate(taskForm.getLocalDate());
            task.setLocalTime(taskForm.getLocalTime());
            task.setStatus(taskForm.getStatus());
            task.setPriority(taskForm.getPriority());
            task.setCategory(taskForm.getCategory() != null ? taskForm.getCategory() : Category.OTHERS);
            task.setUpdatedAt(LocalDateTime.now());
            task.setCreatedAt(LocalDateTime.now());
            task.setUser(loggedInUser);

            logger.info("Task details: {}", task);
            taskServices.newTask(task);
            logger.info("Task saved successfully.");

            return "redirect:/user/dashboard";
        } catch (Exception e) {
            logger.error("Error while saving task: ", e);
            model.addAttribute("errorMessage", "An error occurred while saving the task.");
            return "/user/addTask";
        }
    }

    @GetMapping("/updateTask")
    public String updateTaskPage() {
        return "user/updateTask";
    }

    @ModelAttribute
    public void commonAttribute(Model model){
        model.addAttribute("category", category);
        model.addAttribute("status", status);
        model.addAttribute("priority", priority);
    }

    @GetMapping("/profile")
    public String profilePage(){
        return "user/profile";
    }
}
