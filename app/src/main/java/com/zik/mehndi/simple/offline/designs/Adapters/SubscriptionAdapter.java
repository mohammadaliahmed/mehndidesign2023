package com.zik.mehndi.simple.offline.designs.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.ProductDetails;
import com.zik.mehndi.simple.offline.designs.R;

import java.util.List;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.Viewholder> {
    Context context;
    List<ProductDetails> productDetailsList;
    SubscriptionAdapterCallback callback;
    public SubscriptionAdapter(Context context, List<ProductDetails> productDetailsList,SubscriptionAdapterCallback callback) {

        this.context = context;
        this.productDetailsList = productDetailsList;
        this.callback = callback;
    }

    public void setProductDetailsList(List<ProductDetails> productDetailsList) {
        this.productDetailsList = productDetailsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SubscriptionAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Viewholder viewHolder;
        View view = LayoutInflater.from(context).inflate(R.layout.subscription_item_layout, parent, false);
        viewHolder = new Viewholder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SubscriptionAdapter.Viewholder holder, int position) {
        ProductDetails details = productDetailsList.get(position);

        if (details.getProductId().equals("lifetime")) {
            holder.description.setText(details.getOneTimePurchaseOfferDetails().getFormattedPrice()+" for life");
            holder.title.setText("Lifetime");

        } else {
            String end="";
            if(details.getProductId().equals("weekly")){
                end="/week";
                holder.title.setText("Weekly");
            }else if(details.getProductId().equals("monthly")){
                end="/month";
                holder.title.setText("Monthly");
            }else if(details.getProductId().equals("yearly")){
                end="/year";
                holder.title.setText("Yearly");
            }
            holder.description.setText(details.getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList().get(0).getFormattedPrice()+end);

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.OnPurchase(details);
            }
        });


    }

    @Override
    public int getItemCount() {
        return productDetailsList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView title, description;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);

        }
    }

    public interface SubscriptionAdapterCallback{
        public void OnPurchase(ProductDetails details);
    }
}
