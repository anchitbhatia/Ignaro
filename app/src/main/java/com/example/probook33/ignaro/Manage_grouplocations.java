package com.example.probook33.ignaro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.probook33.ignaro.classes.GroupLocation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.probook33.ignaro.GroupPage.grp;

public class Manage_grouplocations extends AppCompatActivity {

    EditText type,lon,lat;
    TextView grpname;
    Button add,fetch;
    GroupLocation grploc;
    ProgressDialog pd;
    appLocationService app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_grouplocations);

        grpname= (TextView) findViewById(R.id.groupname);
        type= (EditText) findViewById(R.id.type);
        lat= (EditText) findViewById(R.id.lat);
        lon= (EditText) findViewById(R.id.lon);
        add= (Button) findViewById(R.id.add);
        fetch = (Button) findViewById(R.id.fetch);
        grpname.setText(grp.getGroupname());

        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app= new appLocationService(getApplicationContext());
                Location nwLocation = app.getLocation(LocationManager.NETWORK_PROVIDER);
                if (nwLocation != null) {
                    double latitude = nwLocation.getLatitude();
                    double longitude = nwLocation.getLongitude();
                    latitude =(Math.round(latitude * 1000d) / 1000d);
                    longitude= Math.round(longitude * 1000d) / 1000d;
                    lat.setText(String.valueOf(latitude));
                    lon.setText(String.valueOf(longitude));
                } else {
                    Toast.makeText(getApplicationContext(),"Failed to get Location at this time.Try after some time",Toast.LENGTH_SHORT).show();;
                }
            }
        });
        //grploc.setG_id(grp.getG_id());
        //grploc.getG_name(grp.getGroupname());
        //grploc.setLat(String.valueOf(lat.getText()));
        //grploc.setLon(String.valueOf(lon.getText()));
        //grploc.setName(String.valueOf(type.getText()));


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pd = new ProgressDialog(Manage_grouplocations.this);
                //pd.setMessage("Fetching details... ");
                //pd.show();
                grploc=new GroupLocation(grp.getG_id(),grp.getGroupname(),String.valueOf(type.getText()),String.valueOf(lat.getText()),String.valueOf(lon.getText()));
                Log.d("g_id",grploc.getG_id());
                Log.d("g_name",grploc.getG_name());
                Log.d("name",grploc.getName());
                Log.d("lat",grploc.getLat());
                Log.d("g_lon",grploc.getLon());

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference mydata = database.getReference("locations");
                String locID=mydata.push().getKey();
                mydata.child(locID).setValue(grploc);
                type.setText("");
                lon.setText("");
                lat.setText("");
                //pd.dismiss();
                Toast.makeText(getApplicationContext(),"Location Added",Toast.LENGTH_SHORT).show();;
                Intent i=new Intent(Manage_grouplocations.this,Group_settings.class);
                startActivity(i);
            }
        });

    }

}
