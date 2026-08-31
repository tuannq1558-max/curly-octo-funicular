package com.aura.admin.repository;

import com.aura.admin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByRole(User.Role role);
    long countByRole(User.Role role);
}
