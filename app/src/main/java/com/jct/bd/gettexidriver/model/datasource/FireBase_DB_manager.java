package com.jct.bd.gettexidriver.model.datasource;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jct.bd.gettexidriver.controller.MyService;
import com.jct.bd.gettexidriver.model.backend.IDB_Backend;
import com.jct.bd.gettexidriver.model.entities.Driver;
import com.jct.bd.gettexidriver.model.entities.Ride;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FireBase_DB_manager implements IDB_Backend {
    public Context context;

    public FireBase_DB_manager(Context context) {
        this.context = context;
        context.startService(new Intent(context, MyService.class));
    }
    static List<Ride> rides;
    static List<Driver> drivers;
    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static DatabaseReference DriveRef = database.getReference("Driver");
    static DatabaseReference RideRef = database.getReference("Ride");
    public interface NotifyDataChange<T> {
        void OnDataChanged(T obj);
        void onFailure(Exception exception);
    }
    public interface Action<T> {
        void onSuccess(String obj);

        void onFailure(Exception exception);

        void onProgress(String status, double percent);
    }
    @Override
    public void addDriver(final Driver driver,final Action<String> action) {
        String key = driver.getId();
        DriveRef.child(key).setValue(driver).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                action.onSuccess(driver.getId());
                action.onProgress("upload Driver data", 100);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                action.onFailure(e);
                action.onProgress("error upload Driver data", 100);
            }
        });
    }
    @Override
    public List<Ride> availableRides() {
        rides.clear();
        RideRef.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                rides = (List<Ride>) RideRef.orderByChild("drive").equalTo("AVAILABLE");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return rides;
    }
    @Override
    public List<Ride> finishedRides() {
            rides.clear();
            List<Ride> toRemove = new ArrayList<Ride>();
            RideRef.addValueEventListener(new ValueEventListener(){
                public void onDataChange(DataSnapshot dataSnapshot){
                    rides = (List<Ride>) RideRef.orderByChild("drive").equalTo("FINISH");
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            return rides;
    }

    @Override
    public List<Ride> specificDriverRides(Driver driver) {
            return driver.getDriverRides();
    }
    @Override
    public List<Ride> availableRidesOnCity(String city) {
            List<Ride> cityRides = availableRides();
            List<Ride> toRemove = new ArrayList<Ride>();
            for (Ride ride:cityRides) {
                if(ride.getEndLocation()!=!=city)
                toRemove.add(ride);
            }
            cityRides.removeAll(toRemove);
            return  cityRides;
    }
    @Override
    public List<Ride> availableRidesforDriver(Driver driver) {
            List<Ride> driverRides = availableRides();
            List<Ride> toRemove = new ArrayList<Ride>();
            for (Ride ride : driverRides) {
                if (ride.getStartLocation().distanceTo(driver.getCurrentLocation())/1000 >= 25)
                toRemove.add(ride);
            }
            driverRides.removeAll(toRemove);
            return driverRides;
    }
    @Override
    public List<Ride> dateRides(Date date) {
            List<Ride> driverRides = availableRides();
            List<Ride> toRemove = new ArrayList<Ride>();
            for (Ride ride : driverRides) {
                if (ride.getStartDrive != date)
                    toRemove.add(ride);
            }
            driverRides.removeAll(toRemove);
            return driverRides;
    }
    @Override
    public List<String> DriversUserName() {
            drivers.clear();
            final List<String> usernames = new ArrayList<String>();
            DriveRef.addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    drivers = dataSnapshot.getValue(List.class);
                    for (Driver driver:drivers) {
                        String fullName = driver.getFullName();
                        usernames.add(fullName);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        return usernames;
    }
    @Override
    public List<Ride> paymentRides(double payment) {
            List<Ride> paymentRides = availableRides();
            List<Ride> toRemove = new ArrayList<Ride>();
            for (Ride ride : paymentRides) {
                double ridepay = (ride.getStartLocation().distanceTo(ride.getEndLocation())/1000)*5;
                if (ridepay != payment)
                    toRemove.add(ride);
            }
            paymentRides.removeAll(toRemove);
            return paymentRides;
    }
}