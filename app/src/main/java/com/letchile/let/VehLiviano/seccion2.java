package com.letchile.let.VehLiviano;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.letchile.let.BD.DBprovider;
import com.letchile.let.EmailActivity;
import com.letchile.let.InsPendientesActivity;
import com.letchile.let.R;
import com.letchile.let.Servicios.ConexionInternet;
import com.letchile.let.Servicios.TransmitirOiEmail;
import com.letchile.let.VehLiviano.Fotos.documento;
import com.letchile.let.detalleActivity;
import com.letchile.let.VehLiviano.CamposAnexosActivity;
import com.letchile.let.Servicios.TransferirInspeccion;

import java.io.File;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class seccion2 extends AppCompatActivity {

    DBprovider db;
    Boolean connec = false;
    String id_inspeccion, perfil,usuario;

    public seccion2() { db = new DBprovider(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seccion2);
        ButterKnife.bind(this);
        connec = new ConexionInternet(this).isConnectingToInternet();

        Bundle bundle = getIntent().getExtras();
        id_inspeccion = bundle.getString("id_inspeccion");
        perfil = db.obtenerPerfil();
        usuario=db.obtenerUsuario();




        //VALIDAR QUETODO LO OBLIGATORIO ESTÉ LISTO
      Button btnTransmitir = findViewById(R.id.btnTranJg1);
       btnTransmitir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //verificar nuevamente la conexión
                connec = new ConexionInternet(seccion2.this).isConnectingToInternet();
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(seccion2.this);
                builder.setCancelable(false);
                builder.setMessage(Html.fromHtml("¿Seguro que desea transmitir la inspeccion <b>N°OI: " + id_inspeccion + "</b>?."));

                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int fotosTomadas = db.fotosObligatoriasTomadas(Integer.parseInt(id_inspeccion));
                        String email = db.DatosOiEmail(id_inspeccion);


                        if(perfil.equals("3")){

                            if (fotosTomadas >= 18) {

                                Intent seccion = new Intent(seccion2.this, CamposAnexosActivity.class);
                                seccion.putExtra("id_inspeccion",id_inspeccion);
                                startActivity(seccion);
                                finish();

                                /*Intent servis2 = new Intent(seccion2.this, TransmitirOiEmail.class);
                                servis2.putExtra("id_inspeccion", id_inspeccion);
                                servis2.putExtra("email", email);
                                startService(servis2);*/


                            } else {
                                Toast.makeText(seccion2.this, "Faltan fotos obligatorias por tomar" , Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {

                            if (fotosTomadas >= 18) {

                                Intent seccion = new Intent(seccion2.this, CamposAnexosActivity.class);
                                seccion.putExtra("id_inspeccion",id_inspeccion);
                                startActivity(seccion);
                                finish();


                                //cambiar inspeccion a estado para transmitir
                               /* db.cambiarEstadoInspeccion(Integer.parseInt(id_inspeccion), 2);
                                if (connec) {
                                    Intent servis = new Intent(seccion2.this, TransferirInspeccion.class);
                                    startService(servis);
*/
                                   Intent servis2 = new Intent(seccion2.this, TransmitirOiEmail.class);
                                   servis2.putExtra("id_inspeccion", id_inspeccion);
                                   servis2.putExtra("email", email);
                                   startService(servis2);
                            /*    }

                                Intent seccion = new Intent(seccion2.this, InsPendientesActivity.class);
                                startActivity(seccion);*/

                            } else {
                               Toast.makeText(seccion2.this, "Faltan fotos obligatorias por tomar" , Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });

                builder.setNegativeButton("Rechazar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(seccion2.this, "Inspección no transmitida", Toast.LENGTH_LONG).show();
                    }
                });
                android.app.AlertDialog alert = builder.create();
                alert.show();
            }
        });


    }

    @OnClick(R.id.btnFotoJg)
    public void FotosVehLiv(View view)
    {
       Intent intent = new Intent(seccion2.this, documento.class);
       intent.putExtra("id_inspeccion",id_inspeccion);
       startActivity(intent);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(c.getTime());

        try {
           // File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "LogInspector.txt");
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "LogInspector_"+usuario+".txt");
            if (!file.exists()) {
                file.mkdir();
            }

            OutputStreamWriter fout = null;

            fout = new FileWriter(file.getAbsoluteFile(), true);
            fout.write(id_inspeccion+"| Sección Foto |"+ strDate);
            fout.append("\n");
            fout.close();

        } catch (Exception ex) {
            Log.e("Error", "ex: " + ex);

        }
    }

    @OnClick(R.id.btnInspJg)
    public void DatosInspeccion(View view)
    {
        Intent intent = new Intent(seccion2.this, DatosInspActivity.class);
        intent.putExtra("id_inspeccion", id_inspeccion);
        startActivity(intent);
    }

    @OnClick(R.id.btnAsegJg)
    public void DatosAsegurados(View view)
    {
        Intent intent = new Intent(seccion2.this, DatosAsegActivity.class);
        intent.putExtra("id_inspeccion", id_inspeccion);
        startActivity(intent);
    }

    @OnClick(R.id.btnVehJg)
    public void DatosVehiculo(View view)
    {
        Intent intent = new Intent(seccion2.this, DatosVehActivity.class);
        intent.putExtra("id_inspeccion", id_inspeccion);
        startActivity(intent);
    }
    @OnClick(R.id.btnAcc)
    public void DatosAccesorios(View view)
    {
        Intent intent = new Intent(seccion2.this, AccActivity.class);
        intent.putExtra("id_inspeccion", id_inspeccion);
        startActivity(intent);
    }
    /*@OnClick(R.id.btnAudioJg)
    public void DatosAudio(View view)
    {
        Intent intent = new Intent(seccion2.this, AudioActivity.class);
        intent.putExtra("id_inspeccion", id_inspeccion);
        startActivity(intent);
    }*/

   /* @OnClick(R.id.btnNeumJg)
    public void DatosNeumatico(View view)
    {
        Intent intent = new Intent(seccion2.this, NeuActivity.class);
        intent.putExtra("id_inspeccion", id_inspeccion);
        startActivity(intent);
    }*/
    /*@OnClick(R.id.btnTechoJg)
    public void DatosTecho(View view)
    {
        Intent intent = new Intent(seccion2.this, TechoActivity.class);
        intent.putExtra("id_inspeccion", id_inspeccion);
        startActivity(intent);
    }*/

    @OnClick(R.id.btnObsJg)
    public void DatosObservacion(View view)
    {
        Intent intent = new Intent(seccion2.this, ObsActivity.class);
        intent.putExtra("id_inspeccion", id_inspeccion);
        startActivity(intent);
    }


    @OnClick(R.id.btnVolverSecJg)
    public void DatosVolver(View view)
    {
        Intent intent = new Intent(seccion2.this, detalleActivity.class);
        intent.putExtra("id_inspeccion", id_inspeccion);
        startActivity(intent);
    }


}
