package com.sb.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sb.entity.Account;
import com.sb.entity.Transaction;
import com.sb.service.BankingService;

@RestController
@RequestMapping("/banking-api")
public class BankingController {

    @Autowired
    private BankingService bankingService;

    // Create Account
    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        return ResponseEntity.ok(bankingService.createAccount(account));
    }

    // Get Account
    @GetMapping("/accounts/{accNum}")
    public ResponseEntity<Account> getAccount(@PathVariable String accNum) {
        return ResponseEntity.ok(bankingService.getAccountByAccountNumber(accNum));
    }

    // Get All Accounts Sorted
    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllSortedAccounts() {
        return ResponseEntity.ok(bankingService.getAllAccountsSorted());
    }

    // Update Account
    @PutMapping("/accounts/{accNum}")
    public ResponseEntity<Account> updateAccount(@PathVariable String accNum, @RequestBody Account updated) {
        return ResponseEntity.ok(bankingService.updateAccount(accNum, updated));
    }

    // Get Balance
    @GetMapping("/accounts/{accNum}/balance")
    public ResponseEntity<Double> getBalance(@PathVariable String accNum) {
        return ResponseEntity.ok(bankingService.getBalance(accNum));
    }

    // Deposit
    @PostMapping("/accounts/{accNum}/deposit")
    public ResponseEntity<Map<String, Object>> deposit(@PathVariable String accNum, @RequestParam Double amount) {
        Account updatedAccount = bankingService.deposit(accNum, amount);
        Map<String, Object> response = new HashMap<>();
        response.put("accountNumber", accNum);
        response.put("newBalance", updatedAccount.getBalance());
        response.put("message", "Deposit successful");
        return ResponseEntity.ok(response);
    }

    // Withdraw
    @PostMapping("/accounts/{accNum}/withdraw")
    public ResponseEntity<Map<String, Object>> withdraw(@PathVariable String accNum, @RequestParam Double amount) {
        Account updatedAccount = bankingService.withdraw(accNum, amount);
        Map<String, Object> response = new HashMap<>();
        response.put("accountNumber", accNum);
        response.put("newBalance", updatedAccount.getBalance());
        response.put("message", "Withdrawal successful");
        return ResponseEntity.ok(response);
    }

    // Transfer
    @PostMapping("/accounts/{from}/transfer")
    public ResponseEntity<Map<String, Object>> transfer(
            @PathVariable String from,
            @RequestParam String to,
            @RequestParam Double amount) {
        Account updatedAccount = bankingService.transfer(from, to, amount);
        Map<String, Object> response = new HashMap<>();
        response.put("accountNumber", from);
        response.put("newBalance", updatedAccount.getBalance());
        response.put("message", "Transfer successful to " + to);
        return ResponseEntity.ok(response);
    }

    // Transaction History
    @GetMapping("/accounts/{accNum}/transactions")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable String accNum) {
        return ResponseEntity.ok(bankingService.getTransactionHistory(accNum));
    }

    // Delete Account
    @DeleteMapping("/accounts/{accNum}")
    public ResponseEntity<String> deleteAccount(@PathVariable String accNum) {
        bankingService.deleteAccount(accNum);
        return ResponseEntity.ok("Account deleted successfully.");
    }
}