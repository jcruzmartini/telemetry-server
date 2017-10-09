package com.techner.tau.server.domain.interfaces;

import com.techner.tau.server.core.processor.receptor.AlertStationProcessor;
import com.techner.tau.server.core.processor.receptor.CustomerRequestProcessor;
import com.techner.tau.server.core.processor.receptor.DailySummaryProcessor;
import com.techner.tau.server.core.processor.receptor.ErrorProcessor;
import com.techner.tau.server.core.processor.receptor.OperationResponseProcessor;
import com.techner.tau.server.core.processor.receptor.ReceptorProcessor;
import com.techner.tau.server.core.processor.receptor.ResponseProcessor;
import com.techner.tau.server.core.processor.receptor.ServerOperationProcessor;
import com.techner.tau.server.core.processor.receptor.StatusEventProcessor;
import com.techner.tau.server.core.processor.sender.AlertStationSender;
import com.techner.tau.server.core.processor.sender.CustomerRequestSender;
import com.techner.tau.server.core.processor.sender.ErrorSender;
import com.techner.tau.server.core.processor.sender.ResponseSender;
import com.techner.tau.server.core.processor.sender.SenderProcessor;
import com.techner.tau.server.core.processor.sender.ServerOperationSender;
import com.techner.tau.server.core.processor.sender.StatusEventSender;

/**
 * Interfaz con informacion generica de todos los mensajes
 * 
 * @author juan
 * 
 */
public interface Message extends Responsable {

    /** longitud maxima del mensaje */
    int SMS_LENGTH = 145;
    /** cantidad maxima de reintentos */
    int RETRIES_MAX = 3;

    /**
     * Posibles estados de los SMS
     * 
     * @author juan
     * 
     */
    enum Status {

        // @formatter:off
        PENDING("P"), 
        SENDING("S"), 
        FAILED("F"), 
        WAITING_STATION_RESPONSE("W"), 
        COMPLETED("C"), 
        FAILED_BAD_FORMAT("BF"), 
        FAILED_SENDING("FS"), 
        FAILED_TIMEOUT("FT"), 
        PENDING_APPROVAL("PA");
        // @formatter:on
        /**
         * Codigo del estado del SMS
         */
        private String state;

        private Status(String state) {
            this.state = state;
        }

        public String getCode() {
            return state;
        }
    }

    /**
     * Tipos de SMS
     * 
     * @author juan
     * 
     */
    enum Type {
        // @formatter:off
        CUSTOMER_REQUEST("INFO", CustomerRequestProcessor.class, CustomerRequestSender.class), 
        DAILY_SUMMARY("D",DailySummaryProcessor.class, null), 
        ERROR("ERR", ErrorProcessor.class, ErrorSender.class), 
        RESPONSE("R", ResponseProcessor.class, ResponseSender.class), 
        SERVER_OPERATION("O",ServerOperationProcessor.class, ServerOperationSender.class), 
        ALERT_STATION_EVENT("A",AlertStationProcessor.class, AlertStationSender.class), 
        STATUS("EST", StatusEventProcessor.class,StatusEventSender.class), 
        REPLY("RTA", OperationResponseProcessor.class, null);
        // @formatter:on
        /**
         * Codigo del SMS
         */
        private String code;
        /**
         * Procesador de recepcion del SMS
         */
        private Class<? extends ReceptorProcessor> receptor;
        /**
         * Procesador de envio del SMS
         */
        private Class<? extends SenderProcessor> sender;

        private Type(String code, Class<? extends ReceptorProcessor> receptor, Class<? extends SenderProcessor> sender) {
            this.code = code;
            this.receptor = receptor;
            this.sender = sender;
        }

        public String getCode() {
            return code;
        }

        public Class<? extends ReceptorProcessor> getReceptorProcessor() {
            return receptor;
        }

        /**
         * @return the sender
         */
        public Class<? extends SenderProcessor> getSenderProcessor() {
            return sender;
        }

    }

    /**
     * Retorna el tipo del SMS
     * 
     * @return codigo del tipo del SMS
     */
    String getType();
}
