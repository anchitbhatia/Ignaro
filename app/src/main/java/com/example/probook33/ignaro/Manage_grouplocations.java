package com.example.probook33.ignaro;

import android.content.Intent;
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
    Button add;
    GroupLocation grploc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_grouplocations);

        grpname= (TextView) findViewById(R.id.groupname);
        type= (EditText) findViewById(R.id.type);
        lat= (EditText) findViewById(R.id.lat);
        lon= (EditText) findViewById(R.id.lon);
        add= (Button) findViewById(R.id.add);
        grpname.setText(grp.getGroupname());


        //grploc.setG_id(grp.getG_id());
        //grploc.getG_name(grp.getGroupname());
        //grploc.setLat(String.valueOf(lat.getText()));
        //grploc.setLon(String.valueOf(lon.getText()));
        //grploc.setName(String.valueOf(type.getText()));


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                Toast.makeText(getApplicationContext(),type.getText()+" added",Toast.LENGTH_SHORT).show();;
                Intent i=new Intent(Manage_grouplocations.this,Group_settings.class);
                startActivity(i);
            }
        });

    }

}
