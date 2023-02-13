package com.letchile.let.VehLiviano.Fotos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.BuildConfig;
import com.letchile.let.Clases.PropiedadesFoto;
import com.letchile.let.Clases.PropiedadesTexto;
import com.letchile.let.Clases.Validaciones;
import com.letchile.let.R;
import com.letchile.let.Servicios.ConexionInternet;
import com.letchile.let.Servicios.TransferirFoto;
import com.letchile.let.VehLiviano.DatosVehActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class vl_techo extends AppCompatActivity {


    DBprovider db;
    Boolean connec = false;
    private final int PHOTO_DANO = 200;
    private final int PHOTO_SISTEMA_TECHO = 300;
    private final int PHOTO_PARRILLA = 400;
    private final int PHOTO_PORTA_EQUIPAJE = 500;
    private final int PHOTO_PORTA_SKY= 600;
    private final int PHOTO_SUNROOF= 700;
    private Button btnTechoVolverE,btnVolverSecTechoE,btnSiguienteTechoE,btnFotoDanoTechoE,btnSeccionDosTechoE,btnSeccionTres,btnSisTecho;
    private ImageView imagenTechoDanoE,imageBarraPortaEquipajeE,imageParrillaTechoE,imagePortaEquipajeE,imagePortaSkyE,imageSunroofE,imageViewSistTecho;
    private CheckBox barraPortaEquipajeE,parrillaTechoE,portaEquipajeE,portaSkyE,sunroofE;
    private Spinner spinnerPiezaTechoE,spinnerDanoTechoE,spinnerDeducibleTechoE, tipo_sunroof;
    private TextView txtPieza,txtTipoDanoE,txtDeducibleE, txtTipoSunroof,textCantT,contPostT,textView103,textView102,textView99,textView104,textCanTecho,contPosTecho;
    private String mPath;
    private File ruta_sd;
    private String ruta = "",oi;
    PropiedadesFoto foto;
    String nombreimagen = "", comentarioDañoImg="";
    EditText maParrilla,maBarras,maPorta, maPortaSki;
    Validaciones validaciones;
    int correlativo=0;
    JSONObject llenado;
    String dañosDedu[][];


    public vl_techo(){db = new DBprovider(this);foto=new PropiedadesFoto(this); validaciones = new Validaciones(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vl_techo);

        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion = bundle.getString("id_inspeccion");

        oi=  id_inspeccion ;

        connec = new ConexionInternet(this).isConnectingToInternet();

        btnFotoDanoTechoE = findViewById(R.id.btnFotoDanoTechoE);
        imagenTechoDanoE = findViewById(R.id.imagenTechoDanoE);
        imageViewSistTecho = findViewById(R.id.imageViewSistTecho);
        barraPortaEquipajeE = findViewById(R.id.barraPortaEquipajeE);
       // imageBarraPortaEquipajeE = findViewById(R.id.imageBarraPortaEquipajeE);
        parrillaTechoE = findViewById(R.id.parrillaTechoE);
        //imageParrillaTechoE = findViewById(R.id.imageParrillaTechoE);
        portaEquipajeE = findViewById(R.id.portaEquipajeE);
        //imagePortaEquipajeE = findViewById(R.id.imagePortaEquipajeE);
        portaSkyE = findViewById(R.id.portaSkyE);
        //imagePortaSkyE = findViewById(R.id.imagePortaSkyE);
        sunroofE = findViewById(R.id.sunroofE);
        tipo_sunroof = findViewById(R.id.spinner_tipo_sunroof);
        txtTipoSunroof = findViewById(R.id.txtTipoSunroof);
        imageSunroofE = findViewById(R.id.imageSunroofE);
        btnSeccionDosTechoE = findViewById(R.id.btnSeccionDosTechoE);
        spinnerPiezaTechoE = findViewById(R.id.spinnerPiezaTechoE);
        spinnerDanoTechoE = findViewById(R.id.spinnerDanoTechoE);
        spinnerDeducibleTechoE = findViewById(R.id.spinnerDeducibleTechoE);
        txtPieza = findViewById(R.id.txtPieza);
        txtTipoDanoE = findViewById(R.id.txtTipoDanoE);
        txtDeducibleE = findViewById(R.id.txtDeducibleE);
        btnSeccionTres = findViewById(R.id.btnSeccionTres);
        textView102 = findViewById(R.id.textView102);
        textView103 = findViewById(R.id.textView103);
        textView99 = findViewById(R.id.textView99);
        textView104 = findViewById(R.id.textView104);

        btnSisTecho = findViewById(R.id.btnSisTecho);
        imageViewSistTecho = findViewById(R.id.imageViewSistTecho);



        textCanTecho = findViewById(R.id.textCanTecho);
        contPosTecho = findViewById(R.id.contPosTecho);
        contPosTecho.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Techo")));

        //marca parrilla
        maParrilla = findViewById(R.id.maParrilla);
        maParrilla.setOnEditorActionListener(new PropiedadesTexto());
        maParrilla.setText(db.accesorio(Integer.parseInt(id_inspeccion),309).toString());

        //marca barras porta equipaje
        maBarras = findViewById(R.id.maBarras);
        maBarras.setOnEditorActionListener(new PropiedadesTexto());
        maBarras.setText(db.accesorio(Integer.parseInt(id_inspeccion),773).toString());

        //marca porta equipaje
        maPorta = findViewById(R.id.maPorta);
        maPorta.setOnEditorActionListener(new PropiedadesTexto());
        maPorta.setText(db.accesorio(Integer.parseInt(id_inspeccion),772).toString());

        //porta ski
        maPortaSki = findViewById(R.id.maPortaSki);
        maPortaSki.setOnEditorActionListener(new PropiedadesTexto());
        maPortaSki.setText(db.accesorio(Integer.parseInt(id_inspeccion),771).toString());

        textCantT = findViewById(R.id.textCantT);
        contPostT = findViewById(R.id.contPostT);
        contPostT.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Danio Techo")));

        Toast.makeText(vl_techo.this,"Error en los datos"+oi,Toast.LENGTH_SHORT);


        btnFotoDanoTechoE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {funcionCamara(id_inspeccion,"_Foto_Techo_Dano.jpg",PHOTO_DANO);
            }
        });

        barraPortaEquipajeE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),325));
        barraPortaEquipajeE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 325).toString().equals("Ok")) {
                        //funcionCamara(id_inspeccion,"_Foto_Techo_Barra_Equipaje.jpg",PHOTO_BARRA);
                        db.insertarValor(Integer.parseInt(id_inspeccion),325,"Ok");
                        maBarras.setEnabled(true);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),325,"");
                    //imageBarraPortaEquipajeE.setImageBitmap(null);
                    maBarras.setEnabled(false);
                }
            }
        });

        parrillaTechoE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),293));
        parrillaTechoE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 293).toString().equals("Ok")) {
                       // funcionCamara(id_inspeccion,"_Foto_Techo_Parrilla.jpg",PHOTO_PARRILLA);
                        db.insertarValor(Integer.parseInt(id_inspeccion),293,"Ok");
                        maParrilla.setEnabled(true);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),293,"");
                   // imageParrillaTechoE.setImageBitmap(null);
                    maParrilla.setEnabled(false);
                }
            }
        });

        portaEquipajeE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),355));
        portaEquipajeE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 355).toString().equals("Ok")) {
                        //funcionCamara(id_inspeccion,"_Foto_Techo_Porta_Equipaje.jpg",PHOTO_PORTA_EQUIPAJE);
                        db.insertarValor(Integer.parseInt(id_inspeccion),355,"Ok");
                        maPorta.setEnabled(true);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),355,"");
                    //imagePortaEquipajeE.setImageBitmap(null);
                    maPorta.setEnabled(false);
                }
            }
        });

        portaSkyE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),327));
        portaSkyE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 327).toString().equals("Ok")) {
                        //funcionCamara(id_inspeccion,"_Foto_Techo_Porta_Sky.jpg",PHOTO_PORTA_SKY);
                        db.insertarValor(Integer.parseInt(id_inspeccion),327,"Ok");
                        maPortaSki.setEnabled(true);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),327,"");
                    //imagePortaSkyE.setImageBitmap(null);
                    maPortaSki.setEnabled(false);
                }
            }
        });

        sunroofE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),262));
        sunroofE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 262).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_Techo_Sunroof.jpg",PHOTO_SUNROOF);
                       // db.insertarValor(Integer.parseInt(id_inspeccion),262,"Ok");
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),262,"");
                    imageSunroofE.setImageBitmap(null);
                }
            }
        });

        btnSisTecho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                funcionCamara(id_inspeccion,"_Sistema_techo.jpg",PHOTO_SISTEMA_TECHO);
            }
        });



        String[] arrayuso = getResources().getStringArray(R.array.tipo_sunroof);
        final List<String> arrayusolist = Arrays.asList(arrayuso);
        ArrayAdapter<String> spinner_adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayuso);
        spinner_adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipo_sunroof.setAdapter(spinner_adapter2);
        tipo_sunroof.setSelection(arrayusolist.indexOf(db.accesorio(Integer.parseInt(id_inspeccion),263).toString()));


        //Log.i("ccc","ccc" + db.accesorio(Integer.parseInt(id_inspeccion),263).toString());
        if (db.accesorio(Integer.parseInt(id_inspeccion),263).toString().equals("")) {
            tipo_sunroof.setSelection(0);

            if(tipo_sunroof.getSelectedItem().toString().equals("Seleccione..."))
            {
                tipo_sunroof.getSelectedItem().toString().equals("");

            }


        }
        else
        {
           tipo_sunroof.getSelectedItem().toString();

        }



        btnSeccionDosTechoE.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {desplegarCamposSeccionDos(id_inspeccion);  }
                                              });
        btnSeccionTres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {               desplegarCamposSeccionTres(id_inspeccion);
            }
        });

        btnTechoVolverE = findViewById(R.id.btnTechoVolverE);
        btnTechoVolverE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarDatos();
                Intent intent   = new Intent(vl_techo.this,llantasneumaticos.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
                finish();
            }
        });

        btnSiguienteTechoE = findViewById(R.id.btnSiguienteTechoE);
        btnSiguienteTechoE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarDatos();
                Intent intent   = new Intent(vl_techo.this,interior.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
                finish();
            }
        });


    }


    ///FUNCIÓN ABRE LA CAMARA Y TOMA LAS FOTOGRAFIAS
    public void funcionCamara(String id,String nombre_foto,int CodigoFoto){
        String id_inspeccion = id;
        ruta_sd = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString()+'/'+id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if(!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if(isDirectoryCreated){

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+nombre_foto;

            ruta = file.toString() + "/" + nombreimagen;
            mPath = ruta;

            File newFile = new File(mPath);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,  FileProvider.getUriForFile(vl_techo.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            startActivityForResult(intent, CodigoFoto);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion=bundle.getString("id_inspeccion");

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_DANO:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitmapTecho = BitmapFactory.decodeFile(mPath);
                    bitmapTecho = foto.redimensiomarImagen(bitmapTecho);

                    String imagenTechoDano = foto.convertirImagenDano(bitmapTecho);

                    comentarioDañoImg = spinnerPiezaTechoE.getSelectedItem().toString()+' '+spinnerDanoTechoE.getSelectedItem().toString()+' '+spinnerDeducibleTechoE.getSelectedItem().toString()+' ';
                    db.insertarComentarioFoto(Integer.parseInt(id_inspeccion),comentarioDañoImg,"techo");
                    String comentarito = db.comentarioFoto(Integer.parseInt(id_inspeccion),"techo");

                    db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, comentarito, 0, imagenTechoDano, 0);


                    dañosDedu = db.DeduciblePieza(spinnerPiezaTechoE.getSelectedItem().toString(), "techo");
                    //danioPo=db.Deducible(spinnerDeduciblePoE.getSelectedItem().toString());

                    //daño
                    db.insertarValor(Integer.parseInt(id_inspeccion),Integer.parseInt(dañosDedu[0][0]),String.valueOf(db.obtenerDanio(spinnerDanoTechoE.getSelectedItem().toString())));

                    //deducible
                    //db.insertarValor(Integer.parseInt(id_inspeccion),Integer.parseInt(dañosDedu[0][1]),db.obtenerDeducible(db.obtenerDanio(spinnerDanoTechoE.getSelectedItem().toString()),spinnerDeducibleTechoE.getSelectedItem().toString()));
                    db.insertarValor(Integer.parseInt(id_inspeccion),Integer.parseInt(dañosDedu[0][1]),spinnerDeducibleTechoE.getSelectedItem().toString());

                    imagenTechoDano = "data:image/jpg;base64,"+imagenTechoDano;
                    String base64Image1 = imagenTechoDano.split(",")[1];
                    byte[] decodedString1 = Base64.decode(base64Image1, Base64.DEFAULT);
                    Bitmap decodedByte1 = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.length);
                    imagenTechoDanoE.setImageBitmap(decodedByte1);

                        Intent servis = new Intent(vl_techo.this, TransferirFoto.class);
                        servis.putExtra("comentario",comentarito);
                        servis.putExtra("id_inspeccion",id_inspeccion);
                        startService(servis);

                    int cantFotoTecho=db.cantidadF(Integer.parseInt(id_inspeccion),"techo");
                    cantFotoTecho = cantFotoTecho + 1;
                    db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"techo",cantFotoTecho);
                    contPostT.setText(String.valueOf(cantFotoTecho));

                    break;

                case PHOTO_SISTEMA_TECHO:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitmapSisTecho = BitmapFactory.decodeFile(mPath);
                    bitmapSisTecho = foto.redimensiomarImagen(bitmapSisTecho);

                    String imagenSistemaTecho = foto.convertirImagenDano(bitmapSisTecho);
                    db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Techo", 0, imagenSistemaTecho, 0);
                    imagenSistemaTecho   = "data:image/jpg;base64,"+imagenSistemaTecho;
                    String base64ImageA = imagenSistemaTecho.split(",")[1];
                    byte[] decodedStringA = Base64.decode(base64ImageA, Base64.DEFAULT);
                    Bitmap decodedByteA  = BitmapFactory.decodeByteArray(decodedStringA, 0, decodedStringA.length);
                    imageViewSistTecho.setImageBitmap(decodedByteA);

                    servis = new Intent(vl_techo.this, TransferirFoto.class);
                    servis.putExtra("comentario", "Foto Techo");
                    servis.putExtra("id_inspeccion", id_inspeccion);
                    startService(servis);

                    int cantFotoT=db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Techo");
                    cantFotoT = cantFotoT + 1;
                    db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Foto Techo",cantFotoT);
                    contPosTecho.setText(String.valueOf(cantFotoT));


                    break;
                /*case PHOTO_BARRA:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitmapBarra = BitmapFactory.decodeFile(mPath);
                    bitmapBarra = foto.redimensiomarImagen(bitmapBarra);

                    String imagenBarra = foto.convertirImagenDano(bitmapBarra);
                    db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Barra Equipaje Techo", 0, imagenBarra, 0);
                    db.insertarValor(Integer.parseInt(id_inspeccion),325,"Ok");
                    imagenBarra = "data:image/jpg;base64,"+imagenBarra;
                    String base64Image2 = imagenBarra.split(",")[1];
                    byte[] decodedString2 = Base64.decode(base64Image2, Base64.DEFAULT);
                    Bitmap decodedByte2 = BitmapFactory.decodeByteArray(decodedString2, 0, decodedString2.length);
                    imageBarraPortaEquipajeE.setImageBitmap(decodedByte2);

                        servis = new Intent(vl_techo.this, TransferirFoto.class);
                    servis.putExtra("comentario","Foto Barra Equipaje Techo");
                        servis.putExtra("id_inspeccion",id_inspeccion);
                        startService(servis);

                    break;
                case PHOTO_PARRILLA:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitmapParrilla = BitmapFactory.decodeFile(mPath);
                    bitmapParrilla = foto.redimensiomarImagen(bitmapParrilla);

                    String imagenParrilla = foto.convertirImagenDano(bitmapParrilla);
                    db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Parrilla Techo", 0, imagenParrilla, 0);
                    db.insertarValor(Integer.parseInt(id_inspeccion),293,"Ok");
                    imagenParrilla = "data:image/jpg;base64,"+imagenParrilla;
                    String base64Image3 = imagenParrilla.split(",")[1];
                    byte[] decodedString3 = Base64.decode(base64Image3, Base64.DEFAULT);
                    Bitmap decodedByte3 = BitmapFactory.decodeByteArray(decodedString3, 0, decodedString3.length);
                    imageParrillaTechoE.setImageBitmap(decodedByte3);

                        servis = new Intent(vl_techo.this, TransferirFoto.class);
                    servis.putExtra("comentario","Foto Parrilla Techo");
                        servis.putExtra("id_inspeccion",id_inspeccion);
                        startService(servis);

                    break;
                case PHOTO_PORTA_EQUIPAJE:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitmapPorta = BitmapFactory.decodeFile(mPath);
                    bitmapPorta = foto.redimensiomarImagen(bitmapPorta);

                    String imagenPorta = foto.convertirImagenDano(bitmapPorta);
                    db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Porta Equipaje Techo", 0, imagenPorta, 0);
                    db.insertarValor(Integer.parseInt(id_inspeccion),355,"Ok");
                    imagenPorta = "data:image/jpg;base64,"+imagenPorta;
                    String base64Image4 = imagenPorta.split(",")[1];
                    byte[] decodedString4 = Base64.decode(base64Image4, Base64.DEFAULT);
                    Bitmap decodedByte4 = BitmapFactory.decodeByteArray(decodedString4, 0, decodedString4.length);
                    imagePortaEquipajeE.setImageBitmap(decodedByte4);

                        servis = new Intent(vl_techo.this, TransferirFoto.class);
                    servis.putExtra("comentario","Foto Porta Equipaje Techo");
                        servis.putExtra("id_inspeccion",id_inspeccion);
                        startService(servis);

                    break;
                case PHOTO_PORTA_SKY:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitmapPortaSky = BitmapFactory.decodeFile(mPath);
                    bitmapPortaSky = foto.redimensiomarImagen(bitmapPortaSky);

                    String imagenPortaSky = foto.convertirImagenDano(bitmapPortaSky);
                    db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Porta Sky Techo", 0, imagenPortaSky, 0);
                    db.insertarValor(Integer.parseInt(id_inspeccion),327,"Ok");
                    imagenPortaSky = "data:image/jpg;base64,"+imagenPortaSky;
                    String base64Image5 = imagenPortaSky.split(",")[1];
                    byte[] decodedString5 = Base64.decode(base64Image5, Base64.DEFAULT);
                    Bitmap decodedByte5 = BitmapFactory.decodeByteArray(decodedString5, 0, decodedString5.length);
                    imagePortaSkyE.setImageBitmap(decodedByte5);

                        servis = new Intent(vl_techo.this, TransferirFoto.class);
                    servis.putExtra("comentario","Foto Porta Sky Techo");
                        servis.putExtra("id_inspeccion",id_inspeccion);
                        startService(servis);

                    break;*/

                case PHOTO_SUNROOF:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitmapSunroof = BitmapFactory.decodeFile(mPath);
                    bitmapSunroof = foto.redimensiomarImagen(bitmapSunroof);

                    String imagenSunroof = foto.convertirImagenDano(bitmapSunroof);
                    db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Sunroof Techo", 0, imagenSunroof, 0);
                    db.insertarValor(Integer.parseInt(id_inspeccion),262,"Ok");

                    imagenSunroof = "data:image/jpg;base64,"+imagenSunroof;
                    String base64Image6 = imagenSunroof.split(",")[1];
                    byte[] decodedString6 = Base64.decode(base64Image6, Base64.DEFAULT);
                    Bitmap decodedByte6 = BitmapFactory.decodeByteArray(decodedString6, 0, decodedString6.length);
                    imageSunroofE.setImageBitmap(decodedByte6);

                        servis = new Intent(vl_techo.this, TransferirFoto.class);
                    servis.putExtra("comentario","Foto Sunroof Techo");
                        servis.putExtra("id_inspeccion",id_inspeccion);
                        startService(servis);





                    break;
            }
        }
    }

    public void desplegarCamposSeccionDos(String id)    {
        if (txtPieza.getVisibility()==View.VISIBLE)
        {
            txtPieza.setVisibility(View.GONE);
            spinnerPiezaTechoE.setVisibility(View.GONE);
            txtTipoDanoE.setVisibility(View.GONE);
            spinnerDanoTechoE.setVisibility(View.GONE);
            txtDeducibleE.setVisibility(View.GONE);
            spinnerDeducibleTechoE.setVisibility(View.GONE);
            btnFotoDanoTechoE.setVisibility(View.GONE);
            imagenTechoDanoE.setVisibility(View.GONE);
            imagenTechoDanoE.setImageBitmap(null);
            textCantT.setVisibility(View.GONE);
            contPostT.setVisibility(View.GONE);

        }
        else
        {

            //SECCION TRES
            barraPortaEquipajeE.setVisibility(View.GONE);
            btnSisTecho.setVisibility(View.GONE);
            textCanTecho.setVisibility(View.GONE);
            contPosTecho.setVisibility(View.GONE);
            imageViewSistTecho.setVisibility(View.GONE);
            imageViewSistTecho.setImageBitmap(null);
            //imageBarraPortaEquipajeE.setVisibility(View.GONE);
            //imageBarraPortaEquipajeE.setImageBitmap(null);
            parrillaTechoE.setVisibility(View.GONE);
            //imageParrillaTechoE.setVisibility(View.GONE);
            //imageParrillaTechoE.setImageBitmap(null);
            portaEquipajeE.setVisibility(View.GONE);
            //imagePortaEquipajeE.setVisibility(View.GONE);
            //imagePortaEquipajeE.setImageBitmap(null);
            portaSkyE.setVisibility(View.GONE);
            //imagePortaSkyE.setVisibility(View.GONE);
            //imagePortaSkyE.setImageBitmap(null);
            sunroofE.setVisibility(View.GONE);
            txtTipoSunroof.setVisibility(View.GONE);
            tipo_sunroof.setVisibility(View.GONE);
            imageSunroofE.setVisibility(View.GONE);
            imageSunroofE.setImageBitmap(null);
            maPorta.setVisibility(View.GONE);
            textView103.setVisibility(View.GONE);
            textView102.setVisibility(View.GONE);
            textView99.setVisibility(View.GONE);
            textView104.setVisibility(View.GONE);
            maParrilla.setVisibility(View.GONE);
            maBarras.setVisibility(View.GONE);
            maPorta.setVisibility(View.GONE);
            maPortaSki.setVisibility(View.GONE);


            String imagenTechoDano = db.foto(Integer.parseInt(id),db.comentarioFoto(Integer.parseInt(id),"techo"));

            if(imagenTechoDano.length()>3)
            {
                byte[] decodedString = Base64.decode(imagenTechoDano, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imagenTechoDanoE.setImageBitmap(decodedByte);
            }

            // spinnerDeducibleTechoE
            txtPieza.setVisibility(View.VISIBLE);
            spinnerPiezaTechoE.setVisibility(View.VISIBLE);
            spinnerPiezaTechoE = (Spinner)findViewById(R.id.spinnerPiezaTechoE);
            String listapieza[][] =  db.listaPiezasTecho();
            String[] listapiezaTecho = new String[listapieza.length+1];
            listapiezaTecho[0] = "Seleccionar Pieza";
            for(int i=0;i<listapieza.length;i++){
                listapiezaTecho[i+1] = listapieza[i][0];
            }
            ArrayAdapter<String> adapterTechoE = new ArrayAdapter<String>(vl_techo.this,android.R.layout.simple_spinner_item,listapiezaTecho);
            adapterTechoE.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerPiezaTechoE.setAdapter(adapterTechoE);
            spinnerPiezaTechoE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ///////SPINNERDAÑO POSTERIOR
                    if(position!=0) {
                        txtTipoDanoE.setVisibility(View.VISIBLE);
                        spinnerDanoTechoE.setVisibility(View.VISIBLE);
                        spinnerDanoTechoE = (Spinner) findViewById(R.id.spinnerDanoTechoE);
                        String listaDano[][] = db.listaDanoPosterior();
                        final String[] listaDanoPosterior = new String[listaDano.length + 1];
                        listaDanoPosterior[0] = "Seleccionar Daño";
                        for (int i = 0; i < listaDano.length; i++) {
                            listaDanoPosterior[i + 1] = listaDano[i][0];
                        }
                        ArrayAdapter<String> adapterDanoPosterior = new ArrayAdapter<String>(vl_techo.this, android.R.layout.simple_spinner_item, listaDanoPosterior);
                        adapterDanoPosterior.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerDanoTechoE.setAdapter(adapterDanoPosterior);

                        spinnerDanoTechoE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if(i!=0) {
                                    txtDeducibleE.setVisibility(View.VISIBLE);
                                    spinnerDeducibleTechoE.setVisibility(View.VISIBLE);
                                    String[][] listadedu = db.listaDeduciblesPosterior(spinnerDanoTechoE.getSelectedItem().toString());
                                    Spinner spinnerDeducibleTechoE = (Spinner) findViewById(R.id.spinnerDeducibleTechoE);
                                    String[] spinnerDedu = new String[listadedu.length + 1];
                                    spinnerDedu[0] = "Seleccione deducible";
                                    for (int ii = 0; ii < listadedu.length; ii++) {
                                        spinnerDedu[ii + 1] = listadedu[ii][0];
                                    }
                                    ArrayAdapter<String> adapterdedu = new ArrayAdapter<String>(vl_techo.this, android.R.layout.simple_spinner_item, spinnerDedu);
                                    adapterdedu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinnerDeducibleTechoE.setAdapter(adapterdedu);
                                    spinnerDeducibleTechoE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            if(position!=0){
                                                btnFotoDanoTechoE.setVisibility(View.VISIBLE);
                                                imagenTechoDanoE.setVisibility(View.VISIBLE);
                                                textCantT.setVisibility(View.VISIBLE);
                                                contPostT.setVisibility(View.VISIBLE);
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {}
                                    });
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {}
                        });
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            imagenTechoDanoE.setVisibility(View.VISIBLE);
        }

    }
    private void desplegarCamposSeccionTres(String id)    {
        if (barraPortaEquipajeE.getVisibility() == View.VISIBLE) {

            barraPortaEquipajeE.setVisibility(View.GONE);
            //imageBarraPortaEquipajeE.setVisibility(View.GONE);
            //imageBarraPortaEquipajeE.setImageBitmap(null);
            btnSisTecho.setVisibility(View.GONE);
            textCanTecho.setVisibility(View.GONE);
            contPosTecho.setVisibility(View.GONE);
            imageViewSistTecho.setVisibility(View.GONE);
            imageViewSistTecho.setImageBitmap(null);
            parrillaTechoE.setVisibility(View.GONE);
            //imageParrillaTechoE.setVisibility(View.GONE);
            //imageParrillaTechoE.setImageBitmap(null);
            portaEquipajeE.setVisibility(View.GONE);
            //imagePortaEquipajeE.setVisibility(View.GONE);
            //imagePortaEquipajeE.setImageBitmap(null);
            portaSkyE.setVisibility(View.GONE);
            //imagePortaSkyE.setVisibility(View.GONE);
            //imagePortaSkyE.setImageBitmap(null);
            sunroofE.setVisibility(View.GONE);
            txtTipoSunroof.setVisibility(View.GONE);
            tipo_sunroof.setVisibility(View.GONE);
            imageSunroofE.setVisibility(View.GONE);
            imageSunroofE.setImageBitmap(null);
            textView103.setVisibility(View.GONE);
            textView102.setVisibility(View.GONE);
            textView99.setVisibility(View.GONE);
            textView104.setVisibility(View.GONE);
            maParrilla.setVisibility(View.GONE);
            maBarras.setVisibility(View.GONE);
            maPorta.setVisibility(View.GONE);
            maPortaSki.setVisibility(View.GONE);
        }
        else
        {

            //seccion dos

            txtPieza.setVisibility(View.GONE);
            spinnerPiezaTechoE.setVisibility(View.GONE);
            txtTipoDanoE.setVisibility(View.GONE);
            spinnerDanoTechoE.setVisibility(View.GONE);
            txtDeducibleE.setVisibility(View.GONE);
            spinnerDeducibleTechoE.setVisibility(View.GONE);
            btnFotoDanoTechoE.setVisibility(View.GONE);
            imagenTechoDanoE.setVisibility(View.GONE);
            imagenTechoDanoE.setImageBitmap(null);
            textCantT.setVisibility(View.GONE);
            contPostT.setVisibility(View.GONE);




            /*String imageBarraPortaEquipaje = db.foto(Integer.parseInt(id),"Foto Barra Equipaje Techo");
            String imageParrillaTecho = db.foto(Integer.parseInt(id),"Foto Parrilla Techo");
            String imagePortaEquipaje = db.foto(Integer.parseInt(id),"Foto Porta Equipaje Techo");
            String imagePortaSky = db.foto(Integer.parseInt(id),"Foto Porta Sky Techo");*/

            String imageSunroof = db.foto(Integer.parseInt(id),"Foto Sunroof Techo");
            String imagenTecho = db.foto(Integer.parseInt(id),"Foto Techo");


           /* if(imageBarraPortaEquipaje.length()>3)
            {
                byte[] decodedString = Base64.decode(imageBarraPortaEquipaje, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageBarraPortaEquipajeE.setImageBitmap(decodedByte);
                barraPortaEquipajeE.setChecked(true);
            }
            if(imageParrillaTecho.length()>3)
            {
                byte[] decodedString = Base64.decode(imageParrillaTecho, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageParrillaTechoE.setImageBitmap(decodedByte);
                parrillaTechoE.setChecked(true);
            }
            if(imagePortaEquipaje.length()>3)
            {
                byte[] decodedString = Base64.decode(imagePortaEquipaje, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imagePortaEquipajeE.setImageBitmap(decodedByte);
                portaEquipajeE.setChecked(true);
            }
            if(imagePortaSky.length()>3)
            {
                byte[] decodedString = Base64.decode(imagePortaSky, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imagePortaSkyE.setImageBitmap(decodedByte);
                portaSkyE.setChecked(true);
            }*/

           if(imageSunroof.length()>3)
            {
                byte[] decodedString = Base64.decode(imageSunroof, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageSunroofE.setImageBitmap(decodedByte);
                sunroofE.setChecked(true);
            }

            if(imagenTecho.length()>3)
            {
                byte[] decodedString1 = Base64.decode(imagenTecho, Base64.DEFAULT);
                Bitmap decodedByte1 = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.length);
                imageViewSistTecho.setImageBitmap(decodedByte1);

            }



            barraPortaEquipajeE.setVisibility(View.VISIBLE);
            //imageBarraPortaEquipajeE.setVisibility(View.VISIBLE);
            parrillaTechoE.setVisibility(View.VISIBLE);
            //imageParrillaTechoE.setVisibility(View.VISIBLE);
            portaEquipajeE.setVisibility(View.VISIBLE);
            //imagePortaEquipajeE.setVisibility(View.VISIBLE);
            portaSkyE.setVisibility(View.VISIBLE);
            //imagePortaSkyE.setVisibility(View.VISIBLE);
            sunroofE.setVisibility(View.VISIBLE);
            txtTipoSunroof.setVisibility(View.VISIBLE);
            tipo_sunroof.setVisibility(View.VISIBLE);
            imageSunroofE.setVisibility(View.VISIBLE);
            textView103.setVisibility(View.VISIBLE);
            textView102.setVisibility(View.VISIBLE);
            textView99.setVisibility(View.VISIBLE);
            textView104.setVisibility(View.VISIBLE);
            maParrilla.setVisibility(View.VISIBLE);
            maBarras.setVisibility(View.VISIBLE);
            maPorta.setVisibility(View.VISIBLE);
            maPortaSki.setVisibility(View.VISIBLE);
            btnSisTecho.setVisibility(View.VISIBLE);
            textCanTecho.setVisibility(View.VISIBLE);
            contPosTecho.setVisibility(View.VISIBLE);
            imageViewSistTecho.setVisibility(View.VISIBLE);

        }

    }

    public void guardarDatos(){
        try{
            JSONObject datosTipoSun = new JSONObject();
            datosTipoSun.put("valor_id",263);
            datosTipoSun.put("texto",tipo_sunroof.getSelectedItem().toString());

            JSONObject valor75 = new JSONObject();
            valor75.put("valor_id",309);
            valor75.put("texto",maParrilla.getText().toString());

            JSONObject valor76 = new JSONObject();
            valor76.put("valor_id",773);
            valor76.put("texto",maBarras.getText().toString());

            JSONObject valor77 = new JSONObject();
            valor77.put("valor_id",772);
            valor77.put("texto",maPorta.getText().toString());

            JSONObject valor78 = new JSONObject();
            valor78.put("valor_id",771);
            valor78.put("texto",maPortaSki.getText().toString());

            JSONArray jsonArray = new JSONArray();
            jsonArray.put(datosTipoSun);
            jsonArray.put(valor75);
            jsonArray.put(valor76);
            jsonArray.put(valor77);
            jsonArray.put(valor78);

            Log.i("por  aca","por aca" + jsonArray.put(datosTipoSun));

            if(!jsonArray.isNull(0)){
                for(int i=0;i<jsonArray.length();i++){
                    llenado = new JSONObject(jsonArray.getString(i));
                    db.insertarValor(Integer.parseInt(oi),llenado.getInt("valor_id"),llenado.getString("texto"));

                   // Toast.makeText(vl_techo.this,"Error en los datos"+ tipo_sunroof.getSelectedItem().toString(),Toast.LENGTH_SHORT);

                }
            }

        }catch (Exception e){
            Toast.makeText(vl_techo.this,"Error en los datos",Toast.LENGTH_SHORT);
        }
    }
}
