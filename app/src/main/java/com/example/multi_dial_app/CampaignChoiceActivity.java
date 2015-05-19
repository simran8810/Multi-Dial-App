package com.example.multi_dial_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import java.util.ArrayList;


public class CampaignChoiceActivity extends Activity {

    private static String serverUrl;

    private String radioChoice ;

    private ArrayList<String> campaignChoices;

    Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.campaign_choices);

        serverUrl=getResources().getString(R.string.serverUrl);

        radioChoice = getResources().getString(R.string.noCampaignChoosen);

        campaignChoices = getIntent().getStringArrayListExtra("CAMPAIGNS");


        addRadioButtons(campaignChoices);

        btn = (Button) findViewById(R.id.chooseCampaign);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (radioChoice.equals(getResources().getString(R.string.noCampaignChoosen))){

                    Toast.makeText(getApplicationContext(), "please select a Campaign", Toast.LENGTH_SHORT).show();
                }

                else{
                    Intent intent = new Intent(getApplicationContext(), CallActivity.class);

                    intent.putExtra("RadioChoice",radioChoice);
                    Toast.makeText(getApplicationContext(), radioChoice, Toast.LENGTH_SHORT).show();

                    startActivity(intent);
                }


            }
        });

        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroupCampaign);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton checkedRadioButton = (RadioButton) findViewById(checkedId);

                radioChoice = checkedRadioButton.getText().toString();


            }
        });


    }



    public void addRadioButtons(ArrayList<String> campaignArray) {

        int number = (Integer) campaignArray.size();

        for (int row = 0; row < number; row++) {
            RadioGroup rgp= (RadioGroup) findViewById(R.id.radioGroupCampaign);
            RadioGroup.LayoutParams rprms;

            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(campaignArray.get(row));
            radioButton.setId(row);
            rprms= new RadioGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            rgp.addView(radioButton, rprms);
        }

    }



}



