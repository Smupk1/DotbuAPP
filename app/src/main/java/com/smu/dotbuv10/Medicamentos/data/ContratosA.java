package com.smu.dotbuv10.Medicamentos.data;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class ContratosA {
    public static final String CONTENT_AUTHORITY = "com.smu.dotbuv10";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_VEHICLE = "reminder-path";

    public static final class AlarmReminderEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_VEHICLE);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VEHICLE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VEHICLE;

        public final static String TABLE_NAME = "alertas";

        public final static String _ID = BaseColumns._ID;

        public static final String KEY_TITLE = "titulo";
        public static final String KEY_DATE = "Fecha";
        public static final String KEY_TIME = "Hora";
        public static final String KEY_REPEAT = "Repetir";
        public static final String KEY_REPEAT_NO = "Repetir_no";
        public static final String KEY_REPEAT_TYPE = "Repetir_type";
        public static final String KEY_ACTIVE = "active";

    }

    @SuppressLint("Range")
    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString( cursor.getColumnIndex(columnName) );
    }
}
