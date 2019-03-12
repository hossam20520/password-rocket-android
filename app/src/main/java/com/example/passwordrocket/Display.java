package com.example.passwordrocket;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Display extends AppCompatActivity {
    ListView listView;
    DBHelpear db;
    ImageButton add_aa;

    static String curren_name , new_txt;
    public static int signed ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);

        add_aa = (ImageButton ) findViewById(R.id.add_aa);
        listView = (ListView) findViewById(R.id.list_view);
        db = new DBHelpear(this);
        final ArrayList<String> arrr = db.getAllContacts();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1 ,arrr);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                curren_name = arrr.get(position);
//                Toast.makeText(Display.this, " "+ position + " " + x  , Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), SingleView.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        add_aa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intenta = new Intent(getApplicationContext(), AddActivity.class);
                intenta.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intenta);
            }
        });



    }


}