package com.example.devcash.CustomAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.devcash.Object.Product;
import com.example.devcash.Object.Services;
import com.example.devcash.R;

import java.util.List;

public class AllPurchasesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final int VIEW_TYPE_PRODUCT = 0;
    final int VIEW_TYPE_SERVICE = 1;

    Context context;
    List<Product> productList;
    List<Services> servicesList;

    public AllPurchasesAdapter(Context context, List<Product> productList, List<Services> servicesList) {
        this.context = context;
        this.productList = productList;
        this.servicesList = servicesList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {

//        View itemView = viewGroup.getRootView();

        if(position == VIEW_TYPE_PRODUCT){
            return new ProductViewHolder(viewGroup.getRootView());
        }

        if(position == VIEW_TYPE_SERVICE){
            return new ServiceViewHolder(viewGroup.getRootView());
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if(viewHolder instanceof ProductViewHolder){
            ((ProductViewHolder) viewHolder).populate(productList.get(i));
        }

        if(viewHolder instanceof ServiceViewHolder){
            ((ServiceViewHolder) viewHolder).populate(servicesList.get(i - productList.size()));
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType(int position){
        if(position < productList.size()){
            return VIEW_TYPE_PRODUCT;
        }

        if(position - productList.size() < servicesList.size()){
            return VIEW_TYPE_SERVICE;
        }

        return -1;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView prodName;
        TextView timeStamp;
        ImageView userImage;

        public ProductViewHolder(View itemView){
            super(itemView);

            prodName = (TextView) itemView.findViewById(R.id.purchase_prodname);
//            timeStamp = (TextView) itemView.findViewById(R.id.Single_Item_Chat_TimeStamp);
//            userImage = (ImageView) itemView.findViewById(R.id.Single_Item_Chat_ImageView);
        }

        public void populate(Product product){
            prodName.setText(product.getProd_name());
//            userImage.setText(chatWrapper.getTimestamp());
        }
    }

    public class ServiceViewHolder extends RecyclerView.ViewHolder {
//        TextView dataTitle;
//        TextView dataLink;
//        TextView dataSnippet;
//        ImageView image;
//        ImageButton dataSendButton;
        TextView serviceName;

        public ServiceViewHolder(View itemView){
            super(itemView);

            serviceName = (TextView) itemView.findViewById(R.id.purchase_prodname);
//            dataLink = (TextView) itemView.findViewById(R.id.Image_data_Link);
//            dataSnippet = (TextView) itemView.findViewById(R.id.Image_data_Snippet);
//            image = (ImageView) itemView.findViewById(R.id.Image_data_Image);
//            dataSendButton = (ImageButton) itemView.findViewById(R.id.Image_data_SendButton);
        }

        public void populate(Services services){
            serviceName.setText(services.getService_name());
//            dataTitle.setText(imageDataWrapper.getPage_Title());
//            dataLink.setText(imageDataWrapper.getPage_Link());
//            dataSnippet.setText(imageDataWrapper.getPage_Desc());
//            Picasso.with(context).load(imageDataWrapper.getPage_ImageThumb()).into(image);
        }
    }
}
