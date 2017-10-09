package com.techner.tau.server.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smslib.InboundMessage;
import org.smslib.OutboundMessage;
import org.smslib.Service;

import com.google.inject.Inject;
import com.techner.tau.server.core.config.Config;
import com.techner.tau.server.data.dao.interfaces.SmsDao;
import com.techner.tau.server.domain.interfaces.Message;
import com.techner.tau.server.domain.model.Measure;
import com.techner.tau.server.domain.model.SMS;
import com.techner.tau.server.domain.model.SMSCustomerRequest;
import com.techner.tau.server.domain.model.SMSServerOperation;

public class SMSService {
    /** Patter para formateo de fechas */
    private static final String DATE_PATTERN = "dd/MM/yyyy 'a las' HH:mm:ss";
    /** Separador para variables */
    public static final String SEPARATOR_VARIABLE_VALUE = ": ";
    /** Separador Espacio en Bco */
    public static final String BLANK_SPACE = " ";
    /** Separador coma */
    public static final String COMA_SEPARATOR = ",";
    /** Separador para fechas */
    public static final String DATE_SEPARATOR = " - ";
    /** Separador para variables */
    public static final String BARRA_SEPARATOR = " // ";
    /** slf4j logger */
    private static final Logger logger = LoggerFactory.getLogger(SMSService.class);
    /** random generator */
    private static Random randomData;
    /** sms dao */
    private final SmsDao dao;
    /** sms lib service */
    private final Service smsLibService;

    @Inject
    public SMSService( SmsDao dao, Service smsLibService) {
        this.dao = dao;
        this.smsLibService = smsLibService;
    }

    /**
     * Solo para guardar por primera vez o para actualizar
     * 
     * @param sms
     */
    public void saveOrUpdateSMS(SMS sms) {
        dao.insert(sms);
    }

    public void updateToFailStatus(SMS sms) {
        sms.setState(SMS.Status.FAILED.getCode());
        dao.insert(sms);
    }

    /**
     * Realiza las tareas post/pre procesamiento y lo graba
     * 
     * @param sms
     */
    public void postProcessSMS(SMS message, boolean avoidWaitingStatus) {

        // Anexamos el token si es que el server necesia una
        // respuesta por parte de la ETA
        if (message.needsResponse()) {
            // Si es un reintento no generamos el nuevo token
            if (message.getTokenNumber() == -1) {
                Integer token = generateTokenNumber();
                message.setTokenNumber(token);
                message.setText(concatToken(token, message.getText()));
            }
            // Alguna clase de mensajes no necesitan quedar en espera de la eta
            if (!avoidWaitingStatus) {
                message.setState(SMS.Status.WAITING_STATION_RESPONSE.getCode());
            }
        }
        saveOrUpdateSMS(message);
    }

    /**
     * Realiza las tareas post/pre procesamiento y lo graba
     * 
     * @param sms
     */
    public void postProcessSMS(SMS message) {
        postProcessSMS(message, false);
    }

    /**
     * Obtiene mensaje de pedido de consula de datos en espera
     * 
     * @param stationId
     *            id de la estacion
     * @param token
     *            token de identificacion
     * @return sms
     */
    public SMSCustomerRequest getSMSCustomerRequestWaitingForResponse(Integer stationId, Integer token) {
        return dao.findLastSmsInWaitingForResponse(stationId, token);
    }

    /**
     * Obtiene mensaje de operación en espera
     * 
     * @param originator
     *            numero de la estacion
     * @param tokenNumber
     *            token de identificación
     * @return sms
     */
    public SMSServerOperation getSMSOperationWaitingForResponse(String originator, Integer tokenNumber) {
        return dao.getOperationInWaitingResponse(originator, tokenNumber);
    }

    /**
     * Encuentra el primer SMS en estado pendiente
     * 
     * @return sms
     */
    public SMS getPrioritySMSWaiting() {
        logger.debug("Buscando el primer SMS en estado <{}>", SMS.Status.PENDING.getCode());
        SMS sms = dao.findFirstSMSWaiting();
        if (sms != null) {
            logger.info("1 SMS encontrado en estado PENDIENTE. SMS.id #: {}", sms.getId());
        } else {
            logger.debug("No hay SMS en estado PENDIENTE");
        }
        return sms;
    }

    /**
     * Obtiene la cantidad de request enviadas por el cliente
     * 
     * @param customerId
     *            id del cliente
     * @return cantidad de request
     */
    public Long getCountRequestSentByCustomer(Integer customerId) {
        return dao.countSMSRequestSentByCustomer(customerId);
    }

    /**
     * Graba SMS con problemas de formato
     * 
     * @param sms
     */
    public void saveSMSWithFormatProblems(InboundMessage sms) {
        SMS badSMS = new SMS();
        badSMS.setDate(sms.getDate());
        badSMS.setText(sms.getText());
        badSMS.setFrom(sms.getOriginator());
        badSMS.setState(SMS.Status.FAILED_BAD_FORMAT.getCode());
        saveOrUpdateSMS(badSMS);
    }

    /**
     * Obtiene SMS en espera de respuesta
     * 
     * @param originator
     *            estacion
     * @param tokenNumber
     *            token de identificacion
     * @return sms
     */
    public SMS getSMStInWaitingResponse(String originator, Integer tokenNumber) {
        logger.info("Buscando sms en espera de respuesta. EMA simCard # {}, token # {}", originator, tokenNumber);
        SMS sms = dao.getSMStInWaitingResponse(originator, tokenNumber);
        if (sms != null) {
            logger.info("Encontrado un sms de operacion en estado pendiente. SMS id {}", sms.getId());
        }
        return sms;
    }

    /**
     * Obtiene sms por numero de referencia
     * 
     * @param refNo
     *            num de referencia
     * @param modemId
     *            id del modem que envio el sms
     * @return sms
     */
    public SMS getSMSByRefNo(int refNo, String modemId) {
        logger.info("Buscando sms con RefNo {} enviado por el modem con id {}", refNo, modemId);
        SMS sms = dao.getSMSByRefNo(refNo, modemId);
        return sms;
    }

    /**
     * Actualizar sms a estado fallido
     * 
     * @param sms
     *            sms a actualizar
     */
    public void updateToFailSendingStatus(SMS sms) {
        if (sms.getState().equals(SMS.Status.FAILED_SENDING.getCode())) {
            int qty = dao.getRetriesQty(sms.getId());
            if (qty == 0) {
                sms.setState(SMS.Status.FAILED.getCode());
            } else {
                sms.setRetries(qty - 1);
            }
        } else {
            sms.setState(SMS.Status.FAILED_SENDING.getCode());
        }
        dao.insert(sms);
    }

    /**
     * Genera token de identificación aleatorio
     * 
     * @return token
     */
    public Integer generateTokenNumber() {
        if (randomData == null) {
            randomData = new Random(99);
        }
        return randomData.nextInt(99);
    }

    /**
     * Obtiene el token number de un texto
     * 
     * @param text
     *            texto del mensaje
     * @return token
     */
    public Integer getTokenNumber(String text) {
        try {
            String confirmationId = StringUtils.substringAfterLast(text, Config.GENERIC_SEPARATOR);
            return Integer.parseInt(confirmationId);
        } catch (Exception e) {
            logger.error("Error convirtiendo String a Integer", e);
            return -1;
        }
    }

    /**
     * Envia una lista de sms
     * 
     * @param listSMS
     *            lista de sms
     * @param sms
     *            objeto sms al que corresponde
     */
    public void sendSMS(List<OutboundMessage> listSMS, SMS sms) {
        int retries = sms.getRetries();
        for (int i = 1; i <= retries; i++) {
            try {
                OutboundMessage smsToSend = listSMS.get(0);
                logger.info("Mensaje a enviar . Contenido del SMS {}", smsToSend);
                int success = smsLibService.sendMessages(listSMS);
                if (success != 0) {
                    logger.info("{} mensaje enviado correctamente. SMS # {}", success, sms.getId());
                    sms.setRefNo(smsToSend.getRefNo());
                    sms.setSent_at(new Date());
                    sms.setSentByModemId(smsToSend.getGatewayId());
                    break;
                } else {
                    processErrorSending(sms, i);
                }
            } catch (Exception e) {
                processErrorSending(sms, i);
            }
        }
    }

    /**
     * Envia una lista de sms
     * 
     * @param listSMS
     *            lista de sms
     */
    public void sendSMS(List<OutboundMessage> listSMS) {
        try {
            OutboundMessage smsToSend = listSMS.get(0);
            logger.info("Mensaje a enviar . Contenido del SMS {}", smsToSend);
            int success = smsLibService.sendMessages(listSMS);
            if (success != 0) {
                logger.info("{} mensaje enviado correctamente.", success);
            } else {
                logger.error("Error enviando lista de {} SMS", listSMS.size());
            }
        } catch (Exception e) {
            logger.error("Error enviando lista de {} SMS", listSMS.size());
        }
    }

    /**
     * Procesamos el error en el envio, actualiza el num de retries y en caso
     * que no queden mas retries disponible actualiza con error el sms
     * 
     * @param sms
     *            mensaje a enviar
     * @param i
     *            num de retry
     */
    private void processErrorSending(SMS sms, int i) {
        logger.error("Error al enviar. SMS # {}. Reintento # {}", sms.getId(), i);
        if (i == Message.RETRIES_MAX) {
            updateToFailSendingStatus(sms);
        } else {
            sms.setRetries(i);
        }
    }

    /**
     * Concatena token y texto para ser enviado
     * 
     * @param token
     *            numero de token
     * @param text
     *            texto del sms
     * @return texto final del sms
     */
    private final String concatToken(Integer token, String text) {
        StringBuffer sb = new StringBuffer(text).append(Config.GENERIC_SEPARATOR).append(token);
        return sb.toString();
    }

    /**
     * Crea mensaje de texto a enviar
     * 
     * @param text
     *            texto del sms
     * @param to
     *            destinatario
     * @return mensaje a enviar
     */
    public OutboundMessage createMessageToSend(String text, String to) {
        OutboundMessage smsToSend = new OutboundMessage();
        smsToSend.setText(text);
        smsToSend.setRecipient(to);
        return smsToSend;
    }

    /**
     * Crear texto amigable al usuario final con el valor de la consulta
     * 
     * @param measures
     *            lista de medidas
     * @return texto
     */
    public String buildFriendlyText(List<Measure> measures) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        StringBuffer sb = new StringBuffer(SMS.SMS_LENGTH);
        Date date = null;
        for (Measure measure : measures) {
            if (date == null) {
                date = measure.getDate();
            }
            if (sb.length() > 0) {
                sb.append(BARRA_SEPARATOR);
            }
            String value = StringUtils.replace(String.valueOf(measure.getValue()), ".", COMA_SEPARATOR);
            String unit = StringUtils.isBlank(measure.getVariable().getUnit()) ? StringUtils.EMPTY : measure
                    .getVariable().getUnit();
            sb.append(measure.getVariable().getSmsDescription()).append(SEPARATOR_VARIABLE_VALUE).append(value)
                    .append(BLANK_SPACE).append(unit);
        }

        try {
            String fecha = sdf.format(date);
            sb.append(DATE_SEPARATOR).append(fecha);
        } catch (Exception e) {
            logger.error("Error formateando fecha con valor {}", date, e);
        }
        return sb.toString();
    }

    /**
     * Envia un único mensaje a un determinado numero
     * 
     * @param text
     *            texto a enviar
     * @param number
     *            numero al que se debe enviar
     */
    public void sendMessageTo(String text, String number) {
        OutboundMessage message = createMessageToSend(text, number);
        logger.info("Mensaje a enviar . Contenido del SMS {}", message);
        try {
            boolean success = smsLibService.sendMessage(message);
            if (success) {
                logger.info("Mensaje enviado correctamente.");
            } else {
                logger.error("Mensaje NO fue enviado correctamente.");
            }

        } catch (Exception e) {
            logger.error("Error enviando SMS ", e);
        }
    }

    /**
     * Obtiene todas las lista de sms en Waiting con mas de 30 minutos sin
     * respuesta
     * 
     * @return lista de sms en waiting
     */
    public List<SMS> getAllSMSWaitingResponse() {
        return dao.getAllSMSWaitingResponse();
    }

}
