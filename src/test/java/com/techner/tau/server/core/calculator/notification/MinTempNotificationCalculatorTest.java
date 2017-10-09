package com.techner.tau.server.core.calculator.notification;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.techner.tau.server.core.ConfigTest;
import com.techner.tau.server.core.config.Config;
import com.techner.tau.server.core.guice.SMSModule;
import com.techner.tau.server.domain.model.AditionalInformation;
import com.techner.tau.server.domain.model.Station;

public class MinTempNotificationCalculatorTest {

	private MinTempNotificationCalculator calculator;
	private static Injector injector;

	@BeforeClass
	public static void setUpClass() {
		String path = ConfigTest.class.getClassLoader().getResource("etc/sms_config.xml").getPath();
		System.setProperty(Config.CONFIG_PATH_PROPERTY, path);
		injector = Guice.createInjector(new SMSModule());
	}

	@Before
	public void setUp() throws Exception {
		calculator = injector.getInstance(MinTempNotificationCalculator.class);
	}

	@After
	public void tearDown() throws Exception {
		calculator = null;
	}

	@Test
	public void test() {

		Station station = new Station();
		station.setId(5);
		AditionalInformation info = new AditionalInformation();
		info.setId(1);
		station.setInformation(info);

		calculator.calculate("MT", station);

	}
}
