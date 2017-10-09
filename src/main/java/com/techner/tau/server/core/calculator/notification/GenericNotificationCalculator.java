package com.techner.tau.server.core.calculator.notification;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techner.tau.server.domain.model.EventNotification;
import com.techner.tau.server.domain.model.Measure;
import com.techner.tau.server.domain.model.Notification;
import com.techner.tau.server.domain.model.RiskDescriptionComment;
import com.techner.tau.server.domain.model.Station;
import com.techner.tau.server.services.EventNotificationService;
import com.techner.tau.server.services.MeasureService;
import com.techner.tau.server.services.NotificationService;
import com.techner.tau.server.services.RiskDescriptionCommentService;
import com.techner.tau.server.services.VariableService;

public abstract class GenericNotificationCalculator implements NotificationsCalculator {
	/** variable service */
	final VariableService variableService;
	/** notification service **/
	final NotificationService notificationService;
	/** Measure service **/
	final MeasureService measureService;
	/** Measure service **/
	final RiskDescriptionCommentService commentService;
	/** Event notification service */
	final EventNotificationService eventNotificationService;

	/** logger **/
	private static final Logger logger = LoggerFactory.getLogger(GenericNotificationCalculator.class);

	public GenericNotificationCalculator(VariableService variableService, NotificationService notificationService,
			MeasureService measureService, RiskDescriptionCommentService commentService,
			EventNotificationService eventNotificationService) {
		this.variableService = variableService;
		this.notificationService = notificationService;
		this.measureService = measureService;
		this.commentService = commentService;
		this.eventNotificationService = eventNotificationService;
	}

	/**
	 * Obtiene nuevo evento de notificacion
	 * 
	 * @param codNotification
	 *            codigo de la notificacion
	 * @return evento
	 */
	public EventNotification getEventNotification(String codNotification, String risk, Station station) {
		EventNotification event = null;
		RiskDescriptionComment comment = null;

		EventNotification eventNotificationActive = eventNotificationService.getLastEventNotificationActive(
				codNotification, station.getId());

		if (eventNotificationActive != null) {
			String riskActive = eventNotificationActive.getComment().getRisk();
			logger.debug("Hay un evento de notificacion activa del tipo de codificacion {} y riesgo {}",
					codNotification, riskActive);
			if (risk == null || !riskActive.equals(risk)) {
				eventNotificationActive.setEndDate(new Date());
				eventNotificationService.saveOrUpdate(eventNotificationActive);
			} else if (riskActive.equals(risk)) {
				return eventNotificationActive;
			}
		}

		Notification notification = notificationService.getNotificationByCode(codNotification);
		if (notification != null) {
			comment = commentService.getRiskComment(notification.getId(), risk);
			if (comment != null) {
				event = new EventNotification();
				event.setStartDate(new Date());
				event.setComment(comment);
				event.setStation(station);
			} else {
				logger.warn("No hay comentario de riesgo existente con cod. de notificacion {} y riesgo {}",
						codNotification, risk);
			}
		} else {
			logger.error("No hay notificacion existente con codigo {}", codNotification);
		}
		return event;
	}

	/**
	 * Obtiene el ultimo valor de una variable determinada para una estacion
	 * dada
	 * 
	 * @param variable
	 *            variable
	 * @param idStation
	 *            estacion
	 * @return valor de la variable
	 */
	public Double getLastMeasureValue(String variable, Integer idStation) {
		Double value = null;
		Measure measure = measureService.getLastMeasureValue(variable, idStation);
		if (measure != null) {
			value = measure.getValue();
		}
		return value;
	}

}
