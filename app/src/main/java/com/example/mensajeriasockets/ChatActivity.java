package com.example.mensajeriasockets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mensajeriasockets.adapters.RecyclerAdapter;
import com.example.mensajeriasockets.extra.SpacingDecorator;
import com.example.mensajeriasockets.io.Cliente;
import com.example.mensajeriasockets.io.Mensaje;
import com.example.mensajeriasockets.io.Servidor;

import java.io.IOException;
import java.util.ArrayList;


public class ChatActivity extends AppCompatActivity {

    ArrayList<Mensaje> listaMensajes;
    Cliente cliente;
    Servidor servidor;
    Thread hiloServidor, hiloMensaje;
    Thread hiloCliente;

    RecyclerView rvChat;
    RecyclerAdapter adapter;
    SpacingDecorator spacingDecorator;

    Mensaje mensajeRecibido;

    TextView txtChat;
    Button btnEnviar;

    String nombre, mensaje, host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().hide();
        Intent intent = getIntent();
        if (intent != null) {
            nombre = intent.getStringExtra("NOMBRE");
            host = intent.getStringExtra("DESTINO");
        }

        listaMensajes = new ArrayList<Mensaje>();
        listaMensajes.add(new Mensaje("Mario", host, "Tengo hambre"));

        rvChat = (RecyclerView) findViewById(R.id.rvChat);
        txtChat = (TextView) findViewById(R.id.txtChat);
        btnEnviar = (Button) findViewById(R.id.btnEnviar);

        LinearLayoutManager managerLayout = new LinearLayoutManager(this);

        rvChat.setLayoutManager(managerLayout);
        spacingDecorator = new SpacingDecorator(10);
        rvChat.addItemDecoration(spacingDecorator);
        adapter = new RecyclerAdapter(listaMensajes);
        rvChat.setAdapter(adapter);

        hiloServidor = new Thread(new Runnable() {
            @Override
            public void run() {
                if (servidor == null)
                    servidor = new Servidor();
            }
        });
        hiloServidor.start();

        try {
            hiloServidor.join();
        } catch (InterruptedException e) {
            Log.d("CHAT", "No se pudo esperar a que iniciase el servidor");
        }

        hiloMensaje = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!servidor.finConexion){
                    servidor.RecibirMensaje();
                    mensajeRecibido = servidor.getMensajeEntrante();
                    if (mensajeRecibido != null){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listaMensajes.add(mensajeRecibido);
                                Log.d("INSERTADO", mensajeRecibido.getMensaje() + " " + listaMensajes.size());
                                adapter.notifyItemInserted(listaMensajes.size()-1);
                                adapter.notifyItemRangeChanged(listaMensajes.size()-1, listaMensajes.size());
                                rvChat.scrollToPosition(listaMensajes.size() - 1);
                            }
                        });
                    }
                }

            }
        });
        hiloMensaje.start();

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mensaje = txtChat.getText().toString();
                txtChat.setText("");
                hiloCliente = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        cliente = new Cliente(host);
                        cliente.EnviarMensaje(new Mensaje(nombre,host,mensaje));
                    }
                });
                hiloCliente.start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        servidor.finConexion = true;    //  Este parámetro actúa de semáforo para detener el hilo encargado de escuchar los mensajes.
        //  Por si acaso, tratamos de forzar la interrupción del hilo manualmente.
        if (hiloMensaje.isAlive()) {
            try {
                hiloMensaje.interrupt();
                servidor.serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}