package com.nikkon.groceryman.Fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.nikkon.groceryman.Adaptors.ItemRecyclerViewAdapter;
import com.nikkon.groceryman.Models.Item;
import com.nikkon.groceryman.Models.ItemModel;
import com.nikkon.groceryman.R;
import com.nikkon.groceryman.Utils.AppSnackBar;


public class HomeFragment extends Fragment {
    ItemRecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    View view;
    ItemModel itemModel;


    public HomeFragment() {
        // Required empty public constructor
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    //initiate ui
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

        Spinner spinner = view.findViewById(R.id.spnCatHome);
        //set adapter for spinner
        //collection of category list
        String[] categories = getContext().getResources().getStringArray(R.array.array_category);
        //add all to the beginning of the list
        String[] allCategories = new String[categories.length + 1];
        allCategories[0] = "All categories";
        System.arraycopy(categories, 0, allCategories, 1, categories.length);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, allCategories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //display category list on spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String Cat = spinner.getSelectedItem().toString();
                if (Cat.equals("All categories")) {
                    loadAllItems();
                } else {
                    setupData(itemModel.findItemsByCategory(Cat));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //
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
        Context context = getContext();
        if(context != null){
            adapter = new ItemRecyclerViewAdapter(context, items);
            recyclerView.setAdapter(adapter);
        }

        if(items.length == 0){
            AppSnackBar.showSnack(view, "No items found");
        }

    }

    //search for items
    private void performSearch(String query) {
        setupData(itemModel.searchItemsByTitle(query));

    }




}