package com.techner.tau.server.core.utils;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.techner.tau.server.core.calculator.sunset.SunriseSunsetCalculator;

public class SunriseSunsetCalculatorTest {

	private SunriseSunsetCalculator calculator;

	@Before
	public void setUp() throws Exception {
		calculator = new SunriseSunsetCalculator(-32.719719, -62.105167, new Date(), 0);
	}

	@After
	public void tearDown() throws Exception {
		calculator = null;
	}

	@Test
	public void test() {
		System.out.println(calculator.isSunDown());
		System.out.println(calculator.isSunUp());
		System.out.println(calculator.isSunrise());
		System.out.println(calculator.getSunset());
		System.out.println(calculator.getSunrise());
	}

}
