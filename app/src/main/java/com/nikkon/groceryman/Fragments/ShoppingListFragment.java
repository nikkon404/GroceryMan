package com.nikkon.groceryman.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.nikkon.groceryman.Activities.MapActivity;
import com.nikkon.groceryman.R;
public class ShoppingListFragment extends Fragment {
    View view;

    Button btnShowMap;

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

        btnShowMap = view.findViewById(R.id.btnShowMap);
        btnShowMap.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MapActivity.class);
            startActivity(intent);
        });

        return view;
    }
}