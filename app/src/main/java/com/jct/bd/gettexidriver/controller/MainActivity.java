package com.jct.bd.gettexidriver.controller;

import android.content.Intent;
import android.os.Bundle;
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
import com.jct.bd.gettexidriver.model.entities.Ride;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button firstFragment;
    String driverName;
    String driverEmail;
    String driverPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
// get the reference of Button's
        firstFragment = (Button) findViewById(R.id.firstFragment);
// perform setOnClickListener event on First Button
        firstFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new firstFragment());
            }
        });
    }
    private void loadFragment(firstFragment fragment) {
        fragment.getIntance(driverName);
// create a FragmentManager
      FragmentManager fm = getSupportFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit(); // save the changes
    }
}
