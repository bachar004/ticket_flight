package com.example.projet_mobile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {

    public interface Ticketclic {
        void ticketclic(Ticket t);
    }
    Ticketclic listener;
    List<Ticket> ticketList;
    String selectedclass;
    //interface pour clic sur le ticket
    public TicketAdapter(List<Ticket> ticketList,String selectedclass,Ticketclic listener) {
        this.listener=listener;
        this.ticketList = ticketList;
        this.selectedclass=selectedclass;
    }

    public static class TicketViewHolder extends RecyclerView.ViewHolder {
        ImageView airlineLogo;
        TextView departureCode, arrivalCode, duration, classType, price;

        public TicketViewHolder(View itemView) {
            super(itemView);
            airlineLogo = itemView.findViewById(R.id.airline_logo);
            departureCode = itemView.findViewById(R.id.departure_code);
            arrivalCode = itemView.findViewById(R.id.arrival_code);
            duration = itemView.findViewById(R.id.flight_duration);
            classType = itemView.findViewById(R.id.seat_class);
            price = itemView.findViewById(R.id.ticket_price);
        }
    }

    @Override
    public TicketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_item, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TicketViewHolder holder, int position) {
        Ticket ticket=ticketList.get(position);
        String imageName = ticket.airlineName; // ex: "tunisair"
        int imgid = holder.itemView.getContext().getResources().getIdentifier(imageName, "drawable", holder.itemView.getContext().getPackageName());
        holder.airlineLogo.setImageResource(imgid);
        holder.departureCode.setText(ticket.from);
        holder.arrivalCode.setText(ticket.to);
        holder.duration.setText(ticket.duration);
        holder.price.setText(ticket.price.get(selectedclass));
        holder.classType.setText(selectedclass);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.ticketclic(ticket);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }
}
