package com.example.multi_dial_app;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CallActivity extends ActionBarActivity {

    private static RelativeLayout lLayout;
    private static ToggleButton toggle;
    private static TextView name;
    private static TextView number;
    private static RadioGroup rg;
    private static EditText note;
    private Switch toggleSwitch;
    private TextView campaignTextView;

    private Button submitBtn;

    private Button contBtn;

    private static String callNumber;

    private static String callName;

    private static String feedback = "None";

    private static String detailNote;

    private static String lead_id;

    private static String callerName;

    private static String serverUrl;

    private static ArrayList<String> radioButtonChoices;

    private static String campaign;

    private static String radioChoice ;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(true);


        lLayout = (RelativeLayout) findViewById(R.id.background);


        serverUrl=getResources().getString(R.string.serverUrl);
        radioChoice = getResources().getString(R.string.noCampaignChoosen);
        final String defaultValue = getResources().getString(R.string.defaultUser);


        SharedPreferences preferences = getApplicationContext().getSharedPreferences("user_info", 0);
        callerName = preferences.getString(getResources().getString(R.string.callerName),defaultValue);


        campaign = preferences.getString(getResources().getString(R.string.runningCampaign), defaultValue);

        ArrayList<String> myAList=new ArrayList<String>();
        int size=preferences.getInt("choiceSize",0);

        for(int j=0;j<size;j++)
        {
            myAList.add(preferences.getString("choice"+j,"default"));
        }


        radioButtonChoices = new ArrayList<String>();
        radioButtonChoices = myAList;
        addRadioButtons(radioButtonChoices);


        campaignTextView= (TextView) findViewById(R.id.campaignTextView);
        campaignTextView.setText("Campaign: "+campaign);
        toggleSwitch = (Switch) findViewById(R.id.togglebutton);
        toggleSwitch.setChecked(true);

        //toggle = (ToggleButton)findViewById(R.id.togglebutton);
        //toggle.setChecked(true);


        name = (TextView)findViewById(R.id.personName);
        number = (TextView)findViewById(R.id.personNumber);
        rg=(RadioGroup)findViewById(R.id.radioGroup);
        rg.setEnabled(false);
        note = (EditText)findViewById(R.id.companyNameEditText);

        submitBtn = (Button)findViewById(R.id.continueCalling);

        contBtn = (Button)findViewById(R.id.next);
        contBtn.setEnabled(false);


        makeCall();

        //attach a listener to check for changes in state
        toggleSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    Toast.makeText(getApplicationContext(),"Switch is currently ON", Toast.LENGTH_SHORT).show();
                    // Enable screen

                    if(!number.getText().equals("")){
                        contBtn.setEnabled(false);
                        submitBtn.setEnabled(true);

                    }
                    else{
                        contBtn.setEnabled(true);
                        submitBtn.setEnabled(false);
                    }

                    rg.setEnabled(true);
                    lLayout.setBackgroundColor(getResources().getColor(R.color.white));

                }else{
                    Toast.makeText(getApplicationContext(),"Switch is currently OFF", Toast.LENGTH_SHORT).show();
                    // Disable screen
                    contBtn.setEnabled(false);
                    submitBtn.setEnabled(false);
                    rg.setEnabled(false);
                    lLayout.setBackgroundColor(getResources().getColor(R.color.grey));
                }

            }
        });


        if(toggleSwitch.isChecked()){
            Toast.makeText(getApplicationContext(),"I am ON", Toast.LENGTH_SHORT).show();
            if(!number.getText().equals("")){
                contBtn.setEnabled(false);
                submitBtn.setEnabled(true);

            }
            else{
                contBtn.setEnabled(true);
                submitBtn.setEnabled(false);
            }

            rg.setEnabled(true);
            lLayout.setBackgroundColor(getResources().getColor(R.color.white));
        }
        else {
            Toast.makeText(getApplicationContext(),"I am  OFF", Toast.LENGTH_SHORT).show();
            // Disable screen
            contBtn.setEnabled(false);
            submitBtn.setEnabled(false);
            rg.setEnabled(false);
            lLayout.setBackgroundColor(getResources().getColor(R.color.grey));
        }
/*
        public void onToggleClicked(View view) {
            // Is the toggle on?
            boolean on = ((ToggleButton) view).isChecked();

            if (on) {
                // Enable screen

                if(!number.getText().equals("")){
                    contBtn.setEnabled(false);
                    submitBtn.setEnabled(true);

                }
                else{
                    contBtn.setEnabled(true);
                    submitBtn.setEnabled(false);
                }

                rg.setEnabled(true);
                lLayout.setBackgroundColor(getResources().getColor(R.color.white));

            } else {
                // Disable screen
                contBtn.setEnabled(false);
                submitBtn.setEnabled(false);
                rg.setEnabled(false);
                lLayout.setBackgroundColor(getResources().getColor(R.color.grey));
            }
        }

*/
        rg = (RadioGroup) findViewById(R.id.radioGroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                Log.e("presed",String.valueOf(checkedId));
                RadioButton checkedRadioButton = (RadioButton) findViewById(checkedId);

                radioChoice = checkedRadioButton.getText().toString();


            }
        });



        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    if(number.getText().equals("")){
                        Toast.makeText(getApplicationContext(),"No Contact to submit", Toast.LENGTH_SHORT).show();
                    }
                    else{

                        if (radioChoice.equals(getResources().getString(R.string.noCampaignChoosen))){

                            Toast.makeText(getApplicationContext(), "please select a feedback", Toast.LENGTH_SHORT).show();
                        }
                        else{

                            submitForm();

                        }

                    }






            }
        });



        contBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!number.getText().equals("")){

                    Toast.makeText(getApplicationContext(),"Please submit previous details", Toast.LENGTH_SHORT).show();

                }
                else{
                    submitBtn.setEnabled(false);
                    contBtn.setEnabled(false);

                    makeCall();
                }




            }
        });


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(com.example.multi_dial_app.R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem score = menu.findItem(R.id.telecallername);
        score.setTitle(callerName);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "settings", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.logout:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);

    }






    public void addRadioButtons(ArrayList<String> feedbackChoiceArray) {

        int number = (Integer) feedbackChoiceArray.size();

        for (int row = 0; row < number; row++) {
            RadioGroup rgp= (RadioGroup) findViewById(R.id.radioGroup);
            RadioGroup.LayoutParams rprms;

            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(feedbackChoiceArray.get(row));

            radioButton.setId(row);
            Log.e("xxx", String.valueOf(row));
            rprms= new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rgp.addView(radioButton, rprms);
        }

    }


    private void submitForm()

    {

        new postCallDetails().execute(serverUrl+"add_details/");

    }

    private void makeCall()

    {

        new getContact().execute(serverUrl);

    }





    public void call()
    {


        callName = name.getText().toString();
        callNumber = number.getText().toString();

        submitBtn.setEnabled(true);
        contBtn.setEnabled(false);

        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+callNumber));




        startActivity(intent);
    }






    private class getContact extends AsyncTask<String, Void, String> {

        ProgressDialog dialog=new ProgressDialog(CallActivity.this);
        protected void onPreExecute(){
            // create dialog here
            dialog.setMessage("Please wait..");
            dialog.show();
        }




        protected String doInBackground(String... urls) {
            // Creating HTTP client
            HttpClient httpClient = new DefaultHttpClient();
            // Creating HTTP Post
            HttpPost httpPost = new HttpPost(urls[0]);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("campaign",campaign ));

            Log.e("campoign",campaign);

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
            Log.e("Http Response:", st);

            JSONObject jsn = null;
            try {
                jsn = new JSONObject(st);
                String myname = jsn.getString("name");
                String mynumber = jsn.getString("number");
                lead_id  = jsn.getString("lead_id");

                if (number.equals("")){
                    Toast.makeText(getApplicationContext(),"No Number To Call", Toast.LENGTH_SHORT).show();
                }
                else{
                    name.setText(myname);
                    number.setText(mynumber);

                    call();
                    submitBtn.setEnabled(true);
                    contBtn.setEnabled(false);

                }



            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }

    private class postCallDetails extends AsyncTask<String, Void, String> {

        ProgressDialog dialog=new ProgressDialog(CallActivity.this);
        protected void onPreExecute(){
            // create dialog here
            dialog.setMessage("Please wait..");
            dialog.show();
        }


        protected String doInBackground(String... urls) {

            int selected = rg.getCheckedRadioButtonId();
            RadioButton rButton = (RadioButton) findViewById(selected);
            if(rButton != null) {
                feedback = String.valueOf(rButton.getText());
            }
            detailNote = String.valueOf(note.getText());

            callName = name.getText().toString();
            callNumber = number.getText().toString();


            // Creating HTTP client
            HttpClient httpClient = new DefaultHttpClient();
            // Creating HTTP Post
            HttpPost httpPost = new HttpPost(urls[0]);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("lead_id",lead_id ));
            nameValuePairs.add(new BasicNameValuePair("name", callName));
            nameValuePairs.add(new BasicNameValuePair("number", callNumber));
            nameValuePairs.add(new BasicNameValuePair("feedback", feedback));
            nameValuePairs.add(new BasicNameValuePair("detailFeedback",detailNote ));

            Log.e("xxx",lead_id+" "+callName+" "+callNumber+" "+feedback+" "+detailNote);


            // Making HTTP Request
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpClient.execute(httpPost);

                Log.e("response post",response.toString());
                return response.toString();

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

            submitBtn.setEnabled(false);
            contBtn.setEnabled(true);

            if (note != null) note.setText("");
            if (name != null) name.setText("");
            if (number != null) number.setText("");
            Log.e("Posting data", st);
            Toast.makeText(getApplicationContext(),"Successfully Submitted", Toast.LENGTH_SHORT).show();


        }


    }







}
