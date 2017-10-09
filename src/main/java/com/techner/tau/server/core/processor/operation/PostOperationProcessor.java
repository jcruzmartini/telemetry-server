package com.techner.tau.server.core.processor.operation;

import com.techner.tau.server.domain.model.SMSServerOperation;

/**
 * Interfaz para ser implementada por los post procesadores de operaciones
 * 
 * @author juan
 * 
 */
public interface PostOperationProcessor {

	/**
	 * Trabajo posteriores que se deben realizar despues que una operación
	 * resultó exitosa
	 * 
	 * @param operation
	 *            sms de operación
	 * @return true en caso de exito , false en otro caso
	 */
	boolean postProcessOperation(SMSServerOperation operation);
}
