package com.amzn.mehndi.simple.offline.designs.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.amzn.mehndi.simple.offline.designs.R;

import java.util.List;

public class ImagePagerAdapter extends PagerAdapter {
    List<Integer> itemList;
    Context context;
    ImageView image;
    public ImagePagerAdapter(Context context, List<Integer> itemList) {
        this.context=context;
        this.itemList=itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.view_pager_item, container, false);
        image = itemView.findViewById(R.id.image);

        Glide.with(context).load(itemList.get(position)).into(image);

        container.addView(itemView);


        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        try{
            container.removeView((ImageView) object);

        }catch (Exception e){

        }
    }


}