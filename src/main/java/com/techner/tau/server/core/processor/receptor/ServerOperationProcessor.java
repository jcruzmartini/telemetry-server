package com.techner.tau.server.core.processor.receptor;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smslib.InboundMessage;

import scala.actors.threadpool.Arrays;

import com.google.inject.Inject;
import com.techner.tau.server.core.config.Config;
import com.techner.tau.server.domain.interfaces.Message;
import com.techner.tau.server.domain.model.SMSServerOperation;
import com.techner.tau.server.domain.model.Station;
import com.techner.tau.server.services.SMSService;
import com.techner.tau.server.services.StationService;

/**
 * Procesador encargado de la recepcion de mensajes de operacion por parte de un
 * admin
 * 
 * @author juan
 * 
 */
public class ServerOperationProcessor implements ReceptorProcessor {

    /** slf4j logger */
    private static final Logger logger = LoggerFactory.getLogger(ServerOperationProcessor.class);
    /** Servicio de SMS */
    private final SMSService smsService;
    /** Admin cels */
    private final Set<String> admins;
    /** Servicio de estaciones */
    private final StationService stationService;

    @Inject
    public ServerOperationProcessor(SMSService smsService, Config config, StationService stationService) {
        this.smsService = smsService;
        this.admins = new HashSet<String>(Arrays.asList(config.getAdminCels()));
        this.stationService = stationService;
    }

    @Override
    public Message process(InboundMessage inboundMessage) {
        logger.debug("Procesando operacion enviada por celular administrador");
        SMSServerOperation operation = new SMSServerOperation(inboundMessage);
        // Validar que viene de un admin
        if (!admins.contains(inboundMessage.getOriginator())) {
            logger.error("Mensaje de operacion enviado por celular NO ADMINISTRADOR. Texto: {}",
                    inboundMessage.getOriginator());
            smsService.updateToFailStatus(operation);
            return operation;
        }
        String[] textSplited = inboundMessage.getText().split(Config.SPLIT_CHARACTER_GENERAL);
        if (textSplited == null || textSplited.length < 3) {
            logger.error("Error de formato de mensaje enviado por Administrado via SMS. Texto: {}",
                    inboundMessage.getText());
            smsService.updateToFailStatus(operation);
            return operation;
        }

        Integer idStation = null;
        try {
            idStation = Integer.valueOf(textSplited[1]);
        } catch (NumberFormatException e) {
            logger.error("Error de formato de mensaje enviado por Administrado via SMS. Texto: {}",
                    inboundMessage.getText());
            smsService.updateToFailStatus(operation);
        }
        Station station = stationService.findStationById(idStation);
        if (station == null) {
            logger.error("Error estacion con ID {} no existe.", idStation);
            smsService.updateToFailStatus(operation);
            return operation;
        }
        operation.setStation(station);
        String operationToInsert = StringUtils.removeStart(operation.getText(), operation.getType()
                + Config.GENERIC_SEPARATOR + idStation + Config.GENERIC_SEPARATOR);
        operation.setText(operationToInsert);
        smsService.saveOrUpdateSMS(operation);
        return operation;
    }
}
