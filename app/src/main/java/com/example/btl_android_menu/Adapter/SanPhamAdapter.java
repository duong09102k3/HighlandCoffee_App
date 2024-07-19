package com.example.btl_android_menu.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.btl_android_menu.ChiTietSP;
import com.example.btl_android_menu.Model.LoaiModel;
import com.example.btl_android_menu.Model.SanPhamModel;
import com.example.btl_android_menu.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.text.Collator;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class SanPhamAdapter extends FirebaseRecyclerAdapter<SanPhamModel, SanPhamAdapter.myViewHolder> {

    public SanPhamAdapter(@NonNull FirebaseRecyclerOptions<SanPhamModel> options, OnItemSPClickListener listener) {
        super(options);
        this.mListener = listener;

    }

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *

     */



    public interface OnItemSPClickListener {
        void onItemClick(String idSP);
    }
    private OnItemSPClickListener mListener;


    @SuppressLint("RecyclerView")
    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull SanPhamModel model) {
        //Làm sắp xếp ds
//        List<SanPhamModel> sortedList = new ArrayList<>(getSnapshots());
//        Collections.sort(sortedList, new Comparator<SanPhamModel>() {
//            @Override
//            public int compare(SanPhamModel sp1, SanPhamModel sp2) {
//                return sp1.getNameSP().compareToIgnoreCase(sp2.getNameSP());
//            }
//        });
//        SanPhamModel sortedModel = sortedList.get(position);
        holder.nameSP.setText(model.getNameSP());
        holder.moTa.setText(model.getMoTa());
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        float giaSP = model.getGia();
        String formattedGiaSP = decimalFormat.format(giaSP);
        holder.gia.setText(formattedGiaSP);
//        holder.gia.setText(Float.toString(model.getGia()));


        Glide.with(holder.imgSP.getContext())
                .load(model.getImgSP())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.imgSP);

        String idSP = getRef(position).getKey();
        holder.itemSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(idSP);
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sanpham,parent,false);

        return new SanPhamAdapter.myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        ImageView imgSP;
        TextView nameSP, moTa, gia;
        LinearLayout itemSP;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            imgSP = (ImageView)itemView.findViewById(R.id.imgSP);
            nameSP = (TextView)itemView.findViewById(R.id.nameSP);
            moTa =(TextView) itemView.findViewById(R.id.moTa);
            gia = (TextView) itemView.findViewById(R.id.gia);
            itemSP = (LinearLayout) itemView.findViewById(R.id.itemSP);

        }
    }
}
