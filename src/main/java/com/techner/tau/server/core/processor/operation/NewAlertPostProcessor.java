package com.techner.tau.server.core.processor.operation;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.techner.tau.server.core.config.Config;
import com.techner.tau.server.domain.model.Alert;
import com.techner.tau.server.domain.model.AlertRule;
import com.techner.tau.server.domain.model.SMSServerOperation;
import com.techner.tau.server.services.AlertRuleService;
import com.techner.tau.server.services.AlertService;

/**
 * Procesador de los resultados de una alerta
 * 
 * @author juan
 * 
 */
public class NewAlertPostProcessor implements PostOperationProcessor {
	/** alert station service */
	private final AlertRuleService service;
	/** Alert service */
	private final AlertService alertService;
	/** slf4j logger */
	private static final Logger logger = LoggerFactory.getLogger(NewAlertPostProcessor.class);

	/**
	 * Constructor
	 * 
	 * @param service
	 *            service
	 */
	@Inject
	public NewAlertPostProcessor(AlertRuleService service, AlertService alertService) {
		this.service = service;
		this.alertService = alertService;
	}

	@Override
	public boolean postProcessOperation(SMSServerOperation operation) {
		String[] text = StringUtils.split(operation.getText(), Config.GENERIC_SEPARATOR);
		if (text != null && text.length >= 4) {
			String code = text[1];
			String min = text[2];
			String max = text[3];
			AlertRule alertRule = service.getAlertByCodeAndStation(operation.getStation().getId(), code);

			if (alertRule != null) {
				alertRule.setEndDate(null);
				alertRule.setStartDate(new Date());
			} else {
				Alert alert = alertService.getAlertByCode(code);
				if (alert != null) {
					alertRule = new AlertRule();
					alertRule.setMax(Double.valueOf(max));
					alertRule.setMin(Double.valueOf(min));
					alertRule.setStartDate(new Date());
					alertRule.setAlert(alert);
					alertRule.setStation(operation.getStation());
				} else {
					logger.error("Error obteniendo Alerta con codigo {}", code);
					return false;
				}
			}
			service.saveOrUpdateAlertStation(alertRule);
			return true;
		}
		return false;
	}

}
