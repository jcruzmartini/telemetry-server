package com.techner.tau.server.core.utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.techner.tau.server.core.calculator.measure.MeasureCalculator;
import com.techner.tau.server.core.config.Config;
import com.techner.tau.server.domain.model.Measure;
import com.techner.tau.server.domain.model.SMS;
import com.techner.tau.server.domain.model.Variable;
import com.techner.tau.server.services.VariableService;

/**
 * Factoria para la creacion de los objetos de medidas tomadas por la estacion.
 * 
 * @author juan
 * 
 */
public class MeasuresFactory {

    /** Servicio de variables */
    private final VariableService variableService;
    /** Simple Date Format */
    private final String SDF = "yyMMddHH:mm";
    /** slfj4 logger */
    private static final Logger logger = LoggerFactory.getLogger(MeasuresFactory.class);

    /**
     * Constructor
     * 
     * @param variableService
     *            servicio de variables
     */
    @Inject
    public MeasuresFactory(VariableService variableService) {
        this.variableService = variableService;
    }

    /**
     * Obtiene mapa de medidas para una medida en formato texto
     * 
     * @param textVarVal
     *            medida en formato texto
     * @return mapa
     */
    public HashMap<Variable, String> getMapOneMeasure(String textVarVal) {
        HashMap<Variable, String> mapHour = new HashMap<Variable, String>();
        String[] variables = textVarVal.split(Config.SPLIT_CHARACTER_VARIABLES);
        if (ArrayUtils.isEmpty(variables)) {
            logger.error("No hay medidas para almacenar ");
            return mapHour;
        }
        for (String varVal : variables) {
            String[] variableValue = varVal.split(Config.SPLIT_CHARACTER_MEASURE_OR_HOURSMINS_VALUE);
            if (variableValue == null || variableValue.length != 2) {
                logger.error("Measure with bad format. Correct format is: <variable:value> ");
            } else {
                Variable variable = variableService.getVariableByCode(variableValue[0]);
                if (variable != null) {
                    mapHour.put(variable, variableValue[1]);
                } else {
                    logger.error("Variable con identificador {} no existe", variableValue[0]);
                }
            }
        }
        return mapHour;
    }

    /**
     * Obtiene lista de objectos con todas las medidas a partir de las medidas
     * en formato texto
     * 
     * @param sms
     *            mensaje con las medidas
     * @return lista de medidas
     */
    public List<Measure> getListOfMeasures(SMS sms) {
        List<Measure> listMeasure = new ArrayList<Measure>();
        Date readyDate = null;

        String[] typeMeasures = sms.getText().split(Config.SPLIT_CHARACTER_GENERAL);
        String date = typeMeasures[1];
        String[] typeMeasuresCleaned = ArrayUtils.subarray(typeMeasures, 2, typeMeasures.length);

        HashMap<String, HashMap<Variable, String>> mapOfMapsVariableValue = getMapOfMapVariableValue(typeMeasuresCleaned);
        for (Entry<String, HashMap<Variable, String>> entryParent : mapOfMapsVariableValue.entrySet()) {
            try {
                readyDate = DateUtils.parseDate(date + entryParent.getKey(), SDF);
            } catch (ParseException e) {
                logger.error("Error parseando fecha ");
                continue;
            }
            for (Entry<Variable, String> entryChild : entryParent.getValue().entrySet()) {
                Measure measure = new Measure();
                measure.setDate(sms.getDate());
                measure.setVariable(entryChild.getKey());
                // TODO: hotfix horrible para velocidad de viento
                if (measure.getVariable().getCode().equals(MeasureCalculator.WIND_VELOCITY_CODE)) {
                    measure.setValue(Double.valueOf(entryChild.getValue()) * 2.3);
                } else {
                    measure.setValue(Double.valueOf(entryChild.getValue()));
                }
                listMeasure.add(measure);
                logger.debug("Creada medida {} en fecha {} ", entryChild, readyDate);
            }
        }
        return listMeasure;
    }

    /**
     * Obtiene el mapa de todas las variables con sus respectivos valores
     * 
     * @param typeMeasuresCleaned
     * @return mapa
     */
    private HashMap<String, HashMap<Variable, String>> getMapOfMapVariableValue(String[] typeMeasuresCleaned) {
        HashMap<String, HashMap<Variable, String>> hashTotal = new HashMap<String, HashMap<Variable, String>>();

        if (ArrayUtils.isEmpty(typeMeasuresCleaned)) {
            logger.error("No hay medidas para almacenar");
            return hashTotal;
        }

        HashMap<Variable, String> mapHour = getMapOneMeasure(typeMeasuresCleaned[1]);
        hashTotal.put(typeMeasuresCleaned[0], mapHour);
        return hashTotal;
    }
}
