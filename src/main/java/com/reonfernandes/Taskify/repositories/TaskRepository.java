package com.reonfernandes.Taskify.repositories;

import com.reonfernandes.Taskify.models.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByStatus(Status status, Pageable pageable);

    Page<Task> findByCategory(Category category, Pageable pageable);

    Page<Task> findByPriority(Priority priority, Pageable pageable);

    Page<Task> findByUser(User user, Pageable pageable);
}
