package com.techner.tau.server.core.mailer;

import java.util.List;
import java.util.Set;

import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.techner.tau.common.entity.MeasureResult;
import com.techner.tau.server.domain.model.Station;

/**
 * Interface a ser implementada por el servicio de mails
 */
public interface Mailer {
	/**
	 * Envia email critico a los emails pasados como parametros
	 * 
	 * @param emails
	 *            email destinatarios
	 * @param content
	 *            contenido del mail
	 * @param subject
	 *            asunto del mail
	 * @throws ResourceNotFoundException
	 *             template no se encuentra
	 * @throws ParseErrorException
	 *             error sintaxis del template
	 */
	void sendCriticalStationMail(Set<String> emails, String station, String client, String error)
			throws ResourceNotFoundException, ParseErrorException;

	/**
	 * Envia email critico al email pasados como parametros
	 * 
	 * @param email
	 *            email destinatario
	 * @param content
	 *            contenido del mail
	 * @param subject
	 *            asunto del mail
	 * @throws ResourceNotFoundException
	 *             template no se encuentra
	 * @throws ParseErrorException
	 *             error sintaxis del template
	 */
	void sendCriticalStationMail(String email, String station, String client, String error)
			throws ResourceNotFoundException, ParseErrorException;

	/**
	 * Envia email de advertencia a los emails pasados como parametros
	 * 
	 * @param email
	 *            email destinatario
	 * @param content
	 *            contenido del mail
	 * @param subject
	 *            asunto del mail
	 * @throws ResourceNotFoundException
	 *             template no se encuentra
	 * @throws ParseErrorException
	 *             error sintaxis del template
	 */
	void sendWarningStationMail(String email, String station, String client, String error)
			throws ResourceNotFoundException, ParseErrorException;
	
	 /**
         * Envia email de advertencia alos emails pasados como parametros
         * 
         * @param emails
         *            email destinatarios
         * @param content
         *            contenido del mail
         * @param subject
         *            asunto del mail
         * @throws ResourceNotFoundException
         *             template no se encuentra
         * @throws ParseErrorException
         *             error sintaxis del template
         */
        void sendWarningStationMail(Set<String> emails, String station, String client, String error)
                        throws ResourceNotFoundException, ParseErrorException;
	
	       /**
         * Envia email de advertencia a los emails pasados como parametros
         * 
         * @param emails
         *            email destinatarios
         * @param content
         *            contenido del mail
         * @param subject
         *            asunto del mail
         * @throws ResourceNotFoundException
         *             template no se encuentra
         * @throws ParseErrorException
         *             error sintaxis del template
         */
        void sendCustomStationMail(Set<String> emails, Station station, String client, String error, String subject)
                        throws ResourceNotFoundException, ParseErrorException;

	/**
	 * Envia email de ingformaciòn a los emails pasados como parametros
	 * 
	 * @param emails
	 *            email destinatarios
	 * @param content
	 *            contenido del mail
	 * @param subject
	 *            asunto del mail
	 * @throws ResourceNotFoundException
	 *             template no se encuentra
	 * @throws ParseErrorException
	 *             error sintaxis del template
	 */
	void sendInformationStationMail(Set<String> emails, String station, String client, String error)
			throws ResourceNotFoundException, ParseErrorException;

	/**
	 * Envia mail con la informacion de las medidas, este es el reporte
	 * periodico que el usuario va a recibir
	 * 
	 * @param emails
	 *            emails destinatarios
	 * @param measures
	 *            lista de medidas
	 */
	void sendPeriodicNotificationEmail(Set<String> emails, List<MeasureResult> measures, Station station);

	/***
	 * Enviar reporte de error
	 * 
	 * @param e
	 *            exception
	 */
	public void sendErrorReportMail(Throwable e);
}
