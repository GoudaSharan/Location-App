package com.example.mtap.locationapp.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.net.Authenticator;

public class AuthenticatorService extends Service {
    public AuthenticatorService() {
    }

    // Instance field that stores the authenticator object
    private com.example.mtap.locationapp.sync.Authenticator mAuthenticator;
    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new com.example.mtap.locationapp.sync.Authenticator(this);
    }
    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }

}
