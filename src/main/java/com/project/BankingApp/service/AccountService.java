package com.project.BankingApp.service;

import com.project.BankingApp.dto.AccountDto;
import com.project.BankingApp.entity.Account;

public interface AccountService {

    AccountDto createAccount(AccountDto accountDto);
}
