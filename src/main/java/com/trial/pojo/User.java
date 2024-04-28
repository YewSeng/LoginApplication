package com.trial.pojo;

import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.trial.constants.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Document(collection = "users")
public class User extends Person{

    @Id
	private ObjectId userId;
    
    public User(String name, String username, String password) {
    	super(name, username, password, Role.USER);
    	this.userId = new ObjectId();
    }
}
