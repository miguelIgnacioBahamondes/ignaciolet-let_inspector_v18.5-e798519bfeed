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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.BuildConfig;
import com.letchile.let.Clases.PropiedadesFoto;
import com.letchile.let.R;
import com.letchile.let.Servicios.ConexionInternet;
import com.letchile.let.Servicios.TransferirFoto;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class lateralderecho extends AppCompatActivity {
    DBprovider db;
    Boolean connec = false;

    private final int PHOTO_CODE = 200;
    private final int PHOTO_ADICIONAL = 300;
    private final int PHOTO_ADICIONAL_DOS = 400;
    private final int PHOTO_DANO = 500;
    private Button btnVolverLdE, btnVolverSecldE, btnSiguienteLdE, btnLateDerechoE, btnAdicionalUnoE,btnAdicionalDosE,btnFotoDanoLateE,btnSeccionLateralDerechoE,btnSeccionDosLateralDerechoE;
    private Spinner spinnerDanoDeE, spinnerPiezaDeE,spinnerDeducibleDeE;
    private TextView txtPieza,txtTipoDanoE,txtDeducibleE,textCant10,contPost10, textCant11,contPost11, textCant12,contPost12, textCantLD,contPostLD;
    private ImageView imagenLateDerechoE,imageAdicionalUnoLateE,imageAdicionalDosLateE,imagenLaDeDanoE;
    private String mPath;
    private File ruta_sd;
    private String ruta = "";
    String nombreimagen = "",comentarioDañoImg="";
    PropiedadesFoto foto;
    int correlativo = 0;
    String dañosDedu[][];


    public lateralderecho(){db = new DBprovider(this);foto=new PropiedadesFoto(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lateralderecho);

        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion = bundle.getString("id_inspeccion");

        connec = new ConexionInternet(this).isConnectingToInternet();


        btnLateDerechoE = findViewById(R.id.btnLateDerechoE);
        imagenLateDerechoE = findViewById(R.id.imagenLateDerechoE);
        btnAdicionalUnoE = findViewById(R.id.btnAdicionalUnoE);
        imageAdicionalUnoLateE = findViewById(R.id.imageAdicionalUnoLateE);
        //btnAdicionalDosE = findViewById(R.id.btnAdicionalDosE);
        //imageAdicionalDosLateE = findViewById(R.id.imageAdicionalDosLateE);
        btnFotoDanoLateE = findViewById(R.id.btnFotoDanoLateE);
        imagenLaDeDanoE = findViewById(R.id.imagenLaDeDanoE);
        btnSeccionLateralDerechoE = findViewById(R.id.btnSeccionLateralDerechoE);
        btnSeccionDosLateralDerechoE = findViewById(R.id.btnSeccionDosLateralDerechoE);
        txtPieza = findViewById(R.id.txtPieza);
        txtTipoDanoE = findViewById(R.id.txtTipoDanoE);
        txtDeducibleE = findViewById(R.id.txtDeducibleE);
        spinnerDeducibleDeE = findViewById(R.id.spinnerDeducibleDeEe);
        spinnerPiezaDeE = findViewById(R.id.spinnerPiezaDeE);
        spinnerDanoDeE  = findViewById(R.id.spinnerDanoDeE);

        textCant10 = findViewById(R.id.textCant10);
        contPost10 = findViewById(R.id.contPost10);
        contPost10.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Lateral Derecho")));

        textCant11 = findViewById(R.id.textCant11);
        contPost11 = findViewById(R.id.contPost11);
        contPost11.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Adicional 1 Lateral Derecho")));

       /* textCant12 = findViewById(R.id.textCant12);
        contPost12 = findViewById(R.id.contPost12);
        contPost12.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Adicional 2 Lateral Derecho")));*/

        textCantLD = findViewById(R.id.textCantLD);
        contPostLD = findViewById(R.id.contPostLD);
        contPostLD.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Danio Lateral Derecho")));


        btnSeccionLateralDerechoE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {desplegarCamposSeccionUno(id_inspeccion); }
        });
        btnSeccionDosLateralDerechoE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {desplegarCamposSeccionDos(id_inspeccion); }
        });

        //botones Abren Camara
        btnLateDerechoE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {funcionCamara(id_inspeccion,"_Foto_Lateral_Derecho.jpg",PHOTO_CODE);}
        });
        btnAdicionalUnoE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {funcionCamara(id_inspeccion,"_Foto_Lateral_Derecho_Adicional.jpg",PHOTO_ADICIONAL);}
        });
       /* btnAdicionalDosE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {funcionCamara(id_inspeccion,"_Foto_Lateral_Derecho_Adicional_Dos.jpg",PHOTO_ADICIONAL_DOS);}
        });*/
        btnFotoDanoLateE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {funcionCamara(id_inspeccion,"_Foto_Lateral_Derecho_Dano.jpg",PHOTO_DANO);}
        });


        btnVolverLdE = findViewById(R.id.btnVolverLdE);
        btnVolverLdE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inte = new Intent(lateralderecho.this,Posterior.class);
                inte.putExtra("id_inspeccion",id_inspeccion);
                startActivity(inte);
                finish();
            }
        });

        btnSiguienteLdE =  findViewById(R.id.btnSiguienteLdE);
        btnSiguienteLdE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String imagenLateDerecho = db.foto(Integer.parseInt(id_inspeccion),"Foto Lateral Derecho");
                //String imageAdicionalUnoLate = db.foto(Integer.parseInt(id_inspeccion),10);


                if(imagenLateDerecho.length()<4 )
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(lateralderecho.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe Tomar Fotos Obligatorias</b><p><ul><li>- Foto Lateral Derecho</li></ul></p>"));
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    //Toast toast =  Toast.makeText(lateralderecho.this, "Debe Tomar Fotos Obligatorias", Toast.LENGTH_SHORT);
                    //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    //toast.show();
                }
                else {
                    Intent intent = new Intent(lateralderecho.this, frontal.class);
                    intent.putExtra("id_inspeccion",id_inspeccion);
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
            intent.putExtra(MediaStore.EXTRA_OUTPUT,  FileProvider.getUriForFile(lateralderecho.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            startActivityForResult(intent, CodigoFoto);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion=bundle.getString("id_inspeccion");

        try {
            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    case PHOTO_CODE:
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
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Lateral Derecho", 0, imagen, 0);
                        imagen = "data:image/jpg;base64,"+imagen;
                        String base64Image22 = imagen.split(",")[1];
                        byte[] decodedString22 = Base64.decode(base64Image22, Base64.DEFAULT);
                        Bitmap decodedByte22 = BitmapFactory.decodeByteArray(decodedString22, 0, decodedString22.length);
                        imagenLateDerechoE.setImageBitmap(decodedByte22);

                        Intent servis = new Intent(lateralderecho.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Foto Lateral Derecho");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        int cantFoto10=db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Lateral Derecho");
                        cantFoto10= cantFoto10 +1;
                        db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Foto Lateral Derecho",cantFoto10);
                        contPost10.setText(String.valueOf(cantFoto10));
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
                        String imagenAdcional = foto.convertirImagenDano(bitmapAdcional);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Adicional Uno Lateral Derecho", 0, imagenAdcional, 0);
                        imagenAdcional = "data:image/jpg;base64,"+imagenAdcional;
                        String base64Image1 = imagenAdcional.split(",")[1];
                        byte[] decodedString1 = Base64.decode(base64Image1, Base64.DEFAULT);
                        Bitmap decodedByte1 = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.length);
                        imageAdicionalUnoLateE.setImageBitmap(decodedByte1);


                        servis = new Intent(lateralderecho.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Adicional Uno Lateral Derecho");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        int cantFoto11=db.cantidadF(Integer.parseInt(id_inspeccion),"Adicional 1 Lateral Derecho");
                        cantFoto11= cantFoto11 +1;
                        db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Adicional 1 Lateral Derecho",cantFoto11);
                        contPost11.setText(String.valueOf(cantFoto11));


                        break;
                   /* case PHOTO_ADICIONAL_DOS:
                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });


                        Bitmap bitmapAdcionalDos = BitmapFactory.decodeFile(mPath);
                        bitmapAdcionalDos = foto.redimensiomarImagen(bitmapAdcionalDos);

                        String imagenAdicionalDos = foto.convertirImagenDano(bitmapAdcionalDos);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Adicional Dos Lateral Derecho", 0, imagenAdicionalDos, 0);
                        imagenAdicionalDos = "data:image/jpg;base64,"+imagenAdicionalDos;
                        String base64Image2 = imagenAdicionalDos.split(",")[1];
                        byte[] decodedString2 = Base64.decode(base64Image2, Base64.DEFAULT);
                        Bitmap decodedByte2 = BitmapFactory.decodeByteArray(decodedString2, 0, decodedString2.length);
                        imageAdicionalDosLateE.setImageBitmap(decodedByte2);

                        servis = new Intent(lateralderecho.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Adicional Dos Lateral Derecho");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);


                        int cantFoto12=db.cantidadF(Integer.parseInt(id_inspeccion),"Adicional 2 Lateral Derecho");
                        cantFoto12= cantFoto12 +1;
                        db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Adicional 2 Lateral Derecho",cantFoto12);
                        contPost12.setText(String.valueOf(cantFoto12));

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



                        comentarioDañoImg = spinnerPiezaDeE.getSelectedItem().toString() + ' ' + spinnerDanoDeE.getSelectedItem().toString() + ' ' + spinnerDeducibleDeE.getSelectedItem().toString() + ' ';
                        db.insertarComentarioFoto(Integer.parseInt(id_inspeccion), comentarioDañoImg, "lateral_derecho");
                        String comentarito = db.comentarioFoto(Integer.parseInt(id_inspeccion), "lateral_derecho");

                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, comentarito, 0, imagenDano, 0);

                        dañosDedu = db.DeduciblePieza(spinnerPiezaDeE.getSelectedItem().toString(), "lateral_derecho");
                        //daño
                        db.insertarValor(Integer.parseInt(id_inspeccion), Integer.parseInt(dañosDedu[0][0]), String.valueOf(db.obtenerDanio(spinnerDanoDeE.getSelectedItem().toString())));

                        //deducible
                        db.insertarValor(Integer.parseInt(id_inspeccion), Integer.parseInt(dañosDedu[0][1]), db.obtenerDeducible(db.obtenerDanio(spinnerDanoDeE.getSelectedItem().toString()), spinnerDeducibleDeE.getSelectedItem().toString()));




                        imagenDano = "data:image/jpg;base64,"+imagenDano;
                        String base64Image3 = imagenDano.split(",")[1];
                        byte[] decodedString3 = Base64.decode(base64Image3, Base64.DEFAULT);
                        Bitmap decodedByte3 = BitmapFactory.decodeByteArray(decodedString3, 0, decodedString3.length);
                        imagenLaDeDanoE.setImageBitmap(decodedByte3);

                        servis = new Intent(lateralderecho.this, TransferirFoto.class);
                        servis.putExtra("comentario", comentarito);
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        int cantFotoLD=db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Danio Lateral Derecho");
                        cantFotoLD= cantFotoLD +1;
                        db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Foto Danio Lateral Derecho",cantFotoLD);
                        contPostLD.setText(String.valueOf(cantFotoLD));
                        //danioPo=db.Deducible(spinnerDeduciblePoE.getSelectedItem().toString());

                        break;


                }

            }
        }catch (Exception e){
            Log.e("Error Lder",e.getMessage());
        }
    }

    public void desplegarCamposSeccionUno(String id)    {
        if (btnLateDerechoE.getVisibility()==View.VISIBLE)
        {
            btnLateDerechoE.setVisibility(View.GONE);
            imagenLateDerechoE.setVisibility(View.GONE);
            btnAdicionalUnoE.setVisibility(View.GONE);
            imageAdicionalUnoLateE.setVisibility(View.GONE);
            //btnAdicionalDosE.setVisibility(View.GONE);
            //imageAdicionalDosLateE.setVisibility(View.GONE);
            textCant10.setVisibility(View.GONE);
            contPost10.setVisibility(View.GONE);
            textCant11.setVisibility(View.GONE);
            contPost11.setVisibility(View.GONE);
            //textCant12.setVisibility(View.GONE);
            //contPost12.setVisibility(View.GONE);

        }
        else
        {
            txtPieza.setVisibility(View.GONE);
            spinnerPiezaDeE.setVisibility(View.GONE);
            txtTipoDanoE.setVisibility(View.GONE);
            spinnerDanoDeE.setVisibility(View.GONE);
            txtDeducibleE.setVisibility(View.GONE);
            spinnerDeducibleDeE.setVisibility(View.GONE);
            btnFotoDanoLateE.setVisibility(View.GONE);
            imagenLaDeDanoE.setVisibility(View.GONE);
            imagenLaDeDanoE.setImageBitmap(null);
            textCantLD.setVisibility(View.GONE);
            contPostLD.setVisibility(View.GONE);


            String imagenLateDerecho = db.foto(Integer.parseInt(id),"Foto Lateral Derecho");
            String imageAdicionalUnoLate = db.foto(Integer.parseInt(id),"Adicional Uno Lateral Derecho");
            //String imageAdicionalDosLate = db.foto(Integer.parseInt(id),"Adicional Dos Lateral Derecho");

            if(imagenLateDerecho.length()>=3 )
            {
                // Bitmap bitmap = BitmapFactory.decodeFile(imagenDanoPosterior);
                //bitmap = redimensiomarImagen(bitmap,600,800);
                byte[] decodedString = Base64.decode(imagenLateDerecho, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imagenLateDerechoE.setImageBitmap(decodedByte);
            }

            if(imageAdicionalUnoLate.length()>=3 )
            {
                // Bitmap bitmap = BitmapFactory.decodeFile(imagenDanoPosterior);
                //bitmap = redimensiomarImagen(bitmap,600,800);
                byte[] decodedString = Base64.decode(imageAdicionalUnoLate, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageAdicionalUnoLateE.setImageBitmap(decodedByte);
            }

           /* if(imageAdicionalDosLate.length()>=3)
            {
                byte[] decodedString = Base64.decode(imageAdicionalDosLate, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageAdicionalDosLateE.setImageBitmap(decodedByte);
            }*/

            btnLateDerechoE.setVisibility(View.VISIBLE);
            imagenLateDerechoE.setVisibility(View.VISIBLE);
            btnAdicionalUnoE.setVisibility(View.VISIBLE);
            imageAdicionalUnoLateE.setVisibility(View.VISIBLE);
            //btnAdicionalDosE.setVisibility(View.VISIBLE);
            //imageAdicionalDosLateE.setVisibility(View.VISIBLE);
            textCant10.setVisibility(View.VISIBLE);
            contPost10.setVisibility(View.VISIBLE);
            textCant11.setVisibility(View.VISIBLE);
            contPost11.setVisibility(View.VISIBLE);
           // textCant12.setVisibility(View.VISIBLE);
            //contPost12.setVisibility(View.VISIBLE);

        }

    }
    public void desplegarCamposSeccionDos(String id)    {
        if (txtPieza.getVisibility()==View.VISIBLE)
        {
            txtPieza.setVisibility(View.GONE);
            spinnerPiezaDeE.setVisibility(View.GONE);
            txtTipoDanoE.setVisibility(View.GONE);
            spinnerDanoDeE.setVisibility(View.GONE);
            txtDeducibleE.setVisibility(View.GONE);
            spinnerDeducibleDeE.setVisibility(View.GONE);
            btnFotoDanoLateE.setVisibility(View.GONE);
            imagenLaDeDanoE.setVisibility(View.GONE);
            imagenLaDeDanoE.setImageBitmap(null);
            textCantLD.setVisibility(View.GONE);
            contPostLD.setVisibility(View.GONE);
        }
        else
        {

            btnLateDerechoE.setVisibility(View.GONE);
            imagenLateDerechoE.setVisibility(View.GONE);
            imagenLateDerechoE.setImageBitmap(null);
            btnAdicionalUnoE.setVisibility(View.GONE);
            imageAdicionalUnoLateE.setVisibility(View.GONE);
            imageAdicionalUnoLateE.setImageBitmap(null);
            //btnAdicionalDosE.setVisibility(View.GONE);
            //imageAdicionalDosLateE.setVisibility(View.GONE);
            //imageAdicionalDosLateE.setImageBitmap(null);
            textCant10.setVisibility(View.GONE);
            contPost10.setVisibility(View.GONE);
            textCant11.setVisibility(View.GONE);
            contPost11.setVisibility(View.GONE);
           // textCant12.setVisibility(View.GONE);
            //contPost12.setVisibility(View.GONE);



            String imagenLaDeDano = db.foto(Integer.parseInt(id),db.comentarioFoto(Integer.parseInt(id),"lateral_derecho"));

            if(imagenLaDeDano.length()>=3)
            {
                byte[] decodedString = Base64.decode(imagenLaDeDano, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imagenLaDeDanoE.setImageBitmap(decodedByte);
            }

            ///////SPINNERPIEZA LATERAL DERECHO
            spinnerPiezaDeE = (Spinner) findViewById(R.id.spinnerPiezaDeE);
            String listapieza[][] = db.listaPiezasLateralDerecho();
            String[] listapiezaLateralDerecho = new String[listapieza.length+1];
            listapiezaLateralDerecho[0] = "Seleccione";
            for (int i = 0; i < listapieza.length; i++) {
                listapiezaLateralDerecho[i+1] = listapieza[i][0];
            }
            ArrayAdapter<String> adapterLateralDerecho = new ArrayAdapter<String>(lateralderecho.this, android.R.layout.simple_spinner_item, listapiezaLateralDerecho);
            adapterLateralDerecho.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerPiezaDeE.setAdapter(adapterLateralDerecho);

            ///////SPINNERDAÑO POSTERIOR
            spinnerPiezaDeE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position != 0) {
                        ///////SPINNERDAÑO POSTERIOR
                        spinnerDanoDeE = (Spinner) findViewById(R.id.spinnerDanoDeE);
                        txtTipoDanoE.setVisibility(View.VISIBLE);
                        spinnerDanoDeE.setVisibility(View.VISIBLE);
                        String listaDano[][] = db.listaDanoPosterior();
                        String[] listaDanoPosterior = new String[listaDano.length+1];
                        listaDanoPosterior[0]="Seleccione";
                        for (int i = 0; i < listaDano.length; i++) {
                            listaDanoPosterior[i+1] = listaDano[i][0];
                        }
                        ArrayAdapter<String> adapterDanoPosterior = new ArrayAdapter<String>(lateralderecho.this, android.R.layout.simple_spinner_item, listaDanoPosterior);
                        adapterDanoPosterior.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerDanoDeE.setAdapter(adapterDanoPosterior);

                        spinnerDanoDeE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if(i !=0) { // 6 -> faltante
                                    String[][] listadedu = db.listaDeduciblesPosterior(spinnerDanoDeE.getSelectedItem().toString());
                                    Spinner spinnerDeducibleDeE = (Spinner) findViewById(R.id.spinnerDeducibleDeEe);
                                    spinnerDeducibleDeE.setVisibility(View.VISIBLE);
                                    String[] spinnerDedu = new String[listadedu.length+1];
                                    spinnerDedu[0]="Seleccione";
                                    for (int ii = 0; ii < listadedu.length; ii++) {
                                        spinnerDedu[ii+1] = listadedu[ii][0];
                                    }
                                    ArrayAdapter<String> adapterdedu = new ArrayAdapter<String>(lateralderecho.this, android.R.layout.simple_spinner_item, spinnerDedu);
                                    adapterdedu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinnerDeducibleDeE.setAdapter(adapterdedu);
                                    txtDeducibleE.setVisibility(View.VISIBLE);
                                    spinnerDeducibleDeE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            if (position != 0) {
                                                btnFotoDanoLateE.setVisibility(View.VISIBLE);
                                                textCantLD.setVisibility(View.VISIBLE);
                                                contPostLD.setVisibility(View.VISIBLE);
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
                public void onNothingSelected(AdapterView<?> parent) {}
            });




           /* spinnerDanoDeE = (Spinner) findViewById(R.id.spinnerDanoDeE);
                String listaDano[][] = db.listaDanoPosterior();

                final String[] listaDanoPosterior = new String[listaDano.length];
                listaDanoPosterior[0] = "Seleccionar Daño";
                for (int i = 1; i < listaDano.length; i++) {
                    listaDanoPosterior[i] = listaDano[i][0];
                }
                ArrayAdapter<String> adapterDanoPosterior = new ArrayAdapter<String>(lateralderecho.this, android.R.layout.simple_spinner_item, listaDanoPosterior);

                adapterDanoPosterior.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerDanoDeE.setAdapter(adapterDanoPosterior);

                spinnerDanoDeE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String[][] listadedu = db.listaDeduciblesPosterior(spinnerDanoDeE.getSelectedItem().toString());
                        Spinner spinnerDeducibleDeE = (Spinner) findViewById(R.id.spinnerDeducibleDeE);
                        String[] spinnerDedu = new String[listadedu.length];
                        spinnerDedu[0]="Seleccionar Deducible";
                        for (int ii = 0; ii < listadedu.length; ii++) {
                            spinnerDedu[ii] = listadedu[ii][0];
                        }
                        ArrayAdapter<String> adapterdedu = new ArrayAdapter<String>(lateralderecho.this, android.R.layout.simple_spinner_item, spinnerDedu);

                        adapterdedu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerDeducibleDeE.setAdapter(adapterdedu);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });*/
            txtPieza.setVisibility(View.VISIBLE);
            spinnerPiezaDeE.setVisibility(View.VISIBLE);
            imagenLaDeDanoE.setVisibility(View.VISIBLE);
        }

    }


}
