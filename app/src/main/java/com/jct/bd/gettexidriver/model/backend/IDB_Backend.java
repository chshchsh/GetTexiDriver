package com.jct.bd.gettexidriver.model.backend;

import com.jct.bd.gettexidriver.model.datasource.Action;
import com.jct.bd.gettexidriver.model.entities.Driver;
import com.jct.bd.gettexidriver.model.entities.Ride;

import java.util.Date;
import java.util.List;

public interface IDB_Backend {
    public Void addDriver(Driver driver, final Action<String> action);
    public List<Ride> availableRides(List<Ride> notifyRides);
    public List<Ride> finishedRides(List<Ride> notifyRides);
    public List<Ride> specificDriverRides(String driverName);
    public List<Ride> availableRidesOnCity(String city);
    public List<Ride> availableRidesforDriver(Driver driver);
    public List<Ride> dateRides(Date date);
    public List<Ride> progressRides(List<Ride> notifyRides);
    public List<String> DriversUserName();
    public List<Ride> paymentRides(double payment);
    public void RideBeProgress(Ride ride) throws Exception;
    public void RideBeFinish(Ride ride) throws Exception;
    public Void updateRide(Ride ride, final Action<String> action);
}
