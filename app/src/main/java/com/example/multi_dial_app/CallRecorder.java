package com.example.multi_dial_app;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;




class CallRecorder extends PhoneStateListener {


    private Context mContext;

    private static MediaRecorder Callrecorder;

    private int deviceCallVol; private AudioManager audioManager;

    private int prevState = TelephonyManager.CALL_STATE_IDLE;

    private int currentState;

    private static boolean on_record;


    public CallRecorder(Context context) {
        mContext = context;
    }



    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);





        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
//                currentState = TelephonyManager.CALL_STATE_IDLE;
//                // CALL_STATE_IDLE;
//
               //Toast.makeText(mContext, "phone is neither ringing nor in a call",
                       //Toast.LENGTH_LONG).show();
                if((Callrecorder!=null && currentState == TelephonyManager.CALL_STATE_IDLE) && (prevState != TelephonyManager.CALL_STATE_IDLE)){
                    audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, deviceCallVol, 0);
                    Callrecorder.stop();
                    Callrecorder.reset();
                    Callrecorder.release();
                    Callrecorder = null;
                    on_record = false;

                    Uri file = Uri.fromFile(
                            new File(Environment.getExternalStorageDirectory().getAbsolutePath()));
                    Toast.makeText(mContext,
                            "record stored at " + file.toString(),
                            Toast.LENGTH_LONG).show();

                    //mContext.finish();
                }



                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:

                Callrecorder = new MediaRecorder();

                audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

                //audioManager.setSpeakerphoneOn(true);

                //audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                //audioManager.setStreamVolume(AudioManager.MODE_IN_CALL, audioManager.getStreamMaxVolume(AudioManager.MODE_IN_CALL), 1);

                prevState = TelephonyManager.CALL_STATE_OFFHOOK;

                //String currentAudioPath = getFilesDir().getAbsolutePath()+File.separator+"callrec/";

                String currentAudioPath = Environment.getExternalStorageDirectory().getAbsolutePath();


                //Toast.makeText(mContext, currentAudioPath+File.separator +"telecaller.3gp",
                     //   Toast.LENGTH_LONG).show();


                on_record = true;
                Callrecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
                Callrecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                Callrecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                Callrecorder.setOutputFile(currentAudioPath+File.separator +"telecaller.3gp");
                //Callrecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                Callrecorder.setAudioEncodingBitRate(16);
                Callrecorder.setAudioSamplingRate(44100);


                try {
                    Callrecorder.prepare();
                } catch (IllegalStateException e) {
                    System.out.println("Error is happened here in Prepare Method1");
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {

                    //throwing I/O Exception
                    System.out.println("Error is happened here in Prepare Method2");
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try{
                    Callrecorder.start();
                }catch(IllegalStateException e){
                    Log.e("Error is this", e.toString());
                    e.printStackTrace();
                    //Here it is thorowing illegal State exception
                    System.out.println("Error is happened here in Start Method");
                }



                // CALL_STATE_OFFHOOK;
               // Toast.makeText(mContext, "Phone is Currently in A call",
                   //     Toast.LENGTH_LONG).show();

                break;
            case TelephonyManager.CALL_STATE_RINGING:
                // CALL_STATE_RINGING
                Toast.makeText(mContext, "Phone is ringing or waiting call",
                        Toast.LENGTH_LONG).show();

                break;
            default:
                break;
        }


    }

}
