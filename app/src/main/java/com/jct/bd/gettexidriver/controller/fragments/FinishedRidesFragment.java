package com.jct.bd.gettexidriver.controller.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jct.bd.gettexidriver.R;
import com.jct.bd.gettexidriver.controller.Adapters.ListViewAdapter;
import com.jct.bd.gettexidriver.model.backend.FactoryBackend;
import com.jct.bd.gettexidriver.model.datasource.NotifyDataChange;
import com.jct.bd.gettexidriver.model.entities.Ride;

import java.util.List;

public class FinishedRidesFragment extends Fragment {
    View view;
    public static List<Ride> FinishRides;
    ListView finishRides;
    ListViewAdapter listViewAdapter;
    String driverName;

    public void getIntance(String driverName) {
        this.driverName = driverName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_finished_rides, container, false);
        finishRides = (ListView) view.findViewById(R.id.FinishedRides);
        final Context context = this.getContext();
        FactoryBackend.getInstance().notifyToRideList(new NotifyDataChange<List<Ride>>() {
            @Override
            public void OnDataChanged(List<Ride> obj) {
                for (Ride ride : obj) {
                    if (!driverName.equals(ride.getDriverName()))
                        obj.remove(ride);
                }
                FinishRides = obj;
                if (FinishRides.size() != 0) {
                    listViewAdapter = new ListViewAdapter(context);
                    finishRides.setAdapter(listViewAdapter);
                }
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });

        return view;
    }
}
