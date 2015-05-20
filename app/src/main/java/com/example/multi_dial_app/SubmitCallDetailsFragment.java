package com.example.multi_dial_app;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ht on 19/5/15.
 */



public class SubmitCallDetailsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener{

    private static View view;

    private static Switch OnOffSwitch;

    private static TextView name;

    private static TextView number;

    private static RadioGroup rg;

    private static EditText note;

    private static ImageButton submitBtn;

    private static RelativeLayout background;



    private static ArrayList<String> radioButtonChoices;

    private String serverUrl;

    private String radioChoice;

    private static String campaign;

    private static String callNumber;

    private static String callName;

    private static String feedback = "None";

    private static String detailNote;

    private static String lead_id;

    private static String leadName;

    private static String leadNumber;

    private String defaultValue;





    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        background = (RelativeLayout)getActivity().findViewById(R.id.background);



    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.submit_call_details_fragment, container, false);


        serverUrl = getResources().getString(R.string.serverUrl);
        defaultValue = getResources().getString(R.string.defaultUser);
        radioChoice = getResources().getString(R.string.noCampaignChoosen);

        Bundle bundle = getArguments();
        leadName = bundle.getString("name");
        leadNumber = bundle.getString("number");
        lead_id = bundle.getString("lead_id");



        //get running Campaign from SharedPreferences
        SharedPreferences preferences = getActivity().getSharedPreferences("user_info", 0);



        name = (TextView)view.findViewById(R.id.personName);
        name.setText(leadName);
        number = (TextView)view.findViewById(R.id.personNumber);
        number.setText(leadNumber);

        rg=(RadioGroup)view.findViewById(R.id.radioGroup);
        rg.setEnabled(false);

        note = (EditText)view.findViewById(R.id.companyNameEditText);

        submitBtn = (ImageButton)view.findViewById(R.id.continueCalling);
        submitBtn.setEnabled(true);







        //Setting Arraylist for values of radio buttons for selected campaign
        ArrayList<String> myAList=new ArrayList<String>();
        int size=preferences.getInt("choiceSize",0);

        for(int j=0;j<size;j++)
        {
            myAList.add(preferences.getString("choice"+j,"default"));
        }

        radioButtonChoices = new ArrayList<String>();
        radioButtonChoices = myAList;

        // addRadioButtons will create radio buttons on runtime
        addRadioButtons(radioButtonChoices);









        //attach listener to radio group
        rg = (RadioGroup) view.findViewById(R.id.radioGroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {


                RadioButton checkedRadioButton = (RadioButton) view.findViewById(checkedId);

                radioChoice = checkedRadioButton.getText().toString();


            }
        });





        //attach listener to submit detail button
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(number.getText().equals("")){
                    Toast.makeText(getActivity(),"No Contact to submit", Toast.LENGTH_SHORT).show();
                }
                else{

                    if (radioChoice.equals(getResources().getString(R.string.noCampaignChoosen))){

                        Toast.makeText(getActivity(), "please select a feedback", Toast.LENGTH_SHORT).show();
                    }
                    else{

                        new postCallDetails().execute(serverUrl + "add_details/");

                    }

                }

            }
        });

      return view;
    }



    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
        ((CallActivity) activity).setTitle(getString(R.string.submitDetailTitle));

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

            submitBtn.setEnabled(true);

        }else{

            background.setBackgroundColor(getResources().getColor(R.color.grey));

            submitBtn.setEnabled(false);


        }
    }







    //method used to add dynamically radio buttons at runtime
    public void addRadioButtons(ArrayList<String> feedbackChoiceArray) {

        int number = (Integer) feedbackChoiceArray.size();

        for (int row = 0; row < number; row++) {
            RadioGroup rgp= (RadioGroup) view.findViewById(R.id.radioGroup);
            RadioGroup.LayoutParams rprms;

            RadioButton radioButton = new RadioButton((getActivity().getApplicationContext()));
            radioButton.setText(feedbackChoiceArray.get(row));
            radioButton.setTextColor(getResources().getColor(R.color.black));
            radioButton.setBackgroundColor(getResources().getColor(R.color.grey));


            radioButton.setId(row);
            rprms= new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rgp.addView(radioButton, rprms);
        }

    }







    //Async task to submit details of call
    private class postCallDetails extends AsyncTask<String, Void, String> {

        ProgressDialog dialog=new ProgressDialog(getActivity());
        protected void onPreExecute(){
            // create dialog here
            dialog.setCancelable(true);
            dialog.setMessage("Submitting details...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setProgress(0);
            dialog.show();
        }


        protected String doInBackground(String... urls) {

            int selected = rg.getCheckedRadioButtonId();

            RadioButton rButton = (RadioButton) view.findViewById(selected);


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

            if (note != null) note.setText("");
            if (name != null) name.setText("");
            if (number != null) number.setText("");
            Log.e("Posting data", st);
            Toast.makeText(getActivity(),"Successfully Submitted", Toast.LENGTH_SHORT).show();

            Fragment fragment = new LeadDetailFragment();


            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack
            transaction.replace(R.id.mainContent, fragment, "leadDetailFragment");
            getFragmentManager().popBackStack();
//
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();


        }


    }
}
