package com.techner.tau.server.core.calculator.notification;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.techner.tau.server.core.calculator.measure.MeasureCalculator;
import com.techner.tau.server.domain.model.EventNotification;
import com.techner.tau.server.domain.model.Station;
import com.techner.tau.server.services.EventNotificationService;
import com.techner.tau.server.services.MeasureService;
import com.techner.tau.server.services.NotificationService;
import com.techner.tau.server.services.RiskDescriptionCommentService;
import com.techner.tau.server.services.VariableService;

public class ITHNotificationCalculator extends GenericNotificationCalculator {

	private static final Logger logger = LoggerFactory.getLogger(ITHNotificationCalculator.class);
	private String codNotification;
	private Station station;

	@Inject
	public ITHNotificationCalculator(VariableService variableService, NotificationService notificationService,
			MeasureService measureService, RiskDescriptionCommentService commentService,
			EventNotificationService eventNotificationService) {
		super(variableService, notificationService, measureService, commentService, eventNotificationService);
	}

	@Override
	public List<EventNotification> calculate(String codNotificacion, Station station) {
		this.codNotification = codNotificacion;
		this.station = station;

		Double ith = getLastMeasureValue(MeasureCalculator.ITH_CODE, station.getId());

		if (ith == null) {
			return null;
		}
		logger.info("Evaluando la notificacion : {}", codNotificacion);
		return calculateITHEvent(ith);
	}

	/**
	 * Calcula el evento de ITH
	 * 
	 * @param temp
	 *            temperatura
	 * @param hum
	 *            humedad
	 * @return lista de eventos
	 */
	private List<EventNotification> calculateITHEvent(Double ith) {
		List<EventNotification> notifications = new ArrayList<EventNotification>();

		String criticality = null;
		if (ith >= 75 && ith <= 79) {
			criticality = NotificationsCalculator.Criticality.LOW.getCode();
		} else if (ith >= 80 && ith <= 83) {
			criticality = NotificationsCalculator.Criticality.MED.getCode();
		} else if (ith >= 84) {
			criticality = NotificationsCalculator.Criticality.HIGH.getCode();
		}
		EventNotification event = getEventNotification(codNotification, criticality, station);
		if (event != null) {
			event.setValue(ith);
			notifications.add(event);
		}
		return notifications;
	}
}
