package com.letchile.let;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.Clases.PropiedadesTexto;
import com.letchile.let.Servicios.ConexionInternet;
import com.letchile.let.Servicios.TransferirInspeccion;
import com.letchile.let.Servicios.TransmitirOiEmail;
import com.letchile.let.VehLiviano.CamposAnexosActivity;
import com.letchile.let.VehLiviano.DatosInspActivity;
import com.letchile.let.VehLiviano.ObsActivity;
import com.letchile.let.Clases.PropiedadesTexto;
import com.letchile.let.VehPesado.SeccionVpActivity;

public class EmailActivity extends AppCompatActivity {
    DBprovider db;
    EditText emailC;
    Button btnSigE,btnVolverJg;
    String id_inspeccion;
    Boolean connec = false;

    public EmailActivity(){db = new DBprovider(this);
 }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        Bundle bundle = getIntent().getExtras();
        id_inspeccion=bundle.getString("id_inspeccion");

        connec = new ConexionInternet(this).isConnectingToInternet();

        emailC = findViewById(R.id.emailC);
        emailC.setOnEditorActionListener(new PropiedadesTexto());
        emailC.setText(db.obtenerEmail(Integer.parseInt(id_inspeccion)).toString());

        String[][] datosInspeccion=db.BuscaDatosInspeccion(id_inspeccion);



        //Botón Volver
        btnVolverJg = findViewById(R.id.btnVolverJg);
        btnVolverJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[][] datosInspeccion=db.BuscaDatosInspeccion(id_inspeccion);

                if(datosInspeccion[0][7].toString().equals("vl1")) {
                    Intent seccion = new Intent(EmailActivity.this, CamposAnexosActivity.class);
                    seccion.putExtra("id_inspeccion", id_inspeccion);
                    startActivity(seccion);
                    finish();
                }
                else
                {
                    Intent seccion = new Intent(EmailActivity.this, SeccionVpActivity.class);
                    seccion.putExtra("id_inspeccion", id_inspeccion);
                    startActivity(seccion);
                    finish();

                }

            }

        });





        //Botón Finalizar
        btnSigE = findViewById(R.id.btnSigE);
        btnSigE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String getText=emailC.getText().toString();


                String Expn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"

                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."

                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"

                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"

                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

                if (getText.matches(Expn) == false || emailC.getText().toString().length() == 0)
                {
                    Toast.makeText(EmailActivity.this,"Debe ingresar email válido "   ,Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Toast.makeText(DatosInspActivity.this,"email valido" + getText.matches(Expn),Toast.LENGTH_SHORT).show();
                    //insertar en tabla oi email
                    db.insertaOiEmail(Integer.parseInt(id_inspeccion),emailC.getText().toString());


                    //cambiar inspeccion a estado para transmitir
                            db.cambiarEstadoInspeccion(Integer.parseInt(id_inspeccion), 2);

                            if (connec) {
                                Intent servis = new Intent(EmailActivity.this, TransferirInspeccion.class);
                                startService(servis);

                                String email = db.DatosOiEmail(id_inspeccion);

                                Intent servis2 = new Intent(EmailActivity.this, TransmitirOiEmail.class);
                                servis2.putExtra("id_inspeccion", id_inspeccion);
                                servis2.putExtra("email", email);
                                //context.startService(servis2);
                                startService(servis2);

                                //Toast.makeText(CamposAnexosActivity.this, "Inspección " + id_inspeccion , Toast.LENGTH_LONG).show();
                            }

                            Intent seccion = new Intent(EmailActivity.this, InsPendientesActivity.class);
                            seccion.putExtra("id_inspeccion",id_inspeccion);
                            startActivity(seccion);
                            finish();


                }


            }
        });
    }
}
