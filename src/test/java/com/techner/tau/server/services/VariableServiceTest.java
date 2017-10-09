package com.techner.tau.server.services;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.techner.tau.server.core.config.Config;
import com.techner.tau.server.core.guice.SMSModule;

public class VariableServiceTest {

	private VariableService service;
	private static Injector injector;

	@BeforeClass
	public static void setUpClass() {
		String path = VariableServiceTest.class.getClassLoader().getResource("etc/sms_config.xml").getPath();
		System.setProperty(Config.CONFIG_PATH_PROPERTY, path);
		injector = Guice.createInjector(new SMSModule());
	}

	@Before
	public void setUp() throws Exception {
		service = injector.getInstance(VariableService.class);
	}

	@After
	public void tearDown() throws Exception {
		service = null;
	}

	@Test
	public void test() {
		List<String> codes = service.getMeteorologicalVariablesEnableByStation(5);
		System.out.println(codes);
	}

}
