package com.techner.tau.server.services;

import java.util.Date;
import java.util.List;

import com.google.inject.Inject;
import com.techner.tau.server.data.dao.interfaces.StationDao;
import com.techner.tau.server.domain.model.PeriodicalNotification;
import com.techner.tau.server.domain.model.Station;

public class StationService {

    private final StationDao dao;

    @Inject
    public StationService(StationDao dao) {
        this.dao = dao;
    }

    public Station getStationBySimNumber(String simCard) {
        return dao.findStationBySimNumber(simCard);
    }

    public List<Station> getListOfStationWithoutSunsetSunriseInformation(Date startDate) {
        return dao.getListOfStationWithoutSunsetSunriseInformation(startDate);
    }

    public void saveOrUpdateStation(Station station) {
        dao.insert(station);
    }

    public Date getSunsetTime(Station station, Date date) {
        return dao.getSunsetTime(station, date);
    }

    public List<PeriodicalNotification> getStationsInNotificationTime(String day, Date date) {
        return dao.getStationsInNotificationTime(day, date);
    }

    public List<Station> getAllEnableStations() {
        return dao.getAllEnableStations();
    }

    public Station findStationById(Integer idStation) {
        return dao.findStationById(idStation);
    }
}
