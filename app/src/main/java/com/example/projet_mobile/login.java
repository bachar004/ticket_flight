package com.example.projet_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class login extends ConnectActivity {
    Button btnSignUpp;
    Button btnLogin;
    EditText log;
    EditText pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        btnSignUpp=findViewById(R.id.btnSignUpp);
        btnLogin=findViewById(R.id.btnLogin);
        log=findViewById(R.id.etFullNamee);
        pwd=findViewById(R.id.etPasswordd);
        btnSignUpp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=log.getText().toString().trim();
                String mdp=pwd.getText().toString();
                checklog(id,mdp);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(login.this, signup.class);
                startActivity(intent);
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void onBackPressed() {
        Intent i=new Intent(login.this, MainActivity.class);
        startActivity(i);
    }
    public void checklog(String id, String mdp){
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("users");
        Query checkuserdb = dbref.orderByChild("loginid").equalTo(id);

        checkuserdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot userSnap : snapshot.getChildren()) {
                        String pwdfromdb = userSnap.child("pwd").getValue(String.class);
                        if (pwdfromdb != null && pwdfromdb.trim().equals(mdp)) {
                            Intent i = new Intent(login.this, avion.class);
                            i.putExtra("logid",id);
                            startActivity(i);
                            finish();
                            break;
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid password", Toast.LENGTH_LONG).show();
                            break;
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Login doesn't exist", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DEBUG", "Database error: " + error.getMessage());
            }
        });
    }

}