package com.jct.bd.gettexidriver.model.backend;

import com.jct.bd.gettexidriver.model.datasource.Action;
import com.jct.bd.gettexidriver.model.entities.Driver;
import com.jct.bd.gettexidriver.model.entities.Ride;

import java.util.Date;
import java.util.List;

public interface IDB_Backend {
    public void addDriver(Driver driver,final Action<String> action);
    public List<Ride> availableRides();
    public List<Ride> finishedRides();
    public List<Ride> specificDriverRides(Driver driver);
    public List<Ride> availableRidesOnCity(String city);
    public List<Ride> availableRidesforDriver(Driver driver);
    public List<Ride> dateRides(Date date);
    public List<String> DriversUserName();
    public List<Ride> paymentRides(double payment);
    public List<Ride> getRideList();
    public List<Driver> getDriverList();
    public void RideBeProgress(Ride ride) throws Exception;
    public void RideBeFinish(Ride ride) throws Exception;
}
