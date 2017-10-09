package com.techner.tau.server.core.error;

/**
 * Interfaz para ser implementada por el servicio encargado de enviar informes
 * de errores
 * 
 * @author juan
 * 
 */
public interface ErrorReporter {

	/***
	 * Enviar informe de error
	 * 
	 * @param e
	 *            exception
	 */
	void sendErrorReport(Throwable e);
}
