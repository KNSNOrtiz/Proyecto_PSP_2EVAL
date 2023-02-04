package com.example.mensajeriasockets.io;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Cliente {
    private Socket clienteSocket;
    private String host;
    private final int PUERTO = 1234;

    private ObjectOutputStream flujoSalida;

    public Cliente(String host) {
        this.host = host;
        try {
            clienteSocket = new Socket(this.host, PUERTO);
        } catch (IOException e) {
            Log.d("CLIENTE", e.getMessage());
        }
    }

    public void EnviarMensaje(Mensaje mensaje){
        try {
            if (clienteSocket != null){
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
