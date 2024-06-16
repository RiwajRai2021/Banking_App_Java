package com.project.BankingApp.service.impl;

import com.project.BankingApp.Mapper.AccountMapper;
import com.project.BankingApp.dto.AccountDto;
import com.project.BankingApp.entity.Account;
import com.project.BankingApp.exception.AccountException;
import com.project.BankingApp.repository.AccountRepository;
import com.project.BankingApp.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
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
}


