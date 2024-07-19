package com.example.btl_android_menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.btl_android_menu.Adapter.HD_SanPhamMuaAdapter;
import com.example.btl_android_menu.Model.HoaDonModel;
import com.example.btl_android_menu.Model.SanPhamModel;
import com.example.btl_android_menu.Model.SanPhamMuaModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Context;

import java.security.AccessController;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ChiTietHD extends AppCompatActivity {
    TextView maHD,thoiGianL,tenCH,diaChiCH1,diaChiCH2,TinhTam,GiaTongT,GiaTTT;
    ImageView btnTroLai;
    RecyclerView rcvChiTietHD;
    HD_SanPhamMuaAdapter adapter;

    ArrayList<SanPhamMuaModel> arrSanPhamM = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_hd);
        getView();

        Intent intent = getIntent();
        if (intent != null) {
            String idHD = intent.getStringExtra("idHD");
            loatChiTietHD(idHD);

        }
        btnTroLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    @SuppressLint("WrongViewCast")
    private void getView(){
        maHD = findViewById(R.id.tvChiTietHD_MaHD);
        thoiGianL = findViewById(R.id.tvChiTietHD_ThoiGianL);
        tenCH = findViewById(R.id.tvChiTietHD_TenCH);
        diaChiCH1 = findViewById(R.id.tvChiTietHD_DiaChiCH1);
        diaChiCH2 = findViewById(R.id.tvChiTietHD_DiaChiCH2);
        TinhTam= findViewById(R.id.tvChiTietHD_TamTinh);
        GiaTongT = findViewById(R.id.tvChiTietHD_GiaTongT);
        GiaTTT= findViewById(R.id.tvChiTietHD_GiaTTT);
        btnTroLai = findViewById(R.id.btnTroLai_gh);
        rcvChiTietHD =findViewById(R.id.rcvSanPhamMua);
    }

    private  void loatChiTietHD(String idHD){
        ChiTietHD nhap = this;

        DatabaseReference SPRef = FirebaseDatabase.getInstance().getReference().child("hoadon").child(idHD);
        SPRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    HoaDonModel hoaDonModel = dataSnapshot.getValue(HoaDonModel.class);
                    if (hoaDonModel != null) {
                        maHD.setText(hoaDonModel.getMaHd());
                        thoiGianL.setText(String.format("%s %s", hoaDonModel.getNgayL(), hoaDonModel.getGioL()));
                        tenCH.setText(hoaDonModel.getNameCH());
                        diaChiCH1.setText(hoaDonModel.getDiachiCH());
                        diaChiCH2.setText(hoaDonModel.getLocation());
                        DecimalFormat decimalFormat = new DecimalFormat("#,###");
                        float giaSP = hoaDonModel.getTongTien();
                        String formattedGiaSP = decimalFormat.format(giaSP);
                        TinhTam.setText(String.format("%s đ", formattedGiaSP));
                        GiaTongT.setText(String.format("%s đ", formattedGiaSP));
                        GiaTTT.setText(String.format("%s đ", formattedGiaSP));
                        arrSanPhamM = (ArrayList<SanPhamMuaModel>) hoaDonModel.getSanPhamMua();

                        rcvChiTietHD.setLayoutManager(new LinearLayoutManager(nhap));
                        // Tạo và thiết lập adapter cho RecyclerView
                        adapter = new HD_SanPhamMuaAdapter(nhap.getLayoutInflater(), arrSanPhamM, new HD_SanPhamMuaAdapter.OnItemSanPhamMClickListener() {
                            @Override
                            public void onItemClick(String idHD,Integer stt) {

                            }
                        });
                        rcvChiTietHD.setAdapter(adapter);
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

}