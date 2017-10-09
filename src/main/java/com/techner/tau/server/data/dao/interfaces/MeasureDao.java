package com.techner.tau.server.data.dao.interfaces;

import java.util.Date;
import java.util.List;

import com.techner.tau.server.domain.model.Measure;

public interface MeasureDao extends ObjectDao<Measure> {
	/**
	 * Busca el ultimo valor de una variable dada una estacion
	 * 
	 * @param varCode
	 *            codigo de la variable
	 * @param idStation
	 *            id de la estacion
	 * @return medida
	 */
	public Measure getLastMeasureValue(String varCode, Integer idStation);

	/**
	 * Lista de ultimas variables obtenidas por la estacion que ocurrieron desde
	 * ahora a 15 minutos atras
	 * 
	 * @param idStation
	 *            id de la estacion
	 * @param minutes
	 *            determinar la longevidad de las medidas para q sean
	 *            consideradas validas
	 * @return lista de medidas, o nulo en caso que sean mas antiguas que
	 *         minutes minutos atras
	 */
	public List<Measure> getLastMeasures(Integer idStation, int minutes);

	/**
	 * Obtiene tiene desde ultima medida reportada
	 * 
	 * @param idStation
	 *            id de estacion
	 * @return minutos
	 */
	public Integer getLastestMeasureTime(Integer idStation);

	/**
	 * Obtiene el valor de la variable a cierta hora
	 * 
	 * @param code
	 *            codigo de la variable
	 * @param sunsetMinusTwo
	 *            hora y fecha a buscar
	 * @param IdStation
	 *            id de la estacion
	 * @return valor
	 */
	public Double getMeasureaAtTime(String code, Date sunsetMinusTwo, Integer IdStation);

	/**
	 * Obtiene el acumulado de la lluvia
	 * 
	 * @param code
	 *            codigo de la lluvia
	 * @param idStation
	 *            id de la estacion
	 * @return acumulado
	 */
	public Double getPrecipitationAcum(String code, Integer idStation);
}
