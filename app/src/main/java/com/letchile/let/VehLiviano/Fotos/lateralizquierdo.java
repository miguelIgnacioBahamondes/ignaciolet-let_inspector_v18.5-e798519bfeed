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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.BuildConfig;
import com.letchile.let.Clases.PropiedadesFoto;
import com.letchile.let.Clases.Validaciones;
import com.letchile.let.R;
import com.letchile.let.Servicios.ConexionInternet;
import com.letchile.let.Servicios.TransferirFoto;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class lateralizquierdo extends AppCompatActivity {

    DBprovider db;
    Boolean connec = false;
    private final int PHOTO_LATERALIZ = 200;
    private final int PHOTO_ADICIONAL1 = 300;
    private final int PHOTO_ADICIONAL2 = 400;
    private final int PHOTO_DANO = 500;
    private final int PHOTO_GRABA = 600;
    private final int PHOTO_LAMINA = 700;
    private final int PHOTO_PIZADERA = 800;
   // private final int PHOTO_POLARIZADO = 900;
    private Button btnVolverLaIzE,btnVolverSecLaIzE,btnSiguienteLaIzE,btnLateIzE,btnAdicionalIzE,btnAdicionalIz2E,btnFotoDanoIzE,btnSeccionUnoLaIzE,btnSeccionDosLaIzE,btnSeccionTresLaIzE;
    private ImageView imageLateIzE,imageAdicionalIzE,imageAdicionalIz2E,imagenIzDanoE,imageGabradoPatenteE,imagelaminasSeguridadLaIzE,imagePisaderasLaIzE,imagePolarizadoLaIzE;
    private CheckBox gabradoPatenteE,laminasSeguridadLaIzE,pisaderasLaIzE,polarizadoLaIzE;
    private Spinner spinnerPiezaIzE,spinnerDanoIzE,spinnerDeducibleIzE;
    private TextView txtPieza,txtTipoDanoE,txtDeducibleIzE,textCant16,contPost16,textCant17,contPost17,textCant18,contPost18,textCantLI,contPostLI;
    private String mPath;
    private File ruta_sd;
    private String ruta = "";
    String nombreimagen = "", comentarioDañoImg="";
    PropiedadesFoto foto;
    Validaciones validaciones;
    int correlativo = 0;
    String dañosDedu[][];
    View linea;

    public lateralizquierdo(){db = new DBprovider(this);foto=new PropiedadesFoto(this);validaciones = new Validaciones(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lateralizquierdo);

        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion = bundle.getString("id_inspeccion");

        connec = new ConexionInternet(this).isConnectingToInternet();

        btnLateIzE = findViewById(R.id.btnLateIzE);
        imageLateIzE = findViewById(R.id.imageLateIzE);
        btnAdicionalIzE = findViewById(R.id.btnAdicionalIzE);
        imageAdicionalIzE = findViewById(R.id.imageAdicionalIzE);
       // btnAdicionalIz2E = findViewById(R.id.btnAdicionalIz2E);
        //imageAdicionalIz2E = findViewById(R.id.imageAdicionalIz2E);
        btnFotoDanoIzE = findViewById(R.id.btnFotoDanoIzE);
        imagenIzDanoE = findViewById(R.id.imagenIzDanoE);
        gabradoPatenteE = findViewById(R.id.gabradoPatenteE);
        imageGabradoPatenteE = findViewById(R.id.imageGabradoPatenteE);
        laminasSeguridadLaIzE = findViewById(R.id.laminasSeguridadLaIzE);
        imagelaminasSeguridadLaIzE = findViewById(R.id.imagelaminasSeguridadLaIzE);
        pisaderasLaIzE = findViewById(R.id.pisaderasLaIzE);
        imagePisaderasLaIzE = findViewById(R.id.imagePisaderasLaIzE);
        polarizadoLaIzE = findViewById(R.id.polarizadoLaIzE);
        //imagePolarizadoLaIzE = findViewById(R.id.imagePolarizadoLaIzE);
        btnSeccionUnoLaIzE = findViewById(R.id.btnSeccionUnoLaIzE);
        btnSeccionDosLaIzE = findViewById(R.id.btnSeccionDosLaIzE);
        btnSeccionTresLaIzE = findViewById(R.id.btnSeccionTresLaIzE);
        txtPieza = findViewById(R.id.txtPieza);
        txtTipoDanoE = findViewById(R.id.txtTipoDanoE);
        txtDeducibleIzE = findViewById(R.id.txtDeducibleIzE);
        spinnerPiezaIzE = findViewById(R.id.spinnerPiezaIzE);
        spinnerDanoIzE = findViewById(R.id.spinnerDanoIzE);
        spinnerDeducibleIzE = findViewById(R.id.spinnerDeducibleIzE);
        linea = findViewById(R.id.linea);

        textCant16 = findViewById(R.id.textCant16);
        contPost16 = findViewById(R.id.contPost16);
        contPost16.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Lateral Izquierdo")));

        textCant17 = findViewById(R.id.textCant17);
        contPost17 = findViewById(R.id.contPost17);
        contPost17.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Adicional 1 Lateral Izquierdo")));

        /*textCant18 = findViewById(R.id.textCant18);
        contPost18 = findViewById(R.id.contPost18);
        contPost18.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Adicional 2 Lateral Izquierdo")));*/

        textCantLI = findViewById(R.id.textCantLI);
        contPostLI = findViewById(R.id.contPostLI);
        contPostLI.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"lateral_izquierdo")));


        btnSeccionUnoLaIzE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {DesplegarCamposSeccionUno(id_inspeccion);}
        });
        btnSeccionDosLaIzE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {desplegarCamposSeccionDos(id_inspeccion);
            }
        });
        btnSeccionTresLaIzE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {desplegarCamposSeccionTres(id_inspeccion);}
        });

        btnLateIzE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {funcionCamara(id_inspeccion,"_Foto_Lateral_Izquierdo.jpg",PHOTO_LATERALIZ);}
        });
        btnAdicionalIzE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {funcionCamara(id_inspeccion,"_Foto_Adicional1_Lateral_Izquierdo.jpg",PHOTO_ADICIONAL1);}
        });
        /*btnAdicionalIz2E.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {funcionCamara(id_inspeccion,"_Foto_Adicional2_LateraL_Izquierdo.jpg",PHOTO_ADICIONAL2);}
        });*/
        btnFotoDanoIzE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  funcionCamara(id_inspeccion,"_Foto_Dano_LateraL_Izquierdo.jpg",PHOTO_DANO); }
        });

        gabradoPatenteE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),298));
        gabradoPatenteE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 298).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_Graba_Patente.jpg",PHOTO_GRABA);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),298,"");
                    imageGabradoPatenteE.setImageBitmap(null);
                }
            }
        });

        laminasSeguridadLaIzE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),342));
        laminasSeguridadLaIzE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 342).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_Lamina_Seguridad.jpg",PHOTO_LAMINA);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),342,"");
                    imagelaminasSeguridadLaIzE.setImageBitmap(null);
                }
            }
        });

        pisaderasLaIzE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),276));
        pisaderasLaIzE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 276).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_Pisadera_Lateral_Izquierdo.jpg",PHOTO_PIZADERA);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),276,"");
                    imagePisaderasLaIzE.setImageBitmap(null);
                }
            }
        });

        polarizadoLaIzE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),341));
        polarizadoLaIzE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 341).toString().equals("Ok")) {

                        db.insertarValor(Integer.parseInt(id_inspeccion),341,"Ok");
                        //funcionCamara(id_inspeccion,"_Foto_Polarizado_Lateral_Izquierdo.jpg",PHOTO_POLARIZADO);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),341,"");
                   // imagePolarizadoLaIzE.setImageBitmap(null);
                }
            }
        });

        btnVolverLaIzE = (Button)findViewById(R.id.btnVolverLaIzE);
        btnVolverLaIzE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(lateralizquierdo.this, frontal.class);
                intent.putExtra("id_inspeccion", id_inspeccion);
                startActivity(intent);
                finish();
            }
        });
        btnSiguienteLaIzE = (Button)findViewById(R.id.btnSiguienteLaIzE);
        btnSiguienteLaIzE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String imageLateIz = db.foto(Integer.parseInt(id_inspeccion),"Foto Lateral Izquierdo");

                if(imageLateIz.length()<=3)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(lateralizquierdo.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe Tomar Fotos Obligatorias</b><p><ul><li>- Foto Lateral Izquierdo</li></ul></p>"));
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else {
                    Intent intent = new Intent(lateralizquierdo.this, llantasneumaticos.class);
                    intent.putExtra("id_inspeccion", id_inspeccion);
                    startActivity(intent);
                    finish();
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
            intent.putExtra(MediaStore.EXTRA_OUTPUT,  FileProvider.getUriForFile(lateralizquierdo.this,
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
                    case PHOTO_LATERALIZ:
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

                        String imagen = foto.convertirImagenDano(bitmap);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Lateral Izquierdo", 0, imagen, 0);

                        imagen = "data:image/jpg;base64,"+imagen;
                        String base64Image = imagen.split(",")[1];
                        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imageLateIzE.setImageBitmap(decodedByte);

                        Intent servis = new Intent(lateralizquierdo.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Foto Lateral Izquierdo");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        int cantFoto16=db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Lateral Izquierdo");
                        cantFoto16= cantFoto16 +1;
                        db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Foto Lateral Izquierdo",cantFoto16);
                        contPost16.setText(String.valueOf(cantFoto16));

                        break;
                    case PHOTO_ADICIONAL1:
                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });

                        Bitmap bitmapAdicional1 = BitmapFactory.decodeFile(mPath);
                        bitmapAdicional1 = foto.redimensiomarImagen(bitmapAdicional1);

                        String imagenAdicional1 = foto.convertirImagenDano(bitmapAdicional1);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Adicional 1 Lateral Izquierdo", 0, imagenAdicional1, 0);
                        imagenAdicional1 = "data:image/jpg;base64,"+imagenAdicional1;
                        String base64Image1 = imagenAdicional1.split(",")[1];
                        byte[] decodedString1 = Base64.decode(base64Image1, Base64.DEFAULT);
                        Bitmap decodedByte1 = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.length);
                        imageAdicionalIzE.setImageBitmap(decodedByte1);


                        servis = new Intent(lateralizquierdo.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Foto Adicional 1 Lateral Izquierdo");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        int cantFoto17=db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Adicional 1 Lateral Izquierdo");
                        cantFoto17= cantFoto17 +1;
                        db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Foto Adicional 1 Lateral Izquierdo",cantFoto17);
                        contPost17.setText(String.valueOf(cantFoto17));

                        break;
                    /*case PHOTO_ADICIONAL2:
                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });

                        Bitmap bitmapAdicional2 = BitmapFactory.decodeFile(mPath);
                        bitmapAdicional2 = foto.redimensiomarImagen(bitmapAdicional2);

                        String imagenAdicional2 = foto.convertirImagenDano(bitmapAdicional2);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Adicional 2 Lateral Izquierdo", 0, imagenAdicional2, 0);
                        imagenAdicional2 = "data:image/jpg;base64,"+imagenAdicional2;
                        String base64Image2 = imagenAdicional2.split(",")[1];
                        byte[] decodedString2 = Base64.decode(base64Image2, Base64.DEFAULT);
                        Bitmap decodedByte2 = BitmapFactory.decodeByteArray(decodedString2, 0, decodedString2.length);
                        imageAdicionalIz2E.setImageBitmap(decodedByte2);

                        servis = new Intent(lateralizquierdo.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Foto Adicional 2 Lateral Izquierdo");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        int cantFoto18=db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Adicional 2 Lateral Izquierdo");
                        cantFoto18= cantFoto18 +1;
                        db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Foto Adicional 2 Lateral Izquierdo",cantFoto18);
                        contPost18.setText(String.valueOf(cantFoto18));

                        break;*/
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

                        Bitmap bitmapDano = BitmapFactory.decodeFile(mPath);
                        bitmapDano = foto.redimensiomarImagen(bitmapDano);

                        String imagenDano = foto.convertirImagenDano(bitmapDano);


                        comentarioDañoImg = spinnerPiezaIzE.getSelectedItem().toString() + ' ' + spinnerDanoIzE.getSelectedItem().toString() + ' ' + spinnerDeducibleIzE.getSelectedItem().toString() + ' ';
                        db.insertarComentarioFoto(Integer.parseInt(id_inspeccion), comentarioDañoImg, "lateral_izquierdo");
                        String comentarito = db.comentarioFoto(Integer.parseInt(id_inspeccion), "lateral_izquierdo");

                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, comentarito, 0, imagenDano, 0);

                        dañosDedu = db.DeduciblePieza(spinnerPiezaIzE.getSelectedItem().toString(), "lateral_izquierdo");
                        //daño
                        db.insertarValor(Integer.parseInt(id_inspeccion), Integer.parseInt(dañosDedu[0][0]), String.valueOf(db.obtenerDanio(spinnerDanoIzE.getSelectedItem().toString())));
                        //deducible
                       db.insertarValor(Integer.parseInt(id_inspeccion), Integer.parseInt(dañosDedu[0][1]), db.obtenerDeducible(db.obtenerDanio(spinnerDanoIzE.getSelectedItem().toString()), spinnerDeducibleIzE.getSelectedItem().toString()));
                        //db.insertarValor(Integer.parseInt(id_inspeccion), Integer.parseInt(dañosDedu[0][1]), spinnerDeducibleIzE.getSelectedItem().toString());

                        imagenDano = "data:image/jpg;base64,"+imagenDano;
                        String base64Image3 = imagenDano.split(",")[1];
                        byte[] decodedString3 = Base64.decode(base64Image3, Base64.DEFAULT);
                        Bitmap decodedByte3 = BitmapFactory.decodeByteArray(decodedString3, 0, decodedString3.length);
                        imagenIzDanoE.setImageBitmap(decodedByte3);

                        servis = new Intent(lateralizquierdo.this, TransferirFoto.class);
                        servis.putExtra("comentario", comentarito);
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        int cantFotoLI=db.cantidadF(Integer.parseInt(id_inspeccion),"lateral_izquierdo");
                        cantFotoLI= cantFotoLI +1;
                        db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"lateral_izquierdo",cantFotoLI);
                        contPostLI.setText(String.valueOf(cantFotoLI));


                        break;
                    case PHOTO_GRABA:
                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });

                        Bitmap bitmapGraba = BitmapFactory.decodeFile(mPath);
                        bitmapGraba = foto.redimensiomarImagen(bitmapGraba);

                        String imagenGraba = foto.convertirImagenDano(bitmapGraba);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Grabado Patente Lateral Izquierdo", 0, imagenGraba, 0);
                        db.insertarValor(Integer.parseInt(id_inspeccion), 298, "Ok");
                        imagenGraba = "data:image/jpg;base64,"+imagenGraba;
                        String base64Image4 = imagenGraba.split(",")[1];
                        byte[] decodedString4 = Base64.decode(base64Image4, Base64.DEFAULT);
                        Bitmap decodedByte4 = BitmapFactory.decodeByteArray(decodedString4, 0, decodedString4.length);
                        imageGabradoPatenteE.setImageBitmap(decodedByte4);


                        servis = new Intent(lateralizquierdo.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Foto Grabado Patente Lateral Izquierdo");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        break;
                    case PHOTO_LAMINA:
                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });

                        Bitmap bitmapLamina = BitmapFactory.decodeFile(mPath);
                        bitmapLamina = foto.redimensiomarImagen(bitmapLamina);

                        String imagenLamina = foto.convertirImagenDano(bitmapLamina);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Lamina de Seguridad Lateral Izquierdo", 0, imagenLamina, 0);
                        db.insertarValor(Integer.parseInt(id_inspeccion), 342, "Ok");
                        imagenLamina = "data:image/jpg;base64,"+imagenLamina;
                        String base64Image5 = imagenLamina.split(",")[1];
                        byte[] decodedString5 = Base64.decode(base64Image5, Base64.DEFAULT);
                        Bitmap decodedByte5 = BitmapFactory.decodeByteArray(decodedString5, 0, decodedString5.length);

                        imagelaminasSeguridadLaIzE.setImageBitmap(decodedByte5);


                        servis = new Intent(lateralizquierdo.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Foto Lamina de Seguridad Lateral Izquierdo");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        break;
                    case PHOTO_PIZADERA:
                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });

                        Bitmap bitmapPisadera = BitmapFactory.decodeFile(mPath);
                        bitmapPisadera = foto.redimensiomarImagen(bitmapPisadera);

                        String imagenPisadera = foto.convertirImagenDano(bitmapPisadera);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Pisadera Lateral Izquierdo", 0, imagenPisadera, 0);
                        db.insertarValor(Integer.parseInt(id_inspeccion), 276, "Ok");
                        imagenPisadera = "data:image/jpg;base64,"+imagenPisadera;
                        String base64Image6 = imagenPisadera.split(",")[1];
                        byte[] decodedString6 = Base64.decode(base64Image6, Base64.DEFAULT);
                        Bitmap decodedByte6 = BitmapFactory.decodeByteArray(decodedString6, 0, decodedString6.length);
                        imagePisaderasLaIzE.setImageBitmap(decodedByte6);

                        servis = new Intent(lateralizquierdo.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Foto Pisadera Lateral Izquierdo");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        break;
                    /*case PHOTO_POLARIZADO:
                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });

                        Bitmap bitmapPolarizado = BitmapFactory.decodeFile(mPath);
                        bitmapPolarizado = foto.redimensiomarImagen(bitmapPolarizado);

                        String imagenPolarizado = foto.convertirImagenDano(bitmapPolarizado);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Polarizado Lateral Izquierdo", 0, imagenPolarizado, 0);
                        db.insertarValor(Integer.parseInt(id_inspeccion), 341, "Ok");
                        imagenPolarizado = "data:image/jpg;base64,"+imagenPolarizado;
                        String base64Image7 = imagenPolarizado.split(",")[1];
                        byte[] decodedString7 = Base64.decode(base64Image7, Base64.DEFAULT);
                        Bitmap decodedByte7 = BitmapFactory.decodeByteArray(decodedString7, 0, decodedString7.length);

                        imagePolarizadoLaIzE.setImageBitmap(decodedByte7);

                        servis = new Intent(lateralizquierdo.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Foto Polarizado Lateral Izquierdo");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);
                        break;*/
                }
            }
        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }
    }

    private  void DesplegarCamposSeccionUno(String id)    {

        if (btnLateIzE.getVisibility()==View.VISIBLE)
        {
            btnLateIzE.setVisibility(View.GONE);
            imageLateIzE.setVisibility(View.GONE);
            imageLateIzE.setImageBitmap(null);
            btnAdicionalIzE.setVisibility(View.GONE);
            imageAdicionalIzE.setVisibility(View.GONE);
            imageAdicionalIzE.setImageBitmap(null);
           // btnAdicionalIz2E.setVisibility(View.GONE);
            //imageAdicionalIz2E.setVisibility(View.GONE);
            //imageAdicionalIz2E.setImageBitmap(null);
            textCant16.setVisibility(View.GONE);
            contPost16.setVisibility(View.GONE);
            textCant17.setVisibility(View.GONE);
            contPost17.setVisibility(View.GONE);
            //textCant18.setVisibility(View.GONE);
            //contPost18.setVisibility(View.GONE);

        }
        else
        {

            //seccion dos

            txtPieza.setVisibility(View.GONE);
            txtTipoDanoE.setVisibility(View.GONE);
            txtDeducibleIzE.setVisibility(View.GONE);
            spinnerPiezaIzE.setVisibility(View.GONE);
            spinnerDanoIzE.setVisibility(View.GONE);
            spinnerDeducibleIzE.setVisibility(View.GONE);
            btnFotoDanoIzE.setVisibility(View.GONE);
            imagenIzDanoE.setVisibility(View.GONE);
            imagenIzDanoE.setImageBitmap(null);
            textCantLI.setVisibility(View.GONE);
            contPostLI.setVisibility(View.GONE);


            //seccion tres
            gabradoPatenteE.setVisibility(View.GONE);
            imageGabradoPatenteE.setVisibility(View.GONE);
            imageGabradoPatenteE.setImageBitmap(null);
            laminasSeguridadLaIzE.setVisibility(View.GONE);
            imagelaminasSeguridadLaIzE.setVisibility(View.GONE);
            imagelaminasSeguridadLaIzE.setImageBitmap(null);
            pisaderasLaIzE.setVisibility(View.GONE);
            imagePisaderasLaIzE.setVisibility(View.GONE);
            imagePisaderasLaIzE.setImageBitmap(null);
            polarizadoLaIzE.setVisibility(View.GONE);
            linea.setVisibility(View.GONE);
            //imagePolarizadoLaIzE.setVisibility(View.GONE);
            //imagePolarizadoLaIzE.setImageBitmap(null);


            String imageLateIz = db.foto(Integer.parseInt(id),"Foto Lateral Izquierdo");
            String imageAdicionalIz = db.foto(Integer.parseInt(id),"Foto Adicional 1 Lateral Izquierdo");
            String imageAdicionalIz2 = db.foto(Integer.parseInt(id),"Foto Adicional 2 Lateral Izquierdo");

            if(imageLateIz.length()>3)
            {
                byte[] decodedString = Base64.decode(imageLateIz, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageLateIzE.setImageBitmap(decodedByte);
            }
            if(imageAdicionalIz.length()>3)
            {
                byte[] decodedString = Base64.decode(imageAdicionalIz, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageAdicionalIzE.setImageBitmap(decodedByte);
            }
            /*if(imageAdicionalIz2.length()>3)
            {
                byte[] decodedString = Base64.decode(imageAdicionalIz2, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageAdicionalIz2E.setImageBitmap(decodedByte);
            }*/


            btnLateIzE.setVisibility(View.VISIBLE);
            imageLateIzE.setVisibility(View.VISIBLE);
            btnAdicionalIzE.setVisibility(View.VISIBLE);
            imageAdicionalIzE.setVisibility(View.VISIBLE);
            //btnAdicionalIz2E.setVisibility(View.VISIBLE);
            //imageAdicionalIz2E.setVisibility(View.VISIBLE);
            textCant16.setVisibility(View.VISIBLE);
            contPost16.setVisibility(View.VISIBLE);
            textCant17.setVisibility(View.VISIBLE);
            contPost17.setVisibility(View.VISIBLE);
            //textCant18.setVisibility(View.VISIBLE);
            //contPost18.setVisibility(View.VISIBLE);


        }
    }

    private  void desplegarCamposSeccionDos(String id)    {

        if (txtPieza.getVisibility() == View.VISIBLE) {

            txtPieza.setVisibility(View.GONE);
            txtTipoDanoE.setVisibility(View.GONE);
            txtDeducibleIzE.setVisibility(View.GONE);
            spinnerPiezaIzE.setVisibility(View.GONE);
            spinnerDanoIzE.setVisibility(View.GONE);
            spinnerDeducibleIzE.setVisibility(View.GONE);
            btnFotoDanoIzE.setVisibility(View.GONE);
            imagenIzDanoE.setVisibility(View.GONE);
            imagenIzDanoE.setImageBitmap(null);
            textCantLI.setVisibility(View.GONE);
            contPostLI.setVisibility(View.GONE);
        }
        else
        {
            //seccion uno
            btnLateIzE.setVisibility(View.GONE);
            imageLateIzE.setVisibility(View.GONE);
            imageLateIzE.setImageBitmap(null);
            btnAdicionalIzE.setVisibility(View.GONE);
            imageAdicionalIzE.setVisibility(View.GONE);
            imageAdicionalIzE.setImageBitmap(null);
            //btnAdicionalIz2E.setVisibility(View.GONE);
            //imageAdicionalIz2E.setVisibility(View.GONE);
            //imageAdicionalIz2E.setImageBitmap(null);
            textCant16.setVisibility(View.GONE);
            contPost16.setVisibility(View.GONE);
            textCant17.setVisibility(View.GONE);
            contPost17.setVisibility(View.GONE);
            //textCant18.setVisibility(View.GONE);
            //contPost18.setVisibility(View.GONE);

            //seccion tres
            gabradoPatenteE.setVisibility(View.GONE);
            imageGabradoPatenteE.setVisibility(View.GONE);
            imageGabradoPatenteE.setImageBitmap(null);
            laminasSeguridadLaIzE.setVisibility(View.GONE);
            imagelaminasSeguridadLaIzE.setVisibility(View.GONE);
            imagelaminasSeguridadLaIzE.setImageBitmap(null);
            pisaderasLaIzE.setVisibility(View.GONE);
            imagePisaderasLaIzE.setVisibility(View.GONE);
            imagePisaderasLaIzE.setImageBitmap(null);
            polarizadoLaIzE.setVisibility(View.GONE);
            linea.setVisibility(View.GONE);
           // imagePolarizadoLaIzE.setVisibility(View.GONE);
            //imagePolarizadoLaIzE.setImageBitmap(null);

            String imagenIzDano = db.foto(Integer.parseInt(id),db.comentarioFoto(Integer.parseInt(id),"lateral_izquierdo"));

            if(imagenIzDano.length()>3)
            {
                byte[] decodedString = Base64.decode(imagenIzDano, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imagenIzDanoE.setImageBitmap(decodedByte);
            }

            ///////SPINNERPIEZA POSTERIOR
            txtPieza.setVisibility(View.VISIBLE);
            spinnerPiezaIzE.setVisibility(View.VISIBLE);
            spinnerPiezaIzE = (Spinner)findViewById(R.id.spinnerPiezaIzE);
            String listapieza[][] =  db.listaPiezasLateralIzquierdo();

            String[] listapiezaLateralIzquierdo = new String[listapieza.length+1];
            listapiezaLateralIzquierdo[0] = "Seleccionar Pieza";
            for(int i=0;i<listapieza.length;i++){
                listapiezaLateralIzquierdo[i+1] = listapieza[i][0];
            }
            ArrayAdapter<String> adapterLateralIzquierdo = new ArrayAdapter<String>(lateralizquierdo.this,android.R.layout.simple_spinner_item,listapiezaLateralIzquierdo);
            adapterLateralIzquierdo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerPiezaIzE.setAdapter(adapterLateralIzquierdo);

            spinnerPiezaIzE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ///////SPINNERDAÑO POSTERIOR
                    if(position!=0) {
                        txtTipoDanoE.setVisibility(View.VISIBLE);
                        spinnerDanoIzE.setVisibility(View.VISIBLE);
                        spinnerDanoIzE = (Spinner) findViewById(R.id.spinnerDanoIzE);
                        String listaDano[][] = db.listaDanoPosterior();
                        final String[] listaDanoPosterior = new String[listaDano.length + 1];
                        listaDanoPosterior[0] = "Seleccionar Daño";
                        for (int i = 0; i < listaDano.length; i++) {
                            listaDanoPosterior[i + 1] = listaDano[i][0];
                        }
                        ArrayAdapter<String> adapterDanoPosterior = new ArrayAdapter<String>(lateralizquierdo.this, android.R.layout.simple_spinner_item, listaDanoPosterior);
                        adapterDanoPosterior.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerDanoIzE.setAdapter(adapterDanoPosterior);
                        spinnerDanoIzE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if(i!=0) {
                                    txtDeducibleIzE.setVisibility(View.VISIBLE);
                                    spinnerDeducibleIzE.setVisibility(View.VISIBLE);
                                    String[][] listadedu = db.listaDeduciblesPosterior(spinnerDanoIzE.getSelectedItem().toString());
                                    Spinner spinnerDeducibleIzE = (Spinner) findViewById(R.id.spinnerDeducibleIzE);
                                    String[] spinnerDedu = new String[listadedu.length + 1];
                                    spinnerDedu[0] = "Seleccionar Deducible";
                                    for (int ii = 0; ii < listadedu.length; ii++) {
                                        spinnerDedu[ii + 1] = listadedu[ii][0];
                                    }
                                    ArrayAdapter<String> adapterdedu = new ArrayAdapter<String>(lateralizquierdo.this, android.R.layout.simple_spinner_item, spinnerDedu);

                                    adapterdedu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinnerDeducibleIzE.setAdapter(adapterdedu);
                                    spinnerDeducibleIzE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            if(position!=0){
                                                btnFotoDanoIzE.setVisibility(View.VISIBLE);
                                                textCantLI.setVisibility(View.VISIBLE);
                                                contPostLI.setVisibility(View.VISIBLE);
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            imagenIzDanoE.setVisibility(View.VISIBLE);

        }
    }

    private void desplegarCamposSeccionTres(String id)    {
        if (gabradoPatenteE.getVisibility() == View.VISIBLE) {

            gabradoPatenteE.setVisibility(View.GONE);
            imageGabradoPatenteE.setVisibility(View.GONE);
            imageGabradoPatenteE.setImageBitmap(null);
            laminasSeguridadLaIzE.setVisibility(View.GONE);
            imagelaminasSeguridadLaIzE.setVisibility(View.GONE);
            imagelaminasSeguridadLaIzE.setImageBitmap(null);
            pisaderasLaIzE.setVisibility(View.GONE);
            imagePisaderasLaIzE.setVisibility(View.GONE);
            imagePisaderasLaIzE.setImageBitmap(null);
            polarizadoLaIzE.setVisibility(View.GONE);
            linea.setVisibility(View.GONE);
            //imagePolarizadoLaIzE.setVisibility(View.GONE);
            //imagePolarizadoLaIzE.setImageBitmap(null);

        }
        else
        {
            //seccion uno
            btnLateIzE.setVisibility(View.GONE);
            imageLateIzE.setVisibility(View.GONE);
            imageLateIzE.setImageBitmap(null);
            btnAdicionalIzE.setVisibility(View.GONE);
            imageAdicionalIzE.setVisibility(View.GONE);
            imageAdicionalIzE.setImageBitmap(null);
            //btnAdicionalIz2E.setVisibility(View.GONE);
            //imageAdicionalIz2E.setVisibility(View.GONE);
            //imageAdicionalIz2E.setImageBitmap(null);
            textCant16.setVisibility(View.GONE);
            contPost16.setVisibility(View.GONE);
            textCant17.setVisibility(View.GONE);
            contPost17.setVisibility(View.GONE);
            //textCant18.setVisibility(View.GONE);
            //contPost18.setVisibility(View.GONE);

            //seccion dos

            txtPieza.setVisibility(View.GONE);
            txtTipoDanoE.setVisibility(View.GONE);
            txtDeducibleIzE.setVisibility(View.GONE);
            spinnerPiezaIzE.setVisibility(View.GONE);
            spinnerDanoIzE.setVisibility(View.GONE);
            spinnerDeducibleIzE.setVisibility(View.GONE);
            btnFotoDanoIzE.setVisibility(View.GONE);
            imagenIzDanoE.setVisibility(View.GONE);
            imagenIzDanoE.setImageBitmap(null);
            textCantLI.setVisibility(View.GONE);
            contPostLI.setVisibility(View.GONE);



            String imageGabradoPatente = db.foto(Integer.parseInt(id),"Foto Grabado Patente Lateral Izquierdo");
            String imagelaminasSeguridadLaIz = db.foto(Integer.parseInt(id),"Foto Lamina de Seguridad Lateral Izquierdo");
            String imagePisaderasLaIz = db.foto(Integer.parseInt(id),"Foto Pisadera Lateral Izquierdo");
            String imagePolarizadoLaIz = db.foto(Integer.parseInt(id),"Foto Polarizado Lateral Izquierdo");

            if(imageGabradoPatente.length()>3)
            {
                byte[] decodedString = Base64.decode(imageGabradoPatente, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageGabradoPatenteE.setImageBitmap(decodedByte);
                gabradoPatenteE.setChecked(true);
            }
            if(imagelaminasSeguridadLaIz.length()>3)
            {
                byte[] decodedString = Base64.decode(imagelaminasSeguridadLaIz, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imagelaminasSeguridadLaIzE.setImageBitmap(decodedByte);
                laminasSeguridadLaIzE.setChecked(true);
            }
            if(imagePisaderasLaIz.length()>3)
            {
                byte[] decodedString = Base64.decode(imagePisaderasLaIz, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imagePisaderasLaIzE.setImageBitmap(decodedByte);
                pisaderasLaIzE.setChecked(true);
            }
           /* if(imagePolarizadoLaIz.length()>3)
            {
                byte[] decodedString = Base64.decode(imagePolarizadoLaIz, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imagePolarizadoLaIzE.setImageBitmap(decodedByte);
                polarizadoLaIzE.setChecked(true);
            }*/

            gabradoPatenteE.setVisibility(View.VISIBLE);
            imageGabradoPatenteE.setVisibility(View.VISIBLE);
            laminasSeguridadLaIzE.setVisibility(View.VISIBLE);
            imagelaminasSeguridadLaIzE.setVisibility(View.VISIBLE);
            pisaderasLaIzE.setVisibility(View.VISIBLE);
            imagePisaderasLaIzE.setVisibility(View.VISIBLE);
            polarizadoLaIzE.setVisibility(View.VISIBLE);
            linea.setVisibility(View.VISIBLE);
            //imagePolarizadoLaIzE.setVisibility(View.VISIBLE);



        }

    }
}
