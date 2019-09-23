package com.example.devcash.CustomAdapters;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.Object.CustomerTransaction;
import com.example.devcash.Object.CustomerTransactionlistdata;
import com.example.devcash.R;
import com.example.devcash.ViewReceiptActivity;

import org.w3c.dom.Text;

import java.util.List;

public class AllReceiptsAdapter extends RecyclerView.Adapter<AllReceiptsAdapter.ViewHolder> {

    List<CustomerTransactionlistdata> list;

    public AllReceiptsAdapter(List<CustomerTransactionlistdata> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customcard_allreceipt, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int receiptNo = list.get(viewHolder.getAdapterPosition()).getCustomer_id();
                String dateTime = list.get(viewHolder.getAdapterPosition()).getTransaction_datetime();
                double amountDue = list.get(viewHolder.getAdapterPosition()).getAmount_due();

                Intent intent = new Intent(v.getContext(), ViewReceiptActivity.class);
                intent.putExtra("customer_id", receiptNo);
                intent.putExtra("date_time", dateTime);
                intent.putExtra("amount_due", amountDue);


                Bundle b = new Bundle();
//                b.putInt("customer_id", receiptNo);
//                intent.putExtras(b);
                v.getContext().startActivity(intent);

//                Toast.makeText(v.getContext(), receiptNo+" from the adapter", Toast.LENGTH_SHORT).show();

            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        CustomerTransactionlistdata transaction = list.get(i);

        viewHolder.amountDue.setText(String.valueOf(transaction.getAmount_due()));
        viewHolder.dateTime.setText(transaction.getTransaction_datetime());
        viewHolder.receiptno.setText("Receipt# "+(transaction.getCustomer_id()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView amountDue, receiptno, dateTime;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            amountDue = (TextView) itemView.findViewById(R.id.receiptAmountDue);
            dateTime = (TextView) itemView.findViewById(R.id.receiptDate);
            receiptno = (TextView) itemView.findViewById(R.id.reciptnumber);
            layout = (LinearLayout) itemView.findViewById(R.id.receiptLayout);
        }
    }
}
