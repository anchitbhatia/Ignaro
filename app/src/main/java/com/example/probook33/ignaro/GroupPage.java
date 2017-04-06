package com.example.probook33.ignaro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.probook33.ignaro.classes.group;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GroupPage extends AppCompatActivity {

    String title;
    ListView notelv;
    public final static group grp=new group();
    ArrayList<String> notes,nids,stat;
    ProgressDialog pd;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page);
        Bundle bundle = getIntent().getExtras();
        title= bundle.getString("title");
        setTitle(title);

        pd = new ProgressDialog(this);
        pd.setMessage("Fetching details... ");
        pd.show();

        notelv= (ListView) findViewById(R.id.notelv);
        notes=new ArrayList<>();
        nids=new ArrayList<>();
        stat=new ArrayList<>();

        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(GroupPage.this,Add_member.class);
                i.putExtra("admin",grp.getAdmin());
                i.putExtra("gid",grp.getG_id());
                i.putExtra("name",grp.getGroupname());
                i.putExtra("key",grp.getKey());
                startActivity(i);

            }
        });

        FloatingActionButton newnote = (FloatingActionButton) findViewById(R.id.newnote);
        newnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(GroupPage.this,Create_note.class);
                startActivity(i);

            }
        });

        FloatingActionButton delnote = (FloatingActionButton) findViewById(R.id.delnote);
        delnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        FloatingActionButton grpsetting = (FloatingActionButton) findViewById(R.id.grpsetting);
        grpsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(GroupPage.this,Group_settings.class);
                startActivity(i);

            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(GroupPage.this,Dashboard.class);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final DatabaseReference data = FirebaseDatabase.getInstance().getReference("groups");
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            String id;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String a="";
                String g="";
                String key="";
                //grp=new group();
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren())
                {
                    g=(String) messageSnapshot.child("groupname").getValue();
                    if(g.equals(title))
                    {
                        a=(String) messageSnapshot.child("admin").getValue();
                        id=(String) messageSnapshot.child("g_id").getValue();
                        key=(String) messageSnapshot.child("key").getValue();
                        grp.setKey(key);
                        grp.setG_id(id);
                        grp.setGroupname(title);
                        Log.d("key:",key);
                        Log.d("id:",id);
                        Log.d("name:",title);
                        break;
                    }

                }
                final DatabaseReference getadmin = FirebaseDatabase.getInstance().getReference("users");
                final String finalA = a;
                getadmin.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot notesSnapshot: dataSnapshot.getChildren())
                        {
                            String n= String.valueOf(notesSnapshot.getKey());
                            Log.d("n",n);
                            Log.d("a",finalA);
                            Log.d("Ans", String.valueOf(n.equals(finalA)));
                            if(n.equals(finalA))
                            {
                                String ad=String.valueOf(notesSnapshot.child("name").getValue());
                                grp.setAdmin(ad);
                                Log.d("admin:",ad);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                final DatabaseReference data2 = FirebaseDatabase.getInstance().getReference("notes");
                data2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot notesSnapshot: dataSnapshot.getChildren())
                        {
                            String nid = String.valueOf(notesSnapshot.getKey());
                            Log.v("Notekey",nid);

                            String n= String.valueOf(notesSnapshot.child("g_id").getValue());
                            if(n.equals(grp.getG_id()))
                            {
                                String note=String.valueOf(notesSnapshot.child("text").getValue());
                                String s=String.valueOf(notesSnapshot.child("status").getValue());
                                if(!notes.contains(note))
                                {
                                    notes.add(note);
                                    nids.add(nid);
                                    stat.add(s);
                                }
                            }
                        }
                        if(notes!=null) {
                           // ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.note_list_element, notes);
                            MyCustomAdapter adapter = new MyCustomAdapter(notes,nids,stat,getApplicationContext());
                            notelv.setAdapter(adapter);
                            pd.dismiss();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"No notes",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
