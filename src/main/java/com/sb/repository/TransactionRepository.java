package com.sb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sb.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	List<Transaction> findByAccountId(Long accountId);
}
