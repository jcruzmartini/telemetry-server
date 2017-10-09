package com.techner.tau.server.data.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techner.tau.server.data.dao.interfaces.StationDao;
import com.techner.tau.server.domain.model.PeriodicalNotification;
import com.techner.tau.server.domain.model.Station;
import com.techner.tau.server.domain.model.SunriseSunsetInformation;

public class StationDaoImpl extends ObjectDAOImpl<Station> implements StationDao {

    /** slf4j logger */
    private static final Logger logger = LoggerFactory.getLogger(StationDaoImpl.class);

    public Station findStationBySimNumber(String simNumber) {
        logger.debug("Inside findStationBySimNumber(String simNumber) simNumber = " + simNumber);
        Station station = null;
        Session session = getSession();
        try {
            session.beginTransaction();
            Query sqlQ = session.createQuery("from Station ms where ms.simCard.number = :simNumber");
            sqlQ.setParameter("simNumber", simNumber);
            station = (Station) sqlQ.uniqueResult();
            session.getTransaction().commit();
            logger.debug("Exiting  findStationBySimNumber(String simNumber)");
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("Error obteniendo obteniendo estación con simNumber {}", e, simNumber);
        }
        return station;
    }

    public List<Station> getListOfStationWithoutSunsetSunriseInformation(Date startDate) {
        logger.debug("Inside getListOfStationWithoutSunsetSunriseInformation() ");
        List<String> ids = new ArrayList<String>();
        List<Station> stations = new ArrayList<Station>();
        Session session = getSession();
        try {
            session.beginTransaction();
            Criteria crit = session.createCriteria(Station.class);
            crit.createCriteria("information", "i", CriteriaSpecification.INNER_JOIN);
            crit.createCriteria("i.sunriseSunsetInfo", "s", CriteriaSpecification.LEFT_JOIN);
            crit.add(Restrictions.ge("s.date", startDate));
            crit.setProjection(Projections.distinct(Projections.property("id")));
            ids = crit.list();

            Criteria crit1 = session.createCriteria(Station.class);
            if (ids != null && !ids.isEmpty()) {
                crit1.add(Restrictions.not(Restrictions.in("id", ids)));
            }
            stations = crit1.list();
            session.getTransaction().commit();
            logger.debug("Buscando estaciones sin informacion sobre sunset y sunrise information");
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("Error obteniendo information sobre salida del sol y puesta del sol", e);
        }

        return stations;
    }

    public Date getSunsetTime(Station station, Date date) {
        Date sunset = null;
        logger.debug("Inside getSunsetTime(Station station) ");
        Session session = getSession();
        try {
            session.beginTransaction();
            Criteria crit = session.createCriteria(SunriseSunsetInformation.class);
            crit.add(Restrictions.eq("information.id", station.getInformation().getId()));
            crit.add(Restrictions.eq("date", date));
            SunriseSunsetInformation info = (SunriseSunsetInformation) crit.uniqueResult();
            if (info != null) {
                sunset = info.getSunsetTime();
            } else {
                logger.error("Error obteniendo informacion de salida y puesta de sol con id station {} y fecha {}",
                        station.getInformation().getId(), date);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("Error obteniendo information sobre puesta del sol", e);
        }
        logger.debug("Exiting getSunsetTime(Station station) ");
        return sunset;
    }

    @Override
    public List<PeriodicalNotification> getStationsInNotificationTime(String day, Date date) {
        List<PeriodicalNotification> notifications = new ArrayList<PeriodicalNotification>();
        logger.debug("Buscando estaciones aderidas a notificaciones periodicas el dia {} a la hora {}", day, date);
        Session session = getSession();
        try {
            session.beginTransaction();
            Criteria crit = session.createCriteria(PeriodicalNotification.class);
            crit.add(Restrictions.eq(day, true));
            crit.add(Restrictions.eq("hour", date));
            notifications = crit.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("Error obteniendo information sobre las estaciones aderidas a las notificaciones periodicas",
                    e);
        }
        return notifications;
    }

    @Override
    public List<Station> getAllEnableStations() {
        logger.debug("Inside getAllEnableStations");
        List<Station> stations = null;
        Session session = getSession();
        try {
            session.beginTransaction();
            Query sqlQ = session.createQuery("from Station where enable = :enable");
            sqlQ.setBoolean("enable", Boolean.TRUE);
            stations = sqlQ.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("Error obteniendo todas las estaciones habilitadas", e);
        }
        logger.debug("Exiting  getAllEnableStations");
        return stations;
    }

    @Override
    public List<Station> getStationWithCreditDueDateTomorrow() {
        logger.debug("Inside getStationWithCreditDueDateTomorrow");
        List<Station> stations = null;
        Session session = getSession();
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_MONTH, 1);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            session.beginTransaction();
            Query sqlQ = session
                    .createQuery("from Station ms where ms.simCard.creditDueDay = :day and enable = :enable");
            sqlQ.setInteger("day", day);
            sqlQ.setBoolean("enable", Boolean.TRUE);
            stations = sqlQ.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("Error obteniendo todas las estaciones con vencimiento de credito proximo", e);
        }
        logger.debug("Exiting getStationWithCreditDueDateTomorrow");
        return stations;
    }

    @Override
    public Station findStationById(Integer id) {
        Station station = null;
        Session session = getSession();
        try {
            session.beginTransaction();
            Query sqlQ = session.createQuery("from Station s where s.id = :id");
            sqlQ.setParameter("id", id);
            station = (Station) sqlQ.uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("Error obteniendo obteniendo estación con id {}", e, id);
        }
        return station;
    }
}
