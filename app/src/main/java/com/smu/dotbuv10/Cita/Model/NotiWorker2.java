package com.smu.dotbuv10.Cita.Model;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.smu.dotbuv10.R;

import java.util.concurrent.TimeUnit;

public class NotiWorker2 extends Worker {
    private final Context context;
    private static final String KEY_INPUT_URL = "KEY_INPUT_URL";
    private static final String KEY_OUTPUT_FILE_NAME = "KEY_OUTPUT_FILE_NAME";
    private NotificationManager notificationManager;
    public NotiWorker2(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context=context;
        notificationManager = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);
    }

    @NonNull
    @Override
    public Result doWork() {
        String tag=getInputData().getString("Tag");
        String key=getInputData().getString("key");
        String key2=getInputData().getString("key2");
        displayNotification(tag,key,key2);

        return Result.success();
    }
    private void displayNotification(String keyword,String key,String key2) {

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("simplifiedcoding", "simplifiedcoding", NotificationManager.IMPORTANCE_DEFAULT);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "simplifiedcoding")
                .setContentTitle("Recordatorio Cita medica : " )
                .setContentText("Recuerda que tu paciente"+ keyword+"\n"+" debe asistir a su cita medica en:"+key+"\n"+"a las:"+key2)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(android.R.drawable.stat_notify_error);

        assert notificationManager != null;
        notificationManager.notify(1, builder.build());

    }


}
