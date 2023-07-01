package com.letchile.let;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.text.Html;
import android.Manifest;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.Geocoder;
import android.location.Address;
import android.widget.Toast;
import android.text.InputType;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.Clases.Localizacion;
import com.letchile.let.Clases.PropiedadesFoto;
import com.letchile.let.Servicios.AgendarInspeccion;
import com.letchile.let.Servicios.AgregarHito;
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

/**
 * Created by Johana Godoy on 28-06-2019.
 */

public class FotoGeolocalizacion extends AppCompatActivity{

    DBprovider db;
    private static String APP_DIRECTORY = "";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY ;
    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_GEO = 250;
    private File ruta_fa;
    private String ruta = "", m_Text = "";
    private String mPath;
    PropiedadesFoto foto;
    ImageView imagenGeo;
    Boolean connec = false;
    Button btnFotoGeo,btnContinuar,btnVolverJg,btnAgendar, botonAgregarHito;
    Context contexto = this;
    int correlativo = 0;
    String nombreimagen = "",perfil;
    TextView  textCantG,contPostG,latitud,longitud,direccion, fono, contacto, comentario;

    public FotoGeolocalizacion(){db = new DBprovider(this);foto = new PropiedadesFoto(this);}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto_geolocalizacion);

        Date currentTime = Calendar.getInstance().getTime();

        connec = new ConexionInternet(this).isConnectingToInternet();
        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion=bundle.getString("id_inspeccion");


        perfil = db.obtenerPerfil();
       // String[][] datosInspeccion=db.BuscaDatosInspeccion(id_inspeccion);


        String[][] datosInspeccion=db.BuscaDatosInspeccion(id_inspeccion);

        fono = findViewById(R.id.fono_inicio);
        fono.setText(datosInspeccion[0][2]);

        Button btnLlamar = findViewById(R.id.llamarContacto_inicio);
        btnLlamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int REQUEST_PHONE_CALL = 1;
                Intent callIntent2 = new Intent(Intent.ACTION_CALL);
                callIntent2.setData(Uri.parse("tel:"+fono.getText().toString()));

                if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(ContextCompat.checkSelfPermission(FotoGeolocalizacion.this,Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(FotoGeolocalizacion.this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                    }else{
                        startActivity(callIntent2);
                    }
                }
            }
        });


        contacto = findViewById(R.id.contacto_incio);
        contacto.setText(datosInspeccion[0][1]);

        comentario = findViewById(R.id.comentario_inicio);
        comentario.setText(datosInspeccion[0][3]);

        latitud = findViewById(R.id.txtLatitud);
        longitud = findViewById(R.id.txtLongitud);
        direccion = findViewById(R.id.txtDireccion);

        //FOTO
        imagenGeo = findViewById(R.id.imagenGeo);

        //Botón volver al detalle de la OI
        final Button btnVolverJg = findViewById(R.id.btnVolverJg);
        btnVolverJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent( FotoGeolocalizacion.this, InsPendientesActivity.class);
                intent.putExtra("id_inspeccion",id_inspeccion);

                startActivity(intent);


            }
        });

        btnContinuar = findViewById(R.id.btnSigObsJg);
        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imagenFotoGeo = db.foto(Integer.parseInt(id_inspeccion),"Foto Geolocalizacion");
                final boolean gpsActivado=checkIfLocationOpened();

                if(gpsActivado == true)
                {
                    if(imagenFotoGeo.length()<=3){
                        //Toast toast =  Toast.makeText(prueba.this, "Debe Tomar Fotos Obligatorias", Toast.LENGTH_SHORT);
                        //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        //toast.show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(FotoGeolocalizacion.this);
                        builder.setCancelable(false);
                        builder.setTitle("LET Chile");
                        builder.setMessage(Html.fromHtml("<b>Debe Tomar Fotos Obligatoria</b><p><ul><li>- Foto Geolocalizacion</li></ul></p>"));
                        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                    else{
                        Intent intent   = new Intent(FotoGeolocalizacion.this,detalleActivity.class);
                        intent.putExtra("id_inspeccion",id_inspeccion);
                        startActivity(intent);
                        finish();

                    }

                }
                else
                {

                    Toast.makeText(FotoGeolocalizacion.this,"Debe activar GPS",Toast.LENGTH_SHORT).show();

                }


            }
        });
        btnAgendar = findViewById(R.id.btnAgendar);
        btnAgendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(FotoGeolocalizacion.this,"HOLA",Toast.LENGTH_SHORT).show();
                Intent intentA   = new Intent(FotoGeolocalizacion.this,AgendarActivity.class);
                intentA.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intentA);
            }
        });

        botonAgregarHito = findViewById(R.id.btnAgregarHito);
        botonAgregarHito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(FotoGeolocalizacion.this);
                TextView title = new TextView(FotoGeolocalizacion.this);
                title.setTextColor(ContextCompat.getColor(FotoGeolocalizacion.this, android.R.color.black));
                title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                title.setTypeface(Typeface.DEFAULT_BOLD);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 20, 0, 0);
                title.setPadding(0,30,0,60);
                title.setLayoutParams(lp);
                title.setText("Agregar Hito");
                title.setGravity(Gravity.CENTER);
                dialog.setCustomTitle(title);
                //dialog.setMessage("Dialog box with custom title view ");
                final EditText input = new EditText(FotoGeolocalizacion.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setHint("Escriba la actualización");
                dialog.setView(input);

                // Set up the buttons
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int estadoOI1 = db.estadoInspeccion(Integer.parseInt(id_inspeccion));
                        Log.e("Parametros agendamiento", "ESTADO 1° "+String.valueOf(estadoOI1));

                        m_Text = input.getText().toString();
                        Log.i("modal","el texto que llego es: "+m_Text);
                        Intent servis2 = new Intent(FotoGeolocalizacion.this, AgregarHito.class);
                        servis2.putExtra("id_inspeccion", id_inspeccion);
                        servis2.putExtra("comentario", m_Text);
                        startService(servis2);


                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            /*se hace un "sleep" para que alcance a gatillzarse el servicio y actualizacion de estado, antes de volver a consultar y verificar que todo esta OK*/
                            @Override
                            public void run() {
                                int estadoOI2 = db.estadoInspeccion(Integer.parseInt(id_inspeccion));
                                Log.e("Parametros agendamiento", "ESTADO 2° "+String.valueOf(estadoOI2));

                                if(estadoOI2==99) {
                                    /*cuando el estado es 99 osea si se gatillo el servicio de Agregar hito, reiniciamos el estado de la oi a 0 como estaba en un comienzo.*/
                                    db.cambiarEstadoInspeccion(Integer.parseInt(id_inspeccion),0);
                                    int estadoOI3 = db.estadoInspeccion(Integer.parseInt(id_inspeccion));
                                    Log.e("Parametros agendamiento", "ESTADO 3° "+String.valueOf(estadoOI3));

                                    Log.e("Parametros agendamiento", "TODO PERFECTO, SE DESPLIEGA TOASTR ");
                                    Toast.makeText(FotoGeolocalizacion.this,"Hito agregado correctamente.",Toast.LENGTH_LONG).show();
                                }else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(FotoGeolocalizacion.this);
                                    builder.setCancelable(false);
                                    builder.setTitle("LET Chile");
                                    builder.setMessage(Html.fromHtml("Error al intentar agregar hito, si el problema persiste contactarse con soporte."));
                                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                            }
                        }, 2000);









                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                dialog.show();
            }
        });



        //FOOTER
        btnFotoGeo = findViewById(R.id.btnFotoGeo);
        btnFotoGeo.setOnClickListener(new View.OnClickListener() {
            final boolean gpsActivado2=checkIfLocationOpened();
            @Override
            public void onClick(View v) {
                int cantGeo=db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Geolocalizacion");

                if(cantGeo>=1)
                {
                    Toast.makeText(FotoGeolocalizacion.this,"Ya existe fotografía del lugar en la OI "+ Integer.parseInt(id_inspeccion),Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(gpsActivado2 == true) {
                        openCamara(Integer.parseInt(id_inspeccion));
                    }
                    else{
                        Toast.makeText(FotoGeolocalizacion.this,"Debe activar GPS",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
            //image view
        String imagenG = db.foto(Integer.parseInt(id_inspeccion),"Foto Geolocalizacion");

        byte[] decodedString = Base64.decode(imagenG, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imagenGeo.setImageBitmap(decodedByte);
        imagenGeo.setVisibility(View.VISIBLE);

        //codigo geolocalizacion
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }
    }

    private boolean checkIfLocationOpened() {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        System.out.println("Provider contains=> " + provider);
        /* SE COMENTA PORQUE DA ERROR EN ANDROID 12 EMULADOR DE ANDROID... (parece que es por ser android 12 jaja */
        /*if (provider.contains("gps") || provider.contains("network")){
            return true;
        }
        return false;*/
        return true;
    }

    //codigo geolocalizacion
    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

       if (!this.checkIfLocationOpened()) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);
        latitud.setText("Localización agregada");
        direccion.setText("");
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }

    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                if(connec)
                {
                    //Log.i("conec","conec");
                    List<Address> list = geocoder.getFromLocation(
                            loc.getLatitude(), loc.getLongitude(), 1);
                    if (!list.isEmpty())
                    {
                        Address DirCalle = list.get(0);
                        longitud.setText(Double.toString(loc.getLongitude()));
                        latitud.setText(Double.toString(loc.getLatitude()));
                        direccion.setText(DirCalle.getAddressLine(0));
                    //    Log.i("LOCALIZACION: ", direccion.toString());
                    }
                }
                else
                {
                    //Log.i("no conec","no conec");
                    longitud.setText(Double.toString(loc.getLongitude()));
                    latitud.setText(Double.toString(loc.getLatitude()));
                    direccion.setText("ERROR AL OBTENER LA DIRECCION");
                 //   Log.i("LOCALIZACION: ", direccion.getText().toString());
                }
            } catch (IOException e) {
               // e.printStackTrace();
               Log.i("LOCALIZACION", "ERROR - CATCH EXCEPTION" + e);
            }
        }
    }

    //fin codigo


    private void openCamara(int id_inspeccion){
        ruta_fa = Environment.getExternalStorageDirectory();
        File file = new File(ruta_fa.toString() + '/' + id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();
            Log.i("camara","camara" +  file.mkdirs());

            if (isDirectoryCreated) {
                Log.i("camarass","camarass" +  isDirectoryCreated );
                correlativo = db.correlativoFotosFallida(id_inspeccion);
                nombreimagen = String.valueOf(id_inspeccion) + "_" + String.valueOf(correlativo) + "_FotoGeo.jpg";
                ruta = file.toString() + "/" + nombreimagen;
                mPath = ruta;

                File newFile = new File(mPath);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(FotoGeolocalizacion.this,
                        BuildConfig.APPLICATION_ID + ".provider", newFile));
                startActivityForResult(intent, PHOTO_GEO);
            }


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion=bundle.getString("id_inspeccion");
        String usr = db.obtenerUsuario();

        //Date fecha=Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fecha = sdf.format(new Date());
        String lat=latitud.getText().toString();
        String lon=longitud.getText().toString();
        String direcc=direccion.getText().toString();




        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case PHOTO_GEO:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitmap = BitmapFactory.decodeFile(mPath);
                    bitmap = foto.redimensiomarImagen(bitmap);
                    imagenGeo.setImageBitmap(bitmap);
                    imagenGeo.setVisibility(View.VISIBLE);
                    String imagen = foto.convertirImagenDano(bitmap);

                    //Log.i("transferir foto","foto");

                    db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Geolocalizacion", 0, imagen, 0);
                    Intent servis = new Intent(contexto, TransferirFoto.class);
                    servis.putExtra("comentario", "Foto Geolocalizacion");
                    servis.putExtra("id_inspeccion", id_inspeccion);
                    startService(servis);


                    int cantGeo=db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Geolocalizacion");
                    cantGeo = cantGeo + 1;
                    db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Foto Geolocalizacion",cantGeo);


                    Calendar c = Calendar.getInstance();
                    String strDate = sdf.format(c.getTime());
                    String usuario="", id_inspecc="" ;
                    usuario=db.obtenerUsuario();
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "LogInspector_"+usuario+".txt");

            try {


                    if(!lon.toString().equals("Longitud")){


                        //Log.i("inserta geo","geo");
                        db.insertaGeolocalizacion(Integer.parseInt(id_inspeccion),usr, fecha, lat , lon, direcc);
                        //Log.i("var","var" +  db.insertaGeolocalizacion(Integer.parseInt(id_inspeccion), usr, fecha, lat , lon, direcc));
                        Intent servi2 = new Intent(contexto, TransferirGeolocalizacion.class);
                        servi2.putExtra("id_inspeccion", id_inspeccion);
                        startService(servi2);

                        //AGREGADO NACHO - INSERTA EL REGISTRO DEL ESTADO DEL SERVICIO DE TRANSMISION DE LOS DATOS DE GEOLOCALIZACION - EL 1 ES error Y EL 2 ES Ok
                        db.insertRegistroTransGeo(Integer.parseInt(id_inspeccion),2);

                        if (!file.exists()) {
                            file.mkdir();
                        }

                        OutputStreamWriter fout = null;
                        fout = new FileWriter(file.getAbsoluteFile(), true);
                        fout.write("transmision datos geo....Ok"+" | "+ strDate);
                        fout.append("\n\r");
                        fout.close();

                    }else{
                        //AGREGADO NACHO - INSERTA EL REGISTRO DEL ESTADO DEL SERVICIO DE TRANSMISION DE LOS DATOS DE GEOLOCALIZACION - EL 1 ES error Y EL 2 ES Ok
                        db.insertRegistroTransGeo(Integer.parseInt(id_inspeccion),2);
                        Toast.makeText(FotoGeolocalizacion.this,"Se ha producido un error, favor volver a tomar fotografía",Toast.LENGTH_SHORT).show();

                        if (!file.exists()) {
                            file.mkdir();
                        }

                        OutputStreamWriter fout = null;
                        fout = new FileWriter(file.getAbsoluteFile(), true);
                        fout.write("transmision datos Geo....ERRONEA..."+" | "+ strDate);
                        fout.append("\n\r");
                        fout.close();
                    }
            } catch (Exception ex) {

                if (!file.exists()) {
                    file.mkdir();
                }


            }
                    break;

            }
        }
    }

}