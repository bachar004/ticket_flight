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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class signup extends ConnectActivity {
    Button signup;
    Button btnlogin;
    EditText fullname;
    EditText phone;
    EditText logid;
    EditText pwd;
    EditText cpwd;
    String name,tel,log,mdp,cmdp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        fullname=findViewById(R.id.etFullName);
        phone=findViewById(R.id.etPhone);
        logid=findViewById(R.id.etloginid);
        pwd=findViewById(R.id.etPassword);
        cpwd=findViewById(R.id.etConfirmPassword);

        signup=findViewById(R.id.btnSignUp);
        btnlogin=findViewById(R.id.btnLogin);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(signup.this,login.class);
                startActivity(i);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=fullname.getText().toString();
                tel=phone.getText().toString();
                log=logid.getText().toString().trim();
                mdp=pwd.getText().toString();
                cmdp=cpwd.getText().toString();
                if(name.trim().equals("")||tel.trim().equals("")||log.trim().equals("")||mdp.trim().equals("")||cmdp.trim().equals("")){
                    Toast.makeText(getApplicationContext(),"All fields are required",Toast.LENGTH_LONG).show();
                }
                else{
                    boolean b=checkname(name);
                    if(b==false){
                        Toast.makeText(getApplicationContext(),"Full Name only have characters",Toast.LENGTH_LONG).show();
                        fullname.setText("");
                    }
                    boolean c=checkpwd(mdp,cmdp);
                    if(c==false){
                        Toast.makeText(getApplicationContext(),"Confirm your password",Toast.LENGTH_LONG).show();
                        cpwd.setText("");}
                    checkSignup(log, logid, new LoginCheckCallback() {
                        @Override
                        public void onResult(boolean exists) {
                            if (exists) {}
                            else {
                                if(c==true && b==true){
                                    Toast.makeText(getApplicationContext(),"Sign up succeeded",Toast.LENGTH_LONG).show();
                                    addtofirebase(name,log,tel,mdp);
                                    fullname.setText("");
                                    phone.setText("");
                                    logid.setText("");
                                    pwd.setText("");
                                    cpwd.setText("");
                                    Intent i = new Intent(signup.this, avion.class);
                                    i.putExtra("logid",log);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        }
                    });
                }
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signup), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void checkSignup(String chaine, EditText logid, LoginCheckCallback callback) {
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("users");

        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot user) {
                boolean exists = false;
                for (DataSnapshot i : user.getChildren()) {
                    String login = i.child("loginid").getValue(String.class);
                    if (chaine.equals(login)) {
                        exists = true;
                        break;
                    }
                }
                if (exists) {
                    Toast.makeText(getApplicationContext(), "Login existant", Toast.LENGTH_LONG).show();
                    logid.setText("");
                }

                // Callback avec le résultat
                callback.onResult(exists);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FIREBASE", "Erreur de lecture : " + error.getMessage());
                callback.onResult(true);
            }
        });
    }

    private boolean checkpwd(String str1,String str2){
        if(str1.equals(str2)) {
            return true;
        }
        else{
            return false;
        }

    }
    private boolean checkname(String str){
        if(str.matches("[a-zA-Z- ]+"))
            return true;
        else
            return false;
    }
    private void addtofirebase(String nom,String id,String num,String mdp){
        HashMap <String,String> user=new HashMap<>();
        user.put("fullname",nom);
        user.put("loginid",id);
        user.put("phone",num);
        user.put("pwd",mdp);

        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("users");
        if(id!=null)
            dbref.child(id).setValue(user);
    }
}