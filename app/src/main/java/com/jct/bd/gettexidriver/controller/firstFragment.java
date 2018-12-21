package com.jct.bd.gettexidriver.controller;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.jct.bd.gettexidriver.R;
import com.jct.bd.gettexidriver.model.backend.FactoryBackend;
import com.jct.bd.gettexidriver.model.datasource.FireBase_DB_manager;
import com.jct.bd.gettexidriver.model.datasource.NotifyDataChange;
import com.jct.bd.gettexidriver.model.entities.Ride;

import java.util.ArrayList;
import java.util.List;

public class firstFragment extends Fragment {
    View view;
    private ListView lv;
    public static List<Ride> rideArrayList = new ArrayList<>();
    //private CustomAdapter customAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_first, container, false);
        lv = (ListView) view.findViewById(R.id.lv);
        final RideArrayAdapter itemArrayAdapter = new RideArrayAdapter(this, R.layout.ride_item, rideArrayList);
        rideArrayList = FactoryBackend.getInstance().notifyToRideList(new NotifyDataChange<List<Ride>>() {
            @Override
            public void OnDataChanged(List<Ride> obj) {
                lv.setAdapter(itemArrayAdapter);
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
        return view;
    }
}
