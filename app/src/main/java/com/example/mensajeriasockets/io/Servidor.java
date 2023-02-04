package com.example.mensajeriasockets.io;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public ServerSocket serverSocket;
    private Socket clienteSocket;
    private final int PUERTO = 1234;
    public boolean finConexion = false;

    private Mensaje mensajeEntrante;

    private ObjectInputStream flujoEntrada;

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

    public void RecibirMensaje(){
        try {
            if (serverSocket!=null) {
                clienteSocket = serverSocket.accept();
                flujoEntrada = new ObjectInputStream(clienteSocket.getInputStream());
                mensajeEntrante = (Mensaje) flujoEntrada.readObject();
                Log.d("SERVIDOR", "Mensaje obtenido: " + mensajeEntrante.getMensaje());
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
