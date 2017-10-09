package com.techner.tau.server.core.threads;

/**
 * Factoria de Hilos enviadores
 * 
 * @author juan
 * 
 */
public interface ThreadSenderFactory {
	/**
	 * Crea un nuevo hilo enviador
	 * 
	 * @param threadName
	 *            nombre del hilo enviador
	 * @return hilo enviador
	 */
	ThreadSender create(String threadName);
}
