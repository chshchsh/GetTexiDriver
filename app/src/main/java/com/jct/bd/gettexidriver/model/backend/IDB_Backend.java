package com.jct.bd.gettexidriver.model.backend;

import com.jct.bd.gettexidriver.model.entities.Driver;
import com.jct.bd.gettexidriver.model.entities.Ride;

import java.util.Date;
import java.util.List;

public interface IDB_Backend {
    public void addDriver(Driver driver);
    public List<Ride> availableRides();
    public List<Ride> finishedRides();
    public List<Ride> specificDriverRides(Driver driver);
    public List<Ride> availableRidesOnCity(String city);
    public List<Ride> availableRidesfromDriver(Driver driver);
    public List<Ride> dateRides(Date date);
    public List<String> DriversUserName();
    public List<Ride> paymentRides();
}
