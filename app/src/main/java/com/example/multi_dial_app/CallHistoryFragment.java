package com.example.multi_dial_app;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Switch;

import com.example.multi_dial_app.database.SqlController;
import com.example.multi_dial_app.database.Database;

/**
 * Created by ht on 21/5/15.
 */
public class CallHistoryFragment extends Fragment implements CompoundButton.OnCheckedChangeListener{


    private View view;

    private RelativeLayout background;

    private static Switch OnOffSwitch;

    private SqlController dbcon;

    private Cursor cursor;


    private static String callerName;




    @Override
    public void onAttach(Activity activity) {


        super.onAttach(activity);

        ((CallActivity) activity).setTitle(getString(R.string.callHistory));
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        background = (RelativeLayout) getActivity().findViewById(R.id.background);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.call_history_fragment, container, false);

        SharedPreferences preferences = getActivity().getSharedPreferences("user_info", 0);
        callerName = preferences.getString(getResources().getString(R.string.callerName),"telecaller");


        Log.e("telecaller", callerName);



        //open db connection
        dbcon = new SqlController(getActivity());
        dbcon.open();

        //set cursor to call history table
        cursor = dbcon.getCallHistory(callerName);

        // The desired columns to be bound
        String[] columns = new String[] {
                Database.COL_NAME,
                Database.COL_LOCATION,
                Database.COL_SHORT_FEEDBACK,
                Database.COL_CALL_DATETIME

        };

        // the XML defined views which the data will be bound to
        int[] to = new int[] {
                R.id.personName,
                R.id.location,
                R.id.shortFeedback,
                R.id.callDateTime
        };


        try {


            // create the adapter using the cursor pointing to the desired data
            SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(
                    getActivity(), R.layout.call_history_row,
                    cursor,
                    columns,
                    to,
                    0);

            ListView listView = (ListView) view.findViewById(R.id.callHistoryListView);

            // Assign adapter to ListView
            listView.setAdapter(dataAdapter);

        }
        catch (RuntimeException e){
            Log.e("adpater", e.toString(), e);
        }

        //close db connection
        dbcon.close();




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


        }else{


            background.setBackgroundColor(getResources().getColor(R.color.grey));



        }
    }




}

