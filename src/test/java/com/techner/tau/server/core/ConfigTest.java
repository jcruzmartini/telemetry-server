package com.techner.tau.server.core;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.regex.Pattern;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.techner.tau.server.core.config.Config;

public class ConfigTest {

	private Config config;

	@Before
	public void setUp() throws ConfigurationException {
		String path = ConfigTest.class.getClassLoader().getResource("etc/sms_config.xml").getPath();
		config = new Config(path);
	}

	@After
	public void tearDown() {
		config = null;
	}

	@Test
	public void testRead() {
		assertNotNull(config.getExcludedVariablesToSave());
		assertNotNull(config.getDaemonReceptorSleepTime());
		assertNotNull(config.getDaemonSenderSleepTime());
		assertNotNull(config.getThreadReceptorSleepTime());
		assertNotNull(config.getThreadSenderSleepTime());
		assertNotNull(config.getQueryText());
		assertNotNull(config.getRegExpAlert());
		assertNotNull(config.getRegExpDailySummary());
		assertNotNull(config.getRegExpError());
		assertNotNull(config.getRegExpStatus());
		assertNotNull(config.getHostname());
		assertNotNull(config.getEnvironment());
		assertNotNull(config.getEmailToReportError());
		assertNotNull(config.getEndpointServices());
		assertNotNull(config.getTokenForServices());
		assertTrue(config.getAdminCels().length != 0);
	}

	@Test
	public void cleanList() {
		String regDaily = StringUtils.replace(config.getRegExpDailySummary(), "meteovar", "T|L|H|P");
		String regStatus = StringUtils.replace(config.getRegExpStatus(), "events", "ON|MEMFULL|BAT");
		String regAlert = StringUtils.replace(config.getRegExpAlert(), "alerttypes", "T|L|H");

		assertTrue(Pattern.compile(regDaily).matcher("D|130105|01:15|T:20.62*L:0.000*H:89.95*P:999.41|01").matches());
		assertTrue(Pattern.compile(regDaily).matcher("R|130106|15:46|T:-3.66*P:998.11*H:48.76*L:4|29").matches());
		assertTrue(Pattern.compile(regDaily).matcher("D|130105|01:15|T:1.0*L:0*H:-89.95*P:999.41|1").matches());
		assertTrue(Pattern.compile(regDaily).matcher("R|130106|15:46|T:-3.66*P:998*H:8.76*L:-3|29").matches());
		assertTrue(Pattern.compile(regStatus).matcher("EST|130202|21:10|ON|15|05").matches());
		assertTrue(Pattern.compile(regAlert).matcher("A|T|21.0|43").matches());
		assertTrue(Pattern.compile(regAlert).matcher("A|T|-21.0|43").matches());
		assertTrue(Pattern.compile(regAlert).matcher("A|T|21|43").matches());
		assertTrue(Pattern.compile(regStatus).matcher("EST|000110|14:49|ON|12-04BC-C907|01").matches());

	}

}
