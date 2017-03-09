package com.dariojolo.myapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class ThirdActivity extends AppCompatActivity {

    EditText txtDestino;
    EditText txtSubject;
    EditText txtMensaje;
    Button   btnEnviar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        //Activar flecha ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        txtDestino = (EditText)findViewById(R.id.txtDest);
        txtSubject = (EditText)findViewById(R.id.txtSub);
        txtMensaje = (EditText)findViewById(R.id.txtMessage);
        btnEnviar  = (Button)findViewById(R.id.btnEnviar);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMailTo = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+txtDestino.getText().toString()));
                Intent intentMailCompleto = new Intent(Intent.ACTION_SEND);
                intentMailCompleto.setType("text/html");

                final PackageManager pm =  ThirdActivity.this.getPackageManager();
                final List<ResolveInfo> matches = pm.queryIntentActivities(intentMailCompleto, 0);
                String className = null;
                for (final ResolveInfo info : matches) {
                    if (info.activityInfo.packageName.equals("com.google.android.gm")) {
                        className = info.activityInfo.name;

                        if(className != null && !className.isEmpty()){
                            break;
                        }
                    }
                }
                intentMailCompleto.setClassName("com.google.android.gm", className);

                intentMailCompleto.setData(Uri.parse(txtDestino.getText().toString()));
                intentMailCompleto.putExtra(Intent.EXTRA_SUBJECT,txtSubject.getText().toString());
                intentMailCompleto.putExtra(Intent.EXTRA_TEXT, txtMensaje.getText().toString());
                intentMailCompleto.putExtra(Intent.EXTRA_EMAIL, new String[] {"dariojolo2@gmail.com"});
                startActivity(intentMailCompleto);
            }
        });

    }

}
