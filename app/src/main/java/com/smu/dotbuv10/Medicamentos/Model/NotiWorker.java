package com.smu.dotbuv10.Medicamentos.Model;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.ForegroundInfo;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.smu.dotbuv10.Medicamentos.Controler.AddMed;
import com.smu.dotbuv10.R;

import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class NotiWorker extends Worker {
    private final Context context;
    private static final String KEY_INPUT_URL = "KEY_INPUT_URL";
    private static final String KEY_OUTPUT_FILE_NAME = "KEY_OUTPUT_FILE_NAME";
    private NotificationManager notificationManager;
    public NotiWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context=context;
        notificationManager = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);
    }

    @NonNull
    @Override
    public Result doWork() {
        String tag=getInputData().getString("Tag");
        displayNotification(tag);
        int frecuencia=getInputData().getInt("frecuencia",0);
        Data.Builder data = new Data.Builder();
        data.putString("Tag",tag);
        data.putInt("frecuencia",frecuencia);
        OneTimeWorkRequest mywork = new OneTimeWorkRequest.Builder(NotiWorker.class)
                .setInitialDelay(frecuencia, TimeUnit.HOURS).addTag(tag).setInputData(data.build())
                .build();
        WorkManager.getInstance(this.context).enqueue(mywork);

        return Result.success();
    }
    private void displayNotification(String keyword) {

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("simplifiedcoding", "simplifiedcoding", NotificationManager.IMPORTANCE_DEFAULT);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "simplifiedcoding")
                .setContentTitle("Recordatorio tomar Medicamento  : " + keyword)
                .setContentText("Recuerda que tu paciente debe tomar su medicamento")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(android.R.drawable.stat_notify_error);

        assert notificationManager != null;
        notificationManager.notify(1, builder.build());

    }


}
