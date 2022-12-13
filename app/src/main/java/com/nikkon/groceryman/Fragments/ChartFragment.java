package com.nikkon.groceryman.Fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
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

    @SuppressLint("SetTextI18n")
    private void initChart() {
        pieChart = view.findViewById(R.id.piechart);
        LinearLayout legendLayout = view.findViewById(R.id.legendLayout);
        ItemModel itemModel = new ItemModel(getContext());
        Item[] items = itemModel.findAllItems();

        TextView tv = view.findViewById(R.id.txtLegend);
        tv.setText("Legend: (Total Items: " + items.length + ")");

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
            LinearLayout innerLayout = new LinearLayout(getContext());
            innerLayout.setOrientation(LinearLayout.HORIZONTAL);
            innerLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            innerLayout.setPadding(10, 10, 10, 12);
            //create vColor with color
            View vColor = new View(getContext());
            vColor.setLayoutParams(new LinearLayout.LayoutParams(45, 45));
            vColor.setBackgroundColor(color);
            vColor.setPadding(0,0,0,0);
            //create textview with category name
            TextView textView = new TextView(getContext());
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            textView.setText(category + " (" + count + ")");
            textView.setTextSize(13);
            textView.setGravity(Gravity.CENTER_VERTICAL);

            textView.setPadding(16, 0, 0, 0);


            //add vColor and textview to linearlayout
            innerLayout.addView(vColor);
            innerLayout.addView(textView);
            innerLayout.setGravity(Gravity.CENTER_VERTICAL);
            //add linearlayout to legend layout
            legendLayout.addView(innerLayout);
            //set center gravity
            legendLayout.setGravity(Gravity.CENTER_VERTICAL);
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