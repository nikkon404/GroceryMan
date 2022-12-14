package com.nikkon.groceryman.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nikkon.groceryman.Activities.MapActivity;
import com.nikkon.groceryman.Adaptors.ShoppingRecyclerViewAdapter;
import com.nikkon.groceryman.Models.Shopping;
import com.nikkon.groceryman.Models.ShoppingModel;
import com.nikkon.groceryman.R;
import com.nikkon.groceryman.Utils.AppSnackBar;
import com.nikkon.groceryman.Utils.Utilities;

public class ShoppingListFragment extends Fragment {
    View view;

    Button btnShowMap;
    FloatingActionButton fabAddItem;
    ShoppingModel shoppingModel;
    RecyclerView recyclerView;
    ShoppingRecyclerViewAdapter adapter;


    public ShoppingListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        shoppingModel = new ShoppingModel(getContext());


        initUI();
        loadAllItems();
        return view;
    }

    void initUI(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView = view.findViewById(R.id.rvShoppingList);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        btnShowMap = view.findViewById(R.id.btnShowMap);
        btnShowMap.setOnClickListener(v -> {
            if(!Utilities.hasInternet(view.getContext())){
                AppSnackBar.showSnack(view, "Please check your internet connection and try again");
                return;
            }
           askLocationPermission();
        });

        fabAddItem = view.findViewById(R.id.fab);
        fabAddItem.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Add Item", Toast.LENGTH_SHORT).show();

            showDialog();

        });
    }

    public void loadAllItems(){
        setupData(shoppingModel.findAllShoppings());
    }


    void askLocationPermission(){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Permission needed")
                        .setMessage("Please allow location permission")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        } else {
            Intent intent = new Intent(getActivity(), MapActivity.class);
            startActivity(intent);
        }
    }


    //set up the RecyclerView
    void setupData(Shopping[] shoppingList){
        adapter = new ShoppingRecyclerViewAdapter( shoppingList, getContext());
        recyclerView.setAdapter(adapter);
    }

    void showDialog(){
        final EditText edtItem = new EditText(getContext());
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Add a new item")
                .setView(edtItem)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String data = String.valueOf(edtItem.getText());
                        shoppingModel.addShopping(new Shopping(data));
                        loadAllItems();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();

    }
}