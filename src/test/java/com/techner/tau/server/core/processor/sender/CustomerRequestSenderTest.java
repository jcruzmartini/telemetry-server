package com.techner.tau.server.core.processor.sender;

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
import com.techner.tau.server.domain.model.SMSCustomerRequest;
import com.techner.tau.server.domain.model.Station;

public class CustomerRequestSenderTest {

	private CustomerRequestSender sender;
	private static Injector injector;

	@BeforeClass
	public static void setUpClass() {
		String path = ConfigTest.class.getClassLoader().getResource("etc/sms_config.xml").getPath();
		System.setProperty(Config.CONFIG_PATH_PROPERTY, path);
		injector = Guice.createInjector(new SMSModule());
	}

	@Before
	public void setUp() throws Exception {
		sender = injector.getInstance(CustomerRequestSender.class);
	}

	@After
	public void tearDown() throws Exception {
		sender = null;
	}

	@Test
	// @Ignore
	public void test() {
		SMSCustomerRequest req = new SMSCustomerRequest(new InboundMessage(new Date(), "5493416527111", "INFO|91", 1,
				""));
		Station station = new Station();
		station.setId(5);
		req.setStation(station);
		sender.process(req);
	}
}
