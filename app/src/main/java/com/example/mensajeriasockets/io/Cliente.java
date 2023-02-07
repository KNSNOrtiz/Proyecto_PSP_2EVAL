package com.example.mensajeriasockets.io;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Cliente {
    public Socket clienteSocket;    //  Socket cliente que se conectara al socket servidor.
    private final int PUERTO = 1234;    //  Puerto ocupado por el serverSocket.

    private ObjectOutputStream flujoSalida; //  Flujo de salida de objetos, para poder enviar paquetes serializados con varios datos.

    //  Se la pasa como parámetro la dirección del serverSocket destino.
    public Cliente(String host) {
        try {
            clienteSocket = new Socket(host, PUERTO);
        } catch (IOException e) {
            Log.d("CLIENTE", e.getMessage());
        }
    }
    //  Método encargado de enviar los paquetes al servidor.
    public void EnviarMensaje(Mensaje mensaje){
        try {
            //  Comprobamos que no sea nulo, porque de ser así, no se habría establecido la conexión.
            if (clienteSocket != null){
                //  Se obtiene el flujo de salida de objetos a partir del flujo del socket cliente.
                flujoSalida = new ObjectOutputStream(clienteSocket.getOutputStream());
                flujoSalida.writeObject(mensaje);
                flujoSalida.close();
                clienteSocket.close();
            }
        } catch (IOException e) {
            Log.d("CLIENTE", "Error al obtener flujo de salida.");
        }
    }
}
