package com.techner.tau.server.core.calculator.measure.daily;

import java.rmi.ServerException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.techner.tau.common.entity.ActualMeasuresResult;
import com.techner.tau.common.entity.MeasureResult;
import com.techner.tau.common.entity.ServiceResult;
import com.techner.tau.common.entity.TAUActualMeasuresResult;
import com.techner.tau.common.entity.TAUServiceResult;
import com.techner.tau.common.entity.Variable;
import com.techner.tau.server.core.annotation.EndpointURL;
import com.techner.tau.server.core.annotation.TokenForServices;
import com.techner.tau.server.core.calculator.measure.GenericMeasureCalculator;
import com.techner.tau.server.core.calculator.measure.MeasureCalculator;
import com.techner.tau.server.domain.model.AditionalInformation;
import com.techner.tau.server.domain.model.Measure;
import com.techner.tau.server.domain.model.Station;
import com.techner.tau.server.services.VariableService;

/**
 * Calculador de la variable de evapotraspiración
 * 
 * @author juan
 * 
 */
public class EvapotranspirationCalculator extends GenericMeasureCalculator {
    /** slf4j logger */
    private static final Logger logger = LoggerFactory.getLogger(EvapotranspirationCalculator.class);
    /** path del servicio a llamar */
    private static final String SERVICE = "measures/summary/daily/";
    /** Codigo de la variable a calculatr */
    private String variableCode;
    /** Fecha de la medida */
    private Date date;
    /** Rest client */
    private final Client client;
    /** end point */
    private static final String SDF = "yyyy/MM/dd";
    /** end point */
    private final String endpoint;
    /** token */
    private final String token;
    /** Dias que tiene el año */
    private static final int NUM_DAYS_YEAR = 365;
    /** Gsc constante solar = 0,082 */
    private static final double GSC = 0.082;
    /** Coeficiente especifico del cultivo ALBEDO */
    private static double COEF_CULTIVO = 0.23;
    /** constante de Stefan-Boltzmann */
    private static final double CSBOLTZ = 0.000000005;
    /** calor latente de vaporización, 2,45 [ MJ kg-1], */
    private static final double CLV = 2.45;
    /** calor específico a presión constante, */
    private static final double CP = 0.001013;
    /** ε cociente del peso molecular de vapor de agua /aire seco = 0,622. */
    private static final double CPM = 0.622;
    /** Lista de variables obligatorias */
    private final static List<String> CODES = Arrays.asList(TEMPERATURE_CODE, HUMIDITY_CODE, WIND_VELOCITY_CODE,
            RADIATION_CODE, PRESURE_CODE);

    @Inject
    public EvapotranspirationCalculator(VariableService variableService, Client client, @EndpointURL String endpoint,
            @TokenForServices String token) {
        super(variableService);
        this.client = client;
        this.endpoint = endpoint;
        this.token = token;
    }

    @Override
    public Period getPeriod() {
        return MeasureCalculator.Period.daily;
    }

    @Override
    public Measure calculate(String variable, Station station, List<String> enableVars) throws ServerException {
        if (enableVars != null) {
            if (!enableVars.containsAll(CODES)) {
                logger.warn("Escapando el calculo de la Evapotraspiración. "
                        + "Algunas de las variable necesarias para el calculo, no habilitadas");
                return null;
            }
        }
        this.variableCode = variable;
        logger.info("Calculando la variable : {}", variableCode);
        return calculateEvapo(station);
    }

    /**
     * Calcular evaportraspiración
     * 
     * @return evapotraspiración
     * @throws ServerException
     */
    private Measure calculateEvapo(Station station) throws ServerException {
        AditionalInformation info = station.getInformation();
        SimpleDateFormat sdf = new SimpleDateFormat(SDF);

        // Altitud del lugar donde esta instalada la estacion,
        double altitud = info.getAltitude(); // mts

        // Latitud del lugar donde esta instalada la estacion
        double latitud = info.getLatitude();

        // Latitud en Radianes
        double latitudRad = ((latitud * Math.PI) / 180);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        // Calculo la del dia anterior
        cal.add(Calendar.DAY_OF_YEAR, -1);
        cal.set(Calendar.MINUTE, 55);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.HOUR_OF_DAY, 23);

        date = cal.getTime();

        // Numero de día donde 1 es el 1 de Enero y NUM_DAYS_YEAR es el 31 de
        // Dic
        int dayOfYear = cal.get(Calendar.DAY_OF_YEAR);
        String strDate = sdf.format(date);

        logger.debug("Dia del año.     Día = {}", date);
        logger.debug("Numero de dia del año.     NuD = {}", dayOfYear);

        Map<String, com.techner.tau.common.entity.Measure> results = callRestService(station.getId(), strDate);

        if (results == null) {
            return null;
        }

        // Temperatura ºC
        double tempMax = results.get(TEMPERATURE_CODE).getMax();
        double tempMin = results.get(TEMPERATURE_CODE).getMin();

        // Humedad %
        double HRMax = results.get(HUMIDITY_CODE).getMax();
        double HRMin = results.get(HUMIDITY_CODE).getMin();

        // u2, velocidad de viento promedio diaria a 2 metros de altura
        // Convierto k/m a m/s
        double vViento = (results.get(WIND_VELOCITY_CODE).getAvg() * 1000) / 3600;
        // Rs promedio diario
        double radiacion = results.get(RADIATION_CODE).getAvg(); // MJ m-2
        // Convierto W m-2 a MJ m-2
        radiacion = radiacion * 0.0864;

        // Presion ajustada al nivel del mar
        // convierto de hPa a KPa
        double presion = results.get(PRESURE_CODE).getAvg() / 10;

        double tempMedia = (tempMax + tempMin) / 2;
        double tempA = tempMedia + 237.3f;

        // Pendiente de la curva de presión de saturación de vapor // ec13
        double pcv = ((4098f * calcularDeficitVapor(tempMedia)) / (Math.pow(tempA, 2)));
        logger.debug("Pendiente curva de presion.  Δ = {}", pcv);

        // constante psicrométrica //ec8
        double cpsico = (CP * presion) / (CPM * CLV);
        logger.debug("Constante psicrométrica.     γ = {}", cpsico);

        double ecp1 = (1 + 0.34f * vViento);

        // / --------------PARA USAR AL FINAL
        double ecp11 = (pcv + cpsico * ecp1);
        double ecpf2 = pcv / ecp11;
        double ecpf3 = cpsico / ecp11;
        double ecpf4 = (900f / (tempMedia + 273f)) * vViento;
        // / ------------- PARA USAR AL FINAL

        // deficit de vapor max //ec11
        double dvemax = calcularDeficitVapor(tempMax);
        // deficit de vapor min //ec11
        double dvemin = calcularDeficitVapor(tempMin);
        // deficit de vapor med //ec12
        double dvemed = (dvemax + dvemin) / 2;

        // Presión real de vapor (ea) derivada de datos de humedad relativa
        // //ec17
        double prv = (dvemin * (HRMax / 100) + dvemax * (HRMin / 100)) / 2;

        // Déficit de presión de vapor
        // / --------------PARA USAR AL FINAL
        double dpv = dvemed - prv;
        logger.debug("Déficit presión vapor. (es-ea) = {}", dpv);

        double numDayAux = ((2 * Math.PI * dayOfYear) / NUM_DAYS_YEAR);

        // La distancia relativa inversa Tierra-Sol, dr
        double dr = (1 + 0.033 * Math.cos(numDayAux));
        logger.debug("D. rel. inv. tierra-sol.  (dr) = {}", dr);

        // Decinacion solar δ
        double decSol = (0.409 * Math.sin(numDayAux - 1.39f));
        logger.debug("Declinación solar.           δ = {}", decSol);

        // Angulo de radiacion a la puesta del sol (ws)
        double raSin = (Math.sin(latitudRad) * Math.sin(decSol));
        double raCos = (Math.cos(latitudRad) * Math.cos(decSol));
        double raTan = (-Math.tan(latitudRad) * Math.tan(decSol));

        double aRadPSol = (float) Math.acos(raTan);
        logger.debug("Angulo rad puesta del sol (ws) = {}", aRadPSol);

        // Radiación extraterrestre para periodos diarios (Ra)
        double ra = (((24 * 60) / Math.PI) * GSC * dr * (aRadPSol * raSin + raCos * Math.sin(aRadPSol)));
        logger.debug("Rad. extraterrestre.      (Ra) = {}", ra);

        // Radiacion dia despejado //ec37
        double rso = ra * (0.75f + 2 * 0.00002f * altitud);
        logger.debug("Rad. dia despejado.      (Rso) = {}", rso);

        // Radiacion neta solar onda corta Rns
        double rns = (1f - COEF_CULTIVO) * radiacion;
        logger.debug("Rad. Neta onda corta.    (Rns) = {}", rns);

        // Radiacion neta solar onda larga Rnl part1 //ec39
        double rnlp11 = CSBOLTZ * Math.pow((tempMax + 273.16f), 4);
        double rnlp12 = CSBOLTZ * Math.pow((tempMin + 273.16f), 4);
        double rnlp1 = (rnlp11 + rnlp12) / 2;
        // Radiacion neta solar onda larga Rnl part2 //ec39
        double rnlp2 = (0.34f - 0.14f * Math.sqrt(prv));
        // Radiacion neta solar onda larga Rnl part3 //ec39
        double rnlp31 = (radiacion / rso);
        double rnlp3 = 1.35f * rnlp31 - 0.35f;

        // Radiacion neta solar onda larga Rnl Final //ec39
        double rnl = rnlp1 * rnlp2 * rnlp3;
        logger.debug("Rad. Neta onda larga.    (Rnl) = {}", rnl);

        // Radiacion neta //ec40
        double rn = rns - rnl;
        logger.debug("Rad. Neta.                (Rn) = {}", rn);

        // Flujo del calor del suelo, es 0 cuando se usan datos diarios
        double G = 0f;

        // / --------------PARA USAR AL FINAL
        double ecpf5 = 0.408f * (rn - G);
        double ecpf6 = ecpf5 * ecpf2;
        double ecpf7 = ecpf4 * dpv * ecpf3;
        double ETO = ecpf6 + ecpf7;

        logger.debug("Evapotraspiración total.   ETO = {}", ETO);

        Measure measure = getNewMeasure(variableCode, date);
        measure.setValue(ETO);

        return measure;
    }

    /**
     * Construye mapa con las variables y sus valores
     * 
     * @param id
     *            id de la estacion
     * @param date2
     *            fecha a consultar
     * @return resultados
     * @throws ServerException
     */
    private Map<String, com.techner.tau.common.entity.Measure> callRestService(Integer id, String date2)
            throws ServerException {
        ClientResponse response = null;
        ServiceResult<TAUActualMeasuresResult> result = null;
        Map<String, com.techner.tau.common.entity.Measure> results = null;

        StringBuffer sb = new StringBuffer(endpoint).append(SERVICE);
        sb.append(id).append("/").append(date2).append("/").append(listOfCodes()).append("?eToken=").append(token);

        logger.debug("LLAMADA AL SERVICIO :" + sb.toString());

        try {
            WebResource resource = client.resource(sb.toString());
            response = resource.accept(MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);
        } catch (Exception e) {
            logger.error("Error llamando al servicio {} ", sb.toString(), e);
            throw new ServerException("Error llamando al servicio {} " + sb.toString());
        }

        if (response != null && response.getStatus() == Response.Status.OK.getStatusCode()) {
            results = new HashMap<String, com.techner.tau.common.entity.Measure>();
            GenericType<TAUServiceResult<TAUActualMeasuresResult>> type = new GenericType<TAUServiceResult<TAUActualMeasuresResult>>() {
            };
            result = response.getEntity(type);

            if (result.isSuccess()) {
                ActualMeasuresResult summary = result.getResult();
                List<MeasureResult> entries = summary.getEntries();

                if (entries.size() != CODES.size()) {
                    logger.error(
                            "Algunas medidas obligatorias no fueron devueltas por el servicio. Se esperaban {} son {} medidas",
                            CODES.size(), entries.size());
                    throw new ServerException(
                            "Error consultando servicio para calcular Evapo. Algunas medidas obligatorias no fueron devueltas por el servicio");
                }
                for (MeasureResult measureResult : entries) {
                    Variable var = measureResult.getVariable();
                    if (var == null) {
                        logger.error("Objeto Variable no presente");
                        throw new ServerException(
                                "Error con la respuesta del servicio para calcular Evapo. Objeto Variable no presente");
                    }

                    if (CollectionUtils.isEmpty(measureResult.getValues())) {
                        logger.error("Valores no presente");
                        throw new ServerException(
                                "Error con la respuesta del servicio para calcular Evapo. Objeto values no presente");
                    }
                    com.techner.tau.common.entity.Measure values = measureResult.getValues().get(0);
                    results.put(var.getCode(), values);
                }
            }
        } else {
            logger.error("Error ejecutando servicios. {}", response);
            throw new ServerException("Error en llamada al servicio. Respuesta : " + response);
        }

        logger.debug("RESPUESTA DEL SERVICIO : {} . Tabla de Resultados: {}", response, results);

        return results;
    }

    /**
     * Crea lista de cod de var a consultar
     * 
     * @return lista de codigos de variables a consultar
     */
    private String listOfCodes() {
        StringBuffer sb = new StringBuffer();
        for (String code : CODES) {
            if (sb.length() != 0) {
                sb.append(",");
            }
            sb.append(code);
        }
        return sb.toString();
    }

    /**
     * Calcula deficit de vapor
     * 
     * @param tempMedia
     *            temperatura media
     * @return deficit de vapor
     */
    private double calcularDeficitVapor(double tempMedia) {
        double tempA = tempMedia + 237.3f;
        return (0.6108 * Math.exp(((17.27 * tempMedia) / tempA)));
    }

    @Override
    public Measure calculate(String variable, List<Measure> measures, List<String> enableVars) {
        throw new UnsupportedOperationException("Metodo no implementado para " + this.getClass());
    }
}
