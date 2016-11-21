package com.scottlindley.networkinginandroidlab;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Scott Lindley on 11/17/2016.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>{
    private List<Item> mItems;

    public RecyclerAdapter(List<Item> items) {
        mItems = items;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.mItemName.setText(mItems.get(position).getName());
        holder.mItemPrice.setText(mItems.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    protected class RecyclerViewHolder extends RecyclerView.ViewHolder{
        public TextView mItemName, mItemPrice;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            mItemName = (TextView)itemView.findViewById(R.id.item_name);
            mItemPrice = (TextView)itemView.findViewById(R.id.item_price);
        }
    }
}
