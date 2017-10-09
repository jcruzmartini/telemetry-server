package com.techner.tau.server.core.monitor;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.techner.tau.server.core.config.Config;
import com.techner.tau.server.core.mailer.Mailer;
import com.techner.tau.server.domain.model.SMS;
import com.techner.tau.server.domain.model.Station;
import com.techner.tau.server.services.MonitoringService;
import com.techner.tau.server.services.SMSService;

/**
 * Tarea para chequear el estado de las estaciones y operaciones en timeout
 * 
 * @author jmartini
 * 
 */
public class MonitoringStationCheckerTask implements MonitoringStationChecker {

    /** slf4j logger. */
    private static final Logger logger = LoggerFactory.getLogger(MonitoringStationCheckerTask.class);
    /** servicio de monitore */
    private final MonitoringService monitoring;
    /** enviador de mail */
    private final Mailer mailer;
    /** config */
    private final Config config;
    /** SMS service */
    private final SMSService service;
    /** mensaje de error */
    private final String ERROR = "Estación fuera de línea";
    /** Timeout para operaciones en waiting response en seconds */
    private final int TIMEOUT = 30 * 60;

    @Inject
    public MonitoringStationCheckerTask(MonitoringService monitoring, Mailer mailer, Config config, SMSService service) {
        this.monitoring = monitoring;
        this.mailer = mailer;
        this.config = config;
        this.service = service;
    }

    @Override
    public void run() {
        Map<Station, Integer> stations = monitoring.getStationsHealth();
        for (Entry<Station, Integer> entry : stations.entrySet()) {
            if (entry.getValue() == null || entry.getValue() > 60) {
                StringBuffer stationText = new StringBuffer(entry.getKey().getId().toString()).append(" - ").append(
                        entry.getKey().getSimCard().getNumber());
                StringBuffer client = new StringBuffer(entry.getKey().getCustomer().getName()).append(", ").append(
                        entry.getKey().getCustomer().getLastName());
                mailer.sendCriticalStationMail(config.getEmailToReportError(), stationText.toString(),
                        client.toString(), ERROR);
            }
        }

        logger.info("Chequeando operaciones pendientes en TIMEOUT");
        List<SMS> sms = service.getAllSMSWaitingResponse();
        if (CollectionUtils.isEmpty(sms)) {
            logger.info("No hay operaciones en [W]");
            return;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        for (SMS waiting : sms) {
            if (waiting.getSent_at() == null) {
                continue;
            }
            long diff = (cal.getTimeInMillis() - waiting.getSent_at().getTime()) / 1000;
            if (diff >= TIMEOUT) {
                waiting.setState(SMS.Status.FAILED_TIMEOUT.getCode());
                service.saveOrUpdateSMS(waiting);
                logger.info("Cambiado estado de [W] -> [FT] para SMS con id # {}", waiting.getId());
            }
        }
    }
}
