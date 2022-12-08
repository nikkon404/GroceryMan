package com.nikkon.groceryman.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nikkon.groceryman.Adaptors.ItemRecyclerViewAdapter;
import com.nikkon.groceryman.Models.Item;
import com.nikkon.groceryman.Models.ItemModel;
import com.nikkon.groceryman.R;


public class HomeFragment extends Fragment {
    ItemRecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    View view;
    ItemModel itemModel;

    private static HomeFragment instance;

    public HomeFragment() {
        // Required empty public constructor
    }

    //make fragment singleton
    public static HomeFragment getInstance() {
        if (instance == null) {
            instance = new HomeFragment();
        }
        return instance;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_home, container, false);

        itemModel = new ItemModel(getContext());

        initui();
        loadAllItems();
        return view;
    }


    private void initui(){
        recyclerView = view.findViewById(R.id.rvItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void loadAllItems(){
        setupData(itemModel.findAllItems());
    }


    //set up the RecyclerView
    void setupData(Item[] items){
        adapter = new ItemRecyclerViewAdapter(getContext(), items);
        recyclerView.setAdapter(adapter);
    }


}