package com.reonfernandes.Taskify.controllers;

import com.reonfernandes.Taskify.forms.TaskForm;
import com.reonfernandes.Taskify.models.*;
import com.reonfernandes.Taskify.services.impl.TaskServicesImpl;
import com.reonfernandes.Taskify.services.impl.UserServicesImpl;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {
    private final List<String> category = Arrays.asList(
            "FOOD_DRINKS", "SHOPPING", "HOUSING", "BILLS", "TRANSPORT", "VEHICLE", "ELECTRONICS", "LEISURE",
            "TECHNOLOGY","OTHERS");
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
    public String dashboardPage(@RequestParam(value = "page", defaultValue = "1") int page,
                                @RequestParam(value = "size", defaultValue = "6") int size,
                                Model model) {
        // Fetch the logged-in user
        User loggedInUser = userServices.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        // Get tasks for the logged-in user
        Page<Task> paginated = taskServices.getTasksForUser(loggedInUser, page, size);
        List<Task> taskList = paginated.getContent();

        // Add attributes to the model
        model.addAttribute("currentPage", page);
        model.addAttribute("firstPage", paginated.isFirst());
        model.addAttribute("lastPage", paginated.isLast());
        model.addAttribute("totalPages", paginated.getTotalPages());
        model.addAttribute("totalItems", paginated.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("taskList", taskList);
        model.addAttribute("priority", priority);

        return "user/dashboard";
    }

    @GetMapping("/search")
    public String searchTasks(@RequestParam("query") String query,
                              @RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "size", defaultValue = "6") int size, Model model) {

        if (query == null || query.trim().isEmpty()){
            return "redirect:/user/dashboard";
        }

        User loggedInUser = userServices.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Page<Task> paginated = taskServices.searchTaskForUser(query, loggedInUser, page, size);
        List<Task> searchTaskList = paginated.getContent();

        model.addAttribute("currentPage", page);
        model.addAttribute("firstPage", page == 1);
        model.addAttribute("lastPage", page == paginated.getTotalPages());
        model.addAttribute("totalPages", paginated.getTotalPages());
        model.addAttribute("totalItems", paginated.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("taskList", searchTaskList);
        model.addAttribute("searchQuery", query);

        return "/user/searchResult";
    }

    @GetMapping("/filter")
    public String filterTaskByPriority(@RequestParam(value = "priority", required = false) List<Priority> priorities,
                                       @RequestParam(value = "page", defaultValue = "1") int pageNo,
                                       @RequestParam(value = "size", defaultValue = "6") int pageSize,
                                       Model model){

        User loggedInUser = userServices.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (priorities == null || priorities.isEmpty()){
            Page<Task> allTasks = taskServices.getTasksForUser(loggedInUser, pageNo, pageSize);
            model.addAttribute("filtered_task", allTasks.getContent());
            return "/user/dashboard";
        }

        Page<Task> filteredTasks = taskServices.getTasksByPriority(loggedInUser, priorities, pageNo, pageSize);
        model.addAttribute("filtered_task", filteredTasks.getContent());

        return "/user/filtered-task";
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

    @GetMapping("/updateTask/{id}")
    public String updateTaskPage(@PathVariable("id") Long id, Model model) {
        Optional<Task> optionalTask = taskServices.getTaskById(id);
        if (optionalTask.isPresent()){
            Task task = optionalTask.get();
            TaskForm taskForm = new TaskForm();
            taskForm.setTitle(task.getTitle());
            taskForm.setDescription(task.getDescription());
            taskForm.setLocalDate(task.getLocalDate());
            taskForm.setLocalTime(task.getLocalTime());
            taskForm.setPriority(task.getPriority());
            taskForm.setStatus(task.getStatus());
            taskForm.setCategory(task.getCategory());

            model.addAttribute("taskForm", taskForm);
            model.addAttribute("taskId", id);
            return "/user/updateTask";
        }
        return "redirect:/user/dashboard";
    }

    @PostMapping("/processUpdateTaskPage/{id}")
    public String processUpdateTaskPage(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute("taskForm") TaskForm taskForm,
            BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("taskForm", taskForm);
            model.addAttribute("taskId", id);
            return "/user/updateTask";
        }

        Optional<Task> optionalTask = taskServices.getTaskById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setTitle(taskForm.getTitle());
            task.setDescription(taskForm.getDescription());
            task.setLocalDate(taskForm.getLocalDate());
            task.setLocalTime(taskForm.getLocalTime());
            task.setPriority(taskForm.getPriority());
            task.setStatus(taskForm.getStatus());
            task.setCategory(taskForm.getCategory());
            task.setUpdatedAt(LocalDateTime.now());

            taskServices.updateTask(task, id);
            return "redirect:/user/dashboard";
        }

        model.addAttribute("errorMessage", "Task not found");
        return "/user/updateTask";
    }


    @GetMapping("/deleteTask/{id}")
    public String deleteTask(@PathVariable("id") Long id){
        logger.info("Deleting task with id: " + id);
        taskServices.deleteTask(id);
        logger.info("Task with id: " + id + " deleted successfully.");
        return "redirect:/user/dashboard";
    }

    @GetMapping("/deleteAllTask")
    public String deleteAllTask(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        List<Task> userTasks = taskServices.getTasksForUser(user, 1, Integer.MAX_VALUE).getContent();

        if (!userTasks.isEmpty()){
            List<Long> taskIdList = new ArrayList<>();

            for (Task task : userTasks){
                taskIdList.add(task.getId());
            }

            Long[] taskIds = taskIdList.toArray(new Long[taskIdList.size()]);
            taskServices.deleteAllTask(taskIds);

            logger.info("All tasks for user {} are deleted", user.getEmail());
        }
        else {
            logger.warn("No tasks found for user {}", user.getEmail());
        }

        return "redirect:/user/dashboard";
    }

    @GetMapping("/toggleTask/{id}")
    public String toggleTask(@PathVariable("id") Long id){
        logger.info("Task with id: " + id + " completed..");
        taskServices.completeTask(id);
        return "redirect:/user/dashboard";
    }

    @GetMapping("/completeAllTask")
    public String toggleAllTasks(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        List<Task> userTasks = taskServices.getTasksForUser(user, 1, Integer.MAX_VALUE).getContent();

        if (!userTasks.isEmpty()){
            List<Long> taskIdList = new ArrayList<>();

            for (Task task : userTasks){
                taskIdList.add(task.getId());
            }

            Long[] taskIds = taskIdList.toArray(new Long[taskIdList.size()]);

            taskServices.completeAllTask(taskIds);
            logger.info("All tasks for user {} completed.", user.getEmail());
        }
        else {
            logger.warn("No tasks found for user {}.", user.getEmail());
        }

        return "redirect:/user/dashboard";
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
