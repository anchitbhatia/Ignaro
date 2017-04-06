package com.example.probook33.ignaro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.probook33.ignaro.classes.GroupLocation;
import com.example.probook33.ignaro.classes.NewNote;
import com.example.probook33.ignaro.classes.group;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Create_note extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView groupname;
    EditText text,lat,lon;
    Button add;
    ProgressDialog pd;
    Spinner spinner;
    GroupLocation grploc;
    ArrayList<GroupLocation> grplocation;
    String names[];
    ArrayAdapter<String> dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        setTitle("New Note");

        groupname= (TextView) findViewById(R.id.groupname);
        text= (EditText) findViewById(R.id.text);
        lat= (EditText) findViewById(R.id.lat);
        lon= (EditText) findViewById(R.id.lon);

        spinner= (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        groupname.setText("Post in "+GroupPage.grp.getGroupname());

        grplocation=new ArrayList<>();

        final DatabaseReference loc = FirebaseDatabase.getInstance().getReference("locations");
        loc.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot locSnapshot: dataSnapshot.getChildren())
                {
                    String n= String.valueOf(locSnapshot.child("name").getValue());
                    String latitude= String.valueOf(locSnapshot.child("lat").getValue());
                    String longitude= String.valueOf(locSnapshot.child("lon").getValue());
                    String groupid= String.valueOf(locSnapshot.child("g_id").getValue());

                    grploc=new GroupLocation(groupid,String.valueOf(groupname.getText()),n,latitude,longitude);
                    Log.d("n",grploc.getName());
                    Log.d("latitude",grploc.getLat());
                    Log.d("long",grploc.getLon());
                    Log.d("gid",grploc.getG_id());
                    grplocation.add(grploc);
                }
                names=new String[grplocation.size()];
                for (int i = 0; i <grplocation.size(); i++)
                {
                    names[i] = grplocation.get(i).getName();
                }
                dataAdapter= new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, names);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                spinner.setAdapter(dataAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });



        add= (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(Create_note.this);
                pd.setMessage("Fetching details... ");
                pd.show();
                final DatabaseReference data = FirebaseDatabase.getInstance().getReference("users");
                data.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String currentuser= String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        Log.d("current user",currentuser);
                        for (DataSnapshot messageSnapshot: dataSnapshot.getChildren())
                        {
                            String g= messageSnapshot.getKey();
                            Log.d("u",messageSnapshot.getKey().toString());

                            if(messageSnapshot.getKey().toString().equals(currentuser))
                            {
                                String owner= String.valueOf(messageSnapshot.child("name").getValue());
                                Log.d("ans", GroupPage.grp.getG_id());
                                NewNote note=new NewNote(owner,text.getText().toString(),GroupPage.grp.getG_id(),FirebaseAuth.getInstance().getCurrentUser().getUid().toString(),lat.getText().toString(),lon.getText().toString(),"pending");

                                Log.v("Note",String.valueOf(note));

                                final DatabaseReference data =FirebaseDatabase.getInstance().getReference("notes");
                                String noteid= data.push().getKey();
                                data.child(noteid).setValue(note);
                                text.setText("");
                                lat.setText("");
                                lon.setText("");
                                pd.dismiss();
                                Toast.makeText(getApplicationContext(),"Note created successfully",Toast.LENGTH_SHORT).show();

                                Intent i=new Intent(Create_note.this,GroupPage.class);
                                i.putExtra("title", GroupPage.grp.getGroupname());
                                startActivity(i);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(Create_note.this,GroupPage.class);
        i.putExtra("title", GroupPage.grp.getGroupname());
        startActivity(i);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        lat.setText(grplocation.get(position).getLat());
        lon.setText(grplocation.get(position).getLon());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
