package com.techner.tau.server.core.mailer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.techner.tau.common.entity.StationInfo;
import com.techner.tau.server.core.config.Config;
import com.techner.tau.server.core.guice.SMSModule;
import com.techner.tau.server.domain.model.AditionalInformation;
import com.techner.tau.server.domain.model.Customer;
import com.techner.tau.server.domain.model.Email;
import com.techner.tau.server.domain.model.Measure;
import com.techner.tau.server.domain.model.Station;
import com.techner.tau.server.domain.model.Variable;

public class MailerServiceTest {

	private MailerService service;
	private static Injector injector;

	@BeforeClass
	public static void setUpClass() {
		String path = MailerServiceTest.class.getClassLoader().getResource("etc/sms_config.xml").getPath();
		System.setProperty(Config.CONFIG_PATH_PROPERTY, path);
		injector = Guice.createInjector(new SMSModule());
	}

	@Before
	public void setUp() throws Exception {
		service = injector.getInstance(MailerService.class);
	}

	@After
	public void tearDown() throws Exception {
		service = null;
	}

	@Test
	public void testErrorReportMail() {
		List<String> emails = new ArrayList<String>();
		emails.add("jcruzmartini@gmail.com");
		service.sendErrorReportMail(new NumberFormatException("Error convirtiendo a Double"));
	}

	@Test
	public void testSendWarnMail() {
		Set<String> emails = new HashSet<String>();
		emails.add("jcruzmartini@gmail.com");
		service.sendWarningStationMail(emails, "234 - (+544315460866)", "Martini, Juan Cruz",
				"Reinicio de la estación");
	}
	
	@Test
	public void testSendCustomMail() {
	       AditionalInformation info = new AditionalInformation();
	       info.setLocation("INTA Marcos Juarez");
	       Station station = new Station();
	       Email email = new Email();
	       email.setEmail("jcruzmartini@gmail.com");
	       Email email1 = new Email();
	       email1.setEmail("soporte@techner.com.ar");
	       Customer cus = new Customer();
	       cus.setName("Prueba NUEVA");
	       cus.setLastName("OTRA");
	       cus.setEmails(Arrays.asList(email, email1));
	       station.setCustomer(cus);
	       station.setId(6);
	       station.setInformation(info);
	      
	       Set<String> emails = new HashSet<String>();
	       emails.add("jcruzmartini@gmail.com");
	       emails.add("cfontana0@gmail.com");
	       service.sendCustomStationMail(emails, station, "Martini, Juan Cruz", "Reinicio de la estación", 
	                "Alerta Temperatura : -2.2 °C");
	}

	@Test
	public void testSendCriticalMail() {
		Set<String> emails = new HashSet<String>();
		emails.add("jcruzmartini@gmail.com");
		service.sendCriticalStationMail(emails, "234 - (+544315460866)", "Martini, Juan Cruz",
				"Nivel de Bateria menor al 10%");
	}

	@Test
	public void testSendInfoMail() {
		Set<String> emails = new HashSet<String>();
		emails.add("jcruzmartini@gmail.com");
		service.sendInformationStationMail(emails, "234 - (+544315460866)", "Martini, Juan Cruz",
				"Este es un mail de información");
	}

	@Test
	public void testSendPeriodicalNotificationsMail() {
		Set<String> emails = new HashSet<String>();
		AditionalInformation info = new AditionalInformation();
		info.setLocation("Estancia Martini");
		emails.add("jcruzmartini@gmail.com");
		List<Measure> measures = getListMeasures();
		Station station = new Station();
		Customer cus = new Customer();
		cus.setName("Juan Cruz");
		cus.setLastName("Martini");
		station.setCustomer(cus);
		station.setId(5);
		station.setInformation(info);
		service.sendPeriodicNotificationEmail(emails, null, station);
	}

	private List<Measure> getListMeasures() {
		Variable v1 = new Variable();
		v1.setCode("T");
		v1.setDescription("Temperatura");
		v1.setSmsDescription("Temp");
		v1.setUnit("°C");
		Variable v2 = new Variable();
		v2.setCode("P");
		v2.setSmsDescription("Presión");
		v2.setDescription("Presión Atmosferica");
		v2.setUnit("hPa");
		Variable v3 = new Variable();
		v3.setCode("L");
		v3.setDescription("Lluvia");
		v3.setUnit("mm");
		Measure m1 = new Measure();
		m1.setDate(new Date());
		m1.setValue(24.67d);
		m1.setVariable(v1);
		Measure m2 = new Measure();
		m2.setDate(new Date());
		m2.setValue(20.67d);
		m2.setVariable(v2);
		Measure m3 = new Measure();
		m3.setDate(new Date());
		m3.setValue(100d);
		m3.setVariable(v3);
		Variable v4 = new Variable();
		v4.setCode("DP");
		v4.setDescription("Punto de Rocío");
		v4.setUnit("°C");
		Variable v5 = new Variable();
		v5.setCode("ITH");
		v5.setDescription("Indice de Temperatura y Humedad");
		v5.setUnit("");
		Variable v6 = new Variable();
		v6.setCode("HI");
		v6.setDescription("Indice de Calor");
		v6.setUnit("°C");

		Measure m4 = new Measure();
		m4.setDate(new Date());
		m4.setValue(34d);
		m4.setVariable(v4);
		Measure m5 = new Measure();
		m5.setDate(new Date());
		m5.setValue(56d);
		m5.setVariable(v5);
		Measure m6 = new Measure();
		m6.setDate(new Date());
		m6.setValue(23d);
		m6.setVariable(v6);
		List<Measure> list = new ArrayList<Measure>();
		list.add(m2);
		list.add(m3);
		list.add(m1);
		list.add(m4);
		list.add(m5);
		list.add(m6);
		return list;
	}
}
