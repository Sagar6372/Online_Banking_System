package com.sb.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.sb.entity.Account;
import com.sb.entity.Transaction;

public interface BankingService {
    Account createAccount(Account account);
    Account getAccountByAccountNumber(String accountNumber);
    Double getBalance(String accountNumber);
    List<Account> getAllAccountsSorted();
    Account updateAccount(String accountNumber, Account updatedAccount);
    Account deposit(String accountNumber, Double amount); // FIXED
    Account withdraw(String accountNumber, Double amount);
    Account transfer(String fromAccountNumber, String toAccountNumber, Double amount);
    List<Transaction> getTransactionHistory(String accountNumber);
    void deleteAccount(String accountNumber);
}


