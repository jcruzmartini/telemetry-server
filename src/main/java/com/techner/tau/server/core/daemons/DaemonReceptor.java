package com.techner.tau.server.core.daemons;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.techner.tau.server.core.threads.ThreadReceptor;
import com.techner.tau.server.core.threads.ThreadReceptorFactory;
import com.techner.tau.server.data.dao.interfaces.ThreadLocalDao;
import com.techner.tau.server.domain.model.LocalThread;

/**
 * Demonio que se encarga de inicializar los hilos de recepcion
 * 
 * @author juan
 * 
 */
public class DaemonReceptor extends DaemonGeneric {

	/** slf4j logger */
	private static final Logger logger = LoggerFactory.getLogger(DaemonReceptor.class);
	/** factoria para crear hilos receptores */
	private final ThreadReceptorFactory factory;
	/** Pool Executor */
	private final ThreadPoolExecutor excecutor = new ThreadPoolExecutor(1, 5, 10, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>());

	/**
	 * Constructor
	 * 
	 * @param factory
	 *            factoria de hilos receptores
	 * @param config
	 *            configuracion
	 */
	@Inject
	public DaemonReceptor(ThreadReceptorFactory factory, ThreadLocalDao dao) {
		super(dao);
		this.factory = factory;
	}

	@Override
	public void run() {
		List<LocalThread> listReceptors = (List<LocalThread>) dao.getAllThreadsReceptors();
		logger.debug("Initializing ALL the threads for Reception");
		for (LocalThread threadItem : listReceptors) {
			logger.debug("Trying to INIT thread: {} ", threadItem.getName());
			ThreadReceptor thread = factory.create(threadItem.getModem(), threadItem.getName());
			Thread tReceptor = excecutor.getThreadFactory().newThread(thread);
			tReceptor.setName(threadItem.getName());
			excecutor.execute(tReceptor);
			logger.info("Thread: {} STARTED", threadItem.getName());
		}
	}
}
