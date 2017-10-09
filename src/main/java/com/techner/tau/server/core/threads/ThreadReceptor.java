package com.techner.tau.server.core.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smslib.InboundBinaryMessage;
import org.smslib.InboundMessage;
import org.smslib.InboundMessage.MessageClasses;
import org.smslib.Message.MessageTypes;
import org.smslib.Service;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.techner.tau.server.core.config.Config;
import com.techner.tau.server.core.processor.receptor.ReceptorProcessor;
import com.techner.tau.server.domain.interfaces.Message;
import com.techner.tau.server.domain.model.Customer;
import com.techner.tau.server.domain.model.ModemGSM;
import com.techner.tau.server.services.ConfigUtilsService;
import com.techner.tau.server.services.CustomerService;
import com.techner.tau.server.services.SMSService;

/**
 * Hilo que se encarga de realiza la lectura de los sms de un modem gsm
 * determinado
 * 
 * @author juan
 * 
 */
public class ThreadReceptor extends ThreadGeneric {

    /** slf4j logger */
    private static final Logger logger = LoggerFactory.getLogger(ThreadReceptor.class);
    /** huevito de pascua */
    private final String EASTER_EGG = "CACU";
    /** config */
    private final ConfigUtilsService configService;
    /** sleep time */
    private final Long sleepTime;
    /** modem */
    private final ModemGSM modem;
    /** sms service */
    private final SMSService smsService;
    /** processors */
    private final Map<String, ReceptorProcessor> processors;
    /** customer service */
    private final CustomerService customerService;
    /** sms lib service */
    private final Service smsLibService;

    /**
     * Constructor
     * 
     * @param modem
     *            modem
     * @param threadName
     *            nombre de thread
     * @param configService
     *            servicio de conf
     * @param smsService
     *            servicio de sms
     * @param processors
     *            procesadores
     * @param config
     *            configuracion
     * @param customerService
     *            servicio de cliente
     * @param smsLibService
     *            SMSLIB service
     */
    @Inject
    public ThreadReceptor(@Assisted ModemGSM modem, @Assisted String threadName, ConfigUtilsService configService,
            SMSService smsService, Map<String, ReceptorProcessor> processors, Config config,
            CustomerService customerService, Service smsLibService) {
        super(threadName);
        this.modem = modem;
        this.configService = configService;
        this.smsService = smsService;
        this.processors = processors;
        this.sleepTime = config.getThreadReceptorSleepTime();
        this.customerService = customerService;
        this.smsLibService = smsLibService;
    }

    @Override
    public void run() {
        while (!isStop()) {
            try {
                List<InboundMessage> msgList = new ArrayList<InboundMessage>();
                logger.debug("Consultado MODEM GSM con id <{}> por nuevos SMS", modem.getId());
                smsLibService.readMessages(msgList, MessageClasses.ALL, modem.getId().toString());
                if (msgList.size() > 0) {
                    logger.info("{} SMSs recibidos", msgList.size());
                    processSMSList(msgList);
                }
            } catch (Exception e) {
                logger.error(String.format("Error leyendo SMSs con el modem {}", modem), e);
            }
            try {
                logger.debug("<SLEEPING> Thread: {} por {} segundos", getName(), sleepTime.longValue() / 1000);
                Thread.sleep(sleepTime.longValue());
            } catch (InterruptedException e) {
            }
            logger.debug("<WAKING UP> Thread: ", getName());
        }
    }

    /**
     * Procesa la lista de mensajes
     * 
     * @param msgList
     *            lista de sms
     */
    private void processSMSList(List<InboundMessage> msgList) {
        for (InboundMessage inboundMessage : msgList) {
            logger.info("SMS recibido : {} ", inboundMessage);
            try {
                processSMS(inboundMessage);
            } catch (Exception e) {
                logger.error("Error procesando SMS {}. ELIMINANDO EL MENSAJE ", inboundMessage.getId(), e);
                deleteSMS(inboundMessage);
            }
        }
    }

    /**
     * Procesa/Valida/Guarda mensaje recien recibido
     * 
     * @param inboundMessage
     *            mensaje en crudo
     */
    private void processSMS(InboundMessage inboundMessage) {

        if (inboundMessage.getType() == MessageTypes.UNKNOWN || inboundMessage instanceof InboundBinaryMessage) {
            logger.error("Eliminando mensaje de tipo desconocido", inboundMessage);
            // Borramos SMS del modem
            deleteSMS(inboundMessage);
            return;
        }
        boolean hasBadFormat = false;
        String text = StringUtils.deleteWhitespace(inboundMessage.getText()).toUpperCase();
        String[] textSplited = text.split(Config.SPLIT_CHARACTER_GENERAL);
        String type = StringUtils.upperCase(textSplited[0]);

        // Si es valido
        if (isTypeValid(type)) {

            // Buscamos el validador segun el tipo de SMS
            Matcher validator = null;
            Pattern pattern = configService.getValidator(type);
            if (pattern != null) {
                validator = pattern.matcher(text);
            }

            // Validar si existe validador
            if (validator != null) {
                hasBadFormat = !validator.matches();
                logger.warn("Validando SMS del tipo : {}. Resultado: {} . ", type, hasBadFormat);
            }

            // Si el mensaje tiene formato valido lo procesamos
            if (!hasBadFormat) {
                // Seteamos el texto en mayucula y sin espacios
                inboundMessage.setText(text);
                Message message = processors.get(type).process(inboundMessage);
                // Doble chequeo si es nulo
                if (message == null) {
                    logger.error("Error el mensaje devuelto por el procesador del mensaje no puede ser nulo. CHEQUEAR ERRORES PREVIOS");
                } else {
                    logger.info(String.format("Fin del procesamiento del SMS del tipo : %s . Repuesta necesaria %s ",
                            type, message.needsResponse()));
                }
            }
        } else {
            hasBadFormat = true;
            logger.info("Tipo de SMS invalido: {} . Eliminando SMS", type);
            Customer customer = customerService.getCustomeBySimCard(inboundMessage.getOriginator());
            if (customer != null) {
                if (StringUtils.contains(text, EASTER_EGG)) {
                    smsService.sendMessageTo("poco culo, poca teta, puteada de mujeres, codigo codigo",
                            inboundMessage.getOriginator());
                } else {
                    logger.info("Enviado notificación de formato inválido al cliente con ID: {} . ", customer.getId());
                    smsService.sendMessageTo("Mensaje con formato incorrecto. No se pudo procesar su pedido. TECHNER",
                            inboundMessage.getOriginator());
                }
            }
        }
        // SMS with BAD Format
        if (hasBadFormat) {
            smsService.saveSMSWithFormatProblems(inboundMessage);
        }
        // Borramos SMS del modem
        deleteSMS(inboundMessage);
    }

    /**
     * Elimina SMS del modem gsm una vez procesado
     * 
     * @param inboundMessage
     */
    private void deleteSMS(InboundMessage inboundMessage) {
        try {
            smsLibService.deleteMessage(inboundMessage);
        } catch (Exception e) {
            logger.error("Error Deleting Message from {}", getModem(), e);
        }
    }

    /**
     * Verifica si es valido o no el sms
     * 
     * @param type
     * @return true si es valido, false en otro caso
     */
    private boolean isTypeValid(String type) {
        return configService.getValidSMSTypes().contains(type);
    }

    /**
     * Retorna el modem gsm asociado con este hilo
     * 
     * @return
     */
    public ModemGSM getModem() {
        return modem;
    }
}