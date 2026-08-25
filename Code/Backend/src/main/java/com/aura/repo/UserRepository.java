package com.aura.repo;

import com.aura.model.User;
import org.springframework.data.jpa.repository.JpaRepository; // cung cap nhieu thao tac nhu save, delete ma khong can viet sql
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}