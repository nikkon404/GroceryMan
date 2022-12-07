package com.nikkon.groceryman.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.nikkon.groceryman.Models.Item;
import com.nikkon.groceryman.R;
import com.nikkon.groceryman.Utils.Converter;

import java.util.Calendar;

public class FormActivity extends AppCompatActivity {

    ////Creating UI element Instances
    EditText title,description,brand;
    Spinner category;
    String spncategory;
    DatePickerDialog datepicker;
    TextView expdate,remdate;
    ImageView imageview;
    String path;
    Uri uri;

    Item fetchedItem;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        fetchedItem = (Item)getIntent().getSerializableExtra("item");


        initUI();
        setupValues();

    }

    void initUI(){
        title = findViewById(R.id.txtTitle);
        description = findViewById(R.id.txtDescription);
        brand = findViewById(R.id.txtBrand);
        category = findViewById(R.id.spnCategory);
        expdate = findViewById(R.id.expDate);
        remdate = findViewById(R.id.remDate);
//        imagepick = findViewById(R.id.pickimage);
        imageview = findViewById(R.id.imageview);
        datepicker = new DatePickerDialog(getApplicationContext());
        imageview = findViewById(R.id.imageview);

        //collection of caregory list
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.array_category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter);

        //display category list on spinner
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spncategory = category.getSelectedItem().toString();
               fetchedItem.setCategory(spncategory);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //datepicker t0 set expiry date
        final Calendar calendar = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);

        expdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datepicker = new DatePickerDialog(getApplicationContext(), new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                        // adding the selected date in the edittext
                        expdate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);


                datepicker.getDatePicker().setMinDate(calendar.getTimeInMillis());
                // show the dialog
                datepicker.show();
            }
        });

        //datepicker t0 set reminder date
        remdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datepicker = new DatePickerDialog(getApplicationContext(), new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                        // adding the selected date in the edittext
                        expdate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);

                String expirydate = String.valueOf(calendar.before(expdate.getText()));
                datepicker.getDatePicker().setMaxDate(Long.parseLong(expirydate));
                // show the dialog
                datepicker.show();
            }
        });

//        imagepick.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

    }

    void setupValues(){
        title.setText(fetchedItem.getTitle());
        description.setText(fetchedItem.getDescription());
        brand.setText(fetchedItem.getBrand());

        //checking if array is not empty

        if(fetchedItem.getImages().size() >0){
            String imageUrl =  fetchedItem.getImages().get(0);
            imageview.setImageDrawable(Converter.LoadImageFromWebOperations(imageUrl));


        }


    }
}