package com.example.alexm.jsonparsing;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class MyObject {

    int id;
    String survey;
    String description;



    MyObject(JSONObject json) throws JSONException {
        id = json.getInt("idSurvey");
        survey = json.getString("survey");
        description = json.getString("description");

    }

    @Override
    public String toString() {
        return "id: " + id + "\n" +
                "survey: " + survey + "\n" +
                "description: " + description;
    }
}

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button showTextButton;
    private static  String url = "http://192.168.1.107:9090/api/tests/info";
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

        ArrayList<MyObject> objects = new ArrayList<>();

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall(url);

            if (jsonStr != null) {
                try {

                    JSONArray c = new JSONArray(jsonStr);
                    for(int i = 0; i < c.length(); i++) {
                        MyObject obj = new MyObject(c.getJSONObject(i));
                        objects.add(obj);
                    }
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
            textView.setText(objects.toString());
        }


    }
}
