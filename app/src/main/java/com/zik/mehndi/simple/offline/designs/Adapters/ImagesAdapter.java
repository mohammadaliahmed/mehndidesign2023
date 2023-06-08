package com.zik.mehndi.simple.offline.designs.Adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.zik.mehndi.simple.offline.designs.Models.CategoryModel;
import com.zik.mehndi.simple.offline.designs.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.Viewholder> {
    Context context;
    List<Integer> itemList;
    ImagesAdapterCallback callback;
    boolean favorites;
    public ImagesAdapter(Context context, List<Integer> itemList,boolean favorites,ImagesAdapterCallback callback) {
        this.callback = callback;
        this.favorites = favorites;
        this.context = context;
        this.itemList = itemList;
    }
    public void setItemList(List<Integer> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ImagesAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Viewholder viewHolder;
            View view = LayoutInflater.from(context).inflate(R.layout.img_item_layout, parent, false);
            viewHolder = new Viewholder(view);

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull ImagesAdapter.Viewholder holder, int position) {
        Integer image = itemList.get(position);
        Glide.with(context).load(image).into(holder.image);

        holder.heartImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isLiked = (boolean) view.getTag();
                if (!isLiked) {
                    holder.heartImageView.setImageResource(R.drawable.heart_filled);
                    view.setTag(true);
                    callback.OnLikedUnliked(image,true);
                } else {
                    holder.heartImageView.setImageResource(R.drawable.ic_heart);
                    view.setTag(false);
                    callback.OnLikedUnliked(image,false);

                }
            }
        });
        if(favorites) {
            holder.heartImageView.setImageResource(R.drawable.heart_filled);
            holder.heartImageView.setTag(true);
        }else{
            holder.heartImageView.setImageResource(R.drawable.ic_heart);
            holder.heartImageView.setTag(false);
        }



    }
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView image,heartImageView;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            heartImageView=itemView.findViewById(R.id.heartImageView);
            image=itemView.findViewById(R.id.image);

        }

    }
    public interface ImagesAdapterCallback{
        public void OnLikedUnliked(Integer img,boolean like);
    }
}
