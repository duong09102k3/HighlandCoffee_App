package com.example.btl_android_menu.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android_menu.Model.SinhVienModel;
import com.example.btl_android_menu.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.text.DecimalFormat;

public class SinhVienAdapter extends FirebaseRecyclerAdapter<SinhVienModel, SinhVienAdapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public SinhVienAdapter(@NonNull FirebaseRecyclerOptions<SinhVienModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull SinhVienAdapter.myViewHolder holder, int position, @NonNull SinhVienModel model) {
        holder.MaSV.setText(model.getMaSV());
        holder.NameSV.setText(model.getHoTen());
        holder.DiaChi.setText(model.getDiaChi());
        holder.ngaySinh.setText(model.getDate());
        holder.gTinh.setText(model.getGioiTinh());
        holder.Email.setText(model.getEmail());
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        float diem = model.getDiemTB();
        String formattedDiem = decimalFormat.format(diem);
        holder.DiemTB.setText(formattedDiem);

    }

    @NonNull
    @Override
    public SinhVienAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chi_tiet_sv,parent,false);

        return new SinhVienAdapter.myViewHolder(view);
    }
    class myViewHolder extends RecyclerView.ViewHolder {
        TextView MaSV, NameSV, DiaChi, ngaySinh, gTinh, Email, DiemTB;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            MaSV = (TextView) itemView.findViewById(R.id.maSV);
            NameSV = (TextView) itemView.findViewById(R.id.nameSV);
            DiaChi = (TextView) itemView.findViewById(R.id.diaChi);
            ngaySinh =(TextView) itemView.findViewById(R.id.date);
            gTinh = (TextView) itemView.findViewById(R.id.gioiTinh);
            Email = (TextView) itemView.findViewById(R.id.email);
            DiemTB = (TextView) itemView.findViewById(R.id.diemTB);

        }
    }
}
