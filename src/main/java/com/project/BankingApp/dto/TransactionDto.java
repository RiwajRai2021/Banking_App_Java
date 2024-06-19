package com.project.BankingApp.dto;

public record TransactionDto(Long id,
                             Long accountId,
                             double amount,
                             String transactionType,
                             java.time.LocalDate timestamp){


}
