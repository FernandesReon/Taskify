package com.reonfernandes.Taskify.repositories;

import com.reonfernandes.Taskify.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
