package com.example.multi_dial_app;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

public class CallActivity extends ActionBarActivity{

    private DrawerLayout mDrawerLayout;

    private RelativeLayout mDrawerPane;

    private ActionBarDrawerToggle mDrawerToggle;

    private TextView mUserName;

    private RelativeLayout mProfileBox;

    private ImageView imageView;

    private TextView mEmail;

    private Button applied;

    private Button logout;

    private Button callRecords;

    private RelativeLayout background;

    private Handler mHandler;

    private static Switch OnOffSwitch;

    private static SharedPreferences preferences;

    private static SharedPreferences.Editor editor;

    private static String callerName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_activity);

        background = (RelativeLayout)findViewById(R.id.background);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mProfileBox = (RelativeLayout) findViewById(R.id.profileBox);
        mUserName = (TextView) findViewById(R.id.userName);  //name of user for the profile box on the drawer
        mEmail = (TextView) findViewById(R.id.desc);
        applied = (Button) findViewById(R.id.callRecords);
        logout = (Button) findViewById(R.id.logout);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);



        final String defaultValue = getResources().getString(R.string.defaultUser);
        preferences = getApplicationContext().getSharedPreferences("user_info", 0);
        editor = preferences.edit();
        callerName = preferences.getString(getResources().getString(R.string.callerName),defaultValue);
        mUserName.setText(callerName);               //will set username for profile box
        mEmail.setText("htcampus@hindustantimes.com");





        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(CallActivity.this, LoginActivity.class);
                editor.putBoolean(getResources().getString(R.string.isLoggedIn), false);
                editor.apply();

                startActivity(intent);

                finish();

            }
        });


        //Listener for Slide drawer toggle

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                //invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.d("TAG", "onDrawerClosed: " + getTitle());

                //invalidateOptionsMenu();
            }
        };


        mDrawerLayout.setDrawerListener(mDrawerToggle);

        callRecords = (Button)findViewById(R.id.callRecords);
        callRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mDrawerLayout.closeDrawers();
                    }
                }, 150);


                Fragment fragment = new CallHistoryFragment();
                replaceFragments(fragment, "callHistory");

            }
        });

/***********************************************************/

        Fragment fragment = new PersonDetailFragment();
        replaceFragments(fragment, "leadDetailFragment");

/***********************************************************/
    }



    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(com.example.multi_dial_app.R.menu.menu_main, menu);

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }



        int id = item.getItemId();
        switch (id) {

            case android.R.id.home:
                Fragment frag = getFragmentManager().findFragmentByTag("leadDetailFragment");
                if(frag != null && frag.isVisible())
                {
                    getFragmentManager().popBackStack();
                    changeDrawerUpIndicator(true);
                }
                //finish();
               overridePendingTransition(R.anim.activity_anim_back_in, R.anim.activity_anim_back_out);
                return true;

        }

        return super.onOptionsItemSelected(item);

    }

    public void changeDrawerUpIndicator(Boolean value){

        mDrawerToggle.setDrawerIndicatorEnabled(value);
    }



    @Override
    public void onBackPressed() {

        Fragment frag = getFragmentManager().findFragmentByTag("callHistory");


        if((frag != null && frag.isVisible())) {
            getFragmentManager().popBackStack();
            //changeDrawerUpIndicator(true);
                //super.onBackPressed();
        }
        else {
                //getFragmentManager().popBackStack();
        }
    }




    public void replaceFragments(Fragment newFragment, String tag)
    {

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.mainContent, newFragment, tag);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

    }



}
