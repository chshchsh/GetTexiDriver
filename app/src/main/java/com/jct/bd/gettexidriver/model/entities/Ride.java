package com.jct.bd.gettexidriver.model.entities;

import android.location.Location;

import com.google.firebase.database.Exclude;

import java.sql.Date;

public class Ride {
    private TypeOfDrive drive;
    private String id;
    private String email;
    private String name;
    private Date startDrive;
    private Date endDrive;
    private Location startLocation;
    private Location endLocation;
    private String phone;


    public Ride(TypeOfDrive drive, String id, String email, String name, Date startDrive, Date endDrive, Location startLocation, Location endLocation, String phone) {
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
        this.endLocation = null;
        this.startLocation = null;
        this.phone = "";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws Exception {
        if(email.contains("@"))
         this.email = email;
        else
            throw new Exception("the email must contains @");
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) throws Exception {
        if (phone.length() == 10 || phone.length() == 9)
            this.phone = phone;
        else
            throw new Exception("the length of the phone number is not make sense");
    }

    public Date getStartDrive() {

        return startDrive;
    }

    public void setStartDrive(Date startDrive) {
        this.startDrive = startDrive;
    }

    public Date getEndDrive() {
        return endDrive;
    }

    public void setEndDrive(Date endDrive) {
        this.endDrive = endDrive;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
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

    public String getId() {
        return id;
    }
    @Exclude
    public void setId(String id) throws Exception {
        if (IDCheck(id))
        this.id = id;
        else
            throw new Exception ("this id not exists");
    }
    static boolean IDCheck(String strID)
    {
        int[] id_12_digits = { 1, 2, 1, 2, 1, 2, 1, 2, 1 };
        int count = 0;
        if (strID == null)
            return false;
        strID = leftPad(strID,9,'0');
        for (int i = 0; i < 9; i++)
        {
            int num = Integer.parseInt(strID.substring(i, i+1)) * id_12_digits[i];
            if (num > 9)
                num = (num / 10) + (num % 10);
            count += num;
        }
        return (count % 10 == 0);
    }
    public static String leftPad(String originalString, int length,
                                 char padCharacter) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(padCharacter);
        }
        String padding = sb.toString();
        String paddedString = padding.substring(originalString.length())
                + originalString;
        return paddedString;
    }
}
