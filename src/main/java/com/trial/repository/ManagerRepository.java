package com.trial.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.trial.pojo.Manager;

@Repository
public interface ManagerRepository extends MongoRepository<Manager, ObjectId>, UniqueUsernameRepository<Manager> {

}
