package daoTests;

import org.junit.* ;

import java.sql.SQLException;
import java.util.ArrayList;

import access.PersonDao;
import model.Person;

import static org.junit.Assert.* ;

public class PersonDaoTest {

    @Before
    public void setUp() throws Exception {
        PersonDao access = new PersonDao();

        System.out.println("clearing");
        access.clear();

        System.out.println("adding bob");
        Person bob = new Person(
                "descendant",
                "1",
                "bob",
                "by",
                "m",
                "father",
                "mother",
                "spouse"
        );
        access.addNewPerson(bob);

        System.out.println("adding tom");
        Person tom = new Person("some_guy",
                "2",
                "tom",
                "bombadill",
                "m",
                "father",
                "mother",
                "spouse"
        );
        access.addNewPerson(tom);
//        System.out.println("getting person with id");
//        Person person = access.getPersonWithPersonID(test.getPersonID());
//        System.out.println(person.toString());
//
//        ArrayList<Person> People = new ArrayList<>();
//        System.out.println("getting descendants, descendant");
//        People = access.getPeopleWithDescendant(test.getDescendant());
//        for (int i = 0; i< People.size(); i++) {
//            System.out.println(People.get(i).toString());
//            System.out.println();
//        }
//
//
//        System.out.println("clearing");
//        access.clear();

        System.out.println("getting all Persons.");

        ArrayList<Person> People = new ArrayList<>();
        People = access.getAllPeople();
        for (int i = 0; i< People.size(); i++) {
            System.out.println(People.get(i).toString());
            System.out.println();
        }

        System.out.println("adding tom as bobs spouse: ");
        access.addSpouseOf(bob, tom);

        System.out.println("getting all Persons.");

        People = new ArrayList<>();
        People = access.getAllPeople();
        for (int i = 0; i< People.size(); i++) {
            System.out.println(People.get(i).toString());
            System.out.println();
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getPersonWithPersonID() throws SQLException {
        PersonDao access = new PersonDao();
        assertTrue(access.getPersonWithPersonID("1") != null);
        assertTrue(access.getPersonWithPersonID("2") != null);
    }

    @Test
    public void getPeopleWithDescendant() throws SQLException {
        PersonDao access = new PersonDao();
        ArrayList<Person> list = new ArrayList<>();

        assertTrue(list.size() == 0);

        list = access.getPeopleWithDescendant("some_guy");
        assertTrue(list.size() == 1);

        list.clear();
        assertTrue(list.size() == 0);

        list = access.getPeopleWithDescendant("descendant");
        assertTrue(list.size() == 1);


    }

    @Test
    public void addSpouseOf() throws SQLException {
        PersonDao access = new PersonDao();

        System.out.println("adding bobbett");
        Person bobbett = new Person(
                "some_guy",
                "11",
                "bobbet",
                "by",
                "f",
                "father",
                "mother",
                "spouse"
        );
        access.addNewPerson(bobbett);

        System.out.println("adding joe");
        Person joe = new Person("descendant",
                "22",
                "joe",
                "blo",
                "m",
                "father",
                "mother",
                "spouse"
        );
        access.addNewPerson(joe);

        access.addSpouseOf(joe,bobbett);

        assertTrue(access.getPersonWithPersonID("22") != null);
        assertTrue(access.getPersonWithPersonID("11") != null);

        System.out.println("joe id: " + access.getPersonWithPersonID("22").getPersonID() + "spouse id: " + access.getPersonWithPersonID("22").getSpouse());
        System.out.println("bobbett id: " + access.getPersonWithPersonID("11").getPersonID() + "spouse id: " + access.getPersonWithPersonID("11").getSpouse());


        assertTrue(access.getPersonWithPersonID("22").getSpouse().equals(access.getPersonWithPersonID("11").getPersonID()));
        assertTrue(access.getPersonWithPersonID("11").getSpouse().equals(access.getPersonWithPersonID("22").getPersonID()));

        System.out.println("deleting joe");
        access.deletePerson("22");
        assertTrue(access.getPersonWithPersonID("22") == null);
    }

    @Test
    public void clear() throws SQLException {
        PersonDao access = new PersonDao();
        ArrayList<Person> list = new ArrayList<>();

        list = access.getAllPeople();
        System.out.println("list size: " + list.size());
        assertTrue(list.size() == 2);

        access.clear();
        list = access.getAllPeople();
        assertTrue(list.size() == 0);
    }
}