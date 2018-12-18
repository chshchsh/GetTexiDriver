package com.jct.bd.gettexidriver.model.datasource;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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
import com.jct.bd.gettexidriver.controller.MainActivity;
import com.jct.bd.gettexidriver.model.backend.MyService;
import com.jct.bd.gettexidriver.model.backend.IDB_Backend;
import com.jct.bd.gettexidriver.model.entities.Driver;
import com.jct.bd.gettexidriver.model.entities.Ride;
import com.jct.bd.gettexidriver.model.entities.TypeOfDrive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FireBase_DB_manager implements IDB_Backend {
    public Context context;
    public static List<Ride> rides ;
    public FireBase_DB_manager(Context context) {
        this.context = context;
        context.startService(new Intent(context,MyService.class));
        this.rides = new ArrayList<>();
        this.drivers = new ArrayList<>();
    }

    public List<Driver> drivers ;
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
    public void addDriver(final Driver driver, final Action<String> action) {
        String key = driver.getId();
        DriveRef.child(key).setValue(driver).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                action.onSuccess(driver.getId());
                action.onProgress("upload Driver data", 100);
                drivers.add(driver);
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
        List<Ride> rides = getRideList();
        for (Ride ride : rides) {
        if(ride.getDrive()!=TypeOfDrive.AVAILABLE)
            rides.remove(ride);
        }
        return rides;
    }

    @Override
    public List<Ride> finishedRides() {
        List<Ride> rides = getRideList();
        for (Ride ride : rides){
            if(ride.getDrive()!=TypeOfDrive.FINISH)
                rides.remove(ride);
        }
        return rides;
    }

    @Override
    public List<Ride> specificDriverRides(Driver driver) {
        List<Ride> rides = getRideList();
        for (Ride ride : rides){
            if(ride.getDriverName()!= driver.getFullName())
                rides.remove(ride);
        }
        return rides;
    }

    @Override
    public List<Ride> availableRidesOnCity(String city) {
        List<Ride> cityRides = availableRides();
        List<Ride> toRemove = new ArrayList<Ride>();
        for (Ride ride : cityRides) {
            if (getPlace(ride.getEndLocation())!= city)
            toRemove.add(ride);
        }
        cityRides.removeAll(toRemove);
        return cityRides;
    }

    @Override
    public List<Ride> availableRidesforDriver(Driver driver) {
        List<Ride> driverRides = availableRides();
        for (Ride ride : driverRides) {
            if (ride.getStartLocation().distanceTo(driver.getCurrentLocation()) / 1000 >= 5)
                driverRides.remove(ride);
        }
        return driverRides;
    }

    @Override
    public List<Ride> dateRides(Date date) {
        List<Ride> driverRides = availableRides();
        List<Ride> toRemove = new ArrayList<Ride>();
        for (Ride ride : driverRides) {
            if (ride.getStartDrive() != date)
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
                for (Driver driver : drivers) {
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
        for (Ride ride : paymentRides) {
            double ridepay = (ride.getStartLocation().distanceTo(ride.getEndLocation()) / 1000) * 5;
            if (ridepay != payment)
                paymentRides.remove(ride);
        }
        return paymentRides;
    }

    @Override
    public List<Ride> getRideList() {
        RideRef.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot rideSnapshot : dataSnapshot.getChildren()) {
                    Ride ride = rideSnapshot.getValue(Ride.class);
                    rides.add(ride);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return rides;
    }

    @Override
    public List<Driver> getDriverList() {
        RideRef.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot driverSnapshot : dataSnapshot.getChildren()) {
                    Driver driver = driverSnapshot.getValue(Driver.class);
                    drivers.add(driver);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return drivers;
    }

    @Override
    public void RideBeProgress(Ride ride) throws Exception {
        if (ride.getDrive() == TypeOfDrive.AVAILABLE)
            ride.setDrive(TypeOfDrive.PROGRESS);
        else
            throw new Exception("the drive not available!");
    }

    @Override
    public void RideBeFinish(Ride ride) throws Exception {
        if (ride.getDrive() == TypeOfDrive.PROGRESS)
            ride.setDrive(TypeOfDrive.FINISH);
        else
            throw new Exception("the drive isn't on progress yet!");
    }
    private static ChildEventListener rideRefChildEventListener;
    public static void notifyToRideList(final NotifyDataChange<List<Ride>> notifyDataChange) {
        if (notifyDataChange != null) {
            if (rideRefChildEventListener != null) {
                notifyDataChange.onFailure(new Exception("first unNotify ride list"));
                return;
            }
            rides.clear();
            rideRefChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Ride ride = dataSnapshot.getValue(Ride.class);
                    String id = dataSnapshot.getKey();
                    try {
                        ride.setId(id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    rides.add(ride);
                    notifyDataChange.OnDataChanged(rides);
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    notifyDataChange.onFailure(databaseError.toException());
                }
            };
            RideRef.addChildEventListener(rideRefChildEventListener);
        }
    }
    public static void stopNotifyToRidesList() {
        if (rideRefChildEventListener != null) {
            RideRef.removeEventListener(rideRefChildEventListener);
            rideRefChildEventListener = null;
        }
    }

    private static ChildEventListener driverRefChildEventListener;
    public void notifyToDriverList(final NotifyDataChange<List<Driver>> notifyDataChange) {
        if (notifyDataChange != null) {
            if (driverRefChildEventListener != null) {
                notifyDataChange.onFailure(new Exception("first unNotify driver list"));
                return;
            }
            drivers.clear();
            driverRefChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Driver driver = dataSnapshot.getValue(Driver.class);
                    String id = dataSnapshot.getKey();
                    try {
                        driver.setId(id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    drivers.add(driver);
                    notifyDataChange.OnDataChanged(drivers);
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    notifyDataChange.onFailure(databaseError.toException());
                }
            };
            DriveRef.addChildEventListener(driverRefChildEventListener);
        }
    }
    //this func get location and convert it to name of location
    public String getPlace(Location location) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (addresses.size() > 0) {
                String cityName = addresses.get(0).getAddressLine(0);
                return cityName;
            }

            return "no place: \n (" + location.getLongitude() + " , " + location.getLatitude() + ")";
        } catch (
                IOException e)

        {
            e.printStackTrace();
        }
        return "IOException ...";
    }
}