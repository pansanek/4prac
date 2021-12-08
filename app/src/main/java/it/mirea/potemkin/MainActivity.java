package it.mirea.potemkin;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    RecyclerView categoryRecycle;


    private static final int REQUEST_CODE_PERMISSION_CAMERA = 5;
    private static final String NOTIFICATION_CHANNEL_ID = "1";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        createNotificationChannel();
        Button button = findViewById(R.id.notif);
        button.setOnClickListener(v -> showNotification());
        Button buttonCamera = findViewById(R.id.camera);
        int permissionStatus = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS);

        buttonCamera.setOnClickListener(v -> {
            if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                workWithCamera();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]
                                {Manifest.permission.CAMERA},
                        REQUEST_CODE_PERMISSION_CAMERA);
            }
        });
    }


    private void workWithCamera() {
        Camera camera = Camera.open();

        camera.setDisplayOrientation(90);

        SurfaceView surfaceView = findViewById(R.id.surfaceView);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        CharSequence name = "Notifications";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                workWithCamera();
            }
        }

    }

    private void showNotification() {
        NotificationCompat.Builder builder = new
                NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Практическая работа 4")
                .setContentText("Потемкин Александр Станиславович ИКБО-07-20")
                .setStyle(new NotificationCompat.BigTextStyle());
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);
        Random random = new Random();
        notificationManager.notify(random.nextInt(), builder.build());
    }
}
