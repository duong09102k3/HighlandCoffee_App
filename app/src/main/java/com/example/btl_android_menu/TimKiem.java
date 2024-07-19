package com.example.btl_android_menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import com.example.btl_android_menu.Adapter.CuaHangAdapter;
import com.example.btl_android_menu.Adapter.SanPhamAdapter;
import com.example.btl_android_menu.Model.CuaHangModel;
import com.example.btl_android_menu.Model.SanPhamModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class TimKiem extends AppCompatActivity  {
    private ImageView icBackTK;
    RecyclerView recyclerView;
    SanPhamAdapter sanPhamAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tim_kiem);
        getViews();
        Toolbar toolbar = findViewById(R.id.your_toolbar_id); // Replace with your actual toolbar ID
        setSupportActionBar(toolbar);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SanPhamAdapter.OnItemSPClickListener spClickListener = new SanPhamAdapter.OnItemSPClickListener() {
            @Override
            public void onItemClick(String idSP) {
                Intent intent = new Intent(TimKiem.this, ChiTietSP.class);
                intent.putExtra("idSP", idSP);
                startActivity(intent);
            }
        };


        FirebaseRecyclerOptions<SanPhamModel> options =
                new FirebaseRecyclerOptions.Builder<SanPhamModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("sanpham"), SanPhamModel.class)
                        .build();
        sanPhamAdapter = new SanPhamAdapter(options, spClickListener);
        recyclerView.setAdapter(sanPhamAdapter);

        icBackTK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void getViews(){

        icBackTK = findViewById(R.id.icBackTK);
        recyclerView = findViewById(R.id.rvSanPhamTC);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            // If there are fragments, pop the back stack to go back to the previous fragment
            getSupportFragmentManager().popBackStack();
        } else {
            // If there are no fragments in the back stack, finish the activity to go back to the previous activity
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        sanPhamAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        sanPhamAdapter.stopListening();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.search) {
            // Handle the search item click
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView =(SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("Search", "onQueryTextSubmit: " + query);
                txtSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                Log.d("Search", "onQueryTextSubmit: " + query);
                txtSearch(query);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    private  void txtSearch(String str){
        SanPhamAdapter.OnItemSPClickListener spClickListener = new SanPhamAdapter.OnItemSPClickListener() {
            @Override
            public void onItemClick(String idSP) {
                Intent intent = new Intent(TimKiem.this, ChiTietSP.class);
                intent.putExtra("idSP", idSP);
                startActivity(intent);
            }
        };

        FirebaseRecyclerOptions<SanPhamModel> options =
                new FirebaseRecyclerOptions.Builder<SanPhamModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("sanpham").orderByChild("nameSP").startAt(str).endAt(str+"\uf8ff"), SanPhamModel.class)
                        .build();

        sanPhamAdapter = new SanPhamAdapter(options,spClickListener);
        sanPhamAdapter.startListening();
        recyclerView.setAdapter(sanPhamAdapter);


    }





}