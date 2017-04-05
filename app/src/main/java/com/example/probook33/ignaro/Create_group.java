package com.example.probook33.ignaro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.probook33.ignaro.classes.Membership;
import com.example.probook33.ignaro.classes.group;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Create_group extends AppCompatActivity {

    Button create;
    EditText groupname,g_key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        setTitle("Create Group");

        groupname= (EditText) findViewById(R.id.groupname);
        g_key= (EditText) findViewById(R.id.key);
        //g_id= (EditText) findViewById(R.id.groupid);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference mydata = database.getReference("groups");
        final DatabaseReference mydata2 = database.getReference("membership");

        create=(Button)findViewById(R.id.createbtn);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gname= String.valueOf(groupname.getText());
                String gkey=String.valueOf(g_key.getText());
              //  String gid=String.valueOf(g_id.getText());
                String admin= String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid());

                //add group
                String groupID=mydata.push().getKey();
                group grp=new group(admin,gname,groupID,gkey);
                mydata.child(groupID).setValue(grp);

                //add membership
                String membershipid= mydata2.push().getKey();
                Membership m=new Membership(groupID,admin);
                mydata2.child(membershipid).setValue(m);

                Toast.makeText(getApplicationContext(),"Group created",Toast.LENGTH_SHORT).show();;
                Intent i=new Intent(Create_group.this,Dashboard.class);
                startActivity(i);


            }
        });

    }
}
