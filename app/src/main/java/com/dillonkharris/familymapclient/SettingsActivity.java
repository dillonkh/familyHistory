package com.dillonkharris.familymapclient;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import model.Client;
import response.EventsResponse;
import request.LoginRequest;
import response.PeopleResponse;
import model.Person;
import response.RegisterLoginResponse;

public class SettingsActivity extends AppCompatActivity {

    private Activity mMainActivity;
    private Activity mSettingsActivity;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, mMainActivity.getClass());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(intent);
                break;
        }
        return true;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mMainActivity = new MainActivity();
        mSettingsActivity = this;
        Spinner mLifeSpinner = findViewById(R.id.lifeStorySpinner);
        setUpColorSpinner(mLifeSpinner, colorByIndex(Client.getInstance().getLifeStoryLineColor()));
        Switch mLifeSwitch = findViewById(R.id.storyLinesSwitch);
        mLifeSwitch.setChecked(Client.getInstance().isFamilyTreeLinesOn());
        setUpSwitch(mLifeSwitch);

        Spinner mFamilySpinner = findViewById(R.id.familyTreeSpinner);
        setUpColorSpinner(mFamilySpinner, colorByIndex(Client.getInstance().getFamilyTreeLineColor()));
        Switch mFamilySwitch = findViewById(R.id.familyTreeSwitch);
        mFamilySwitch.setChecked(Client.getInstance().isFamilyTreeLinesOn());
        setUpSwitch(mFamilySwitch);

        Spinner mSpouseSpinner = findViewById(R.id.spouseSpinner);
        setUpColorSpinner(mSpouseSpinner, colorByIndex(Client.getInstance().getSpouseLineColor()));
        Switch mSpouseSwitch = findViewById(R.id.spouseSwitch);
        mSpouseSwitch.setChecked(Client.getInstance().isSpouseLinesOn());
        setUpSwitch(mSpouseSwitch);

        Spinner mMapSpinner = findViewById(R.id.mapTypeSpinner);
        final String[] mapTypes = getResources().getStringArray((R.array.map_type));
        mMapSpinner.setSelection(mapByIndex(Client.getInstance().getMapType()));
        mMapSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0 :
                                Client.getInstance().setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                break;
                            case 1 :
                                Client.getInstance().setMapType(GoogleMap.MAP_TYPE_HYBRID);
                                break;
                            case 2 :
                                Client.getInstance().setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                                break;
                            case 3 :
                                Client.getInstance().setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                                break;
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Must be overridden but we don't use it.
                    }
                });

        View mReSyncView = findViewById(R.id.reSync);
        mReSyncView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginRequest loginRequest = new LoginRequest(Client.getInstance().getUsername(),Client.getInstance().getPassword());
                new LoginTask().execute(loginRequest);

            }
        });
        View mLogoutView = findViewById(R.id.logout);
        mLogoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"Good Bye",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), mMainActivity.getClass());
                startActivity(intent);
            }
        });

    }
    private int colorByIndex(int color) {
        switch(color) {
            case Color.RED :
                return 0;
            case Color.GREEN :
                return 1;
            case Color.BLUE :
                return 2;
            case Color.YELLOW :
                return 3;
            case Color.GRAY :
                return 4;
            default:
                return 0;
        }
    }
    private int mapByIndex(int type) {
        switch(type) {
            case GoogleMap.MAP_TYPE_NORMAL:
                return 0;
            case GoogleMap.MAP_TYPE_HYBRID:
                return 1;
            case GoogleMap.MAP_TYPE_SATELLITE:
                return 2;
            case GoogleMap.MAP_TYPE_TERRAIN:
                return 3;
            default:
                return 0;
        }
    }
    private void setUpSwitch(final Switch theSwitch) {
        theSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (theSwitch.isChecked()) {
                    turnOnLines(theSwitch.getId());
                }
                else {
                    turnOffLines(theSwitch.getId());
                }
            }
        });
    }
    private void turnOnLines(int id) {
        switch(id) {
            case R.id.storyLinesSwitch :
                Client.getInstance().setLifeStoryLinesOn(true);
                break;
            case R.id.familyTreeSwitch :
                Client.getInstance().setFamilyTreeLinesOn(true);
                break;
            case R.id.spouseSwitch :
                Client.getInstance().setSpouseLinesOn(true);
                break;
        }
    }
    private void turnOffLines(int id) {
        switch(id) {
            case R.id.storyLinesSwitch :
                Client.getInstance().setLifeStoryLinesOn(false);
                break;
            case R.id.familyTreeSwitch :
                Client.getInstance().setFamilyTreeLinesOn(false);
                break;
            case R.id.spouseSwitch :
                Client.getInstance().setSpouseLinesOn(false);
                break;
        }
    }
    private void setUpColorSpinner(final Spinner spinner, int index) {
        // Handle Spinner
        final String[] colors = getResources().getStringArray((R.array.colors_array));
        spinner.setSelection(index);
        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        final int FAMILY = 0;
                        final int STORY = 1;
                        final int SPOUSE = 2;

                        switch (spinner.getId()) {
                            case R.id.familyTreeSpinner :
                                Client.getInstance().setFamilyTreeLineColor(findColor(position));
                                break;
                            case R.id.lifeStorySpinner :
                                Client.getInstance().setLifeStoryLineColor(findColor(position));
                                break;
                            case R.id.spouseSpinner :
                                Client.getInstance().setSpouseLineColor(findColor(position));
                                break;
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Must be overridden but we don't use it.
                    }
                });
    }
    private int findColor(int position) {
        switch (position) {
            case 0 :
                return Color.RED;
            case 1 :
                return Color.GREEN;
            case 2 :
                return Color.BLUE;
            case 3 :
                return Color.YELLOW;
            case 4 :
                return Color.GRAY;
            default :
                return Color.BLACK;

        }
    }
    private class LoginTask extends AsyncTask<LoginRequest,Void, RegisterLoginResponse> {

        @Override
        protected RegisterLoginResponse doInBackground(LoginRequest... params) {
            RegisterLoginResponse resp = new RegisterLoginResponse();
            try {
                resp = Client.getInstance().getProxy().login(params[0]);
            }
            catch(Exception ex) {
                ex.printStackTrace();
                return new RegisterLoginResponse(null,null,null);
            }

            return resp;
        }
        @Override
        protected void onPostExecute (RegisterLoginResponse resp) {
            try {
                if (resp.getPersonID() != null) {
                    new PersonTask().execute(resp);
                }
                else {
                    Toast.makeText(mSettingsActivity,
                            "Re-Sync Failed",
                            Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }
    private class PersonTask extends AsyncTask<RegisterLoginResponse,Void,Person> {

        @Override
        protected Person doInBackground(RegisterLoginResponse ... params) {
            Person person = new Person();
            try {
                person = Client.getInstance().getProxy().getPerson(params[0].getPersonID(),params[0].getAuthToken());
                PeopleResponse family = Client.getInstance().getProxy().getFamily(params[0].getAuthToken());
                EventsResponse events = Client.getInstance().getProxy().getEvents(params[0].getAuthToken());

                //set the family and current user
                Client.getInstance().setEvents(events.getEvents());
                Client.getInstance().setShownEvents(events.getEvents());
                Client.getInstance().setFamily(family.getPeople());
                Client.getInstance().setUser(person);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }

            return person;
        }
        @Override
        protected void onPostExecute(Person person) {

                Intent intent = new Intent(mSettingsActivity, mMainActivity.getClass());
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("extra",0);
                startActivity(intent);
        }
    }
}
