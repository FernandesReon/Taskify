package com.reonfernandes.Taskify.services.impl;

import com.reonfernandes.Taskify.exceptions.ResourceNotFoundException;
import com.reonfernandes.Taskify.models.Category;
import com.reonfernandes.Taskify.models.Priority;
import com.reonfernandes.Taskify.models.Status;
import com.reonfernandes.Taskify.models.Task;
import com.reonfernandes.Taskify.repositories.TaskRepository;
import com.reonfernandes.Taskify.services.TaskServices;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class TaskServicesImpl implements TaskServices {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final TaskRepository taskRepository;

    public TaskServicesImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task newTask(Task task) {
        logger.info("Creating a new task: {}", task);
        Task saveTask = taskRepository.save(task);
        logger.info("Task saved with ID: {}", saveTask.getId());
        return saveTask;
    }

    @Override
    public Page<Task> getAllTask(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    @Override
    public Optional<Task> updateTask(Task task) {
        Task updateTask = taskRepository.findById(task.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Task with id: " + task.getId() + " not found.")
        );

        logger.info("Updating task: {}", task);
        updateTask.setTitle(task.getTitle());
        updateTask.setDescription(task.getDescription());
        updateTask.setLocalDate(task.getLocalDate());
        updateTask.setLocalTime(task.getLocalTime());
        updateTask.setPriority(task.getPriority());
        updateTask.setStatus(task.getStatus());
        updateTask.setCategory(task.getCategory());
        updateTask.setUpdatedAt(task.getUpdatedAt());

        logger.info("Updated task: {}", updateTask);
        Task saveTask = taskRepository.save(updateTask);

        return Optional.ofNullable(saveTask);
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public void completeTask(Long id) {
        Task toggleTask = taskRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Task with id: " + " not found.")
        );
        toggleTask.setCompleted(!toggleTask.isCompleted());
        taskRepository.save(toggleTask);
    }

    @Override
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public Page<Task> getTasksByStatus(Status status, Pageable pageable) {
        return taskRepository.findByStatus(status, pageable);
    }

    @Override
    public Page<Task> getTasksByCategory(Category category, Pageable pageable) {
        return taskRepository.findByCategory(category, pageable);
    }

    @Override
    public Page<Task> getTasksByPriority(Priority priority, Pageable pageable) {
        return taskRepository.findByPriority(priority, pageable);
    }
}
