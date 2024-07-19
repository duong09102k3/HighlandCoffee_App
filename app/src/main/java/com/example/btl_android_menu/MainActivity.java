package com.example.btl_android_menu;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.btl_android_menu.Model.PreferenceUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        bottomNavigationView
                = findViewById(R.id.bottomNavigationView);
        ColorStateList iconColors = getResources().getColorStateList(R.color.bottom_nav_icon_colors);

// Thiết lập màu cho icon của các MenuItem trong BottomNavigationView
        bottomNavigationView.setItemIconTintList(iconColors);
        bottomNavigationView.setItemTextColor(iconColors);


// Thiết lập lắng nghe sự kiện khi chọn menu item
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        Bundle extras = getIntent().getExtras();
        if (getIntent().hasExtra("key")) {
            String data = extras.getString("key");
            chuyenFragment(data);

        }else if (getIntent().hasExtra("fragment")) {
            String data = extras.getString("fragment");
            chuyenFragment(data);

        }else if (getIntent().hasExtra("fragmentHS")) {
            String data = extras.getString("fragmentHS");
            chuyenFragment(data);

        }else if (getIntent().hasExtra("SPMain")) {
            String data = extras.getString("SPMain");
            chuyenFragment(data);

        }else{
            // Replace fragment mặc định khi khởi động
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flFragment, new TrangChu())
                    .commit();
        }
//        if (getIntent().hasExtra("fragment_to_show")) {
//            // Lấy thông tin đánh dấu
//            String fragmentToShow = getIntent().getStringExtra("fragment_to_show");
//
//            // Nếu là DatHang, hiển thị Fragment DatHang
//            if (fragmentToShow.equals("dat_hang")) {
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.flFragment, new DatHang())
//                        .commit();
//                bottomNavigationView.setSelectedItemId(R.id.ic_dathang);
//            }
//        }  else {
//            // Nếu không có thông tin đánh dấu, hiển thị Fragment TrangChu
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.flFragment, new TrangChu())
//                    .commit();
//            bottomNavigationView.setSelectedItemId(R.id.ic_trangchu);
//        }
//
//        if(getIntent().hasExtra("fragment_to_show")){
//            String fragmentToShow = getIntent().getStringExtra("fragment_to_show");
//            //nếu là giỏ hàng hiển thị fragment giỏ hàng
//            if(fragmentToShow.equals("gio_hang")){
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.flFragment, new GioHang())
//                        .commit();
//                bottomNavigationView.setSelectedItemId(R.id.ic_giohang);
//            } else{
//                //nếu k có thông tin đánh dấu hiện thì fragment trang chủ
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.flFragment, new TrangChu())
//                        .commit();
//                bottomNavigationView.setSelectedItemId(R.id.ic_trangchu);
//            }
//        }



    }

    private void chuyenFragment(String TenFragment){
        if (TenFragment.toLowerCase(Locale.ROOT).equals("dathang")) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, datHang)
                    .commit();
            bottomNavigationView.setSelectedItemId(R.id.ic_dathang);
        } else if (TenFragment.toLowerCase(Locale.ROOT).equals("lichsu")) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, lichSu)
                    .commit();
            bottomNavigationView.setSelectedItemId(R.id.ic_history);
        } else if (TenFragment.toLowerCase(Locale.ROOT).equals("cuahang")) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, cuaHang)
                    .commit();
            bottomNavigationView.setSelectedItemId(R.id.ic_cuahang);
        } else if (TenFragment.toLowerCase(Locale.ROOT).equals("giohang")) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, gioHang)
                    .commit();
            bottomNavigationView.setSelectedItemId(R.id.ic_giohang);
        }
    }
    TrangChu trangChu = new TrangChu();

    DatHang datHang = new DatHang();
    LichSu lichSu = new LichSu();
    CuaHang cuaHang = new CuaHang();
    GioHang gioHang = new GioHang();



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();


        if (itemId == R.id.ic_trangchu) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, trangChu)
                    .commit();
            return true;
        } else if (itemId == R.id.ic_dathang) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, datHang)
                    .commit();
            return true;
        } else if (itemId == R.id.ic_history) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, lichSu)
                    .commit();
            return true;
        } else if (itemId == R.id.ic_cuahang) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, cuaHang)
                    .commit();
            return true;
        } else if (itemId == R.id.ic_giohang) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, gioHang)
                    .commit();
            return true;
        }



        return false;
    }

}