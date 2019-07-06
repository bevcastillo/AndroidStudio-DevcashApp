package com.example.devcash;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class InventoryRecyclerViewAdapter extends RecyclerView.Adapter {
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customlayout_inventorylist, viewGroup, false);
        return new InvRecycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((InvRecycleViewHolder)viewHolder).bindView(i);
    }

    @Override
    public int getItemCount() {
        return InventoryRecyclerViewDataList.label.length;
    }

    private class InvRecycleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mPicturePath;
        private TextView mLabel;

        public InvRecycleViewHolder(View itemView){
            super(itemView);
            mPicturePath = (ImageView) itemView.findViewById(R.id.inventorylist_icon);
            mLabel = (TextView) itemView.findViewById(R.id.inventorylist_title);
            itemView.setOnClickListener(this);
        }

        //onlick listener for itemview
        @Override
        public void onClick(View v) {

        }

        public void bindView(int position){
            mPicturePath.setImageResource(InventoryRecyclerViewDataList.picturePath[position]);
            mLabel.setText(InventoryRecyclerViewDataList.label[position]);
        }
    }
}
