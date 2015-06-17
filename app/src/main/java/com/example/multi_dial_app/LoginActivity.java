package com.example.multi_dial_app;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends Activity {


    Button btn;

    EditText name;

    EditText pwd;

    private String username;

    private String userpassword;

    private static String serverUrl;

    private static final String feedbackChoices = "feedbackChoices";
    private static final String campaign = "campaign";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_screen_activity);

        /********************************************************/


        serverUrl=getResources().getString(R.string.serverUrl);



        btn = (Button) findViewById(R.id.startCalling);
        name = (EditText) findViewById(R.id.login_email);
        pwd = (EditText) findViewById(R.id.login_password);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new authUser().execute(serverUrl+"auth_user/");

            }
        });


    }


    private class authUser extends AsyncTask<String, Void, String> {


        ProgressDialog dialog=new ProgressDialog(LoginActivity.this);
        @Override
        protected void onPreExecute(){

            // create dialog here
            //super.onPreExecute();
            dialog.setMessage("Authenticating...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setProgress(0);
            dialog.show();

        }


        @Override
        protected String doInBackground(String... urls) {

            username = name.getText().toString();
            userpassword  = pwd.getText().toString();

            // Creating HTTP client
            HttpClient httpClient = new DefaultHttpClient();
            // Creating HTTP Post
            HttpPost httpPost = new HttpPost(urls[0]);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("name", username));
            nameValuePairs.add(new BasicNameValuePair("password", userpassword));


            // Making HTTP Request
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
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

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            try{
                Log.e("Posting data", st);
                if (st.equals("failure")){
                    Toast.makeText(getApplicationContext(), "Wrong Credentials", Toast.LENGTH_SHORT).show();
                }
                else {
                    try{
                        SharedPreferences preferences = getApplicationContext().getSharedPreferences("user_info", 0);
                        SharedPreferences.Editor editor = preferences.edit();

                        JSONObject jsn = null;
                        jsn = new JSONObject(st);

                        JSONArray jsonArray = jsn.getJSONArray("feedbackChoices");
                        String campaignObject = jsn.getString("campaign");
                       // String recording = jsn.getString("call_recording");

                        ArrayList<String> feedbackChoiceList = new ArrayList<String>();
                        for (int i=0; i<jsonArray.length(); i++) {
                            feedbackChoiceList.add(jsonArray.getString(i));
                        }

                        for(int i=0;i<feedbackChoiceList.size();i++)
                        {
                            editor.putString("choice"+i,feedbackChoiceList.get(i));
                        }
                        editor.putInt("choiceSize",feedbackChoiceList.size());
                        editor.putBoolean(getResources().getString(R.string.isLoggedIn), true);
                        editor.putString(getResources().getString(R.string.callerName), username);
                        editor.putString(getResources().getString(R.string.runningCampaign),String.valueOf(campaignObject));
                        //editor.putString(getResources().getString(R.string.recording),String.valueOf(recording));
                        editor.apply();

                        String camp = preferences.getString(getResources().getString(R.string.runningCampaign),"helo");
                        Log.e("xxx",camp);


                        Intent intent = new Intent(getApplicationContext(), CallActivity.class);

                        startActivity(intent);
                    }

                    catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "Some error occured", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }


                }





            }

            catch (Exception e){

                Toast.makeText(getApplicationContext(), "Server is down", Toast.LENGTH_SHORT).show();

            }






        }


    }
}



