package com.techner.tau.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class
 * 
 * @author juan
 * 
 */
public class Main {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AplicationControl main = new AplicationControl();
		try {
			main.initApplication();
		} catch (Exception e) {
			logger.error("Error iniciando aplicaciòn", e);
		}
	}

}
