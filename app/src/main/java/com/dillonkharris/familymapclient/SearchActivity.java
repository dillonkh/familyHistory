package com.dillonkharris.familymapclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import java.util.ArrayList;

import model.Client;
import model.Event;
import model.Person;

public class SearchActivity extends AppCompatActivity implements PersonRecyclerViewAdapter.ItemClickListener, EventRecyclerViewAdapter.ItemClickListener {

    private Activity mMainActivity;
    private Activity mSearchActivity;
    private SearchView mSearchView;
    private PersonRecyclerViewAdapter personAdapter;
    private EventRecyclerViewAdapter eventAdapter;
    private PersonActivity mPersonActivity;
    private EventActivity mEventActivity;

    private ArrayList<Person> people = new ArrayList<>();
    private ArrayList<Event> events = new ArrayList<>();

    private SearchActivity searchActivity = this;

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
        setContentView(R.layout.activity_search);

        mSearchActivity = this;

        mMainActivity = new MainActivity();
        mPersonActivity = new PersonActivity();
        mEventActivity = new EventActivity();

        mSearchView = findViewById(R.id.search);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                people = findAllPeople(newText);
                RecyclerView recyclerView = findViewById(R.id.rvPeople);
                recyclerView.setLayoutManager(new LinearLayoutManager(mSearchActivity));
                personAdapter = new PersonRecyclerViewAdapter(mSearchActivity, people);
                personAdapter.setClickListener(searchActivity);
                recyclerView.setAdapter(personAdapter);

                events = findAllEvents(newText);
                System.out.println(events);
                RecyclerView recyclerViewEvents = findViewById(R.id.rvEvents);
                recyclerViewEvents.setLayoutManager(new LinearLayoutManager(mSearchActivity));
                eventAdapter = new EventRecyclerViewAdapter(mSearchActivity, events);
                eventAdapter.setClickListener(searchActivity);
                recyclerViewEvents.setAdapter(eventAdapter);

                return false;
            }
        });


    }
    private ArrayList<Person> findAllPeople(String substring) {
        ArrayList<Person> allPeople = Client.getInstance().getFamily();
        ArrayList<Person> foundPeople = new ArrayList<>();

        for (int i = 0; i < allPeople.size(); i++) {
            if (allPeople.get(i).getFirstName().toLowerCase().contains(substring.toLowerCase())
                    || allPeople.get(i).getLastName().toLowerCase().contains(substring.toLowerCase())) {
                foundPeople.add(allPeople.get(i));
            }
        }
        return foundPeople;
    }
    private ArrayList<Event> findAllEvents(String substring) {
        substring = substring.toLowerCase();
        ArrayList<Event> shownEvents = Client.getInstance().getShownEvents();
        ArrayList<Event> foundEvents = new ArrayList<>();

        for (int i = 0; i < shownEvents.size(); i++) {
            Event event = shownEvents.get(i);
            if (event.getYear().toLowerCase().contains(substring)
                    || event.getCity().toLowerCase().contains(substring)
                    || event.getCountry().toLowerCase().contains(substring)
                    || event.getEventType().toLowerCase().contains(substring)) {

                foundEvents.add(shownEvents.get(i));
            }
        }
        return foundEvents;
    }

    // for clicking an item in the list
    @Override
    public void onItemClick(View view, int position) {
        Event event = eventAdapter.getItem(position);
        Client.getInstance().setCurrentEvent(event);
        Intent intent = new Intent(view.getContext(),mEventActivity.getClass());
        startActivity(intent);
    }
    @Override
    public void onPersonClick(View view, int position) {
        Person person = personAdapter.getItem(position);
        Client.getInstance().setCurrentPerson(person);
        Client.getInstance().setCurrentPersonFamily(person);
        Client.getInstance().setCurrentPersonEvents(person);

        Intent intent = new Intent(view.getContext(),mPersonActivity.getClass());
        startActivity(intent);
    }



}
