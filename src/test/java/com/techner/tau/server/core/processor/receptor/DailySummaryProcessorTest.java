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

public class DailySummaryProcessorTest {

	private static Injector injector;
	private DailySummaryProcessor processor;

	@BeforeClass
	public static void setUpClass() {
		String path = ConfigTest.class.getClassLoader().getResource("etc/sms_config.xml").getPath();
		System.setProperty(Config.CONFIG_PATH_PROPERTY, path);
		injector = Guice.createInjector(new SMSModule());
	}

	@Before
	public void setUp() throws Exception {
		processor = injector.getInstance(DailySummaryProcessor.class);
	}

	@After
	public void tearDown() throws Exception {
		processor = null;
	}

	@Test
	// @Ignore
	public void test() {
		processor.process(new InboundMessage(new Date(), "5493413378790",
				"D|000113|13:48|T:31.2*H:65.1*L:0.0*P:980.8*S:31.6*B:0.0|07", 1, ""));
	}

}
