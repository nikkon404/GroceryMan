package com.nikkon.groceryman.Adaptors;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.nikkon.groceryman.Activities.FormActivity;
import com.nikkon.groceryman.Activities.ItemDetailActivity;
import com.nikkon.groceryman.Models.Item;
import com.nikkon.groceryman.Models.ItemModel;
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

        //show edit or delete options on long click

        holder.itemView.setOnLongClickListener(v -> {
            //open bottom sheet
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(v.getContext());
            //create linear layout with 2 text views
            LinearLayout linearLayout = new LinearLayout(v.getContext());
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            //set linear layout padding
            linearLayout.setPadding(20, 20, 20, 20);
            //create text views
            TextView tvEdit = new TextView(v.getContext());
            TextView tvDelete = new TextView(v.getContext());
            //set text
            tvEdit.setText("Edit");
            tvDelete.setText("Delete");
            //set text size
            tvEdit.setTextSize(20);
            tvDelete.setTextSize(20);
            //set padding
            tvEdit.setPadding(20, 20, 20, 20);
            tvDelete.setPadding(20, 20, 20, 20);
            //set text color
            tvEdit.setTextColor(Color.parseColor("#000000"));
            tvDelete.setTextColor(Color.parseColor("#FF0000"));

            //get default pencil icon from android
            Drawable pencil = v.getContext().getDrawable(android.R.drawable.ic_menu_edit);
            //get default trash icon from android
            Drawable trash = v.getContext().getDrawable(android.R.drawable.ic_menu_delete);
            //set icon to text view
            tvEdit.setCompoundDrawablesWithIntrinsicBounds(pencil, null, null, null);
            tvDelete.setCompoundDrawablesWithIntrinsicBounds(trash, null, null, null);




            //add text views to linear layout
            linearLayout.addView(tvEdit);
            linearLayout.addView(tvDelete);
            //set content view
            bottomSheetDialog.setContentView(linearLayout);
            bottomSheetDialog.show();


            ItemModel itemModel = new ItemModel(v.getContext());

            //on click listener for edit
            tvEdit.setOnClickListener(v1 -> {
                //open details activity
                Intent intent = new Intent(v.getContext(), FormActivity.class);
                intent.putExtra("item", item);
                intent.putExtra("edit", true);
                v.getContext().startActivity(intent);
                bottomSheetDialog.dismiss();
            });

            //on click listener for delete
            tvDelete.setOnClickListener(v12 -> {
                //delete item from database
                itemModel.deleteItem(item.getID());
                Snackbar.make(v12, "Item deleted", Snackbar.LENGTH_SHORT).show();

                itemsArray = ArrayUtils.removeAll(itemsArray, item);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, itemsArray.length);
                bottomSheetDialog.dismiss();
            });

            return true;
        });

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