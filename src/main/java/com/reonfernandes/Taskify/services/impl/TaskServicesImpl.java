package com.reonfernandes.Taskify.services.impl;

import com.reonfernandes.Taskify.exceptions.ResourceNotFoundException;
import com.reonfernandes.Taskify.models.*;
import com.reonfernandes.Taskify.repositories.TaskRepository;
import com.reonfernandes.Taskify.services.TaskServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

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
    public Page<Task> getTasksForUser( User user, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return taskRepository.findByUser(user, pageable);
    }

    @Override
    public Page<Task> searchTaskForUser(String query, User user, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return taskRepository.findByTitleContainingIgnoreCaseAndUser(query, user, pageable);
    }

    @Override
    public void updateTask(Task task, Long id) {
        Task updateTask = taskRepository.findById(task.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Task with id: " + id + " not found.")
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
        taskRepository.save(updateTask);
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

    // Delete all task logic
    @Override
    public void deleteAllTask(Long ...id) {
        List<Task> deleteTasks = taskRepository.findAllById(Arrays.asList(id));
        if (!deleteTasks.isEmpty()){
            taskRepository.deleteAll(deleteTasks);
            logger.info("All task are deleted.");
        }
        else {
            logger.warn("No tasks found for the given IDs");
            throw new ResourceNotFoundException("No task found for the given IDs");
        }
    }

    // Complete all task logic
    @Override
    public void completeAllTask(Long ...id) {
        List<Task> allTask = taskRepository.findAllById(Arrays.asList(id));
        if (!allTask.isEmpty()) {
            allTask.forEach(task -> task.setCompleted(true));
            taskRepository.saveAll(allTask);
            logger.info("All specified task marked as completed");
        }
        else {
            logger.warn("No tasks found for the given IDs");
            throw new ResourceNotFoundException("No task found for the given IDs");
        }
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
