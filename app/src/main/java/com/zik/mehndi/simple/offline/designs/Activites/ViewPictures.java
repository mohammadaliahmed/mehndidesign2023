package com.zik.mehndi.simple.offline.designs.Activites;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.zik.mehndi.simple.offline.designs.Adapters.ImagePagerAdapter;
import com.zik.mehndi.simple.offline.designs.Models.CategoryModel;
import com.zik.mehndi.simple.offline.designs.R;
import com.zik.mehndi.simple.offline.designs.Utils.CommonUtils;
import com.zik.mehndi.simple.offline.designs.Utils.SharedPrefs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewPictures extends AppCompatActivity {


    ViewPager viewPager;
    int position;
    List<Integer> imgsList = new ArrayList<>();
    ImageView share,like,next;
    HashMap<Integer,Integer> favMap;
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pictures);
        getSupportActionBar().hide();
        adView=findViewById(R.id.adView);
        if(!SharedPrefs.getPremium().equalsIgnoreCase("1")) {
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }
        viewPager = findViewById(R.id.viewpager);
        imgsList = getIntent().getIntegerArrayListExtra("imgs");
        share = findViewById(R.id.share);
        like = findViewById(R.id.like);
        next = findViewById(R.id.next);
        position = getIntent().getIntExtra("position", 0);
        ImagePagerAdapter adapter = new ImagePagerAdapter(this, imgsList);
        favMap= SharedPrefs.getLikedMap();
        if(favMap==null){
            favMap=new HashMap<>();
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = position + 1;
                if (position >= imgsList.size() - 1) {
                    position = 0;
                }
                viewPager.setCurrentItem(position);
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.showToast(imgsList.get(position)+"");
            }
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favMap.containsKey(imgsList.get(position))){
                    Glide.with(ViewPictures.this).load(R.drawable.ic_heart).into(like);
                    favMap.remove(imgsList.get(position));
                    CommonUtils.showToast("Design removed from favorites");
                }else{
                    favMap.put(imgsList.get(position),imgsList.get(position));
                    Glide.with(ViewPictures.this).load(R.drawable.heart_filled).into(like);
                    CommonUtils.showToast("Design added to favorites");

                }
                SharedPrefs.setLikedMap(favMap);
            }
        });

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int pos) {
                position=pos;
                if(favMap.containsKey(imgsList.get(position))){
                    Glide.with(ViewPictures.this).load(R.drawable.heart_filled).into(like);
                }else{
                    Glide.with(ViewPictures.this).load(R.drawable.ic_heart).into(like);

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


}
