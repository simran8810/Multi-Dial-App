package com.example.multi_dial_app;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

public class CallActivity extends ActionBarActivity{


    private static RelativeLayout background;

    private static String callerName;

    private static Switch OnOffSwitch;

    private static SharedPreferences preferences;

    private static SharedPreferences.Editor editor;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        background = (RelativeLayout)findViewById(R.id.background);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayUseLogoEnabled(true);



        final String defaultValue = getResources().getString(R.string.defaultUser);


        preferences = getApplicationContext().getSharedPreferences("user_info", 0);
        editor = preferences.edit();

        callerName = preferences.getString(getResources().getString(R.string.callerName),defaultValue);



/***********************************************************/
        Fragment fragment = new LeadDetailFragment();
        FragmentManager fragmentManager = getFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.background,fragment, "leadDetailFragment");

        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();

/***********************************************************/


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


            case R.id.telecallername:

                Toast.makeText(getApplicationContext(),"ht is pressed",Toast.LENGTH_SHORT).show();

                return true;

            case R.id.logout:

                Intent intent = new Intent(CallActivity.this, MainActivity.class);
                editor.putBoolean(getResources().getString(R.string.isLoggedIn), false);
                editor.apply();

                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);

    }


    @Override
    public void onBackPressed() {

    }



}
