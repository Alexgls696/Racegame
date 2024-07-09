package com.mygdx.game;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class AndroidLauncher extends AndroidApplication implements GameMainActivity {
    private AlarmManager manager;
    private Context context;
    private static final int AUDIO_PERMISSION_CODE = 100;
    private static final int NOTIFICATION_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        context = getApplicationContext();

        CheckNotificationPermission(Manifest.permission.POST_NOTIFICATIONS, NOTIFICATION_PERMISSION_CODE); //Проверка разрешения на уведомления
        DailyNotificationReceiver.setDailyAlarm(context, 10, 0);

        initialize(new Racing(this), config);
    }

    public void CheckNotificationPermission(String permission, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            }
        }
    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        } else {
            permissionStatus = "OK";
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AUDIO_PERMISSION_CODE || requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                runOnUiThread(() -> Toast.makeText(context, "Разрешение получено", Toast.LENGTH_SHORT).show());
                permissionStatus = "OK";
            } else {
                runOnUiThread(() -> Toast.makeText(context, "Разрешение не было получено", Toast.LENGTH_SHORT).show());
                permissionStatus = "NO";
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            Uri uri = data.getData();
            Toast.makeText(context, uri.getPath(), Toast.LENGTH_SHORT).show();
            filepath = getPathFromUri(context,uri);
        } else {
            filepath = "Closed";
        }
    }

    public String getPathFromUri(Context context, Uri uri) {
        if ("content".equals(uri.getScheme())) {
            String[] projection = { MediaStore.MediaColumns.DISPLAY_NAME };
            try (Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
                    String displayName = cursor.getString(index);
                    File file = new File(context.getExternalFilesDir(null), displayName);
                    try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
                         OutputStream outputStream = new FileOutputStream(file)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return file.getPath();
                }
            }
        } else if ("file".equals(uri.getScheme())) {
            return uri.getPath();
        }
        return "Closed";
    }

    private String filepath;
    private String permissionStatus;

    @Override
    public String getAudioFilePath() {
        return filepath;
    }
    @Override
    public void setPathNull(){
        filepath=null;
    }

    @Override
    public String getAudioPermission() {
        return permissionStatus;
    }

    @Override
    public void checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, AUDIO_PERMISSION_CODE);
        } else {
            checkPermission(Manifest.permission.READ_MEDIA_AUDIO, AUDIO_PERMISSION_CODE);
        }
    }

    @Override
    public void openFileChooser() {
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("audio/*");
            startActivityForResult(intent, 1);
        } catch (ActivityNotFoundException ex) {
            filepath = "Not found";
            runOnUiThread(() -> Toast.makeText(context, "Не удалось запустить приложение для выбора музыки", Toast.LENGTH_SHORT).show());
        }
    }

    @Override
    public void finishAffinity(){
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}
