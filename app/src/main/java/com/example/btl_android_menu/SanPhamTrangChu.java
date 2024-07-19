package com.example.btl_android_menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.btl_android_menu.Adapter.LoaiAdapter;
import com.example.btl_android_menu.Adapter.SanPhamAdapter;
import com.example.btl_android_menu.Model.LoaiModel;
import com.example.btl_android_menu.Model.SanPhamModel;
import com.example.btl_android_menu.Model.SelectedStore;
import com.example.btl_android_menu.Utils.DsSanPhamMuaUtil;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SanPhamTrangChu extends AppCompatActivity implements SanPhamAdapter.OnItemSPClickListener {
    ImageView btnHuyTC, btnTimKiem;
    TextView txtNameTC, txtNameCH;
    RecyclerView recyclerSP;
    LinearLayout llCuaHang;
    TextView tvsoLuongSPM;
    ImageView gioHang;
    SanPhamAdapter sanPhamAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_san_pham_trang_chu);
        getView();





        //27/03
        String tenCuaHang = SelectedStore.getInstance().getStoreName();

        // Kiểm tra xem tên cửa hàng có tồn tại không
        if (tenCuaHang != null && !tenCuaHang.isEmpty()) {
            // Thiết lập tên cửa hàng cho TextView txtCuaHang
            txtNameCH.setText(tenCuaHang);
        } else {
            txtNameCH.setText(" ");
        }

        onResume();
        String slMua = String.valueOf(DsSanPhamMuaUtil.getInstance().getSoLuongSP());
        tvsoLuongSPM.setText(slMua);



        gioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tenCuaHang != null && !tenCuaHang.isEmpty()){
                    Intent intent = new Intent(SanPhamTrangChu.this, ActyvityGioHang.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(SanPhamTrangChu.this, MainActivity.class);
                    intent.putExtra("SPMain", "cuahang");
                    startActivity(intent);
                }


            }
        });

        //27/03
        llCuaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SanPhamTrangChu.this, MainActivity.class);
                intent.putExtra("SPMain", "cuahang");
                startActivity(intent);
            }
        });




        Intent intent = getIntent();
        if (intent != null) {
            String idLoai = intent.getStringExtra("idLoai");
            updateChiTietSP(idLoai);
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        String slMua = String.valueOf(DsSanPhamMuaUtil.getInstance().getSoLuongSP());
        tvsoLuongSPM.setText(slMua);

    }

    private void updateChiTietSP(String idLoai){
        Query query = FirebaseDatabase.getInstance().getReference().child("sanpham").orderByChild("idLoai").equalTo(idLoai);
        FirebaseRecyclerOptions<SanPhamModel> spOptions =
                new FirebaseRecyclerOptions.Builder<SanPhamModel>()
                        .setQuery(query, SanPhamModel.class)
                        .build();

        SanPhamAdapter.OnItemSPClickListener spClickListener = new SanPhamAdapter.OnItemSPClickListener() {
            @Override
            public void onItemClick(String idSP) {
                Intent intent = new Intent(SanPhamTrangChu.this, ChiTietSP.class);
                intent.putExtra("idSP", idSP);
                startActivity(intent);
            }
        };

        sanPhamAdapter = new SanPhamAdapter(spOptions, spClickListener);
        sanPhamAdapter.startListening();
        LinearLayoutManager layoutManagerSP = new LinearLayoutManager(this);
        recyclerSP.setLayoutManager(layoutManagerSP);
        recyclerSP.setAdapter(sanPhamAdapter);

        DatabaseReference loaiRef = FirebaseDatabase.getInstance().getReference().child("loai").child(idLoai);
        loaiRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    LoaiModel loaiModel = dataSnapshot.getValue(LoaiModel.class);
                    if (loaiModel != null) {
                        String nameLoai = loaiModel.getNameLoai();
                        txtNameTC.setText(nameLoai);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        btnTimKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SanPhamTrangChu.this, TimKiem.class);
                startActivity(intent);
            }
        });

        btnHuyTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


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
    private void getView(){
        btnHuyTC = findViewById(R.id.btnHuyTC);
        btnTimKiem = findViewById(R.id.btnTimKiemTC);
        txtNameTC = findViewById(R.id.txtNameTC);
        recyclerSP = findViewById(R.id.rvSanPhamTChu);
        txtNameCH = findViewById(R.id.txtNameCH);
        llCuaHang = findViewById(R.id.llCuaHang);
        tvsoLuongSPM = findViewById(R.id.tvSoLuongSP);
        gioHang = findViewById(R.id.imageViewGioHang);

    }

    @Override
    public void onItemClick(String idSP) {
        Intent intent = new Intent(this, ChiTietSP.class);
        intent.putExtra("idSP", idSP);
        startActivity(intent);
    }
}