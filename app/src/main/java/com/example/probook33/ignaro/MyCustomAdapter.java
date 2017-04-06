package com.example.probook33.ignaro;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by probook@33 on 06-04-2017.
 */

public class MyCustomAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private ArrayList<String> list1 = new ArrayList<String>();
    private ArrayList<String> list2 = new ArrayList<String>();
    private Context context;
    private ProgressDialog pd;


    public MyCustomAdapter(ArrayList<String> list,  ArrayList<String> list1,ArrayList<String> list2,Context context) {
        this.list = list;
        this.list1=list1;
        this.list2=list2;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.note_list_element, null);
        }

        //Handle TextView and display string from your list
        final CheckBox addBtn = (CheckBox)view.findViewById(R.id.add_btn);

        final TextView listItemText = (TextView)view.findViewById(R.id.tv);
        String s=list2.get(position);
        listItemText.setText(list.get(position));
        Log.v("ServiceEq",s);
        if(!s.equals("pending")){
            listItemText.setPaintFlags(listItemText.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

        }
        else{
            listItemText.setPaintFlags(listItemText.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
        //Handle buttons and add onClickListeners



        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something

                String s=list2.get(position);
                if(s.equals("complete")){
                    return;
                }

                listItemText.setPaintFlags(listItemText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                //Toast.makeText(context, String.valueOf(listItemText.getText()), Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "Task marked completed!", Toast.LENGTH_SHORT).show();
                Log.v("Notes",list1.get(position));
                final DatabaseReference data2 = FirebaseDatabase.getInstance().getReference("notes").child(list1.get(position));
                Map<String, Object> updates = new HashMap<String, Object>();
                updates.put("status","complete");
                data2.updateChildren(updates);
                notifyDataSetChanged();
            }
        });

        return view;
    }
}
