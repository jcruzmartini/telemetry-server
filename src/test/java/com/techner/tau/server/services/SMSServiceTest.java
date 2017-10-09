package com.techner.tau.server.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.techner.tau.server.domain.model.Measure;
import com.techner.tau.server.domain.model.Variable;

public class SMSServiceTest {

	private SMSService service;

	@Before
	public void setUp() {
		service = new SMSService(null, null);
	}

	@After
	public void tearDown() {
		service = null;
	}

	@Test
	public void testRandomData() {
		Integer aux = null;
		for (int i = 0; i < 100; i++) {
			Integer now = service.generateTokenNumber();
			Assert.assertNotSame(aux, now);
			aux = now;
		}
	}

	@Test
	public void testBuildFriendlyTest() {
		Variable v1 = new Variable();
		v1.setCode("T");
		v1.setSmsDescription("Temp");
		v1.setUnit("°C");
		Variable v2 = new Variable();
		v2.setCode("P");
		v2.setSmsDescription("Presión");
		v2.setUnit("hPa");

		Measure m1 = new Measure();
		m1.setDate(new Date());
		m1.setValue(24.67d);
		m1.setVariable(v1);
		Measure m2 = new Measure();
		m2.setDate(new Date());
		m2.setValue(20.67d);
		m2.setVariable(v2);

		List<Measure> list = new ArrayList<Measure>();
		list.add(m2);
		list.add(m1);

		String text = service.buildFriendlyText(list);
		Assert.assertNotNull(text);
	}

}
