package com.example.mensajeriasockets.io;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

//  Esta clase va a contener los datos que se enviarán al serverSocket destino.
public class Mensaje implements Serializable {
    String nombre;      //  Nombre del emisor.
    String hostDestino; //  Dirección IP del destinatario (serversocket)
    String mensaje;     //  Cuerpo del paquete, el mensaje en sí mismo.
    String horaEnvio;   //  Hora en la que se envió el mensaje, se toma de forma automática.
    int tipo;        //  Para determinar el aspecto visual del mensaje posteriormente, se establece esta variable que valdrá "salida" o "entrada".
    public final static int MSG_ENTRADA = 0;
    public final static int MSG_SALIDA = 1;

    public Mensaje(String nombre, String hostDestino, String mensaje, int tipo) {
        this.nombre = nombre;
        this.hostDestino = hostDestino;
        this.mensaje = mensaje;
        this.tipo = tipo;
        Date hora = new Date();   //  Se obtiene la fecha actual para extraer la hora.
        this.horaEnvio = new SimpleDateFormat("HH:mm").format(hora); //  Se aplica el formato hora:minuto y seteamos la propiedad.
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getHostDestino() {
        return hostDestino;
    }

    public void setHostDestino(String hostDestino) {
        this.hostDestino = hostDestino;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getHoraEnvio() {
        return horaEnvio;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}
