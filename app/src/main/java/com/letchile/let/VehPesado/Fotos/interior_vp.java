package com.letchile.let.VehPesado.Fotos;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.BuildConfig;
import com.letchile.let.Clases.PropiedadesFoto;
import com.letchile.let.R;
import com.letchile.let.Servicios.TransferirFoto;
import com.letchile.let.VehPesado.DatosAsegVpActivity;
import com.letchile.let.VehPesado.SeccionVpActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class interior_vp extends AppCompatActivity {

    @BindView(R.id.btnPosteriorVP)Button btnFotoPosterior;
    @BindView(R.id.imageViewFotoPvpMQ)ImageView imagenPosteriorVp;
    @BindView(R.id.btnPosteriorVP2)Button btnFotoPosterior2;
    @BindView(R.id.imageViewFotoPvpMQ2)ImageView imagenPosteriorVp2;
    @BindView(R.id.btnPosteriorVP3)Button btnFotoPosterior3;
    @BindView(R.id.imageViewFotoPvpMQ3)ImageView imagenPosteriorVp3;

    @BindView(R.id.btnFotoAdiocionalPvpMQ)Button btnFotoAdicionalPosterior;
    @BindView(R.id.imageFotoAdicionalPvpMQ)ImageView imagenAdicionalPosteriorVp;
    @BindView(R.id.btnFotoAdiocionalPvpMQ2)Button btnFotoAdicionalPosterior2;
    @BindView(R.id.imageFotoAdicionalPvpMQ2)ImageView imagenAdicionalPosteriorVp2;

    @BindView(R.id.txtDanio)TextView txtDanioPvp;
    @BindView(R.id.DescripDanoPvpMQ)EditText edtDescripcionDañoPvp;
    @BindView(R.id.btnFotoDanoPvp)Button btnFotoDanoPvp;
    @BindView(R.id.imgFotoDanoPvp)ImageView imgFotoDanoPvp;

    private final int TAKE_POSTERIOR = 100,TAKE_POSTERIOR2=400,TAKE_POSTERIOR3=500, TAKE_ADDPOVP=200,TAKE_ADDPOVP2=600,TAKE_DANOPVP=300;
    String id_inspeccion,ruta,mPath,nombreimagen,tipoVeh;
    private File ruta_sd;
    int correlativo = 0;
    private TextView textCant16,contPost16, textCant17,contPost17,textCant18,contPost18, textCant19,contPost19,textCant20,contPost20, textCantInt,contPostInt;
    DBprovider db;
    PropiedadesFoto foto;
    Intent servis;


    public interior_vp(){
        db = new DBprovider(this);
        foto = new PropiedadesFoto(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interior_vp);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        id_inspeccion = bundle.getString("id_inspeccion");
        tipoVeh = bundle.getString("tipoVeh");

        textCant16 = findViewById(R.id.textCant16);
        contPost16 = findViewById(R.id.contPost16);
        contPost16.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Panel_Interior")));

        textCant17= findViewById(R.id.textCant17);
        contPost17 = findViewById(R.id.contPost17);
        contPost17.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Panel_Exterior")));

        textCant18 = findViewById(R.id.textCant18);
        contPost18 = findViewById(R.id.contPost18);
        contPost18.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Radio")));

        textCant19 = findViewById(R.id.textCant19);
        contPost19 = findViewById(R.id.contPost19);
        contPost19.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Kilometraje")));

        textCant20 = findViewById(R.id.textCant20);
        contPost20 = findViewById(R.id.contPost20);
        contPost20.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Adicional Interior")));

        textCantInt = findViewById(R.id.textCantInt);
        contPostInt = findViewById(R.id.contPostInt);
        contPostInt.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto danio Interior")));

        //region Sacar foto a posterior
        btnFotoPosterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                funcionCamara(id_inspeccion,"_Foto_Panel_ext_VP.jpg",TAKE_POSTERIOR);

            }
        });
        //endregion

        //region Sacar foto a posterior 2
        btnFotoPosterior2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                funcionCamara(id_inspeccion,"_Foto_panel_int_VP.jpg",TAKE_POSTERIOR2);

            }
        });
        //endregion

        //region Sacar foto a posterior 3
        btnFotoPosterior3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                funcionCamara(id_inspeccion,"_Foto_Radio_VP.jpg",TAKE_POSTERIOR3);

            }
        });
        //endregion

        //region Sacar foto adicional
        btnFotoAdicionalPosterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                funcionCamara(id_inspeccion,"_Foto_Kilometraje_VP.jpg",TAKE_ADDPOVP);

            }
        });
        //endregion

        //region Sacar foto adicional
        btnFotoAdicionalPosterior2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                funcionCamara(id_inspeccion,"_Foto_Adicional_Interior_VP.jpg",TAKE_ADDPOVP2);

            }
        });
        //endregion

        //region Sacar foto daño posterior
        btnFotoDanoPvp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtDescripcionDañoPvp.length()<=0){
                    Toast.makeText(interior_vp.this,"Debe ingresar la descripción del daño",Toast.LENGTH_SHORT).show();
                }else {

                    funcionCamara(id_inspeccion,"_Foto_Ddano_Interior_VP.jpg",TAKE_DANOPVP);

                }
            }
        });
        //endregion

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
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(interior_vp.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            startActivityForResult(intent, CodigoFoto);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        db.cambiarEstadoInspeccion(Integer.parseInt(id_inspeccion),1);
        if(resultCode== RESULT_OK){
            switch(requestCode){
                case TAKE_POSTERIOR:

                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitPosterior = BitmapFactory.decodeFile(mPath);
                    bitPosterior = foto.redimensiomarImagen(bitPosterior);
                    imagenPosteriorVp.setImageBitmap(bitPosterior);
                    String imagenPosterior = foto.convertirImagenDano(bitPosterior);
                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, "Panel_Interior",0,imagenPosterior, 0);

                    servis = new Intent(interior_vp.this, TransferirFoto.class);
                    servis.putExtra("comentario","Panel_Interior");
                    servis.putExtra("id_inspeccion",id_inspeccion);
                    startService(servis);

                    int cantFoto16=db.cantidadF(Integer.parseInt(id_inspeccion),"Panel_Interior");
                    cantFoto16= cantFoto16 +1;
                    db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Panel_Interior",cantFoto16);
                    contPost16.setText(String.valueOf(cantFoto16));

                    break;

                case TAKE_POSTERIOR2:

                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitPosterior2 = BitmapFactory.decodeFile(mPath);
                    bitPosterior2 = foto.redimensiomarImagen(bitPosterior2);
                    imagenPosteriorVp2.setImageBitmap(bitPosterior2);
                    String imagenPosterior2 = foto.convertirImagenDano(bitPosterior2);
                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, "Panel_Exterior",0,imagenPosterior2, 0);

                    servis = new Intent(interior_vp.this, TransferirFoto.class);
                    servis.putExtra("comentario","Panel_Exterior");
                    servis.putExtra("id_inspeccion",id_inspeccion);
                    startService(servis);

                    int cantFoto17=db.cantidadF(Integer.parseInt(id_inspeccion),"Panel_Exterior");
                    cantFoto17= cantFoto17 +1;
                    db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Panel_Exterior",cantFoto17);
                    contPost17.setText(String.valueOf(cantFoto17));


                    break;

                case TAKE_POSTERIOR3:

                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitPosterior3 = BitmapFactory.decodeFile(mPath);
                    bitPosterior3 = foto.redimensiomarImagen(bitPosterior3);
                    imagenPosteriorVp3.setImageBitmap(bitPosterior3);
                    String imagenPosterior3 = foto.convertirImagenDano(bitPosterior3);
                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, "Radio",0,imagenPosterior3, 0);

                    servis = new Intent(interior_vp.this, TransferirFoto.class);
                    servis.putExtra("comentario","Radio");
                    servis.putExtra("id_inspeccion",id_inspeccion);
                    startService(servis);

                    int cantFoto18=db.cantidadF(Integer.parseInt(id_inspeccion),"Radio");
                    cantFoto18= cantFoto18 +1;
                    db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Radio",cantFoto18);
                    contPost18.setText(String.valueOf(cantFoto18));

                    break;

                case TAKE_ADDPOVP:

                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitAddPosterior = BitmapFactory.decodeFile(mPath);
                    bitAddPosterior = foto.redimensiomarImagen(bitAddPosterior);
                    imagenAdicionalPosteriorVp.setImageBitmap(bitAddPosterior);
                    String imagenAddPosterior = foto.convertirImagenDano(bitAddPosterior);
                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, "Kilometraje",0,imagenAddPosterior,0);

                    servis = new Intent(interior_vp.this, TransferirFoto.class);
                    servis.putExtra("comentario","Kilometraje");
                    servis.putExtra("id_inspeccion",id_inspeccion);
                    startService(servis);

                    int cantFoto19=db.cantidadF(Integer.parseInt(id_inspeccion),"Kilometraje");
                    cantFoto19= cantFoto19 +1;
                    db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Kilometraje",cantFoto19);
                    contPost19.setText(String.valueOf(cantFoto19));

                    break;

                case TAKE_ADDPOVP2:

                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitAddPosterior2 = BitmapFactory.decodeFile(mPath);
                    bitAddPosterior2 = foto.redimensiomarImagen(bitAddPosterior2);
                    imagenAdicionalPosteriorVp2.setImageBitmap(bitAddPosterior2);
                    String imagenAddPosterior2 = foto.convertirImagenDano(bitAddPosterior2);
                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, "Adicional Interior",0,imagenAddPosterior2, 0);

                    servis = new Intent(interior_vp.this, TransferirFoto.class);
                    servis.putExtra("comentario","Adicional Interior");
                    servis.putExtra("id_inspeccion",id_inspeccion);
                    startService(servis);

                    int cantFoto20=db.cantidadF(Integer.parseInt(id_inspeccion),"Adicional Interior");
                    cantFoto20= cantFoto20 +1;
                    db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Adicional Interior",cantFoto20);
                    contPost20.setText(String.valueOf(cantFoto20));

                    break;

                case TAKE_DANOPVP:

                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitDanoDePosterior = BitmapFactory.decodeFile(mPath);
                    bitDanoDePosterior = foto.redimensiomarImagen(bitDanoDePosterior);
                    imgFotoDanoPvp.setImageBitmap(bitDanoDePosterior);
                    String imagenDanoDePost = foto.convertirImagenDano(bitDanoDePosterior);

                    db.insertarComentarioFoto(Integer.parseInt(id_inspeccion),edtDescripcionDañoPvp.getText().toString(),"Interior");
                    String comentarito = db.comentarioFoto(Integer.parseInt(id_inspeccion),"Interior");

                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen,comentarito,0,imagenDanoDePost, 0);

                    servis = new Intent(interior_vp.this, TransferirFoto.class);
                    servis.putExtra("comentario",comentarito);
                    servis.putExtra("id_inspeccion",id_inspeccion);
                    startService(servis);


                    int cantFotoInt=db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Danio Interior");
                    cantFotoInt= cantFotoInt +1;
                    db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Foto Danio Interior",cantFotoInt);
                    contPostInt.setText(String.valueOf(cantFotoInt));

                    break;
            }
        }
    }

    @OnClick(R.id.seccionDosPosteriorvpMQ) //Mostrar seccion 2
    public void Seccion2(View view){
        if(edtDescripcionDañoPvp.getVisibility()==View.VISIBLE){
            txtDanioPvp.setVisibility(View.GONE);
            edtDescripcionDañoPvp.setVisibility(View.GONE);
            btnFotoDanoPvp.setVisibility(View.GONE);
            imgFotoDanoPvp.setImageBitmap(null);
        }else{
            txtDanioPvp.setVisibility(View.VISIBLE);
            edtDescripcionDañoPvp.setVisibility(View.VISIBLE);
            btnFotoDanoPvp.setVisibility(View.VISIBLE);
            imgFotoDanoPvp.setVisibility(View.VISIBLE);

            btnFotoPosterior.setVisibility(View.GONE);
            imagenPosteriorVp.setVisibility(View.GONE);
            imagenPosteriorVp.setImageBitmap(null);

            textCant16.setVisibility(View.GONE);
            contPost16.setVisibility(View.GONE);
            textCant17.setVisibility(View.GONE);
            contPost17.setVisibility(View.GONE);
            textCant18.setVisibility(View.GONE);
            contPost18.setVisibility(View.GONE);
            textCant19.setVisibility(View.GONE);
            contPost19.setVisibility(View.GONE);
            textCant20.setVisibility(View.GONE);
            contPost20.setVisibility(View.GONE);

            btnFotoPosterior2.setVisibility(View.GONE);
            imagenPosteriorVp2.setVisibility(View.GONE);
            imagenPosteriorVp2.setImageBitmap(null);

            btnFotoPosterior3.setVisibility(View.GONE);
            imagenPosteriorVp3.setVisibility(View.GONE);
            imagenPosteriorVp3.setImageBitmap(null);

            btnFotoAdicionalPosterior.setVisibility(View.GONE);
            imagenAdicionalPosteriorVp.setVisibility(View.GONE);
            imagenAdicionalPosteriorVp.setImageBitmap(null);

            btnFotoAdicionalPosterior2.setVisibility(View.GONE);
            imagenAdicionalPosteriorVp2.setVisibility(View.GONE);
            imagenAdicionalPosteriorVp2.setImageBitmap(null);

            String imagenDanoPosterior = db.foto(Integer.parseInt(id_inspeccion),db.comentarioFoto(Integer.parseInt(id_inspeccion),"Interior"));

            if(imagenDanoPosterior.length()>=3 )
            {
                byte[] decodedString = Base64.decode(imagenDanoPosterior, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imgFotoDanoPvp.setImageBitmap(decodedByte);
            }
        }
    }

    @OnClick(R.id.btnSeccionUnoPosteriorvpMQ) //Mostrar seccion 1
    public void Seccion1(View view){
        if(btnFotoPosterior.getVisibility()==View.VISIBLE){
            btnFotoPosterior.setVisibility(View.GONE);
            imagenPosteriorVp.setVisibility(View.GONE);
            imagenPosteriorVp.setImageBitmap(null);
            btnFotoPosterior2.setVisibility(View.GONE);
            imagenPosteriorVp2.setVisibility(View.GONE);
            imagenPosteriorVp2.setImageBitmap(null);
            btnFotoPosterior3.setVisibility(View.GONE);
            imagenPosteriorVp3.setVisibility(View.GONE);
            imagenPosteriorVp3.setImageBitmap(null);
            btnFotoAdicionalPosterior.setVisibility(View.GONE);
            imagenAdicionalPosteriorVp.setVisibility(View.GONE);
            imagenAdicionalPosteriorVp.setImageBitmap(null);
            btnFotoAdicionalPosterior2.setVisibility(View.GONE);
            imagenAdicionalPosteriorVp2.setVisibility(View.GONE);
            imagenAdicionalPosteriorVp2.setImageBitmap(null);
            textCant16.setVisibility(View.GONE);
            contPost16.setVisibility(View.GONE);
            textCant17.setVisibility(View.GONE);
            contPost17.setVisibility(View.GONE);
            textCant18.setVisibility(View.GONE);
            contPost18.setVisibility(View.GONE);
            textCant19.setVisibility(View.GONE);
            contPost19.setVisibility(View.GONE);
            textCant20.setVisibility(View.GONE);
            contPost20.setVisibility(View.GONE);

        }else{
            btnFotoPosterior.setVisibility(View.VISIBLE);
            imagenPosteriorVp.setVisibility(View.VISIBLE);
            btnFotoPosterior2.setVisibility(View.VISIBLE);
            imagenPosteriorVp2.setVisibility(View.VISIBLE);
            btnFotoPosterior3.setVisibility(View.VISIBLE);
            imagenPosteriorVp3.setVisibility(View.VISIBLE);
            btnFotoAdicionalPosterior.setVisibility(View.VISIBLE);
            imagenAdicionalPosteriorVp.setVisibility(View.VISIBLE);
            btnFotoAdicionalPosterior2.setVisibility(View.VISIBLE);
            imagenAdicionalPosteriorVp2.setVisibility(View.VISIBLE);
            textCant16.setVisibility(View.VISIBLE);
            contPost16.setVisibility(View.VISIBLE);
            textCant17.setVisibility(View.VISIBLE);
            contPost17.setVisibility(View.VISIBLE);
            textCant18.setVisibility(View.VISIBLE);
            contPost18.setVisibility(View.VISIBLE);
            textCant19.setVisibility(View.VISIBLE);
            contPost19.setVisibility(View.VISIBLE);
            textCant20.setVisibility(View.VISIBLE);
            contPost20.setVisibility(View.VISIBLE);

            txtDanioPvp.setVisibility(View.GONE);
            edtDescripcionDañoPvp.setVisibility(View.GONE);
            btnFotoDanoPvp.setVisibility(View.GONE);
            imgFotoDanoPvp.setImageBitmap(null);

            String imagenPosterior = db.foto(Integer.parseInt(id_inspeccion),"Panel_Interior");
            String imagenPosterior2 = db.foto(Integer.parseInt(id_inspeccion),"Panel_Exterior");
            String imagenPosterior3 = db.foto(Integer.parseInt(id_inspeccion),"Radio");


            String imagenAdicional = db.foto(Integer.parseInt(id_inspeccion),"Kilometraje");
            String imagenAdicional2 = db.foto(Integer.parseInt(id_inspeccion),"Adicional Interior");


            if(imagenPosterior.length()>=3){
                byte[] decodeString = Base64.decode(imagenPosterior,Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodeString,0,decodeString.length);
                imagenPosteriorVp.setImageBitmap(decodedByte);
            }
            if(imagenPosterior2.length()>=3){
                byte[] decodeString = Base64.decode(imagenPosterior2,Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodeString,0,decodeString.length);
                imagenPosteriorVp2.setImageBitmap(decodedByte);
            }
            if(imagenPosterior3.length()>=3){
                byte[] decodeString = Base64.decode(imagenPosterior3,Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodeString,0,decodeString.length);
                imagenPosteriorVp3.setImageBitmap(decodedByte);
            }
            if(imagenAdicional.length()>=3){
                byte[] decodeString = Base64.decode(imagenAdicional,Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodeString,0,decodeString.length);
                imagenAdicionalPosteriorVp.setImageBitmap(decodedByte);
            }
            if(imagenAdicional2.length()>=3){
                byte[] decodeString = Base64.decode(imagenAdicional2,Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodeString,0,decodeString.length);
                imagenAdicionalPosteriorVp2.setImageBitmap(decodedByte);
            }

        }
    }


    @OnClick(R.id.btnSiguientePvpMQ)//Seguir a la siguiente seccion de camara
    public void seguir(View view){

        String imagenPosterior = db.foto(Integer.parseInt(id_inspeccion),"Panel_Interior");
        String imagenPosterior2 = db.foto(Integer.parseInt(id_inspeccion),"Panel_Exterior");
        String imagenPosterior3 = db.foto(Integer.parseInt(id_inspeccion),"Radio");
        String imagenAdicional = db.foto(Integer.parseInt(id_inspeccion),"Kilometraje");

        Log.i("xx","xx" + tipoVeh );
        if(imagenPosterior.length()>=3 && imagenPosterior2.length()>=3 && imagenPosterior3.length()>=3 && imagenAdicional.length()>=3) {

            if(tipoVeh.equals("1") || tipoVeh.equals("2"))
            {

                Intent in = new Intent(interior_vp.this, MotorVp.class);
                in.putExtra("id_inspeccion", id_inspeccion);
                in.putExtra("tipoVeh", tipoVeh);
                startActivity(in);
                finish();
            }
            else
            {

                Intent in = new Intent(interior_vp.this, DatosAsegVpActivity.class);
                in.putExtra("id_inspeccion", id_inspeccion);
                in.putExtra("tipoVeh", tipoVeh);
                startActivity(in);
                finish();

            }

        }else{
            Toast.makeText(interior_vp.this,"Faltan fotos obligatorias por tomar",Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btnVolverPvpMQ)//Volver a las secciones
    public void volver(View view){
        Intent in = new Intent(interior_vp.this, llanaNeuma_vp.class);
        in.putExtra("id_inspeccion",id_inspeccion);
        in.putExtra("tipoVeh",tipoVeh);
        startActivity(in);
        finish();
    }

}
