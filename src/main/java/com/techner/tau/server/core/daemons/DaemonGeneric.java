package com.techner.tau.server.core.daemons;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.techner.tau.server.data.dao.interfaces.ThreadLocalDao;
import com.techner.tau.server.domain.model.LocalThread;

/**
 * Clase para ser implementada por todos los tipos de demonios
 * 
 * @author juan
 * 
 */
public abstract class DaemonGeneric implements Runnable {

	/** lista de hilos registrados para este demonio */
	private Map<LocalThread, Thread> threads = Collections.synchronizedMap(new HashMap<LocalThread, Thread>());
	/** flag parado */
	private volatile boolean stop = false;
	/** flag vivo */
	private volatile boolean isAlive = true;

	protected ThreadLocalDao dao;

	/**
	 * Default constructor
	 */
	public DaemonGeneric(ThreadLocalDao dao) {
		this.dao = dao;
	}

	/**
	 * Retorna si el demonio esta arrancado o no
	 * 
	 * @return
	 */
	public boolean isStop() {
		return stop;
	}

	/**
	 * Setea el estado del demonio, si esta arrancado o no
	 * 
	 * @param stop
	 */
	public void setStop(boolean stop) {
		this.stop = stop;
	}

	// /**
	// * Apaga todos los threads registrados en este demonio
	// */
	// void turnOffAllThreads() {
	// Set<Entry<LocalThread, Thread>> entries = threads.entrySet();
	// for (Entry<LocalThread, Thread> entry : entries) {
	// entry.getKey().setStarted(Boolean.FALSE);
	// dao.insert(entry.getKey());
	// }
	// }

	/**
	 * Agrega un thread al demonio
	 * 
	 * @param entity
	 *            objeto de dominio
	 * @param thread
	 *            thread
	 */
	void addThread(LocalThread entity, Thread thread) {
		threads.put(entity, thread);
	}

	/**
	 * Logica del demonio
	 */
	public void run() {
		// logica para ser implementada por cada implementacion
	}

	/**
	 * Retorna si el demonio esta vivo
	 * 
	 * @return true si esta vivo, false en otro caso
	 */
	public boolean isAlive() {
		return isAlive;
	}

	/**
	 * Setea estado si esta vivo o no el demonio
	 * 
	 * @param isAlive
	 *            true/false
	 */
	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

}
