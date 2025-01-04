package com.reonfernandes.Taskify.services;

import com.reonfernandes.Taskify.models.Category;
import com.reonfernandes.Taskify.models.Priority;
import com.reonfernandes.Taskify.models.Status;
import com.reonfernandes.Taskify.models.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TaskServices {
    // CRUD functionalities
    Task newTask (Task task);
    Page<Task> getAllTask(Pageable pageable);
    Optional<Task> updateTask(Task task);
    void deleteTask(Long id);
    void completeTask(Long id);

    // Additional functionalities
    Optional<Task> getTaskById(Long id);
    Page<Task> getTasksByStatus(Status status, Pageable pageable);
    Page<Task> getTasksByCategory(Category category, Pageable pageable);
    Page<Task> getTasksByPriority(Priority priority, Pageable pageable);

}
