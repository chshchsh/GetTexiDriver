package com.jct.bd.gettexidriver.controller.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.jct.bd.gettexidriver.R;
import com.jct.bd.gettexidriver.controller.fragments.FinishedRidesFragment;
import com.jct.bd.gettexidriver.controller.fragments.HomeFragment;
import com.jct.bd.gettexidriver.controller.fragments.availableRiedsFragment;
import com.jct.bd.gettexidriver.controller.fragments.progressRidesFragment;
import com.jct.bd.gettexidriver.model.backend.CurentLocation;
import com.jct.bd.gettexidriver.model.backend.FactoryBackend;
import com.jct.bd.gettexidriver.model.datasource.NotifyDataChange;
import com.jct.bd.gettexidriver.model.entities.Driver;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String driverName;
    String driverEmail;
    String driverPassword;
    DrawerLayout dl;
    ActionBarDrawerToggle t;
    NavigationView nv;
    final private int REQUEST_MULTIPLE_PERMISSIONS = 124;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AccessContact();
        HomeFragment homeFragment = new HomeFragment();
        loadFragment(homeFragment);
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
        dl = (DrawerLayout) findViewById(R.id.activityMain);
        t = new ActionBarDrawerToggle(this,dl,R.string.open,R.string.close){
            public void onDrawerClosed(View view){
                getSupportActionBar().setTitle(R.string.open);
                supportInvalidateOptionsMenu();
            }
            public void onDrawerOpened(View view){
                getSupportActionBar().setTitle(R.string.close);
                supportInvalidateOptionsMenu();
            }
        };
        dl.addDrawerListener(t);
        t.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nv = (NavigationView) findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.Home:
                        HomeFragment homeFragment = new HomeFragment();
                        loadFragment(homeFragment);
                    case R.id.availableRides:
                        availableRiedsFragment availableRiedsFragment = new availableRiedsFragment();
                        availableRiedsFragment.getIntance(driverName);
                        loadFragment(availableRiedsFragment);
                    case R.id.progressRides:
                        progressRidesFragment progressRidesFragment = new progressRidesFragment();
                        progressRidesFragment.getIntance(driverName);
                        loadFragment(progressRidesFragment);
                    case R.id.finishRides:
                        FinishedRidesFragment finishedRidesFragment = new FinishedRidesFragment();
                        finishedRidesFragment.getIntance(driverName);
                        loadFragment(finishedRidesFragment);
                    case R.id.signOut:
                        Toast.makeText(getApplicationContext(), R.string.goodbye,Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        finish();
                }
                dl.closeDrawer(GravityCompat.START);
                getSupportActionBar().setTitle(R.string.close);
                supportInvalidateOptionsMenu();
                dl.addDrawerListener(t);
                return true;
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(t.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
    public void loadFragment(Fragment fragment) {
        // create a FragmentManager
        FragmentManager fm = getSupportFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit(); // save the changes
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void AccessContact() {
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
