package com.example.probook33.ignaro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Group_settings extends AppCompatActivity {

    TextView groupname,admin,key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_group_settings);
        groupname= (TextView) findViewById(R.id.groupname);
        admin= (TextView) findViewById(R.id.admin);
        key= (TextView) findViewById(R.id.key);

        groupname.setText(GroupPage.grp.getGroupname());
        admin.setText(GroupPage.grp.getAdmin());
        key.setText(GroupPage.grp.getKey());
    }
}
