package com.reonfernandes.Taskify.services;

import com.reonfernandes.Taskify.models.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TaskServices {
    // CRUD functionalities
    Task newTask (Task task);
    Page<Task> getTasksForUser(User user, int pageNo, int pageSize);
    Page<Task> searchTaskForUser(String query, User user, int pageNo, int pageSize);
    void updateTask(Task task, Long id);
    void deleteTask(Long id);
    void completeTask(Long id);

    void deleteAllTask(Long ...id);
    void completeAllTask(Long ...id);

    // Additional functionalities
    Optional<Task> getTaskById(Long id);
    Page<Task> getTasksByStatus(Status status, Pageable pageable);
    Page<Task> getTasksByCategory(Category category, Pageable pageable);
    Page<Task> getTasksByPriority(Priority priority, Pageable pageable);

}
