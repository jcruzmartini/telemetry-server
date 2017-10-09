package com.techner.tau.server.services;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.techner.tau.server.core.config.Config;
import com.techner.tau.server.domain.interfaces.Message;
import com.techner.tau.server.domain.model.SMS;

public class ConfigUtilsService {

	/** slf4j logger */
	private static final Logger logger = LoggerFactory.getLogger(ConfigUtilsService.class);
	private final AlertService alertService;
	private final VariableService variableService;
	private final EventStatusService eventService;
	private final Config config;
	private static HashMap<String, Pattern> validationMatrix;
	private static ArrayList<String> typeValues;

	@Inject
	public ConfigUtilsService(AlertService alertService, VariableService variableService, Config config,
			EventStatusService eventService) {
		this.alertService = alertService;
		this.variableService = variableService;
		this.config = config;
		this.eventService = eventService;
	}

	private void buildValidationMatrix() {
		Class cls = ConfigUtilsService.class;
		validationMatrix = new HashMap<String, Pattern>();
		for (Message.Type type : Message.Type.values()) {
			try {
				Method m = cls.getMethod("build" + type.name(), null);
				validationMatrix.put(type.getCode(), (Pattern) m.invoke(this, null));
			} catch (Exception e) {
				logger.info("No exite expresion regular para validr el tipo de SMS  {}", type.getCode());
			}
		}
	}

	public Pattern buildRESPONSE() {
		return buildDAILY_SUMMARY();
	}

	public Pattern buildDAILY_SUMMARY() {
		String regexpDailySummarySMSValidation = null;
		try {
			String partialRegexpDailySummarySMSValidation = config.getRegExpDailySummary();
			if (!StringUtils.isBlank(partialRegexpDailySummarySMSValidation)) {
				StringBuffer sb = new StringBuffer();
				List<String> variables = variableService.getAllMeteorologicalVariableCodes();
				for (String code : variables) {
					if (sb.length() > 0) {
						sb.append(Config.GENERIC_SEPARATOR);
					}
					sb.append(code);
				}
				regexpDailySummarySMSValidation = partialRegexpDailySummarySMSValidation.replaceAll(
						Config.REGEXP_METEOROLOGIC_VARIABLES_KEY, sb.toString());
			} else {
				logger.error("No se pudo obtener la expresion regular para validar Reportes Diario de Medidas");
			}
		} catch (Exception e) {
			logger.error("Error obteniendo expresion regular para la validacion de Reportes Diario de Medidas", e);
		}
		Pattern pattern = null;
		try {
			pattern = Pattern.compile(regexpDailySummarySMSValidation);
		} catch (Exception e) {
			logger.error("Error compiling regular expresion : {}", regexpDailySummarySMSValidation, e);
		}
		return pattern;
	}

	public Pattern buildALERT_STATION_EVENT() {
		String regexpAlertSMSValidation = null;
		try {
			String partialRegexpAlertSMSValidation = config.getRegExpAlert();
			if (!StringUtils.isBlank(partialRegexpAlertSMSValidation)) {
				StringBuffer sb = new StringBuffer();
				List<String> alertTypes = alertService.getAlertTypes();
				for (String code : alertTypes) {
					if (sb.length() > 0) {
						sb.append(Config.GENERIC_SEPARATOR);
					}
					sb.append(code);
				}
				regexpAlertSMSValidation = partialRegexpAlertSMSValidation.replaceAll(Config.REGEXP_ALERT_TYPES_KEY,
						sb.toString());
			} else {
				logger.error("No se pudo obtener la expresion regular para validar Alertas");
			}
		} catch (Exception e) {
			logger.error("Error obteniendo expresion regular para la validacion de Alertas", e);
		}
		Pattern pattern = null;
		try {
			pattern = Pattern.compile(regexpAlertSMSValidation);
		} catch (Exception e) {
			logger.error("Error compiling regular expresion : {} ", regexpAlertSMSValidation, e);
		}
		return pattern;
	}

	public Pattern buildSTATUS() {
		String regexpStatusSMSValidation = null;
		Pattern pattern = null;
		try {
			String partialRegexpStatusSMSValidation = config.getRegExpStatus();
			if (!StringUtils.isBlank(partialRegexpStatusSMSValidation)) {
				try {
					StringBuffer sb = new StringBuffer();
					List<String> eventCodes = eventService.getAllEventStatusCodes();
					for (String code : eventCodes) {
						if (sb.length() > 0) {
							sb.append(Config.GENERIC_SEPARATOR);
						}
						sb.append(code);
					}
					regexpStatusSMSValidation = partialRegexpStatusSMSValidation.replaceAll(
							Config.REGEXP_EVENT_VARIABLES_KEY, sb.toString());
					pattern = Pattern.compile(regexpStatusSMSValidation);
				} catch (Exception e) {
					logger.error("Error compiling regular expresion : {} ", regexpStatusSMSValidation, e);
				}
			} else {
				logger.error("No se pudo obtener la expresion regular para validar Reportes de Estados de la ETA");
			}
		} catch (Exception e) {
			logger.error("Error obteniendo expresion regular para la validacion de Reportes de Estados de la ETA", e);
		}
		return pattern;
	}

	public Pattern buildERROR() {
		String regexpErrorSMSValidation = null;
		Pattern pattern = null;
		try {
			regexpErrorSMSValidation = config.getRegExpError();
			if (!StringUtils.isBlank(regexpErrorSMSValidation)) {
				try {
					pattern = Pattern.compile(regexpErrorSMSValidation);
				} catch (Exception e) {
					logger.error("Error compiling regular expresion : {} ", regexpErrorSMSValidation, e);
				}
			} else {
				logger.error("No se pudo obtener la expresion regular para validar Errores enviados por la ETA");
			}
		} catch (Exception e) {
			logger.error("Error obteniendo expresion regular para la validacion de Errores enviados por la ETA", e);
		}
		return pattern;
	}

	public Pattern buildREPLY() {
		String regexpReplySMSValidation = null;
		Pattern pattern = null;
		try {
			regexpReplySMSValidation = config.getRexExpReply();
			if (!StringUtils.isBlank(regexpReplySMSValidation)) {
				try {
					pattern = Pattern.compile(regexpReplySMSValidation);
				} catch (Exception e) {
					logger.error("Error compiling regular expresion : {} ", regexpReplySMSValidation, e);
				}
			} else {
				logger.error("No se pudo obtener la expresion regular para validar Respuestas a operaciones del Servidor");
			}
		} catch (Exception e) {
			logger.error(
					"Error obteniendo expresion regular para la validacion de Respuestas a operaciones del Servidor", e);
		}
		return pattern;
	}

	public ArrayList<String> getValidSMSTypes() {
		if (CollectionUtils.isEmpty(typeValues)) {
			typeValues = new ArrayList<String>();
			for (SMS.Type _type : SMS.Type.values()) {
				typeValues.add(_type.getCode());
			}
		}
		return typeValues;
	}

	public Pattern getValidator(String type) {
		if (validationMatrix == null) {
			buildValidationMatrix();
		}
		if (validationMatrix.containsKey(type)) {
			return validationMatrix.get(type);
		}
		return null;
	}

}
