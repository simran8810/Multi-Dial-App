package com.example.multi_dial_app;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.app.Activity;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class MainActivity extends Activity {

    private static String numbers;

    private Integer i =0;

    private final Integer RESCODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numbers = "tel:8765342756";


        Button btn = (Button)findViewById(R.id.startCalling);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                makeACall();


            }
        });




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void makeACall()
    {

        new getNewContact().execute("http://172.16.64.127:8000/");



    }



    private class getNewContact extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            // Showing progress dialog
            ProgressDialog pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... urls) {
            // Creating HTTP client
            HttpClient httpClient = new DefaultHttpClient();
            // Creating HTTP Post
            HttpPost httpPost = new HttpPost(urls[0]);

            // Making HTTP Request
            try {
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                String st  = EntityUtils.toString(entity);
                return st;
            } catch (ClientProtocolException e) {
                // writing exception to log
                e.printStackTrace();
            } catch (IOException e) {
                // writing exception to log
                e.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onPostExecute(String st) {
            Log.e("Http Response:", st);

            JSONObject jsn = null;
            try {
                jsn = new JSONObject(st);
                String name = jsn.getString("name");
                String number = jsn.getString("number");

                numbers = (number);
                String url =  numbers;

                Intent intent = new Intent(getApplicationContext(), CallActivity.class);

                intent.putExtra("URLKEY",url);
                intent.putExtra("name",name);

                // startActivity(intent);

                startActivityForResult(intent, RESCODE);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to

        if (requestCode == RESCODE) {
            // Make sure the request was successful
            String message = data.getStringExtra("MESSAGE");


            if (message.equals("STOP")) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                //now no more calls will be made, otherwise let it pass
            }
            else if(message.equals("CONTINUE"))
            {

                    makeACall();

            }

        }

    }


}
