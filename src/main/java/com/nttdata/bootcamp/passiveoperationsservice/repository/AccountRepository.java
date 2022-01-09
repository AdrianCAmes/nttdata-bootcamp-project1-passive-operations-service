package com.nttdata.bootcamp.passiveoperationsservice.repository;

import com.nttdata.bootcamp.passiveoperationsservice.model.Account;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface AccountRepository extends ReactiveMongoRepository<Account, String> {
    Flux<Account>  findAccountsByCustomerId(String id);
}
