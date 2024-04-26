package com.example.sim.category;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.example.sim.MainActivity;
import com.example.sim.R;

import com.example.sim.dto.category.CategoryDto;
import com.example.sim.services.ApplicationNetwork;
import com.example.sim.services.BaseActivity;
import com.example.sim.utils.CommonUtils;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.widget.ProgressBar;

public class CategoryCreateActivity extends BaseActivity {
    TextInputLayout tlCategoryName;
    TextInputLayout tlCategoryDescription;
    ImageView ivSelectImage;
    private String filePath;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_create);
        tlCategoryName = findViewById(R.id.tlCategoryName);
        tlCategoryDescription = findViewById(R.id.tlCategoryDescription);
        ivSelectImage = findViewById(R.id.ivSelectImage);
        String url = "https://cdn.pixabay.com/photo/2016/01/03/00/43/upload-1118929_1280.png";
        Glide  .with(this)
                .load(url)
                .apply(new RequestOptions().override(300))
                .into(ivSelectImage);
    }

    private final String TAG="CategoryCreateActivity";
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    public void openGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the URI of the selected image
            Uri uri = data.getData();

            Glide
                    .with(this)
                    .load(uri)
                    .apply(new RequestOptions().override(300))
                    .into(ivSelectImage);

            // If you want to get the file path from the URI, you can use the following code:
            filePath = getPathFromURI(uri);
        }
    }

    private String getPathFromURI(Uri contentUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String filePath = cursor.getString(column_index);
            cursor.close();
            return filePath;
        }
        return null;
    }

    public void onClickCreateCategory(View view) {
        CommonUtils.showLoading(this);

        try {
            String name = tlCategoryName.getEditText().getText().toString().trim();
            String description = tlCategoryDescription.getEditText().getText().toString().trim();

            Map<String, RequestBody> params = new HashMap<>();
            params.put("name", RequestBody.create(MediaType.parse("text/plain"), name));
            params.put("description", RequestBody.create(MediaType.parse("text/plain"), description));

            MultipartBody.Part imagePart=null;
            if (filePath != null) {
                File imageFile = new File(filePath);
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
                imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);
            }

            ApplicationNetwork.getInstance()
                    .getCategoriesApi()
                    .create(params, imagePart)
                    .enqueue(new Callback<CategoryDto>() {
                        @Override
                        public void onResponse(Call<CategoryDto> call, Response<CategoryDto> response) {
                            if(response.isSuccessful())
                            {
                                Intent intent = new Intent(CategoryCreateActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                                CommonUtils.hideLoading();
                            }
                        }

                        @Override
                        public void onFailure(Call<CategoryDto> call, Throwable throwable) {

                        }
                    });
        }
        catch(Exception ex) {
            Log.e("--CategoryCreateActivity--", "Problem create "+ ex.getMessage());
        }
    }
}