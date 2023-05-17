package com.smu.dotbuv10.Medicamentos.recordatorio;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.smu.dotbuv10.Medicamentos.AniadirRecord;
import com.smu.dotbuv10.Medicamentos.Controler.Medicamentos;
import com.smu.dotbuv10.Medicamentos.data.ContratosA;
import com.smu.dotbuv10.R;


public class ServicioA extends IntentService{
    private static final String TAG = ServicioA.class.getSimpleName();

    private static final int NOTIFICATION_ID = 42;

    Cursor cursor;
    //This is a deep link intent, and needs the task stack
    public static PendingIntent getReminderPendingIntent(Context context, Uri uri) {
        Intent action = new Intent(context, ServicioA.class);
        action.setData(uri);
        Log.w("alarm","get remider");
        return PendingIntent.getService(context, 0, action, PendingIntent.FLAG_IMMUTABLE);
    }
    public static PendingIntent getReminderPendingIntent2(Context context, Uri uri) {
        Intent action = new Intent(context, ServicioA.class);
        action.setData(uri);
        Log.w("alarm","get remider2");
        return PendingIntent.getService(context, 0, action, PendingIntent.FLAG_IMMUTABLE);
    }

    public ServicioA() {
        super(TAG);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onHandleIntent(Intent intent) {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// The id of the channel.
        String id = "my_channel_01";

// The user-visible name of the channel.
        CharSequence name = "Recordatorios";

// The user-visible description of the channel.
        String description = "recordatorio de medicamentos";

        int importance = NotificationManager.IMPORTANCE_LOW;

        NotificationChannel mChannel = new NotificationChannel(id, name,importance);

// Configure the notification channel.
        mChannel.setDescription(description);

        mChannel.enableLights(true);
// Sets the notification light color for notifications posted to this
// channel, if the device supports this feature.
        mChannel.setLightColor(Color.RED);

        mChannel.enableVibration(true);
        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

        mNotificationManager.createNotificationChannel(mChannel);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Uri uri = intent.getData();

        //Display a notification to view the task details
        Intent action = new Intent(this, Medicamentos.class);
        action.setData(uri);
        PendingIntent operation = TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(action)
                .getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE);

        //Grab the task description
        if(uri != null){
            cursor = getContentResolver().query(uri, null, null, null, null);
        }

        String description2 = "";
        try {
            if (cursor != null && cursor.moveToFirst()) {
                description2 = ContratosA.getColumnString(cursor, ContratosA.AlarmReminderEntry.KEY_TITLE);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Notification note = new NotificationCompat.Builder(this)
                .setContentTitle("Tomar medicina")
                .setContentText(description2)
                .setSmallIcon(R.drawable.ic_add_alert_black_24dp)
                .setContentIntent(operation)
                .setAutoCancel(true)
                .setChannelId(id)
                .build();

        manager.notify(NOTIFICATION_ID, note);
    }
}
