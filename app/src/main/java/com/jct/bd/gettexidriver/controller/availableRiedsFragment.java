package com.jct.bd.gettexidriver.controller;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;

import com.jct.bd.gettexidriver.R;
import com.jct.bd.gettexidriver.model.backend.FactoryBackend;
import com.jct.bd.gettexidriver.model.datasource.NotifyDataChange;
import com.jct.bd.gettexidriver.model.entities.Ride;

import java.util.ArrayList;
import java.util.List;

public class availableRiedsFragment extends Fragment {
    View view;
    ExpandableListAdapter listAdapter;
    EditText distanceFilter;
    String driverName;
    private ExpandableListView lv;
    public static List<Ride> rideArrayList = new ArrayList<>();

    public void getIntance(String driverName) {
        this.driverName = driverName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_first, container, false);
        lv = (ExpandableListView) view.findViewById(R.id.lv);
        distanceFilter = (EditText) view.findViewById(R.id.distanceFilter);
        lv.setTextFilterEnabled(true);
        distanceFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count < before) {
                    // We're deleting char so we need to reset the adapter data
                    listAdapter.resetData();
                }

                listAdapter.getFilter().filter(s.toString());

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        final Context context = this.getContext();
        rideArrayList = FactoryBackend.getInstance().availableRides();
        FactoryBackend.getInstance().notifyToRideList(new NotifyDataChange<List<Ride>>() {
            @Override
            public void OnDataChanged(List<Ride> obj) {
                if (rideArrayList.size() != 0) {
                    listAdapter = new ExpandableListAdapter(context, rideArrayList, driverName);
                    lv.setAdapter(listAdapter);
                }
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
        return view;
    }
}
