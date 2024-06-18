package com.project.BankingApp.service;

import com.project.BankingApp.dto.AccountDto;
import com.project.BankingApp.dto.TransferFundDto;
import com.project.BankingApp.entity.Account;

import java.util.List;

public interface AccountService {

    AccountDto createAccount(AccountDto accountDto);

    AccountDto getAccountById(Long id);

    AccountDto deposit(Long id, double amount);

    AccountDto withdraw(Long id, double amount);

    List<AccountDto>getAllAccounts();

    void deleteAccount(Long id);

    void transferFunds(TransferFundDto transferFundDto);



}
