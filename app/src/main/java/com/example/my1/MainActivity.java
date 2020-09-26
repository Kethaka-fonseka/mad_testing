package com.example.my1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText id,name;
    Button save,show,delete;
    DatabaseReference dbRef;
    FirebaseAuth fAuth;
    User u1;
    long maxID=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        id=findViewById(R.id.etID);
        name=findViewById(R.id.etName);

        save=findViewById(R.id.btSave);
        show=findViewById(R.id.btShow);
        delete=findViewById(R.id.btnDelete);
fAuth=FirebaseAuth.getInstance();
        u1=new User();



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                dbRef= FirebaseDatabase.getInstance().getReference().child("User");
                dbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            maxID=(dataSnapshot.getChildrenCount());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                if(TextUtils.isEmpty(id.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Please enter id",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(name.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Please enter name",Toast.LENGTH_SHORT).show();
                }
                else{
                   u1.setID(id.getText().toString().trim());
                   u1.setName(name.getText().toString().trim());

                   fAuth.createUserWithEmailAndPassword(u1.getID(),u1.getName()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if(task.isSuccessful()){
                               dbRef.push().setValue(u1);
                               Toast.makeText(getApplicationContext(),"Data successfully added",Toast.LENGTH_SHORT).show();
                           }

                       }
                   });




                }
            }
        });


        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbRef= FirebaseDatabase.getInstance().getReference().child("User");
                dbRef.orderByChild("id").equalTo(id.getText().toString().trim()).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        User user=dataSnapshot.getValue(User.class);
                       // id.setText(dataSnapshot.getKey());


                        Intent intent=new Intent(MainActivity.this,display.class);
                        intent.putExtra("std",user);
                        startActivity(intent);



                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataSnapshot dataSnapshot = null;
                dbRef=FirebaseDatabase.getInstance().getReference().child("User").child(id.getText().toString().trim());
                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dbRef.removeValue();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }


}