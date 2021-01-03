package pl.edu.pb.drawer;

import android.annotation.SuppressLint;
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

import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;

import java.util.Arrays;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class CameraActivity extends AppCompatActivity {

    CameraDevice mCamera = null;

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

        startCamera();

        FloatingActionButton button_photo = findViewById(R.id.start_camera);
        button_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPreview();
                button_photo.setVisibility(View.INVISIBLE);
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
        TextureView textureView = findViewById(R.id.texture);
        Matrix matrix = new Matrix();
        RectF preview = new RectF(0, 0, 640, 480);
        RectF texture = new RectF(0, 0, textureView.getWidth(), textureView.getHeight());
        matrix.setRectToRect(preview, texture, Matrix.ScaleToFit.FILL);
        matrix.postScale(1.5f, 1.5f, preview.centerX(), preview.centerY());
        textureView.setTransform(matrix);
        SurfaceTexture surface = textureView.getSurfaceTexture();
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

}