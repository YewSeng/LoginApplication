package com.trial.controller;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({BaseControllerTest.class, ManagerControllerTest.class, 
	SuperAdminControllerTest.class, UserControllerTest.class})
public class GenericControllerLayerTest {

}
