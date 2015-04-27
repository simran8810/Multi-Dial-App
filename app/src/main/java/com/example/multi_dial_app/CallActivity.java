package com.example.multi_dial_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class CallActivity extends Activity {



    EditText note;
    TextView name;
    TextView number;
    RadioGroup rg;
    ToggleButton toggle;

    private final Integer RESCODE = 1;

    private Button submitBtn;

    private Button contBtn;

    private String message;

    private String callNumber;

    private String callName;

    private String feedback = "None";

    private String detailNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_second);

        toggle = (ToggleButton)findViewById(R.id.togglebutton);

        toggle.setChecked(true);

        callNumber = getIntent().getStringExtra("URLKEY");
        callName = getIntent().getStringExtra("name");

        note = (EditText)findViewById(R.id.companyNameEditText);
        name = (TextView)findViewById(R.id.personName);
        number = (TextView)findViewById(R.id.personNumber);
        rg=(RadioGroup)findViewById(R.id.radioGroup);

        name.setText(callName);
        number.setText(callNumber);

        Log.d("name and number", callNumber + " " + callName);


        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+callNumber));

        startActivity(intent);

        submitBtn = (Button)findViewById(R.id.continueCalling);
        contBtn = (Button)findViewById(R.id.next);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(number.getText().equals("")){
                    Toast.makeText(getApplicationContext(),"No Contact to submit", Toast.LENGTH_SHORT).show();
                }
                else{
                    submitForm();
                }



            }
        });

        contBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!number.getText().equals("")){
                    Log.e("number is",number.getText().toString());
                    Toast.makeText(getApplicationContext(),"Please submit previous details", Toast.LENGTH_SHORT).show();
                }
                else{
                    message = "CONTINUE";

                    //returning result
                    Intent intent1 = new Intent();
                    intent1.putExtra("MESSAGE", message);
                    setResult(RESCODE, intent1);
                    finish();//finishing activity
                }




            }
        });

    }

    public void onToggleClicked(View view) {
        // Is the toggle on?
        boolean on = ((ToggleButton) view).isChecked();

        if (on) {
            // Enable vibrate
            contBtn.setEnabled(true);
            submitBtn.setEnabled(true);
            rg.setEnabled(true);

        } else {
            // Disable vibrate
            contBtn.setEnabled(false);
            submitBtn.setEnabled(false);
            rg.setEnabled(false);
//            message = "STOP";
//
//            //returning result
//            Intent intent1= new Intent();
//            intent1.putExtra("MESSAGE",message);
//            setResult(RESCODE,intent1);
//            finish();//finishing activity
        }
    }

    private void submitForm()
    {

        new postCallDetails().execute("http://172.16.64.127:8000/add_details/");

        if (note != null) note.setText("");
        if (name != null) name.setText("");
        if (number != null) number.setText("");
        rg.clearCheck();



    }

    private class postCallDetails extends AsyncTask<String, Void, String> {



        protected String doInBackground(String... urls) {

            int selected = rg.getCheckedRadioButtonId();
            RadioButton rButton = (RadioButton) findViewById(selected);
            if(rButton != null) {
                feedback = String.valueOf(rButton.getText());
            }
            detailNote = String.valueOf(note.getText());


            // Creating HTTP client
            HttpClient httpClient = new DefaultHttpClient();
            // Creating HTTP Post
            HttpPost httpPost = new HttpPost(urls[0]);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("name", callName));
            nameValuePairs.add(new BasicNameValuePair("number", callNumber));
            nameValuePairs.add(new BasicNameValuePair("feedback", feedback));
            nameValuePairs.add(new BasicNameValuePair("detailFeedback",detailNote ));


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
            Log.e("Posting data", st);
            Toast.makeText(getApplicationContext(),"Successfully Submitted", Toast.LENGTH_SHORT).show();


        }


    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_noAnswer:
                if (checked)
                    //Toast.makeText(getApplicationContext(),"No answer", Toast.LENGTH_SHORT).show();
                break;
            case R.id.radio_callLater:
                if (checked)
                    //Toast.makeText(getApplicationContext(),"Call Later", Toast.LENGTH_SHORT).show();
                break;
            case R.id.radio_recieved:
                if (checked)
                    //Toast.makeText(getApplicationContext(),"Call Received", Toast.LENGTH_SHORT).show();
                break;
            case R.id.radio_feedback:
                if (checked)
                    //Toast.makeText(getApplicationContext(),"Feedback", Toast.LENGTH_SHORT).show();
                break;
        }
    }



}
