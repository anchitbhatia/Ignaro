package com.example.probook33.ignaro;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.probook33.ignaro.classes.Membership;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Add_member extends AppCompatActivity {

    private String admin;
    private String g_id;
    private String gname;
    private String key;
    TextView groupname;
    EditText username,inputkey;
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        Bundle bundle = getIntent().getExtras();
        admin = bundle.getString("admin");
        g_id = bundle.getString("gid");
        gname = bundle.getString("name");
        key = bundle.getString("key");

        groupname= (TextView) findViewById(R.id.groupname);
        username= (EditText) findViewById(R.id.username);
        inputkey= (EditText) findViewById(R.id.key);
        add= (Button) findViewById(R.id.add);

        groupname.setText(gname);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference mydata =FirebaseDatabase.getInstance().getReference("users");
                mydata.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!String.valueOf(inputkey.getText()).equals(key))
                        {
                            Toast.makeText(getApplicationContext(),"Wrong key !",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            int flag=0;
                            String user_key="";
                            for (DataSnapshot userSnapshot: dataSnapshot.getChildren())
                            {
                                Log.d("username : ", (String) userSnapshot.child("u_id").getValue());
                                Log.d("entered : ", String.valueOf(username.getText()));
                                Log.d("answer ", String.valueOf(String.valueOf(userSnapshot.child("u_id").getValue()).equals(String.valueOf(username.getText()))));
                                if(String.valueOf(userSnapshot.child("u_id").getValue()).equals(String.valueOf(username.getText())))
                                {
                                    flag = 1;
                                    user_key=userSnapshot.getKey();
                                    break;
                                }
                            }
                            if(flag==0)
                            {
                                Toast.makeText(getApplicationContext(),"Username does not exist !",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                final DatabaseReference data =FirebaseDatabase.getInstance().getReference("membership");
                                String membershipid= data.push().getKey();
                                Membership m=new Membership(g_id,user_key);
                                data.child(membershipid).setValue(m);

                                Toast.makeText(getApplicationContext(),username.getText()+" added to "+gname,Toast.LENGTH_SHORT).show();;
                                Intent intent=new Intent(Add_member.this,GroupPage.class);
                                intent.putExtra("title",gname);
                                startActivity(intent);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // ...
                    }
                });
            }
        });
    }
}
