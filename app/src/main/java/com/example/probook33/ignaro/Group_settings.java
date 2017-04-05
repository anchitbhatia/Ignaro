package com.example.probook33.ignaro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Group_settings extends AppCompatActivity {

    TextView groupname,admin,key;
    Button manageloc,members;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_group_settings);
        groupname= (TextView) findViewById(R.id.groupname);
        admin= (TextView) findViewById(R.id.admin);
        key= (TextView) findViewById(R.id.key);
        manageloc= (Button) findViewById(R.id.locbtn);
        members= (Button) findViewById(R.id.members);

        manageloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Group_settings.this,Manage_grouplocations.class);
                startActivity(intent);
            }
        });

        groupname.setText(GroupPage.grp.getGroupname());
        admin.setText(GroupPage.grp.getAdmin());
        key.setText(GroupPage.grp.getKey());
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(Group_settings.this,GroupPage.class);
        i.putExtra("title", GroupPage.grp.getGroupname());
        startActivity(i);
    }
}
