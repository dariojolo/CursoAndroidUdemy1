package com.dariojolo.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btn;
    private ImageButton btnTel;
    private ImageButton btnWeb;
    private ImageButton btnCam;
    private EditText text;
    private EditText txtTel;
    private EditText txtWeb;
    private final int PHONE_CALL_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.btnAceptar);
        text = (EditText) findViewById(R.id.txtMensaje);
        btnTel = (ImageButton) findViewById(R.id.btnTel);
        txtTel = (EditText) findViewById(R.id.txtTel);
        btnWeb = (ImageButton) findViewById(R.id.btnWeb);
        txtWeb = (EditText) findViewById(R.id.txtWeb);
        btnCam = (ImageButton) findViewById(R.id.btnCamera);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Accedemos a la otra activity
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("Message", text.getText().toString());
                startActivity(intent);
            }
        });
        //Boton para llamada telefonica
        btnTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numeroTelefono = txtTel.getText().toString();
                if (numeroTelefono != null && !numeroTelefono.isEmpty()) {
                    //Comprobar version actual de android que esta corriendo
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        //Comprobar si ha aceptado, no ha aceptado o nunca se le ha preguntado
                        if (checkPermission(Manifest.permission.CALL_PHONE)) {
                            //Ha aceptado
                            Intent i = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+numeroTelefono));
                            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            startActivity(i);
                        }else{
                            //O no ha aceptado o es la primera vez que se le pregunta
                            if (!shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)){
                                //No se le ha preguntado aún
                                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PHONE_CALL_CODE);
                            }else{
                                //Ha denegado
                                Toast.makeText(MainActivity.this, "Por favor habilite el permiso", Toast.LENGTH_SHORT).show();
                                //Abrir ventana de settings de la aplicación
                                Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                i.addCategory(Intent.CATEGORY_DEFAULT);
                                i.setData(Uri.parse("package:" + getPackageName()));
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                startActivity(i);
                            }
                        }

                        //Preguntamos por los permisos en tiempo de ejecucion
                        //requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PHONE_CALL_CODE);
                    } else {
                        OlderVersions(numeroTelefono);
                    }
                }else
                {
                    Toast.makeText(MainActivity.this, "Ingrese un numero de telefono valido", Toast.LENGTH_SHORT).show();
                }
            }

            //Chequeo de permisos en telefonos nuevos y antiguos
            private void OlderVersions(String numeroTelefono) {
                Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + numeroTelefono));
                if (checkPermission(Manifest.permission.CALL_PHONE)) {
                    startActivity(intentCall);
                } else {
                    Toast.makeText(MainActivity.this, "No acepto los permisos de acceso", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Botón para navegación WEB
        btnWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = txtWeb.getText().toString();
                if (url != null && !url.isEmpty()){
                    Intent intentWeb = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+ url));
                    //El paso anterior se puede hacer de la siguiente forma
                    //Intent intentWeb = new Intent();
                    //intentWeb.setAction(Intent.ACTION_VIEW);
                    //intentWeb.setData(Uri.parse("http://"+ url));
                    startActivity(intentWeb);
                }

            }
        });

    }

    //Manejamos la respuesta a la peticion del permiso
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Respondio al permiso de acceso al telefono
        switch (requestCode) {
            case PHONE_CALL_CODE:
                String permission = permissions[0];
                int result = grantResults[0];
                //Comprobamos que el permiso sea el de CALL_PHONE
                if (permission.equals(Manifest.permission.CALL_PHONE)) {
                    //Comprobamos si el permiso fue aceptado o denegado
                    if (result == PackageManager.PERMISSION_GRANTED) {
                        //Concedio el permiso
                        String phoneNumber = txtTel.getText().toString();
                        Toast.makeText(MainActivity.this,"Numero marcado "+ phoneNumber,Toast.LENGTH_SHORT).show();
                        Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                        //Comprueba que el permiso haya sido aceptado
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        startActivity(intentCall);
                    }
                    else{
                        //No concedio el permiso
                        Toast.makeText(MainActivity.this,"No concedio los permisos de acceso",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    private boolean checkPermission(String permission){
        int result = this.checkCallingOrSelfPermission(permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }

}
