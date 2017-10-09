package com.techner.tau.server.services;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.google.inject.Inject;
import com.techner.tau.server.data.dao.interfaces.AlertEventDao;
import com.techner.tau.server.domain.model.Alert;
import com.techner.tau.server.domain.model.AlertEvent;
import com.techner.tau.server.domain.model.AlertRule;
import com.techner.tau.server.domain.model.SMS;

/**
 * 
 * @author juan
 * 
 */
public class AlertEventService {
	private static final String SEPARATOR_VARIABLE_VALUE = " : ";
	private static final String BLANK_SPACE = "  ";

	/** alert station dao */
	private final AlertEventDao dao;

	@Inject
	public AlertEventService(AlertEventDao dao) {
		this.dao = dao;
	}

	/**
	 * Obtiene un evento de alerta
	 * 
	 * @param idAlertRule
	 *            id de la regla
         * @param idStation
         *            id de la estacion
	 * @return evento
	 */
	public AlertEvent getAlertEvent(Integer idAlertRule, Integer idStation) {
		return dao.getAlertEvent(idAlertRule, idStation);
	}

	/**
	 * Actualizar o Insertar alert event
	 * 
	 * @param event
	 *            objeto
	 */
	public void saveOrUpdateAlertStation(AlertEvent event) {
		dao.insert(event);
	}

	/**
	 * Crea un nuevo evento
	 * 
	 * @param rule
	 *            regla
	 * @param value
	 *            valor
	 * @return evento
	 */
	public AlertEvent createEvent(AlertRule rule, String value) {
		AlertEvent event = new AlertEvent();
		event.setStartDate(new Date());
		event.setLastUpdate(new Date());
		event.setRule(rule);
		event.setValue(Double.valueOf(value));
		return event;
	}

	/**
	 * Determina si un evento es activo o no
	 * 
	 * @param event
	 *            evento
	 * @return true si es activo, no en otro caso
	 */
	public boolean isActiveEvent(AlertEvent event) {
		Calendar calNow = Calendar.getInstance();
		calNow.setTime(new Date());
		Calendar calLast = Calendar.getInstance();
		Date last = event.getLastUpdate();
		calLast.setTime(last);
		long diff = calNow.getTimeInMillis() - calLast.getTimeInMillis();
		return (diff < 3600000) ? true : false;
	}

	/**
	 * Crea el texto de alerta a enviar
	 * 
	 * @param alert
	 *            Alerta
	 * @param value
	 *            Valor de la alerta
	 * @return Texto de alerta bien formado
	 */
	public StringBuffer getAlertText(Alert alert, String value) {
		StringBuffer alertText = new StringBuffer(SMS.SMS_LENGTH);
		if (!StringUtils.isBlank(value)) {
			alertText.append(alert.getAlertDescription()).append(SEPARATOR_VARIABLE_VALUE).append(value)
					.append(BLANK_SPACE).append(alert.getVariable().getUnit());
		}
		return alertText;
	}
}
