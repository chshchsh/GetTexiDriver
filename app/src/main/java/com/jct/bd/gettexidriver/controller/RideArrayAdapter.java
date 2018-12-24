package com.jct.bd.gettexidriver.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jct.bd.gettexidriver.R;
import com.jct.bd.gettexidriver.model.backend.CurentLocation;
import com.jct.bd.gettexidriver.model.entities.Ride;

import java.util.List;

public class RideArrayAdapter extends ArrayAdapter<Ride> {
    CurentLocation location;
    private int listRideLayout;
    public RideArrayAdapter(firstFragment context, int layoutId, List<Ride> rideList) {
        super(context.getContext(),layoutId,rideList);
        listRideLayout = layoutId;
        location = new CurentLocation(getContext());
        location.getLocation(getContext());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Ride ride = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(listRideLayout, parent, false);
            viewHolder.destination = (TextView) convertView.findViewById(R.id.destination);
            viewHolder.distance = (TextView) convertView.findViewById(R.id.distance);
            convertView.setTag(viewHolder); // view lookup cache stored in tag
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.destination.setText(location.getPlace(ride.getEndLocation(),getContext()));
        float distance = (ride.getStartLocation().distanceTo(location.locationA));
        distance /= 100;
        int temp = (int)(distance);
        distance = (float)(temp) / 10;
        viewHolder.distance.setText(String.valueOf(distance)+ " KM");
        // Return the completed view to render on screen
        return convertView;
    }

    private static class ViewHolder {
        TextView destination;
        TextView distance;

    }
}
