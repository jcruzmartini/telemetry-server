package com.techner.tau.server.services;

import java.util.List;

import com.google.inject.Inject;
import com.techner.tau.server.data.dao.interfaces.AlertRuleDao;
import com.techner.tau.server.domain.model.AlertRule;

/**
 * 
 * @author juan
 * 
 */
public class AlertRuleService {
	/** alert station dao */
	private final AlertRuleDao dao;

	@Inject
	public AlertRuleService(AlertRuleDao dao) {
		this.dao = dao;
	}

	/**
	 * Busca alertas q estan o estuvieron activas por estacion
	 * 
	 * @param station
	 *            station id
	 * @param code
	 *            codigo
	 * @return alerta
	 */
	public AlertRule getAlertByCodeAndStation(Integer station, String code) {
		return dao.findAlertStationByCode(station, code);
	}

	/**
	 * Busca alertas q estan por chequeador
	 * 
	 * @param station
	 *            station id
	 * @param checker
	 *            checker
	 * @return lista de reglas de alerta
	 */
	public List<AlertRule> findActiveAlertRulesByChecker(Integer station, String checker) {
		return dao.findActiveAlertRulesByChecker(station, checker);
	}

	/**
	 * Actualizar o Insertar alert station
	 * 
	 * @param alertRule
	 *            objeto
	 */
	public void saveOrUpdateAlertStation(AlertRule alertRule) {
		dao.insert(alertRule);
	}
}
