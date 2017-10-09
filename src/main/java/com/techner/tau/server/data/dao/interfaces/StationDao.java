package com.techner.tau.server.data.dao.interfaces;

import java.util.Date;
import java.util.List;

import com.techner.tau.server.domain.model.PeriodicalNotification;
import com.techner.tau.server.domain.model.Station;

public interface StationDao extends ObjectDao<Station> {

    public Station findStationBySimNumber(String simNumber);

    public Station findStationById(Integer id);

    public List<Station> getListOfStationWithoutSunsetSunriseInformation(Date startDate);

    public Date getSunsetTime(Station station, Date date);

    public List<PeriodicalNotification> getStationsInNotificationTime(String day, Date date);

    public List<Station> getAllEnableStations();

    public List<Station> getStationWithCreditDueDateTomorrow();

}
