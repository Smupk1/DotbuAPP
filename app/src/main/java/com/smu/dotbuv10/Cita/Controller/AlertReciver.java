package com.smu.dotbuv10.Cita.Controller;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.smu.dotbuv10.R;

public class AlertReciver extends BroadcastReceiver {
    private Context context;
    private static final String KEY_INPUT_URL = "KEY_INPUT_URL";
    private static final String KEY_OUTPUT_FILE_NAME = "KEY_OUTPUT_FILE_NAME";
    private NotificationManager notificationManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        notificationManager = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);
        displayNotification("asdasdadacas");

    }

    private void displayNotification(String keyword) {

        NotificationManager notificationManager = (NotificationManager) context.getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("simplifiedcoding", "simplifiedcoding", NotificationManager.IMPORTANCE_DEFAULT);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), "simplifiedcoding")
                .setContentTitle("Recordatorio Cita medica paciente  : " + keyword)
                .setContentText("Recuerda que tu paciente debe asistir a su cita medica en:"+""+"a las:")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(android.R.drawable.stat_notify_error);

        assert notificationManager != null;
        notificationManager.notify(1, builder.build());

    }
}
