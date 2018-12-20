package com.jct.bd.gettexidriver.controller;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jct.bd.gettexidriver.R;
import com.jct.bd.gettexidriver.model.backend.FactoryBackend;
import com.jct.bd.gettexidriver.model.datasource.FireBase_DB_manager;
import com.jct.bd.gettexidriver.model.entities.Ride;
import java.util.List;

public class firstFragment extends Fragment {
    View view;
    private ListView lv;
    public static List<Ride> rideArrayList;
    //private CustomAdapter customAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_first, container, false);
        lv = (ListView) view.findViewById(R.id.lv);
        rideArrayList = getRide();
        //customAdapter = new CustomAdapter(this.getContext());
       //lv.setAdapter(customAdapter);
        return view;
    }
    private List<Ride> getRide(){
        FireBase_DB_manager backend = FactoryBackend.getInstance();
        List<Ride> rides = backend.getRideList();
        return rides;
    }
}
