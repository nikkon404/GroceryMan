package com.nikkon.groceryman.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikkon.groceryman.Models.GroceryResponse;
import com.nikkon.groceryman.Models.Item;
import com.nikkon.groceryman.R;
import com.nikkon.groceryman.Utils.Converter;
import com.nikkon.groceryman.Utils.Dialog;
import com.nikkon.groceryman.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LoadDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_data);
        //get the data from the intent
        String data = getIntent().getStringExtra("data");
        //set the data to the text view
       ( (TextView)findViewById(R.id.testText)).setText(data);
        new UpdateTask(LoadDataActivity.this).execute(data);

    }

}

 class UpdateTask extends AsyncTask<String, String,String> {

    private Item item;

    private Activity activity;
     public UpdateTask(Activity activity) {
            this.activity=activity;
     }

     @Override
     protected void onPostExecute(String s) {
         super.onPostExecute(s);
         if(item!=null){
             Intent intent = new Intent(activity, FormActivity.class);
             intent.putExtra("item",item);
             intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
             intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             activity.startActivity(intent);
             activity.finish();
         }
     }

     protected String doInBackground(String... urls) {

        Log.d("TAG", "doInBackground:     "+ urls);
        String url = urls[0];
        loadData(url);
        return "";
    }
     void loadData(String barCode) {
         //get request okhttp
         OkHttpClient client = new OkHttpClient();

         String url = Utils.API_URL + barCode;
         Request request = new Request.Builder()
                 .url(url)
                 .build();
         try {
             Response response = client.newCall(request).execute();
         }
         catch (Exception e) {
             e.printStackTrace();
         }
         client.newCall(request).enqueue(new Callback() {

             @Override
             public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                 ResponseBody body = response.body();
                 int code= response.code();
                 if(code != 200){
                     activity.runOnUiThread(() -> {
                         try {
                             Dialog.show(activity, "Error", response.body().string());
                         } catch (IOException e) {
                             e.printStackTrace();
                         }
                     });

                     return;

                 }
                 try {
                     if (body != null) {
                         String bodyString = body.string();
                         HashMap mapping = new ObjectMapper().readValue(bodyString, HashMap.class);
                            GroceryResponse groceryResponse = new GroceryResponse(mapping);
                            if(groceryResponse.getCode().equals("OK") &&  groceryResponse.getItems().length>0){
                                //goto form page
                                 item = groceryResponse.getItems()[0];
                               onPostExecute("");

                            }
                            else{
                                activity.runOnUiThread(() -> Dialog.show(activity,"Error",groceryResponse.getCode()+ ": Failed to load data"));
                            }
                     }
                 }
                    catch (IOException e) {
                        e.printStackTrace();
                        String messgae = e.getMessage();
                        Log.e("TAG", "onResponse:  "+messgae );
                        activity.runOnUiThread(() -> Dialog.show(activity, "Error", messgae));
                 }

             }
             @Override
             public void onFailure(@NonNull Call call, @NonNull IOException e) {

                 Log.d("TAG", "onFailure: " + e.getMessage());

             }
         });
     }

}