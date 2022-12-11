package com.nikkon.groceryman.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.nikkon.groceryman.Models.Item;
import com.nikkon.groceryman.R;
import com.nikkon.groceryman.Utils.Utilities;

import java.util.Objects;

public class ItemDetailActivity extends AppCompatActivity {


    Item fetchedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        fetchedItem = (Item) getIntent().getSerializableExtra("item");
        initUI();
    }

    void initUI(){
        Objects.requireNonNull(getSupportActionBar()).setTitle(fetchedItem.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);// provide compatibility to all the versions

        ((ImageView)findViewById(R.id.groceryImage)).setImageBitmap(
                Utilities.decodeImage(fetchedItem.getBase64Image())
        );

        ((TextView) findViewById(R.id.tvGroceryItemTitle)).setText(fetchedItem.getTitle());
        ((TextView) findViewById(R.id.tvGroceryItemDesc)).setText(fetchedItem.getDescription());
        ((TextView) findViewById(R.id.tvGroceryItemBrand)).setText(fetchedItem.getBrand());
        ((TextView) findViewById(R.id.tvGroceryItemCat)).setText(fetchedItem.getCategory());
        ((TextView) findViewById(R.id.tvAddedIn)).setText(Utilities.getReadableDate(fetchedItem.getCreatedAt()));

        String expText = "";
        int daysLeft = fetchedItem.getDaysBeforeExpiration();
        if(daysLeft > 0){
            expText = daysLeft + " days left - ("+ Utilities.getReadableDate(fetchedItem.getExpdate())+")";
        }else if(daysLeft == 0){
            expText = "Expired Today";
        }else{
            expText = "Expired "+(-daysLeft)+" days ago";

        }
        ((TextView) findViewById(R.id.tvExpInfo)).setText(expText);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            //check if there is any activity in the stack
            if(isTaskRoot()){
                startActivity(new Intent(this,MainActivity.class));
            }

            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}