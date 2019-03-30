package com.example.chatapllication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private String TAG="MainActivity";
    
    EditText etmessage;
    Button send;
    FirebaseDatabase mDatabase;
    FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private DatabaseReference mRefUser;
    private FirebaseUser mUser;
//    FirebaseAuth.AuthStateListener mAuthStateListener;

    RecyclerView recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//STep 2 declare and call the recycler view as well and the linear layout manager
        recyclerview=findViewById(R.id.recylerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(linearLayoutManager);

        mAuth=FirebaseAuth.getInstance();
//        mAuthStateListener=new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
//
//                if (mAuth.getCurrentUser()==null)
//                { Intent intent=new Intent(MainActivity.this,REGISTER.class);
//                    startActivity(intent);}
//            }
//        };

        etmessage=findViewById(R.id.etmessage);
        send=findViewById(R.id.btnsend);

        mRef=FirebaseDatabase.getInstance().getReference().child("Messages");


//PUSHING DATA INTO FIREBASE
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String msg=etmessage.getText().toString().trim();
                mUser = mAuth.getCurrentUser();
                mRefUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid());

                if(msg!=null) {
//                    final DatabaseReference newPost =  mRef.push();
                        mRefUser.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Log.e(TAG, "onDataChange: ",null );

                                for(DataSnapshot ds:dataSnapshot.getChildren()) {
                                    message msgobj = new message();
//@@@@@@@@@@@@@@@@@@@@@@@
                                    mRef.child("content").setValue(msg);
                                    mRef.child("username").setValue(ds.child("Name").getValue()).
                                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(Task<Void> task) {

                                                    Log.e(TAG, "onComplete:listener ", null);
                                                }
                                            });
                                }


                                recyclerview.scrollToPosition(recyclerview.getAdapter().getItemCount());

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                                Log.e(TAG, "onCancelled:databaseerror ",null );
                            }
                        });
                    }
                    etmessage.setText("");
                }
        });
    }

//STER 4 CREATE ONSTART METHOD, AFTER ADDING DEPENDENCY
// implementation 'com.firebaseui:firebase-ui-database:0.4.0'
//  CALL FIREBASERECYCLERADATPTER with params as model.class,the items of the view,myviewholder.class,
//  databasereference
//setadapter

    @Override
    protected void onStart() {
        super.onStart();
//        mAuth.addAuthStateListener(mAuthStateListener);
        FirebaseRecyclerAdapter<message,myviewholder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<message, myviewholder>(
                message.class,
                R.layout.chatback,
                myviewholder.class,
                mRef
        )
        {
            @Override
            protected void populateViewHolder(myviewholder viewHolder, message model, int position) {
                viewHolder.setContent(model.getContent());
                viewHolder.setUsername(model.getUsername());

            }
        };
        recyclerview.setAdapter(firebaseRecyclerAdapter);
    }

//STEP 3 CREATING A VIEWHOLDER CLASS EXTENDING RECLYCLERVIEW.VIEWHOLDER
//MAKE AFUNCTION SETTING THE REQUIRED VALUES
    public static class myviewholder extends RecyclerView.ViewHolder {

        View view;
        public myviewholder(View itemView) {
            super(itemView);
            view=itemView;
        }

//keep the child name and the string name same @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        public void setContent(String content) {
            TextView mymessage=view.findViewById(R.id.tvmessage);
            mymessage.setText(content);
        }
        public void setUsername(String username) {
           TextView myusername=view.findViewById(R.id.tvname);
           myusername.setText(username);
    }
    }
}
