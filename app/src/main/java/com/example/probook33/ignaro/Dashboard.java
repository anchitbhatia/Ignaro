package com.example.probook33.ignaro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<String> grps,grpsid;
    ListView groupslv;
    ProgressDialog pd;
    TextView navbar_name,navbar_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Intent intent = new Intent(Dashboard.this, NotifService.class);
        startService(intent);
        
        pd = new ProgressDialog(this);
        pd.setMessage("Fetching details... ");
        pd.show();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

      //  navbar_name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
       // navbar_email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Dashboard.this,Create_group.class);
                startActivity(i);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        groupslv= (ListView) findViewById(R.id.groupslv);
        groupslv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(Dashboard.this,GroupPage.class);
                intent.putExtra("title",grps.get(position));
                startActivity(intent);
            }
        });
        grpsid=new ArrayList<>();
        grps=new ArrayList<>();
        final DatabaseReference mydata =FirebaseDatabase.getInstance().getReference("membership");
        mydata.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userid= String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid());
                Log.d("my id ",userid);
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    String u=String.valueOf(messageSnapshot.child("username").getValue());
                    Log.d("userid", userid);
                    Log.d("u retreived", u);
                    if(String.valueOf(userid).equals(String.valueOf(u))) {
                        String groupid = (String) messageSnapshot.child("g_id").getValue();
                        Log.d("retreived", String.valueOf(messageSnapshot.child("username").getValue()));
                        grpsid.add(groupid);
                        Log.d("groupid added ", groupid);
                    }

                    final DatabaseReference data =FirebaseDatabase.getInstance().getReference("groups");
                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot messageSnapshot: dataSnapshot.getChildren())
                            {
                                if(grpsid.contains(messageSnapshot.child("g_id").getValue()))
                                {
                                    String g=(String) messageSnapshot.child("groupname").getValue();
                                    if(!grps.contains(g)) {
                                        grps.add(g);

                                    }
                                }
                            }
                            pd.dismiss();
                        ArrayAdapter adapter=new ArrayAdapter(getApplicationContext(), R.layout.group_list_element,grps);
                            groupslv.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        finishAffinity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.creategroup) {
            Intent intent=new Intent(Dashboard.this,Create_group.class);
            startActivity(intent);

        } else if (id == R.id.mygroups) {

        }
        else if (id == R.id.signout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(Dashboard.this,Register.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
