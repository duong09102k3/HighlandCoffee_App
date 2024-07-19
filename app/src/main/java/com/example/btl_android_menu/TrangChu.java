package com.example.btl_android_menu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.btl_android_menu.Adapter.BannerAdapter;
import com.example.btl_android_menu.Adapter.CuaHangAdapter;
import com.example.btl_android_menu.Adapter.LoaiAdapter;
import com.example.btl_android_menu.Adapter.SanPhamAdapter;
import com.example.btl_android_menu.Adapter.SanPhamBestSeller;
import com.example.btl_android_menu.Model.CuaHangModel;
import com.example.btl_android_menu.Model.LoaiModel;
import com.example.btl_android_menu.Model.SanPhamModel;
import com.example.btl_android_menu.Model.SelectedStore;
import com.example.btl_android_menu.Utils.DsSanPhamMuaUtil;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
// * Use the {@link TrangChu} factory method to
 * create an instance of this fragment.
 */
public class TrangChu extends Fragment  {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String idLoai;
    private ImageView icTimKiemTC, icHoSoTC;
    TextView txtCuaHang;
    LinearLayout btnCH;
    BottomNavigationView bottomNavigationView;
    TextView tvsoLuongSPM;
    RecyclerView recyclerView, rvSPBestSeller;
    LoaiAdapter loaiAdapter;
    ImageView gioHang;
    SanPhamBestSeller sanPhamBestSeller;

    private ViewPager viewPager;
    private List<String> mImageUrls;
    private int currentPage = 0;
    private Timer timer;
    private final long DELAY_MS = 0; // Thời gian trễ trước khi bắt đầu chạy auto scroll
    private final long PERIOD_MS = 5000;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    public TrangChu() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TrangChu newInstance(String param1, String param2) {
        TrangChu fragment = new TrangChu();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trang_chu, container, false);

        //Chạy Banner
        viewPager = view.findViewById(R.id.viewBanner);
        mImageUrls = new ArrayList<>();

        loadImagesFromFirebaseStorage();// Tải danh sách hình ảnh từ Firebase Storage

        BannerAdapter adapter = new BannerAdapter(getActivity(), mImageUrls); // Khởi tạo adapter và gán vào ViewPager
        viewPager.setAdapter(adapter);

        // Tự động lướt qua các banner mỗi 5 giây nè
        final Handler handler = new Handler();
        final Runnable update = () -> {
            if (currentPage == mImageUrls.size()) {
                currentPage = 0;
            }
            viewPager.setCurrentItem(currentPage++, true);
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, DELAY_MS, PERIOD_MS);



        //Số lượng giỏ hàng
        tvsoLuongSPM = view.findViewById(R.id.tvSoLuongMua);
        onResume();
        String slMua = String.valueOf(DsSanPhamMuaUtil.getInstance().getSoLuongSP());
        tvsoLuongSPM.setText(slMua);

        //Chuyển trang CuaHang Fragment okela
        btnCH = view.findViewById(R.id.btnCHang);
        btnCH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //27/03
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("SPMain", "cuahang");
                startActivity(intent);
            }
        });


        //Hiện tên Cuahang vừa chọn nhé
        txtCuaHang = view.findViewById(R.id.txtNameCH);
        String tenCuaHang = SelectedStore.getInstance().getStoreName();

        // Kiểm tra xem tên cửa hàng có tồn tại không
        if (tenCuaHang != null && !tenCuaHang.isEmpty()) {
            // Thiết lập tên cửa hàng cho TextView txtCuaHang
            txtCuaHang.setText(tenCuaHang);
        } else {
            txtCuaHang.setText("");
        }

        // Ấn chuyển sang giỏ hàng
        gioHang =view.findViewById(R.id.imageViewGioHang);
        gioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tenCuaHang != null && !tenCuaHang.isEmpty()){
                    Intent intent = new Intent(getActivity(), ActyvityGioHang.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("SPMain", "cuahang");
                    startActivity(intent);
                }


            }
        });

        //Hện Horizontal của các Loại SP nè
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView = view.findViewById(R.id.rvLoai);
        recyclerView.setLayoutManager(layoutManager);

        LoaiAdapter.OnItemClickListener listener = new LoaiAdapter.OnItemClickListener() {
            @Override
            public void onLoaiItemClick(String idLoai) {
                Intent intent = new Intent(getActivity(), SanPhamTrangChu.class);
                intent.putExtra("idLoai", idLoai);
                startActivity(intent);
            }
        };

        FirebaseRecyclerOptions<LoaiModel> options =
                new FirebaseRecyclerOptions.Builder<LoaiModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("loai"), LoaiModel.class)
                        .build();

        loaiAdapter = new LoaiAdapter(options,listener);
        recyclerView.setAdapter(loaiAdapter);


        //Hiện SP Best Seller nè
        LinearLayoutManager layoutManagerSP = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvSPBestSeller = view.findViewById(R.id.rvSPBest);
        rvSPBestSeller.setLayoutManager(layoutManagerSP);

        SanPhamBestSeller.OnItemSPBestClickListener spBestClickListener = new SanPhamBestSeller.OnItemSPBestClickListener() {
            @Override
            public void onItemClick(String idSP) {
                Intent intent = new Intent(getActivity(), ChiTietSP.class);
                intent.putExtra("idSP", idSP);
                startActivity(intent);
            }
        };
        FirebaseRecyclerOptions<SanPhamModel> optionSP =
                new FirebaseRecyclerOptions.Builder<SanPhamModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("sanpham").orderByChild("isBanChay").startAt(true).endAt(true), SanPhamModel.class)
                        .build();
        sanPhamBestSeller = new SanPhamBestSeller(optionSP, spBestClickListener);
        rvSPBestSeller.setAdapter(sanPhamBestSeller);
        sanPhamBestSeller.startListening();


        //Sang trang tìm kiếm SP
        icTimKiemTC = view.findViewById(R.id.icTimKiemTC);
        icHoSoTC = view.findViewById(R.id.icHoSoTC);
        icTimKiemTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TimKiem.class);
                startActivity(intent);
            }
        });

        //Sang Trang Hồ Sơ nè
        icHoSoTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HoSo.class);
                startActivity(intent);
            }
        });

        return view;
    }

    //Lấy ảnh Banner từ Storage và hiện lên viewPage Adapter
    private void loadImagesFromFirebaseStorage() {
        // Khởi tạo Firebase Storage và lấy tham chiếu đến thư mục "Banner"
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("Banner");

        // Lấy danh sách các tệp từ Firebase Storage
        storageRef.listAll().addOnSuccessListener(listResult -> {
            // Duyệt qua từng tệp và tải URL của hình ảnh
            for (StorageReference item : listResult.getItems()) {
                item.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Nếu tải URL thành công, thêm URL vào danh sách
                    String imageUrl = uri.toString();
                    mImageUrls.add(imageUrl);

                    // Cập nhật adapter
                    viewPager.getAdapter().notifyDataSetChanged();
                }).addOnFailureListener(exception -> {
                    // Xử lý khi có lỗi xảy ra trong quá trình tải URL
                    Log.e("FirebaseStorage", "Error getting download URL", exception);
                });
            }
        }).addOnFailureListener(exception -> {
            // Xử lý khi có lỗi xảy ra trong quá trình lấy danh sách tệp
            Log.e("FirebaseStorage", "Error listing files", exception);
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        String slMua = String.valueOf(DsSanPhamMuaUtil.getInstance().getSoLuongSP());
        tvsoLuongSPM.setText(slMua);

    }

    @Override
    public void onStart() {
        super.onStart();
        loaiAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
//        loaiAdapter.stopListening();
    }
}