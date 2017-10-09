package com.techner.tau.server.core.processor.operation;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Assert;
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
import com.techner.tau.server.core.processor.receptor.OperationResponseProcessor;

public class NewAlertPostProcessorTest {

	private static Injector injector;
	private OperationResponseProcessor processor;

	@BeforeClass
	public static void setUpClass() {
		String path = ConfigTest.class.getClassLoader().getResource("etc/sms_config.xml").getPath();
		System.setProperty(Config.CONFIG_PATH_PROPERTY, path);
		injector = Guice.createInjector(new SMSModule());
	}

	@Before
	public void setUp() throws Exception {
		processor = injector.getInstance(OperationResponseProcessor.class);
	}

	@After
	public void tearDown() throws Exception {
		processor = null;
	}

	@Test
	@Ignore
	public void test() {
		processor.process(new InboundMessage(new Date(), "5493416527111", "RTA|0003|81", 1, ""));
	}

	@Test
	public void testExtract() {
		String value = "78.000002";
		if (StringUtils.contains(value, ".")) {
			String integer = StringUtils.substringBefore(value, ".");
			String decimal = StringUtils.substringAfter(value, ".");
			if (!StringUtils.isBlank(decimal) && decimal.length() > 2) {
				decimal = StringUtils.left(decimal, 2);
			}
			value = integer + "." + decimal;
		}
		Assert.assertEquals("78.00", value);
	}

}
