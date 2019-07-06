package com.example.devcash;

import android.content.Context;
import android.gesture.GestureLibraries;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class InventoryRecyclerViewAdapter extends RecyclerView.Adapter<InventoryRecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<Integer> mIcon = new ArrayList<>();
    private ArrayList<String> mLabel = new ArrayList<>();
    private Context context;

    public InventoryRecyclerViewAdapter(Context context, ArrayList<Integer> mIcon, ArrayList<String> mLabel) {
        this.mIcon = mIcon;
        this.mLabel = mLabel;
        this.context = context;
    }

    //responsible for inflating the view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customlayout_inventorylist, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

        @Override
        public void onBindViewHolder(@NonNull InventoryRecyclerViewAdapter.ViewHolder viewHolder, final int i) {
            Log.d(TAG, "onBindViewHolder: called.");
            Glide.with(context)
                    .asBitmap()
                    .load(mIcon.get(i))
                    .into(viewHolder.icon);


            viewHolder.label.setText(mLabel.get(i));
            viewHolder.customLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "OnClick: clicked on: " + mLabel.get(i));
                    Toast.makeText(context, mLabel.get(i), Toast.LENGTH_SHORT).show();
                }
            });

        }

    @Override
    public int getItemCount() {
        return mLabel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView icon;
        TextView label;
        LinearLayout customLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.inventorylist_icon);
            label = itemView.findViewById(R.id.inventorylist_title);
            customLayout = itemView.findViewById(R.id.inventoryoptions_layout);
        }
    }

////    private OnInventoryListener mOnInventoryListener;
//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        View view = LayoutInflater.from(viewGroup.getContext())
//                .inflate(R.layout.customlayout_inventorylist, viewGroup, false);
//
////        return new InvRecycleViewHolder(view, mOnInventoryListener);
//    }

//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
//        ((InvRecycleViewHolder)viewHolder).bindView(i);
//    }
//
//    @Override
//    public int getItemCount() {
//        return InventoryRecyclerViewDataList.label.length;
//    }
//
//    //adding listeners
//    private class InvRecycleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        private ImageView mPicturePath;
//        private TextView mLabel;
//        private View v;
//
//        //added for the onInventoryListener
//        OnInventoryListener onInventoryListener;
//
//        public InvRecycleViewHolder(View itemView, OnInventoryListener onInventoryListener){ //onInventoryListener is added
//            super(itemView);
//            mPicturePath = (ImageView) itemView.findViewById(R.id.inventorylist_icon);
//            mLabel = (TextView) itemView.findViewById(R.id.inventorylist_title);
//
//            //added for the onInventoryListener
//            this.onInventoryListener = onInventoryListener;
////            v = itemView;
//
//    //            itemView.setOnClickListener(this);
//        }
//
//        //onlick listener for itemview
//        @Override
//        public void onClick(View v) {
//            //add some code here..
//            onInventoryListener.onInventoryClick(getAdapterPosition());
//        }
//
//        public void bindView(int position){
//            mPicturePath.setImageResource(InventoryRecyclerViewDataList.picturePath[position]);
//            mLabel.setText(InventoryRecyclerViewDataList.label[position]);
//        }
//    }
//
//    public interface OnInventoryListener{
//        void onInventoryClick(int position);
//    }
}
