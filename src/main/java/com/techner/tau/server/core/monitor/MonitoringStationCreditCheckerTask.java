package com.techner.tau.server.core.monitor;

import java.util.List;

import com.google.inject.Inject;
import com.techner.tau.server.core.config.Config;
import com.techner.tau.server.core.mailer.Mailer;
import com.techner.tau.server.domain.model.Station;
import com.techner.tau.server.services.MonitoringService;

/**
 * Tarea para chequear las estaciones con credito a vencer
 * 
 * @author jmartini
 * 
 */
public class MonitoringStationCreditCheckerTask implements MonitoringStationCreditChecker {
    /** servicio de monitore */
    private final MonitoringService monitoring;
    /** enviador de mail */
    private final Mailer mailer;
    /** config */
    private final Config config;
    /** mensaje de warning */
    private final String WARN = "Credito de la estación pronto a vencer";

    @Inject
    public MonitoringStationCreditCheckerTask(MonitoringService monitoring, Mailer mailer, Config config) {
        this.monitoring = monitoring;
        this.mailer = mailer;
        this.config = config;
    }

    @Override
    public void run() {
        List<Station> stations = monitoring.getStationsWithoutCredit();
        for (Station station : stations) {
            StringBuffer stationText = new StringBuffer(station.getId().toString()).append(" - ").append(
                    station.getSimCard().getNumber());
            StringBuffer client = new StringBuffer(station.getCustomer().getName()).append(", ").append(
                    station.getCustomer().getLastName());
            mailer.sendWarningStationMail(config.getEmailToReportError(), stationText.toString(), client.toString(),
                    WARN);
        }
    }
}
