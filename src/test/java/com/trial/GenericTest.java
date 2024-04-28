package com.trial;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import com.trial.controller.BaseControllerTest;
import com.trial.controller.ManagerControllerTest;
import com.trial.controller.SuperAdminControllerTest;
import com.trial.controller.UserControllerTest;
import com.trial.service.AuthenticationServiceTest;
import com.trial.service.BcryptServiceTest;
import com.trial.service.DefaultUserServiceImplementationTest;
import com.trial.service.ManagerServiceTest;
import com.trial.service.UserServiceTest;

@Suite
@SelectClasses({AuthenticationServiceTest.class, BcryptServiceTest.class, 
	DefaultUserServiceImplementationTest.class, ManagerServiceTest.class,
	UserServiceTest.class, BaseControllerTest.class, ManagerControllerTest.class, 
	SuperAdminControllerTest.class, UserControllerTest.class})
public class GenericTest {

}
