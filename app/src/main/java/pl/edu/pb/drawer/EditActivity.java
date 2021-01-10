package pl.edu.pb.drawer;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EditActivity extends AppCompatActivity {

    ImageView edit_image_view;
    Bitmap current_photo;
    Bitmap original_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        edit_image_view = findViewById(R.id.editing_picture_view);
        loadImageFromStorage();


        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container_view);

        if (fragment == null) {

            fragment = new FiltersFragment(current_photo);
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container_view, fragment)
                    .commit();
        }

        // top options
        FloatingActionButton grid = findViewById(R.id.fab_grid);
        grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current_photo != null)
                {
                    int x_step = current_photo.getWidth()/10;
                    int y_step = current_photo.getHeight()/10;

                    for(int x = x_step; x < current_photo.getWidth(); x+=x_step) {
                        for (int y = 0; y < current_photo.getHeight(); y++) {
                            current_photo.setPixel(x, y, Color.argb(255, 0, 0, 0));
                        }
                    }

                    for(int y = y_step; y < current_photo.getHeight(); y+=y_step) {
                        for (int x = 0; x < current_photo.getWidth(); x++) {
                            current_photo.setPixel(x, y, Color.argb(255, 0, 0, 0));
                        }
                    }
                }
            }
        });

        FloatingActionButton sketch = findViewById(R.id.fab_sketch);
        sketch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FiltersLibrary.MedianFilter(current_photo);
                FiltersLibrary.GreyscaleFilter(current_photo);
                FiltersLibrary.SharpenFilter(current_photo);
            }
        });

        FloatingActionButton contures = findViewById(R.id.fab_contures);
        contures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FiltersLibrary.GreyscaleFilter(current_photo);
                FiltersLibrary.MedianFilter(current_photo);
                FiltersLibrary.NiblackFilter(current_photo, 2, -10);
            }
        });

        FloatingActionButton reset = findViewById(R.id.fab_reset_img);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // copy from original
                int [] pixels = new int[original_photo.getWidth() * original_photo.getHeight()];
                original_photo.getPixels(pixels, 0, original_photo.getWidth(), 0, 0, original_photo.getWidth(), original_photo.getHeight());
                current_photo.setPixels(pixels, 0, original_photo.getWidth(), 0, 0, original_photo.getWidth(), original_photo.getHeight());
            }
        });

        FloatingActionButton save = findViewById(R.id.fab_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    SaveImage(current_photo);
                    Snackbar.make(view, "Image saved.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else
                {
                    Snackbar.make(view, "Cannot save image due to too old version.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    private void loadImageFromStorage()
    {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

        try {
            File f = new File(directory, "image.jpg");
            original_photo = BitmapFactory.decodeStream(new FileInputStream(f));
            current_photo = original_photo.copy(original_photo.getConfig(), true);
            UpdateCurrentImage();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void SaveImage(Bitmap finalBitmap) {

        if (!isStoragePermissionGranted()) {
            return;
        }

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Drawer");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        String fname = "Image-"+ dtf.format(now) +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ())
            file.delete ();
        try {
            Log.d("Try to save image", "trying...");
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Save image", "Cannot save.");
        }

        galleryAddPic(fname);
    }

    private void galleryAddPic(String fpath) {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(fpath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d("Storage Permission", "Permission is granted");
                return true;
            } else {
                Log.d("Storage Permission", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.d("Storage Permission","Permission is granted");
            return true;
        }
    }

    public void UpdateCurrentImage()
    {
        if(current_photo != null)
        {
            edit_image_view.setImageBitmap(current_photo);
        }
    }

}