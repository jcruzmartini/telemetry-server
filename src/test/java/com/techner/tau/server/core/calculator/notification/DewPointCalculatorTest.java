package com.techner.tau.server.core.calculator.notification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.techner.tau.server.core.calculator.measure.hourly.DewPointCalculator;
import com.techner.tau.server.core.config.Config;
import com.techner.tau.server.core.guice.SMSModule;
import com.techner.tau.server.domain.model.Measure;
import com.techner.tau.server.domain.model.Variable;

public class DewPointCalculatorTest {

	private DewPointCalculator calculator;
	private static Injector injector;
	private List<String> codes;

	@BeforeClass
	public static void setUpClass() {
		String path = DewPointCalculatorTest.class.getClassLoader().getResource("etc/sms_config.xml").getPath();
		System.setProperty(Config.CONFIG_PATH_PROPERTY, path);
		injector = Guice.createInjector(new SMSModule());
	}

	@Before
	public void setUp() throws Exception {
		calculator = injector.getInstance(DewPointCalculator.class);
	}

	@After
	public void tearDown() throws Exception {
		calculator = null;
	}

	@Test
	public void test() {
		codes = new ArrayList(Arrays.asList("T", "H", DewPointCalculator.DEW_POINT_CODE));
		Measure m = new Measure();
		Variable v = new Variable();
		v.setCode(DewPointCalculator.TEMPERATURE_CODE);
		m.setVariable(v);
		m.setValue(30d);
		Measure m1 = new Measure();
		Variable v1 = new Variable();
		v1.setCode(DewPointCalculator.HUMIDITY_CODE);
		m1.setVariable(v1);
		m1.setValue(40d);
		List<Measure> list = new ArrayList<Measure>();
		list.add(m1);
		list.add(m);
		calculator.calculate(DewPointCalculator.DEW_POINT_CODE, list, codes);
	}

}
