package com.yde.sapiensdelivery.controllers.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yde.sapiensdelivery.R;
import com.yde.sapiensdelivery.controllers.EditShoppingListActivity;
import com.yde.sapiensdelivery.use_cases.ShoppingListManager;

public class CommodityListAdapter extends RecyclerView.Adapter<CommodityListAdapter.ViewHolder> {

    private ShoppingListManager slManager;
    private EditShoppingListActivity activity;

    public CommodityListAdapter (EditShoppingListActivity activity, ShoppingListManager slManager){
        this.activity = activity;
        this.slManager = slManager;
    }

    /**
     * A class that holds the View for each individual index of the RecyclerView
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{
        Button add1BT;
        Button remove1BT;
        TextView commName;
        TextView commPrice;
        TextView commQuantity;

        ViewHolder(View view) {
            super(view);
            this.add1BT = view.findViewById(R.id.add_comm_BT);
            this.remove1BT = view.findViewById(R.id.remove_comm_BT);
            this.commName = view.findViewById(R.id.comm_name_TV);
            this.commPrice = view.findViewById(R.id.comm_price_TV);
            this.commQuantity = view.findViewById(R.id.comm_quant_TV);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View commView = LayoutInflater.from((parent.getContext())).
                inflate(R.layout.commodity_list,parent,false);
        return new ViewHolder(commView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CommodityListAdapter.ViewHolder holder, int position) {
        // Binds the ViewHolder to data to reflect the item at the given position.
        holder.commName.setText(slManager.getCommodityName(position));
        holder.commPrice.setText("$ " + slManager.getCommodityTotalPrice(position));
        holder.commQuantity.setText("x " + slManager.getCommodityQuantity(position));

        // When buttons are clicked, modify data and update the ViewHolder
        holder.remove1BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                slManager.remove1Commodity(position);
                notifyItemChanged(position);
            }
        });

        holder.add1BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                slManager.add1Commodity(position);
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return slManager.getSize();
    }

    public void setCommList(ShoppingListManager slManager, int position) {
        this.slManager = slManager;
        notifyItemChanged(position);
    }
}