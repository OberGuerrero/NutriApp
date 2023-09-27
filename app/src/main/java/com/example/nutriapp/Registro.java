package com.example.nutriapp;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registro extends AppCompatActivity {
    private EditText etNombre, etTelefono, etCorreo, etPass, etPassChk;
    private Button btnRegistrar;
    private Button btnVolver;
    private static final int REQUEST_CODE_PERMISSIONS = 1001;
    private static final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private ImageView ivPerfil;
    private Button btnCaptura;
    private ImageCapture imageCapture;
    private Executor executor = Executors.newSingleThreadExecutor();
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etNombre = findViewById(R.id.etNombre);
        etTelefono = findViewById(R.id.etTelefono);
        etCorreo = findViewById(R.id.etCorreo);
        etPass = findViewById(R.id.etPass);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnVolver = findViewById(R.id.btnVolver);
        ivPerfil = findViewById(R.id.ivPerfil);
        btnCaptura = findViewById(R.id.btnCaptura);

        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }

        //btnRegistrar.setOnClickListener(new View.OnClickListener()
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnCaptura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captura();
            }
        });
       GitHubService apiService = (GitHubService) RetrofitNutriApp.getRetrofitInstance().create(GitHubService.class);
        Call<Respuesta> call = apiService.listRepos(String.valueOf(id));

        call = apiService.listRepos(String.valueOf(id));

        call.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                if (response.isSuccessful()) {
                    Respuesta respuesta = response.body();
                } else {
                    // Manejar error de respuesta, por ejemplo, mostrar un mensaje de error
                }
            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {
                // Manejar error de red, por ejemplo, mostrar un mensaje de error de red
            }
        });
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider);
                } catch (ExecutionException | InterruptedException e) {
                    Log.e("ProfileActivity", "Error starting camera: " + e.toString());
                }
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        ImageCapture.Builder builder = new ImageCapture.Builder();
        imageCapture = builder.build();
        CameraSelector cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;

        cameraProvider.unbindAll();

        try {
            cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, imageCapture);
        } catch (Exception e) {
            Log.e("ProfileActivity", "Error binding camera: " + e.toString());
        }
    }
    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void captura() {
        if (imageCapture != null) {
            File photoFile = createImageFile();

            ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(photoFile).build();

            imageCapture.takePicture(outputFileOptions, executor, new ImageCapture.OnImageSavedCallback() {
                @Override
                public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            ivPerfil.setImageURI(Uri.fromFile(photoFile));

                            String photoFilePath = photoFile.getAbsolutePath();

                            Toast.makeText(Registro.this, "Foto tomada y guardada", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onError(@NonNull ImageCaptureException exception) {
                    Log.e("ProfileActivity", "Error al tomar la foto: " + exception.getMessage());
                }
            });
        }
    }

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir("ProfileImages");

        File imageFile = null;
        try {
            imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (Exception e) {
            Log.e("ProfileActivity", "Error creando el archivo de imagen: " + e.toString());
        }

        return imageFile;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(this, "Se requieren permisos para usar la cámara.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}

