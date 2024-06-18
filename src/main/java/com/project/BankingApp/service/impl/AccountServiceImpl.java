package com.project.BankingApp.service.impl;

import com.project.BankingApp.Mapper.AccountMapper;
import com.project.BankingApp.dto.AccountDto;
import com.project.BankingApp.dto.TransferFundDto;
import com.project.BankingApp.entity.Account;
import com.project.BankingApp.entity.Transaction;
import com.project.BankingApp.exception.AccountException;
import com.project.BankingApp.repository.AccountRepository;
import com.project.BankingApp.repository.TransactionRepository;
import com.project.BankingApp.service.AccountService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private TransactionRepository transactionRepository;

    private AccountRepository accountRepository;

    private static final String TRANSACTION_TYPE_DEPOSIT = "DEPOSIT";
    private static final String TRANSACTION_TYPE_WITHDRAW = "WITHDRAW";

    private static final String TRANSACTION_TYPE_TRANSFER = "TRANSFER";

    public AccountServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {

        Account account = AccountMapper.mapToAccount(accountDto);
        Account savedAccount = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto getAccountById(Long id) {

        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountException("Account does not exist"));
        return AccountMapper.mapToAccountDto(account);
    }

    @Override
    public AccountDto deposit(Long id, double amount) {

        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountException("Account does not exist"));

        double total = account.getBalance() + amount;
        account.setBalance(total);
        Account savedAccount = accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccountId(account.getId());
        transaction.setAmount(amount);
        transaction.setTransactionType(TRANSACTION_TYPE_DEPOSIT);
        transaction.setTimestamp(LocalDate.now());


        transactionRepository.save(transaction);


        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto withdraw(Long id, double amount) {

        Account account = accountRepository
                .findById(id).orElseThrow(() -> new AccountException("Account does not exist"));

        if (account.getBalance() < amount) {
            throw new AccountException("Insufficient amount");

        }

        double total = account.getBalance() - amount;
        account.setBalance(total);
        Account savedAccount = accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccountId(id);
        transaction.setAmount(amount);
        transaction.setTransactionType(TRANSACTION_TYPE_WITHDRAW);
        transaction.setTimestamp(LocalDate.now());

        transactionRepository.save(transaction);


        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map((account) -> AccountMapper.mapToAccountDto(account))
                .collect(Collectors.toList());

       }

    @Override
    public void deleteAccount(Long id) {
        Account account = accountRepository
                .findById(id).orElseThrow(() -> new AccountException("Account does not exist"));

         accountRepository.deleteById(id);
    }

    @Override
    public void transferFunds(TransferFundDto transferFundDto) {

        // Retrieve the account from which we send the amount.

        Account fromAccount = accountRepository.findById(transferFundDto.fromAccountId())
                .orElseThrow(() ->new AccountException("Account does not exists"));

        // Retrieve the account to which we send the amount

        Account toAccount = accountRepository.findById(transferFundDto.toAccountId())
                .orElseThrow(() ->new AccountException("Account does not exists"));

        if(fromAccount.getBalance() < transferFundDto.amount()){
            throw new RuntimeException("Insufficient Amount");
        }

        //Debit the amount from fromAccount object
        fromAccount.setBalance(fromAccount.getBalance() - transferFundDto.amount());

        //Credit the amount to toAccount object
        toAccount.setBalance(toAccount.getBalance()+transferFundDto.amount());

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction transaction = new Transaction();
        transaction.setAccountId(transferFundDto.fromAccountId());
        transaction.setAmount(transferFundDto.amount());
        transaction.setTransactionType("TRANSACTION_TYPE_TRANSFER");
        transaction.setTimestamp(LocalDate.now());

        transactionRepository.save(transaction);


    }

}


