package com.techner.tau.server.core.calculator.measure.hourly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import com.techner.tau.server.domain.model.Measure;
import com.techner.tau.server.domain.model.Variable;

public class HeatIndexCalculatorTest {

    // Lista de variables obligatorias
    private final static List<String> CODES = Arrays.asList(MeasureCalculator.TEMPERATURE_CODE,
            MeasureCalculator.HUMIDITY_CODE, MeasureCalculator.WIND_VELOCITY_CODE, MeasureCalculator.RADIATION_CODE,
            MeasureCalculator.PRESURE_CODE, MeasureCalculator.EVAP_CODE);
    private static Injector injector;
    private HeatIndexCalculator calculator;

    @BeforeClass
    public static void setUpClass() {
        String path = HeatIndexCalculatorTest.class.getClassLoader().getResource("etc/sms_config.xml").getPath();
        System.setProperty(Config.CONFIG_PATH_PROPERTY, path);
        injector = Guice.createInjector(new SMSModule());
    }

    @Before
    public void setUp() throws Exception {
        calculator = injector.getInstance(HeatIndexCalculator.class);
    }

    @After
    public void tearDown() throws Exception {
        calculator = null;
    }

    @Test
    public void test() {
        Measure m = calculator.calculate(MeasureCalculator.HEAT_INDEX_CODE, getListMeasures(), CODES);
        Assert.assertNotNull(m);
    }

    private List<Measure> getListMeasures() {
        Variable v1 = new Variable();
        v1.setCode("T");
        v1.setDescription("Temperatura");
        v1.setSmsDescription("Temp");
        v1.setUnit("°C");
        Variable v2 = new Variable();
        v2.setCode("H");
        v2.setSmsDescription("Humedad");
        v2.setDescription("Humedad");
        v2.setUnit("H");
        Measure m1 = new Measure();
        m1.setDate(new Date());
        m1.setValue(4.1d);
        m1.setVariable(v1);
        Measure m2 = new Measure();
        m2.setDate(new Date());
        m2.setValue(89d);
        m2.setVariable(v2);

        List<Measure> list = new ArrayList<Measure>();
        list.add(m2);
        list.add(m1);
        return list;
    }
}
