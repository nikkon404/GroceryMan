package com.nikkon.groceryman.Adaptors;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nikkon.groceryman.Activities.ItemDetailActivity;
import com.nikkon.groceryman.Models.Item;
import com.nikkon.groceryman.R;
import com.nikkon.groceryman.Utils.Converter;

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder> {

    private Item[] itemsArray;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public ItemRecyclerViewAdapter(Context context, Item[] data) {
        this.mInflater = LayoutInflater.from(context);
        this.itemsArray = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = itemsArray[position];
        holder.tvTitle.setText(item.getTitle());
        holder.myImageView.setImageBitmap(Converter.decodeImage(item.getBase64Image()));
        //int to string
        String expdate = String.valueOf(item.getDaysBeforeExpiration());
        holder.tvDays.setText(expdate);

        //on click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open details activity
                Intent intent = new Intent(v.getContext(), ItemDetailActivity.class);
                intent.putExtra("item", item);
                v.getContext().startActivity(intent);
            }
        });

        //make the view red if the item is expired
        if (item.getDaysBeforeExpiration() < 0) {

            //light red color
            holder.itemView.setBackgroundColor(0xFFFFE0E0);
        }


    }

    // total number of rows
    @Override
    public int getItemCount() {
        return itemsArray.length;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTitle;
        TextView tvDays;
        ImageView myImageView;

        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvGroceryTitle);
            myImageView = itemView.findViewById(R.id.imgGrocery);
            tvDays = itemView.findViewById(R.id.tvExpDays);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());

         }
    }


    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}