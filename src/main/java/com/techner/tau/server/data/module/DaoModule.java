package com.techner.tau.server.data.module;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.techner.tau.server.data.dao.impl.AlertDaoImpl;
import com.techner.tau.server.data.dao.impl.AlertEventDaoImpl;
import com.techner.tau.server.data.dao.impl.AlertRuleDaoImpl;
import com.techner.tau.server.data.dao.impl.CustomerDaoImpl;
import com.techner.tau.server.data.dao.impl.ErrorDaoImpl;
import com.techner.tau.server.data.dao.impl.EventNotificationDaoImpl;
import com.techner.tau.server.data.dao.impl.EventStatusDaoImpl;
import com.techner.tau.server.data.dao.impl.MeasureDaoImpl;
import com.techner.tau.server.data.dao.impl.ModemGSMDaoImpl;
import com.techner.tau.server.data.dao.impl.NotificationDaoImpl;
import com.techner.tau.server.data.dao.impl.ResponseDaoImpl;
import com.techner.tau.server.data.dao.impl.RiskDescriptionCommentDaoImpl;
import com.techner.tau.server.data.dao.impl.SmsDaoImpl;
import com.techner.tau.server.data.dao.impl.StationDaoImpl;
import com.techner.tau.server.data.dao.impl.ThreadLocalDaoImpl;
import com.techner.tau.server.data.dao.impl.UserDaoImpl;
import com.techner.tau.server.data.dao.impl.VariableDaoImpl;
import com.techner.tau.server.data.dao.interfaces.AlertDao;
import com.techner.tau.server.data.dao.interfaces.AlertEventDao;
import com.techner.tau.server.data.dao.interfaces.AlertRuleDao;
import com.techner.tau.server.data.dao.interfaces.CustomerDao;
import com.techner.tau.server.data.dao.interfaces.ErrorDao;
import com.techner.tau.server.data.dao.interfaces.EventNotificationDao;
import com.techner.tau.server.data.dao.interfaces.EventStatusDao;
import com.techner.tau.server.data.dao.interfaces.MeasureDao;
import com.techner.tau.server.data.dao.interfaces.ModemGSMDao;
import com.techner.tau.server.data.dao.interfaces.NotificationDao;
import com.techner.tau.server.data.dao.interfaces.ResponseDao;
import com.techner.tau.server.data.dao.interfaces.RiskDescriptionCommentDao;
import com.techner.tau.server.data.dao.interfaces.SmsDao;
import com.techner.tau.server.data.dao.interfaces.StationDao;
import com.techner.tau.server.data.dao.interfaces.ThreadLocalDao;
import com.techner.tau.server.data.dao.interfaces.UserDao;
import com.techner.tau.server.data.dao.interfaces.VariableDao;

public class DaoModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(AlertDao.class).to(AlertDaoImpl.class).in(Scopes.SINGLETON);
		bind(AlertRuleDao.class).to(AlertRuleDaoImpl.class).in(Scopes.SINGLETON);
		bind(AlertEventDao.class).to(AlertEventDaoImpl.class).in(Scopes.SINGLETON);
		bind(CustomerDao.class).to(CustomerDaoImpl.class).in(Scopes.SINGLETON);
		bind(ErrorDao.class).to(ErrorDaoImpl.class).in(Scopes.SINGLETON);
		bind(ResponseDao.class).to(ResponseDaoImpl.class).in(Scopes.SINGLETON);
		bind(MeasureDao.class).to(MeasureDaoImpl.class).in(Scopes.SINGLETON);
		bind(ModemGSMDao.class).to(ModemGSMDaoImpl.class).in(Scopes.SINGLETON);
		bind(SmsDao.class).to(SmsDaoImpl.class).in(Scopes.SINGLETON);
		bind(StationDao.class).to(StationDaoImpl.class).in(Scopes.SINGLETON);
		bind(ThreadLocalDao.class).to(ThreadLocalDaoImpl.class).in(Scopes.SINGLETON);
		bind(UserDao.class).to(UserDaoImpl.class).in(Scopes.SINGLETON);
		bind(VariableDao.class).to(VariableDaoImpl.class).in(Scopes.SINGLETON);
		bind(NotificationDao.class).to(NotificationDaoImpl.class).in(Scopes.SINGLETON);
		bind(EventNotificationDao.class).to(EventNotificationDaoImpl.class).in(Scopes.SINGLETON);
		bind(RiskDescriptionCommentDao.class).to(RiskDescriptionCommentDaoImpl.class).in(Scopes.SINGLETON);
		bind(EventStatusDao.class).to(EventStatusDaoImpl.class).in(Scopes.SINGLETON);
	}

}
