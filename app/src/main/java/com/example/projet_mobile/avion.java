package com.example.projet_mobile;
import com.example.projet_mobile.R;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class avion extends ConnectActivity {
    String selecteditemfrom,selecteditemto,selectedclass;
    Button search;
    Spinner spinner, spinner2, spinnerseat;
    ImageView hoverImage;
    BottomNavigationView navbar;
    TextView id,date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_avion);
        id=findViewById(R.id.loginid);
        String log=getIntent().getStringExtra("logid");
        id.setText(log);
        date=findViewById(R.id.datedep);

        hoverImage = findViewById(R.id.hoverImage);

        hoverImage.setOnClickListener(v -> showCalendar(R.id.datedep));

        spinnerseat=findViewById(R.id.seat);
        ArrayList<String>seat=new ArrayList<>();
        seat.add("Business Class");
        seat.add("First Class");
        seat.add("Economy Class");
        ArrayAdapter<String> adapter=new ArrayAdapter<>(avion.this,android.R.layout.simple_spinner_item,seat);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerseat.setAdapter(adapter);

        spinner = findViewById(R.id.mySpinner);
        spinner2 = findViewById(R.id.mySpinner2);
        fromto(spinner);
        fromto(spinner2);

        navbar=findViewById(R.id.nav);
        navbar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id= item.getItemId();
                //mezel ne9es
                if (id==R.id.nav_home){
                        return true;}
                else if (id==R.id.nav_account){
                    Intent intent = new Intent(avion.this, account.class);
                    intent.putExtra("logid",log);
                    startActivity(intent);
                        return true;}
                else if (id==R.id.nav_logout){
                    Intent intent = new Intent(avion.this, login.class);
                    startActivity(intent);
                    finish();
                    return true;}
                else
                    return false;
                }
        });
//adulte
        Button plusAdultBtn = findViewById(R.id.plusAdult);
        Button minusAdultBtn = findViewById(R.id.minuadult);
        TextView adultCountText = findViewById(R.id.textView5);
        int[] adultCount = {0};
        adultCountText.setText(String.valueOf(adultCount[0]));

        plusAdultBtn.setOnClickListener(v -> {

            if(adultCount[0]<5){
                adultCount[0]++;
                adultCountText.setText(String.valueOf(adultCount[0]));}
        });

        minusAdultBtn.setOnClickListener(v -> {
            if (adultCount[0] > 0) {
                adultCount[0]--;
                adultCountText.setText(String.valueOf(adultCount[0]));
            }
        });
        // Gestion des enfants
        Button plusChildBtn = findViewById(R.id.plusCHILD);
        Button minusChildBtn = findViewById(R.id.minuChild);
        TextView childCountText = findViewById(R.id.textView55);
        int[] childCount = {0};
        childCountText.setText(String.valueOf(childCount[0]));

        plusChildBtn.setOnClickListener(v -> {
             if(childCount[0]<5){
                childCount[0]++;
                childCountText.setText(String.valueOf(childCount[0]));}
        });

        minusChildBtn.setOnClickListener(v -> {
            if (childCount[0] > 0) {
                childCount[0]--;
                childCountText.setText(String.valueOf(childCount[0]));
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selecteditemfrom=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selecteditemto=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerseat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedclass=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }});
        search=findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int nbplace=adultCount[0]+childCount[0];
                Intent i =new Intent(avion.this,flight.class);
                i.putExtra("from",selecteditemfrom);
                i.putExtra("to",selecteditemto);
                i.putExtra("date",date.getText().toString());
                i.putExtra("class",selectedclass);
                i.putExtra("nbperson",String.valueOf(nbplace));
                i.putExtra("logid",log);
                startActivity(i);
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.avion), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right,-60);
            return insets;
        });
    }
    @Override
    //pour bloque le retour avec le triangle de telephone
    public void onBackPressed() {
        Toast.makeText(this, "press logout", Toast.LENGTH_SHORT).show();
    }
    private void showCalendar(int textViewId) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(avion.this, R.style.MyDatePickerDialogTheme, (view, year1, monthOfYear, dayOfMonth) -> {
                    TextView date = findViewById(textViewId);
                    String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                    date.setText(selectedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
    }
    private void fromto(Spinner s){
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Locations");
        ArrayList<String>locationlist=new ArrayList<>();
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot location) {
                locationlist.clear();//eviter les doublons
                for (DataSnapshot i : location.getChildren()) {
                    String loc= i.child("Name").getValue(String.class);
                    if(loc!=null){
                        locationlist.add(loc);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(avion.this,
                        android.R.layout.simple_spinner_item, locationlist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                s.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FIREBASE", "Erreur de lecture : " + error.getMessage());
            }
        });
    }
}