package com.dillonkharris.familymapclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;

import model.Client;
import model.Event;
import model.Person;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private PolylineOptions lines;

    private TextView mEventText;
    private ImageView mEventIcon;
    private LinearLayout mEventLayout;

    private Activity mPersonActivity;
    private Activity mSettingsActivity;
    private Activity mFilterActivity;
    private Activity mSearchActivity;


    public static MapFragment newInstance(){
        return new MapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        super.onCreateView(layoutInflater, viewGroup, bundle);
        View v = layoutInflater.inflate(R.layout.fragment_map, viewGroup, false);

        mapFragment = (SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setHasOptionsMenu(true);

        mPersonActivity = new PersonActivity();
        mSearchActivity = new SearchActivity();
        mFilterActivity = new FilterActivity();
        mSettingsActivity = new SettingsActivity();

        mEventText = v.findViewById(R.id.eventInfo);
        mEventIcon = v.findViewById(R.id.eventImage);
        mEventLayout = v.findViewById(R.id.eventLayout);

        Client.getInstance().setMapType(GoogleMap.MAP_TYPE_NORMAL);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        mEventText.setText("Click on a Marker to see Event Details");
        mEventIcon.setImageResource(R.drawable.ic_droid);

        if (mMap != null) {
            mMap.clear();
            mMap.setMapType(Client.getInstance().getMapType());
            Client client = Client.getInstance();
            ArrayList<MarkerOptions> markers = new ArrayList<>();

            for (int i = 0; i < client.getShownEvents().size(); i++) {
                markers.add(setUpMarker(client.getShownEvents().get(i)));

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.search:
                loadSearch();
                return true;
            case R.id.filter:
                loadFilter();
                return true;
            case R.id.settings:
                loadSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private MarkerOptions setUpMarker(Event event) {

        double lat = Double.parseDouble(event.getLatitude());
        double longi = Double.parseDouble(event.getLongitude());

        LatLng place = new LatLng(lat,longi);
        MarkerOptions options = new MarkerOptions();

        options
                .position(place)
                .title(getEventName(event) + ", " + event.getEventType());

        options = selectMarkerColor(options,event.getEventType());

        mMap.addMarker(options);

        HashMap<MarkerOptions,Event> newMap  = Client.getInstance().getShownMarkerEventMap();
        if (newMap == null) {
            newMap = new HashMap<>();
        }
        newMap.put(options,event);
        Client.getInstance().setShownMarkerEventMap(newMap);

        return options;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Client client = Client.getInstance();
        ArrayList<MarkerOptions> markers = new ArrayList<>();

        for (int i = 0; i < client.getShownEvents().size(); i++) {
           markers.add(setUpMarker(client.getShownEvents().get(i)));
        }
        client.setMarkersShown(markers);
        client.setAllMarkers(markers);

        setMarkerListeners();
        CameraUpdate point = null;
        if (Client.getInstance().getCurrentEvent() == null) {
            point = CameraUpdateFactory.newLatLng(new LatLng(0, 0));
        }
        else {
            Double latitude = Double.parseDouble(Client.getInstance().getCurrentEvent().getLatitude());
            Double longitude = Double.parseDouble(Client.getInstance().getCurrentEvent().getLongitude());
            point = CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude));

            changeEvent(Client.getInstance().getCurrentEvent());
        }

        // moves camera to coordinates
        mMap.moveCamera(point);

        if (client.getCurrentEvent() != null) {
            setUpLines(client.getCurrentEvent());
            client.setCurrentEvent(null);
        }
    }
    private void setUpLines(Event event) {

        for (int i = 0; i < Client.getInstance().getSpousePolylines().size(); i++) {
            Client.getInstance().getSpousePolylines().get(i).remove();
        }

        for (int i = 0; i < Client.getInstance().getFamilyTreeLines().size(); i++) {
            Client.getInstance().getFamilyTreeLines().get(i).remove();
        }

        for (int i = 0; i < Client.getInstance().getLifeStoryLines().size(); i++) {
            Client.getInstance().getLifeStoryLines().get(i).remove();
        }

        Person currPerson = Client.getInstance().findPerson(event.getPersonID());
        if (Client.getInstance().isSpouseLinesOn()) {
            drawSpouseLines(event,currPerson);
        }
        if (Client.getInstance().isFamilyTreeLinesOn()) {
            drawFamilyTreeLines(event,currPerson,new ArrayList<Polyline>(),15);
        }
        if (Client.getInstance().isLifeStoryLinesOn()) {
            drawLifeStoryLines(event,currPerson);
        }
    }
    private  void drawSpouseLines(Event event,Person currPerson) {
        ArrayList<Event> spouseEvents = Client.getInstance().getEventsOfPerson(currPerson.getSpouse());
        Event earliestSpouseEvent = Client.getInstance().getEarliestEvent(spouseEvents);
        Double eventLat = Double.parseDouble(event.getLatitude());
        Double eventLongi = Double.parseDouble(event.getLongitude());
        ArrayList<Polyline> lines = new ArrayList<>();

        if (earliestSpouseEvent == null) {
            return;
        }
        else {
            Double spouseLat = Double.parseDouble(earliestSpouseEvent.getLatitude());
            Double spouseLongi = Double.parseDouble(earliestSpouseEvent.getLongitude());

            PolylineOptions line = new PolylineOptions();
            line.add(new LatLng(spouseLat,spouseLongi),new LatLng(eventLat,eventLongi));
            line.color(Client.getInstance().getSpouseLineColor());
            Polyline polyLine = mMap.addPolyline(line);
            lines.add(polyLine);
        }

        Client.getInstance().setSpousePolylines(lines);

    }
    private void drawFamilyTreeLines(Event event,Person currPerson,ArrayList<Polyline> lines,float width) {

        Double eventLat = Double.parseDouble(event.getLatitude());
        Double eventLongi = Double.parseDouble(event.getLongitude());

        Client client = Client.getInstance();
        ArrayList<Event> fathersEvents = client.getEventsOfPerson(currPerson.getFather());
        ArrayList<Event> mothersEvents = client.getEventsOfPerson(currPerson.getMother());

        Event fathersEarliestEvent = client.getEarliestEvent(fathersEvents);
        Event mothersEarliestEvent = client.getEarliestEvent(mothersEvents);

        PolylineOptions line = new PolylineOptions();

        if (fathersEarliestEvent != null) {
            Double dadLat = Double.parseDouble(fathersEarliestEvent.getLatitude());
            Double dadLongi = Double.parseDouble(fathersEarliestEvent.getLongitude());
            line.add(new LatLng(dadLat,dadLongi),new LatLng(eventLat,eventLongi));
            line.color(Client.getInstance().getFamilyTreeLineColor());
            line.width(width);
            Polyline polyLine = mMap.addPolyline(line);
            lines.add(polyLine);
            drawFamilyTreeLines(fathersEarliestEvent,client.findPerson(fathersEarliestEvent.getPersonID()),lines,width/2);
        }
        if (mothersEarliestEvent != null) {
            Double momLat = Double.parseDouble(mothersEarliestEvent.getLatitude());
            Double momLongi = Double.parseDouble(mothersEarliestEvent.getLongitude());
            line = new PolylineOptions();
            line.add(new LatLng(momLat,momLongi),new LatLng(eventLat,eventLongi));
            line.color(Client.getInstance().getFamilyTreeLineColor());
            line.width(width);
            Polyline polyLine = mMap.addPolyline(line);
            lines.add(polyLine);
            drawFamilyTreeLines(mothersEarliestEvent,client.findPerson(mothersEarliestEvent.getPersonID()),lines,width/2);
        }

        client.setFamilyTreeLines(lines);

    }
    private void drawLifeStoryLines(Event event,Person currPerson) {
        ArrayList<Event> lifeStoryEvents = Client.getInstance().getEventsOfPerson(currPerson.getPersonID());
        ArrayList<Polyline> lines = new ArrayList<>();

        for (int i = 0; i < lifeStoryEvents.size(); i++) {
            Double eventLat = Double.parseDouble(event.getLatitude());
            Double eventLongi = Double.parseDouble(event.getLongitude());

            Double spouseLat = Double.parseDouble(lifeStoryEvents.get(i).getLatitude());
            Double spouseLongi = Double.parseDouble(lifeStoryEvents.get(i).getLongitude());

            PolylineOptions line = new PolylineOptions();
            line.add(new LatLng(spouseLat,spouseLongi),new LatLng(eventLat,eventLongi));
            line.color(Client.getInstance().getLifeStoryLineColor());
            Polyline polyLine = mMap.addPolyline(line);
            lines.add(polyLine);
            event = lifeStoryEvents.get(i);
        }
        Client.getInstance().setLifeStoryLines(lines);
    }

    private void loadSearch() {
        Intent intent = new Intent(getActivity(),mSearchActivity.getClass());
        startActivity(intent);
    }
    private void loadFilter() {
        Intent intent = new Intent(getActivity(),mFilterActivity.getClass());
        startActivity(intent);
    }
    private void loadSettings() {
        Intent intent = new Intent(getActivity(),mSettingsActivity.getClass());
        startActivity(intent);    }

    private void setMarkerListeners() {
        // marker listeners
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Event event = findMarker(marker);

                changeEvent(event);
                setUpLines(event);
                return false;
            }
        });

    }
    private void setEventlayoutListener() {
        // event area listener
        mEventLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),mPersonActivity.getClass());
                startActivity(intent);
            }
        });
    }

    private void changeEvent(Event event) {
        setEventlayoutListener();
        String eventName = getEventName(event);
        setCurrentPerson(event);
        Client.getInstance().setCurrentPersonFamily(Client.getInstance().getCurrentPerson());
        Client.getInstance().setCurrentPersonEvents(Client.getInstance().getCurrentPerson());

        mEventText.setText(eventName + "\n"
                + event.getEventType() + ": " + event.getCity() + ", "
                + event.getCountry() + " (" + event.getYear() + ")");

        if (isMaleEvent(event) == true) {
            mEventIcon.setImageResource(R.drawable.ic_man);
        }
        else {
            mEventIcon.setImageResource(R.drawable.ic_woman);
        }
    }

    private void setCurrentPerson(Event event) {
        Client client = Client.getInstance();
        for (int i = 0; i < client.getFamily().size(); i++) {
            Person person = client.getFamily().get(i);

            if (person.getPersonID().equals(event.getPersonID())) {
                client.setCurrentPerson(person);
            }
        }

    }

    private boolean isMaleEvent(Event event) {

        Client client = Client.getInstance();
        for (int i = 0; i < client.getFamily().size(); i++) {
            Person person = client.getFamily().get(i);

            if (person.getPersonID().equals(event.getPersonID())) {
                if (person.getGender().equals("m") || person.getGender().equals("M")) {
                    return true;
                }
                else {
                    return false;
                }
            }
        }

        return true;
    }



    private Event findMarker(Marker marker) {

        for (int i = 0; i < Client.getInstance().getShownEvents().size(); i++) {
            if (marker.getPosition().longitude == Double.parseDouble(Client.getInstance().getShownEvents().get(i).getLongitude())
                    && marker.getPosition().latitude == Double.parseDouble(Client.getInstance().getShownEvents().get(i).getLatitude())) {
                return Client.getInstance().getShownEvents().get(i);
            }
        }
        return null;
    }

    private MarkerOptions selectMarkerColor(MarkerOptions options,String eventType) {
        switch (eventType.toLowerCase()) {
            case "birth": options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                break;
            case "marriage": options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                break;
            case "baptism": options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                break;
            case "death": options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                break;
            default: options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                break;
        }
        return options;
    }

    private String getEventName(Event event) {
        Client client = Client.getInstance();
        for (int i = 0; i < client.getFamily().size(); i++) {
            Person person = client.getFamily().get(i);

            if (person.getPersonID().equals(event.getPersonID())) {
                return person.getFirstName() + " " + person.getLastName();
            }
        }
        return "";
    }
}
