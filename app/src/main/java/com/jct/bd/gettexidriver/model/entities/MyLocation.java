package com.jct.bd.gettexidriver.model.entities;


import android.location.Location;
/**
 * i did this class because i want send location type to the firebase
 * because on the orignal location he hadn't a deafult constructor i need to do location for me
 * @author  David Elkayam and Nath Ascoli
 * @version 1.0
 * @since   2019-01-23
 */
public class MyLocation extends Location {
    /**
     * this is default constructor i use on the constructor of location
     */
    public MyLocation(){
        super("");
    }
}
