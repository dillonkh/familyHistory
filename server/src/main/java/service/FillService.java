package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

import access.AuthTokenDao;
import access.EventDao;
import access.PersonDao;
import access.UserDao;
import model.AuthToken;
import model.Event;
import model.Person;
import model.User;
import response.DefaultResponse;

/** Populates the server's database with generated data for the specified user name */
public class FillService {

//    private int number = 0;

    /**
     * Populates the server's database with generated data for the specified user name up to the specified number of generations
     * @param username          The user's username
     * @param numGenerations    The number of generations we will fill
     * @return                  Returns the DefaultResponse Message
     */
    public DefaultResponse fill (String username, int numGenerations) {

        try {
            if (numGenerations < 0) {
                return new DefaultResponse("Invalid number of generations.");
            }
            numGenerations++;
            System.out.println("in fill service");
            PersonDao personAccess = new PersonDao();
            UserDao userAccess = new UserDao();
            DefaultResponse response;

            // make this user a person
            User user = userAccess.getUserWithUsername(username);
            //System.out.println(user);
            if (user == null) {
                System.out.println("username not found");
                response = new DefaultResponse("Username not found.");
                return response;
            }

            deleteUsersData(username); // delete any data relating to this user.

            Person father = generatePeople(username,user.getLastName(),"M",numGenerations-1,numGenerations);
            Person mother = generatePeople(username,getRandomLastName(),"F",numGenerations-1,numGenerations);

            String fatherID = new String();
            String motherID = new String();

            if (father == null) {
                fatherID = null;
                motherID = null;
            }
            else {
                fatherID = father.getPersonID();
                motherID = mother.getPersonID();
                personAccess.addSpouseOf(father,mother);
                father.setSpouse(motherID);
                mother.setSpouse(fatherID);
            }

            Person person = new Person(
                    username,
                    user.getPersonID(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getGender(),
                    fatherID,
                    motherID,
                    null
            );
            personAccess.addNewPerson(person);
            //number++;
            if (fatherID != null) {
                addRandomEvents(fatherID,motherID,username,numGenerations,numGenerations);
            }
            addRandomEvents(user.getPersonID(),null,username,numGenerations,numGenerations);

            double numPeopleAdded = 0;
            for (int i = 0; i <= numGenerations-1; i++) {
                numPeopleAdded = numPeopleAdded + Math.pow(2,i);
                //System.out.println(numPeopleAdded);
            }
            //numPeopleAdded++;
            double numEventsAdded = (numPeopleAdded) * 4;
            response = new DefaultResponse("Successfully added "
                    + (int)numPeopleAdded + " persons and " + (int)numEventsAdded + " events to the database.");
            System.out.println(personAccess.getAllPeople().size());
            System.out.println(new EventDao().getAllEvents().size());
            return response;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            DefaultResponse response = new DefaultResponse("A database error occurred.");
            return response;
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
            DefaultResponse response = new DefaultResponse("An internal server error occurred.");
            return response;
        }

    }

    /**
     * Populates the server's database with generated data for the specified user name up to 4 generations
     * @param username  The user's username
     * @return          Returns the DefaultResponse Message
     */
    public DefaultResponse fill (String username) { // defualt numGenerations = 4
        final int DEFAULT_GENERATIONS = 4;

        return fill(username,DEFAULT_GENERATIONS);
    }

    private void deleteUsersData(String username) throws SQLException {
        UserDao userAccess = new UserDao();
        PersonDao personAccess = new PersonDao();
        EventDao eventAccess = new EventDao();
        //AuthTokenDao tokenAccess = new AuthTokenDao(); // FIXME: not sure if i need to clear this

        User user = userAccess.getUserWithUsername(username);

        //Delete Person data
        ArrayList<Person> ansestorsOfUser = personAccess.getPeopleWithDescendant(username);
        for (int i = 0; i < ansestorsOfUser.size(); i++) {
            personAccess.deletePerson(ansestorsOfUser.get(i).getPersonID());
        }

        //Delete Event data
        ArrayList<Event> eventsOfUser = eventAccess.getEventsWithDescendant(username);
        for (int i = 0; i < eventsOfUser.size(); i++) {
            eventAccess.deleteEvent(eventsOfUser.get(i).getEventID());
        }

    }

    private Person generatePeople(String username,String lastName,String gender,int numGenerations,int originalGeneration) throws SQLException,FileNotFoundException {

        if (numGenerations > 0) {
            PersonDao personAcccess = new PersonDao();

            Person father = generatePeople(username,lastName,"M",numGenerations-1,originalGeneration);
            Person mother = generatePeople(username,getRandomLastName(),"F",numGenerations-1,originalGeneration);

            String fatherID = new String();
            String motherID = new String();

            if (father == null) {
                fatherID = null;
                motherID = null;
            }
            else {
                fatherID = father.getPersonID();
                motherID = mother.getPersonID();
                personAcccess.addSpouseOf(father,mother);
                father.setSpouse(mother.getPersonID());
                mother.setSpouse(father.getPersonID());
            }

            String firstName = new String();
            if (gender == "M" || gender == "m") {
                firstName = getRandomMaleName();
            }
            else {
                firstName = getRandomFemaleName();
            }

            Person person = new Person(
                    username,
                    UUID.randomUUID().toString(),
                    firstName,
                    lastName,
                    gender,
                    fatherID,
                    motherID,
                    null
            );
            PersonDao personAccess = new PersonDao();

            personAccess.addNewPerson(person);
//            number++;
            if (fatherID != null) {
                addRandomEvents(fatherID,motherID,username,numGenerations,originalGeneration);
            }

            return person;
        }
        else {
            return null;
        }

    }

    private ArrayList<String> calculateStandardYears(int currentGen, int originalGen) {
        // birth. bap = birth+8. marriage = birth+25. death = birth+85.
        // originalGen birth = 1990
        // currentGen birth = originalGen birth + (28 * (originalGen - currentGen + 1))

        final int CURRENT_YEAR        = 2018;
        final int ORIGINAL_BIRTH_YEAR = 1990; // just a starting point.
        final int CHILD_BEARING_YEAR  = 28;
        final int BAPTISM_AGE         = 8;
        final int MARRIAGE_AGE        = 25;
        final int DEATH_AGE           = 85;

        int birth = ORIGINAL_BIRTH_YEAR - (CHILD_BEARING_YEAR * (originalGen - currentGen));
        int baptism = birth + BAPTISM_AGE;
        int marriage = birth + MARRIAGE_AGE;
        int death = birth + DEATH_AGE;

        ArrayList<String> list = new ArrayList<>();
        list.add(Integer.toString(birth));
        list.add(Integer.toString(baptism));

        if (marriage > CURRENT_YEAR) {
            list.add("-");
        }
        else {
            list.add(Integer.toString(marriage));
        }

        if (death > CURRENT_YEAR) {
            list.add("-");
        }
        else {
            list.add(Integer.toString(death));
        }

        return list;
    }

    private void addRandomEvents(String fatherID,String motherID, String descendant,int generation,int originalGen)
            throws SQLException, FileNotFoundException {

        final int BIRTH     = 0;
        final int BAPTISM   = 1;
        final int MARRIAGE  = 2;
        final int DEATH     = 3;

        EventDao eventAccess = new EventDao();
        Gson gson = new Gson();

        LocationData locations = gson.fromJson(new FileReader("jsonResourses/locations.json"), LocationData.class);
        ArrayList<String> years = calculateStandardYears(generation,originalGen);
        Random rand = new Random();

        if (motherID == null) {
            // Birth
            makeBirths(fatherID,motherID,descendant,generation,originalGen);

            // Baptism
            makeBaptisms(fatherID,motherID,descendant,generation,originalGen);
        }
        else {
            // Birth
            makeBirths(fatherID,motherID,descendant,generation,originalGen);

            // Baptism
            makeBaptisms(fatherID,motherID,descendant,generation,originalGen);

            // Marriage
            makeMarriages(fatherID,motherID,descendant,generation,originalGen);

            // Death
            makeDeaths(fatherID,motherID,descendant,generation,originalGen);
        }


    }
    private void makeBirths(String fatherID,String motherID, String descendant,int generation,int originalGen)
            throws SQLException, FileNotFoundException {
        final int BIRTH     = 0;
        final int BAPTISM   = 1;
        final int MARRIAGE  = 2;
        final int DEATH     = 3;

        EventDao eventAccess = new EventDao();
        Gson gson = new Gson();

        LocationData locations = gson.fromJson(new FileReader("jsonResourses/locations.json"), LocationData.class);
        ArrayList<String> years = calculateStandardYears(generation,originalGen);
        Random rand = new Random();

        // Birth
        int n = rand.nextInt(locations.getData().size()) + 0;
        Location location1 = locations.getData().get(n);
        n = rand.nextInt(locations.getData().size()) + 0;
        Location location2 = locations.getData().get(n);


        Event birth1 = new Event(
                UUID.randomUUID().toString(),
                descendant,
                fatherID,
                location1.getLatitude(),
                location1.getLongitude(),
                location1.getCountry(),
                location1.getCity(),
                "Birth",
                years.get(BIRTH)
        );
        Event birth2 = new Event(
                UUID.randomUUID().toString(),
                descendant,
                motherID,
                location2.getLatitude(),
                location2.getLongitude(),
                location2.getCountry(),
                location2.getCity(),
                "Birth",
                years.get(BIRTH)
        );

        eventAccess.addNewEvent(birth1);
        eventAccess.addNewEvent(birth2);
    }
    private void makeBaptisms(String fatherID,String motherID, String descendant,int generation,int originalGen)
            throws SQLException, FileNotFoundException {
        final int BIRTH     = 0;
        final int BAPTISM   = 1;
        final int MARRIAGE  = 2;
        final int DEATH     = 3;

        EventDao eventAccess = new EventDao();
        Gson gson = new Gson();

        LocationData locations = gson.fromJson(new FileReader("jsonResourses/locations.json"), LocationData.class);
        ArrayList<String> years = calculateStandardYears(generation,originalGen);
        Random rand = new Random();

        // Birth
        int n = rand.nextInt(locations.getData().size()) + 0;
        Location location1 = locations.getData().get(n);
        n = rand.nextInt(locations.getData().size()) + 0;
        Location location2 = locations.getData().get(n);

        n = rand.nextInt(locations.getData().size()) + 0;
        location1 = locations.getData().get(n);
        Event baptism1 = new Event(
                UUID.randomUUID().toString(),
                descendant,
                fatherID,
                location1.getLatitude(),
                location1.getLongitude(),
                location1.getCountry(),
                location1.getCity(),
                "Baptism",
                years.get(BAPTISM)
        );
        n = rand.nextInt(locations.getData().size()) + 0;
        location2 = locations.getData().get(n);
        Event baptism2 = new Event(
                UUID.randomUUID().toString(),
                descendant,
                motherID,
                location2.getLatitude(),
                location2.getLongitude(),
                location2.getCountry(),
                location2.getCity(),
                "Baptism",
                years.get(BAPTISM)
        );

        eventAccess.addNewEvent(baptism1);
        eventAccess.addNewEvent(baptism2);
    }
    private void makeMarriages(String fatherID,String motherID, String descendant,int generation,int originalGen)
            throws SQLException, FileNotFoundException {
        final int BIRTH     = 0;
        final int BAPTISM   = 1;
        final int MARRIAGE  = 2;
        final int DEATH     = 3;

        EventDao eventAccess = new EventDao();
        Gson gson = new Gson();

        LocationData locations = gson.fromJson(new FileReader("jsonResourses/locations.json"), LocationData.class);
        ArrayList<String> years = calculateStandardYears(generation,originalGen);
        Random rand = new Random();

        // Birth
        int n = rand.nextInt(locations.getData().size()) + 0;
        Location location1 = locations.getData().get(n);
//        n = rand.nextInt(locations.getData().size()) + 0;
//        Location location2 = locations.getData().get(n);

        Event marriage1 = new Event(
                UUID.randomUUID().toString(),
                descendant,
                fatherID,
                location1.getLatitude(),
                location1.getLongitude(),
                location1.getCountry(),
                location1.getCity(),
                "Marriage",
                years.get(MARRIAGE)
        );
        Event marriage2 = new Event(
                UUID.randomUUID().toString(),
                descendant,
                motherID,
                location1.getLatitude(),
                location1.getLongitude(),
                location1.getCountry(),
                location1.getCity(),
                "Marriage",
                years.get(MARRIAGE)
        );
        eventAccess.addNewEvent(marriage1);
        eventAccess.addNewEvent(marriage2);
    }
    private void makeDeaths(String fatherID,String motherID, String descendant,int generation,int originalGen)
            throws SQLException, FileNotFoundException {
        final int BIRTH     = 0;
        final int BAPTISM   = 1;
        final int MARRIAGE  = 2;
        final int DEATH     = 3;

        EventDao eventAccess = new EventDao();
        Gson gson = new Gson();

        LocationData locations = gson.fromJson(new FileReader("jsonResourses/locations.json"), LocationData.class);
        ArrayList<String> years = calculateStandardYears(generation,originalGen);
        Random rand = new Random();

        // Birth
        int n = rand.nextInt(locations.getData().size()) + 0;
        Location location1 = locations.getData().get(n);
        n = rand.nextInt(locations.getData().size()) + 0;
        Location location2 = locations.getData().get(n);

        Event death1 = new Event(
                UUID.randomUUID().toString(),
                descendant,
                fatherID,
                location1.getLatitude(),
                location1.getLongitude(),
                location1.getCountry(),
                location1.getCity(),
                "Death",
                years.get(DEATH)
        );
        Event death2 = new Event(
                UUID.randomUUID().toString(),
                descendant,
                motherID,
                location2.getLatitude(),
                location2.getLongitude(),
                location2.getCountry(),
                location2.getCity(),
                "Death",
                years.get(DEATH)
        );
        eventAccess.addNewEvent(death1);
        eventAccess.addNewEvent(death2);
    }



    private String getRandomFemaleName() throws FileNotFoundException {
        Gson gson = new Gson();

        NameData names = gson.fromJson(new FileReader("jsonResourses/fnames.json"), NameData.class);

        //System.out.println(names.getNames().get(0));

        Random rand = new Random();
        int n = rand.nextInt(names.getNames().size()) + 0;

        return names.getNames().get(n);
    }
    private String getRandomMaleName() throws FileNotFoundException {
        Gson gson = new Gson();

        NameData names = gson.fromJson(new FileReader("jsonResourses/mnames.json"), NameData.class);

        //System.out.println(names.getNames().get(0));

        Random rand = new Random();
        int n = rand.nextInt(names.getNames().size()) + 0;

        return names.getNames().get(n);
    }
    private String getRandomLastName() throws FileNotFoundException {
        Gson gson = new Gson();

        NameData names = gson.fromJson(new FileReader("jsonResourses/snames.json"), NameData.class);

        //System.out.println(names.getNames().get(0));

        Random rand = new Random();
        int n = rand.nextInt(names.getNames().size()) + 0;

        return names.getNames().get(n);
    }

}

