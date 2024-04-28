package com.trial.service;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({AuthenticationServiceTest.class, BcryptServiceTest.class, 
	DefaultUserServiceImplementationTest.class, ManagerServiceTest.class,
	UserServiceTest.class})
public class GenericServiceLayerTest {

}
