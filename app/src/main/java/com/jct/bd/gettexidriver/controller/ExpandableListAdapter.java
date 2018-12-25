package com.jct.bd.gettexidriver.controller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jct.bd.gettexidriver.R;
import com.jct.bd.gettexidriver.model.backend.CurentLocation;
import com.jct.bd.gettexidriver.model.backend.FactoryBackend;
import com.jct.bd.gettexidriver.model.datasource.Action;
import com.jct.bd.gettexidriver.model.entities.Ride;

import java.util.Calendar;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    String driverName;
    private List<Ride> rideList;
    CurentLocation location;
    public ExpandableListAdapter(Context context, List<Ride> rideList,String driverName) {
        this.context = context;
        this.driverName = driverName;
        this.rideList = rideList;
        location = new CurentLocation(context);
        location.getLocation(context);
    }

    @Override
    public int getGroupCount() {
        return rideList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return rideList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return rideList.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Ride ride = (Ride) getGroup(groupPosition);
        // Check if an existing view is being reused, otherwise inflate the view
        ExpandableListAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ExpandableListAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_group, null);
            viewHolder.destination = (TextView) convertView.findViewById(R.id.destination);
            viewHolder.distance = (TextView) convertView.findViewById(R.id.distance);
            convertView.setTag(viewHolder); // view lookup cache stored in tag
        } else {
            viewHolder = (ExpandableListAdapter.ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.destination.setText(location.getPlace(ride.getEndLocation(),context));
        float distance = (ride.getStartLocation().distanceTo(location.locationA));
        distance /= 100;
        int temp = (int)(distance);
        distance = (float)(temp) / 10;
        viewHolder.distance.setText(String.valueOf(distance)+ " KM");
        // Return the completed view to render on screen
        return convertView;
    }
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final Ride ride = (Ride) getChild(groupPosition,childPosition);
        final ExpandableListAdapter.ViewHolder2 viewHolder2;
        if(convertView == null){
            viewHolder2 = new ExpandableListAdapter.ViewHolder2();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.ride_item, null);
            viewHolder2.full_name = (TextView) convertView.findViewById(R.id.nameInput);
            viewHolder2.idNumber = (TextView) convertView.findViewById(R.id.idInput);
            viewHolder2.source = (TextView) convertView.findViewById(R.id.sourceInput);
            viewHolder2.callButton = (Button) convertView.findViewById(R.id.buttonCall);
            viewHolder2.emailButton = (Button) convertView.findViewById(R.id.buttonEmail);
            viewHolder2.messageButton = (Button) convertView.findViewById(R.id.buttonMessage);
            viewHolder2.startButton = (Button) convertView.findViewById(R.id.buttonTakeDrive);
            viewHolder2.finishButton = (Button) convertView.findViewById(R.id.buttonFinishDrive);
            convertView.setTag(viewHolder2);
        }else {
            viewHolder2 = (ViewHolder2) convertView.getTag();
        }
        viewHolder2.source.setText(location.getPlace(ride.getStartLocation(),context));
        viewHolder2.idNumber.setText(ride.getId());
        viewHolder2.full_name.setText(ride.getName());
        viewHolder2.startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder2.startButton.setVisibility(View.INVISIBLE);
                viewHolder2.finishButton.setVisibility(View.VISIBLE);
                try {
                    FactoryBackend.getInstance().RideBeProgress(ride);
                } catch (Exception e) {
                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
                ride.setStartDrive(Calendar.getInstance().getTime());
                ride.setDriverName(driverName);
                FactoryBackend.getInstance().updateRide(ride, new Action<String>() {
                    @Override
                    public void onSuccess(String obj) {
                        Toast.makeText(context,"update ride",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Exception exception) {
                        Toast.makeText(context,exception.getMessage(),Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onProgress(String status, double percent) {

                    }
                });
            }
        });
        viewHolder2.finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder2.startButton.setVisibility(View.VISIBLE);
                viewHolder2.finishButton.setVisibility(View.INVISIBLE);
                try {
                    FactoryBackend.getInstance().RideBeFinish(ride);
                } catch (Exception e) {
                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
                ride.setEndDrive(Calendar.getInstance().getTime());
                FactoryBackend.getInstance().updateRide(ride, new Action<String>() {
                    @Override
                    public void onSuccess(String obj) {
                        Toast.makeText(context,"update ride",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Exception exception) {

                    }

                    @Override
                    public void onProgress(String status, double percent) {

                    }
                });
            }
        });
        viewHolder2.messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("sms:" + ride.getPhone());
                Intent intent = new Intent(Intent.ACTION_SEND,uri);
                intent.setType("text/palain");
                intent.putExtra("jid",ride.getPhone()+"@s.whatsapp.net");
                intent.setPackage("com.whatsapp");
                context.startActivity(intent);
            }
        });
        viewHolder2.emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mail:"));
                intent.putExtra(Intent.EXTRA_EMAIL,ride.getEmail());
                intent.setType("message/rfc822");
                context.startActivity(Intent.createChooser(intent,"choose an email client"));
            }
        });
        viewHolder2.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + ride.getPhone()));
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    public static class ViewHolder {
        TextView destination;
        TextView distance;
    }
    public static class ViewHolder2 {
        TextView full_name;
        TextView idNumber;
        TextView source;
        Button callButton;
        Button messageButton;
        Button emailButton;
        Button startButton;
        Button finishButton;
    }
}
