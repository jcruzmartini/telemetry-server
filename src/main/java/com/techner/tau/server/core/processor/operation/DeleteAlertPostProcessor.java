package com.techner.tau.server.core.processor.operation;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.techner.tau.server.core.config.Config;
import com.techner.tau.server.domain.model.AlertRule;
import com.techner.tau.server.domain.model.SMSServerOperation;
import com.techner.tau.server.services.AlertRuleService;

/**
 * Procesador de los resultados de una alerta
 * 
 * @author juan
 * 
 */
public class DeleteAlertPostProcessor implements PostOperationProcessor {
	/** alert station service */
	private final AlertRuleService service;
	/** slf4j logger */
	private static final Logger logger = LoggerFactory.getLogger(DeleteAlertPostProcessor.class);

	/**
	 * Constructor
	 * 
	 * @param service
	 *            service
	 */
	@Inject
	public DeleteAlertPostProcessor(AlertRuleService service) {
		this.service = service;
	}

	@Override
	public boolean postProcessOperation(SMSServerOperation operation) {
		String[] text = StringUtils.split(operation.getText(), Config.GENERIC_SEPARATOR);
		if (text != null && text.length >= 2) {
			String code = text[1];
			AlertRule alertStation = service.getAlertByCodeAndStation(operation.getStation().getId(), code);

			if (alertStation != null) {
				alertStation.setEndDate(new Date());
			} else {
				logger.error(
						"Error no hay alertas activas para esta estacion y este codigo de alerta. Station {} . Alerta {}",
						operation.getStation().getId(), code);
				return false;
			}
			service.saveOrUpdateAlertStation(alertStation);
			return true;
		}
		return false;
	}

}
