package com.example.sim.category;

import android.Manifest;
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
import com.example.sim.constants.Urls;
import com.example.sim.dto.category.CategoryDto;
import com.example.sim.dto.category.CategoryEditDto;
import com.example.sim.services.ApplicationNetwork;
import com.example.sim.services.BaseActivity;
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

public class CategoryEditActivity extends BaseActivity {
    int id = 0;
    TextInputLayout tlCategoryNameEdit;
    TextInputLayout tlCategoryDescriptionEdit;
    ImageView ivSelectImageEdit;
    private String filePath;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_edit);

        Bundle b = getIntent().getExtras();
        if(b!=null)
            id=b.getInt("id");

        if (id != 0) {
            fetchCategoryInfo(id);
        }

        tlCategoryNameEdit = findViewById(R.id.tlCategoryNameEdit);
        tlCategoryDescriptionEdit = findViewById(R.id.tlCategoryDescriptionEdit);
        ivSelectImageEdit = findViewById(R.id.ivSelectEditImage);

    }

    private void fetchCategoryInfo(int categoryId) {

        ApplicationNetwork.getInstance()
                .getCategoriesApi()
                .getCategoryInfo(categoryId)
                .enqueue(new Callback<CategoryDto>() {
                    @Override
                    public void onResponse(Call<CategoryDto> call, Response<CategoryDto> response) {
                        if (response.isSuccessful()) {
                            CategoryDto category = response.body();
                            if (category != null) {
                                // Populate the form fields with the retrieved category information
                                tlCategoryNameEdit.getEditText().setText(category.getName());
                                tlCategoryDescriptionEdit.getEditText().setText(category.getDescription());

                                String url = "https://cdn.pixabay.com/photo/2016/01/03/00/43/upload-1118929_1280.png";

                                if (category.getImage() != null) {
                                    url = Urls.BASE + "/images/" + category.getImage() ;

                                }
                                Glide.with(CategoryEditActivity.this)
                                        .load(url)
                                        .apply(new RequestOptions().override(300))
                                        .into(ivSelectImageEdit);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CategoryDto> call, Throwable t) {
                        // Handle failure
                    }
                });
    }

    private final String TAG="CategoryEditActivity";
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
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


//    public void onClickSelectImage(View view) {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("image/*");
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
//    }


    public void openEditGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            Uri uri = data.getData();
//            File imageFile = new File(getRealPathFromURI(uri));
//        }
//    }
//
//    private String getRealPathFromURI(Uri uri) {
//        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//        cursor.moveToFirst();
//        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//        return cursor.getString(idx);
//    }


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
                    .into(ivSelectImageEdit);


            // If you want to get the file path from the URI, you can use the following code:
            filePath = getPathFromURI(uri);
        }
    }


    // This method converts the image URI to the direct file system path of the image file
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


    public void onClickEditCategory(View view) {
        try {
            String name = tlCategoryNameEdit.getEditText().getText().toString().trim();
            String description = tlCategoryDescriptionEdit.getEditText().getText().toString().trim();
            int categoryId = id;

            Map<String, RequestBody> params = new HashMap<>();
            params.put("name", RequestBody.create(MediaType.parse("text/plain"), name));
            params.put("description", RequestBody.create(MediaType.parse("text/plain"), description));
            params.put("id", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(categoryId)));



            MultipartBody.Part imagePart=null;
            if (filePath != null) {
                File imageFile = new File(filePath);
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
                imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);
            }


            ApplicationNetwork.getInstance()
                    .getCategoriesApi()
                    .edit(params, imagePart)
                    .enqueue(new Callback<CategoryDto>() {
                        @Override
                        public void onResponse(Call<CategoryDto> call, Response<CategoryDto> response) {
                            if(response.isSuccessful())
                            {
                                Intent intent = new Intent(CategoryEditActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
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

