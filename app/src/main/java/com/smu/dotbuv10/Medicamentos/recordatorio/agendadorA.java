package com.smu.dotbuv10.Medicamentos.recordatorio;
import static android.app.PendingIntent.FLAG_MUTABLE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

public class agendadorA {
    /**
     * Schedule a reminder alarm at the specified time for the given task.
     *
     * @param context Local application or activity context

     * @param reminderTask Uri referencing the task in the content provider
     */

    public void setAlarm(Context context, long alarmTime, Uri reminderTask) {
        Log.w("alarm","setting");
        AlarmManager manager = ProveedorMang.getAlarmManager(context);

        PendingIntent operation =
                ServicioA.getReminderPendingIntent(context, reminderTask);


        if (Build.VERSION.SDK_INT >= 23) {
            Log.w("alarm","setting 4 23");
            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, operation);

        } else if (Build.VERSION.SDK_INT >= 19) {
            Log.w("alarm","setting 2 19");
            manager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, operation);

        } else {

            manager.set(AlarmManager.RTC_WAKEUP, alarmTime, operation);

        }
    }

    public void setRepeatAlarm(Context context, long alarmTime, Uri reminderTask, long RepeatTime) {
        AlarmManager manager = ProveedorMang.getAlarmManager(context);

        PendingIntent operation =
                ServicioA.getReminderPendingIntent(context, reminderTask);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, RepeatTime, operation);


    }

    public void cancelAlarm(Context context, Uri reminderTask) {
        AlarmManager manager = ProveedorMang.getAlarmManager(context);

        PendingIntent operation =
                ServicioA.getReminderPendingIntent2(context, reminderTask);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Log.e("alarma","eliminado");
            alarmManager.cancel(operation);

    }
}
