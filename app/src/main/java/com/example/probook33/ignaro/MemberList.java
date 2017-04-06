package com.example.probook33.ignaro;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Set;

public class MemberList extends AppCompatActivity {
    ArrayList<String> grps=new ArrayList<String>(),grpid,mem=new ArrayList<String>();
    ListView memLv;
    String s;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);

        GroupPage g = new GroupPage();
        ArrayList<String> notes,nids,stat;
        s = GroupPage.grp.getG_id();
        memLv= (ListView) findViewById(R.id.meml);
        pd = new ProgressDialog(this);
        pd.setMessage("Fetching details... ");
        pd.show();
        final DatabaseReference mydata = FirebaseDatabase.getInstance().getReference("membership");
        mydata.addListenerForSingleValueEvent(new ValueEventListener() {
                                                  @Override
                                                  public void onDataChange(DataSnapshot dataSnapshot) {


                                                      for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                                                          String u = String.valueOf(messageSnapshot.child("username").getValue());
                                                          String groupid = (String) messageSnapshot.child("g_id").getValue();
                                                          if (s.equals(groupid)) {
                                                              Log.v("retreived", String.valueOf(messageSnapshot.child("username").getValue()));
                                                              if(!grps.contains(u)){grps.add(u);}
                                                              Log.v("groupid added ", u);
                                                          }


                                                          final DatabaseReference data = FirebaseDatabase.getInstance().getReference("users");
                                                          data.addListenerForSingleValueEvent(new ValueEventListener() {
                                                              @Override
                                                              public void onDataChange(DataSnapshot dataSnapshot) {
                                                                  for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                                                                      if (grps.contains(messageSnapshot.getKey())) {
                                                                          String g = (String) messageSnapshot.child("name").getValue();
                                                                          Log.v("Added",g);
                                                                             if(!mem.contains(g)) mem.add(g);


                                                                      }
                                                                  }

                                                                  GroupCustomAdapter adapter=new GroupCustomAdapter(mem,getApplicationContext());
                                                                  memLv.setAdapter(adapter);
                                                                  pd.dismiss();
                                                              }

                                                              @Override
                                                              public void onCancelled(DatabaseError databaseError) {

                                                              }
                                                          });
                                                      }

                                                  }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
