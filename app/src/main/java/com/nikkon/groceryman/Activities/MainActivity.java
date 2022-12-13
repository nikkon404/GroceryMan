package com.nikkon.groceryman.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.nikkon.groceryman.Fragments.ChartFragment;
import com.nikkon.groceryman.Fragments.HomeFragment;
import com.nikkon.groceryman.Fragments.ScannerFragment;
import com.nikkon.groceryman.Fragments.ShoppingListFragment;
import com.nikkon.groceryman.R;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {


    public static MainActivity instance;
    private HomeFragment homeFragment;




    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnItemSelectedListener(this);
        //setting instance of singleton class
        instance = this;

        //check if the app is opened for the first time
        if (savedInstanceState != null) {
            //Restore the fragment's instance
            homeFragment = (HomeFragment) getSupportFragmentManager().getFragment(savedInstanceState, "homeFragment");

        }
        else {
            homeFragment = new HomeFragment();
        }

        //making the home fragment the default fragment
        loadFragment(new HomeFragment());

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's instance
        if (homeFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "homeFragment", homeFragment);

        }

    }

    private void loadFragment(Fragment fragment) {
        try {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.relativelayout, fragment);
            ft.commitAllowingStateLoss();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    //set bottom navigation index
    public void updateHomeData() {
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.menu_main is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    //handle menu item clicks
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.nav_home:
                fragment =  new HomeFragment();
                break;
            case R.id.nav_chart:
                fragment = new ChartFragment();
                break;
            case R.id.nav_shopping_list:
                fragment = new ShoppingListFragment();
                break;
            case R.id.nav_qr_scan:
                fragment = new ScannerFragment();
                break;
        }
        if (fragment != null) {
            loadFragment(fragment);
        }
       return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }


    //handle menu item clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
        case R.id.info:
            startActivity(new Intent(this, InfoActivity.class));
            return(true);

    }
        return(super.onOptionsItemSelected(item));
    }





}