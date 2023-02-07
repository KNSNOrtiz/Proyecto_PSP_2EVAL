package com.example.mensajeriasockets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class InicioActivity extends AppCompatActivity {
    String host, nombre;
    TextView txtHost, txtNombre;
    Button btnIniciar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        btnIniciar = (Button) findViewById(R.id.btnIniciar);
        txtHost = (TextView) findViewById(R.id.txtHost);
        txtNombre = (TextView) findViewById(R.id.txtNombreInicio);


        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Obtenemos los datos necesarios para la conexión mediante texto, y comprobamos que no estén vacíos.
                host = txtHost.getText().toString().trim();
                nombre = txtNombre.getText().toString().trim();
                if (!(host.isEmpty() || nombre.isEmpty())){
                    //  De estar completos, se pasa esta información a la siguiente Activity, ya que será necesaria.
                    Intent intent = new Intent(InicioActivity.this, ChatActivity.class);
                    intent.putExtra("NOMBRE", nombre);
                    intent.putExtra("DESTINO", host);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(InicioActivity.this,"Por favor, introduce un valor en cada campo", Toast.LENGTH_LONG).show();
                }

            }
        });


    }
}