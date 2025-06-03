package com.sb.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sb.entity.Account;
import com.sb.entity.Transaction;
import com.sb.repository.AccountRepository;
import com.sb.repository.TransactionRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class BankingServiceImpl implements BankingService {

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private TransactionRepository txRepo;

    // Create Acc
    @Override
    public Account createAccount(Account acc) {
        if (accountRepo.findByAccountNumber(acc.getAccountNumber()).isPresent()) {
            throw new IllegalArgumentException("Account already exists!");
        }
        acc.setBalance(0.0);
        return accountRepo.save(acc);
    }

    // Get Acc Number
    @Override
    public Account getAccountByAccountNumber(String accountNumber) {
        return accountRepo.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
    }

    // Get Balance
    @Override
    public Double getBalance(String accountNumber) {
        return getAccountByAccountNumber(accountNumber).getBalance();
    }

    // Get Acc Holder name by ASC
    @Override
    public List<Account> getAllAccountsSorted() {
        return accountRepo.findAll()
                .stream()
                .sorted((a1, a2) -> a1.getAccountHolderName().compareToIgnoreCase(a2.getAccountHolderName()))
                .toList();
    }

    // Update ACC
    @Override
    public Account updateAccount(String accountNumber, Account updated) {
        Account existing = getAccountByAccountNumber(accountNumber);
        existing.setAccountHolderName(updated.getAccountHolderName());
        return accountRepo.save(existing);
    }

    // Deposit
    @Override
    @Transactional
    public Account deposit(String accountNumber, Double amount) {
        Account acc = getAccountByAccountNumber(accountNumber);
        acc.setBalance(acc.getBalance() + amount);

        if (acc.getTransactions() == null) {
            acc.setTransactions(new ArrayList<>());
        }

        acc.getTransactions().add(
            new Transaction("DEPOSIT", amount, LocalDateTime.now(), acc, null)
        );

        return accountRepo.save(acc);
    }

    // Withdraw
    @Override
    @Transactional
    public Account withdraw(String accountNumber, Double amount) {
        Account acc = getAccountByAccountNumber(accountNumber);
        if (acc.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        acc.setBalance(acc.getBalance() - amount);

        if (acc.getTransactions() == null) {
            acc.setTransactions(new ArrayList<>());
        }

        acc.getTransactions().add(
            new Transaction("WITHDRAWAL", amount, LocalDateTime.now(), acc, null)
        );

        return accountRepo.save(acc);
    }

    // Transfer Money
    @Override
    @Transactional
    public Account transfer(String from, String to, Double amount) {
        Account src = getAccountByAccountNumber(from);
        Account dest = getAccountByAccountNumber(to);

        if (src.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        src.setBalance(src.getBalance() - amount);
        dest.setBalance(dest.getBalance() + amount);

        if (src.getTransactions() == null) {
            src.setTransactions(new ArrayList<>());
        }
        if (dest.getTransactions() == null) {
            dest.setTransactions(new ArrayList<>());
        }

        src.getTransactions().add(
            new Transaction("TRANSFER_TO", amount, LocalDateTime.now(), src, to)
        );
        dest.getTransactions().add(
            new Transaction("TRANSFER_FROM", amount, LocalDateTime.now(), dest, from)
        );

        accountRepo.save(dest);
        return accountRepo.save(src);
    }

    // Transaction History
    @Override
    public List<Transaction> getTransactionHistory(String accountNumber) {
        Account acc = getAccountByAccountNumber(accountNumber);
        return txRepo.findByAccountId(acc.getId())
                .stream()
                .sorted((t1, t2) -> t2.getTransactionDate().compareTo(t1.getTransactionDate()))
                .toList();
    }

    // Delete Acc
    @Override
    public void deleteAccount(String accountNumber) {
        Account acc = getAccountByAccountNumber(accountNumber);
        accountRepo.delete(acc);
    }
}