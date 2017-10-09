package com.techner.tau.server.core.threads;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smslib.OutboundMessage;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.techner.tau.server.core.config.Config;
import com.techner.tau.server.core.processor.sender.SenderProcessor;
import com.techner.tau.server.domain.model.SMS;
import com.techner.tau.server.services.SMSService;

/**
 * Hilo que se encarga de realiza la lectura de los sms pendientes de envio
 * 
 * @author juan
 * 
 */
public class ThreadSender extends ThreadGeneric {

	/** slf4j logger */
	private static final Logger logger = LoggerFactory.getLogger(ThreadSender.class);
	private final SMSService smsService;
	private final Long sleepTime;
	@SuppressWarnings("rawtypes")
	private final Map<String, SenderProcessor> processors;

	/**
	 * Constructor
	 * 
	 * @param threadName
	 * @param config
	 * @param smsService
	 * @param mapProcessors
	 */
	@Inject
	public ThreadSender(@Assisted String threadName, Config config, SMSService smsService,
			Map<String, SenderProcessor> processors) {
		super(threadName);
		this.smsService = smsService;
		this.processors = processors;
		sleepTime = config.getThreadSenderSleepTime();
	}

	@Override
	public void run() {
		while (!isStop()) {
			SMS sms = null;
			do {
				try {
					sms = smsService.getPrioritySMSWaiting();
					if (sms != null) {
						SenderProcessor senderProc = processors.get(sms.getType());
						if (senderProc != null) {
							List<OutboundMessage> listSMSToSend = senderProc.process(sms);
							if (!CollectionUtils.isEmpty(listSMSToSend)) {
								smsService.sendSMS(listSMSToSend, sms);
							} else {
								logger.error("Error enviando SMS # {} . No hay sms a enviar", sms.getId());
								sms.setState(SMS.Status.FAILED.getCode());
							}
							smsService.saveOrUpdateSMS(sms);
						} else {
							smsService.updateToFailStatus(sms);
							logger.error("El procesador no puede ser nulo. Tipo {} ", sms.getType());
						}
					}
				} catch (Exception e) {
					logger.error("Error enviando SMSs", e);
				}
			} while (sms != null);

			try {
				logger.debug("<SLEEPING> Thread: {} por {} segundos", getName(), sleepTime.longValue() / 1000);
				Thread.sleep(sleepTime.longValue());
			} catch (InterruptedException e) {
			}
			logger.debug("<WAKING UP> Thread: {} ", getName());
		}
	}
}
