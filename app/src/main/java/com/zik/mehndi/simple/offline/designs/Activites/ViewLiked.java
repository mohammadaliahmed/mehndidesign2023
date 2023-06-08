package com.zik.mehndi.simple.offline.designs.Activites;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zik.mehndi.simple.offline.designs.Adapters.ImagesAdapter;
import com.zik.mehndi.simple.offline.designs.Models.CategoryModel;
import com.zik.mehndi.simple.offline.designs.R;
import com.zik.mehndi.simple.offline.designs.Utils.SharedPrefs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ViewLiked extends AppCompatActivity {

    RecyclerView recycler;
    ImagesAdapter adapter;
    private List<Integer> imgsList = new ArrayList<>();
    private HashMap<Integer, Integer> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_category);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);

        }
        this.setTitle("Favorites");

        recycler = findViewById(R.id.recycler);
        map = SharedPrefs.getLikedMap();
        if (map == null) {
            map = new HashMap<>();
        }
        imgsList = new ArrayList<>(map.values());
        adapter = new ImagesAdapter(this, imgsList,true, new ImagesAdapter.ImagesAdapterCallback() {
            @Override
            public void OnLikedUnliked(Integer img, boolean like) {

                map.remove(img);
                SharedPrefs.setLikedMap(map);
                imgsList = new ArrayList<>(map.values());
                adapter.setItemList(imgsList);
            }

        });
        recycler.setLayoutManager(new GridLayoutManager(this, 2));
        recycler.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}