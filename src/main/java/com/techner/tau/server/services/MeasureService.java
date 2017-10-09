package com.techner.tau.server.services;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.techner.tau.server.core.calculator.measure.MeasureCalculator;
import com.techner.tau.server.data.dao.interfaces.MeasureDao;
import com.techner.tau.server.domain.model.Measure;
import com.techner.tau.server.domain.model.Station;

public class MeasureService {

	private static final Logger logger = LoggerFactory.getLogger(MeasureService.class);

	private final MeasureDao dao;

	@Inject
	public MeasureService(MeasureDao dao) {
		this.dao = dao;
	}

	public void saveOrUpdate(Measure measure) {
		dao.insert(measure);
	}

	public Measure getLastMeasureValue(String varCode, Integer idStation) {
		return dao.getLastMeasureValue(varCode, idStation);
	}

	/**
	 * Busca las ultimas medidas que corresponden desde ahora a minutes minutos
	 * atras
	 * 
	 * @param idStation
	 *            id de la estacion
	 * @param minutes
	 *            determinar la longevidad de las medidas para q sean
	 *            consideradas validas
	 * @return medidas
	 */
	public List<Measure> getLastMeasures(Integer idStation, int minutes) {
		return dao.getLastMeasures(idStation, minutes);
	}

	/**
	 * Busca las ultimas medidas que corresponden desde ahora a minutes minutos
	 * atras
	 * 
	 * @param idStation
	 *            id de la estacion
	 * @param minutes
	 *            determinar la longevidad de las medidas para q sean
	 *            consideradas validas
	 * @return medidas
	 */
	public Integer getLastestMeasureTime(Integer idStation) {
		return dao.getLastestMeasureTime(idStation);
	}

	public void saveMeasures(List<Measure> measures, Station station) {
		logger.debug("Almacenando medidas correspondientes a la estacion meteorologica {} ", station.getId());
		for (Measure measure : measures) {
			measure.setStation(station);
			saveOrUpdate(measure);
		}
		logger.info("Fin almacenamiento medidas correspondientes a la estacion meteorologica {} ", station.getId());
	}

	public Double getMeasureaAtTime(String code, Date sunsetMinusTwo, Integer IdStation) {
		return dao.getMeasureaAtTime(code, sunsetMinusTwo, IdStation);
	}

	/**
	 * Obtiene acumulado de la lluvia ultimas 24 horas
	 * 
	 * @param code
	 *            codigo de lluvia
	 * @param IdStation
	 *            id de la estacion
	 * @return acumulado
	 */
	public Double getPrecipitationAcum(String code, Integer IdStation) {
		return dao.getPrecipitationAcum(code, IdStation);
	}

	/**
	 * Remplaza el valor actual por el acumulado
	 * 
	 * @param measures
	 *            lista de medidas
	 * @param acumPrecipitation
	 *            acumulado
	 */
	public void replaceActualPrecipitationForAcum(List<Measure> measures, Double acumPrecipitation) {
		if (acumPrecipitation == null) {
			return;
		}
		for (Measure measure : measures) {
			if (null != measure.getVariable()
					&& measure.getVariable().getCode().equals(MeasureCalculator.PRECIPITATION_CODE)) {
				measure.setValue(acumPrecipitation);
				break;
			}
		}
	}
}
