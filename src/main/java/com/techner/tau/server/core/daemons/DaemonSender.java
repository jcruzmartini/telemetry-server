package com.techner.tau.server.core.daemons;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.techner.tau.server.core.threads.ThreadSender;
import com.techner.tau.server.core.threads.ThreadSenderFactory;
import com.techner.tau.server.data.dao.interfaces.ThreadLocalDao;
import com.techner.tau.server.domain.model.LocalThread;

/**
 * Demonio que se encarga de inicializar los hilos de envio
 * 
 * @author juan
 * 
 */
public class DaemonSender extends DaemonGeneric {

	/** slf4j logger */
	private static final Logger logger = LoggerFactory.getLogger(DaemonSender.class);
	/** factoria para crear hilos enviadores */
	private final ThreadSenderFactory factory;
	/** Pool Executor */
	private final ThreadPoolExecutor excecutor = new ThreadPoolExecutor(1, 5, 10, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>());

	/**
	 * Constructor
	 * 
	 * @param factory
	 *            factoria de hilos enviadores
	 * @param config
	 *            configuracion
	 */
	@Inject
	public DaemonSender(ThreadSenderFactory factory, ThreadLocalDao dao) {
		super(dao);
		this.factory = factory;
	}

	@Override
	public void run() {
		List<LocalThread> listSenders = (List<LocalThread>) dao.getAllThreadsSenders();
		logger.debug("Initializing ALL the threads for Sending");
		for (LocalThread threadItem : listSenders) {
			logger.debug("Trying to INIT thread: {} ", threadItem.getName());
			ThreadSender thread = factory.create(threadItem.getName());
			Thread tSender = excecutor.getThreadFactory().newThread(thread);
			tSender.setName(threadItem.getName());
			excecutor.execute(tSender);
			logger.info("Thread: {} STARTED", threadItem.getName());
		}
	}
}
