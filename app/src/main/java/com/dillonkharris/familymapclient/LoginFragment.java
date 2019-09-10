package com.dillonkharris.familymapclient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import model.Client;
import response.EventsResponse;
import request.LoginRequest;
import response.PeopleResponse;
import model.Person;
import proxy.Proxy;
import response.RegisterLoginResponse;
import request.RegisterRequest;

public class LoginFragment extends Fragment {

    private Button mRegisterButton;
    private Button mLoginButton;
    private RadioGroup mRadioGroup;
    private EditText mPort;
    private EditText mHost;
    private EditText mUserName;
    private EditText mPassword;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmail;
    private RadioButton mMaleButton;
    private RadioButton mFemaleButton;

    private String gender = "";
    private boolean hostEdited = false;
    private boolean portEdited = false;
    private boolean userNameEdited = false;
    private boolean passwordEdited = false;
    private boolean firstNameEdited = false;
    private boolean lastNameEdited = false;
    private boolean eMailEdited = false;
    private boolean genderEdited = false;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        LoginFragment mLoginFragment = this;
        Client.getInstance().setLoginFragment(mLoginFragment);
        Client.getInstance().setProxy(new Proxy());
    }
    @Override
    public void onResume() {
        super.onResume();

    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem search = menu.findItem(R.id.search);
        search.setVisible(false);

        MenuItem filter = menu.findItem(R.id.filter);
        filter.setVisible(false);

        MenuItem settings = menu.findItem(R.id.settings);
        settings.setVisible(false);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login,container,false);

        setHasOptionsMenu(true);

        mHost            = v.findViewById(R.id.hostInput);
        mPort            = v.findViewById(R.id.portInput);
        mUserName        = v.findViewById(R.id.userNameInput);
        mPassword        = v.findViewById(R.id.passwordInput);
        mFirstName       = v.findViewById(R.id.firstNameInput);
        mLastName        = v.findViewById(R.id.lastNameInput);
        mEmail           = v.findViewById(R.id.emailInput);
        mRadioGroup      = v.findViewById(R.id.radioGroup);
        mMaleButton      = v.findViewById(R.id.radioButtonMale);
        mFemaleButton    = v.findViewById(R.id.radioButtonFemale);

        mRegisterButton  = v.findViewById(R.id.registerButton);
        mLoginButton     = v.findViewById(R.id.loginButton);

        setUpViews();

        return v;
    }

    private void setUpViews() {
        setUpRegisterButton();
        setUpLoginButton();
        setUpHost();
        setUpPort();
        setUpUserName();
        setUpPassword();
        setUpFirstName();
        setUpLastName();
        setUpEMail();
        setUpGender();
    }
    private void switchToMap() {
        // Create fragment and give it an argument specifying the article it should show
        MapFragment mapFrag = new MapFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, mapFrag);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    private void setUpRegisterButton() {
        mRegisterButton.setEnabled(false);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Client.getInstance().setHostAddress(mHost.getText().toString());
                Client.getInstance().setPortNumber(mPort.getText().toString());

                RegisterRequest registerRequest = new RegisterRequest(
                        mUserName.getText().toString(),
                        mPassword.getText().toString(),
                        mEmail.getText().toString(),
                        mFirstName.getText().toString(),
                        mLastName.getText().toString(),
                        gender
                );

                new RegisterTask().execute(registerRequest);
            }
        });
    }
    private void setUpLoginButton() {
        mLoginButton.setEnabled(false);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Client.getInstance().setPortNumber(mPort.getText().toString());
                Client.getInstance().setHostAddress(mHost.getText().toString());

                LoginRequest loginRequest = new LoginRequest(
                        mUserName.getText().toString(),
                        mPassword.getText().toString()
                );
                new LoginTask().execute(loginRequest);
            }
        });
    }
    private void setUpHost() {
        mHost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mHost.getText().length() > 0){
                    hostEdited = true;
                }
                else {
                    hostEdited = false;
                }
                boolean loginEnabled = checkLoginEnabled(
                        hostEdited,
                        portEdited,
                        userNameEdited,
                        passwordEdited
                );
                mLoginButton.setEnabled(loginEnabled);
                boolean registerEnabled = checkRegisterEnabled(
                        loginEnabled,
                        firstNameEdited,
                        lastNameEdited,
                        eMailEdited,
                        genderEdited
                );
                mRegisterButton.setEnabled(registerEnabled);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void setUpPort() {
        mPort.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (mPort.getText().length() > 0){
                    portEdited = true;
                }
                else {
                    portEdited = false;
                }
                boolean loginEnabled = checkLoginEnabled(
                        hostEdited,
                        portEdited,
                        userNameEdited,
                        passwordEdited
                );
                mLoginButton.setEnabled(loginEnabled);
                boolean registerEnabled = checkRegisterEnabled(
                        loginEnabled,
                        firstNameEdited,
                        lastNameEdited,
                        eMailEdited,
                        genderEdited
                );
                mRegisterButton.setEnabled(registerEnabled);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void setUpUserName() {
        mUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (mUserName.getText().length() > 0){
                    userNameEdited = true;
                }
                else {
                    userNameEdited= false;
                }
                boolean loginEnabled = checkLoginEnabled(
                        hostEdited,
                        portEdited,
                        userNameEdited,
                        passwordEdited
                );
                mLoginButton.setEnabled(loginEnabled);
                boolean registerEnabled = checkRegisterEnabled(
                        loginEnabled,
                        firstNameEdited,
                        lastNameEdited,
                        eMailEdited,
                        genderEdited
                );
                mRegisterButton.setEnabled(registerEnabled);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void setUpPassword() {
        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (mPassword.getText().length() > 0){
                    passwordEdited = true;
                }
                else {
                    passwordEdited = false;
                }

                boolean loginEnabled = checkLoginEnabled(
                        hostEdited,
                        portEdited,
                        userNameEdited,
                        passwordEdited
                );
                mLoginButton.setEnabled(loginEnabled);
                boolean registerEnabled = checkRegisterEnabled(
                        loginEnabled,
                        firstNameEdited,
                        lastNameEdited,
                        eMailEdited,
                        genderEdited
                );
                mRegisterButton.setEnabled(registerEnabled);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void setUpFirstName() {
        mFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mFirstName.getText().length() > 0){
                    firstNameEdited = true;
                }
                else {
                    firstNameEdited = false;
                }

                boolean loginEnabled = checkLoginEnabled(
                        hostEdited,
                        portEdited,
                        userNameEdited,
                        passwordEdited
                );
                mLoginButton.setEnabled(loginEnabled);
                boolean registerEnabled = checkRegisterEnabled(
                        loginEnabled,
                        firstNameEdited,
                        lastNameEdited,
                        eMailEdited,
                        genderEdited
                );
                mRegisterButton.setEnabled(registerEnabled);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void setUpLastName() {
        mLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mLastName.getText().length() > 0){
                    lastNameEdited = true;
                }
                else {
                    lastNameEdited = false;
                }

                boolean loginEnabled = checkLoginEnabled(
                        hostEdited,
                        portEdited,
                        userNameEdited,
                        passwordEdited
                );
                mLoginButton.setEnabled(loginEnabled);
                boolean registerEnabled = checkRegisterEnabled(
                        loginEnabled,
                        firstNameEdited,
                        lastNameEdited,
                        eMailEdited,
                        genderEdited
                );
                mRegisterButton.setEnabled(registerEnabled);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void setUpEMail() {
        mEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mEmail.getText().length() > 0){
                    eMailEdited = true;
                }
                else {
                    eMailEdited = false;
                }
                boolean loginEnabled = checkLoginEnabled(
                        hostEdited,
                        portEdited,
                        userNameEdited,
                        passwordEdited
                );
                mLoginButton.setEnabled(loginEnabled);
                boolean registerEnabled = checkRegisterEnabled(
                        loginEnabled,
                        firstNameEdited,
                        lastNameEdited,
                        eMailEdited,
                        genderEdited
                );
                mRegisterButton.setEnabled(registerEnabled);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void setUpGender() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                genderEdited = true;
                boolean loginEnabled = checkLoginEnabled(
                        hostEdited,
                        portEdited,
                        userNameEdited,
                        passwordEdited
                );
                mLoginButton.setEnabled(loginEnabled);
                boolean registerEnabled = checkRegisterEnabled(
                        loginEnabled,
                        firstNameEdited,
                        lastNameEdited,
                        eMailEdited,
                        genderEdited
                );
                mRegisterButton.setEnabled(registerEnabled);
                if (group.getCheckedRadioButtonId() == mFemaleButton.getId()) {
                    gender = "F";
                }
                else {
                    gender = "M";
                }
            }
        });
    }
    private boolean checkLoginEnabled(boolean host, boolean port, boolean userName, boolean password) {
        if (host && port && userName && password) {
            return true;
        }
        else {
            return false;
        }
    }
    private boolean checkRegisterEnabled(boolean loginMethod, boolean firstName,
                                         boolean lastName, boolean eMail, boolean gender) {

        if (loginMethod && firstName && lastName && eMail && gender) {
            return true;
        }
        else {
            return false;
        }
    }

    private static final String TAG = "LoginFragment";

    private class RegisterTask extends AsyncTask<RegisterRequest,Void, RegisterLoginResponse> {
        @Override
        protected RegisterLoginResponse doInBackground(RegisterRequest... params) {
            RegisterLoginResponse resp = new RegisterLoginResponse();
            try {
               resp = Client.getInstance().getProxy().register(params[0]);

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
                    Toast.makeText(LoginFragment.super.getContext(),
                            "Register Failed",
                            Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }

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
                    Toast.makeText(LoginFragment.super.getContext(),
                            "Login Failed",
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
                Client.getInstance().setEventTypes(events.getEvents());
                Client.getInstance().initiateTypeToBool();
                Client.getInstance().setFamily(family.getPeople());
                Client.getInstance().setUser(person);
                Client.getInstance().setUsername(mUserName.getText().toString());
                Client.getInstance().setPassword(mPassword.getText().toString());
                Client.getInstance().setPortNumber(mPort.getText().toString());
                Client.getInstance().setHostAddress(mHost.getText().toString());
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            return person;
        }
        @Override
        protected void onPostExecute(Person person) {
            switchToMap();
        }
    }

}
