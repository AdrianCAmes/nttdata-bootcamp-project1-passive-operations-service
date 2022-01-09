package com.nttdata.bootcamp.passiveoperationsservice.model.dto.request;

import com.nttdata.bootcamp.passiveoperationsservice.model.AccountType;
import com.nttdata.bootcamp.passiveoperationsservice.model.Customer;
import com.nttdata.bootcamp.passiveoperationsservice.model.Operation;
import com.nttdata.bootcamp.passiveoperationsservice.model.Person;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AccountUpdateRequestDTO {
    private String id;
    private String status;
    private Date dueDate;
    private Double currentBalance;
    private ArrayList<Operation> operations;
    private ArrayList<Person> holders;
    private ArrayList<Person> signers;
}