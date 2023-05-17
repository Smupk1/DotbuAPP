package com.smu.dotbuv10.Medicamentos.data;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class AlarmReminderDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "alarmreminder.db";

    private static final int DATABASE_VERSION = 1;

    public AlarmReminderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a String that contains the SQL statement to create the reminder table
        String SQL_CREATE_ALARM_TABLE =  "CREATE TABLE " + ContratosA.AlarmReminderEntry.TABLE_NAME + " ("
                + ContratosA.AlarmReminderEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ContratosA.AlarmReminderEntry.KEY_TITLE + " TEXT NOT NULL, "
                + ContratosA.AlarmReminderEntry.KEY_DATE + " TEXT NOT NULL, "
                + ContratosA.AlarmReminderEntry.KEY_TIME + " TEXT NOT NULL, "
                + ContratosA.AlarmReminderEntry.KEY_REPEAT + " TEXT NOT NULL, "
                + ContratosA.AlarmReminderEntry.KEY_REPEAT_NO + " TEXT NOT NULL, "
                + ContratosA.AlarmReminderEntry.KEY_REPEAT_TYPE + " TEXT NOT NULL, "
                + ContratosA.AlarmReminderEntry.KEY_ACTIVE + " TEXT NOT NULL " + " );";

        // Execute the SQL statement
        sqLiteDatabase.execSQL(SQL_CREATE_ALARM_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
