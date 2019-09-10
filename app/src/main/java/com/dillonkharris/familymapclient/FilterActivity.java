package com.dillonkharris.familymapclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import model.Client;


public class FilterActivity extends AppCompatActivity {

    private Activity mMainActivity;
    private Activity mFilterActivity;


    private Switch mFathersSideSwitch;
    private Switch mMothersSideSwitch;

    private Switch mMaleSwitch;
    private Switch mFemaleSwitch;

    private MyCustomAdapter adapter;
    private Client client = Client.getInstance();


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
        setContentView(R.layout.activity_filter_listview);

        //generate list
        ArrayList<String> list = new ArrayList<String>();
        list.addAll(Client.getInstance().getEventTypes());

        //instantiate custom adapter
        adapter = new MyCustomAdapter(list, this, mFilterActivity);

        //handle listview and assign adapter
        ListView lView = (ListView)findViewById(R.id.mobile_list);
        lView.setAdapter(adapter);

        mMainActivity = new MainActivity();
        mFilterActivity = this;

        setUpFathersSideSwitch();
        mFathersSideSwitch.setChecked(Client.getInstance().isFathersSideMarkersOn());
        setUpMothersSideSwitch();
        mMothersSideSwitch.setChecked(Client.getInstance().isMothersSideMarkersOn());

        setUpMaleSwitch();
        mMaleSwitch.setChecked(Client.getInstance().isMaleMarkersOn());
        setUpFemaleSwitch();
        mFemaleSwitch.setChecked(Client.getInstance().isFemaleMarkersOn());
    }

    private void setUpFathersSideSwitch(){
        mFathersSideSwitch = findViewById(R.id.fatherSideSwitch);

        mFathersSideSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFathersSideSwitch.isChecked()) {
                    //turnOnEventsBySide("d");
                    Client.getInstance().setFathersSideMarkersOn(true);
                }
                else {
                    //turnOffEventsBySide("d");
                    Client.getInstance().setFathersSideMarkersOn(false);
                }
                client.reApplyFilters();
            }
        });
    }
    private void setUpMothersSideSwitch(){
        mMothersSideSwitch = findViewById(R.id.motherSideSwitch);

        mMothersSideSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMothersSideSwitch.isChecked()) {
                    //turnOnEventsBySide("m");
                    Client.getInstance().setMothersSideMarkersOn(true);
                }
                else {
                    //turnOffEventsBySide("m");
                    Client.getInstance().setMothersSideMarkersOn(false);
                }
                client.reApplyFilters();
            }
        });
    }

    private void setUpMaleSwitch(){
        mMaleSwitch = findViewById(R.id.maleSwitch);

        mMaleSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMaleSwitch.isChecked()) {
                    //turnOnEventsGender("m");
                    Client.getInstance().setMaleMarkersOn(true);
                }
                else {
                    //turnOffEventsGender("m");
                    Client.getInstance().setMaleMarkersOn(false);
                }
                client.reApplyFilters();
            }
        });
    }
    private void setUpFemaleSwitch(){
        mFemaleSwitch = findViewById(R.id.femaleSwitch);

        mFemaleSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFemaleSwitch.isChecked()) {
                    //turnOnEventsGender("f");
                    Client.getInstance().setFemaleMarkersOn(true);
                }
                else {
                    //turnOffEventsGender("f");
                    Client.getInstance().setFemaleMarkersOn(false);
                }
                client.reApplyFilters();
            }
        });
    }
}
class MyCustomAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;
    private Activity mFilterActivity;


    public MyCustomAdapter(ArrayList<String> list, Context context,Activity mFilterActivity) {
        this.list = list;
        this.context = context;
        this.mFilterActivity = mFilterActivity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_listview, null);
        }

        //Handle TextView and display string from your list
        final TextView listItemText = (TextView)view.findViewById(R.id.eventType);
        listItemText.setText(list.get(position));

        //Handle buttons and add onClickListeners
        final Switch eventSwitch = view.findViewById(R.id.eventTypeSwitch);
        eventSwitch.setChecked(Client.getInstance().getTypeToBool().get(listItemText.getText().toString()));
        eventSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Client client = Client.getInstance();
                if (eventSwitch.isChecked()) {
                    //turnOnEventsType(listItemText.getText().toString());
                    HashMap<String,Boolean> hash = Client.getInstance().getTypeToBool();
                    hash.remove(listItemText.getText().toString());
                    hash.put(listItemText.getText().toString(),true);
                    Client.getInstance().setTypeToBool(hash);
                }
                else {
                    //turnOffEventsType(listItemText.getText().toString());
                    HashMap<String,Boolean> hash = Client.getInstance().getTypeToBool();
                    hash.remove(listItemText.getText().toString());
                    hash.put(listItemText.getText().toString(),false);
                    Client.getInstance().setTypeToBool(hash);
                }
                client.reApplyFilters();
            }
        });

        return view;
    }
}

