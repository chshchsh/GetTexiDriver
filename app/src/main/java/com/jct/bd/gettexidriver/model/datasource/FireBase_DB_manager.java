package com.jct.bd.gettexidriver.model.datasource;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jct.bd.gettexidriver.model.backend.IDB_Backend;
import com.jct.bd.gettexidriver.model.entities.Driver;
import com.jct.bd.gettexidriver.model.entities.Ride;
import com.jct.bd.gettexidriver.model.entities.TypeOfDrive;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FireBase_DB_manager implements IDB_Backend {
    public List<Ride> rides = new ArrayList<>();
    public List<Driver> drivers = new ArrayList<>();
    //CurentLocation location = new CurentLocation();


    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static DatabaseReference DriveRef = database.getReference("Drivers");
    static DatabaseReference RideRef = database.getReference("rides");


    @Override
    public Void addDriver(final Driver driver, final Action<String> action) {
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
        return null;
    }

    @Override
    public List<Ride> availableRides() {
        boolean flag = true;
       notifyToRideList(new NotifyDataChange<List<Ride>>() {
           @Override
           public void OnDataChanged(List<Ride> notifyRides) {
               rides = notifyRides;
               for (Ride ride : rides) {
                   if (ride.getDrive() != TypeOfDrive.AVAILABLE)
                       rides.remove(ride);
               }
           }
           @Override
           public void onFailure(Exception exception) {

           }
       });
        return rides;
    }
    @Override
    public List<Ride> progressRides() {
        boolean flag = true;
        notifyToRideList(new NotifyDataChange<List<Ride>>() {
            @Override
            public void OnDataChanged(List<Ride> notifyRides) {
                rides = notifyRides;
                for (Ride ride : rides) {
                    if (ride.getDrive() != TypeOfDrive.PROGRESS)
                        rides.remove(ride);
                }
            }
            @Override
            public void onFailure(Exception exception) {

            }
        });
        return rides;
    }
    @Override
    public List<Ride> finishedRides() {
        notifyToRideList(new NotifyDataChange<List<Ride>>() {
            @Override
            public void OnDataChanged(List<Ride> notifyRides) {
                rides = notifyRides;
                for (Ride ride : rides) {
                    if (ride.getDrive() != TypeOfDrive.FINISH)
                        rides.remove(ride);
                }
            }
            @Override
            public void onFailure(Exception exception) {

            }
        });
        return rides;
    }

    @Override
    public List<Ride> specificDriverRides(final String driverName) {
        notifyToRideList(new NotifyDataChange<List<Ride>>() {
            @Override
            public void OnDataChanged(List<Ride> notifyRides) {
                rides = notifyRides;
                for (Ride ride : rides) {
                    if (!driverName.equals(ride.getDriverName()))
                        rides.remove(ride);
                }
            }
            @Override
            public void onFailure(Exception exception) {

            }
        });
        return rides;
    }

    @Override
    public List<Ride> availableRidesOnCity(String city) {
        List<Ride> cityRides = availableRides();
        List<Ride> toRemove = new ArrayList<Ride>();
        for (Ride ride : cityRides) {
//            if (location.getPlace(ride.getEndLocation()) != city)
//                toRemove.add(ride);
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("DD/MM/YYYY");
        List<Ride> driverRides = finishedRides();
        List<Ride> toRemove = new ArrayList<Ride>();
        for (Ride ride : driverRides) {
            if (ride.getStartDrive() != simpleDateFormat.format(date))
                toRemove.add(ride);
        }
        driverRides.removeAll(toRemove);
        return driverRides;
    }

    @Override
    public List<String> DriversUserName() {
        final List<String> usernames = new ArrayList<String>();
        notifyToDriverList(new NotifyDataChange<List<Driver>>() {
            @Override
            public void OnDataChanged(List<Driver> notifyDrivers) {
                drivers = notifyDrivers;
                for (Driver driver : drivers) {
                    String fullName = driver.getFullName();
                    usernames.add(fullName);
                }
            }

            @Override
            public void onFailure(Exception exception) {

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
    public void notifyToRideList(final NotifyDataChange<List<Ride>> notifyDataChange) {
        if (notifyDataChange != null) {
            if (rideRefChildEventListener != null) {
                notifyDataChange.onFailure(new Exception("first unNotify ride list"));
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
    @Override
    public Void updateRide(final Ride toUpdate, final Action<String> action) {
        final String key = (toUpdate.getId());
        RideRef.child(key).setValue(toUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                action.onSuccess(toUpdate.getId());
                action.onProgress("upload Driver data", 100);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                action.onFailure(e);
                action.onProgress("error upload Driver data", 100);
            }
        });
        return null;
    }

}