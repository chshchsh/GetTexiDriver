package com.jct.bd.gettexidriver.model.entities;


import com.google.firebase.database.Exclude;
import com.jct.bd.gettexidriver.R;

import java.util.Date;

public class Ride {
    private TypeOfDrive drive;
    private String id;
    private String email;
    private String name;
    private String startDrive;
    private String endDrive;
    private MyLocation startLocation;
    private MyLocation endLocation;
    private String phone;
    private String driverName;
    private Date whenLoadToFirebase;


    public Ride(TypeOfDrive drive, String id, String email, String name, String startDrive, String endDrive, MyLocation startLocation, MyLocation endLocation, String phone) {
        this.drive = drive;
        this.id = id;
        this.email = email;
        this.name = name;
        this.startDrive = startDrive;
        this.endDrive = endDrive;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.phone = phone;
    }

    public Ride() {
        this.drive = TypeOfDrive.AVAILABLE;
        this.id = "";
        this.email = "";
        this.name = "";
        this.endLocation = new MyLocation();
        this.startLocation = new MyLocation();
        this.phone = "";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws Exception {
        if(email.contains("@"))
         this.email = email;
        else
            throw new Exception(String.valueOf(R.string.contains));
    }

    public MyLocation getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(MyLocation startLocation) {
        this.startLocation = startLocation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) throws Exception {
        if (phone.length() == 10 || phone.length() == 9)
            this.phone = phone;
        else
            throw new Exception(String.valueOf(R.string.length_phone));
    }

    public String getStartDrive() {

        return startDrive;
    }

    public void setStartDrive(String startDrive) {
        this.startDrive = startDrive;
    }

    public String getEndDrive() {
        return endDrive;
    }

    public void setEndDrive(String endDrive) {
        this.endDrive = endDrive;
    }

    public MyLocation getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(MyLocation endLocation) {
        this.endLocation = endLocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeOfDrive getDrive() {
        return drive;
    }

    public void setDrive(TypeOfDrive drive) {
        this.drive = drive;
    }
    @Exclude
    public String getId() {
        return id;
    }
    public void setId(String id) throws Exception {
        if (id.length() == 9)
            if (IDCheck(id))
                this.id = id;
            else
                throw new Exception(String.valueOf(R.string.Extract_id));
        else
            throw new Exception(String.valueOf(R.string.length_id));
    }
    public static boolean IDCheck(String strID)
    {
        int[] id_12_digits = { 1, 2, 1, 2, 1, 2, 1, 2, 1 };
        int count = 0;
        if (strID == null)
            return false;
        for (int i = 0; i < 9; i++)
        {
            int num = Integer.parseInt(strID.substring(i, i+1)) * id_12_digits[i];
            if (num > 9)
                num = (num / 10) + (num % 10);
            count += num;
        }
        return (count % 10 == 0);
    }
    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public Date getWhenLoadToFirebase() {
        return whenLoadToFirebase;
    }

    public void setWhenLoadToFirebase(Date whenLoadToFirebase) {
        this.whenLoadToFirebase = whenLoadToFirebase;
    }
}
