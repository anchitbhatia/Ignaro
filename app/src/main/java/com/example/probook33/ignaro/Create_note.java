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

import com.example.probook33.ignaro.classes.NewNote;
import com.example.probook33.ignaro.classes.group;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Create_note extends AppCompatActivity {

    TextView groupname;
    EditText text;
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        groupname= (TextView) findViewById(R.id.groupname);
        text= (EditText) findViewById(R.id.text);

        groupname.setText("Post in "+GroupPage.grp.getGroupname());

        add= (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                NewNote note=new NewNote(owner,text.getText().toString(),GroupPage.grp.getG_id(),FirebaseAuth.getInstance().getCurrentUser().getUid().toString());

                                final DatabaseReference data =FirebaseDatabase.getInstance().getReference("notes");
                                String noteid= data.push().getKey();
                                data.child(noteid).setValue(note);


                                Toast.makeText(getApplicationContext(),"Note created successfully",Toast.LENGTH_SHORT).show();
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
}
