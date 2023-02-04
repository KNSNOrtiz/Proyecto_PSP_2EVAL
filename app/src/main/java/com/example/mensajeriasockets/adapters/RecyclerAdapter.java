package com.example.mensajeriasockets.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mensajeriasockets.R;
import com.example.mensajeriasockets.io.Mensaje;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MensajeHolder> {

    private ArrayList<Mensaje> listaMensajes;

    public RecyclerAdapter(ArrayList<Mensaje> listaMensajes){
        this.listaMensajes = listaMensajes;
    }

    @NonNull
    @Override
    public MensajeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_holder, parent, false);
        MensajeHolder holder = new MensajeHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MensajeHolder holder, int position) {
        holder.txtNombre.setText(listaMensajes.get(position).getNombre());
        holder.txtMensaje.setText(listaMensajes.get(position).getMensaje());
        holder.txtHora.setText(listaMensajes.get(position).getHoraEnvio());
    }

    @Override
    public int getItemCount() {
        return listaMensajes.size();
    }

    public class MensajeHolder extends RecyclerView.ViewHolder {
        TextView txtNombre;
        TextView txtMensaje;
        TextView txtHora;

        public MensajeHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = (TextView) itemView.findViewById(R.id.txtNombre);
            txtMensaje = (TextView) itemView.findViewById(R.id.txtMensaje);
            txtHora = (TextView) itemView.findViewById(R.id.txtHora);

        }
    }
}


