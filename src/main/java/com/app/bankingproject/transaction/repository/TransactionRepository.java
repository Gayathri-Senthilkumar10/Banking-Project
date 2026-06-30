package com.app.bankingproject.transaction.repository;

import com.app.bankingproject.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}