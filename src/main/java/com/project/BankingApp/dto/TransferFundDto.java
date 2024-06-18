package com.project.BankingApp.dto;

public record TransferFundDto(Long fromAccountId,
                              Long toAccountId,
                              double amount){

                              }
