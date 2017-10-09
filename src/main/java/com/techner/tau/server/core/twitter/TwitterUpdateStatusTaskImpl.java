package com.techner.tau.server.core.twitter;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.GeoLocation;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.techner.tau.server.domain.model.Measure;
import com.techner.tau.server.domain.model.Station;

/**
 * Tarea para publicar tweets
 * 
 * @author juan
 * 
 */
public class TwitterUpdateStatusTaskImpl implements TwitterUpdateStatusTask {
        
        /*** LIMIT*/
	private static final int LIMIT_TWITTER_LENGTH = 140;
        /** logger */
	private static final Logger logger = LoggerFactory.getLogger(TwitterUpdateStatusTaskImpl.class);
	/** twitter */
	private final Twitter tw;
	/** Lista de medidas */
	private final List<Measure> measures;
	/** techner web */
	private static final String TECHNER_WEB = " [www.techner.com.ar]";
	/** station object */
	private final Station station;

	@Inject
	public TwitterUpdateStatusTaskImpl(Twitter tw, @Assisted List<Measure> measures, @Assisted Station station) {
		this.tw = tw;
		this.measures = measures;
		this.station = station;
	}

	@Override
	public void run() {
		Double latitude = null;
		Double longitude = null;
		try {
			Thread.sleep(500l);
		} catch (InterruptedException e1) {
			// nothing to do
		}

		try {
			if (station.getInformation().getLatitude() == null || station.getInformation().getLongitude() == null) {
				logger.error(
						"Error publicando en Twitter. Informaciòn de Latitud y Longitud no pueden ser nulos. Estacion #ID {}",
						station.getId(), new NullPointerException("Latitud y/o Longitud no pueden ser nulos"));
				return;
			}

			latitude = station.getInformation().getLatitude();
			longitude = station.getInformation().getLongitude();

			logger.info("Publicando en twitter informacion de las medidas obtenidas para ubicacion (lat={},lon={})",
					latitude, longitude);
			StringBuffer sb = buildTwitterText(measures);

			// Construimos status p twitter
			StatusUpdate statusToPublish = new StatusUpdate(sb.toString());
			statusToPublish.setDisplayCoordinates(true);
			statusToPublish.setLocation(new GeoLocation(latitude, longitude));
			tw.updateStatus(statusToPublish);
			logger.info("Publicacion EXITOSA de las medidas recibidas. ");
		} catch (Exception e) {
			logger.error("Error publicando tweet {}", e.getLocalizedMessage());
			return;
		}
	}

	/**
	 * Construye texto a twitear
	 * 
	 * @param sb
	 */
	private StringBuffer buildTwitterText(List<Measure> measures) {
		StringBuffer sb = new StringBuffer();
		for (Measure measure : measures) {
			if (!measure.getVariable().isMeteorological()) {
				continue;
			}
			if (sb.length() != 0) {
				sb.append(" ");
			}
			if ((sb.length() + TECHNER_WEB.length()) > LIMIT_TWITTER_LENGTH) {
				break;
			}
			StringBuffer measureText = new StringBuffer();
			String unit = (StringUtils.isBlank(measure.getVariable().getUnit())) ? "" : measure.getVariable().getUnit();
			measureText.append(measure.getVariable().getShortDescription()).append(": ").append(measure.getValue())
					.append(" ").append(unit).append(". ");
			if ((sb.length() + measureText.length() + TECHNER_WEB.length()) > LIMIT_TWITTER_LENGTH) {
				break;
			}
			sb.append(measureText);
		}
		sb.append(TECHNER_WEB);
		return sb;
	}

}
