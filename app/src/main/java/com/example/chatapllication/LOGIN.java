package com.example.chatapllication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LOGIN extends AppCompatActivity {

    TextView tvnewregister;
    EditText etemaillogin;
    EditText etpasslogin;
    Button btnlogin;


    private FirebaseAuth mAuth;
    ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvnewregister = findViewById(R.id.tvnewregister);
        etemaillogin = findViewById(R.id.etemaillogin);
        etpasslogin = findViewById(R.id.etpasslogin);
        btnlogin = findViewById(R.id.btnlogin);

        mAuth = FirebaseAuth.getInstance();
        //checking for current user
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(LOGIN.this, MainActivity.class);
            startActivity(intent);
        }


        mDialog = new ProgressDialog(this);
        tvnewregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LOGIN.this,REGISTER.class);
                startActivity(intent);
            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emaillogin;
                String passlogin;
                emaillogin = etemaillogin.getText().toString().trim();
                passlogin = etpasslogin.getText().toString().trim();
                if (emaillogin.isEmpty() || passlogin.isEmpty()) {
                    Toast.makeText(LOGIN.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    mDialog.setMessage("Processing");
                    mDialog.show();
                    mAuth.signInWithEmailAndPassword(emaillogin, passlogin).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(LOGIN.this, "Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LOGIN.this, MainActivity.class);
                                startActivity(intent);
                                mDialog.dismiss();

                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(LOGIN.this, " Authentication failed.", Toast.LENGTH_LONG).show();
                                Log.w("error", "signInWithEmail:failure", task.getException());
                                mDialog.dismiss();
                            }

                        }
                    });
                }
            }
        });


    }
}