package com.appssuite.ai.image.generator.text.to.texttoimageai;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisplayImage extends AppCompatActivity {

    Button btnDownload, btnShare;
    ImageView displayImage;
    AppCompatButton btn_back,pro_option,add_account;
    String uri;
    ProgressDialog progressDialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        displayImage = findViewById(R.id.displayImage);
        btnDownload = findViewById(R.id.btnDownload);
        btnShare = findViewById(R.id.btnShare);
        btn_back = findViewById(R.id.back_btn);
        pro_option = findViewById(R.id.pro_option);


        String result = getIntent().getStringExtra("commandPrompt");
        imageGenrate(result);

        // Download Image
        btnDownload.setOnClickListener(view -> {
            progressDialog1 = new ProgressDialog(this);
            progressDialog1.setTitle("Loading...");
            progressDialog1.setCancelable(false);
            progressDialog1.show();
            bitMap(uri);

        });

        // Share Image
        btnShare.setOnClickListener(view -> {

            ShareImage(uri);

        });

        //Back to Main Screen
        btn_back.setOnClickListener(view -> {
            Intent intent = new Intent(DisplayImage.this,MainActivity.class);
            startActivity(intent);
            finish();
        });

        pro_option.setOnClickListener(view -> {
            Intent intent = new Intent(DisplayImage.this,Premium.class);
            startActivity(intent);
        });

    }

    void imageGenrate(String imageDis) {

        ///256x256, 512x512, or 1024x1024 pixels

        CreateImageRequest createImageRequests = new CreateImageRequest();
        createImageRequests.prompt = imageDis;
        createImageRequests.n = 1;
        createImageRequests.size = "1024x1024";
        createImageRequests.responseFormat = "url";
        ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();


        Call<ImageResult> call = OpenAiService.getOpenAiService().openAiApi.createImage(createImageRequests);


        call.enqueue(new Callback<ImageResult>() {
            @Override
            public void onResponse(Call<ImageResult> call, Response<ImageResult> response) {

                ImageResult imageResult = response.body();

                if(imageResult != null) {
                    progress.dismiss();
                    uri = imageResult.data.get(0).url;
                    Glide.with(getApplicationContext())
                            .load(uri)
                            .centerCrop()
                            .into(displayImage);
                }
            }


            @Override
            public void onFailure(Call<ImageResult> call, Throwable t) {
                Log.d("fail", t.toString());

            }
        });


    }
    private void bitMap(String uri) {
        Glide.with(getApplicationContext())
                .asBitmap()
                .load(uri)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        // Save the image to the device's storage
                        saveImage(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Do nothing
                    }
                });
    }

    private String saveImage(Bitmap image) {
        String savedImagePath = null;

        String imageFileName = "JPEG_" + UUID.randomUUID().toString() + ".jpg";
        ;
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/MyAppImages");
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Add the image to the system gallery
            galleryAddPic(savedImagePath);
            progressDialog1.dismiss();
            Toast.makeText(getApplicationContext(), "SUCCESSFULLY SAVED...", Toast.LENGTH_LONG).show();
        }
        return savedImagePath;
    }

    private void galleryAddPic(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    void ShareImage(String uri){
        Glide.with(getApplicationContext())
                .asBitmap()
                .load(uri)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        // Save the image to the device's storage
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.PNG, 100, bos);
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("image/*");
                        String path = MediaStore.Images.Media.insertImage(getContentResolver(), resource, null, null);
                        Uri imageUri;
                        imageUri = Uri.parse(path);
                        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(intent , "Share"));

                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Do nothing
                    }
                });

    }
}