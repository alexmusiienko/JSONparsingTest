package com.example.alexm.jsonparsing;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

class MyObject {

    String id;
    String externalId;
    double longitude;
    double latitude;
    double altitude;
    int rank;

    MyObject(JSONObject json) throws JSONException {
        id = json.getString("id");
        externalId = json.getString("external_id");
        longitude = json.getDouble("longitude");
        latitude = json.getDouble("latitude");
        altitude = json.getDouble("altitude");
        rank = json.getInt("rank");
    }

    @Override
    public String toString() {
        return "id : " + id + "\n" +
                "externalID : " + externalId + "\n" +
                "longitude : " + longitude + "\n" +
                "latitude : " + latitude + "\n" +
                "altitude : " + altitude + "\n" +
                "rank : " + rank;
    }
}

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button showTextButton;
    private static String url = "http://samples.openweathermap.org/data/3.0/stations?appid=b1b15e88fa797225412429c1c50c122a1";
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        showTextButton = findViewById(R.id.show_text_button);

        showTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetWeather().execute();
            }
        });
    }

    private class GetWeather extends AsyncTask<Void, Void, Void> {

        MyObject myObject;

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall(url);

            if (jsonStr != null) {
                try {

                    JSONObject c = new JSONObject(jsonStr);
                    myObject = new MyObject(c);

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            textView.setText(myObject.toString());
        }


    }
}
