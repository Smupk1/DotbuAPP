package com.smu.dotbuv10.Medicamentos.Controler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.smu.dotbuv10.Medicamentos.Adaptor.AdaptadorMeds;
import com.smu.dotbuv10.Medicamentos.AniadirRecord;

import com.smu.dotbuv10.Medicamentos.data.ContratosA;
import com.smu.dotbuv10.R;
import android.app.LoaderManager;

import android.content.ContentUris;
import android.content.CursorLoader;

import android.content.Loader;

import android.net.Uri;

import android.widget.AdapterView;

public class Medicamentos extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    SharedPreferences preferences;
    Button añadir;

    private ListView reminderListView;
    private AdaptadorMeds adaptador;
    private static final int VEHICLE_LOADER = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicamentos);
        preferences =getSharedPreferences("preferences",MODE_PRIVATE);
        reminderListView = (ListView) findViewById(R.id.lvIMeds);
        adaptador = new AdaptadorMeds(this, null);
        reminderListView.setAdapter(adaptador);

        reminderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(Medicamentos.this, AniadirRecord.class);

                Uri currentVehicleUri = ContentUris.withAppendedId(ContratosA.AlarmReminderEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentVehicleUri);

                startActivity(intent);

            }
        });

        añadir=findViewById(R.id.addMed);
        añadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AniadirRecord.class);
                startActivity(intent);
            }
        });
        getLoaderManager().initLoader(VEHICLE_LOADER, null, this);
    }
    @Override
    public void onResume(){
        super.onResume();

    }
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                ContratosA.AlarmReminderEntry._ID,
                ContratosA.AlarmReminderEntry.KEY_TITLE,
                ContratosA.AlarmReminderEntry.KEY_DATE,
                ContratosA.AlarmReminderEntry.KEY_TIME,
                ContratosA.AlarmReminderEntry.KEY_REPEAT,
                ContratosA.AlarmReminderEntry.KEY_REPEAT_NO,
                ContratosA.AlarmReminderEntry.KEY_REPEAT_TYPE,
                ContratosA.AlarmReminderEntry.KEY_ACTIVE

        };

        return new CursorLoader(this,   // Parent activity context
                ContratosA.AlarmReminderEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adaptador.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adaptador.swapCursor(null);
    }


}