package com.example.multi_dial_app;

import android.os.Bundle;
import android.view.Menu;
import android.app.Activity;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;


public class MainActivity extends Activity {

    private ArrayList<String> numbers;

    private Integer i =0;

    private final Integer RESCODE = 1;

    private int prevState = TelephonyManager.CALL_STATE_IDLE;

    private int currentState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numbers = new ArrayList<>();

        numbers.add("tel:8765342756");
        numbers.add("tel:9785632764");
        numbers.add("tel:7853624567");


        //TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        Button btn = (Button)findViewById(R.id.startCalling);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //while(i<numbers.size())
                //{
                makeACall();

                //}

            }
        });



        //TelephonyMgr.listen(new TeleListener(),
        //          PhoneStateListener.LISTEN_CALL_STATE);




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    /*
        class TeleListener extends PhoneStateListener {
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);



                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE:
                        // CALL_STATE_IDLE;

                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        // CALL_STATE_OFFHOOK;

                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        // CALL_STATE_RINGING

                        break;
                    default:
                        break;
                }


                currentState = state;


            }

        }



        public void check()
        {
            if((currentState == TelephonyManager.CALL_STATE_IDLE) && (prevState != TelephonyManager.CALL_STATE_IDLE))
            {
                if(i<numbers.size())
                    makeACall();
                else
                    finish();
            }

            prevState = currentState;

        }
    */
    public void makeACall()
    {

        String url =  numbers.get(i);

        Log.d("mainurl",url);

        i++;

        Intent intent = new Intent(getApplicationContext(), CallActivity.class);

        intent.putExtra("URLKEY",url);

        // startActivity(intent);

        startActivityForResult(intent, RESCODE);


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
                if(i<numbers.size()) {
                    makeACall();
                }
                else
                    i=0;
            }

        }

    }

}
