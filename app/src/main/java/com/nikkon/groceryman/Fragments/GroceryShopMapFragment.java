package com.nikkon.groceryman.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.gms.tasks.Task;
import com.nikkon.groceryman.R;
import com.nikkon.groceryman.Utils.AppConst;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class GroceryShopMapFragment extends Fragment {


    GoogleMap map;

    @SuppressLint("MissingPermission")
    private void getLastLocation() {

            FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
            mFusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, new CancellationToken() {
                @Override
                public boolean isCancellationRequested() {
                    return false;
                }

                @NonNull
                @Override
                public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                    return null;
                }
            }).addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location == null) {
                      //  requestNewLocationData();
                    } else {
                        map.setMyLocationEnabled(true);
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
//                        map.addMarker(new MarkerOptions().position(latLng).title("You are here"));

                        //comma separated lat and long
                        String latlong = location.getLatitude() + "," + location.getLongitude();
                        findNearbyGroceryShops(latlong);
                    }
                }
            });

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grocery_shop_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(googleMap -> {
                map = googleMap;
                getLastLocation();
            });
        }
    }

    //find nearby grocery shops
    void findNearbyGroceryShops(String latLong) {
        OkHttpClient client = new OkHttpClient();
        String params = "location=" + latLong + "&radius=5000&keyword=grocery&key=" + getString(R.string.google_key);
        String url = AppConst.PLACES_API_URL + params;

        //make a request to the url
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    String bodyString = Objects.requireNonNull(response.body()).string();
                    HashMap mapping = new ObjectMapper().readValue(bodyString, HashMap.class);
                     //get key "results" from the mapping

                    //results is an array of objects
                    //each object has a key "name" and "geometry"
                    //geometry is an object with a key "location"
                    //location is an object with keys "lat" and "lng"
                    //lat and lng are the latitude and longitude of the grocery shop

                    ArrayList<HashMap> results = (ArrayList) mapping.get("results");
                    //loop through the results array

                    //switch to main thread
                    getActivity().runOnUiThread(() -> {
                        results.forEach(result -> {
                            //get the name of the grocery shop
                            String name = (String) result.get("name");
                            //get the geometry object
                            HashMap geometry = (HashMap) result.get("geometry");
                            //get the location object
                            HashMap location = (HashMap) geometry.get("location");
                            //get the latitude and longitude of the grocery shop
                            double lat = (double) location.get("lat");
                            double lng = (double) location.get("lng");
                            String address = (String) result.get("vicinity");
                            //add a marker to the map
                            //add marker with subtitle
                            map.addMarker(new MarkerOptions()
                                    .position(new LatLng(lat, lng))
                                    .title(name)
                                    .snippet(address));

                        });
                    });

                }

            }
        });
        }










}