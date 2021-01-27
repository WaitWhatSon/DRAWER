package pl.edu.pb.drawer;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class GalleryActivity extends AppCompatActivity {

    private final String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int LOCATION_REQUEST = 222;

    ImageView imageView;
    FloatingActionButton fab;
    Button gallery_back_button;
    Button gallery_open_button;
    Button button_edit_loaded_picture;

    Random random;

    private static final String LOADED_IMAGE = "loaded image";
    private Boolean image_choosen = false;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(LOCATION_REQUEST)
    private void checkLocationRequest() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            gallery_open_button.setEnabled(true);
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this,"Please grant permission",
                    LOCATION_REQUEST, perms);
            gallery_open_button.setEnabled(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.random = new Random();

        fab = findViewById(R.id.fab); // floating action button with tips
        gallery_back_button = findViewById(R.id.button_return_from_load_picture); // back to previous activity or cam preview
        gallery_open_button = findViewById(R.id.button_load_picture); // choose picture from gallery, unable next button
        button_edit_loaded_picture = findViewById(R.id.button_edit_loaded_picture); // next activity - edit image

        button_edit_loaded_picture.setEnabled(false); // locked until photo wasn't chosen

        if(savedInstanceState != null) {
            this.image_choosen = savedInstanceState.getBoolean(LOADED_IMAGE);
            if (this.image_choosen)
            {
                loadImageFromStorage();
                button_edit_loaded_picture.setEnabled(true);
            }
            Log.d("hm", "SET SAVED INSTANCE");
        }

        // permissions check
        if (EasyPermissions.hasPermissions(this, galleryPermissions)) {
            gallery_open_button.setEnabled(true);
        } else {
            EasyPermissions.requestPermissions(this, "Access for storage",101, galleryPermissions);
            gallery_open_button.setEnabled(false);
        }

        // tips button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] message = {
                        getString(R.string.quack),
                        getString(R.string.gallery_activity_string1)};

                Snackbar.make(view, message[random.nextInt(message.length)], Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // back to previous activity
        gallery_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // open gallery
        gallery_open_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        // to next activity
        button_edit_loaded_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GalleryActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            imageView = findViewById(R.id.loaded_picture_view);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            saveToInternalStorage(BitmapFactory.decodeFile(picturePath));

            button_edit_loaded_picture.setEnabled(true);
            this.image_choosen = true;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("hm", "SAVE2");
        outState.putBoolean(LOADED_IMAGE, image_choosen);
    }

    private void saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File my_path = new File(directory,"image.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(my_path);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert fos != null;
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadImageFromStorage()
    {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

        try {
            File f = new File(directory, "image.jpg");
            imageView = findViewById(R.id.loaded_picture_view);
            imageView.setImageBitmap(BitmapFactory.decodeStream(new FileInputStream(f)));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }
}