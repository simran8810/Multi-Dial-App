package com.example.multi_dial_app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

public class CallActivity extends Activity {


    private final Integer RESCODE = 1;

    private Button contBtn;

    private Button stopBtn;

    private String message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_second);



        String url = getIntent().getStringExtra("URLKEY");


        Log.d("url",url);


        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));

        startActivity(intent);

        contBtn = (Button)findViewById(R.id.continueCalling);
        stopBtn = (Button)findViewById(R.id.stopCalling);

        contBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                message = "CONTINUE";

                //returning result
                Intent intent1 = new Intent();
                intent1.putExtra("MESSAGE", message);
                setResult(RESCODE, intent1);
                finish();//finishing activity


            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                message = "STOP";

                //returning result
                Intent intent1= new Intent();
                intent1.putExtra("MESSAGE",message);
                setResult(RESCODE,intent1);
                finish();//finishing activity

            }
        });



    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_doIt:
                if (checked)
                    Toast.makeText(getApplicationContext(),"yes checked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.radio_dontDoIt:
                if (checked)
                    Toast.makeText(getApplicationContext(),"no checked", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
