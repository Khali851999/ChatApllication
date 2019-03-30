package com.example.chatapllication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class REGISTER extends AppCompatActivity {

    TextView tvalready;
    EditText etemailreg;
    EditText etpassreg;
    EditText etusernamereg;
    Button btnreg;

    FirebaseAuth mAuth;

    DatabaseReference mRef;
    ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        tvalready= findViewById(R.id.tvalreadylogin);
        etemailreg = findViewById(R.id.etemailreg);
        etpassreg = findViewById(R.id.etpassreg);
        etusernamereg=findViewById(R.id.etusenamereg);
        btnreg = findViewById(R.id.btnreg);

        mAuth = FirebaseAuth.getInstance();
        mRef= FirebaseDatabase.getInstance().getReference().child("Users");
        mDialog = new ProgressDialog(this);


        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String emailreg = etemailreg.getText().toString().trim();
                final String passreg = etpassreg.getText().toString().trim();
                final String username = etusernamereg.getText().toString().trim();
                if (emailreg.isEmpty() || passreg.isEmpty() || username.isEmpty()) {
                    Toast.makeText(REGISTER.this, "Fields Cannot be Empty", Toast.LENGTH_SHORT).show();
                }

                else{
                //Registering Username and pass on firebase
                mDialog.setMessage("Processing");
                mDialog.show();

                mAuth.createUserWithEmailAndPassword(emailreg, passreg).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(Task<AuthResult> task) {
                  if (task.isSuccessful()) {

                      String Uid=mAuth.getCurrentUser().getUid();
                      DatabaseReference currentuserref= mRef.child(Uid);
                      currentuserref.child("Name").setValue(username);
                      currentuserref.child("Email Id").setValue(emailreg);
                      currentuserref.child("Password").setValue(passreg);

                      Toast.makeText(REGISTER.this, "Registration Success", Toast.LENGTH_SHORT).show();
                      mDialog.dismiss();
                       Intent intent = new Intent(REGISTER.this, MainActivity.class);
                       startActivity(intent);
                  } else {
                   Toast.makeText(REGISTER.this, "Problem creating an account", Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();
                          }
                        }
                                                                                              }
                );

            }
            }
        });

        tvalready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(REGISTER.this, LOGIN.class);
                startActivity(intent);
            }
        });

    }
}
