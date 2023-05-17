package com.smu.dotbuv10.Alarm.Model;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Alarm extends BroadcastReceiver {
    int id;
    int idMed;
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("alarmita");

        this.id=intent.getIntExtra("id",0);
        this.idMed=intent.getIntExtra("idM",0);
        System.out.println(this.id);
        medicamentoselect(context);

    }
    private void medicamentoselect(Context context){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://dotbu.tech/wp-json/wcra/v1/updatemed/" +
                "?secret_key=z7PU5dLP9EqMf5iXLduBqEwUSCR9v8Ws&id_paciente="+this.id+"&id_paciente="+this.idMed+"&FechaInicio="+ SystemClock.elapsedRealtime(), new Response.Listener<String>() {
            int id=this.id;

            @Override
            public void onResponse(String response) {

                    StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://dotbu.tech/wp-json/wcra/v1/selectmedpac/" +
                            "?secret_key=z7PU5dLP9EqMf5iXLduBqEwUSCR9v8Ws&id_paciente="+this.id, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONObject jObj = new JSONObject(response);
                                JSONArray data=jObj.getJSONArray("data");
                                setAlarm(data,context);
                            }
                            catch (JSONException e) {
                                Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
                            }
                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
                        }
                    }){
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> parametros=new HashMap<String,String>();
                            return parametros;
                        }
                    };
                    RequestQueue requestQueue   = Volley.newRequestQueue(context);
                    requestQueue.add(stringRequest);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<String,String>();
                return parametros;
            }
        };
        RequestQueue requestQueue   = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    private void setAlarm( JSONArray data,Context context) {
        //getting the alarm manager
        for(int j =0; j<data.length();j++){
            try {
                JSONObject a= (JSONObject)data.get(j);
                AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                //creating a new intent specifying the broadcast receiver
                Intent i = new Intent(context, Alarm.class);
                i.putExtra("id",id);
                //creating a pending intent using the intent
                PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_ONE_SHOT);
                //setting the repeating alarm that will be fired every day
                am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        a.getInt("FechaInicio") +a.getInt("FrecuenciaSuministro"), pi);
                Toast.makeText(context, "Alarm is set", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
