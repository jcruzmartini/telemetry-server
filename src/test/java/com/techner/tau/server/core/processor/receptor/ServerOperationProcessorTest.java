package com.techner.tau.server.core.processor.receptor;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.smslib.InboundMessage;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.techner.tau.server.core.ConfigTest;
import com.techner.tau.server.core.config.Config;
import com.techner.tau.server.core.guice.SMSModule;

public class ServerOperationProcessorTest {

	private static Injector injector;
	private ServerOperationProcessor processor;

	@BeforeClass
	public static void setUpClass() {
		String path = ConfigTest.class.getClassLoader().getResource("etc/sms_config.xml").getPath();
		System.setProperty(Config.CONFIG_PATH_PROPERTY, path);
		injector = Guice.createInjector(new SMSModule());
	}

	@Before
	public void setUp() throws Exception {
		processor = injector.getInstance(ServerOperationProcessor.class);
	}

	@After
	public void tearDown() throws Exception {
		processor = null;
	}

	@Test
	// @Ignore
	public void test() {
		processor.process(new InboundMessage(new Date(), "5493415460866",
				"O|15|SETRTC|", 1, ""));
	}

}
