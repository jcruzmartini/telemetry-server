package com.techner.tau.server.core.processor.sender;

import java.util.ArrayList;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.smslib.InboundMessage;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.techner.tau.server.core.ConfigTest;
import com.techner.tau.server.core.config.Config;
import com.techner.tau.server.core.guice.SMSModule;
import com.techner.tau.server.domain.model.Customer;
import com.techner.tau.server.domain.model.SMSStationEvent;
import com.techner.tau.server.domain.model.SimCard;
import com.techner.tau.server.domain.model.Station;

public class AlertStationSenderTest {

	private static Injector injector;
	private AlertStationSender sender;

	@BeforeClass
	public static void setUpClass() {
		String path = ConfigTest.class.getClassLoader().getResource("etc/sms_config.xml").getPath();
		System.setProperty(Config.CONFIG_PATH_PROPERTY, path);
		injector = Guice.createInjector(new SMSModule());
	}

	@Before
	public void setUp() throws Exception {
		sender = injector.getInstance(AlertStationSender.class);
	}

	@After
	public void tearDown() throws Exception {
		sender = null;
	}

	@Ignore
	@Test
	public void test() {
		SMSStationEvent event = new SMSStationEvent(new InboundMessage(new Date(), "5493416527111", "A|T|22.3|81", 1,
				""));
		event.setAlertCode("T");
		event.setValue("22.3");
		Customer c = new Customer();
		Station st = new Station();
		st.setCustomer(c);
		st.setId(5);
		c.setSimCards(new ArrayList<SimCard>());
		event.setCustomer(c);
		event.setStation(st);
		sender.process(event);
	}

}
