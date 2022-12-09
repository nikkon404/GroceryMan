package com.nikkon.groceryman.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.vision.text.Line;
import com.nikkon.groceryman.Models.Item;
import com.nikkon.groceryman.Models.ItemModel;
import com.nikkon.groceryman.R;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.List;


public class ChartFragment extends Fragment {
    PieChart pieChart;
    View view;


    public ChartFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_chart, container, false);

        initChart();

        return view;
    }

    private void initChart() {
        pieChart = view.findViewById(R.id.piechart);
        LinearLayout legendLayout = view.findViewById(R.id.legendLayout);
        ItemModel itemModel = new ItemModel(getContext());
        Item[] items = itemModel.findAllItems();

        //make a list of items by category
        List<String> categories = new ArrayList<>();
        for (Item item : items) {
            if (!categories.contains(item.getCategory())) {
                categories.add(item.getCategory());
            }
        }
        //convert items array to list
        List<Item> itemList = new ArrayList<>();
        for (Item item : items) {
            itemList.add(item);
        }
        //count items by category
        for (String category : categories) {
            int count = 0;
            for (Item item : itemList) {
                if (item.getCategory().equals(category)) {
                    count++;
                }
            }
            int color = getRandomColor(); //Color.parseColor("#" + Integer.toHexString(getContext().getResources().getColor(R.color.purple_200) & 0x00ffffff));
            pieChart.addPieSlice(new PieModel(category, count, color));

            //create new linearlayout with color and category name
            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout.setPadding(10, 10, 10, 10);
            //create view with color
            View view = new View(getContext());
            view.setLayoutParams(new LinearLayout.LayoutParams(50, 50));
            view.setBackgroundColor(color);
            //create textview with category name
            TextView textView = new TextView(getContext());
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            textView.setText(category);
            textView.setTextSize(18);
            textView.setPadding(10, 10, 10, 10);
            //add view and textview to linearlayout
            linearLayout.addView(view);
            linearLayout.addView(textView);
            //add linearlayout to legend layout
            legendLayout.addView(linearLayout);
        }

        pieChart.startAnimation();
    }

    //get random color
    private int getRandomColor() {
        int red = (int) (Math.random() * 256);
        int green = (int) (Math.random() * 256);
        int blue = (int) (Math.random() * 256);
        return Color.rgb(red, green, blue);
    }
}