package com.trial.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.trial.pojo.User;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId>, UniqueUsernameRepository<User> {

}
