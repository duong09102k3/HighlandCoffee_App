package com.example.btl_android_menu;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.btl_android_menu.Model.PreferenceUtil;
import com.example.btl_android_menu.Model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HoSo extends AppCompatActivity  {
    private ImageView icBackHS, imgAvatar;
    private TextView tvName;
    private EditText edEmail, edHoten, edSinhNhat, edSDT, edGioiTinh;
    private Button luuProfile;
    private Uri mUri;
    private ProgressDialog dialog;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    private User userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ho_so);

        icBackHS = findViewById(R.id.icBackChangePass);
        imgAvatar = findViewById(R.id.img_avatar);
        tvName = findViewById(R.id.tv_Name);
        edEmail = findViewById(R.id.edEmail);
        edHoten = findViewById(R.id.edHoten);
        edSinhNhat = findViewById(R.id.edSinhNhat);
        edGioiTinh = findViewById(R.id.edGioiTinh);
        edSDT = findViewById(R.id.edSDT);
        luuProfile = findViewById(R.id.btn_LuuProfile);
        dialog = new ProgressDialog(HoSo.this);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("DisplayAvatar");

        edSinhNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(HoSo.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                edSinhNhat.setText(selectedDate);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        edGioiTinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HoSo.this);
                builder.setTitle("Chọn Giới Tính");
                builder.setItems(new CharSequence[]{"Nam", "Nữ"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectedGender = "";
                        if (which == 0) {
                            selectedGender = "Nam";
                        } else if (which == 1) {
                            selectedGender = "Nữ";
                        }
                        edGioiTinh.setText(selectedGender);
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        edSDT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phoneNumber = s.toString().trim();
                String mobile = "0[2-9]";
                Matcher mobileMatcher;
                Pattern mobilePattern = Pattern.compile(mobile);
                mobileMatcher = mobilePattern.matcher(phoneNumber);

                if (phoneNumber.length() == 10) {
                    // Nếu nhập đủ 10 số, không làm gì
                } else if (phoneNumber.length() > 10) {
                    // Nếu nhập quá 10 số, cắt bớt số điện thoại
                    edSDT.setText(phoneNumber.substring(0, 10));
                    edSDT.setError("Số điện thoại phải có đúng 10 số");
                    edSDT.setSelection(10); // Di chuyển con trỏ đến cuối
                } else if (!mobileMatcher.find()) {
                    edSDT.setError("Số điện thoại k đúng định dạng");
                } else {
                    edSDT.setError("Số điện thoại chưa đủ 10 số");
                }
            }
        });

        icBackHS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(HoSo.this, MainActivity.class);
//                intent.putExtra("fragmentHS", "giohang");
//                startActivity(intent);
                onBackPressed();
            }
        });

        String loginType = getIntent().getStringExtra("login_type");
        Log.d("LoginType", "Value: " + loginType);
        // Dựa vào loại đăng nhập, gọi phương thức hiển thị thông tin người dùng tương ứng
        if ("email".equals(loginType)) {
            showUser();
            PreferenceUtil.saveLoginType(this, "email");
        } else if ("google".equals(loginType)) {
            showUserGG();
            PreferenceUtil.saveLoginType(this, "google");
        }

        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();

            }
        });

        luuProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onclickLuuProfile();
                onclickLuuProfile();
            }
        });

    }

    private void showUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            return;
        }
        String name = user.getDisplayName();
        String hoten = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();

        if(name ==null){
            tvName.setVisibility(View.GONE);
        }
        else {
            tvName.setVisibility(View.VISIBLE);
            tvName.setText(name);
        }

        if(hoten ==null){
            edHoten.setText("");
        }
        else {
            edHoten.setText(name);
        }
        edEmail.setText(email);
        Glide.with(HoSo.this).load(photoUrl).circleCrop().error(R.drawable.avatar_foreground).into(imgAvatar);
    }

    private void showUserGG(){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //if (account != null) {

        edEmail.setText(account.getEmail());
        edHoten.setText(account.getDisplayName());
        tvName.setText(account.getDisplayName());
        Glide.with(this).load(account.getPhotoUrl()).error(R.drawable.avatar_foreground).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(imgAvatar);

    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }

    private void loadImageToCircleImageView(Uri uri) {
        Glide.with(this)
                .load(uri)
                .circleCrop()
                .into(imgAvatar);;
    }

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK && data != null && data.getData() != null){
            mUri = data.getData();
            imgAvatar.setImageURI(mUri);
            loadImageToCircleImageView(mUri);
            uploadPic();
        }
    }

    private void uploadPic(){
        if(mUri != null){
            //Save the image with uid of the currently logged user
            StorageReference fileReference = storageReference.child(mAuth.getCurrentUser().getUid() + getFiExtension(mUri));

            //Upload image to Storage
            fileReference.putFile(mUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUri = uri;
                            firebaseUser = mAuth.getCurrentUser();

                            //Finally set the display image of the user after upload
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(downloadUri).build();
                            firebaseUser.updateProfile(profileUpdates);
                        }
                    });
                    Toast.makeText(HoSo.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(HoSo.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(HoSo.this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFiExtension(Uri mUri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(mUri));
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Kiểm tra login_type từ SharedPreferences
        String loginType = PreferenceUtil.getLoginType(this);

        // Hiển thị dữ liệu người dùng tùy thuộc vào login_type
        if ("google".equals(loginType)) {
            showUserGG();
        } else if ("email".equals(loginType)) {
            showUser();
        }
        // Khôi phục dữ liệu hồ sơ từ SharedPreferences
        userProfile = PreferenceUtil.getUserProfile(this);
        showUserUpdate();
    }

    private void onclickLuuProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        dialog.show();
        String name = edHoten.getText().toString().trim();
        String gioiTinh = edGioiTinh.getText().toString().trim();
        String dienThoai = edSDT.getText().toString().trim();
        String sinhNhat = edSinhNhat.getText().toString().trim();

        String mobile = "0[2-9]";
        Matcher mobileMatcher;
        Pattern mobilePattern = Pattern.compile(mobile);
        mobileMatcher = mobilePattern.matcher(dienThoai);

        if (dienThoai.length() == 10) {
            // Nếu nhập đủ 10 số, không làm gì
        } else if (dienThoai.length() > 10) {
            // Nếu nhập quá 10 số, cắt bớt số điện thoại
            edSDT.setText(dienThoai.substring(0, 10));
            edSDT.setError("Số điện thoại phải có đúng 10 số");
            edSDT.setSelection(10); // Di chuyển con trỏ đến cuối
        } else if (!mobileMatcher.find()) {
            edSDT.setError("Số điện thoại k đúng định dạng");
        } else {
            edSDT.setError("Số điện thoại chưa đủ 10 số");
        }

        // Lấy URI của ảnh hiện tại
        Uri currentPhotoUri = user.getPhotoUrl();

        // Cập nhật thông tin người dùng trên Firebase Authentication
        UserProfileChangeRequest.Builder profileUpdatesBuilder = new UserProfileChangeRequest.Builder()
                .setDisplayName(name);

        if (currentPhotoUri != null) {
            // Nếu URI của ảnh hiện tại có sẵn, sử dụng nó để cập nhật ảnh người dùng
            profileUpdatesBuilder.setPhotoUri(currentPhotoUri);
        }

        UserProfileChangeRequest profileUpdates = profileUpdatesBuilder.build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Lưu thông tin vào Realtime Database
                            saveUserDataToDatabase(sinhNhat,gioiTinh,dienThoai);
                            showUserUpdate();
                            PreferenceUtil.saveUserProfile(HoSo.this, userProfile);
                        } else {
                            Toast.makeText(HoSo.this, "Lỗi khi cập nhật thông tin người dùng", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        dialog.dismiss();
    }

    private void saveUserDataToDatabase(String sinhNhat, String gioiTinh, String dienThoai) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        User u = new User(sinhNhat, gioiTinh, dienThoai);
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
        userRef.child(user.getUid()).setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(HoSo.this, "Save sucessfully", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(HoSo.this, "Save failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void showUserUpdate() {
        //Kiểm tra trạng thái đăng nhập của người dùng
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Người dùng đã đăng nhập, lấy ID của họ và truy vấn thông tin người dùng từ Firebase Realtime Database
            String userId = currentUser.getUid();
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        // Lấy thông tin người dùng từ snapshot
                        String name = currentUser.getDisplayName();
                        String email = currentUser.getEmail();
                        Uri photoUrl = currentUser.getPhotoUrl();
                        String phoneNumber = user.getPhoneNumber();
                        String gender = user.getGender();
                        String birthday = user.getBirthday();

                        // Hiển thị thông tin người dùng lên giao diện
                        tvName.setText(name);
                        edHoten.setText(name);
                        edGioiTinh.setText(gender);
                        edSDT.setText(phoneNumber);
                        edSinhNhat.setText(birthday);
                        edEmail.setText(email);
                        Uri uriHT = currentUser.getPhotoUrl();
                        if (uriHT != null) {
                            // Nếu URI tồn tại, tải và hiển thị ảnh từ URI
                            Glide.with(HoSo.this).load(uriHT).circleCrop().into(imgAvatar);
                        } else {
                            // Nếu URI không tồn tại, sử dụng ảnh mặc định
                            imgAvatar.setImageResource(R.drawable.avatar_foreground);
                        }                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Xử lý khi có lỗi
                    Toast.makeText(HoSo.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}