package com.techner.tau.server.core.threads;

import com.techner.tau.server.domain.model.ModemGSM;

/**
 * Factoria de Hilos receptores
 * 
 * @author juan
 * 
 */
public interface ThreadReceptorFactory {
	/**
	 * Crea un nuevo hilo receptor
	 * 
	 * @param modem
	 *            modem gsm asociado
	 * @param threadName
	 *            nombre del hilo
	 * @return hilo receptor
	 */
	ThreadReceptor create(ModemGSM modem, String threadName);
}
