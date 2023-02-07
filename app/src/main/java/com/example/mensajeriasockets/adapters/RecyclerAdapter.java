package com.example.mensajeriasockets.adapters;


import static com.example.mensajeriasockets.io.Mensaje.MSG_ENTRADA;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
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
        /* Para diferenciar los mensajes entrantes de los salientes, asignamos un color diferente a aquellos
        cuyo tipo indicado en el paquete sea Entrada o Salida.
         */
        if (listaMensajes.get(position).getTipo() == MSG_ENTRADA){
            //  De esta forma obtenemos colores directamente del archivo de recursos XML, obteniendo el acceso a los recursos a través del contexto de la aplicación.
            holder.constraintLayout.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.morado_oscuro));
        }
        holder.txtNombre.setText(listaMensajes.get(position).getNombre());
        holder.txtMensaje.setText(listaMensajes.get(position).getMensaje());
        holder.txtHora.setText(listaMensajes.get(position).getHoraEnvio());
    }

    @Override
    public int getItemCount() {
        return listaMensajes.size();
    }

    public class MensajeHolder extends RecyclerView.ViewHolder {
        ConstraintLayout constraintLayout;  //  Se guarda la referencia al contenedor para cambiar su color.
        TextView txtNombre;
        TextView txtMensaje;
        TextView txtHora;


        public MensajeHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.constraintMensaje);
            txtNombre = (TextView) itemView.findViewById(R.id.txtNombre);
            txtMensaje = (TextView) itemView.findViewById(R.id.txtMensaje);
            txtHora = (TextView) itemView.findViewById(R.id.txtHora);

        }
    }
}


