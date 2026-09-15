package com.aura.admin.repository;

import com.aura.admin.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
           "WHERE t.status = 'SUCCESS' AND t.createdAt BETWEEN :from AND :to")
    BigDecimal sumRevenueBetween(LocalDateTime from, LocalDateTime to);
}
