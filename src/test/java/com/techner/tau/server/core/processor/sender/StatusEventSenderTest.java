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
import com.techner.tau.server.domain.model.Customer;
import com.techner.tau.server.domain.model.EventStatus;
import com.techner.tau.server.domain.model.SMSStatus;
import com.techner.tau.server.domain.model.SimCard;
import com.techner.tau.server.domain.model.Station;

public class StatusEventSenderTest {

	private StatusEventSender sender;
	private static Injector injector;

	@BeforeClass
	public static void setUpClass() {
		String path = ConfigTest.class.getClassLoader().getResource("etc/sms_config.xml").getPath();
		System.setProperty(Config.CONFIG_PATH_PROPERTY, path);
		injector = Guice.createInjector(new SMSModule());
	}

	@Before
	public void setUp() throws Exception {
		sender = injector.getInstance(StatusEventSender.class);
	}

	@After
	public void tearDown() throws Exception {
		sender = null;
	}

	@Test
	public void test() {
		SMSStatus sms = new SMSStatus(new InboundMessage(new Date(), "5493416527111", "EST|130617|18:46|ON|15|04", 1,
				""));
		Customer customer = new Customer();
		customer.setLastName("Martini");
		customer.setName("Juan Cruz");
		SimCard sim = new SimCard();
		sim.setCompany("CLARO");
		sim.setNumber("+543415460866");
		Station station = new Station();
		station.setId(5);
		station.setSimCard(sim);
		station.setCustomer(customer);
		EventStatus event = new EventStatus();
		event.setCode("EST");
		event.setCritical(false);
		event.setDescription("Reinicio de Estacion");
		sms.setEvent(event);
		sms.setStation(station);
		sender.process(sms);
	}

}
