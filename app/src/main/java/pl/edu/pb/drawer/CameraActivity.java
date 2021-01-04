package pl.edu.pb.drawer;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Arrays;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class CameraActivity extends AppCompatActivity {

    CameraDevice mCamera = null;

    TextureView photo_texture;
    FloatingActionButton take_photo_button;
    FloatingActionButton back_camera_button;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if ((requestCode == MainActivity.CAMERA_REQUEST) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            startCamera();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        photo_texture = findViewById(R.id.texture);

        startCamera();

        FloatingActionButton button_photo = findViewById(R.id.start_camera);
        button_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPreview();
                button_photo.setVisibility(View.INVISIBLE);
            }
        });

        take_photo_button = findViewById(R.id.take_photo_camera_button);
        take_photo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });
        back_camera_button = findViewById(R.id.back_camera_button);
        back_camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPreview();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("MissingPermission")
    private void startCamera() {
        CameraManager cm = (CameraManager) getSystemService(CAMERA_SERVICE);
        try {
            cm.openCamera(
                    cm.getCameraIdList()[0],
                    new CameraDevice.StateCallback() {
                        @Override
                        public void onOpened(@NonNull CameraDevice camera) {
                            Log.i("[CAMERA]", "Camera opened");
                            mCamera = camera;
                        }
                        @Override
                        public void onDisconnected(@NonNull CameraDevice camera) {
                            Log.i("[CAMERA]", "Camera disconnected");
                        }
                        @Override
                        public void onError(@NonNull CameraDevice camera, int error) {
                            Log.e("[CAMERA]", String.format("Camera error: %d", error));
                        }
                    },
                    null
            );
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void startPreview() {
        Matrix matrix = new Matrix();
        RectF preview = new RectF(0, 0, 640, 480);
        RectF texture = new RectF(0, 0, photo_texture.getWidth(), photo_texture.getHeight());
        matrix.setRectToRect(preview, texture, Matrix.ScaleToFit.FILL);
        matrix.postScale(1.f, 1.f, preview.centerX(), preview.centerY());
        photo_texture.setTransform(matrix);
        SurfaceTexture surface = photo_texture.getSurfaceTexture();
        surface.setDefaultBufferSize(640, 480);
        Surface s = new Surface(surface);
        try {
            CaptureRequest.Builder request = mCamera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            request.addTarget(s);
            mCamera.createCaptureSession(
                    Arrays.asList(s),
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession session) {
                            request.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);
                            try {
                                session.setRepeatingRequest(request.build(), null, null);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                        }
                    }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void takePhoto() {
        Matrix matrix = new Matrix();
        RectF preview = new RectF(0, 0, 640, 480);
        RectF texture = new RectF(0, 0, photo_texture.getWidth(), photo_texture.getHeight());
        matrix.setRectToRect(preview, texture, Matrix.ScaleToFit.FILL);
        matrix.postScale(1.f, 1.f, preview.centerX(), preview.centerY());
        photo_texture.setTransform(matrix);
        SurfaceTexture surface = photo_texture.getSurfaceTexture();
        surface.setDefaultBufferSize(640, 480);
        Surface s = new Surface(surface);
        try {
            CaptureRequest.Builder request = mCamera.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            request.addTarget(s);
            mCamera.createCaptureSession(
                    Arrays.asList(s),
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession session) {
                            request.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);
                            try {
                                session.capture(request.build(), null, null);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                        }
                    }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}