package com.techner.tau.server.core.calculator.notification;

import java.util.List;

import com.techner.tau.server.core.calculator.measure.MeasureCalculator;
import com.techner.tau.server.domain.model.EventNotification;
import com.techner.tau.server.domain.model.Station;

/**
 * Interfaz a ser implementada por todos los calculadores de variables
 * calculables
 * 
 * @author juan
 * 
 */
public interface NotificationsCalculator {

	/** Codigo para prediccion de minimas */
	String MIN_TEMPERTATURE_CODE = "MT";

	/***
	 * Enumeracion con los posibles valores de criticidad
	 * 
	 * @author juan
	 * 
	 */
	enum Criticality {
		LOW("B"), MED("M"), HIGH("A");

		private String code;

		private Criticality(String code) {
			this.code = code;
		}

		/**
		 * @return the code
		 */
		public String getCode() {
			return code;
		}

	}

	/**
	 * Enumeracion con los diferentes calculadores de notificaciones
	 * 
	 * @author juan
	 * 
	 */
	enum Calculators {
		ITH(MeasureCalculator.ITH_CODE, ITHNotificationCalculator.class), MINPRED(MIN_TEMPERTATURE_CODE,
				MinTempNotificationCalculator.class);
		/***
		 * Codigo de la notificacion
		 */
		private String notificacion;
		/**
		 * Calculador
		 */
		private Class<? extends NotificationsCalculator> calculator;

		private Calculators(String notificacion, Class<? extends NotificationsCalculator> calculator) {
			this.notificacion = notificacion;
			this.calculator = calculator;
		}

		/**
		 * @return the variable
		 */
		public String getNotificacion() {
			return notificacion;
		}

		/**
		 * @return the calculator
		 */
		public Class<? extends NotificationsCalculator> getCalculator() {
			return calculator;
		}
	}

	/**
	 * Calcula las notificaciones para una estacion dada
	 * 
	 * @param codNotification
	 *            codigo de la notificacion
	 * @param station
	 *            estacion
	 * @return lista de notificaciones
	 */
	List<EventNotification> calculate(String codNotification, Station station);
}
