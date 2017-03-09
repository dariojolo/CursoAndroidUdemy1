package com.dariojolo.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    private EditText txtSalida;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //Activar flecha ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtSalida = (EditText)findViewById(R.id.txtSalida);

        //recuperamos el valor enviado en el intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getString("Message") != null ){
            String mensaje = bundle.getString("Message");
            txtSalida.setText(mensaje);
        }else{
            Toast.makeText(SecondActivity.this,"Intent vacio",Toast.LENGTH_LONG).show();
        }


    }
}
