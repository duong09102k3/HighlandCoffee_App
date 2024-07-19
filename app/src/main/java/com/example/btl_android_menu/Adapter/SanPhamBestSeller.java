package com.example.btl_android_menu.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.btl_android_menu.Model.SanPhamModel;
import com.example.btl_android_menu.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.text.DecimalFormat;

public class SanPhamBestSeller extends FirebaseRecyclerAdapter<SanPhamModel, SanPhamBestSeller.myViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public SanPhamBestSeller(@NonNull FirebaseRecyclerOptions<SanPhamModel> options, SanPhamBestSeller.OnItemSPBestClickListener listener) {
        super(options);
        this.mListener = listener;

    }

    public interface OnItemSPBestClickListener {
        void onItemClick(String idSP);
    }
    private SanPhamBestSeller.OnItemSPBestClickListener mListener;

    @Override
    protected void onBindViewHolder(@NonNull SanPhamBestSeller.myViewHolder holder, int position, @NonNull SanPhamModel model) {
        holder.nameSP.setText(model.getNameSP());
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        float giaSP = model.getGia();
        String formattedGiaSP = decimalFormat.format(giaSP);
        holder.gia.setText(formattedGiaSP);


        Glide.with(holder.imgSP.getContext())
                .load(model.getImgSP())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.imgSP);

        String idSP = getRef(position).getKey();
        holder.itemSPBest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(idSP);
            }
        });
    }

    @NonNull
    @Override
    public SanPhamBestSeller.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sp_best,parent,false);

        return new SanPhamBestSeller.myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        ImageView imgSP;
        TextView nameSP, gia;
        LinearLayout itemSPBest;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            imgSP = (ImageView)itemView.findViewById(R.id.imgAnhSP);
            nameSP = (TextView)itemView.findViewById(R.id.txtNameSP);
            gia = (TextView) itemView.findViewById(R.id.txtGiaSP);
            itemSPBest = (LinearLayout) itemView.findViewById(R.id.itemSPBest);

        }
    }
}
