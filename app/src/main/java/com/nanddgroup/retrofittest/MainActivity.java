package com.nanddgroup.retrofittest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {
    private final String KEY = "trnsl.1.1.20160229T183118Z.dc036c8647387496.c3a503e26ee9b6e7f9ee366e6f81697a134005ea";
    private EditText text;
    private EditText translated;
    private Button bTranslate;
    private MyAsyncTask at;

    private Gson gson = new GsonBuilder().create();

    private Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl("https://translate.yandex.net")
            .build();

    private Link intf = retrofit.create(Link.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (EditText) findViewById(R.id.editText);
        translated = (EditText) findViewById(R.id.editText2);
        bTranslate = (Button) findViewById(R.id.bTranslate);
        at = new MyAsyncTask();


        bTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    at.execute();
            }
        });
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Map<String, String>> {
        String string = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            string = text.getText().toString();

        }

        @Override
        protected Map<String, String> doInBackground(Void... params) {
            Map<String, String> mapJson = new HashMap<String, String>();
            mapJson.put("key", KEY);
            mapJson.put("text", string);
            mapJson.put("lang", "en-ru");
            mapJson.put("format", "plain");
            Call<Object> call = intf.translate(mapJson);
            Response<Object> response = null;
            try {
                response = call.execute();
                Log.d("responseTAG", String.valueOf(response.code()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Map<String, String> map = gson.fromJson(response.body().toString(), Map.class);

            return map;
        }

        @Override
        protected void onPostExecute(Map<String, String> aVoid) {
            super.onPostExecute(aVoid);
            for (Map.Entry e : aVoid.entrySet()) {
                if (e.getKey().equals("text")) {
                    translated.setText(e.getValue().toString());
                    System.out.println(e.getKey().toString() + " " + e.getValue().toString());
                }
            }
            Toast.makeText(getApplicationContext(), "onPostExecte", Toast.LENGTH_SHORT).show();
        }
    }
}
