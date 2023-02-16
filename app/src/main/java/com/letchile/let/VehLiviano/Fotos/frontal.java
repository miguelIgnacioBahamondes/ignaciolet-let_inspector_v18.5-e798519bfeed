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

public class frontal extends AppCompatActivity {

    DBprovider db;
    Boolean connec = false;
    private final int PHOTO_FRONTAL = 200;
    private final int PHOTO_ADICIONAL = 400;
    private final int PHOTO_DANO = 500;
    private final int PHOTO_TURBO = 600;
    private final int PHOTO_HUENCHE = 700;
    private final int PHOTO_COCO = 800;
    private final int PHOTO_NEBLINERO = 900;
    private final int PHOTO_LOGO = 1000;
    private Button btnVolverFrontalE,btnVolerSecFrontalE,btnSiguienteFrontalE,btnFrontalE,btnAdicionalFrontalE,btnFrontalDanoE,btnSeccionUnoFrontalE,btnSeccionDosFrontalE,brnSeccionFos3E;
    private ImageView imageFrontalE,imageAdicionalFrontalE,imagenFrontalDanoE,imageTurboFrontalE,imageHuencheFrontalE,imageCocoFrE,imageNeblineroE,imageLogoE;
    private CheckBox sistemaTurboFrontalE,huincheFoE,CocoFoE,neblinerosFoE,logoFoE;
    private Spinner spinnerPiezaFrontalE,spinnerDanoFrontalE,spinnerDeducibleFrontalE;
    private TextView txtPieza,txtTipoDanoE,txtDeducibleE, textCant13,contPost13, textCant15,contPost15, textCantF,contPostF;
    private String mPath;
    private File ruta_sd;
    private String ruta = "";
    PropiedadesFoto foto;
    String nombreimagen = "",comentarioDañoImg="";
    Validaciones validaciones;
    int correlativo = 0;
    View linea;
    String dañosDedu[][];

    public frontal(){db = new DBprovider(this);foto=new PropiedadesFoto(this);validaciones = new Validaciones(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frontal);

        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion = bundle.getString("id_inspeccion");

        connec = new ConexionInternet(this).isConnectingToInternet();


        btnFrontalE = findViewById(R.id.btnFrontalE);
        imageFrontalE = findViewById(R.id.imageFrontalE);
        btnAdicionalFrontalE = findViewById(R.id.btnAdicionalFrontalE);
        imageAdicionalFrontalE = findViewById(R.id.imageAdicionalFrontalE);
        btnFrontalDanoE = findViewById(R.id.btnFrontalDanoE);
        imagenFrontalDanoE  = findViewById(R.id.imagenFrontalDanoE);
        sistemaTurboFrontalE = findViewById(R.id.sistemaTurboFrontalE);
        //imageTurboFrontalE = findViewById(R.id.imageTurboFrontalE);
        huincheFoE = findViewById(R.id.huincheFoE);
        imageHuencheFrontalE = findViewById(R.id.imageHuencheFrontalE);
        CocoFoE = findViewById(R.id.CocoFoE);
        imageCocoFrE = findViewById(R.id.imageCocoFrE);
        neblinerosFoE = findViewById(R.id.neblinerosFoE);
        imageNeblineroE = findViewById(R.id.imageNeblineroE);
        linea = findViewById(R.id.linea);
        logoFoE = findViewById(R.id.logoFoE);
        //imageLogoE = findViewById(R.id.imageLogoE);
        btnSeccionUnoFrontalE = findViewById(R.id.btnSeccionUnoFrontalE);
        btnSeccionDosFrontalE = findViewById(R.id.btnSeccionDosFrontalE);
        spinnerPiezaFrontalE = findViewById(R.id.spinnerPiezaFrontalE);
        spinnerDanoFrontalE = findViewById(R.id.spinnerDanoFrontalE);
        spinnerDeducibleFrontalE = findViewById(R.id.spinnerDeducibleFrontalE);
        txtPieza = findViewById(R.id.txtPieza);
        txtTipoDanoE = findViewById(R.id.txtTipoDanoE);
        txtDeducibleE = findViewById(R.id.txtDeducibleE);
        brnSeccionFos3E = findViewById(R.id.brnSeccionFos3E);

        textCant13 = findViewById(R.id.textCant13);
        contPost13 = findViewById(R.id.contPost13);
        contPost13.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Frontal")));


        textCant15 = findViewById(R.id.textCant15);
        contPost15 = findViewById(R.id.contPost15);
        contPost15.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Adicional Frontal")));

        textCantF = findViewById(R.id.textCantF);
        contPostF = findViewById(R.id.contPostF);
        contPostF.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Danio Frontal")));


        btnSeccionUnoFrontalE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {DesplegarCamposSeccionUno(id_inspeccion); }
        });
        btnSeccionDosFrontalE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {desplegarCamposSeccionDos(id_inspeccion); }
        });
        brnSeccionFos3E.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {desplegarCamposSeccionTres(id_inspeccion);
            }
        });


        btnFrontalE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcionCamara(id_inspeccion,"_Foto_Frontal.jpg",PHOTO_FRONTAL);
            }
        });
        btnAdicionalFrontalE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                funcionCamara(id_inspeccion,"_Foto_Adicional_Frontal.jpg",PHOTO_ADICIONAL);

            }
        }
        );
        btnFrontalDanoE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcionCamara(id_inspeccion,"_Foto_Dano_Frontal.jpg",PHOTO_DANO);
            }
        });


        sistemaTurboFrontalE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),354));
        sistemaTurboFrontalE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 354).toString().equals("Ok")) {
                        //funcionCamara(id_inspeccion,"_Foto_Sistema_Turbo.jpg",PHOTO_TURBO);
                        //openCamaraSistemaTurbo(id_inspeccion);
                        db.insertarValor(Integer.parseInt(id_inspeccion),354,"Ok");
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),354,"");
                   // imageTurboFrontalE.setImageBitmap(null);
                }
            }
        });

        huincheFoE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),288));
        huincheFoE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 288).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_Huenche_Frontal.jpg",PHOTO_HUENCHE);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),288,"");
                    imageHuencheFrontalE.setImageBitmap(null);
                }
            }
        });

        //NO ES COCO ES SENSORES DE IMPACTO!
        CocoFoE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),316));
        CocoFoE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 316).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_SensoresImpacto_Frontal.jpg",PHOTO_COCO);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),316,"");
                    imageCocoFrE.setImageBitmap(null);
                }
            }
        });

        neblinerosFoE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),286));
        neblinerosFoE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 286).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_Neblinero_Frontal.jpg",PHOTO_NEBLINERO);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),286,"");
                    imageNeblineroE.setImageBitmap(null);
                }
            }
        });

        logoFoE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),260));
        logoFoE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 260).toString().equals("Ok")) {
                        //funcionCamara(id_inspeccion,"_Foto_Logo_Frontal.jpg",PHOTO_LOGO);
                        db.insertarValor(Integer.parseInt(id_inspeccion),260,"Ok");
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),260,"");
                    //imageLogoE.setImageBitmap(null);
                }

            }
        });

        btnVolverFrontalE = (Button)findViewById(R.id.btnVolverFrontalE);
        btnVolverFrontalE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(frontal.this,lateralderecho.class);
                inte.putExtra("id_inspeccion",id_inspeccion);
                startActivity(inte);
                finish();
            }
        });
        btnSiguienteFrontalE = (Button)findViewById(R.id.btnSiguienteFrontalE);
        btnSiguienteFrontalE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String imageFrontal = db.foto(Integer.parseInt(id_inspeccion),"Foto Frontal");


                if(imageFrontal.length()<4  )
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(frontal.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe Tomar Fotos Obligatorias</b><p><ul><li>- Foto Frontal</li><p><li>- Foto Logo Parabrisas</li></p></ul></p>"));
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else if(imageFrontal.length()<4 )
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(frontal.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe Tomar Fotos Obligatorias</b><p><ul><li>- Foto Frontal</li></ul></p>"));
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else {
                    Intent intent = new Intent(frontal.this, lateralizquierdo.class);
                    intent.putExtra("id_inspeccion",id_inspeccion);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }


    public void funcionCamara(String id,String nombre_foto,int CodigoFoto) {
        String id_inspeccion = id;
        ruta_sd = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString() + '/' + id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated) {

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion) + "_" + String.valueOf(correlativo) + nombre_foto;

            ruta = file.toString() + "/" + nombreimagen;
            mPath = ruta;

            File newFile = new File(mPath);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(frontal.this,
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
                    case PHOTO_FRONTAL:
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
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Frontal", 0, imagen, 0);
                        imagen = "data:image/jpg;base64,"+imagen;
                        String base64Image22 = imagen.split(",")[1];
                        byte[] decodedString22 = Base64.decode(base64Image22, Base64.DEFAULT);
                        Bitmap decodedByte22 = BitmapFactory.decodeByteArray(decodedString22, 0, decodedString22.length);
                        imageFrontalE.setImageBitmap(decodedByte22);

                        Intent servis = new Intent(frontal.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Foto Frontal");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        int cantFoto13=db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Frontal");
                        cantFoto13= cantFoto13 +1;
                        db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Foto Frontal",cantFoto13);
                        contPost13.setText(String.valueOf(cantFoto13));

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


                        Bitmap bitmapAdcional = BitmapFactory.decodeFile(mPath);
                        bitmapAdcional = foto.redimensiomarImagen(bitmapAdcional);

                        String imagenAdicional = foto.convertirImagenDano(bitmapAdcional);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Adicional Frontal", 0, imagenAdicional, 0);
                        imagenAdicional = "data:image/jpg;base64,"+imagenAdicional;
                        String base64Image2 = imagenAdicional.split(",")[1];
                        byte[] decodedString2 = Base64.decode(base64Image2, Base64.DEFAULT);
                        Bitmap decodedByte2 = BitmapFactory.decodeByteArray(decodedString2, 0, decodedString2.length);
                        imageAdicionalFrontalE.setImageBitmap(decodedByte2);

                        servis = new Intent(frontal.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Adicional Frontal");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        int cantFoto15=db.cantidadF(Integer.parseInt(id_inspeccion),"Adicional Frontal");
                        cantFoto15= cantFoto15 +1;
                        db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Adicional Frontal",cantFoto15);
                        contPost15.setText(String.valueOf(cantFoto15));

                        break;
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

                        String imagendano = foto.convertirImagenDano(bitmapDano);


                        comentarioDañoImg = spinnerPiezaFrontalE.getSelectedItem().toString() + ' ' + spinnerDanoFrontalE.getSelectedItem().toString() + ' ' + spinnerDeducibleFrontalE.getSelectedItem().toString() + ' ';
                        db.insertarComentarioFoto(Integer.parseInt(id_inspeccion), comentarioDañoImg, "frontal");
                        String comentarito = db.comentarioFoto(Integer.parseInt(id_inspeccion), "frontal");

                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, comentarito, 0, imagendano, 0);


                        dañosDedu = db.DeduciblePieza(spinnerPiezaFrontalE.getSelectedItem().toString(), "frontal");
                        //danioPo=db.Deducible(spinnerDeduciblePoE.getSelectedItem().toString());

                        //daño
                        db.insertarValor(Integer.parseInt(id_inspeccion), Integer.parseInt(dañosDedu[0][0]), String.valueOf(db.obtenerDanio(spinnerDanoFrontalE.getSelectedItem().toString())));


                        //deducible
                        db.insertarValor(Integer.parseInt(id_inspeccion), Integer.parseInt(dañosDedu[0][1]), db.obtenerDeducible(db.obtenerDanio(spinnerDanoFrontalE.getSelectedItem().toString()), spinnerDeducibleFrontalE.getSelectedItem().toString()));
                        //db.insertarValor(Integer.parseInt(id_inspeccion), Integer.parseInt(dañosDedu[0][1]), spinnerDeducibleFrontalE.getSelectedItem().toString());

                        imagendano = "data:image/jpg;base64,"+imagendano;
                        String base64Image33 = imagendano.split(",")[1];
                        byte[] decodedString33 = Base64.decode(base64Image33, Base64.DEFAULT);
                        Bitmap decodedByte33 = BitmapFactory.decodeByteArray(decodedString33, 0, decodedString33.length);
                        imagenFrontalDanoE.setImageBitmap(decodedByte33);

                        servis = new Intent(frontal.this, TransferirFoto.class);
                        servis.putExtra("comentario", comentarito);
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        int cantFotoF=db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Danio Frontal");
                        cantFotoF= cantFotoF +1;
                        db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Foto Danio Frontal",cantFotoF);
                        contPostF.setText(String.valueOf(cantFotoF));


                        break;
                    /*case PHOTO_TURBO:
                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });

                        Bitmap bitmaoTurbo = BitmapFactory.decodeFile(mPath);
                        bitmaoTurbo = foto.redimensiomarImagen(bitmaoTurbo);

                        String imagenTurbo = foto.convertirImagenDano(bitmaoTurbo);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Turbo Frontal", 0, imagenTurbo, 0);
                        db.insertarValor(Integer.parseInt(id_inspeccion), 354, "Ok");
                        imagenTurbo = "data:image/jpg;base64,"+imagenTurbo;
                        String base64Image4 = imagenTurbo.split(",")[1];
                        byte[] decodedString4 = Base64.decode(base64Image4, Base64.DEFAULT);
                        Bitmap decodedByte4 = BitmapFactory.decodeByteArray(decodedString4, 0, decodedString4.length);
                        imageTurboFrontalE.setImageBitmap(decodedByte4);

                        servis = new Intent(frontal.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Turbo Frontal");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        break;*/
                    case PHOTO_HUENCHE:
                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });

                        Bitmap bitmapHuenche = BitmapFactory.decodeFile(mPath);
                        bitmapHuenche = foto.redimensiomarImagen(bitmapHuenche);

                        String imagenHuenche = foto.convertirImagenDano(bitmapHuenche);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Huenche Frontal", 0, imagenHuenche, 0);
                        db.insertarValor(Integer.parseInt(id_inspeccion), 288, "Ok");
                        imagenHuenche = "data:image/jpg;base64,"+imagenHuenche;
                        String base64Image5 = imagenHuenche.split(",")[1];
                        byte[] decodedString5 = Base64.decode(base64Image5, Base64.DEFAULT);
                        Bitmap decodedByte5 = BitmapFactory.decodeByteArray(decodedString5, 0, decodedString5.length);
                        imageHuencheFrontalE.setImageBitmap(decodedByte5);

                        servis = new Intent(frontal.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Huenche Frontal");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        break;
                    case PHOTO_COCO:
                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });

                        Bitmap bitmapCoco = BitmapFactory.decodeFile(mPath);
                        bitmapCoco = foto.redimensiomarImagen(bitmapCoco);

                        String imagenCoco = foto.convertirImagenDano(bitmapCoco);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "sensoresImpacto Frontal", 0, imagenCoco, 0);
                        db.insertarValor(Integer.parseInt(id_inspeccion), 316, "Ok");
                        imagenCoco = "data:image/jpg;base64,"+imagenCoco;
                        String base64Image6 = imagenCoco.split(",")[1];
                        byte[] decodedString6 = Base64.decode(base64Image6, Base64.DEFAULT);
                        Bitmap decodedByte6 = BitmapFactory.decodeByteArray(decodedString6, 0, decodedString6.length);
                        imageCocoFrE.setImageBitmap(decodedByte6);

                        servis = new Intent(frontal.this, TransferirFoto.class);
                        servis.putExtra("comentario", "sensoresImpacto Frontal");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        break;

                    case PHOTO_NEBLINERO:
                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });

                        Bitmap bitmapNeblinero = BitmapFactory.decodeFile(mPath);
                        bitmapNeblinero = foto.redimensiomarImagen(bitmapNeblinero);

                        String imagenNeblinero = foto.convertirImagenDano(bitmapNeblinero);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Neblinero Frontal", 0, imagenNeblinero, 0);
                        db.insertarValor(Integer.parseInt(id_inspeccion), 286, "Ok");
                        imagenNeblinero = "data:image/jpg;base64,"+imagenNeblinero;
                        String base64Image7 = imagenNeblinero.split(",")[1];
                        byte[] decodedString7 = Base64.decode(base64Image7, Base64.DEFAULT);
                        Bitmap decodedByte7 = BitmapFactory.decodeByteArray(decodedString7, 0, decodedString7.length);

                        imageNeblineroE.setImageBitmap(decodedByte7);

                        servis = new Intent(frontal.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Neblinero Frontal");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        break;
                    /*case PHOTO_LOGO:
                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });

                        Bitmap bitmapLogo = BitmapFactory.decodeFile(mPath);
                        bitmapLogo = foto.redimensiomarImagen(bitmapLogo);

                        String imagenLogo = foto.convertirImagenDano(bitmapLogo);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Logo Frontal", 0, imagenLogo, 0);
                        db.insertarValor(Integer.parseInt(id_inspeccion), 260, "Ok");
                        imagenLogo = "data:image/jpg;base64,"+imagenLogo;
                        String base64Image8 = imagenLogo.split(",")[1];
                        byte[] decodedString8 = Base64.decode(base64Image8, Base64.DEFAULT);
                        Bitmap decodedByte8 = BitmapFactory.decodeByteArray(decodedString8, 0, decodedString8.length);
                        imageLogoE.setImageBitmap(decodedByte8);

                        servis = new Intent(frontal.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Logo Frontal");
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

        if (btnFrontalE.getVisibility()==View.VISIBLE)
        {
            btnFrontalE.setVisibility(View.GONE);
            imageFrontalE.setVisibility(View.GONE);
            imageFrontalE.setImageBitmap(null);
            btnAdicionalFrontalE.setVisibility(View.GONE);
            imageAdicionalFrontalE.setVisibility(View.GONE);
            imageAdicionalFrontalE.setImageBitmap(null);
            textCant13.setVisibility(View.GONE);
            contPost13.setVisibility(View.GONE);
            textCant15.setVisibility(View.GONE);
            contPost15.setVisibility(View.GONE);
            linea.setVisibility(View.GONE);

        }
        else
        {

            //seccion dos

            txtPieza.setVisibility(View.GONE);
            txtTipoDanoE.setVisibility(View.GONE);
            txtDeducibleE.setVisibility(View.GONE);
            spinnerPiezaFrontalE.setVisibility(View.GONE);
            spinnerDanoFrontalE.setVisibility(View.GONE);
            spinnerDeducibleFrontalE.setVisibility(View.GONE);
            btnFrontalDanoE.setVisibility(View.GONE);
            imagenFrontalDanoE.setVisibility(View.GONE);
            imagenFrontalDanoE.setImageBitmap(null);
            textCantF.setVisibility(View.GONE);
            contPostF.setVisibility(View.GONE);

            //seccion tres
            sistemaTurboFrontalE.setVisibility(View.GONE);
            //imageTurboFrontalE.setVisibility(View.GONE);
            //imageTurboFrontalE.setImageBitmap(null);
            huincheFoE.setVisibility(View.GONE);
            imageHuencheFrontalE.setVisibility(View.GONE);
            imageHuencheFrontalE.setImageBitmap(null);
            CocoFoE.setVisibility(View.GONE);
            imageCocoFrE.setVisibility(View.GONE);
            imageCocoFrE.setImageBitmap(null);
            neblinerosFoE.setVisibility(View.GONE);
            imageNeblineroE.setVisibility(View.GONE);
            imageNeblineroE.setImageBitmap(null);
            logoFoE.setVisibility(View.GONE);
            linea.setVisibility(View.GONE);
            //imageLogoE.setVisibility(View.GONE);
            //imageLogoE.setImageBitmap(null);


            String imageFrontal = db.foto(Integer.parseInt(id),"Foto Frontal");
            String imageAdicionalFrontal = db.foto(Integer.parseInt(id),"Adicional Frontal");

            if(imageFrontal.length()>=3 )
            {
                byte[] decodedString = Base64.decode(imageFrontal, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageFrontalE.setImageBitmap(decodedByte);
            }
            if(imageAdicionalFrontal.length()>=3 )
            {
                byte[] decodedString = Base64.decode(imageAdicionalFrontal, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageAdicionalFrontalE.setImageBitmap(decodedByte);
            }


            btnFrontalE.setVisibility(View.VISIBLE);
            imageFrontalE.setVisibility(View.VISIBLE);
            btnAdicionalFrontalE.setVisibility(View.VISIBLE);
            imageAdicionalFrontalE.setVisibility(View.VISIBLE);
            textCant13.setVisibility(View.VISIBLE);
            contPost13.setVisibility(View.VISIBLE);
            textCant15.setVisibility(View.VISIBLE);
            contPost15.setVisibility(View.VISIBLE);
            linea.setVisibility(View.VISIBLE);

        }
    }

    private  void desplegarCamposSeccionDos(String id)    {

        if (txtPieza.getVisibility() == View.VISIBLE) {

            txtPieza.setVisibility(View.GONE);
            txtTipoDanoE.setVisibility(View.GONE);
            txtDeducibleE.setVisibility(View.GONE);
            spinnerPiezaFrontalE.setVisibility(View.GONE);
            spinnerDanoFrontalE.setVisibility(View.GONE);
            spinnerDeducibleFrontalE.setVisibility(View.GONE);
            btnFrontalDanoE.setVisibility(View.GONE);
            imagenFrontalDanoE.setVisibility(View.GONE);
            textCantF.setVisibility(View.GONE);
            contPostF.setVisibility(View.GONE);
            imagenFrontalDanoE.setImageBitmap(null);
        }
        else
        {
            //seccion uno
            btnFrontalE.setVisibility(View.GONE);
            imageFrontalE.setVisibility(View.GONE);
            imageFrontalE.setImageBitmap(null);
            btnAdicionalFrontalE.setVisibility(View.GONE);
            imageAdicionalFrontalE.setVisibility(View.GONE);
            imageAdicionalFrontalE.setImageBitmap(null);
            textCant13.setVisibility(View.GONE);
            contPost13.setVisibility(View.GONE);
            textCant15.setVisibility(View.GONE);
            contPost15.setVisibility(View.GONE);
            linea.setVisibility(View.GONE);


            //seccion tres
            sistemaTurboFrontalE.setVisibility(View.GONE);
            //imageTurboFrontalE.setVisibility(View.GONE);
            //imageTurboFrontalE.setImageBitmap(null);
            huincheFoE.setVisibility(View.GONE);
            imageHuencheFrontalE.setVisibility(View.GONE);
            imageHuencheFrontalE.setImageBitmap(null);
            CocoFoE.setVisibility(View.GONE);
            imageCocoFrE.setVisibility(View.GONE);
            imageCocoFrE.setImageBitmap(null);
            neblinerosFoE.setVisibility(View.GONE);
            imageNeblineroE.setVisibility(View.GONE);
            imageNeblineroE.setImageBitmap(null);
            logoFoE.setVisibility(View.GONE);
            linea.setVisibility(View.GONE);
            //imageLogoE.setVisibility(View.GONE);
            //imageLogoE.setImageBitmap(null);

            String imagenFrontalDano = db.foto(Integer.parseInt(id),db.comentarioFoto(Integer.parseInt(id),"frontal"));

            if(imagenFrontalDano.length()>=3 )
            {
                byte[] decodedString = Base64.decode(imagenFrontalDano, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imagenFrontalDanoE.setImageBitmap(decodedByte);
            }

            ///////SPINNERPIEZA POSTERIOR
            txtPieza.setVisibility(View.VISIBLE);
            spinnerPiezaFrontalE.setVisibility(View.VISIBLE);
            spinnerPiezaFrontalE = (Spinner)findViewById(R.id.spinnerPiezaFrontalE);
            String listapieza[][] =  db.listaPiezasFrontal();
            String[] listapiezaFrontal = new String[listapieza.length+1];
            listapiezaFrontal[0] = "Seleccionar Pieza";
            for(int i=0;i<listapieza.length;i++){
                listapiezaFrontal[i+1] = listapieza[i][0];
            }
            ArrayAdapter<String> adapterFrontal = new ArrayAdapter<String>(frontal.this,android.R.layout.simple_spinner_item,listapiezaFrontal);
            adapterFrontal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerPiezaFrontalE.setAdapter(adapterFrontal);

            spinnerPiezaFrontalE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position !=0){
                        ///////SPINNERDAÑO POSTERIOR
                        txtTipoDanoE.setVisibility(View.VISIBLE);
                        spinnerDanoFrontalE.setVisibility(View.VISIBLE);
                        spinnerDanoFrontalE = (Spinner) findViewById(R.id.spinnerDanoFrontalE);
                        String listaDano[][] =  db.listaDanoPosterior();
                        final String[] listaDanoPosterior = new String[listaDano.length+1];
                        listaDanoPosterior[0] = "Seleccionar Daño";
                        for(int i=0;i<listaDano.length;i++){
                            listaDanoPosterior[i+1] = listaDano[i][0];
                        }
                        ArrayAdapter<String> adapterDanoPosterior = new ArrayAdapter<String>(frontal.this,android.R.layout.simple_spinner_item,listaDanoPosterior);
                        adapterDanoPosterior.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerDanoFrontalE.setAdapter(adapterDanoPosterior);

                        spinnerDanoFrontalE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if(i!=0){
                                    txtDeducibleE.setVisibility(View.VISIBLE);
                                spinnerDeducibleFrontalE.setVisibility(View.VISIBLE);
                                String[][] listadedu=db.listaDeduciblesPosterior(spinnerDanoFrontalE.getSelectedItem().toString());
                                Spinner spinnerDeducibleFrontalE = (Spinner)findViewById(R.id.spinnerDeducibleFrontalE);
                                String[] spinnerDedu = new String[listadedu.length+1];
                                spinnerDedu[0]= "Seleccione";
                                for(int ii=0;ii<listadedu.length;ii++){
                                    spinnerDedu[ii+1] = listadedu[ii][0];
                                }
                                ArrayAdapter<String> adapterdedu = new ArrayAdapter<String>(frontal.this,android.R.layout.simple_spinner_item,spinnerDedu);
                                adapterdedu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerDeducibleFrontalE.setAdapter(adapterdedu);
                                spinnerDeducibleFrontalE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        if(position!=0) {
                                            btnFrontalDanoE.setVisibility(View.VISIBLE);
                                            textCantF.setVisibility(View.VISIBLE);
                                            contPostF.setVisibility(View.VISIBLE);
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



            imagenFrontalDanoE.setVisibility(View.VISIBLE);



        }
    }

    private void desplegarCamposSeccionTres(String id)    {
        if (sistemaTurboFrontalE.getVisibility() == View.VISIBLE) {

            sistemaTurboFrontalE.setVisibility(View.GONE);
            //imageTurboFrontalE.setVisibility(View.GONE);
            //imageTurboFrontalE.setImageBitmap(null);
            huincheFoE.setVisibility(View.GONE);
            imageHuencheFrontalE.setVisibility(View.GONE);
            imageHuencheFrontalE.setImageBitmap(null);
            CocoFoE.setVisibility(View.GONE);
            imageCocoFrE.setVisibility(View.GONE);
            imageCocoFrE.setImageBitmap(null);
            neblinerosFoE.setVisibility(View.GONE);
            imageNeblineroE.setVisibility(View.GONE);
            imageNeblineroE.setImageBitmap(null);
            logoFoE.setVisibility(View.GONE);
            linea.setVisibility(View.GONE);
            //imageLogoE.setVisibility(View.GONE);
            //imageLogoE.setImageBitmap(null);
        }
        else
        {
            //seccion uno
            btnFrontalE.setVisibility(View.GONE);
            imageFrontalE.setVisibility(View.GONE);
            imageFrontalE.setImageBitmap(null);
            btnAdicionalFrontalE.setVisibility(View.GONE);
            imageAdicionalFrontalE.setVisibility(View.GONE);
            imageAdicionalFrontalE.setImageBitmap(null);
            textCant13.setVisibility(View.GONE);
            contPost13.setVisibility(View.GONE);
            textCant15.setVisibility(View.GONE);
            contPost15.setVisibility(View.GONE);
            linea.setVisibility(View.GONE);

            //seccion dos

            txtPieza.setVisibility(View.GONE);
            txtTipoDanoE.setVisibility(View.GONE);
            txtDeducibleE.setVisibility(View.GONE);
            spinnerPiezaFrontalE.setVisibility(View.GONE);
            spinnerDanoFrontalE.setVisibility(View.GONE);
            spinnerDeducibleFrontalE.setVisibility(View.GONE);
            btnFrontalDanoE.setVisibility(View.GONE);
            imagenFrontalDanoE.setVisibility(View.GONE);
            imagenFrontalDanoE.setImageBitmap(null);
            textCantF.setVisibility(View.GONE);
            contPostF.setVisibility(View.GONE);


            String imageTurboFrontal = db.foto(Integer.parseInt(id),"Turbo Frontal");
            String imageHuencheFrontal = db.foto(Integer.parseInt(id),"Huenche Frontal");
            String imageCocoFr = db.foto(Integer.parseInt(id),"sensoresImpacto Frontal");
            String imageNeblinero = db.foto(Integer.parseInt(id),"Neblinero Frontal");
            String imageLogo = db.foto(Integer.parseInt(id),"Logo Frontal");


            /*if(imageTurboFrontal.length()>=3 )
            {
                byte[] decodedString = Base64.decode(imageTurboFrontal, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageTurboFrontalE.setImageBitmap(decodedByte);
                sistemaTurboFrontalE.setChecked(true);
            }*/
            if(imageHuencheFrontal.length()>3)
            {
                byte[] decodedString = Base64.decode(imageHuencheFrontal, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageHuencheFrontalE.setImageBitmap(decodedByte);
                huincheFoE.setChecked(true);
            }
            if(imageCocoFr.length()>=3)
            {
                byte[] decodedString = Base64.decode(imageCocoFr, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageCocoFrE.setImageBitmap(decodedByte);
                CocoFoE.setChecked(true);
            }
            if(imageNeblinero.length()>=3)
            {
                byte[] decodedString = Base64.decode(imageNeblinero, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageNeblineroE.setImageBitmap(decodedByte);
                neblinerosFoE.setChecked(true);
            }
           /* if(imageLogo.length()>=3)
            {
                byte[] decodedString = Base64.decode(imageLogo, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageLogoE.setImageBitmap(decodedByte);
                logoFoE.setChecked(true);
            }*/

            sistemaTurboFrontalE.setVisibility(View.VISIBLE);
            //imageTurboFrontalE.setVisibility(View.VISIBLE);
            huincheFoE.setVisibility(View.VISIBLE);
            imageHuencheFrontalE.setVisibility(View.VISIBLE);
            CocoFoE.setVisibility(View.VISIBLE);
            imageCocoFrE.setVisibility(View.VISIBLE);
            neblinerosFoE.setVisibility(View.VISIBLE);
            imageNeblineroE.setVisibility(View.VISIBLE);
            logoFoE.setVisibility(View.VISIBLE);
            linea.setVisibility(View.VISIBLE);
            //imageLogoE.setVisibility(View.VISIBLE);


        }

    }
}
