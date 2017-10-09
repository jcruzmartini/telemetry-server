package com.techner.tau.server.data.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techner.tau.server.data.dao.interfaces.MeasureDao;
import com.techner.tau.server.domain.model.Measure;

public class MeasureDaoImpl extends ObjectDAOImpl<Measure> implements MeasureDao {

	private static final Logger logger = LoggerFactory.getLogger(MeasureDaoImpl.class);

	@Override
	public Measure getLastMeasureValue(String varCode, Integer IdStation) {
		Measure last = null;
		Session session = getSession();
		try {
			session.beginTransaction();
			Query sqlQ = session
					.createQuery("from Measure m where m.variable.code = :varCode and m.station.id = :IdStation order by m.id desc");
			sqlQ.setParameter("varCode", varCode);
			sqlQ.setParameter("IdStation", IdStation);
			sqlQ.setMaxResults(1);
			last = (Measure) sqlQ.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			logger.error(String.format("Error buscando la ultima medida para la variable %s y la estacion %s", varCode,
					IdStation), e);
		}
		return last;
	}

	@Override
	public Double getMeasureaAtTime(String code, Date sunsetMinusTwo, Integer idStation) {
		Double value = null;
		Session session = getSession();
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(sunsetMinusTwo);
			cal.add(Calendar.MINUTE, -30);
			Date from = cal.getTime();
			cal.add(Calendar.MINUTE, 60);
			Date to = cal.getTime();

			session.beginTransaction();
			Criteria crit = session.createCriteria(Measure.class);
			crit.createAlias("station", "s");
			crit.createAlias("variable", "v");
			crit.add(Restrictions.eq("s.id", idStation));
			crit.add(Restrictions.eq("v.code", code));
			crit.add(Restrictions.between("date", from, to));
			crit.setMaxResults(3);
			List<Measure> measures = crit.list();
			session.getTransaction().commit();
			value = getValueNearestMeasure(measures, sunsetMinusTwo);
		} catch (Exception e) {
			session.getTransaction().rollback();
			logger.error(String.format(
					"Error buscando la medida para la variable %s en la Fecha %s y para la estacion %s", code,
					sunsetMinusTwo, idStation), e);
		}
		return value;
	}

	private Double getValueNearestMeasure(List<Measure> measures, Date date) {
		Double value = null;
		long dateToFind = date.getTime();
		long minorDif = Long.MAX_VALUE;
		for (Measure measure : measures) {
			long time = measure.getDate().getTime();
			long difference = Math.abs(time - dateToFind);
			if (difference < minorDif) {
				minorDif = difference;
				value = measure.getValue();
			}
		}
		return value;
	}

	@Override
	public List<Measure> getLastMeasures(Integer idStation, int minutes) {
		List<Measure> measures = new ArrayList<Measure>();
		Session session = getSession();
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.MINUTE, -minutes);
			Date from = cal.getTime();
			session.beginTransaction();
			Criteria crit = session.createCriteria(Measure.class);
			crit.createAlias("station", "s");
			crit.add(Restrictions.eq("s.id", idStation));
			crit.add(Restrictions.between("date", from, new Date()));
			measures = crit.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error("Error obteniendo las ultimas medidas para la estacion {} ", idStation, e);
			session.getTransaction().rollback();
		}
		return measures;
	}

	@Override
	public Double getPrecipitationAcum(String code, Integer idStation) {
		Session session = getSession();
		Double acum = null;
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.HOUR_OF_DAY, -24);
			Date from = cal.getTime();
			session.beginTransaction();
			Criteria crit = session.createCriteria(Measure.class);
			crit.createAlias("station", "s");
			crit.createAlias("variable", "v");
			crit.add(Restrictions.eq("s.id", idStation));
			crit.add(Restrictions.eq("v.code", code));
			crit.add(Restrictions.between("date", from, new Date()));
			crit.setProjection(Projections.sum("value"));
			acum = (Double) crit.list().get(0);
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error("Error obteniendo las ultimas medidas para la estacion {} ", idStation, e);
			session.getTransaction().rollback();
		}
		return acum;
	}

	@Override
	public Integer getLastestMeasureTime(Integer idStation) {
		Integer minutes = null;
		Session session = getSession();
		try {
			session.beginTransaction();
			Query sqlQ = session.createQuery("from Measure m where m.station.id = :IdStation order by m.id desc");
			sqlQ.setParameter("IdStation", idStation);
			sqlQ.setMaxResults(1);
			Measure last = (Measure) sqlQ.uniqueResult();
			if (last == null) {
				return null;
			}
			minutes = (int) ((System.currentTimeMillis() - last.getDate().getTime()) / 60000);
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			logger.error("Error buscando ultima medida reportada de la estacion {}", idStation, e);
		}
		return minutes;
	}
}
