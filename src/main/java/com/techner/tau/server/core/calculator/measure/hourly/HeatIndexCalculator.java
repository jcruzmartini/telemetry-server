package com.techner.tau.server.core.calculator.measure.hourly;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.techner.tau.server.core.calculator.measure.GenericMeasureCalculator;
import com.techner.tau.server.core.calculator.measure.MeasureCalculator;
import com.techner.tau.server.domain.model.Measure;
import com.techner.tau.server.domain.model.Station;
import com.techner.tau.server.services.VariableService;

public class HeatIndexCalculator extends GenericMeasureCalculator {

    @Inject
    public HeatIndexCalculator(VariableService variableService) {
        super(variableService);
    }

    /** slf4j logger */
    private static final Logger logger = LoggerFactory.getLogger(HeatIndexCalculator.class);
    /** Codigo de la variable a calculatr */
    private String variableCode;
    /** Fecha de la medida */
    private Date date;

    @Override
    public Measure calculate(String variable, List<Measure> measures, List<String> enablesVars) {
        if (enablesVars != null) {
            if (!enablesVars.contains(TEMPERATURE_CODE) || !enablesVars.contains(HUMIDITY_CODE)) {
                logger.warn("Escapando el calculo de ITH. Variables [{}] [{}] necesarias, no habilitadas",
                        TEMPERATURE_CODE, HUMIDITY_CODE);
                return null;
            }
        }
        this.variableCode = variable;
        this.date = getDateFromMeasures(measures);

        Double temp = getMeasureValue(TEMPERATURE_CODE, measures);
        Double hum = getMeasureValue(HUMIDITY_CODE, measures);

        if (temp == null || hum == null) {
            return null;

        }
        logger.info("Calculando la variable : {}", variableCode);
        return calculateHeatIndex(temp, hum);
    }

    /***
     * Calculate heat index
     * 
     * @param temp
     *            temperatura
     * @param hum
     *            humedad
     * @return heat index calculado
     */
    private Measure calculateHeatIndex(Double temp, Double hum) {
        Double Tn, m;
        Measure hI = getNewMeasure(variableCode, date);
        if (hI == null) {
            return hI;
        }

        if (temp < 0) {
            Tn = 272.62d;
            m = 22.46d;
        } else {
            Tn = 243.12d;
            m = 17.62d;
        }

        Double tAux = (m * temp) / (Tn + temp);
        Double p = (hum / 100.0d) * Math.exp(tAux);
        Double HIC = temp + (5.0d / 9.0d) * (p - 10.0d);
        
        
        String formated = String.format(Locale.US, "%.2f%n", HIC);
        hI.setValue(Double.valueOf(formated));

        return hI;
    }

    @Override
    public Period getPeriod() {
        return MeasureCalculator.Period.hourly;
    }

    @Override
    public Measure calculate(String variable, Station station, List<String> enableVars) {
        throw new UnsupportedOperationException("Metodo no implementado para " + this.getClass());
    }
}
