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
import com.letchile.let.VehPesado.SeccionVpActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class lateral_derecho_vp extends AppCompatActivity {

    @BindView(R.id.btnPosteriorVP)Button btnFotoPosterior;
    @BindView(R.id.imageViewFotoPvpMQ)ImageView imagenPosteriorVp;
    @BindView(R.id.btnFotoAdiocionalPvpMQ)Button btnFotoAdicionalPosterior;
    @BindView(R.id.imageFotoAdicionalPvpMQ)ImageView imagenAdicionalPosteriorVp;

    @BindView(R.id.txtDanio)TextView txtDanioPvp;
    @BindView(R.id.DescripDanoPvpMQ)EditText edtDescripcionDañoPvp;
    @BindView(R.id.btnFotoDanoPvp)Button btnFotoDanoPvp;
    @BindView(R.id.imgFotoDanoPvp)ImageView imgFotoDanoPvp;

    private final int TAKE_POSTERIOR = 100, TAKE_ADDPOVP=200,TAKE_DANOPVP=300;
    String id_inspeccion,ruta,mPath,nombreimagen,tipoVeh;
    private File ruta_sd;
    int correlativo = 0;
    private TextView textCant6,contPost6, textCant7,contPost7,textCantVD,contPostVD;
    DBprovider db;
    PropiedadesFoto foto;
    Intent servis;

    public lateral_derecho_vp(){
        db = new DBprovider(this);
        foto = new PropiedadesFoto(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lateral_derecho_vp);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        id_inspeccion = bundle.getString("id_inspeccion");
        tipoVeh = bundle.getString("tipoVeh");

        textCant6 = findViewById(R.id.textCant6);
        contPost6 = findViewById(R.id.contPost6);
        contPost6.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"LateralDerecho")));

        textCant7 = findViewById(R.id.textCant7);
        contPost7 = findViewById(R.id.contPost7);
        contPost7.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Adicional LateralDerecho")));

        textCantVD = findViewById(R.id.textCantVD);
        contPostVD = findViewById(R.id.contPostVD);
        contPostVD.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Danio lateralDerecho")));

        //region Sacar foto a posterior
        btnFotoPosterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                funcionCamara(id_inspeccion,"_Foto_Dano_LateralDerecho_VP.jpg",TAKE_POSTERIOR);

            }
        });
        //endregion

        //region Sacar foto adicional
        btnFotoAdicionalPosterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                funcionCamara(id_inspeccion,"_Foto_Adicional_LateralDerecho_VP.jpg",TAKE_ADDPOVP);

            }
        });
        //endregion

        //region Sacar foto daño posterior
        btnFotoDanoPvp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtDescripcionDañoPvp.length()<=0){
                    Toast.makeText(lateral_derecho_vp.this,"Debe ingresar la descripción del daño",Toast.LENGTH_SHORT).show();
                }else {
                    funcionCamara(id_inspeccion,"_Foto_Ddano_LateralDerecho_VP.jpg",TAKE_DANOPVP);
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
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(lateral_derecho_vp.this,
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
                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, "LateralDerecho",0,imagenPosterior, 0);

                    servis = new Intent(lateral_derecho_vp.this, TransferirFoto.class);
                    servis.putExtra("comentario","LateralDerecho");
                    servis.putExtra("id_inspeccion",id_inspeccion);
                    startService(servis);


                    int cantFoto6=db.cantidadF(Integer.parseInt(id_inspeccion),"LateralDerecho");
                    cantFoto6= cantFoto6 +1;
                    db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"LateralDerecho",cantFoto6);
                    contPost6.setText(String.valueOf(cantFoto6));

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
                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, "Adicional LateralDerecho",0,imagenAddPosterior, 0);

                    servis = new Intent(lateral_derecho_vp.this, TransferirFoto.class);
                    servis.putExtra("comentario","Adicional LateralDerecho");
                    servis.putExtra("id_inspeccion",id_inspeccion);
                    startService(servis);

                    int cantFoto7=db.cantidadF(Integer.parseInt(id_inspeccion),"Adicional LateralDerecho");
                    cantFoto7= cantFoto7 +1;
                    db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Adicional LateralDerecho",cantFoto7);
                    contPost7.setText(String.valueOf(cantFoto7));

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

                    db.insertarComentarioFoto(Integer.parseInt(id_inspeccion),edtDescripcionDañoPvp.getText().toString(),"LateralDerecho");
                    String comentarito = db.comentarioFoto(Integer.parseInt(id_inspeccion),"LateralDerecho");

                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen,comentarito,0,imagenDanoDePost,0);

                    servis = new Intent(lateral_derecho_vp.this, TransferirFoto.class);
                    servis.putExtra("comentario",comentarito);
                    servis.putExtra("id_inspeccion",id_inspeccion);
                    startService(servis);

                    int cantFotoVD=db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Danio LateralDerecho");
                    cantFotoVD= cantFotoVD +1;
                    db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Foto Danio LateralDerecho",cantFotoVD);
                    contPostVD.setText(String.valueOf(cantFotoVD));

                    break;
            }
        }
    }


    @OnClick(R.id.btnSeccionUnoPosteriorvpMQ) //Mostrar seccion 1
    public void Seccion1(View view){
        if(btnFotoPosterior.getVisibility()==View.VISIBLE){
            btnFotoPosterior.setVisibility(View.GONE);
            imagenPosteriorVp.setImageBitmap(null);
            imagenPosteriorVp.setVisibility(View.GONE);
            btnFotoAdicionalPosterior.setVisibility(View.GONE);
            imagenAdicionalPosteriorVp.setImageBitmap(null);
            imagenAdicionalPosteriorVp.setVisibility(View.GONE);
            textCant6.setVisibility(View.GONE);
            contPost6.setVisibility(View.GONE);
            textCant7.setVisibility(View.GONE);
            contPost7.setVisibility(View.GONE);

        }else{
            btnFotoPosterior.setVisibility(View.VISIBLE);
            imagenPosteriorVp.setVisibility(View.VISIBLE);
            btnFotoAdicionalPosterior.setVisibility(View.VISIBLE);
            imagenAdicionalPosteriorVp.setVisibility(View.VISIBLE);
            textCant6.setVisibility(View.VISIBLE);
            contPost6.setVisibility(View.VISIBLE);
            textCant7.setVisibility(View.VISIBLE);
            contPost7.setVisibility(View.VISIBLE);

            txtDanioPvp.setVisibility(View.GONE);
            edtDescripcionDañoPvp.setVisibility(View.GONE);
            btnFotoDanoPvp.setVisibility(View.GONE);
            imgFotoDanoPvp.setImageBitmap(null);
            textCantVD.setVisibility(View.GONE);
            contPostVD.setVisibility(View.GONE);


            String imagenPosterior = db.foto(Integer.parseInt(id_inspeccion),"LateralDerecho");
            String imagenAdicional = db.foto(Integer.parseInt(id_inspeccion),"Adicional LateralDerecho");

            if(imagenPosterior.length()>=3){
                byte[] decodeString = Base64.decode(imagenPosterior,Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodeString,0,decodeString.length);
                imagenPosteriorVp.setImageBitmap(decodedByte);
            }
            if(imagenAdicional.length()>=3){
                byte[] decodeString = Base64.decode(imagenPosterior,Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodeString,0,decodeString.length);
                imagenAdicionalPosteriorVp.setImageBitmap(decodedByte);
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
            textCantVD.setVisibility(View.GONE);
            contPostVD.setVisibility(View.GONE);

        }else{
            txtDanioPvp.setVisibility(View.VISIBLE);
            edtDescripcionDañoPvp.setVisibility(View.VISIBLE);
            btnFotoDanoPvp.setVisibility(View.VISIBLE);
            imgFotoDanoPvp.setVisibility(View.VISIBLE);
            textCantVD.setVisibility(View.VISIBLE);
            contPostVD.setVisibility(View.VISIBLE);

            btnFotoPosterior.setVisibility(View.GONE);
            imagenPosteriorVp.setVisibility(View.GONE);
            imagenPosteriorVp.setImageBitmap(null);
            btnFotoAdicionalPosterior.setVisibility(View.GONE);
            imagenAdicionalPosteriorVp.setVisibility(View.GONE);
            imagenAdicionalPosteriorVp.setImageBitmap(null);
            textCant6.setVisibility(View.GONE);
            contPost6.setVisibility(View.GONE);
            textCant7.setVisibility(View.GONE);
            contPost7.setVisibility(View.GONE);

            String imagenDanoPosterior = db.foto(Integer.parseInt(id_inspeccion),db.comentarioFoto(Integer.parseInt(id_inspeccion),"LateralDerecho"));

            if(imagenDanoPosterior.length()>=3 )
            {
                byte[] decodedString = Base64.decode(imagenDanoPosterior, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imgFotoDanoPvp.setImageBitmap(decodedByte);
            }

        }
    }

    @OnClick(R.id.btnSiguientePvpMQ)//Seguir a la siguiente seccion de camara
    public void seguir(View view){

        String imagenPosterior = db.foto(Integer.parseInt(id_inspeccion),"LateralDerecho");

        if(imagenPosterior.length()>=3) {

            Intent in = new Intent(lateral_derecho_vp.this, frontal_vp.class);
            in.putExtra("id_inspeccion", id_inspeccion);
            in.putExtra("tipoVeh", tipoVeh);
            startActivity(in);
            finish();
        }else{
            Toast.makeText(lateral_derecho_vp.this,"Faltan fotos obligatorias por tomar",Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btnVolverPvpMQ)//Volver a las secciones
    public void volver(View view){
        Intent in = new Intent(lateral_derecho_vp.this, posterior_vp.class);
        in.putExtra("id_inspeccion",id_inspeccion);
        in.putExtra("tipoVeh",tipoVeh);
        startActivity(in);
        finish();
    }



}
