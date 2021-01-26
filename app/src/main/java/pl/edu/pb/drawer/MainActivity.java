package pl.edu.pb.drawer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.io.File;
import java.util.Arrays;
import java.util.Random;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity {


    Button camera_button;
    Button gallery_button;
    Button draft_load_button;
    Button stock_images_button;

    Random random;

    static int CAMERA_REQUEST = 1234;
    boolean camera_exists = false;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.random = new Random();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] message = {
                        getString(R.string.quack),
                        getString(R.string.main_activity_string1),
                        getString(R.string.main_activity_string2),
                        getString(R.string.main_activity_string3)};

                Snackbar.make(view, message[random.nextInt(message.length)], Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // check if camera exists
        PackageManager pm = getPackageManager();
        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            this.finish();
        }
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.CAMERA}, CAMERA_REQUEST);
        } else {
            camera_exists = true;
        }

        // redirect to another activity
        camera_button = findViewById(R.id.take_photo_button);
        gallery_button = findViewById(R.id.choose_photo_button);
        draft_load_button = findViewById(R.id.draft_load_button);
        stock_images_button = findViewById(R.id.stock_images_button);

        if(!camera_exists) {
            camera_button.setEnabled(false);
        }
        camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });
        camera_button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Snackbar.make(view, R.string.onLongClick_camera_button_string, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }
        });
        gallery_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
                //intent.putExtra(KEY_EXTRA_SENSOR_TYPE, correctAnswer);
                startActivity(intent);
            }
        });
        gallery_button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Snackbar.make(view, R.string.onLongClick_gallery_button_string, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }
        });
        draft_load_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });
        draft_load_button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Snackbar.make(view, R.string.draft_loading_string, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }
        });
        stock_images_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, StockImagesActivity.class);
                startActivity(intent);
            }
        });
        stock_images_button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Snackbar.make(view, R.string.sock_images_button_info, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }
        });

        // if last file doesn't exist
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File f = new File(directory, "image.jpg");
        if(f == null || !f.exists()) {
            draft_load_button.setEnabled(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckIfDraftExists();

    }

    private void CheckIfDraftExists() {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File f = new File(directory, "image.jpg");
        draft_load_button.setEnabled(f != null && f.exists());
    }
}