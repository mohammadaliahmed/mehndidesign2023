package com.amzn.mehndi.simple.offline.designs.Activites;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.amzn.mehndi.simple.offline.designs.Adapters.ImagePagerAdapter;
import com.amzn.mehndi.simple.offline.designs.R;
import com.amzn.mehndi.simple.offline.designs.Utils.CommonUtils;
import com.amzn.mehndi.simple.offline.designs.Utils.SharedPrefs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    private InterstitialAd interstitialAd;
    private AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pictures);
        getSupportActionBar().hide();
        adView=findViewById(R.id.adView);
        if(!SharedPrefs.getPremium().equalsIgnoreCase("1")) {
            adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
            LoadInterstritial();
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
                shareDrawableToWhatsApp(imgsList.get(position));
                LoadInterstritial();
            }
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadInterstritial();
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
    private Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }
        return bitmap;
    }
    private void shareDrawableToWhatsApp(int drawableResId) {
        try {
            // Get the Drawable object from the resource ID
            Drawable drawable = ContextCompat.getDrawable(this, drawableResId);

            // Convert the drawable to a bitmap
            Bitmap bitmap = drawableToBitmap(drawable);

            // Get the URI from the bitmap
            Uri imageUri = getImageUri(bitmap);


            String text = getResources().getString(com.bogdwellers.pinchtozoom.R.string.app_name)
                    +"\n\nDownload the app from play store: " + "https://play.google.com/store/apps/details?id=" + getPackageName();

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("image/*");
            waIntent.putExtra(Intent.EXTRA_TEXT, text);

            waIntent.putExtra(android.content.Intent.EXTRA_STREAM, imageUri);
            startActivity(Intent.createChooser(waIntent, "Share with"));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Uri getImageUri(Bitmap bitmap) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Image", null);
        return Uri.parse(path);
    }
    public void LoadInterstritial() {

        InterstitialAd.load(
                this,
                getResources().getString(R.string.interstital_ad_unit_id),
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd aa) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        interstitialAd = aa;
                        showInterstitial();
//
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        interstitialAd = null;
                                        Log.d("TAG", "The ad was dismissed.");
//                                        LoadInterstritial();
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        interstitialAd = null;
                                        Log.d("TAG", "The ad failed to show.");
//                                        LoadInterstritial();
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        Log.d("TAG", "The ad was shown.");
//                                        LoadInterstritial();
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        interstitialAd = null;
                        LoadInterstritial();

                    }
                });


    }

    private void showInterstitial() {
        CommonUtils.showToast("Loading Ad..");
        // Show the ad if it's ready. Otherwise toast and restart the game.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (interstitialAd != null) {
                    interstitialAd.show(ViewPictures.this);
                } else {
                    LoadInterstritial();
                }
            }
        }, 1500);
    }

}
