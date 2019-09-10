package com.dillonkharris.familymapclient;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import model.Client;
import model.Event;
import model.Person;

public class PersonActivity extends AppCompatActivity {

    Activity mPersonActivity;
    Activity mEventActivity;
    Set<String> selected = new TreeSet<>();

    // groups of items that can expand/collapse
    List<Group> groups;

    Activity mMainActivity = new MainActivity();
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

    public class Group implements Parent<String> {

        String name;
        String[] values;

        Group(String name, String[] values) {
            this.name = name;
            this.values = values;
        }
        @Override
        public List<String> getChildList() {
            return Arrays.asList(values);
        }
        @Override
        public boolean isInitiallyExpanded() {
            return false;
        }
    }

    private TextView textView;
    private RecyclerView recyclerView;
    private Adapter adapter;

    private TextView mFirstName;
    private TextView mLastName;
    private TextView mGender;
    private ArrayList<Event> events;
    private ArrayList<Person> family;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        mFirstName = findViewById(R.id.first_name);
        mLastName = findViewById(R.id.last_name);
        mGender = findViewById(R.id.gender);
        mPersonActivity = new PersonActivity();
        mEventActivity = new EventActivity();

        events = Client.getInstance().getEventsOfCurrentPerson();
        ArrayList<Event> shownEvents = new ArrayList<>();
        for (int i = 0; i < events.size(); i ++) {
            if (Client.getInstance().notFiltered(events.get(i))) {
                shownEvents.add(events.get(i));
            }
        }
        events = shownEvents;
        family = Client.getInstance().getFamilyOfCurrentPerson();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        updateUI();
    }

    void updateUI() {

        selected.clear();
        updatePerson();
        groups = setUpGroups();
        adapter = new Adapter(this, groups);
        recyclerView.setAdapter(adapter);

    }
    private List<Group> setUpGroups() {
        String[] theFam = new String[family.size()];
        for(int i = 0; i < family.size(); i++) {
            theFam[i] = family.get(i).getPersonID();
        }

        String[] theEvents = new String[events.size()];
        for(int i = 0; i < events.size(); i++) {
            theEvents[i] = events.get(i).getEventID();
        }
        List<Group> g = Arrays.asList(
            new Group("LIFE EVENTS", theEvents),
            new Group("FAMILY", theFam)
        );
        return g;
    }

    void updatePerson() {
        Client client = Client.getInstance();
        mFirstName.setText(mFirstName.getText() + client.getCurrentPerson().getFirstName());
        mLastName.setText(mLastName.getText() + client.getCurrentPerson().getLastName());
        mGender.setText(mGender.getText() + client.getCurrentPerson().getGender());
    }

    class Adapter extends ExpandableRecyclerAdapter<Group, String, GroupHolder, Holder> {

        private LayoutInflater inflater;

        public Adapter(Context context, List<Group> groups) {
            super(groups);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public GroupHolder onCreateParentViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = inflater.inflate(R.layout.list_group, viewGroup, false);
            return new GroupHolder(view);
        }

        @Override
        public Holder onCreateChildViewHolder(@NonNull ViewGroup viewGroup, int i) {

            View view = inflater.inflate(R.layout.list_item, viewGroup, false);

            return new Holder(view);
        }

        @Override
        public void onBindParentViewHolder(@NonNull GroupHolder holder, int i, Group group) {
            holder.bind(group);
        }

        @Override
        public void onBindChildViewHolder(@NonNull Holder holder, int i, int j, String item) {
            holder.bind(item);
        }

    }

    class GroupHolder extends ParentViewHolder {

        private TextView textView;

        public GroupHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.group_title);
        }

        void bind(Group group) {
            textView.setText(group.name);
        }

    }

    class Holder extends ChildViewHolder implements View.OnClickListener {

        private TextView textView;
        private TextView secondaryText;
        private ImageView itemImage;
        private String item;

        public Holder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.item_textview);
            textView.setOnClickListener(this);

            itemImage = view.findViewById(R.id.item_imageview);
            secondaryText = view.findViewById(R.id.secondaryText);
        }

        void bind(String item) {
            this.item = item;
            if (isPerson(item)) {
                Person currPerson = getPersonByID(item);
                String relationship = findRelationship(Client.getInstance().getCurrentPerson(), currPerson);
                if (currPerson.getGender().equals("M") || currPerson.getGender().equals("m")) {
                    itemImage.setImageResource(R.drawable.ic_man);
                }
                else {
                    itemImage.setImageResource(R.drawable.ic_woman);
                }
                textView.setText(currPerson.getFirstName() + " " + currPerson.getLastName());
                secondaryText.setText(relationship);
            }
            else {
                Event currEvent = getEventByID(item);
                itemImage.setImageResource(R.drawable.ic_event);
                textView.setText(currEvent.getEventType()
                        + ": " + currEvent.getCity()
                        + ", " + currEvent.getCountry()
                        + " (" + currEvent.getYear()
                        + ")");
                secondaryText.setText(
                        Client.getInstance().getCurrentPerson().getFirstName()
                        + " " + Client.getInstance().getCurrentPerson().getLastName());
            }
        }
        String findRelationship(Person currPerson, Person relative) {
            if (currPerson.getSpouse() != null) {
                if (currPerson.getSpouse().equals(relative.getPersonID())) {
                    return "Spouse";
                }
            }
            if (currPerson.getFather() != null) {
                if (currPerson.getFather().equals(relative.getPersonID())) {
                    return "Father";
                }
                if (currPerson.getMother().equals(relative.getPersonID())) {
                    return "Mother";
                }
                else {
                    return "Child";
                }
            }
            else {
                return "Child";
            }
        }
        boolean isPerson(String id) {
            for (int i = 0; i < family.size(); i++) {
                if (id.equals(family.get(i).getPersonID())) {
                    return true;
                }
            }
            return false;
        }
        Person getPersonByID(String id) {
            for (int i = 0; i < family.size(); i++) {
                if (id.equals(family.get(i).getPersonID())) {
                    return family.get(i);
                }
            }
            return null;
        }
        Event getEventByID(String id) {
            for (int i = 0; i < events.size(); i++) {
                if (id.equals(events.get(i).getEventID())) {
                    return events.get(i);
                }
            }
            return null;
        }

        @Override
        public void onClick(View view) {
            if (isPerson(item)) {
                Person person = getPersonByID(item);
                Client.getInstance().setCurrentPerson(person);
                Client.getInstance().setCurrentPersonFamily(person);
                Client.getInstance().setCurrentPersonEvents(person);

                Intent intent = new Intent(view.getContext(),mPersonActivity.getClass());
                startActivity(intent);
            }
            else {
                Event event = getEventByID(item);
                Client.getInstance().setCurrentEvent(event);
                Intent intent = new Intent(view.getContext(),mEventActivity.getClass());
                startActivity(intent);
            }
        }
    }
}
