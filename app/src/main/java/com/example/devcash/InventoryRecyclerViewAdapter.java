package com.example.devcash;

import android.content.Context;
import android.gesture.GestureLibraries;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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
import com.example.devcash.Fragments.CategoriesFragment;
import com.example.devcash.Fragments.DiscountsFragment;
import com.example.devcash.Fragments.ProductsFragment;
import com.example.devcash.Fragments.ServicesFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class InventoryRecyclerViewAdapter extends RecyclerView.Adapter<InventoryRecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<Integer> mIcon = new ArrayList<>();
    private ArrayList<String> mLabel = new ArrayList<>();
    private Context context;
    private int selectedPosition = -1; //add a highlight to the selected list in the recycleview
    private int selectedItem;

    public InventoryRecyclerViewAdapter(Context context, ArrayList<Integer> mIcon, ArrayList<String> mLabel) {
        this.mIcon = mIcon;
        this.mLabel = mLabel;
        this.context = context;
        selectedItem = 0;
    }

    //responsible for inflating the view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customlayout_inventorylist, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

        public void selectTaskListItem(int pos){
        int previousItem = selectedItem;
        selectedItem = pos;

        notifyItemChanged(previousItem);
        notifyItemChanged(pos);
        }

        @Override
        public void onBindViewHolder(@NonNull InventoryRecyclerViewAdapter.ViewHolder viewHolder, final int i) {
            Log.d(TAG, "onBindViewHolder: called.");

            Glide.with(context)
                    .asBitmap()
                    .load(mIcon.get(i))
                    .into(viewHolder.icon);

            viewHolder.label.setText(mLabel.get(i));
            if(selectedPosition == i || selectedItem == i){
                viewHolder.itemView.setBackgroundColor(Color.parseColor("#DCDCDC"));
                viewHolder.label.setTextColor(Color.parseColor("#ec4e20"));
                viewHolder.icon.setColorFilter(Color.parseColor("#ec4e20"));
            }else{
                viewHolder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
                viewHolder.label.setTextColor(Color.parseColor("#000000"));
                viewHolder.icon.setColorFilter(Color.parseColor("#000000"));
            }

            viewHolder.customLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "OnClick: clicked on: " + mLabel.get(i));
////                    Toast.makeText(context, mLabel.get(i), Toast.LENGTH_SHORT).show();

//                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    switch (i){
                        case 0:
                            ProductsFragment productsFragment = new ProductsFragment();
                            fragmentTransaction.replace(R.id.inventorylist_fragmentcontainer, productsFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            selectTaskListItem(i);
                            notifyItemChanged(selectedPosition);
                            selectedPosition=i;
                            notifyItemChanged(selectedPosition);
                            break;
                        case 1:
                            ServicesFragment servicesFragment = new ServicesFragment();
                            fragmentTransaction.replace(R.id.inventorylist_fragmentcontainer, servicesFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            notifyItemChanged(selectedPosition);
                            selectedPosition=i;
                            notifyItemChanged(selectedPosition);
                            break;
                        case  2:
                            CategoriesFragment categoriesFragment = new CategoriesFragment();
                            fragmentTransaction.replace(R.id.inventorylist_fragmentcontainer, categoriesFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            notifyItemChanged(selectedPosition);
                            selectedPosition=i;
                            notifyItemChanged(selectedPosition);
                            break;
                        case 3:
                            DiscountsFragment discountsFragment = new DiscountsFragment();
                            fragmentTransaction.replace(R.id.inventorylist_fragmentcontainer, discountsFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            notifyItemChanged(selectedPosition);
                            selectedPosition=i;
                            notifyItemChanged(selectedPosition);
                            break;
                    }
                }
            });

        }

    @Override
    public int getItemCount() {
        return mLabel.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView icon;
        TextView label;
        LinearLayout customLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.inventorylist_icon);
            label = itemView.findViewById(R.id.inventorylist_title);
            customLayout = itemView.findViewById(R.id.inventoryoptions_layout);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public interface onInventoryListener{
        void onInventoryClick(int position);
    }
}
