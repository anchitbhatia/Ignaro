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
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ArrayAdapter;
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
    static Handler handler;
    private double latitude=0,longitude=0;
    private String lat="",lon="",txt="";
    private Double l1=0.0;
    private Double l2=0.0;
    private ArrayList<String> grps = new ArrayList<String>(),grpsid = new ArrayList<String>();
    private ArrayList<String> ntext = new ArrayList<String >(),nlat=new ArrayList<String>(),nlon=new ArrayList<String>();
    private appLocationService app ;


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
        if (nwLocation != null) {
            latitude = nwLocation.getLatitude();
            longitude = nwLocation.getLongitude();
            latitude =(Math.round(latitude * 1000d) / 1000d);
            longitude= Math.round(longitude * 1000d) / 1000d;
            s1= String.valueOf(latitude)+","+String.valueOf(longitude);

        } else {
            Log.v("ServiceNETWORK","Not Granted");
        }
       /* if (gpsLocation != null) {
            latitude = gpsLocation.getLatitude();
            latitude =(double) (Math.round(latitude * 1000d) / 1000d);
            longitude = gpsLocation.getLongitude();
            longitude= (double)Math.round(longitude * 1000d) / 1000d;
            s1= String.valueOf(latitude)+","+String.valueOf(longitude);
            Log.v("ServiceLocationGPS:",s1);

        } else {
            Log.v("ServiceGPS","Not Granted");
        }
*/
        String tmp = String.valueOf(android.os.Process.getThreadPriority(android.os.Process.myTid()));
        Log.d("ServiceThreadId", tmp);
        Handler handler = new Handler();
        Runnable thread = new Runnable() {
            @Override
            public void run() {
                locationpoll();
            }
        };
        handler.post(thread);
        Runnable thread2 = new Runnable() {
            @Override
            public void run() {
                reset();
            }
        };
        handler.postDelayed(thread2,120000);
        //
        //
    }

    public void reset(){
        stopService(new Intent(this, NotifService.class));
        startService(new Intent(this, NotifService.class));
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
                for(final String s1 : grpsid) {
                    final String gname=s1;
                    final DatabaseReference data = FirebaseDatabase.getInstance().getReference("notes");
                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                                Log.v("ServiceS1:", String.valueOf(s1));
                                lat =String.valueOf(messageSnapshot.child("lat").getValue());
                                lon =String.valueOf(messageSnapshot.child("lon").getValue());
                                Boolean x =(lat.equals("null"));
                                Log.v("ServiceLatx:", String.valueOf(x));
                                x = x | (lon.equals("null"));
                                Log.v("ServiceLonx:", String.valueOf(x));
                                Log.v("ServiceLon:", String.valueOf(lon));
                                Log.v("ServiceLatit:", String.valueOf(latitude));
                                Log.v("ServiceLongi:", String.valueOf(longitude));
                                Log.v("ServiceLat:", String.valueOf(x));
                                if(!x) {
                                    l1 = Double.valueOf(lat);
                                    l1=Math.round(l1 * 1000d) / 1000d;
                                    l2 = Double.valueOf(lon);
                                    l2=Math.round(l2 * 1000d) / 1000d;
                                }
                                Log.v("ServiceLat1:", String.valueOf(l1));
                                Log.v("ServiceLon1:", String.valueOf(l2));
                                txt =String.valueOf(messageSnapshot.child("text").getValue());
                             //   lat =String.valueOf(messageSnapshot.child("lat").getValue());
                                Log.v("ServiceNote:", String.valueOf(txt));

                               // lat = (String) messageSnapshot.child("lat").getValue();
                                  //  lon = (String) messageSnapshot.child("lon").getValue();
                                  //  txt = (String) messageSnapshot.child("txt").getValue();

                                    if (l1==latitude && l2==longitude) {
                                        Log.v("ServicePushing",txt);
                                        notification(txt,gname);
                                    }

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                Log.v("ServiceLocation1:", String.valueOf(grpsid));
                //notification();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

    public void notification(String n,String p){
        // TODO Auto-generated method stub
        //grpsid= new ArrayList<String>();
        //LocationListener p = null;
        String task=n;
        app= new appLocationService(getApplicationContext());
        NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify=new Notification.Builder
                (getApplicationContext()).setContentTitle("Title").setContentText(task).
                setContentTitle("Nearby Incomplete Task in group"+p).setSmallIcon(R.drawable.common_google_signin_btn_icon_dark).build();

        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.notify(0, notify);
        return;
    }
}
