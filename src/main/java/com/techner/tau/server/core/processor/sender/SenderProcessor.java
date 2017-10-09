package com.techner.tau.server.core.processor.sender;

import java.util.List;

import org.smslib.OutboundMessage;

import com.techner.tau.server.domain.model.SMS;

/**
 * Interfaz a implementar para cada enviador del tipo T
 * 
 * @author juan
 * 
 * @param <T>
 *            tipo de SMS a procesar para su envio
 */
public interface SenderProcessor<T extends SMS> {
	/**
	 * Procesa el mensaje a enviar y devuelve una lista de los mensajes a ser
	 * enviados
	 * 
	 * @param sms
	 *            sms a procesar
	 * @return lista de mensajes a enviar
	 */
	List<OutboundMessage> process(T sms);
}
