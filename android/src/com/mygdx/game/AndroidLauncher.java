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
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import java.util.Calendar;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;


public class AndroidLauncher extends AndroidApplication implements GameMainActivity {
    private AlarmManager manager;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //startAlarmBroadcastReceiver(this);
        // NotificationReceiver.scheduleNotification(this, 5, "Notification", "Something text");

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        context = getApplicationContext();
        initialize(new Racing(this), config);
    }


    public static void startAlarmBroadcastReceiver(Context context) {
        Intent _intent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, _intent, PendingIntent.FLAG_MUTABLE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 51);
        calendar.set(Calendar.SECOND, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private static final int AUDIO_PERMISSION_CODE = 100;

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
        if (requestCode == AUDIO_PERMISSION_CODE) {
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
            filepath = getPathFromUri(uri);
        } else {
            filepath = "Closed";
        }
    }

    public String getPathFromUri(Uri uri) {
        if (uri == null) {
            return null;
        }
        if ("content".equals(uri.getScheme())) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
                if (index != -1) {
                    String path = cursor.getString(index);
                    cursor.close();
                    return path;
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
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("audio/*");
            startActivityForResult(intent, 1);
        }catch (ActivityNotFoundException ex){
            filepath = "Not found";
            runOnUiThread(() -> Toast.makeText(context, "Не удалось запустить приложение для выбора музыки", Toast.LENGTH_SHORT).show());
        }
    }
}
