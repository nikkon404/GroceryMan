package com.nikkon.groceryman.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.nikkon.groceryman.Models.Item;
import com.nikkon.groceryman.Models.ItemModel;
import com.nikkon.groceryman.R;
import com.nikkon.groceryman.Services.NotificationService;
import com.nikkon.groceryman.Utils.AppSnackBar;
import com.nikkon.groceryman.Utils.Utilities;
import com.nikkon.groceryman.Utils.Dialog;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;


public class FormActivity extends AppCompatActivity {

    ////Creating UI element Instances
    EditText title, description, brand;
    Spinner category;
    String spncategory;
    TextView expdate, remdate, txtPickImg;
    ImageView imageview;
    Button btnSave;
    Calendar remindertime;

    Item fetchedItem;
    View view;

    Boolean isEdit = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        fetchedItem = (Item) getIntent().getSerializableExtra("item");
        isEdit = getIntent().getBooleanExtra("edit", false);
        initUI();
        setupValues();
        getSupportActionBar().setTitle(isEdit? "Edit Item": "Add Item");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().  setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }



    //initializing UI elements
    void initUI() {
        view = findViewById(android.R.id.content);
        title = findViewById(R.id.txtTitle);
        description = findViewById(R.id.txtDescription);
        brand = findViewById(R.id.txtBrand);
        category = findViewById(R.id.spnCategory);
        expdate = findViewById(R.id.expDate);
        remdate = findViewById(R.id.remDate);
        imageview = findViewById(R.id.imageview);
        imageview = findViewById(R.id.imageview);
        txtPickImg = findViewById(R.id.pickImg);
        txtPickImg.setOnClickListener(v -> pickImage());
        imageview.setOnClickListener(v -> pickImage());


        //collection of category list
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.array_category, android.R.layout.simple_spinner_item);
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



        expdate.setOnClickListener(view -> pickExpDate());
        remdate.setOnClickListener(view -> pickRemDate());


        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> {
            String err = validate();
            if (err.isEmpty()) {
               boolean result =  isEdit? updateItem() : saveItem();
                if(result) {
                    MainActivity.instance.updateHomeData();
                    finish();
                }
                else {
                    Dialog.show(this, "Error", "Something went wrong");
                }

            } else {
                Dialog.show(this, "Error", err);
            }

        });

    }

    boolean saveItem(){
        int id  = new ItemModel(this).addItem(fetchedItem);
        boolean success = id > 0;
        if(success){
            fetchedItem.setID(id);
            NotificationService.getInstance(this)
                    .scheduleNotification(remindertime.getTimeInMillis(), fetchedItem);

            Toast.makeText(this, "Item Added!", Toast.LENGTH_SHORT).show();

        }
        return success;

    }

    boolean updateItem(){
        boolean success = new ItemModel(this).updateItem(fetchedItem);
        if(success){
            Toast.makeText(this, "Item Updated!", Toast.LENGTH_SHORT).show();
        }
        return success;

    }

    void setupValues() {
        try {

            title.setText(fetchedItem.getTitle());
            description.setText(fetchedItem.getDescription());
            brand.setText(fetchedItem.getBrand());

            if (isEdit) {
                btnSave.setText("Update");
                    imageview.setVisibility(View.VISIBLE);
                    imageview.setImageBitmap(Utilities.decodeImage(fetchedItem.getBase64Image()));
                    expdate.setText(fetchedItem.getExpdate().toString());
                    //set category spinner value

                spncategory = fetchedItem.getCategory();
                int spinnerPosition = ((ArrayAdapter) category.getAdapter()).getPosition(spncategory);
                category.setSelection(spinnerPosition);

            } else {
                //checking if array is not empty
                if (fetchedItem.getImages().size() > 0) {

                    String imageUrl = fetchedItem.getImages().get(0);
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(imageUrl).getContent());
                    imageview.setImageBitmap(bitmap);
                    fetchedItem.setBase64Image(Utilities.encodeImage(bitmap));

                }
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //callback method for picking image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
        if(data==null){
            return;
        }
            Uri uri = data.getData();

            imageview.setImageURI(uri);

            //get bitmap from imageview
            Bitmap bitmap = ((BitmapDrawable) imageview.getDrawable()).getBitmap();
            fetchedItem.setBase64Image(Utilities.encodeImage(bitmap));
            // Use the uri to load the image
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            // Use ImagePicker.Companion.getError(result.getData()) to show an error
        }
    }

    //image picker
    public void pickImage() {
        ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start();


    }


    //handler for expiration date picker
    public void pickExpDate() {
        final Calendar currentDate = Calendar.getInstance();
        Calendar finalDate = Calendar.getInstance();
        DatePickerDialog dialog =new DatePickerDialog(this, (DatePicker view, int year, int monthOfYear, int dayOfMonth) -> {
            finalDate.set(year, monthOfYear, dayOfMonth);
            new TimePickerDialog(FormActivity.this, (timePicker, i, i1) -> {
                finalDate.set(Calendar.HOUR_OF_DAY, i);
                finalDate.set(Calendar.MINUTE, i1);
                finalDate.set(Calendar.SECOND, 0);
                finalDate.set(Calendar.MILLISECOND, 0);
                fetchedItem.setExpdate(finalDate.getTime());
                expdate.setText(finalDate.getTime().toString());

                //also set reminder date
                //one day before expiration date
                Calendar reminderDate = Calendar.getInstance();
                reminderDate.setTime(finalDate.getTime());
                reminderDate.add(Calendar.DATE, -1);
                setRemindertime(reminderDate);
            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), true).show();

        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));

//        DatePickerDialog dialog =  DatePickerDialog(this, (datePicker, y, m, d) ->
//        {
//
//        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));

        dialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());
        dialog.show();

    }

    //handler for reminder date picker
    public void pickRemDate() {
        if (isEdit){
            AppSnackBar.showSnack(this.view, "You can't change reminder date");
            return;
        }
        final Calendar currentDate = Calendar.getInstance();
        Calendar finalDate = Calendar.getInstance();
        new DatePickerDialog(this, (datePicker, y, m, d) -> {
            finalDate.set(y, m, d);
            new TimePickerDialog(FormActivity.this, (timePicker, i, i1) -> {
                finalDate.set(Calendar.HOUR_OF_DAY, i);
                finalDate.set(Calendar.MINUTE, i1);
                finalDate.set(Calendar.SECOND, 0);
                finalDate.set(Calendar.MILLISECOND, 0);

                setRemindertime(finalDate);

            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), true).show();

        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    void setRemindertime(Calendar time){
        remindertime = time;
        remdate.setText(time.getTime().toString());
    }


    String validate() {
        //set values to item object
        fetchedItem.setTitle(title.getText().toString());
        fetchedItem.setDescription(description.getText().toString());
        fetchedItem.setBrand(brand.getText().toString());
        fetchedItem.setCategory(spncategory);


        //validate
        if (fetchedItem.getTitle().isEmpty()) {
            return "Title is required";
        }
        if (fetchedItem.getDescription().isEmpty()) {
            return "Description is required";
        }
        if (fetchedItem.getBrand().isEmpty()) {
            return "Brand is required";
        }
        if (fetchedItem.getCategory().isEmpty()) {
            return "Category is required";
        }
        if (fetchedItem.getBase64Image() == null) {
            return "Image is required";
        }
        if (fetchedItem.getExpdate() == null) {
            return "Expiry date is required";
        }
        if (!isEdit && remindertime == null) {
            return "Reminder date is required";
        }

        return "";

    }


}