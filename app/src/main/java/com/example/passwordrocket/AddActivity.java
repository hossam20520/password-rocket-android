package com.example.passwordrocket;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {
    DBHelpear db;
    EditText name , email , password;
    Button save,showa , update ;
    String name_txt,email_txt,password_txt;
    int counter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_view);
        db = new DBHelpear(this);
        name = (EditText) findViewById(R.id.name_a);
        email = (EditText) findViewById(R.id.email_a);
        password = (EditText) findViewById(R.id.password_a);

        save = (Button) findViewById(R.id.save_a);
        showa = (Button) findViewById(R.id.show);
        update = (Button) findViewById(R.id.Update);

         update.setVisibility(View.GONE);
        if(SingleView.edit == "edit"){
            save.setVisibility(View.GONE);
            update.setVisibility(View.VISIBLE);
            name.setText(SingleView.name_singl);
            email.setText(SingleView.email_singl);
            password.setText(SingleView.password_singl);
            SingleView.edit = "null";

        }else{
            update.setVisibility(View.GONE);
            save.setVisibility(View.VISIBLE);
            name.setHint("Website");
            email.setHint("Email");
            password.setHint("Password");
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_txt = name.getText().toString();
                email_txt = email.getText().toString();
                password_txt = password.getText().toString();


                check(name_txt);
                name.setText(" ");
                email.setText(" ");
                password.setText(" ");
                Intent intente = new Intent(getApplicationContext(), Display.class);
                intente.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intente);

                //Toast.makeText(getApplicationContext() , "current_name "+ ve, Toast.LENGTH_SHORT).show();

            }
        });

        showa.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intente = new Intent(getApplicationContext(), Display.class);
                intente.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intente);
            }
        });


update.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        name_txt = name.getText().toString();
        email_txt = email.getText().toString();
        password_txt = password.getText().toString();
        String x = db.Update(SingleView.name_singl,email_txt,password_txt, name_txt);
        Intent intente = new Intent(getApplicationContext(), Display.class);
        intente.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intente);

    }
});




    }



    private void check(String name) {
        int ve =  db.get_name(name);
        if(ve <= 0){

        }else{

            counter++;
            name_txt = name_txt+" ("+ve+")";
        }


        db.insertContent(name_txt, email_txt, password_txt);



    }

    @Override
    protected  void  onPause(){
        super.onPause();
//        name.setText(" ");
//        email.setText(" ");
//        password.setText(" ");
//        name.setHint("Website");
//        email.setHint("Email");
//        password.setHint("Password");

    }





}
