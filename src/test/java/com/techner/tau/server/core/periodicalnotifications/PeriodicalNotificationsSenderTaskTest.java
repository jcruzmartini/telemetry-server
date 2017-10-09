package com.techner.tau.server.core.periodicalnotifications;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.techner.tau.server.core.ConfigTest;
import com.techner.tau.server.core.config.Config;
import com.techner.tau.server.core.guice.SMSModule;
import com.techner.tau.server.domain.model.AditionalInformation;
import com.techner.tau.server.domain.model.Customer;
import com.techner.tau.server.domain.model.Email;
import com.techner.tau.server.domain.model.PeriodicalNotification;
import com.techner.tau.server.domain.model.Station;

public class PeriodicalNotificationsSenderTaskTest {

	private static Injector injector;
	private PeriodicalNotificationsCheckerTask processor;
	private PeriodicalNotificationsSender task;
	private PeriodicalNotificationsSender.Factory factory;

	@BeforeClass
	public static void setUpClass() {
		String path = ConfigTest.class.getClassLoader().getResource("etc/sms_config.xml").getPath();
		System.setProperty(Config.CONFIG_PATH_PROPERTY, path);
		injector = Guice.createInjector(new SMSModule());
	}

	@Before
	public void setUp() throws Exception {
		processor = injector.getInstance(PeriodicalNotificationsCheckerTask.class);
		factory = injector.getInstance(PeriodicalNotificationsSender.Factory.class);
	}

	@After
	public void tearDown() throws Exception {
		processor = null;
	}

	@Test
	public void test() {
		processor.run();
	}

	@Test
	@Ignore
	public void testSendEmailTask() {
		PeriodicalNotification no = new PeriodicalNotification();
		no.setEmailEnable(true);
		AditionalInformation info = new AditionalInformation();
		info.setLocation("Estancia Martini - MAIL TEST");
		Station st = new Station();
		Customer cs = new Customer();
		Email e1 = new Email();
		e1.setEmail("jcruzmartini@gmail.com");
		Email e2 = new Email();
		e2.setEmail("cfontana0@gmail.com");
		List<Email> emails = new ArrayList<Email>();
		emails.add(e1);
		emails.add(e2);
		cs.setName("Juan Carlos");
		cs.setLastName("Claramonte");
		cs.setEmails(emails);
		st.setId(5);
		st.setCustomer(cs);
		st.setInformation(info);
		no.setStation(st);
		task = factory.create(no);
		task.run();
	}

	@Test
	public void testSendSMSTask() {
		PeriodicalNotification no = new PeriodicalNotification();
		no.setEmailEnable(false);
		no.setSmsEnable(true);
		AditionalInformation info = new AditionalInformation();
		info.setLocation("Estancia Martini - MAIL TEST");
		Station st = new Station();
		Customer cs = new Customer();
		Email e1 = new Email();
		e1.setEmail("jcruzmartini@gmail.com");
		Email e2 = new Email();
		e2.setEmail("cfontana0@gmail.com");
		List<Email> emails = new ArrayList<Email>();
		emails.add(e1);
		emails.add(e2);
		cs.setName("Juan Carlos");
		cs.setLastName("Claramonte");
		cs.setEmails(emails);
		st.setId(5);
		st.setCustomer(cs);
		st.setInformation(info);
		no.setStation(st);
		task = factory.create(no);
		task.run();
	}
}
