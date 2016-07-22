package com.example.sobin.meetup;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sobin on 7/19/2016.
 */
public class JsonParse extends Activity implements LocationListener {
    LocationManager locationmanager;

    TextView output;
    String loginURL = "https://api.meetup.com/2/groups";
    String data = "";


    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json);
        if (savedInstanceState != null) {
            Log.d("STATE", savedInstanceState.toString());
            locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria cri = new Criteria();
            String provider = locationmanager.getBestProvider(cri, false);

            if (provider != null & !provider.equals("")) {
                try {
                    Location location = locationmanager.getLastKnownLocation(provider);
                    locationmanager.requestLocationUpdates(provider, 2000, 1, this);
                    if (location != null) {
                        onLocationChanged(location);
                    } else {
                        Toast.makeText(getApplicationContext(), "location not found", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {

                }
            } else {
                Toast.makeText(getApplicationContext(), "Provider is null", Toast.LENGTH_LONG).show();
            }
        }

 String url = loginURL+"?req=lat="+lati+"&lon="+lngi+"&page=1&key=5d11401a1335801321166722396f42;
        requestQueue = Volley.newRequestQueue(this);
        output = (TextView) findViewById(R.id.jsonData);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONArray ja = response.getJSONArray("posts");
                            for (int j = 0; j < ja.length(); j++) {

                                JSONObject jsonObject = ja.getJSONObject(j);
                                String city = jsonObject.getString("city");
                                String country = jsonObject.getString("country");

                                for (int i = 0; i < ja.length(); i++) {

                                    JSONObject jsonObject1 = ja.getJSONObject(i);
                                    String urlkey = jsonObject1.getString("urlkey");
                                    String name = jsonObject1.getString("name");
                                    int id = Integer.parseInt(jsonObject1.getString("id").toString());

                                    data += "\n" + urlkey + " \n = " + name + " \n  " + country + "\n" + city + "" + " \n\n\n\n ";
                                }
                            }

                            output.setText(data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");

                    }
                }
        );
        requestQueue.add(jor);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void onLocationChanged(Location location) {

        double lati = location.getLatitude();
        double lngi = location.getLongitude();
    }
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }
    @Override
    public void onProviderEnabled(String s) {
    }
    @Override
    public void onProviderDisabled(String s) {
    }
}
