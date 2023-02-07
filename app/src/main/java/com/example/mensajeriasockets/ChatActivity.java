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

    ArrayList<Mensaje> listaMensajes;   //  Lista de mensajes que recibe el RecyclerView (RV).
    Cliente cliente;
    Servidor servidor;
    //  Declaramos los hilos encargados de realizar tareas de conexión en red, ya que no pueden realizarse en el hilo principal.
    Thread hiloServidor;    //  Hilo encargado de lanzar el constructor del servidor.
    Thread hiloMensaje;     //  Hilo cuya función es mantener al servidor permanentemente escuchando peticiones mientras la conexión se mantenga.
    Thread hiloCliente;     //  Hilo que envía mensajes al serverSocket.

    RecyclerView rvChat;
    RecyclerAdapter adapter;
    SpacingDecorator spacingDecorator;  //  Espaciado del RV.

    Mensaje paquete;           //  Paquetes enviados hacia el servidor o insertados localmente en la lista.
    Mensaje mensajeRecibido;   //  Paquete extraído del servidor para mostrarse en pantalla.

    TextView txtChat, txtNombre;
    Button btnEnviar, btnSalir;

    String nombre, mensaje, host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().hide();
        Intent intent = getIntent();
        //  Obtenemos de vuelta la información que indica el emisor y el receptor.
        if (intent != null) {
            nombre = intent.getStringExtra("NOMBRE");
            host = intent.getStringExtra("DESTINO");
        }

        listaMensajes = new ArrayList<Mensaje>();

        rvChat = (RecyclerView) findViewById(R.id.rvChat);
        txtChat = (TextView) findViewById(R.id.txtChat);
        txtNombre = (TextView) findViewById(R.id.txtNombreChat);
        btnEnviar = (Button) findViewById(R.id.btnEnviar);
        btnSalir = (Button) findViewById(R.id.btnSalir);

        txtNombre.setText(host);    //  Dirección del destinatario, mostrada en la parte superior.

        LinearLayoutManager managerLayout = new LinearLayoutManager(this);
        rvChat.setLayoutManager(managerLayout);
        spacingDecorator = new SpacingDecorator(10);
        rvChat.addItemDecoration(spacingDecorator); //  Se añade la propiedad visual al RV.
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

        //  MUY IMPORTANTE: Antes de poner al servidor a la escucha, esperamos a que este hilo termine mediante join().
        try {
            hiloServidor.join();
        } catch (InterruptedException e) {
            Log.d("CHATACTIVITY", "No se pudo esperar a que iniciase el servidor");
        }

        hiloMensaje = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!servidor.finConexion){
                    servidor.RecibirMensaje();
                    mensajeRecibido = servidor.getMensajeEntrante();
                    if (mensajeRecibido != null){
                        runOnUiThread(new Runnable() {  //  Cualquier cambio en la interfaz se realiza en el hilo principal, función de este método.
                            @Override
                            public void run() {
                                listaMensajes.add(mensajeRecibido);
                                refrescarUI();  //  Método encargado de notificar al RV los mensajes insertados.
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
                mensaje = txtChat.getText().toString().trim();
                //  Si el mensaje contiene solo espacios en blanco o está vacío, no será enviado.
                if (mensaje.isEmpty())
                    return;

                paquete = new Mensaje(nombre,host,mensaje, Mensaje.MSG_SALIDA);
                txtChat.setText("");
                //  En este hilo creamos un nuevo socket cliente con la IP de destino.
                hiloCliente = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        cliente = new Cliente(host);
                        cliente.EnviarMensaje(paquete);
                    }
                });
                listaMensajes.add(paquete);
                refrescarUI();
                Log.d("INSERTADO LOCAL", paquete.getMensaje() + " " + listaMensajes.size());
                hiloCliente.start();
                try {
                    hiloCliente.join();
                } catch (InterruptedException e) {
                    Log.d("CHATACTIVITY", e.getMessage());
                }
                //  Para una mejor comunicación con el usuario, si el cliente no ha establecido una conexión se le avisa mediante
                // la propia caja de texto de que sus mensajes no son recibidos en el servidor, ya sea porque no exista o no esté operativo.
                if (cliente.clienteSocket == null){
                    paquete = new Mensaje("AVISO", "", "El destinatario no existe o se encuentra desconectado.", Mensaje.MSG_ENTRADA);
                    listaMensajes.add(paquete);
                    refrescarUI();
                }
            }
        });
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    //  Es necesario controlar en el cierre de la actividad que el hilo encargado de escuchar los mensajes pare
    //  y el socket del servidor se desocupe para evitar excepciones.
    //  De lo contrario, el bucle seguirá activo y no permitirá establecer una nueva conexión.
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
    //  Este método solamente avisa al adaptador del RV de que han llegado nuevos mensajes para que
    //  se actualice. También se posiciona en la parte más reciente de la lista.
    private void refrescarUI(){
        adapter.notifyItemInserted(listaMensajes.size()-1);
        adapter.notifyItemRangeChanged(listaMensajes.size()-1, listaMensajes.size());
        rvChat.scrollToPosition(listaMensajes.size() - 1);
    }
}