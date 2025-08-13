package com.example.projet_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class account extends AppCompatActivity {
    TextView log,name,phone;
    EditText currentpwd,confirmpwd,newpwd;
    Button confirm;
    String Id,pwd;
    BottomNavigationView navbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account);
        confirm=findViewById(R.id.conf);
        log=findViewById(R.id.logid);
        name=findViewById(R.id.name);
        phone=findViewById(R.id.phone);
        currentpwd=findViewById(R.id.crpwd1);
        confirmpwd=findViewById(R.id.cpwd1);
        newpwd=findViewById(R.id.pwd1);

        Id=getIntent().getStringExtra("logid");

        log.setText(Id);

        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("users");
        Query userid = dbref.orderByChild("loginid").equalTo(Id);
        userid.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot user : snapshot.getChildren()){
                    String idfb=user.child("loginid").getValue(String.class);
                    name.setText(user.child("fullname").getValue(String.class));
                    phone.setText(user.child("phone").getValue(String.class));
                    pwd=user.child("pwd").getValue(String.class);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        navbar = findViewById(R.id.nav);
        navbar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id= item.getItemId();
                if (id==R.id.nav_home){
                    Intent intent = new Intent(account.this, avion.class);
                    intent.putExtra("logid",Id);
                    startActivity(intent);
                    return true;}
                else if (id==R.id.nav_account){
                    return true;}
                else if (id==R.id.nav_logout){
                    Intent intent = new Intent(account.this, login.class);
                    startActivity(intent);
                    finish();
                    return true;}
                else
                    return false;
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userpwd=currentpwd.getText().toString();
                if(!pwd.equals(userpwd)){
                    Toast.makeText(getApplicationContext(),"Invalid Current Password",Toast.LENGTH_LONG).show();
                    currentpwd.setText("");
                }
                else{
                    if(!(newpwd.getText().toString()).equals((confirmpwd.getText().toString()))){
                        Toast.makeText(getApplicationContext(),"Please Confirm New Password",Toast.LENGTH_LONG).show();
                        confirmpwd.setText("");
                    }
                    else{
                        DatabaseReference dbref2 = FirebaseDatabase.getInstance().getReference("users");
                        Query userid2 = dbref2.orderByChild("loginid").equalTo(Id);
                        userid2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot user : snapshot.getChildren()){
                                    dbref2.child(user.getKey()).child("pwd").setValue(newpwd.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                currentpwd.setText("");
                                                confirmpwd.setText("");
                                                newpwd.setText("");
                                                Toast.makeText(getApplicationContext(),"Password Changed Successfully",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right,-60);
            return insets;
        });

    }
}