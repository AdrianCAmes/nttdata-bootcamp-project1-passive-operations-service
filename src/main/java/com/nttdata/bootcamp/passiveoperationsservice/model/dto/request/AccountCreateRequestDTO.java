package com.nttdata.bootcamp.passiveoperationsservice.model.dto.request;

import com.nttdata.bootcamp.passiveoperationsservice.model.AccountType;
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
public class AccountCreateRequestDTO {
    private String customerId;
    private AccountType accountType;
    private Date issueDate;
    private Date dueDate;
    private ArrayList<Person> holders;
    private ArrayList<Person> signers;
}
