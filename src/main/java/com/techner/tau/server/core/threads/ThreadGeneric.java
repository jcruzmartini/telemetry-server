package com.techner.tau.server.core.threads;

/**
 * Clase abstracta para ser extendida por cada clase de hilo existente
 * 
 * @author juan
 * 
 */
public abstract class ThreadGeneric implements Runnable {

	/** stop flag */
	private volatile boolean stop = false;
	/** Nombre del hilo */
	private String name;

	/**
	 * Constructor
	 * 
	 * @param threadName
	 *            thread name
	 */
	public ThreadGeneric(String threadName) {
		setName(threadName);
	}

	/**
	 * Logica del hilo
	 */
	public abstract void run();

	/**
	 * Retorna el estado del hilo
	 * 
	 * @return true si esta parado, false en otro caso
	 */
	public boolean isStop() {
		return stop;
	}

	/**
	 * Setea el estado del hilo
	 * 
	 * @param stop
	 *            true/false
	 */
	public void setStop(boolean stop) {
		this.stop = stop;
	}

	/**
	 * Retonra el nombre del hilo
	 * 
	 * @return nombre del hilo
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setea el nombre al hilo
	 * 
	 * @param name
	 *            nombre
	 */
	public void setName(String name) {
		this.name = name;
	}

}
