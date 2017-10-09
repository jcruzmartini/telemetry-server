package com.techner.tau.server;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smslib.AGateway;
import org.smslib.AGateway.Protocols;
import org.smslib.GatewayException;
import org.smslib.Service;
import org.smslib.Service.ServiceStatus;
import org.smslib.modem.SerialModemGateway;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.techner.tau.server.core.calculator.measure.daily.loader.DailyCalculatedMeasureLoader;
import com.techner.tau.server.core.calculator.sunset.SunriseSunsetCalulatorLoader;
import com.techner.tau.server.core.daemons.DaemonReceptor;
import com.techner.tau.server.core.daemons.DaemonSender;
import com.techner.tau.server.core.error.ReportErrorAppender;
import com.techner.tau.server.core.error.ServerUncaughtExceptionHandler;
import com.techner.tau.server.core.guice.SMSModule;
import com.techner.tau.server.core.monitor.MonitoringStationChecker;
import com.techner.tau.server.core.monitor.MonitoringStationCreditChecker;
import com.techner.tau.server.core.periodicalnotifications.PeriodicalNotificationsChecker;
import com.techner.tau.server.data.dao.interfaces.ModemGSMDao;
import com.techner.tau.server.domain.model.ModemGSM;

/**
 * Clase para realizar el control general de la aplicacion.
 * 
 * @author juan
 * 
 */
public class AplicationControl {

	/** sl4j logger */
	private static Logger logger = LoggerFactory.getLogger(AplicationControl.class);
	/** Demonio enviador que controla los hilos que envian */
	private DaemonSender sender;
	/** Demonio receptor que controla los hilos que reciben */
	private DaemonReceptor receptor;
	/** injector */
	private static Injector injector;
	/** scheduler **/
	ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);
	/** sms lib service */
	private Service smsLibService;

	/**
	 * Arrancar la aplicacion
	 * 
	 * @throws Exception
	 */
	public void initApplication() throws Exception {
		logger.info("ARRANCANDO Application SMSMeteo v.1.0.0");
		try {
			injector = Guice.createInjector(new SMSModule());
			registerErrorReporterAppender();
			receptor = registerDaemonReceptor();
			sender = registerDaemonSender();
			smsLibService = injector.getInstance(Service.class);
			registerGateways();
			registerErrorTracker();
			executeSchedulers();
			startSMSLibService();
		} catch (Exception e) {
			logger.error("ERROR ARRANCANDO Application SMSMeteo v.1.0.0", e);
			throw e;
		}
	}

	/**
	 * Parar la aplicacion
	 * 
	 * @throws Exception
	 */
	public void stopApplication() throws Exception {
		sender.setStop(Boolean.TRUE);
		receptor.setStop(Boolean.TRUE);
		try {
			smsLibService.stopService();
		} catch (Exception e) {
			logger.error("PARANDO SMSLib Service", e);
			throw e;
		}
		while (sender.isAlive() || receptor.isAlive()) {
			// Esperar la finalizacion de todos los demonios
		}
		logger.info("SALIENDO de la Application SMSMeteo v.1.0.0");
	}

	/**
	 * Execute scheduler
	 */
	private void executeSchedulers() {
		// Scheduler para el calculo de las puestas y salidas del sol
		SunriseSunsetCalulatorLoader.Factory factory = injector.getInstance(SunriseSunsetCalulatorLoader.Factory.class);
		SunriseSunsetCalulatorLoader task = factory.create();
		scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.HOURS);

		MonitoringStationChecker.Factory factoryMonitor = injector.getInstance(MonitoringStationChecker.Factory.class);
		MonitoringStationChecker taskMonitor = factoryMonitor.create();
		scheduler.scheduleAtFixedRate(taskMonitor, 0, 1, TimeUnit.HOURS);
		
	        MonitoringStationCreditChecker.Factory factoryCredit = injector.getInstance(MonitoringStationCreditChecker.Factory.class);
	        MonitoringStationCreditChecker taskCredit = factoryCredit.create();
	        scheduler.scheduleAtFixedRate(taskCredit, 0, 1, TimeUnit.DAYS);

		PeriodicalNotificationsChecker.Factory factoryChecker = injector
				.getInstance(PeriodicalNotificationsChecker.Factory.class);
		PeriodicalNotificationsChecker taskChecker = factoryChecker.create();

		Calendar now = Calendar.getInstance();
		now.setTime(new Date());
		int minutes = now.get(Calendar.MINUTE);
		long delay = 0;
		if (minutes == 0) {
			delay = 0;
		} else {
			delay = (60 - minutes) * 60;
		}
		logger.info("Iniciando sheduler de chequeo de notificaciones periodicas con delay = [{}] seg.", delay);
		scheduler.scheduleAtFixedRate(taskChecker, delay, 3600, TimeUnit.SECONDS);

		int hours = now.get(Calendar.HOUR_OF_DAY);
		delay = (60 - minutes) + ((23 - hours) * 60);
		DailyCalculatedMeasureLoader.Factory factoryLoader = injector
				.getInstance(DailyCalculatedMeasureLoader.Factory.class);
		DailyCalculatedMeasureLoader taskLoader = factoryLoader.create();
		scheduler.scheduleAtFixedRate(taskLoader, delay, 1440, TimeUnit.MINUTES);
		logger.info("Iniciando sheduler de calculo de medidas con periocidad diaria con delay = [{}] min.", delay);
	}

	/**
	 * Registrar los modem gsm en SMSLib
	 */
	private void registerGateways() {
		ModemGSMDao dao = injector.getInstance(ModemGSMDao.class);
		Collection<ModemGSM> modemList = dao.getAllModemsEnable();
		for (ModemGSM modemGSM : modemList) {
			try {
				addGateway(modemGSM);
			} catch (GatewayException e) {
				logger.error("Error adding {} to gateway list", modemGSM, e);
			}
		}
	}

	/**
	 * Registrar un modem gsm en SMSLib
	 * 
	 * @param modem
	 * @throws GatewayException
	 */
	private void addGateway(ModemGSM modem) throws GatewayException {

		logger.debug("<ADDING> Modem: {} to Service", modem.toString());
		// Create the Gateway representing the serial GSM modem.
		if (modem != null) {
			SerialModemGateway gateway = new SerialModemGateway(modem.getId().toString(), modem.getPort(),
					modem.getBaudios(), modem.getBranch(), modem.getModel());
			gateway.setProtocol(Protocols.PDU);
			// Do we want the Gateway to be used for Inbound messages?
			gateway.setInbound(modem.isInbound());
			// Do we want the Gateway to be used for Outbound messages?
			gateway.setOutbound(modem.isOutbound());
			// Let SMSLib know which is the SIM PIN.
			gateway.setSimPin(modem.getPinSim());
			try {
				smsLibService.addGateway(gateway);
			} catch (GatewayException e) {
				throw e;
			}
		}
	}

	/**
	 * Crear demonio de Envio
	 * 
	 * @return demonio de envio
	 */
	private DaemonSender registerDaemonSender() {
		logger.debug("Starting DaemonSender ");
		DaemonSender daemon = injector.getInstance(DaemonSender.class);
		Thread daemonSender = new Thread(daemon);
		daemonSender.setDaemon(true);
		daemonSender.setName("DAEMON-SENDER");
		daemonSender.start();
		logger.debug("DaemonSender STARTED ");
		return daemon;
	}

	/**
	 * Crear demonio de recepcion
	 * 
	 * @return demonio de recepcion
	 */
	private DaemonReceptor registerDaemonReceptor() {
		logger.debug("Starting DaemonReceptor ");
		DaemonReceptor daemon = injector.getInstance(DaemonReceptor.class);
		Thread daemonReceptor = new Thread(daemon);
		daemonReceptor.setDaemon(true);
		daemonReceptor.setName("DAEMON-RECEPTOR");
		daemonReceptor.start();
		logger.debug("DaemonReceptor STARTED ");
		return daemon;
	}

	/**
	 * Registra el appender de reporte de errores
	 */
	private void registerErrorReporterAppender() {
		LogManager.getRootLogger().addAppender(injector.getInstance(ReportErrorAppender.class));
	}

	/***
	 * Registra manejar de excepciones
	 */
	private void registerErrorTracker() {
		UncaughtExceptionHandler handler = injector.getInstance(ServerUncaughtExceptionHandler.class);
		Thread.setDefaultUncaughtExceptionHandler(handler);
	}

	/**
	 * Arrancar libreria SMSLib
	 */
	private void startSMSLibService() {
		try {
			smsLibService.setLoadBalancer(new org.smslib.balancing.RoundRobinLoadBalancer());
			smsLibService.startService();
			// Espera que incialize de manera correcta
			while (smsLibService.getServiceStatus() != ServiceStatus.STARTED) {
				Thread.sleep(1000);
			}
			logger.info("SMSLIB SERVICE iniciado correctamente con estado <{}>", ServiceStatus.STARTED);
		} catch (Exception e) {
			logger.error("Error inicializando SMSLIB SERVICE", e);
		}

		for (AGateway modem : smsLibService.getGateways()) {
			if (modem instanceof SerialModemGateway) {
				SerialModemGateway gateway = (SerialModemGateway) modem;
				try {
					// Printout some general information about the modem.
					logger.info("");
					logger.info("Modem Information for Momem ID#: {}", modem.getGatewayId());
					logger.info("  Status : {}", gateway.getStatus());
					logger.info("  Signal Level: {} dBm", gateway.getSignalLevel());
					logger.info("  Battery Level: {}  %", gateway.getBatteryLevel());
					logger.info("");
				} catch (Exception e) {
					logger.error("Error retrieving MODEM INFORMATION", e);
				}
			}

		}
	}
}
