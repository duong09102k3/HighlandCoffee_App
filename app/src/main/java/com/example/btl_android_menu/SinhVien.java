package com.example.btl_android_menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.btl_android_menu.Adapter.SinhVienAdapter;
import com.example.btl_android_menu.Model.SinhVienModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class SinhVien extends AppCompatActivity {
    EditText edMa, edTen, edLocation, edDate, edGT, edEmail, edDiem;
    Button btnHienDS;
    RecyclerView recyclerViewSV;
    SinhVienAdapter sinhVienAdapter;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinh_vien);
        edMa = findViewById(R.id.edMaSV);
        edTen = findViewById(R.id.edNameSV);
        edLocation = findViewById(R.id.edDiaChi);
        edDate = findViewById(R.id.edNgaySinh);
        edGT = findViewById(R.id.edGioiTinh);
        edEmail = findViewById(R.id.edEmail);
        edDiem = findViewById(R.id.edDiemTB);
        btnHienDS = findViewById(R.id.btnHien);
        recyclerViewSV = findViewById(R.id.rvSinhVien);

        edDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo một Calendar instance để lấy ngày tháng mặc định
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                // Tạo một DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(SinhVien.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // Đặt ngày tháng đã chọn vào EditText
                                String dateString = dayOfMonth + "/" + (month + 1) + "/" + year;
                                edDate.setText(dateString);
                            }
                        }, year, month, dayOfMonth);

                // Hiển thị DatePickerDialog
                datePickerDialog.show();
            }
        });

        recyclerViewSV.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<SinhVienModel> optionCH =
                new FirebaseRecyclerOptions.Builder<SinhVienModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("sinhvienMinh"), SinhVienModel.class)
                        .build();
        sinhVienAdapter = new SinhVienAdapter(optionCH);
        recyclerViewSV.setAdapter(sinhVienAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("sinhvienMinh");

        btnHienDS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edMaSV = edMa.getText().toString().trim();
                String edNameSV = edTen.getText().toString().trim();
                String edDiachi = edLocation.getText().toString().trim();
                String edNgay = edDate.getText().toString().trim();
                String edGioiTinh = edGT.getText().toString().trim();
                String edMail = edEmail.getText().toString().trim();
                String edDiemTB = edDiem.getText().toString().trim();
                float diemTB;
                try {

                    diemTB = Float.parseFloat(edDiemTB);
                } catch (NumberFormatException e) {

                    diemTB = 0.0f;
                }


                if (!edMaSV.isEmpty() && !edNameSV.isEmpty()&& !edDiachi.isEmpty()&& !edNgay.isEmpty()&& !edGioiTinh.isEmpty()&& !edMail.isEmpty()&& diemTB != 0.0f) {

                    pushDataToFirebase(edMaSV, edNameSV, edDiachi,edNgay, edGioiTinh, edMail, diemTB);
                    edMa.setText("");
                    edTen.setText("");
                    edLocation.setText("");
                    edDate.setText("");
                    edGT.setText("");
                    edEmail.setText("");
                    edDiem.setText("");
                } else {
                    Toast.makeText(SinhVien.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void pushDataToFirebase(String ma, String ten, String diaChi, String ngay, String gt, String mail, float diem) {

        String idSV = databaseReference.push().getKey();
        SinhVienModel sinhVienModel = new SinhVienModel(ma, ten, diaChi, ngay, gt, mail, diem);
        databaseReference.child(idSV).setValue(sinhVienModel);

        // Hiển thị thông báo cho người dùng
        Toast.makeText(this, "Đã thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onStart() {
        super.onStart();
        sinhVienAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        sinhVienAdapter.stopListening();
    }

}