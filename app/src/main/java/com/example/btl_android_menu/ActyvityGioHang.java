package com.example.btl_android_menu;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btl_android_menu.Adapter.HD_SanPhamMuaAdapter;
import com.example.btl_android_menu.Model.CuaHangModel;
import com.example.btl_android_menu.Model.HoaDonModel;
import com.example.btl_android_menu.Model.SanPhamMuaModel;
import com.example.btl_android_menu.Model.SelectedStore;
import com.example.btl_android_menu.Utils.DsSanPhamMuaUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActyvityGioHang extends AppCompatActivity {
    RecyclerView rcvDsSPMua;
    ImageView btnTroLai;
    private String userId;
    HD_SanPhamMuaAdapter adapter;

    TextView txtXoaGioHang, txtThaiDoiCH,txtGioHangTenCH, txtGhDiaChiCH1,txtGhDiaChiCH2,txtGhiChu;
    TextView txtThemSP,txtGiaTT,txtGh_DatHang,txtTongCong;
    ArrayList<SanPhamMuaModel> DssanPhamMua;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actyvity_gio_hang);

        getView();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = currentUser.getUid();

        CuaHangModel cuaHangModel= SelectedStore.getInstance().getStore();
        if(cuaHangModel.getNameCH() != ""){
            txtGioHangTenCH.setText(cuaHangModel.getNameCH());
            txtGhDiaChiCH1.setText(cuaHangModel.getDiachi());
            txtGhDiaChiCH2.setText(cuaHangModel.getLocation());
        }


        onResume();
        UpLoadDsSanPhamMua();

        txtXoaGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DsSanPhamMuaUtil.getInstance().setDsSanPhamMuaUtil(new ArrayList<SanPhamMuaModel>());
                UpLoadDsSanPhamMua();
            }
        });

        txtGh_DatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DsSanPhamMuaUtil.getInstance().getDsSanPhamMuaUtil().size()>0){
                    UploadDuLieu();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    // Truyền dữ liệu (nếu cần)
                    intent.putExtra("key", "lichsu");
                    // Chuyển đến Activity mới
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "Bạn chưa chọn sản phẩm cần mua", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txtThaiDoiCH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                // Truyền dữ liệu (nếu cần)
                intent.putExtra("key", "cuahang");
                // Chuyển đến Activity mới
                startActivity(intent);
            }
        });


        txtThemSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getApplicationContext(), DatHang.class);
                startActivity(intent);
            }
        });
        btnTroLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        txtThemSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                // Truyền dữ liệu (nếu cần)
                intent.putExtra("key", "DatHang");
                // Chuyển đến Activity mới
                startActivity(intent);

            }
        });
    }
    private  void UploadDuLieu(){
        Calendar calendar = Calendar.getInstance();
        // Lấy ngày hiện tại
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        // Lấy tháng hiện tại (lưu ý: tháng trong Java bắt đầu từ 0)
        int month = calendar.get(Calendar.MONTH) + 1;
        // Lấy năm hiện tại
        int year = calendar.get(Calendar.YEAR);
        //Lấy thời gian
        int hour = calendar.get(Calendar.HOUR_OF_DAY); // Lấy giờ theo định dạng 24h
        int minute = calendar.get(Calendar.MINUTE);

        // Khởi tạo Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Tham chiếu đến nút mà bạn muốn ghi dữ liệu vào
        DatabaseReference myRef = database.getReference("hoadon");

        // Tạo một đối tượng chứa dữ liệu bạn muốn ghi
        // Ví dụ, một đối tượng User
        HoaDonModel hoadon= new HoaDonModel();
        hoadon.setGioL(hour+":"+minute);
        hoadon.setNgayL(String.format("%d/%d/%d", day, month, year));
        hoadon.setMaKh(userId);
        hoadon.setTrangThaiD(1);
        String sTongTien = txtTongCong.getText().toString().replaceAll("[^0-9]", "");
        hoadon.setTongTien(Integer.parseInt(sTongTien));
        List<SanPhamMuaModel> listSanPhamMua = new ArrayList<>(DssanPhamMua);
        hoadon.setSanPhamMua(listSanPhamMua);
        hoadon.setNameCH(txtGioHangTenCH.getText().toString());
        hoadon.setDiachiCH(txtGhDiaChiCH1.getText().toString());
        hoadon.setLocation(txtGhDiaChiCH2.getText().toString());


        // Ghi dữ liệu lên Firebase Realtime Database
        myRef.push().setValue(hoadon)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Ghi dữ liệu thành công
                        DsSanPhamMuaUtil.getInstance().setDsSanPhamMuaUtil(new ArrayList<SanPhamMuaModel>());
                        UpLoadDsSanPhamMua();
                        Log.d(TAG, "Đặt hàng thành công.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Ghi dữ liệu thất bại
                        Log.w(TAG, "Error writing data to Firebase.", e);
                    }
                });
    }
    private  void UpLoadDsSanPhamMua(){
        rcvDsSPMua.setLayoutManager(new LinearLayoutManager(this));
        DssanPhamMua= DsSanPhamMuaUtil.getInstance().getDsSanPhamMuaUtil();
        // Tạo và thiết lập adapter cho RecyclerView
        adapter = new HD_SanPhamMuaAdapter(this.getLayoutInflater(), DssanPhamMua, new HD_SanPhamMuaAdapter.OnItemSanPhamMClickListener() {
            @Override
            public void onItemClick(String idSP,Integer stt) {
                Intent intent = new Intent(getApplicationContext(), ChiTietSP.class);
                intent.putExtra("idSP", idSP);
                intent.putExtra("STT",stt);
                startActivity(intent);
            }
        });
        int TongGia=0;
        for (SanPhamMuaModel sp : DssanPhamMua){
            TongGia =TongGia + sp.getGia();
        }
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedGiaSP = decimalFormat.format(TongGia);
        txtGiaTT.setText(String.format("%s đ", formattedGiaSP));
        txtTongCong.setText(String.format("%s đ", formattedGiaSP));
        txtGh_DatHang.setText(String.format("Đặt Hàng (%s đ)", formattedGiaSP));
        rcvDsSPMua.setAdapter(adapter);
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
        txtXoaGioHang = findViewById(R.id.tvXoaGioHang);
        txtThaiDoiCH = findViewById(R.id.tvThaiDoiCH);
        txtGioHangTenCH = findViewById(R.id.tvGioHangTenCH);
        txtGhDiaChiCH1 = findViewById(R.id.tvGhDiaChiCH1);
        txtGhDiaChiCH2 = findViewById(R.id.tvGhDiaChiCH2);
        txtGhiChu = findViewById(R.id.edGhiChuHD);
        txtThemSP = findViewById(R.id.tvThemSP);
        txtGiaTT =findViewById(R.id.tvGiaTT);
        txtGh_DatHang = findViewById(R.id.tvGh_DatHang);
        txtTongCong = findViewById(R.id.tvGh_TongCong);
        rcvDsSPMua = findViewById(R.id.rcvDsSPMua);
        btnTroLai =findViewById(R.id.icBackGH);
    }
    public void onResume() {
        super.onResume();
        UpLoadDsSanPhamMua();
    }
}