package com.nikkon.groceryman.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.nikkon.groceryman.R;

public class LoadDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_data);
        //get the data from the intent
        String data = getIntent().getStringExtra("data");
        //set the data to the text view
       ( (TextView)findViewById(R.id.testText)).setText(data);
    }
}