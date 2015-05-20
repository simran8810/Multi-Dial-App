package com.example.multi_dial_app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ht on 19/5/15.
 */
public class LeadDetailFragment extends Fragment implements CompoundButton.OnCheckedChangeListener{

    private static View view;

    private static ActionBar actionBar;

    private static TextView campaignTextView;

    private static TextView name;

    private static TextView number;

    private static Switch OnOffSwitch;

    private static ImageButton contBtn;

    private static RelativeLayout background;




    private static String callNumber;

    private static String callName;

    private static String lead_id;

    private String serverUrl;

    private static String campaign;

    private String defaultValue;

    private static FragmentTransaction transaction;



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((CallActivity) activity).setTitle(getString(R.string.leadDetailTitle));
    }

    


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        background = (RelativeLayout)getActivity().findViewById(R.id.background);

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.lead_detail_fragment, container, false);



        //get running Campaign from SharedPreferences
        SharedPreferences preferences = getActivity().getSharedPreferences("user_info", 0);
        campaign = preferences.getString(getResources().getString(R.string.runningCampaign), defaultValue);


        serverUrl = getResources().getString(R.string.serverUrl);
        defaultValue = getResources().getString(R.string.defaultUser);


        //initialize widgets
        campaignTextView= (TextView) view.findViewById(R.id.campaignTextView);
        campaignTextView.setText(campaign);


        name = (TextView)view.findViewById(R.id.personName);
        number = (TextView)view.findViewById(R.id.personNumber);


        contBtn = (ImageButton)view.findViewById(R.id.next);
        contBtn.setEnabled(true);



        //attach a onclicklistener to call button
        contBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();

                // Do something after 5s = 5000ms
                Fragment fragment = new SubmitCallDetailsFragment();


                Bundle bundle = new Bundle();
                bundle.putString("name", name.getText().toString());
                bundle.putString("number", number.getText().toString());
                bundle.putString("lead_id", lead_id);

                fragment.setArguments(bundle);

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack();
                transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.mainContent, fragment, "submitDetailFragment");
                //getFragmentManager().popBackStack();

                transaction.addToBackStack(null);

                try {
                    Log.e("thread is sleeping","yes");
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                transaction.commit();




            }
        });



        new getContact().execute(serverUrl);


        return view;
    }






    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        OnOffSwitch = (Switch)menu.findItem(R.id.myswitch).getActionView().findViewById(R.id.switchForActionBar);

        OnOffSwitch .setOnCheckedChangeListener(this);
    }





    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if(isChecked){


            background.setBackgroundColor(getResources().getColor(R.color.accent));

            contBtn.setEnabled(true);

        }else{


            background.setBackgroundColor(getResources().getColor(R.color.grey));

            contBtn.setEnabled(false);


        }
    }


    //call method to start dialer intent
    public void call()
    {

        callName = name.getText().toString();
        callNumber = number.getText().toString();

        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + callNumber));

        startActivity(intent);


    }




    private class getContact extends AsyncTask<String, Void, String> {

        ProgressDialog dialog=new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute(){
            // create dialog here
            dialog.setCancelable(true);
            dialog.setMessage("Reaching server for new contact...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setProgress(0);
            dialog.show();
        }




        protected String doInBackground(String... urls) {

            // Creating HTTP client
            HttpClient httpClient = new DefaultHttpClient();

            // Creating HTTP Post
            HttpPost httpPost = new HttpPost(urls[0]);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("campaign",campaign ));

            // Making HTTP Request
            try {

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                String st  = EntityUtils.toString(entity);
                Log.e("response", st);
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

            JSONObject jsn = null;
            try {

                jsn = new JSONObject(st);
                String myname = jsn.getString("name");
                String mynumber = jsn.getString("number");
                lead_id  = jsn.getString("lead_id");

                if (number.equals("")){
                    Toast.makeText(getActivity(),"No Number To Call", Toast.LENGTH_SHORT).show();
                }
                else{
                    name.setText(myname);
                    number.setText(mynumber);

                }



            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }



}
