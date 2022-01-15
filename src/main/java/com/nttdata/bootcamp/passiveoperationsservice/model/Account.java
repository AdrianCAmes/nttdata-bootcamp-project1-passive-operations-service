package com.nttdata.bootcamp.passiveoperationsservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Document(collection = "accounts")
public class Account {
    @Id
    private String id;
    private String status;
    private Customer customer;
    private AccountType accountType;
    private AccountSpecifications accountSpecifications;
    private String accountNumber;
    private Date issueDate;
    private Date dueDate;
    private Double currentBalance;
    private Integer doneOperationsInMonth;
    private ArrayList<Operation> operations;
    private ArrayList<Person> holders;
    private ArrayList<Person> signers;
}