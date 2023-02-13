package com.letchile.let.VehLiviano.Fotos;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
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


import org.w3c.dom.Text;

import java.io.File;
import java.text.SimpleDateFormat;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;





public class llantasneumaticos extends AppCompatActivity {

    DBprovider db;
    Boolean connec = false;

    private final int PHOTO_LLANTAS = 200;
    private final int PHOTO_RUEDA = 300;
    private final int PHOTO_ADICIONAL = 400;
    private Button btnLlantasVolverE,btnVolverSecLlaE,btnSiguienteLlantaE,btnLlantasNE,btnRuedaRespuestoLlantasE,btnAdicionalLlantasE,btnSeccionUnoLlantasE,seccionPos2E;
    private ImageView imageLlantasNE,imageRuedaRespuestoLlantasE,imageAdicionalLlantasE;
    private TextView textView40, textView41, textView83, textView84, textView85, textView86, textMarca, TextModelo,Textmedida,textView90,textView91,textView92, textView93,textView95, textView94, textView96, textCant28,contPost28, textCant29,contPost29, textCant30,contPost30,textViewN;
    Spinner estadoNeu, estadoLlanta, tipoRueda,ubiRueda;
    private EditText cantNeu, maNeu, moNeu, meNeu, cantNeu2, maNeu2, moNeu2,meNeu2, cantFierro, cantCromadas, cantAlea,maLlanta;
    private String mPath,checkFaltantes;
    private File ruta_sd;
    private String ruta = "",checkRuresps;
    private CheckBox checkFaltante, checkRuresp;
    PropiedadesFoto foto;
    String nombreimagen = "", id_inspeccion;
    int correlativo = 0;

    Validaciones validaciones;
    String [][] datosInspeccion;
    View linea;

    public llantasneumaticos(){db = new DBprovider(this);foto=new PropiedadesFoto(this);
        db = new DBprovider(this);validaciones=new Validaciones(this);    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llantasneumaticos);


       /* Bundle bundle = getIntent().getExtras();
        final String id_inspeccion = bundle.getString("id_inspeccion");*/

        Bundle bundle = getIntent().getExtras();
        id_inspeccion=bundle.getString("id_inspeccion");

        connec = new ConexionInternet(this).isConnectingToInternet();


        btnLlantasNE = findViewById(R.id.btnLlantasNE);
        imageLlantasNE = findViewById(R.id.imageLlantasNE);
        btnRuedaRespuestoLlantasE = findViewById(R.id.btnRuedaRespuestoLlantasE);
        imageRuedaRespuestoLlantasE = findViewById(R.id.imageRuedaRespuestoLlantasE);
        btnAdicionalLlantasE = findViewById(R.id.btnAdicionalLlantasE);
        imageAdicionalLlantasE = findViewById(R.id.imageAdicionalLlantasE);
        btnSeccionUnoLlantasE = findViewById(R.id.btnSeccionUnoLlantasE);
        seccionPos2E = findViewById(R.id.seccionPos2E);
        textView40 = findViewById(R.id.textView40);
        textView41 = findViewById(R.id.textView41);
        textView83 = findViewById(R.id.textView83);
        textView84 = findViewById(R.id.textView84);
        textView85 = findViewById(R.id.textView85);
        textView86 = findViewById(R.id.textView86);
        textMarca = findViewById(R.id.textMarca);
        TextModelo = findViewById(R.id.TextModelo);
        Textmedida = findViewById(R.id.Textmedida);
        textView90 = findViewById(R.id.textView90);
        textView91 = findViewById(R.id.textView91);
        textView92 = findViewById(R.id.textView92);
        textView93 = findViewById(R.id.textView93);
        textView95 = findViewById(R.id.textView95);
        textView94 = findViewById(R.id.textView94);
        textView96 = findViewById(R.id.textView96);
        textViewN = findViewById(R.id.textViewN);
        linea = findViewById(R.id.linea);

        textCant28 = findViewById(R.id.textCant28);
        contPost28 = findViewById(R.id.contPost28);
        contPost28.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Llantas y Neumaticos")));

        textCant29 = findViewById(R.id.textCant29);
        contPost29 = findViewById(R.id.contPost29);
        contPost29.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Rueda de Respuesto Llantas y Neumaticos")));

        textCant30 = findViewById(R.id.textCant30);
        contPost30 = findViewById(R.id.contPost30);
        contPost30.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Adicional Llantas y Neumaticos")));


        //cantidad neumaticos
        cantNeu = findViewById(R.id.cantNeu);
        cantNeu.setOnEditorActionListener(new PropiedadesTexto());
        cantNeu.setText(db.accesorio(Integer.parseInt(id_inspeccion),188).toString());

        //marca de neumaticos
        maNeu = findViewById(R.id.maNeu);
        maNeu.setOnEditorActionListener(new PropiedadesTexto());
        maNeu.setText(db.accesorio(Integer.parseInt(id_inspeccion),189).toString());

        //modelo de neumaticos
        moNeu = findViewById(R.id.moNeu);
        moNeu.setOnEditorActionListener(new PropiedadesTexto());
        moNeu.setText(db.accesorio(Integer.parseInt(id_inspeccion),190).toString());


        //medida de neumaticos
        meNeu = findViewById(R.id.meNeu);
        meNeu.setOnEditorActionListener(new PropiedadesTexto());
        meNeu.setText(db.accesorio(Integer.parseInt(id_inspeccion),191).toString());

        //cantidad de neumaticos 2
        cantNeu2 = findViewById(R.id.cantNeu2);
        cantNeu2.setOnEditorActionListener(new PropiedadesTexto());
        cantNeu2.setText(db.accesorio(Integer.parseInt(id_inspeccion),193).toString());

        //marca de neumaticos 2
        maNeu2 = findViewById(R.id.maNeu2);
        maNeu2.setOnEditorActionListener(new PropiedadesTexto());
        maNeu2.setText(db.accesorio(Integer.parseInt(id_inspeccion),194).toString());

        //modelo de neumaticos 2
        moNeu2 = findViewById(R.id.moNeu2);
        moNeu2.setOnEditorActionListener(new PropiedadesTexto());
        moNeu2.setText(db.accesorio(Integer.parseInt(id_inspeccion),195).toString());

        //medida de neumaticos 2
        meNeu2 = findViewById(R.id.meNeu2);
        meNeu2.setOnEditorActionListener(new PropiedadesTexto());
        meNeu2.setText(db.accesorio(Integer.parseInt(id_inspeccion),196).toString());

        //cantidad fierro
        cantFierro = findViewById(R.id.cantFierro);
        cantFierro.setOnEditorActionListener(new PropiedadesTexto());
        cantFierro.setText(db.accesorio(Integer.parseInt(id_inspeccion),207).toString());

        //cantidad cromada
        cantCromadas = findViewById(R.id.cantCromadas);
        cantCromadas.setOnEditorActionListener(new PropiedadesTexto());
        cantCromadas.setText(db.accesorio(Integer.parseInt(id_inspeccion),774).toString());

        //cantidad aleacion
        cantAlea = findViewById(R.id.cantAlea);
        cantAlea.setOnEditorActionListener(new PropiedadesTexto());
        cantAlea.setText(db.accesorio(Integer.parseInt(id_inspeccion),775).toString());

        //marca llanta
        maLlanta = findViewById(R.id.maLLanta);
        maLlanta.setOnEditorActionListener(new PropiedadesTexto());
        maLlanta.setText(db.accesorio(Integer.parseInt(id_inspeccion),208).toString());

        // cargar un combo neumáticos
        estadoNeu = findViewById(R.id.spinNeu);
        String[] arrayEstado = getResources().getStringArray(R.array.estado_neumatico);
        final List<String> arrayEstadoList = Arrays.asList(arrayEstado);
        ArrayAdapter<String> spinner_adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayEstadoList);
        spinner_adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estadoNeu.setAdapter(spinner_adapter1);
        estadoNeu.setSelection(arrayEstadoList.lastIndexOf(db.accesorio(Integer.parseInt(id_inspeccion),206).toString()));

        if (db.accesorio(Integer.parseInt(id_inspeccion),206).toString().equals("")) {
            estadoNeu.setSelection(0);

            if(estadoNeu.getSelectedItem().toString().equals("Seleccione..."))
            {
                estadoNeu.getSelectedItem().toString().equals("");

            }


        }
        else
        {
            estadoNeu.getSelectedItem().toString();

        }


        // cargar un combo estado llantas
        estadoLlanta = findViewById(R.id.spinLlanta);
        String[] arrayEstado2 = getResources().getStringArray(R.array.estado_llanta);
        final List<String> arrayEstadoList2 = Arrays.asList(arrayEstado2);
        ArrayAdapter<String> spinner_adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayEstadoList2);
        estadoLlanta.setAdapter(spinner_adapter2);
        estadoLlanta.setSelection(arrayEstadoList2.lastIndexOf(db.accesorio(Integer.parseInt(id_inspeccion),776).toString()));

        if (db.accesorio(Integer.parseInt(id_inspeccion),776).toString().equals("")) {
            estadoLlanta.setSelection(0);

            if(estadoLlanta.getSelectedItem().toString().equals("Seleccione..."))
            {
                estadoLlanta.getSelectedItem().toString().equals("");

            }


        }
        else
        {
            estadoLlanta.getSelectedItem().toString();

        }

       /* checkRuresp = findViewById(R.id.checkRuresp);
        if(db.accesorio(Integer.parseInt(id_inspeccion),289).toString().equals("Ok"))
        {
            checkRuresp.setChecked(true);
            checkRuresps = "Ok";
        }else{
            checkRuresp.setChecked(false);
            checkRuresps = "";
        }
        checkRuresp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    checkRuresps = "Ok";
                }else{
                    checkRuresps = "";
                }
            }
        });*/


        //check faltante
        checkFaltante = findViewById(R.id.checkFaltante);
        if(db.accesorio(Integer.parseInt(id_inspeccion),814).toString().equals("Ok"))
        {
            checkFaltante.setChecked(true);
            checkFaltantes = "Ok";
        }else{
            checkFaltante.setChecked(false);
            checkFaltantes = "";
        }
        checkFaltante.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    checkFaltantes = "Ok";
                }else{
                    checkFaltantes = "";
                }
            }
        });

        // cargar un combo tipo rueda repuesto
       tipoRueda = findViewById(R.id.tipoRueda);
        String[] arraytipo = getResources().getStringArray(R.array.tipo_rueda);
        final List<String> arraytipolist = Arrays.asList(arraytipo);
        ArrayAdapter<String> spinner_adapterT = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arraytipolist);
        spinner_adapterT.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoRueda.setAdapter(spinner_adapterT);
        tipoRueda.setSelection(arraytipolist.lastIndexOf(db.accesorio(Integer.parseInt(id_inspeccion),753).toString()));

        if (db.accesorio(Integer.parseInt(id_inspeccion),753).toString().equals("")) {
            tipoRueda.setSelection(0);

            if(tipoRueda.getSelectedItem().toString().equals("Seleccione tipo rueda repuesto..."))
            {
                tipoRueda.getSelectedItem().toString().equals("");

            }


        }
        else
        {
            tipoRueda.getSelectedItem().toString();

        }

        // cargar un combo ubicacion rueda repuesto
        ubiRueda = findViewById(R.id.ubiRueda);
        String[] arraytipo2 = getResources().getStringArray(R.array.ubicacion_rueda);
        final List<String> arraytipolist2 = Arrays.asList(arraytipo2);
        ArrayAdapter<String> spinner_adapterR = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arraytipolist2);
        spinner_adapterR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ubiRueda.setAdapter(spinner_adapterR);
        ubiRueda.setSelection(arraytipolist2.lastIndexOf(db.accesorio(Integer.parseInt(id_inspeccion),290).toString()));

        if (db.accesorio(Integer.parseInt(id_inspeccion),290).toString().equals("")) {
            ubiRueda.setSelection(0);

            if(ubiRueda.getSelectedItem().toString().equals("Seleccione ubicación rueda repuesto..."))
            {
                ubiRueda.getSelectedItem().toString().equals("");

            }

        }
        else
        {
            ubiRueda.getSelectedItem().toString();

        }


        btnSeccionUnoLlantasE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {desplegarCamposSeccionUno(id_inspeccion);
                //guardarDatos();
            }
        });

        seccionPos2E.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {desplegarCamposSeccionDos(id_inspeccion);
                //guardarDatos();
            }
        });

        btnLlantasNE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {funcionCamara(id_inspeccion,"_Foto_LLANTAS.jpg",PHOTO_LLANTAS);
            }
        });

        btnRuedaRespuestoLlantasE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcionCamara(id_inspeccion,"_Foto_Rueda_Respuesto.jpg",PHOTO_RUEDA);

            }
        });


        btnAdicionalLlantasE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcionCamara(id_inspeccion,"_Foto_Adicional_Llantas.jpg",PHOTO_ADICIONAL);
            }
        });

        btnLlantasVolverE = findViewById(R.id.btnLlantasVolverE);
        btnLlantasVolverE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                guardarDatos();
                Intent intent   = new Intent(llantasneumaticos.this,lateralizquierdo.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
                finish();

            }
        });

        btnSiguienteLlantaE = findViewById(R.id.btnSiguienteLlantaE);
        btnSiguienteLlantaE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String imageLlantasN = db.foto(Integer.parseInt(id_inspeccion),"Foto Llantas y Neumaticos");
                String imageRuedaRespuestoLlantas = db.foto(Integer.parseInt(id_inspeccion),"Foto Rueda de Respuesto Llantas y Neumaticos");
                String marcaNeu = maNeu.getText().toString();
                String cantidadNeu = cantNeu.getText().toString();
                String modeloNeu = moNeu.getText().toString();
                String medidaNeu = meNeu.getText().toString();

                Log.i("lala","lalala" + marcaNeu);



                if(imageLlantasN.length()<=3 && imageRuedaRespuestoLlantas.length()<=3)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(llantasneumaticos.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe Tomar Fotos Obligatorias</b><p><ul><li>- Foto Llantas y Neumáticos</li><p><li>- Foto Rueda de Respuesto</li></p></ul></p>"));
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else if(imageLlantasN.length()<3 )
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(llantasneumaticos.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe Tomar Fotos Obligatorias</b><p><ul><li>- Foto Llantas y Neumáticos</li></ul></p>"));
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else if(imageRuedaRespuestoLlantas.length()<3 )
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(llantasneumaticos.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe Tomar Fotos Obligatorias</b><p><ul><li>- Foto Rueda de Respuesto</li></ul></p>"));
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else if(cantidadNeu.equals("") && marcaNeu.equals("") && modeloNeu.equals("") && medidaNeu.equals("") )
                {

                    AlertDialog.Builder builder = new AlertDialog.Builder(llantasneumaticos.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe ingresar datos obligatorios </b><p><ul><li>- Cantidad neumático</li><p><li>- Marca neumático</li><p><li>- Modelo neumático</li><p><li>- Medida neumático</li></ul></p>"));
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else if(cantidadNeu.equals("")){

                    AlertDialog.Builder builder = new AlertDialog.Builder(llantasneumaticos.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe ingresar datos obligatorios </b><p><ul><li>- Cantidad neumático</li></ul></p>"));
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else if(marcaNeu.equals("")){

                    AlertDialog.Builder builder = new AlertDialog.Builder(llantasneumaticos.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe ingresar datos obligatorios </b><p><ul><li>- Marca neumático</li></ul></p>"));
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else if(modeloNeu.equals("")){

                    AlertDialog.Builder builder = new AlertDialog.Builder(llantasneumaticos.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe ingresar datos obligatorios </b><p><ul><li>- Modelo neumático</li></ul></p>"));
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else if(medidaNeu.equals("")){

                    AlertDialog.Builder builder = new AlertDialog.Builder(llantasneumaticos.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe ingresar datos obligatorios </b><p><ul><li>- Medida neumático</li></ul></p>"));
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else{
                    guardarDatos();
                    Intent intent   = new Intent(llantasneumaticos.this,vl_techo.class);
                    intent.putExtra("id_inspeccion",id_inspeccion);
                    startActivity(intent);
                    finish();
                   // Log.i("dato2","dato2" + estadoLlanta.getSelectedItem().toString());
                }

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
            intent.putExtra(MediaStore.EXTRA_OUTPUT,  FileProvider.getUriForFile(llantasneumaticos.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            startActivityForResult(intent, CodigoFoto);
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion=bundle.getString("id_inspeccion");

        try {

            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    case PHOTO_LLANTAS:
                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });

                        Bitmap bitmapLlanatas = BitmapFactory.decodeFile(mPath);
                        bitmapLlanatas = foto.redimensiomarImagen(bitmapLlanatas);

                        String imagenLlantas = foto.convertirImagenDano(bitmapLlanatas);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Llantas y Neumaticos", 0, imagenLlantas, 0);
                        imagenLlantas = "data:image/jpg;base64,"+imagenLlantas;
                        String base64Image1 = imagenLlantas.split(",")[1];
                        byte[] decodedString1 = Base64.decode(base64Image1, Base64.DEFAULT);
                        Bitmap decodedByte1 = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.length);
                        imageLlantasNE.setImageBitmap(decodedByte1);

                        Intent servis = new Intent(llantasneumaticos.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Foto Llantas y Neumaticos");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        int cantFoto=db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Llantas y Neumaticos");
                        cantFoto= cantFoto +1;
                        db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Foto Llantas y Neumaticos",cantFoto);
                        contPost28.setText(String.valueOf(cantFoto));

                        break;
                    case PHOTO_RUEDA:
                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });

                        Bitmap bitmapRueda = BitmapFactory.decodeFile(mPath);
                        bitmapRueda = foto.redimensiomarImagen(bitmapRueda);

                        String imagenRueda = foto.convertirImagenDano(bitmapRueda);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Rueda de Respuesto Llantas y Neumaticos", 0, imagenRueda, 0);
                        db.insertarValor(Integer.parseInt(id_inspeccion), 289, "Ok");
                        imagenRueda = "data:image/jpg;base64,"+imagenRueda;
                        String base64Image2 = imagenRueda.split(",")[1];
                        byte[] decodedString2 = Base64.decode(base64Image2, Base64.DEFAULT);
                        Bitmap decodedByte2 = BitmapFactory.decodeByteArray(decodedString2, 0, decodedString2.length);
                        imageRuedaRespuestoLlantasE.setImageBitmap(decodedByte2);

                        servis = new Intent(llantasneumaticos.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Foto Rueda de Respuesto Llantas y Neumaticos");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);


                        int cantFoto1=db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Rueda de Respuesto Llantas y Neumaticos");
                        cantFoto1= cantFoto1 +1;
                        db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Foto Rueda de Respuesto Llantas y Neumaticos",cantFoto1);
                        contPost29.setText(String.valueOf(cantFoto1));

                        break;
                    case PHOTO_ADICIONAL:
                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });

                        Bitmap bitmapAdicionalLlantas = BitmapFactory.decodeFile(mPath);
                        bitmapAdicionalLlantas = foto.redimensiomarImagen(bitmapAdicionalLlantas);
                        imageAdicionalLlantasE.setImageBitmap(bitmapAdicionalLlantas);
                        String imagenAdicionalLlantas = foto.convertirImagenDano(bitmapAdicionalLlantas);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Adicional Llantas y Neumaticos", 0, imagenAdicionalLlantas, 0);

                        imagenAdicionalLlantas = "data:image/jpg;base64,"+imagenAdicionalLlantas;
                        String base64Image3 = imagenAdicionalLlantas.split(",")[1];
                        byte[] decodedString3 = Base64.decode(base64Image3, Base64.DEFAULT);
                        Bitmap decodedByte3 = BitmapFactory.decodeByteArray(decodedString3, 0, decodedString3.length);


                        servis = new Intent(llantasneumaticos.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Foto Adicional Llantas y Neumaticos");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        int cantFoto2=db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Adicional Llantas y Neumaticos");
                        cantFoto2= cantFoto2 +1;
                        db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Foto Adicional Llantas y Neumaticos",cantFoto2);
                        contPost30.setText(String.valueOf(cantFoto2));

                        break;
                }
            }
        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }
    }



    public void desplegarCamposSeccionUno(String id)    {
        if (btnLlantasNE.getVisibility()==View.VISIBLE)
        {
            btnLlantasNE.setVisibility(View.GONE);
            imageLlantasNE.setVisibility(View.GONE);
            imageLlantasNE.setImageBitmap(null);
            btnRuedaRespuestoLlantasE.setVisibility(View.GONE);
            imageRuedaRespuestoLlantasE.setVisibility(View.GONE);
            imageRuedaRespuestoLlantasE.setImageBitmap(null);
            checkFaltante.setVisibility(View.GONE);
            tipoRueda.setVisibility(View.GONE);
            ubiRueda.setVisibility(View.GONE);
            btnAdicionalLlantasE.setVisibility(View.GONE);
            imageAdicionalLlantasE.setVisibility(View.GONE);
            imageAdicionalLlantasE.setImageBitmap(null);
            textCant28.setVisibility(View.GONE);
            contPost28.setVisibility(View.GONE);
            textCant29.setVisibility(View.GONE);
            contPost29.setVisibility(View.GONE);
            textCant30.setVisibility(View.GONE);
            contPost30.setVisibility(View.GONE);

        }
        else
        {
            String imageLlantasN = db.foto(Integer.parseInt(id),"Foto Llantas y Neumaticos");
            String imageRuedaRespuestoLlantas = db.foto(Integer.parseInt(id),"Foto Rueda de Respuesto Llantas y Neumaticos");
            String imageAdicionalLlantas = db.foto(Integer.parseInt(id),"Foto Adicional Llantas y Neumaticos");

            if(imageLlantasN.length()>3)
            {
                byte[] decodedString = Base64.decode(imageLlantasN, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageLlantasNE.setImageBitmap(decodedByte);
            }

            if(imageRuedaRespuestoLlantas.length()>3)
            {
                byte[] decodedString = Base64.decode(imageRuedaRespuestoLlantas, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageRuedaRespuestoLlantasE.setImageBitmap(decodedByte);
            }
            if(imageAdicionalLlantas.length()>3)
            {
                byte[] decodedString = Base64.decode(imageAdicionalLlantas, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageAdicionalLlantasE.setImageBitmap(decodedByte);
            }


            btnLlantasNE.setVisibility(View.VISIBLE);
            imageLlantasNE.setVisibility(View.VISIBLE);
            btnRuedaRespuestoLlantasE.setVisibility(View.VISIBLE);
            imageRuedaRespuestoLlantasE.setVisibility(View.VISIBLE);
            checkFaltante.setVisibility(View.VISIBLE);
            tipoRueda.setVisibility(View.VISIBLE);
            ubiRueda.setVisibility(View.VISIBLE);
            btnAdicionalLlantasE.setVisibility(View.VISIBLE);
            imageAdicionalLlantasE.setVisibility(View.VISIBLE);
            textCant28.setVisibility(View.VISIBLE);
            contPost28.setVisibility(View.VISIBLE);
            textCant29.setVisibility(View.VISIBLE);
            contPost29.setVisibility(View.VISIBLE);
            textCant30.setVisibility(View.VISIBLE);
            contPost30.setVisibility(View.VISIBLE);

        }

    }



    public void desplegarCamposSeccionDos(String id)    {
        if (textView40.getVisibility()==View.VISIBLE)
        {
            textView40.setVisibility(View.GONE);
            textView41.setVisibility(View.GONE);
            textView83.setVisibility(View.GONE);
            textView84.setVisibility(View.GONE);
            textView85.setVisibility(View.GONE);
            textView86.setVisibility(View.GONE);
            textMarca.setVisibility(View.GONE);
            TextModelo.setVisibility(View.GONE);
            Textmedida.setVisibility(View.GONE);
            textView90.setVisibility(View.GONE);
            textView91.setVisibility(View.GONE);
            textView93.setVisibility(View.GONE);
            textView94.setVisibility(View.GONE);
            textView95.setVisibility(View.GONE);
            textView96.setVisibility(View.GONE);
            estadoNeu.setVisibility(View.GONE);
            estadoLlanta.setVisibility(View.GONE);
            cantNeu.setVisibility(View.GONE);
            maNeu.setVisibility(View.GONE);
            moNeu.setVisibility(View.GONE);
            meNeu.setVisibility(View.GONE);
            linea.setVisibility(View.GONE);
            cantNeu2.setVisibility(View.GONE);
            maNeu2.setVisibility(View.GONE);
            moNeu2.setVisibility(View.GONE);
            meNeu2.setVisibility(View.GONE);
            cantFierro.setVisibility(View.GONE);
            cantCromadas.setVisibility(View.GONE);
            cantAlea.setVisibility(View.GONE);
            maLlanta.setVisibility(View.GONE);
            textViewN.setVisibility(View.GONE);


        }
        else
        {

            btnLlantasNE.setVisibility(View.GONE);
            imageLlantasNE.setVisibility(View.GONE);
            imageLlantasNE.setImageBitmap(null);
            btnRuedaRespuestoLlantasE.setVisibility(View.GONE);
            imageRuedaRespuestoLlantasE.setVisibility(View.GONE);
            imageRuedaRespuestoLlantasE.setImageBitmap(null);
            checkFaltante.setVisibility(View.GONE);
            tipoRueda.setVisibility(View.GONE);
            ubiRueda.setVisibility(View.GONE);
            btnAdicionalLlantasE.setVisibility(View.GONE);
            imageAdicionalLlantasE.setVisibility(View.GONE);
            imageAdicionalLlantasE.setImageBitmap(null);
            textCant28.setVisibility(View.GONE);
            contPost28.setVisibility(View.GONE);
            textCant29.setVisibility(View.GONE);
            contPost29.setVisibility(View.GONE);
            textCant30.setVisibility(View.GONE);
            contPost30.setVisibility(View.GONE);


            textView40.setVisibility(View.VISIBLE);
            textView41.setVisibility(View.VISIBLE);
            textView83.setVisibility(View.VISIBLE);
            textView84.setVisibility(View.VISIBLE);
            textView85.setVisibility(View.VISIBLE);
            textView86.setVisibility(View.VISIBLE);
            textMarca.setVisibility(View.VISIBLE);
            TextModelo.setVisibility(View.VISIBLE);
            Textmedida.setVisibility(View.VISIBLE);
            textView90.setVisibility(View.VISIBLE);
            textView91.setVisibility(View.VISIBLE);
            textView93.setVisibility(View.VISIBLE);
            textView94.setVisibility(View.VISIBLE);
            textView95.setVisibility(View.VISIBLE);
            textView96.setVisibility(View.VISIBLE);
            estadoNeu.setVisibility(View.VISIBLE);
            estadoLlanta.setVisibility(View.VISIBLE);
            cantNeu.setVisibility(View.VISIBLE);
            maNeu.setVisibility(View.VISIBLE);
            moNeu.setVisibility(View.VISIBLE);
            meNeu.setVisibility(View.VISIBLE);
            linea.setVisibility(View.VISIBLE);
            cantNeu2.setVisibility(View.VISIBLE);
            maNeu2.setVisibility(View.VISIBLE);
            moNeu2.setVisibility(View.VISIBLE);
            meNeu2.setVisibility(View.VISIBLE);
            cantFierro.setVisibility(View.VISIBLE);
            cantCromadas.setVisibility(View.VISIBLE);
            cantAlea.setVisibility(View.VISIBLE);
            maLlanta.setVisibility(View.VISIBLE);
            textViewN.setVisibility(View.VISIBLE);

        }

    }

    public void guardarDatos(){
        try{


            JSONObject valor1 = new JSONObject();
            valor1.put("valor_id",188);
            valor1.put("texto",cantNeu.getText().toString());

            JSONObject valor2 = new JSONObject();
            valor2.put("valor_id",189);
            valor2.put("texto",maNeu.getText().toString());

            JSONObject valor3 = new JSONObject();
            valor3.put("valor_id",190);
            valor3.put("texto",moNeu.getText().toString());

            JSONObject valor4 = new JSONObject();
            valor4.put("valor_id",191);
            valor4.put("texto",meNeu.getText().toString());

            JSONObject valor5 = new JSONObject();
            valor5.put("valor_id",193);
            valor5.put("texto",cantNeu2.getText().toString());

            JSONObject valor6 = new JSONObject();
            valor6.put("valor_id",194);
            valor6.put("texto",maNeu2.getText().toString());

            JSONObject valor7 = new JSONObject();
            valor7.put("valor_id",195);
            valor7.put("texto",moNeu2.getText().toString());

            JSONObject valor8 = new JSONObject();
            valor8.put("valor_id",196);
            valor8.put("texto",meNeu2.getText().toString());

            JSONObject valor9 = new JSONObject();
            valor9.put("valor_id",207);
            valor9.put("texto",cantFierro.getText().toString());

            JSONObject valor10 = new JSONObject();
            valor10.put("valor_id",774);
            valor10.put("texto",cantCromadas.getText().toString());

            JSONObject valor11 = new JSONObject();
            valor11.put("valor_id",775);
            valor11.put("texto",cantAlea.getText().toString());

            JSONObject valor12 = new JSONObject();
            valor12.put("valor_id",208);
            valor12.put("texto",maLlanta.getText().toString());

            JSONObject valor79 = new JSONObject();
            valor79.put("valor_id",206);
            valor79.put("texto",estadoNeu.getSelectedItem().toString());



            JSONObject valor75 = new JSONObject();
            valor75.put("valor_id",753);
            valor75.put("texto",tipoRueda.getSelectedItem().toString());

            JSONObject valor76 = new JSONObject();
            valor76.put("valor_id",290);
            valor76.put("texto",ubiRueda.getSelectedItem().toString());

            JSONObject valor80 = new JSONObject();
            valor80.put("valor_id",776);
            valor80.put("texto",estadoLlanta.getSelectedItem().toString());


            JSONObject valor87 = new JSONObject();
            valor87.put("valor_id",289);
            valor87.put("texto","Ok");

            JSONObject valor88 = new JSONObject();
            valor88.put("valor_id",814);
            valor88.put("texto",checkFaltantes);


            JSONArray jsonArray = new JSONArray();

            jsonArray.put(valor1);
            jsonArray.put(valor2);
            jsonArray.put(valor3);
            jsonArray.put(valor4);
            jsonArray.put(valor5);
            jsonArray.put(valor6);
            jsonArray.put(valor7);
            jsonArray.put(valor8);
            jsonArray.put(valor9);
            jsonArray.put(valor10);
            jsonArray.put(valor11);
            jsonArray.put(valor12);
            jsonArray.put(valor79);
            jsonArray.put(valor75);
            jsonArray.put(valor76);
            jsonArray.put(valor80);
            jsonArray.put(valor87);
            jsonArray.put(valor88);

            JSONObject llenado;

            Log.i("por  aca","por aca" + jsonArray.put(valor80));
            //PREGUNTO SI ES NULO PARA INSERTAR LOS DATOS

            //Log.e("largo json ", Integer.toString(jsonArray.length()));
            if (!jsonArray.isNull(0)) {
                for(int i=0;i<jsonArray.length();i++){

                    // Log.e("valor ii ", Integer.toString(i));
                    llenado = new JSONObject(jsonArray.getString(i));
                    //Log.e("valor json ", jsonArray.getString(i));

                    Log.e("INSERTA EN  CODIGO ", llenado.getString("valor_id"));

                    db.insertarValor(Integer.parseInt(id_inspeccion),llenado.getInt("valor_id"),llenado.getString("texto"));
                    //validaciones.insertarDatos(Integer.parseInt(id_inspeccion),llenado.getInt("valor_id"),llenado.getString("texto"));
                    Log.e("INSERTA EN  CODIGOo ", db.insertarValor(Integer.parseInt(id_inspeccion),llenado.getInt("valor_id"),llenado.getString("texto")));


                }
            }





        }catch (Exception e)
        {

            Toast.makeText(llantasneumaticos.this,e.getMessage(),Toast.LENGTH_SHORT);
        }
    }



}
