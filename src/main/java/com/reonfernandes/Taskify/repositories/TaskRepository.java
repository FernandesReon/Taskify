package com.reonfernandes.Taskify.repositories;

import com.reonfernandes.Taskify.models.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByStatus(Status status, Pageable pageable);
    Page<Task> findByCategory(Category category, Pageable pageable);
    @Query("SELECT t FROM Task t WHERE t.user = :user AND t.priority IN :priority")
    Page<Task> findByUserAndPriority(@Param("user") User user, @Param("priority") List<Priority> priority, Pageable pageable);
    Page<Task> findByUser(User user, Pageable pageable);
    Page<Task> findByTitleContainingIgnoreCaseAndUser(String query, User user, Pageable pageable);
}
