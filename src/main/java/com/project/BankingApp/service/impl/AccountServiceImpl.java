package com.project.BankingApp.service.impl;

import com.project.BankingApp.dto.AccountDto;
import com.project.BankingApp.repository.AccountRepository;
import com.project.BankingApp.service.AccountService;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        return null;
    }


}
