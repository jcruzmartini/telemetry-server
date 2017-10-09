package com.techner.tau.server.core.processor.operation;

/**
 * Tipo de operaciones que pueden llegar al servidor y su correspondiente clase
 * de post operación
 * 
 * @author juan
 * 
 */
public enum OperationTypes {

	// @formatter:off
	NEW_ALERT("AA", NewAlertPostProcessor.class), DELETE_ALERT("BA", DeleteAlertPostProcessor.class);
	// @formatter:on

	/** código de la operación */
	private String code;

	/**
	 * Procesador de recepcion del SMS
	 */
	private Class<? extends PostOperationProcessor> processor;

	private OperationTypes(String code, Class<? extends PostOperationProcessor> processor) {
		this.code = code;
		this.processor = processor;
	}

	/**
	 * @return the processor
	 */
	public Class<? extends PostOperationProcessor> getPostProcessor() {
		return processor;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

}
