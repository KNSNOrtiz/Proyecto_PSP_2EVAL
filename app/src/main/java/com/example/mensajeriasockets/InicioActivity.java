package com.example.mensajeriasockets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


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
                host = txtHost.getText().toString().trim();
                nombre = txtNombre.getText().toString().trim();
                Intent intent = new Intent(InicioActivity.this, ChatActivity.class);
                intent.putExtra("NOMBRE", nombre);
                intent.putExtra("DESTINO", host);
                startActivity(intent);

            }
        });


    }
}