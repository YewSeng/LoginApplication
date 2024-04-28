package com.trial.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import com.trial.pojo.Person;
import java.util.Optional;

@Repository
public interface UniqueUsernameRepository<T extends Person> {

    @Query("{'username': ?0}")
    Optional<T> findByUsername(String username);
}

