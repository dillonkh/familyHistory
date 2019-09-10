package com.dillonkharris.familymapclient;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends SingleFragmentActivity {

    private static final int REQUEST_ERROR = 0;
    private MainActivity mMainActivity;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fragment_map, menu);

        return true;
    }

    @Override
    protected Fragment createFragment() {

        return new LoginFragment();
    }

    @Override
    protected void onResume(){
        super.onResume();
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int errorCode = apiAvailability.isGooglePlayServicesAvailable(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            System.out.println("got the extra!!!");

            MapFragment mapFrag = new MapFragment();

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, mapFrag);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }

        if(errorCode != ConnectionResult.SUCCESS){
            Dialog errorDialog = apiAvailability.getErrorDialog(this, errorCode, REQUEST_ERROR,
                    new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            finish();
                        }
                    });
            errorDialog.show();
        }

    }
}

