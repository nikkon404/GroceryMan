package com.nikkon.groceryman.Fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView = view.findViewById(R.id.rvItems);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        EditText edtSearch = view.findViewById(R.id.edtSearch);

        view.findViewById(R.id.btnClear).setOnClickListener(v -> {
            edtSearch.setText("");
           dismissKeyboard();
            loadAllItems();
        });

        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                edtSearch.clearFocus();
                dismissKeyboard();
                performSearch(edtSearch.getText().toString().trim());
                return true;
            }
            return false;
        });
    }

    void dismissKeyboard(){
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void loadAllItems(){
        setupData(itemModel.findAllItems());
    }


    //set up the RecyclerView
    void setupData(Item[] items){
        adapter = new ItemRecyclerViewAdapter(getContext(), items);
        recyclerView.setAdapter(adapter);
    }

    private void performSearch(String query) {
        setupData(itemModel.searchItemsByTitle(query));

    }




}