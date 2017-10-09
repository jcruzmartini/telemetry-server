package com.techner.tau.server.core.twitter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import twitter4j.Twitter;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.techner.tau.server.core.ConfigTest;
import com.techner.tau.server.core.calculator.measure.hourly.DewPointCalculator;
import com.techner.tau.server.core.config.Config;
import com.techner.tau.server.core.guice.SMSModule;
import com.techner.tau.server.domain.model.AditionalInformation;
import com.techner.tau.server.domain.model.Measure;
import com.techner.tau.server.domain.model.Station;
import com.techner.tau.server.domain.model.Variable;

public class TwitterUpdateStatusTaskImplTest {

	TwitterUpdateStatusTaskImpl task;
	private static Injector injector;

	@BeforeClass
	public static void setUpClass() {
		String path = ConfigTest.class.getClassLoader().getResource("etc/sms_config.xml").getPath();
		System.setProperty(Config.CONFIG_PATH_PROPERTY, path);
		injector = Guice.createInjector(new SMSModule());
	}

	@Before
	public void setUp() throws Exception {
		Twitter tw = injector.getInstance(Twitter.class);
		task = new TwitterUpdateStatusTaskImpl(tw, getListMeasures(), getStation());
	}

	@After
	public void tearDown() throws Exception {
		task = null;
	}

	@Test
	public void test() {
		task.run();
	}

	private List<Measure> getListMeasures() {

		List<Measure> list = new ArrayList<Measure>();
		Measure m = new Measure();
		Variable v = new Variable();
		v.setCode(DewPointCalculator.TEMPERATURE_CODE);
		v.setShortDescription("Prueba 1");
		v.setMeteorological(true);
		m.setVariable(v);
		m.setValue(30d);
		Measure m1 = new Measure();
		Variable v1 = new Variable();
		v1.setCode(DewPointCalculator.HUMIDITY_CODE);
		v1.setMeteorological(true);
		v1.setShortDescription("Prueba 2");
		m1.setVariable(v1);
		m1.setValue(40d);
		Measure m2 = new Measure();
		Variable v2 = new Variable();
		v2.setCode("B");
		v2.setShortDescription("Prueba 3");
		v2.setMeteorological(false);
		m2.setVariable(v2);
		m2.setValue(40d);
		Measure m3 = new Measure();
		Variable v3 = new Variable();
		v3.setCode("B");
		v3.setShortDescription("Prueba 4");
		v3.setMeteorological(true);
		m3.setVariable(v3);
		m3.setValue(40d);
		Measure m4 = new Measure();
		Variable v4 = new Variable();
		v4.setCode("B");
		v4.setShortDescription("Prueba 5                      dasdas           sadasdas        dasdsa");
		v4.setMeteorological(true);
		m4.setVariable(v4);
		m4.setValue(40d);
		list.addAll(Arrays.asList(m, m1, m2, m3, m4));
		return list;
	}

	private Station getStation() {
		AditionalInformation info = new AditionalInformation();
		info.setLatitude(43.5d);
		info.setLongitude(54.5d);
		Station st = new Station();
		st.setId(1);
		st.setInformation(info);
		return st;
	}

}
