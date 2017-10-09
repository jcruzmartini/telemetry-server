package com.techner.tau.server.core.processor.receptor;

import org.smslib.InboundMessage;

import com.techner.tau.server.domain.interfaces.Message;

/**
 * Interfaz a ser implementada por los procesadores de reception de nuevos
 * mensajes
 * 
 * @author juan
 * 
 */
public interface ReceptorProcessor {
	/**
	 * Procesa un nuevo mensaje
	 * 
	 * @param inboundMessage
	 *            mensaje en crudo
	 * @return objeto creado
	 */
	Message process(InboundMessage inboundMessage);
}
