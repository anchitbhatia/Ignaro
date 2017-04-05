package com.example.probook33.ignaro;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by chinmay on 4/4/17.
 */

public class NotifService extends Service {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    GoogleApiClient mGoogleApiClient;
    Location clocation;
    ArrayList<String> grps = new ArrayList<String>(),grpsid = new ArrayList<String>();
    appLocationService app ;


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public NotifService() {
        return;
    }

    public NotifService(String name) {
        return;
    }

    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        // Log.v("MyWebRequestService:","Working" );
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        // Log.v("MyWebRequestService:","Working" );
        return START_STICKY;
    }

    public void onTaskRemoved(Intent rootIntent) {
        // TODO Auto-generated method stub
        Intent restartService = new Intent(getApplicationContext(),
                this.getClass());
        restartService.setPackage(getPackageName());
        PendingIntent restartServicePI = PendingIntent.getService(

                getApplicationContext(), 1, restartService,
                PendingIntent.FLAG_ONE_SHOT);

        //Restart the service once it has been killed android


        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 100, restartServicePI);

    }

    public void onCreate() {
        // TODO Auto-generated method stub
        //grpsid= new ArrayList<String>();
        //LocationListener p = null;
        app= new appLocationService(getApplicationContext());
        String s1="Not Available";
        Location nwLocation = app.getLocation(LocationManager.NETWORK_PROVIDER);
        Location gpsLocation = app.getLocation(LocationManager.GPS_PROVIDER);
        DecimalFormat precision = new DecimalFormat("0.000000");
        if (nwLocation != null) {
            double latitude = nwLocation.getLatitude();
            double longitude = nwLocation.getLongitude();
            s1= String.valueOf(latitude)+","+String.valueOf(longitude);

        } else {
            Log.v("ServiceNETWORK","Not Granted");
        }
        if (gpsLocation != null) {
            double latitude = gpsLocation.getLatitude();
            latitude =(double) (Math.round(latitude * 100000d) / 100000d);
            double longitude = gpsLocation.getLongitude();
            longitude= (double)Math.round(longitude * 100000d) / 100000d;
            s1= String.valueOf(latitude)+","+String.valueOf(longitude);
            Log.v("ServiceLocationGPS:",s1);

        } else {
            Log.v("ServiceGPS","Not Granted");
        }
        NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify=new Notification.Builder
                (getApplicationContext()).setContentTitle("Title").setContentText(s1).
                setContentTitle("New Location").setSmallIcon(R.drawable.common_google_signin_btn_icon_dark).build();

        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.notify(0, notify);
        Thread handler = new Thread() {

            @Override
            public void run() {
                locationpoll();
            }
        };
        handler.start();
        try {
            handler.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.v("ServiceLocation2:", String.valueOf(grpsid));
    }


    public void locationpoll(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("membership");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userid = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid());
                Toast.makeText(getApplicationContext(), userid, Toast.LENGTH_SHORT).show();

                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    String u = String.valueOf(messageSnapshot.child("username").getValue());
                    Log.d("userid", userid);
                    Log.d("u retreived", u);
                    if (String.valueOf(userid).equals(String.valueOf(u))) {
                        String groupid = (String) messageSnapshot.child("g_id").getValue();
                        Log.d("retreived", String.valueOf(messageSnapshot.child("username").getValue()));
                        grpsid.add(groupid);
                        Log.d("groupid added ", groupid);
                    }

                                    }
                Log.v("ServiceLocation1:", String.valueOf(grpsid));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
            String ab = String.valueOf(Looper.myLooper() == Looper.getMainLooper());
        Log.v("ServiceThread:", ab);
        Log.v("ServiceLocation:", String.valueOf(grpsid));
        //String k = String.valueOf(FirebaseAuth.getInstance().getCurrentUser());
//       super.onCreate();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Log.v("ServiceUser:", String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid()));
        } else {
            //terminate activity
        }

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
