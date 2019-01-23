package com.jct.bd.gettexidriver.model.datasource;

import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jct.bd.gettexidriver.model.backend.CurrentLocation;
import com.jct.bd.gettexidriver.model.backend.IDB_Backend;
import com.jct.bd.gettexidriver.model.entities.Driver;
import com.jct.bd.gettexidriver.model.entities.Ride;
import com.jct.bd.gettexidriver.model.entities.TypeOfDrive;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * <h1>funcs on backend with connect to firebase</h1></h1>
 * The FireBase_DB_manager program implements an application that
 * add drive to firebase and get two list of rides and drivers
 * and also alot of funcs on the list that return more specific lists
 * the output on the screen.
 * @author  David Elkayam and Nath Ascoli
 * @version 1.0
 * @since   2019-01-23
 */
public class FireBase_DB_manager implements IDB_Backend {
    public CurrentLocation location;
    public List<Ride> rides = new ArrayList<>();
    public List<Driver> drivers = new ArrayList<>();
    private static ChildEventListener rideRefChildEventListener;
    private static ChildEventListener driverRefChildEventListener;
    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static DatabaseReference DriveRef = database.getReference("Drivers");
    static DatabaseReference RideRef = database.getReference("rides");

    /**
     * This method is used to add driver to firebase. This is
      the simplest form of a class method, it's simply add all the details of the driver to firebase
     * @param driver This is the first paramter to addDriver method
     * @param action  This is the second parameter to addDriver method
     * @return Void for the async task therefore i return null
     */
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
    /**
     * This method is used to return list of available rides from firebase.
      it's simply get the all rides from firbase and remove only the rides that they d'ont available Rides
     * @param notifyRides is the all rides that exist on the firebase
     * @return List<Ride> that the list of only availableRides
     */
    @Override
    public List<Ride> availableRides(List<Ride> notifyRides) {
               rides = notifyRides;
               for (Ride ride : rides) {
                   if (ride.getDrive() != TypeOfDrive.AVAILABLE)
                       rides.remove(ride);
               }
        return rides;
    }
    /**
     * This method is used to return list of progress rides from firebase.
      it's simply get the all rides from firbase and remove only the rides that they d'ont progress Rides
     * @param notifyRides is the all rides that exist on the firebase
     * @return List<Ride> that the list of only progress Rides
     */
    @Override
    public List<Ride> progressRides(List<Ride> notifyRides) {
                rides = notifyRides;
                for (Ride ride : rides) {
                    if (ride.getDrive() != TypeOfDrive.PROGRESS)
                        rides.remove(ride);
                }
        return rides;
    }
    /**
     * This method is used to return list of progress rides from firebase.
      it's simply get the all rides from firbase and remove only the rides that they d'ont finish Rides
     * @param notifyRides is the all rides that exist on the firebase
     * @return List<Ride> that the list of only finish Rides
     */
    @Override
    public List<Ride> finishedRides(List<Ride> notifyRides) {
        rides = notifyRides;
        for (Ride ride : rides) {
            if (ride.getDrive() != TypeOfDrive.FINISH)
                rides.remove(ride);
        }
        return rides;
    }
    /**
     * This method is used to return list of rides of spcific driver from firebase.
      it's simply get the all rides from firbase and remove only the rides that they d'ont do by this driver
     * @param driverName is the name of the driver that we want is rides he does
     * @return List<Ride> that the list of the only Rides that this driver do
     */
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
    /**
     * This method is used to return list of rides that was on spcific city from firebase.
      it's simply get the all rides from firbase and remove only the rides that they d'ont be on this city
     * @param city is the name of the city that we want is rides that was in it
     * @return List<Ride> that the list of the only Rides that was on this city
     */
    @Override
    public List<Ride> availableRidesOnCity(final String city) {
        final List<Ride> toRemove = new ArrayList<Ride>();
        notifyToRideList(new NotifyDataChange<List<Ride>>() {
            @Override
            public void OnDataChanged(List<Ride> obj) {
                rides = obj;
                for (Ride ride : rides) {
            if (location.getPlace(ride.getEndLocation()).matches(city))
                toRemove.add(ride);
                }
            }
            @Override
            public void onFailure(Exception exception) {

            }
        });
        rides.removeAll(toRemove);
        return rides;
    }
    /**
     * This method is used to return list of rides that was on short distance from the driver from firebase.
      it's simply get the all rides from firbase and remove only the rides that they far from the driver above 5KM
     * @param driver is the driver that we search for him rides
     * @return List<Ride> that the list of the only Rides that was on 5KM from the driver
     */
    @Override
    public List<Ride> availableRidesforDriver(final Driver driver) {
        notifyToRideList(new NotifyDataChange<List<Ride>>() {
            @Override
            public void OnDataChanged(List<Ride> obj) {
                 rides = obj;
                for (Ride ride : rides) {
                    if (ride.getStartLocation().distanceTo(driver.getCurrentLocation()) / 1000 >= 5)
                        rides.remove(ride);
                }
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
        return rides;
    }
    /**
     * This method is used to return list of rides that was on spcific date from firebase.
      it's simply get the all rides from firbase and remove only the rides that they d'ont be on this date
     * @param date is the date that we want is rides that was on it
     * @return List<Ride> that the list of the only Rides that was on this date
     */
    @Override
    public List<Ride> dateRides(final Date date) {
        final List<Ride> toRemove = new ArrayList<Ride>();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("DD/MM/YYYY");
        notifyToRideList(new NotifyDataChange<List<Ride>>() {
            @Override
            public void OnDataChanged(List<Ride> obj) {
                rides = obj;
                for (Ride ride : rides) {
                    if (ride.getDrive() == TypeOfDrive.FINISH)
                        if (ride.getEndDrive() != simpleDateFormat.format(date))
                            toRemove.add(ride);
                }
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
        rides.removeAll(toRemove);
        return rides;
    }
    /**
     * This method is used to return list of all the names of the drivers
      it's simply get the all drivers from firbase and get the name from each driver
     * @return List<String> that the list of the only the names of the all drivers
     */
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
    /**
     * This method is used to return list of rides that was large from specific payment from firebase.
      it's simply get the all rides from firbase and remove only the rides that they small from this payment
     * @param payment is the payment that we want is rides that was large from it
     * @return List<Ride> that the list of the only Rides that was large then the payment
     */
    @Override
    public List<Ride> paymentRides(final double payment) {
        notifyToRideList(new NotifyDataChange<List<Ride>>() {
            @Override
            public void OnDataChanged(List<Ride> obj) {
                rides = obj;
                double ridepay;
                for (Ride ride : rides) {
                    if (ride.getDrive() == TypeOfDrive.FINISH) {
                        ridepay = (ride.getStartLocation().distanceTo(ride.getEndLocation()) / 1000) * 5;
                        if (ridepay > payment)
                            rides.remove(ride);
                    }
                }
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
        return rides;
    }
    /**
     * This method is used to change ride from AVAILABLE to be on PROGRESS
     * @param ride is the ride that we want to change it from AVAILABLE to PROGRESS
     * @exception if the ride the we want to change isn't AVAILABLE
     */
    @Override
    public void RideBeProgress(Ride ride) throws Exception {
        if (ride.getDrive() == TypeOfDrive.AVAILABLE)
            ride.setDrive(TypeOfDrive.PROGRESS);
        else
            throw new Exception("the drive not available!");
    }
    /**
     * This method is used to change ride from PROGRESS to be a FINISH
     * @param ride is the ride that we want to change it from PROGRESS to FINISH
     * @exception if the ride the we want to change isn't on PROGRESS
     */
    @Override
    public void RideBeFinish(Ride ride) throws Exception {
        if (ride.getDrive() == TypeOfDrive.PROGRESS)
            ride.setDrive(TypeOfDrive.FINISH);
        else
            throw new Exception("the drive isn't on progress yet!");
    }
    /**
     * This method is used to notify to the firebase if add new ride to firebase
      and if is right he add it to the local list of rides
     * @param notifyDataChange is interface the is job is to tall if was new item add to firebase
     */
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
    /**
     * This method is used to stop notify to the firebase if add new ride to firebase
     */
    public static void stopNotifyToRidesList() {
        if (rideRefChildEventListener != null) {
            RideRef.removeEventListener(rideRefChildEventListener);
            rideRefChildEventListener = null;
        }
    }
    /**
     * This method is used to notify to the firebase if add new driver to firebase
      and if is right he add it to the local list of frivers
     * @param notifyDataChange is interface the is job is to tall if was new item add to firebase
     */
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
    /**
     * This method is used to stop notify to the firebase if add new ride to firebase
     */
    public static void stopNotifyToDriversList() {
        if (driverRefChildEventListener != null) {
            DriveRef.removeEventListener(driverRefChildEventListener);
            driverRefChildEventListener = null;
        }
    }
    /**
     * This method is used to update ride on firebase.
      it's simply add again the new details of this ride to the pld ride and change her
     * @param toUpdate This is the first paramter to addDriver method this the ride we need to update
     * @param action  This is the second parameter to addDriver method that he check that the update success
     * @return Void for the async task therefore i return null
     */
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