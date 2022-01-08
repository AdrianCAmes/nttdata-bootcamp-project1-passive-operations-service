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
@Document(collection = "accounts")
public class Account {
    @Id
    private String id;
    private Customer customer;
    private AccountType accountType;
    private String accountNumber;
    private Date issueDate;
    private Date dueDate;
    private Double currentBalance;
    private ArrayList<Operation> operations;
    private ArrayList<Person> holders;
    private ArrayList<Person> signers;
}