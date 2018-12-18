package com.jct.bd.gettexidriver.controller;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.jct.bd.gettexidriver.R;
import com.jct.bd.gettexidriver.model.backend.CurentLocation;
import com.jct.bd.gettexidriver.model.entities.Driver;

public class CustomAdapter extends BaseAdapter {

    private Context context;


    public CustomAdapter(Context context) {

        this.context = context;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }
    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getCount() {
        return firstFragment.rideArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return firstFragment.rideArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder(); LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ride_item, null, true);


            holder.tvDest = (TextView) convertView.findViewById(R.id.destination);
            holder.tvDistance = (TextView) convertView.findViewById(R.id.distance);

            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }
        CurentLocation location = new CurentLocation(context);
        double distance = firstFragment.rideArrayList.get(position).getStartLocation().distanceTo(location.locationA);
        holder.tvDest.setText(firstFragment.rideArrayList.get(position).getEndLocation().toString());
        holder.tvDistance.setText(String.valueOf(distance));

        return convertView;
    }

    private class ViewHolder {

        private TextView tvDest, tvDistance;

    }

}
