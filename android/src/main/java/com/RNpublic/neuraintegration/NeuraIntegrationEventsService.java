package com.RNpublic.neuraintegration;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.neura.standalonesdk.events.NeuraEvent;
import com.neura.standalonesdk.events.NeuraEventCallBack;
import com.neura.standalonesdk.events.NeuraPushCommandFactory;

public class NeuraIntegrationEventsService extends FirebaseMessagingService {

    private Context context;

    public NeuraIntegrationEventsService() {}
    public NeuraIntegrationEventsService(Context context) {
        this.context = context;
    }

    @Override
    public void onMessageReceived(RemoteMessage message) {
        Log.i(getClass().getSimpleName(), "onMessageReceived");
        if (context == null) {
            context = getApplicationContext();
        }
        boolean isNeuraPush = NeuraPushCommandFactory.getInstance().isNeuraPush(context, message.getData(), new NeuraEventCallBack() {
            @Override
            public void neuraEventDetected(NeuraEvent event) {
                String eventText = (event != null) ? event.toString() : "couldn't parse data";
                Log.i(getClass().getSimpleName(), "received Neura event - " + eventText);
            }
        });

        if (isNeuraPush) {
            Log.i(getClass().getSimpleName(), "received Neura event should have been handled");
        } else {
            Log.i(getClass().getSimpleName(), "received non Neura event should have been ignored");
        }
    }

    @Override
    public void onNewToken(String token) {
        Log.i(getClass().getSimpleName(), "Refreshed token: " + token);
        // Important! Update Neura with your new firebase token
        NeuraIntegrationSingleton.getInstance().getNeuraApiClient().registerFirebaseToken(token);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
    }
}