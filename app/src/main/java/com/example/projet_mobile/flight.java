package com.example.projet_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class flight extends ConnectActivity {
    RecyclerView ticketRecyclerView;
    TicketAdapter ticketAdapter;
    List<Ticket> ticketList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_flight);
        String log=getIntent().getStringExtra("logid");
        String from=getIntent().getStringExtra("from");
        String to=getIntent().getStringExtra("to");
        String classtype=getIntent().getStringExtra("class");
        String date=getIntent().getStringExtra("date");
        String nbseat=getIntent().getStringExtra("nbperson");
        ticketRecyclerView = findViewById(R.id.ticketRecyclerView);
        ticketRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                Intent i1 = new Intent(flight.this, avion.class);
                i1.putExtra("logid",log);
                startActivity(i1);
            }
        });
        ticketList = new ArrayList<>();
        ticketAdapter = new TicketAdapter(ticketList, classtype, new TicketAdapter.Ticketclic() {
            @Override
            public void ticketclic(Ticket t) {
                Intent i=new Intent(flight.this,ticket2.class);
                i.putExtra("from",from);
                i.putExtra("to",to);
                i.putExtra("date",t.date);
                i.putExtra("time",t.time);
                i.putExtra("class",classtype);
                i.putExtra("price",t.price.get(classtype));
                i.putExtra("nbperson",nbseat);
                i.putExtra("airline",t.airlineName);
                i.putExtra("duration",t.duration);
                i.putExtra("logid",log);
                startActivity(i);
            }
        });
        ticketRecyclerView.setAdapter(ticketAdapter);
        //firebase
        DatabaseReference fb= FirebaseDatabase.getInstance().getReference("Flights");
        fb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ticketList.clear();
                for (DataSnapshot flightshot: snapshot.getChildren()){
                    Ticket t=flightshot.getValue(Ticket.class);
                    if(date.equals("")){
                        if(t.from.equals(from) && t.to.equals(to)){
                            ticketList.add(t);}}
                    else{
                        if(t.from.equals(from) && t.to.equals(to) && t.date.equals(date)){
                            ticketList.add(t);}
                    }
            }
                ticketAdapter.notifyDataSetChanged();
                if(ticketList.isEmpty())
                    Toast.makeText(getApplicationContext(),"there is no flight",Toast.LENGTH_LONG).show();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}