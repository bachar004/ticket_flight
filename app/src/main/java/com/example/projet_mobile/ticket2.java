package com.example.projet_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ticket2 extends ConnectActivity {
    ImageView imgairline;
    TextView t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11;
    String log;
    Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ticket2);
        BottomNavigationView navbar=findViewById(R.id.nav);
        navbar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id= item.getItemId();
                if (id==R.id.nav_home){
                    Intent intent = new Intent(ticket2.this, avion.class);
                    intent.putExtra("logid",log);
                    startActivity(intent);
                    return true;}
                else if (id==R.id.nav_account){
                    Intent intent = new Intent(ticket2.this, account.class);
                    intent.putExtra("logid",log);
                    startActivity(intent);
                    return true;}
                else if (id==R.id.nav_logout){
                    Intent intent = new Intent(ticket2.this, login.class);
                    startActivity(intent);
                    finish();
                    return true;}
                else
                    return false;
            }
        });
        log=getIntent().getStringExtra("logid");
        String from=getIntent().getStringExtra("from");
        String to=getIntent().getStringExtra("to");
        String classtype=getIntent().getStringExtra("class");
        String date=getIntent().getStringExtra("date");
        String nbseat=getIntent().getStringExtra("nbperson");
        String time=getIntent().getStringExtra("time");
        String price=getIntent().getStringExtra("price");
        String duration=getIntent().getStringExtra("duration");
        String airline=getIntent().getStringExtra("airline");

        String prix=price.substring(0,price.length()-1);
        int t=Integer.parseInt(nbseat)*Integer.parseInt(prix);
        String total=String.valueOf(t)+"$";
        t1=findViewById(R.id.duree);
        t2=findViewById(R.id.from);
        t3=findViewById(R.id.to);
        t4=findViewById(R.id.from3);
        t5=findViewById(R.id.to3);
        t6=findViewById(R.id.date1);
        t7=findViewById(R.id.time1);
        t8=findViewById(R.id.class2);
        t9=findViewById(R.id.airlines2);
        t10=findViewById(R.id.seat2);
        t11=findViewById(R.id.price2);
        imgairline=findViewById(R.id.imageView3);

        t1.setText(duration);
        t2.setText(from);
        t3.setText(to);
        t4.setText(from);
        t5.setText(to);
        t6.setText(date);
        t7.setText(time);
        t8.setText(classtype);
        t9.setText(airline);
        t10.setText(nbseat);
        t11.setText(total);

        int idimg= getResources().getIdentifier(airline, "drawable", getPackageName());
        imgairline.setImageResource(idimg);

        b=findViewById(R.id.download);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (total.equals("0$"))
                    Toast.makeText(getApplicationContext(), "You must at least book for 1 place", Toast.LENGTH_LONG).show();
                else{
                    Toast.makeText(getApplicationContext(), "Ticket Downloaded !\n Have a good Flight ", Toast.LENGTH_LONG).show();
                    }
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, -60);
            return insets;
        });
    }
}