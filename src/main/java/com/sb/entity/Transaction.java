package com.sb.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
@Table(name = "Bank_Transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String type; // DEPOSIT, WITHDRAWAL, TRANSFER

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private LocalDateTime transactionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column
    private String targetAccountNumber; // For transfers

    // Constructors
    public Transaction() {}

    public Transaction(String type, Double amount, LocalDateTime transactionDate, Account account, String targetAccountNumber) {
        this.type = type;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.account = account;
        this.targetAccountNumber = targetAccountNumber;
    }
}
