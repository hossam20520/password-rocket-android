package com.example.passwordrocket;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SingleView  extends AppCompatActivity {
    static  String name_singl, email_singl , password_singl;
     TextView name_s;
    TextView email_s;
    TextView password_s;
    Button add_single , delete_singl ;
    static String edit ;
    DBHelpear db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_single);

        name_s = (TextView) findViewById(R.id.name_s);
        email_s = (TextView) findViewById(R.id.email_s);
        add_single  = (Button) findViewById(R.id.edit_single);
        password_s = (TextView) findViewById(R.id.password_s);
        delete_singl = (Button) findViewById(R.id.delete_single);
        String c_name = Display.curren_name;

         db = new DBHelpear(this);
        ArrayList<String> array = db.getSingleContent(c_name);
        name_singl = array.get(0);
        email_singl = array.get(1);
        password_singl =  array.get(2);

        name_s.setText(name_singl);
        email_s.setText(email_singl);
        password_s.setText(password_singl);


        add_single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit = "edit";
                Intent intente = new Intent(getApplicationContext(), AddActivity.class);
                intente.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intente);
            }
        });

        delete_singl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               DiolgBox("Confirm Delete", "" , "Are You Sure?");

            }
        });

    }


    public void DiolgBox(String title, String icon , String setMessage){
        AlertDialog.Builder alertDiolgBuilder = new AlertDialog.Builder(SingleView.this);

        alertDiolgBuilder.setTitle(title);
        alertDiolgBuilder.setIcon(R.drawable.ic_launcher_background);
        alertDiolgBuilder.setMessage(setMessage);
        alertDiolgBuilder.setCancelable(false);
        alertDiolgBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.delete(name_singl);
                Intent intente = new Intent(getApplicationContext(), Display.class);
                intente.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intente);
            }
        });

        alertDiolgBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                
            }
        });
        AlertDialog alertDialog = alertDiolgBuilder.create();
        alertDialog.show();
    }
}