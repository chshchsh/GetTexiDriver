package com.jct.bd.gettexidriver.controller.Adapters;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.Build;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.jct.bd.gettexidriver.R;
import com.jct.bd.gettexidriver.controller.fragments.FinishedRidesFragment;
import com.jct.bd.gettexidriver.model.backend.CurentLocation;
import com.jct.bd.gettexidriver.model.entities.Ride;

import java.util.ArrayList;


public class ListViewAdapter extends BaseAdapter {
    Context context;

    public ListViewAdapter(Context context) {
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
        return FinishedRidesFragment.FinishRides.size();
    }

    @Override
    public Object getItem(int position) {
        return FinishedRidesFragment.FinishRides.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        final Ride ride = FinishedRidesFragment.FinishRides.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.finish_ride_item, null, true);
            viewHolder.endDrive = (TextView) convertView.findViewById(R.id.endDriveInput);
            viewHolder.payment = (TextView) convertView.findViewById(R.id.paymentInput);
            viewHolder.AddContacts = (FloatingActionButton) convertView.findViewById(R.id.AddContacts);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.endDrive.setText(ride.getEndDrive().toString());
        float payment = ride.getStartLocation().distanceTo(ride.getEndLocation());
        payment /= 100;
        int temp = (int) payment;
        payment = (float) (temp) / 10;
        viewHolder.payment.setText(String.valueOf(payment) + context.getString(R.string.shekel));
        viewHolder.AddContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
                int rawContactInsertIndex = ops.size();
                ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                        .build());
                //INSERT NAME
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, ride.getName()) // Name of the person
                        .build());
                //INSERT PHONE MOBILE
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, ride.getPhone()) // Number of the person
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                        .build()); //
                //INSERT EMAIL
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Email.DATA, ride.getEmail())
                        .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                        .build()); //
                //INSERT ADDRESS: FULL
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS, CurentLocation.getPlace(ride.getStartLocation(), context))
                        .withValue(ContactsContract.CommonDataKinds.StructuredPostal.TYPE, ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK)
                        .build());
                // SAVE CONTACT IN BCR Structure
                Uri newContactUri = null;
                //PUSH EVERYTHING TO CONTACTS
                try {
                    ContentProviderResult[] res = context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                    if (res != null && res[0] != null) {
                        newContactUri = res[0].uri;
                    } else Toast.makeText(context, "Contact not added.", Toast.LENGTH_LONG).show();
                } catch (RemoteException e) {
                    // error
                    Toast.makeText(context, "Error (1) adding contact.", Toast.LENGTH_LONG).show();
                    newContactUri = null;
                } catch (OperationApplicationException e) {
                    // error
                    Toast.makeText(context, "Error (2) adding contact.", Toast.LENGTH_LONG).show();
                    newContactUri = null;
                }
                Toast.makeText(context, "Contact added to system contacts.", Toast.LENGTH_LONG).show();

                if (newContactUri == null) {
                    Toast.makeText(context, "Error creating contact", Toast.LENGTH_LONG);

                }

            }
        });
        return convertView;
    }

    private class ViewHolder {

        protected FloatingActionButton AddContacts;
        private TextView endDrive, payment;

    }
}
