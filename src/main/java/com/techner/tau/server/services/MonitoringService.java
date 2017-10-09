package com.techner.tau.server.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.techner.tau.server.data.dao.interfaces.MeasureDao;
import com.techner.tau.server.data.dao.interfaces.StationDao;
import com.techner.tau.server.domain.model.Station;

/**
 * Servicio de monitoreo de estaciones
 * 
 * @author jmartini
 * 
 */
public class MonitoringService {

	private final StationDao dao;
	private final MeasureDao measure;

	@Inject
	public MonitoringService(StationDao dao, MeasureDao measure) {
		this.dao = dao;
		this.measure = measure;
	}

	/**
	 * Chequea el estado de las estaciones activas
	 */
	public Map<Station, Integer> getStationsHealth() {
		Map<Station, Integer> map = new HashMap<Station, Integer>();
		List<Station> stations = dao.getAllEnableStations();
		for (Station station : stations) {
			Integer minutes = measure.getLastestMeasureTime(station.getId());
			map.put(station, minutes);
		}
		return map;
	}
	
	/**
         * Chequea las estacions con credito por vencer
         */
        public List<Station> getStationsWithoutCredit() {
                return dao.getStationWithCreditDueDateTomorrow();
        }
}
