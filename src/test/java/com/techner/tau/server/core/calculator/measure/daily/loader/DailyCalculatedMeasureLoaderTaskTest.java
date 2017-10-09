package com.techner.tau.server.core.calculator.measure.daily.loader;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.techner.tau.server.core.ConfigTest;
import com.techner.tau.server.core.config.Config;
import com.techner.tau.server.core.guice.SMSModule;

public class DailyCalculatedMeasureLoaderTaskTest {

	private static Injector injector;
	private DailyCalculatedMeasureLoaderTask task;

	@BeforeClass
	public static void setUpClass() {
		String path = ConfigTest.class.getClassLoader().getResource("etc/sms_config.xml").getPath();
		System.setProperty(Config.CONFIG_PATH_PROPERTY, path);
		injector = Guice.createInjector(new SMSModule());
	}

	@Before
	public void setUp() throws Exception {
		task = injector.getInstance(DailyCalculatedMeasureLoaderTask.class);
	}

	@After
	public void tearDown() throws Exception {
		task = null;
	}

	@Test
	public void test() {
		task.run();
	}

}
