package com.jct.bd.gettexidriver.controller;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jct.bd.gettexidriver.R;
import com.jct.bd.gettexidriver.model.backend.FactoryBackend;
import com.jct.bd.gettexidriver.model.backend.MyService;
import com.jct.bd.gettexidriver.model.datasource.NotifyDataChange;
import com.jct.bd.gettexidriver.model.entities.Driver;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button availableRiedsFragment;
    Button FinishRidesFragment;
    String driverName;
    String driverEmail;
    String driverPassword;
    final private int REQUEST_MULTIPLE_PERMISSIONS = 124;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AccessContact();
        startService(new Intent(MainActivity.this, MyService.class));
        FactoryBackend.getInstance().notifyToDriverList(new NotifyDataChange<List<Driver>>() {
            @Override
            public void OnDataChanged(List<Driver> drivers) {
                Intent intent = getIntent();
                driverEmail = intent.getExtras().getString("driver email");
                driverPassword = intent.getExtras().getString("driver password");
                for (Driver driver : drivers)
                {
                    if (driver.getEmail().matches(driverEmail)&&driver.getPassword().matches(driverPassword))
                        driverName = driver.getFullName();
                }
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
        FinishRidesFragment = (Button) findViewById(R.id.secondFragment);
        FinishRidesFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FinishedRidesFragment finishRidesFragment = new FinishedRidesFragment();
                finishRidesFragment.getIntense(driverName);
                loadFragment(finishRidesFragment);
            }
        });
// get the reference of Button's
        availableRiedsFragment = (Button) findViewById(R.id.firstFragment);
// perform setOnClickListener event on First Button
        availableRiedsFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                availableRiedsFragment availableRiedsFragment = new availableRiedsFragment();
                availableRiedsFragment.getIntance(driverName);
                loadFragment(availableRiedsFragment);
            }
        });
    }
    private void loadFragment(Fragment fragment) {
// create a FragmentManager
      FragmentManager fm = getSupportFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit(); // save the changes
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void AccessContact()
    {
        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.WRITE_CONTACTS))
            permissionsNeeded.add("Write Contacts");
        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                if (checkSelfPermission(Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    showMessageOKCancel(message,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_MULTIPLE_PERMISSIONS);
                                }
                            });
                    return;
                }
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_MULTIPLE_PERMISSIONS);
            return;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);

            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
