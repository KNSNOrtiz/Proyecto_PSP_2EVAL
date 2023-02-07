package com.example.mensajeriasockets.io;

import android.media.MediaSession2Service;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public ServerSocket serverSocket;   //  Socket servidor, a este se conectarán los socket cliente.
    private Socket clienteSocket;       //  Cliente cuya petición de conexión ha sido aceptada por el socket servidor.
    private final int PUERTO = 1234;    //  Puerto ocupado por el serverSocket.
    public boolean finConexion = false; //  Semáforo que determina si el serverSocket debe seguir escuchando peticiones o no.

    private Mensaje mensajeEntrante;    //  Mensaje leído por el servidor.

    private ObjectInputStream flujoEntrada; //  Flujo de entrada de objetos que el servidor obtiene del cliente.

    public Servidor(){
        try {
            serverSocket = new ServerSocket(PUERTO);
        } catch (IOException e) {
            Log.d("SERVIDOR", "No se pudo abrir serverSocket");
            Log.d("SERVIDOR", e.getMessage());
        }
    }

    public Mensaje getMensajeEntrante(){
        return mensajeEntrante;
    }
    //  Método que estará ejecutándose de forma constante en el ChatActivity para recibir los mensajes entrantes.
    public void RecibirMensaje(){
        try {
            if (serverSocket!=null) {
                //  El serverSocket se pone en escucha y espera una petición.
                clienteSocket = serverSocket.accept();
                // Tras aceptarla, obtiene el flujo de entrada del cliente, para poder extraer los paquetes.
                flujoEntrada = new ObjectInputStream(clienteSocket.getInputStream());
                //  Se deserializa el paquete y se obtiene la información.
                mensajeEntrante = (Mensaje) flujoEntrada.readObject();
                //  Se altera el paquete para indicar que es enviado de vuelta por el servidor.
                mensajeEntrante.setTipo(Mensaje.MSG_ENTRADA);
                flujoEntrada.close();
                clienteSocket.close();
            }
        } catch (IOException e) {
            Log.d("SERVIDOR", "Fin de la conexión.");
        } catch (ClassNotFoundException e) {
            Log.d("SERVIDOR", "No se pudo realizar el casting en la lectura de Mensaje.");
        }
    }
}
