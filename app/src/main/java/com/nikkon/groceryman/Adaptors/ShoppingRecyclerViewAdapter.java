package com.nikkon.groceryman.Adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.util.ArrayUtils;
import com.nikkon.groceryman.Models.Shopping;
import com.nikkon.groceryman.Models.ShoppingModel;
import com.nikkon.groceryman.R;



//recycler view adapter for shopping list

public class ShoppingRecyclerViewAdapter extends RecyclerView.Adapter<ShoppingRecyclerViewAdapter.ViewHolder> {

    private Shopping[] shoppingList;
    private Context context;
    private ShoppingModel shoppingModel;

    public ShoppingRecyclerViewAdapter(Shopping[] shoppingList, Context context) {
        this.shoppingList = shoppingList;
        this.context = context;
        shoppingModel = new ShoppingModel(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Shopping shopping = shoppingList[position];
        holder.tvTitle.setText(shopping.getTitle());
//        holder.tvCreatedAt.setText(shopping.getCreatedAt());
        holder.btnDelete.setOnClickListener(v -> {
            shoppingModel.deleteShoppingById(shopping.getId());
             shoppingList = ArrayUtils.removeAll(shoppingList, shopping);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, shoppingList.length);

        });
    }

    @Override
    public int getItemCount() {
        return shoppingList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvCreatedAt;
        ImageView btnDelete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvGShoppingTitle);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}