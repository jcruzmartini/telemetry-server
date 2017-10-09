package com.techner.tau.server.core.calculator.measure.daily;

import java.rmi.ServerException;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.techner.tau.server.core.calculator.measure.MeasureCalculator;
import com.techner.tau.server.core.config.Config;
import com.techner.tau.server.core.guice.SMSModule;
import com.techner.tau.server.domain.model.AditionalInformation;
import com.techner.tau.server.domain.model.Measure;
import com.techner.tau.server.domain.model.Station;

public class EvapotranspirationCalculatorTest {

	// Lista de variables obligatorias
	private final static List<String> CODES = Arrays.asList(MeasureCalculator.TEMPERATURE_CODE,
			MeasureCalculator.HUMIDITY_CODE, MeasureCalculator.WIND_VELOCITY_CODE, MeasureCalculator.RADIATION_CODE,
			MeasureCalculator.PRESURE_CODE, MeasureCalculator.EVAP_CODE);
	private static Injector injector;
	private EvapotranspirationCalculator calculator;

	@BeforeClass
	public static void setUpClass() {
		String path = EvapotranspirationCalculatorTest.class.getClassLoader().getResource("etc/sms_config.xml")
				.getPath();
		System.setProperty(Config.CONFIG_PATH_PROPERTY, path);
		injector = Guice.createInjector(new SMSModule());
	}

	@Before
	public void setUp() throws Exception {
		calculator = injector.getInstance(EvapotranspirationCalculator.class);
	}

	@After
	public void tearDown() throws Exception {
		calculator = null;
	}

	@Test
	public void test() {
		Station station = new Station();
		station.setId(9);
		AditionalInformation info = new AditionalInformation();
		info.setAltitude(116d);
		info.setLatitude(-32.363606);
		station.setInformation(info);
		try {
			Measure m = calculator.calculate(MeasureCalculator.EVAP_CODE, station, CODES);
			Assert.assertNotNull(m);
		} catch (ServerException e) {
			e.printStackTrace();
		}
	}
}
