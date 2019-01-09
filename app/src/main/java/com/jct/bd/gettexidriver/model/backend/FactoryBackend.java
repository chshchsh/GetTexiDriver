package com.jct.bd.gettexidriver.model.backend;


import com.jct.bd.gettexidriver.model.datasource.FireBase_DB_manager;

public class FactoryBackend {
    private static FireBase_DB_manager INSTANCE;

    //return new backend if he not exist
    public static FireBase_DB_manager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FireBase_DB_manager();
        }
        return INSTANCE;
    }
}