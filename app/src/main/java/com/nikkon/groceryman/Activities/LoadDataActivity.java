package com.nikkon.groceryman.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
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

        new UpdateTask(getApplicationContext()).execute(data);

    }




//    void loadData(String barCode){
//
//        //make http request to the api
//        // getting a new volley request
//        // queue for making new requests
//        RequestQueue volleyQueue = Volley.newRequestQueue(this);
//
//        // url of the api through which we get random dog images
//        String url = Utils.API_URL + barCode;
//        StringRequest stringRequest = new StringRequest(
//                Request.Method.GET,
//                url,
//                response -> {
//                    // do something with the response
//                    // response is the json string
//                    // we can parse it using json library
//                    // and get the data
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        // get the status of the response
////                            String status = jsonObject.getString("status");
////                            if (status.equals("1")) {
////                                // get the data from the response
////                                JSONObject data = jsonObject.getJSONObject("data");
////                                // get the image url from the data
////                                String imageUrl = data.getString("url");
////                                // load the image using picasso
////                                Picasso.get().load(imageUrl).into(imageView);
////                            } else {
////                                // if the status is not 1
////                                // then show the error message
////                                String message = jsonObject.getString("message");
////                                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
////                            }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                },
//                error -> {
//                    // do something when error occurred
//                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
//                }
//        );
//
//        // add the json request object created above
//        // to the Volley request queue
//        volleyQueue.add(stringRequest);
//
//
//
//
//
//    }

}

 class UpdateTask extends AsyncTask<String, String,String> {

    private Context context;
     public UpdateTask(Context applicationContext) {
            this.context=applicationContext;

     }

     protected String doInBackground(String... urls) {

        Log.d("TAG", "doInBackground:     "+ urls);
        String url = urls[0];

        loadData2(url);

        return "";
    }
     void loadData2(String barCode) {
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
                 try {
                     if (body != null) {
                         String bodyString = body.string();
                         HashMap mapping = new ObjectMapper().readValue(bodyString, HashMap.class);
                            GroceryResponse groceryResponse = new GroceryResponse(mapping);
                            if(groceryResponse.getCode()=="200"){
                                //goto form page
                                Item item = groceryResponse.getItems()[0];
                            }
                            else{
                                Dialog.show(context,"Error",groceryResponse.getCode()+ ": Failed to load data");
                            }
                     }
                 }
                    catch (IOException e) {
                        e.printStackTrace();
                        String messgae = e.getMessage();
                        Log.e("TAG", "onResponse:  "+messgae );
                        Dialog.show(context, "Error", messgae);
                 }

             }

             @Override
             public void onFailure(@NonNull Call call, @NonNull IOException e) {

                 Log.d("TAG", "onFailure: " + e.getMessage());

             }
         });
     }

}