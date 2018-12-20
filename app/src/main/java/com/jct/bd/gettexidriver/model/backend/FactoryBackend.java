package com.jct.bd.gettexidriver.model.backend;

import android.content.Context;

import com.jct.bd.gettexidriver.controller.MainActivity;
import com.jct.bd.gettexidriver.model.datasource.FireBase_DB_manager;

public class FactoryBackend {
    //return new backend if he not exist
    public static FireBase_DB_manager getInstance() { return new FireBase_DB_manager();
    }

    }
