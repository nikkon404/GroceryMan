package com.nikkon.groceryman.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.nikkon.groceryman.Fragments.ChartFragment;
import com.nikkon.groceryman.Fragments.HomeFragment;
import com.nikkon.groceryman.Fragments.ScannerFragment;
import com.nikkon.groceryman.Fragments.ShoppingListFragment;
import com.nikkon.groceryman.R;
import com.nikkon.groceryman.Services.NotificationService;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {



    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnItemSelectedListener(this);
        loadFragment(new HomeFragment());
    }

    public void loadFragment(Fragment fragment) {
        //to attach fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.relativelayout, fragment).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.nav_home:
                fragment =  HomeFragment.getInstance();
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==444)
        {
            loadFragment(HomeFragment.getInstance());
            //do the things u wanted
        }
    }
}