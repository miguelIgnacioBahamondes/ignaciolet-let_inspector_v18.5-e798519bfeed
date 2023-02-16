package com.letchile.let;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.text.Html;
import android.Manifest;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.Geocoder;
import android.location.Address;
import android.widget.Toast;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.Clases.Localizacion;
import com.letchile.let.Clases.PropiedadesFoto;
import com.letchile.let.Servicios.ConexionInternet;
import com.letchile.let.Servicios.TransferirFoto;
import com.letchile.let.Servicios.TransferirGeolocalizacion;
import com.letchile.let.VehLiviano.DatosInspActivity;

import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.util.Calendar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.io.IOException;

public class AgendarActivity extends AppCompatActivity {
/*
    DBprovider db;

    private static String APP_DIRECTORY = "";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY ;
    private File ruta_fa;
    private String ruta = "";
    private String mPath;
    Boolean connec = false;
    Button btnVolverInspJg;
    Context contexto = this;
    int correlativo = 0;
    String nombreimagen = "",perfil;
    TextView  fono;
*/
@Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_oi);
           /*
        Toast.makeText(AgendarActivity.this,"hola",Toast.LENGTH_SHORT).show();
        Date currentTime = Calendar.getInstance().getTime();

        connec = new ConexionInternet(this).isConnectingToInternet();
        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion=bundle.getString("id_inspeccion");


        perfil = db.obtenerPerfil();
        // String[][] datosInspeccion=db.BuscaDatosInspeccion(id_inspeccion);


        String[][] datosInspeccion=db.BuscaDatosInspeccion(id_inspeccion);

        fono = findViewById(R.id.fono_inicio);
        fono.setText(datosInspeccion[0][2]);


        final Button btnVolverInspJg = findViewById(R.id.btnVolverInspJg);
        btnVolverInspJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent( AgendarActivity.this, InsPendientesActivity.class);
                intent.putExtra("id_inspeccion",id_inspeccion);

                startActivity(intent);


            }
        });
        */
    }


}
