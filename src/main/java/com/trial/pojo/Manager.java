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
@Document(collection = "managers")
public class Manager extends Person {

    @Id
	private ObjectId managerId;
    
    public Manager(String name, String username, String password) {
    	super(name, username, password, Role.MANAGER);
    	this.managerId = new ObjectId();
    }
}
