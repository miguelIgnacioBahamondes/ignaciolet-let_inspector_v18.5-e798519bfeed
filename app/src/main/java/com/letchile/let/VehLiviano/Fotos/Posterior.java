package com.letchile.let.VehLiviano.Fotos;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ablanco.zoomy.TapListener;
import com.ablanco.zoomy.Zoomy;
import com.letchile.let.BD.DBprovider;
import com.letchile.let.BuildConfig;
import com.letchile.let.Clases.PropiedadesFoto;
import com.letchile.let.Clases.Validaciones;
import com.letchile.let.LoginActivity;
import com.letchile.let.R;
import com.letchile.let.Remoto.InterfacePost;
import com.letchile.let.Servicios.ConexionInternet;
import com.letchile.let.Servicios.TransferirFoto;
import com.letchile.let.Servicios.TransferirFotoV2;
import com.letchile.let.VehLiviano.SeccionActivity;
import com.letchile.let.VehLiviano.seccion2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.http.Url;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by LETCHILE on 20/02/2018.
 */

public class Posterior extends AppCompatActivity {

    DBprovider db;
    Boolean connec = false;

    private static String APP_DIRECTORY = "";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY ;
    private final int PHOTO_CODE = 200;
    private final int TAKE_POSTERIOR = 300;
    private final int TAKE_LUNETA = 400;
    private final int TAKE_ADICIONAL = 500;
    private final int TAKE_SENSORES = 600;
    private final int TAKE_CAMARA = 700;
    private final int TAKE_COCO = 800;
    private final int TAKE_MUELA = 900;
    private final int TAKE_REMOLQUE = 1000;

    private final int TAKE_EQFRIO = 1100;
    private final int TAKE_CREFRI = 1200;
    private final int TAKE_CUBREPICK = 1300;
    private final int TAKE_TAPARIGIDA = 1400;
    private final int TAKE_LONACUBRE = 1500;
    private final int TAKE_HERRAMIENTA = 1600;
    private final int TAKE_SISTEMA_ARRASTRE = 1700;


    private ImageView mSetImage, imageViewFotoPoE,imageLogoLunetaE,imageFotoAdicionalE,imageSensores,imageCameraPoE,imageCocoPoE,imageMuelaE,imageenChufeRemolque,imageCamRePoE,imageCubrePickPoE,imageEquipoE,imageTapaRPoE,imageLonaCPoE,imageCajaHerrPoE, imageViewSistArrastre;
    private RelativeLayout mRlView;
    private String mPath;
    private Button btnVolverPoE,btnVolerSecPoE,btnSiguientePoE,btnPosteriorE,btnLogoLunetaE,btnFotoAdiocionalE,btnFotoDanoE,btnSeccionPos1E,seccionPos2E,seccionPos3E,seccionPos3EMQ,btnSistArrastre;
    private TextView txtPieza,txtTipoDanoE,txtDeducibleE,textCant1,contPost, textCant2,contPost2, textCant3,contPost3,textArrastre,contPostS,textCantS, contPostA,textCantA;
    private Spinner spinnerPiezaPoE,spinnerDanoPoE,spinnerDeduciblePoE;
    private String sd;
    private File ruta_sd;
    private String nombre_foto = "";
    private String ruta = "";
    private CheckBox sensoresPoE,camaraPoE,cocoPoE,muelaPoE,enchufeRemolque,camaraRefriPoE,cubrePickPoE,equipoPoE,tapaRiPoE,LonaCubrePoE,cajaHerrPoE ;
    PropiedadesFoto foto;
    String nombreimagen = "", comentarioDañoImg="";
    Validaciones validaciones;
    View linea;
    int correlativo = 0;
    String dañosDedu[][];
    ExifInterface exifObject;



    public Posterior(){db = new DBprovider(this);foto=new PropiedadesFoto(this);validaciones = new Validaciones(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vl_posterior);

        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion = bundle.getString("id_inspeccion");

        connec = new ConexionInternet(this).isConnectingToInternet();

        mRlView = findViewById(R.id.relativeLayout2);
        btnPosteriorE = findViewById((R.id.btnPosteriorE));
        imageViewFotoPoE= findViewById(R.id.imageViewFotoPoE);
        btnLogoLunetaE = findViewById(R.id.btnLogoLunetaE);
        imageLogoLunetaE = findViewById(R.id.imageLogoLunetaE);
        btnFotoAdiocionalE = findViewById(R.id.btnFotoAdiocionalE);
        imageFotoAdicionalE = findViewById(R.id.imageFotoAdicionalE);
        mSetImage = findViewById(R.id.imagenPoDanoE);
        btnFotoDanoE = findViewById((R.id.btnFotoDanoE));
        sensoresPoE = findViewById(R.id.sensoresPoE);
        imageSensores = findViewById(R.id.imageSensores);
        camaraPoE = findViewById(R.id.camaraPoE);
        imageCameraPoE = findViewById(R.id.imageCameraPoE);
        cocoPoE = findViewById(R.id.cocoPoE);
       // imageCocoPoE = findViewById(R.id.imageCocoPoE);
        muelaPoE = findViewById(R.id.muelaPoE);
        //imageMuelaE = findViewById(R.id.imageMuelaE);
        spinnerPiezaPoE = findViewById(R.id.spinnerPiezaPoE);
        spinnerDanoPoE = findViewById(R.id.spinnerDanoPoE);
        spinnerDeduciblePoE = findViewById(R.id.spinnerDeduciblePoE);
        btnSeccionPos1E = findViewById(R.id.btnSeccionPos1E);
        txtPieza = findViewById(R.id.txtPieza);
        txtTipoDanoE = findViewById(R.id.txtTipoDanoE);
        txtDeducibleE = findViewById(R.id.txtDeducibleE);
        seccionPos2E = findViewById(R.id.seccionPos2E);
        seccionPos3E = findViewById(R.id.seccionPos3E);
        //seccionPos3EMQ = findViewById(R.id.seccionPos3MQ);
        enchufeRemolque = findViewById(R.id.enchufeRemolque);
        //imageenChufeRemolque = findViewById(R.id.imageenChufeRemolque);
        //camaraRefriPoE = findViewById(R.id.camaraRefriPoE);
        linea = findViewById(R.id.linea);
        btnSistArrastre = findViewById(R.id.btnSistArrastre);
        imageViewSistArrastre = findViewById(R.id.imageViewSistArrastre);
        textArrastre = findViewById(R.id.textArrastre);

        textCantS = findViewById(R.id.textCantS);
        contPostS = findViewById(R.id.contPostS);
        contPostS.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Danio Posterior")));


        textCant1 = findViewById(R.id.textCant1);
        contPost = findViewById(R.id.contPost);
        contPost.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Posterior")));

        textCant2 = findViewById(R.id.textCant2);
        contPost2 = findViewById(R.id.contPost2);
        contPost2.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Logo Luneta Posterior")));

        textCant3 = findViewById(R.id.textCant3);
        contPost3 = findViewById(R.id.contPost3);
        contPost3.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Adicional Posterior")));

        textCantA = findViewById(R.id.textCantA);
        contPostA = findViewById(R.id.contPostA);
        contPostA.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Sistema Arrastre")));





        //accesorios faltantes
        imageCamRePoE = findViewById(R.id.imageCamRePoE);
        cubrePickPoE = findViewById(R.id.cubrePickPoE);
        imageCubrePickPoE = findViewById(R.id.imageCubrePickPoE);
        equipoPoE = findViewById(R.id.equipoPoE);
        imageEquipoE = findViewById(R.id.imageEquipoE);
        tapaRiPoE = findViewById(R.id.tapaRiPoE);
        imageTapaRPoE = findViewById(R.id.imageTapaRPoE);
        LonaCubrePoE = findViewById(R.id.LonaCubrePoE);
        imageLonaCPoE = findViewById(R.id.imageLonaCPoE);
        cajaHerrPoE = findViewById(R.id.cajaHerrPoE);
        imageCajaHerrPoE = findViewById(R.id.imageCajaHerrPoE);




        /////CUANDO SE SACA LA PRIMERA FOTO SE TIENE QUE GRABAR LA FECHA Y LA HORA

        btnSeccionPos1E.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DesplegarCamposSeccionUno(id_inspeccion);
            }
        });

        seccionPos2E.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desplegarCamposSeccionDos(id_inspeccion);
            }
        });
        seccionPos3E.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desplegarCamposSeccionTres(id_inspeccion);
            }
        });
       /* seccionPos3EMQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                desplegarCamposSeccionTresMQ(id_inspeccion);
            }
        });*/

        btnFotoDanoE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcionCamara(id_inspeccion,"_Foto_Dano_Posterior.jpg",PHOTO_CODE);
            }
        });

        btnPosteriorE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //SOLO EN LA PRIMERA FOTO DE POSTERIOR
               /* SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String fechaInspeccion = sdf.format(new Date());
                db.insertarValor(Integer.parseInt(id_inspeccion),360, fechaInspeccion);

                SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
                String horaInspeccion = sdf2.format(new Date());
                db.insertarValor(Integer.parseInt(id_inspeccion),361,horaInspeccion);*/

                funcionCamara(id_inspeccion,"_Foto_Posterior.jpg",TAKE_POSTERIOR);



            }
        });
        btnLogoLunetaE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {funcionCamara(id_inspeccion,"_Logo_Luneta_Posterior.jpg",TAKE_LUNETA);

            }
        });

        // CHECKBOX
        sensoresPoE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),317));
        sensoresPoE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 317).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_Sensores_Posterior.jpg",TAKE_SENSORES);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),317,"");
                    imageSensores.setImageBitmap(null);
                }
            }
        });

        camaraPoE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),314));
        camaraPoE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 314).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_Camara_Posterior.jpg",TAKE_CAMARA);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),314,"");
                    imageCameraPoE.setImageBitmap(null);
                }
            }
        });

        cocoPoE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),273));
        cocoPoE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 273).toString().equals("Ok")) {

                        db.insertarValor(Integer.parseInt(id_inspeccion),273,"Ok");

                        if (muelaPoE.isChecked() || enchufeRemolque.isChecked() || cocoPoE.isChecked())
                        {
                            btnSistArrastre.setVisibility(View.VISIBLE);
                            imageViewSistArrastre.setVisibility(View.VISIBLE);
                            textCantA.setVisibility(View.VISIBLE);
                            contPostA.setVisibility(View.VISIBLE);

                        }
                        else{
                            btnSistArrastre.setVisibility(View.GONE);
                            imageViewSistArrastre.setVisibility(View.GONE);
                            textCantA.setVisibility(View.GONE);
                            contPostA.setVisibility(View.GONE);
                        }

                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),273,"");

                    if (muelaPoE.isChecked() || enchufeRemolque.isChecked() ||  cocoPoE.isChecked())
                    {
                        btnSistArrastre.setVisibility(View.VISIBLE);
                        imageViewSistArrastre.setVisibility(View.VISIBLE);
                        textCantA.setVisibility(View.VISIBLE);
                        contPostA.setVisibility(View.VISIBLE);

                    }
                    else{
                        btnSistArrastre.setVisibility(View.GONE);
                        imageViewSistArrastre.setVisibility(View.GONE);
                        textCantA.setVisibility(View.GONE);
                        contPostA.setVisibility(View.GONE);
                    }
                }

            }
        });
       /* cocoPoE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 273).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_Coco_Posterior.jpg",TAKE_COCO);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),273,"");
                    imageCocoPoE.setImageBitmap(null);
                }
            }
        });*/

        muelaPoE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),275));
        muelaPoE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    db.insertarValor(Integer.parseInt(id_inspeccion),275,"Ok");
                    if (cocoPoE.isChecked() || enchufeRemolque.isChecked() || muelaPoE.isChecked())
                    {
                        btnSistArrastre.setVisibility(View.VISIBLE);
                        imageViewSistArrastre.setVisibility(View.VISIBLE);
                        textCantA.setVisibility(View.VISIBLE);
                        contPostA.setVisibility(View.VISIBLE);

                    }
                    else{
                        btnSistArrastre.setVisibility(View.GONE);
                        imageViewSistArrastre.setVisibility(View.GONE);
                        textCantA.setVisibility(View.GONE);
                        contPostA.setVisibility(View.GONE);
                    }

                }
                else
                {
                    db.insertarValor(Integer.parseInt(id_inspeccion),275,"");
                    if (cocoPoE.isChecked() || enchufeRemolque.isChecked() || muelaPoE.isChecked())
                    {
                        btnSistArrastre.setVisibility(View.VISIBLE);
                        imageViewSistArrastre.setVisibility(View.VISIBLE);
                        textCantA.setVisibility(View.VISIBLE);
                        contPostA.setVisibility(View.VISIBLE);

                    }
                    else{
                        btnSistArrastre.setVisibility(View.GONE);
                        imageViewSistArrastre.setVisibility(View.GONE);
                        textCantA.setVisibility(View.GONE);
                        contPostA.setVisibility(View.GONE);
                    }

                }

            }
        });



        enchufeRemolque.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),274));
        enchufeRemolque.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                   db.insertarValor(Integer.parseInt(id_inspeccion),274,"Ok");
                    if (cocoPoE.isChecked() || muelaPoE.isChecked() || enchufeRemolque.isChecked())
                    {
                        btnSistArrastre.setVisibility(View.VISIBLE);
                        imageViewSistArrastre.setVisibility(View.VISIBLE);
                        textCantA.setVisibility(View.VISIBLE);
                        contPostA.setVisibility(View.VISIBLE);

                    }
                    else{
                        btnSistArrastre.setVisibility(View.GONE);
                        imageViewSistArrastre.setVisibility(View.GONE);
                        textCantA.setVisibility(View.GONE);
                        contPostA.setVisibility(View.GONE);
                    }

                }
                else
                {

                    db.insertarValor(Integer.parseInt(id_inspeccion),274,"");
                    if (cocoPoE.isChecked() || muelaPoE.isChecked() || enchufeRemolque.isChecked())
                    {
                        btnSistArrastre.setVisibility(View.VISIBLE);
                        imageViewSistArrastre.setVisibility(View.VISIBLE);
                        textCantA.setVisibility(View.VISIBLE);
                        contPostA.setVisibility(View.VISIBLE);

                    }
                    else{
                        btnSistArrastre.setVisibility(View.GONE);
                        imageViewSistArrastre.setVisibility(View.GONE);
                        textCantA.setVisibility(View.GONE);
                        contPostA.setVisibility(View.GONE);
                    }
                }
            }
        });



      /*  cocoPoE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 273).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_Coco_Posterior.jpg",TAKE_COCO);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),273,"");
                    imageCocoPoE.setImageBitmap(null);
                }
            }
        });*/


        btnSistArrastre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                funcionCamara(id_inspeccion,"_Sistema_arrastre.jpg",TAKE_SISTEMA_ARRASTRE);
            }
        });


        //accesorios faltantes

       /* camaraRefriPoE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),331));
        camaraRefriPoE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 331).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_Refrigerada.jpg",TAKE_CREFRI);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),331,"");
                    imageCamRePoE.setImageBitmap(null);
                }
            }
        });*/


        equipoPoE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),346));
        equipoPoE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 346).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_Equipo_Frio.jpg",TAKE_EQFRIO);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),346,"");
                    imageEquipoE.setImageBitmap(null);
                }
            }
        });

        cubrePickPoE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),305));
        cubrePickPoE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 305).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_Cubre_Pick_up.jpg",TAKE_CUBREPICK);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),305,"");
                    imageCubrePickPoE.setImageBitmap(null);
                }
            }
        });

        tapaRiPoE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),307));
        tapaRiPoE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 307).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_Tapa_Rigida.jpg",TAKE_TAPARIGIDA);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),307,"");
                    imageTapaRPoE.setImageBitmap(null);
                }
            }
        });

        LonaCubrePoE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),306));
        LonaCubrePoE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 306).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_Lona_cubre.jpg",TAKE_LONACUBRE);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),306,"");
                    imageLonaCPoE.setImageBitmap(null);
                }
            }
        });

        cajaHerrPoE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),310));
        cajaHerrPoE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 310).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_caja_herramientas.jpg",TAKE_HERRAMIENTA);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),310,"");
                    imageCajaHerrPoE.setImageBitmap(null);
                }
            }
        });


        btnFotoAdiocionalE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcionCamara(id_inspeccion,"_Foto_Adicional_Posterior.jpg",TAKE_ADICIONAL);
            }
        });





        btnVolverPoE = findViewById(R.id.btnVolverPvpMQ);
        btnVolverPoE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent   = new Intent(Posterior.this,documento.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
                finish();
            }
        });


        btnSiguientePoE = findViewById(R.id.btnSiguientePoE);
        btnSiguientePoE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imagenPosterior = db.foto(Integer.parseInt(id_inspeccion),"Posterior");
                String imagenLogoLuneta = db.foto(Integer.parseInt(id_inspeccion),"Logo Luneta Posterior");
                String imagenDanioPosterior = db.foto(Integer.parseInt(id_inspeccion),"Foto Danio Posterior");
                String imagenSistemaArrastre = db.foto(Integer.parseInt(id_inspeccion),"Foto Sistema Arrastre");


              if (cocoPoE.isChecked() || muelaPoE.isChecked() || enchufeRemolque.isChecked()) {

                    if(imagenSistemaArrastre.length()<=3) {
                        Toast.makeText(Posterior.this, "Debe tomar fotografía de sistema de arrastre", Toast.LENGTH_SHORT).show();
                    }
                    else if(imagenLogoLuneta.length()<=3 && imagenPosterior.length()<=3){
                        //Toast toast =  Toast.makeText(prueba.this, "Debe Tomar Fotos Obligatorias", Toast.LENGTH_SHORT);
                        //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        //toast.show()
                        AlertDialog.Builder builder = new AlertDialog.Builder(Posterior.this);
                        builder.setCancelable(false);
                        builder.setTitle("LET Chile");
                        builder.setMessage(Html.fromHtml("<b>Debe Tomar Fotos Obligatorias</b><p><ul><li>- Foto Posterior</li><p><li>- Foto Logo Luneta</li></p></ul></p>"));
                        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();

                    }
                    else if(imagenPosterior.length()<=3){
                        //Toast toast =  Toast.makeText(prueba.this, "Debe Tomar Fotos Obligatorias", Toast.LENGTH_SHORT);
                        //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        //toast.show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Posterior.this);
                        builder.setCancelable(false);
                        builder.setTitle("LET Chile");
                        builder.setMessage(Html.fromHtml("<b>Debe Tomar Fotos Obligatorias</b><p><ul><li>- Foto Posterior</li></ul></p>"));
                        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                    else if(imagenLogoLuneta.length()<=3 ){
                        //Toast toast =  Toast.makeText(prueba.this, "Debe Tomar Fotos Obligatorias", Toast.LENGTH_SHORT);
                        //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        //toast.show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Posterior.this);
                        builder.setCancelable(false);
                        builder.setTitle("LET Chile");
                        builder.setMessage(Html.fromHtml("<b>Debe Tomar Fotos Obligatorias</b><p><ul><li>- Foto Logo Luneta</li></p></ul></p>"));
                        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();

                    }
                    else{
                        Intent intent   = new Intent(Posterior.this,lateralderecho.class);
                        intent.putExtra("id_inspeccion",id_inspeccion);
                        startActivity(intent);
                        finish();

                    }
              }

               else if(imagenLogoLuneta.length()<=3 && imagenPosterior.length()<=3){
                    //Toast toast =  Toast.makeText(prueba.this, "Debe Tomar Fotos Obligatorias", Toast.LENGTH_SHORT);
                    //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    //toast.show()
                    AlertDialog.Builder builder = new AlertDialog.Builder(Posterior.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe Tomar Fotos Obligatorias</b><p><ul><li>- Foto Posterior</li><p><li>- Foto Logo Luneta</li></p></ul></p>"));
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();

                }
                else if(imagenPosterior.length()<=3){
                    //Toast toast =  Toast.makeText(prueba.this, "Debe Tomar Fotos Obligatorias", Toast.LENGTH_SHORT);
                    //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    //toast.show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(Posterior.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe Tomar Fotos Obligatorias</b><p><ul><li>- Foto Posterior</li></ul></p>"));
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else if(imagenLogoLuneta.length()<=3 ){
                    //Toast toast =  Toast.makeText(prueba.this, "Debe Tomar Fotos Obligatorias", Toast.LENGTH_SHORT);
                    //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    //toast.show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(Posterior.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe Tomar Fotos Obligatorias</b><p><ul><li>- Foto Logo Luneta</li></p></ul></p>"));
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();

                }
                else{
                    Intent intent   = new Intent(Posterior.this,lateralderecho.class);
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
            intent.putExtra(MediaStore.EXTRA_OUTPUT,  FileProvider.getUriForFile(Posterior.this,
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
            //CAMBIAR EL ESTADO DE LA INSPECCIÓN A INICIADA PARA PODER VALIDAR DESPUÉS
           // db.cambiarEstadoInspeccion(Integer.parseInt(id_inspeccion), 1);

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

                        String imagenDano = foto.convertirImagenDano(bitmap);


                        dañosDedu = db.DeduciblePieza(spinnerPiezaPoE.getSelectedItem().toString(), "posterior");
                        //daño
                        db.insertarValor(Integer.parseInt(id_inspeccion), Integer.parseInt(dañosDedu[0][0]), String.valueOf(db.obtenerDanio(spinnerDanoPoE.getSelectedItem().toString())));
                        //deducible
                        db.insertarValor(Integer.parseInt(id_inspeccion), Integer.parseInt(dañosDedu[0][1]), db.obtenerDeducible(db.obtenerDanio(spinnerDanoPoE.getSelectedItem().toString()), spinnerDeduciblePoE.getSelectedItem().toString()));
                        //db.insertarValor(Integer.parseInt(id_inspeccion), Integer.parseInt(dañosDedu[0][1]),  spinnerDeduciblePoE.getSelectedItem().toString());


                        comentarioDañoImg = spinnerPiezaPoE.getSelectedItem().toString() + ' ' + spinnerDanoPoE.getSelectedItem().toString() + ' ' + spinnerDeduciblePoE.getSelectedItem().toString() + ' ';
                        db.insertarComentarioFoto(Integer.parseInt(id_inspeccion), comentarioDañoImg, "posterior");
                        String comentarito = db.comentarioFoto(Integer.parseInt(id_inspeccion), "posterior");

                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, comentarito, 0, imagenDano, 0);

                        imagenDano = "data:image/jpg;base64,"+imagenDano;
                        String base64Image14 = imagenDano.split(",")[1];
                        byte[] decodedString14 = Base64.decode(base64Image14, Base64.DEFAULT);
                        Bitmap decodedByte14 = BitmapFactory.decodeByteArray(decodedString14, 0, decodedString14.length);
                        mSetImage.setImageBitmap(decodedByte14);

                        Intent servis = new Intent(Posterior.this, TransferirFoto.class);
                        servis.putExtra("comentario", comentarito);
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        int cantFotoS=db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Danio Posterior");
                        cantFotoS = cantFotoS + 1;
                        db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Foto Danio Posterior",cantFotoS);
                        contPostS.setText(String.valueOf(cantFotoS));

                        break;

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


                        Bitmap bitmapPosterio = BitmapFactory.decodeFile(mPath);
                        bitmapPosterio = foto.redimensiomarImagen(bitmapPosterio);
                        String imagenPosterio = foto.convertirImagenDano(bitmapPosterio);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Posterior", 0, imagenPosterio, 0);
                        imagenPosterio = "data:image/jpg;base64,"+imagenPosterio;
                        String base64Image = imagenPosterio.split(",")[1];
                        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                        Bitmap decodedByte  = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imageViewFotoPoE.setImageBitmap(decodedByte);


                        //servis = new Intent(Posterior.this, TransferirFotoV2.class);
                        servis = new Intent(Posterior.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Posterior");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        int cantFoto=db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Posterior");
                        cantFoto= cantFoto +1;
                        db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Foto Posterior",cantFoto);
                        contPost.setText(String.valueOf(cantFoto));

                        break;
                    case TAKE_LUNETA:
                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });


                        Bitmap bitmapLuneta = BitmapFactory.decodeFile(mPath);
                        bitmapLuneta = foto.redimensiomarImagen(bitmapLuneta);
                        String imagenLuneta = foto.convertirImagenDano(bitmapLuneta);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Logo Luneta Posterior", 0, imagenLuneta, 0);

                        imagenLuneta = "data:image/jpg;base64,"+imagenLuneta;
                        String base64Image1 = imagenLuneta.split(",")[1];
                        byte[] decodedString1 = Base64.decode(base64Image1, Base64.DEFAULT);
                        Bitmap decodedByte1  = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.length);
                        imageLogoLunetaE.setImageBitmap(decodedByte1);

                        servis = new Intent(Posterior.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Logo Luneta Posterior");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        int cantFoto2=db.cantidadF(Integer.parseInt(id_inspeccion),"Logo Luneta Posterior");
                        cantFoto2 = cantFoto2 + 1;
                        db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Logo Luneta Posterior",cantFoto2);
                        contPost2.setText(String.valueOf(cantFoto2));

                        break;
                    case TAKE_ADICIONAL:
                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });

                        Bitmap bitmapAdicional = BitmapFactory.decodeFile(mPath);
                        bitmapAdicional = foto.redimensiomarImagen(bitmapAdicional);

                        String imagenAdicional = foto.convertirImagenDano(bitmapAdicional);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Adicional Posterior", 0, imagenAdicional, 0);
                        imagenAdicional   = "data:image/jpg;base64,"+imagenAdicional;
                        String base64Image2 = imagenAdicional.split(",")[1];
                        byte[] decodedString2 = Base64.decode(base64Image2, Base64.DEFAULT);
                        Bitmap decodedByte2  = BitmapFactory.decodeByteArray(decodedString2, 0, decodedString2.length);
                        imageFotoAdicionalE.setImageBitmap(decodedByte2);

                        servis = new Intent(Posterior.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Adicional Posterior");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        int cantFoto3=db.cantidadF(Integer.parseInt(id_inspeccion),"Adicional Posterior");
                        cantFoto3 = cantFoto3 + 1;
                        db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Adicional Posterior",cantFoto3);
                        contPost3.setText(String.valueOf(cantFoto3));

                        break;
                    case TAKE_SENSORES:

                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });

                        Bitmap bitmapSensores = BitmapFactory.decodeFile(mPath);
                        bitmapSensores = foto.redimensiomarImagen(bitmapSensores);
                        String imagenSensores = foto.convertirImagenDano(bitmapSensores);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Sensores Posteriores", 0, imagenSensores, 0);
                        db.insertarValor(Integer.parseInt(id_inspeccion), 317, "Ok");
                        imagenSensores = "data:image/jpg;base64,"+imagenSensores;
                        String base64Image3 = imagenSensores.split(",")[1];
                        byte[] decodedString3 = Base64.decode(base64Image3, Base64.DEFAULT);
                        Bitmap decodedByte3  = BitmapFactory.decodeByteArray(decodedString3, 0, decodedString3.length);
                        imageSensores.setImageBitmap(decodedByte3);

                        servis = new Intent(Posterior.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Sensores Posteriores");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        break;
                    case TAKE_CAMARA:
                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });


                        Bitmap bitmapCamara = BitmapFactory.decodeFile(mPath);
                        bitmapCamara = foto.redimensiomarImagen(bitmapCamara);

                        String imagenCamara = foto.convertirImagenDano(bitmapCamara);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Camara Posterior", 0, imagenCamara, 0);
                        db.insertarValor(Integer.parseInt(id_inspeccion), 314, "Ok");
                        imagenCamara = "data:image/jpg;base64,"+imagenCamara;
                        String base64Image4 = imagenCamara.split(",")[1];
                        byte[] decodedString4 = Base64.decode(base64Image4, Base64.DEFAULT);
                        Bitmap decodedByte4  = BitmapFactory.decodeByteArray(decodedString4, 0, decodedString4.length);
                        imageCameraPoE.setImageBitmap(decodedByte4);

                        servis = new Intent(Posterior.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Camara Posterior");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);



                        break;
                    case TAKE_COCO:
                        Log.i("cc","paseee" );
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
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Coco Posterior", 0, imagenCoco, 0);
                        db.insertarValor(Integer.parseInt(id_inspeccion), 273, "Ok");
                        imagenCoco = "data:image/jpg;base64,"+imagenCoco;
                        String base64Image5 = imagenCoco.split(",")[1];
                        byte[] decodedString5 = Base64.decode(base64Image5, Base64.DEFAULT);
                        Bitmap decodedByte5  = BitmapFactory.decodeByteArray(decodedString5, 0, decodedString5.length);
                        imageCocoPoE.setImageBitmap(decodedByte5);


                        servis = new Intent(Posterior.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Coco Posterior");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        break;

                    case TAKE_MUELA:
                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });

                        Bitmap bitmapMuela = BitmapFactory.decodeFile(mPath);
                        bitmapMuela = foto.redimensiomarImagen(bitmapMuela);

                        String imagenMuela = foto.convertirImagenDano(bitmapMuela);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Muela Posterior", 0, imagenMuela, 0);
                        db.insertarValor(Integer.parseInt(id_inspeccion), 275, "Ok");
                        imagenMuela = "data:image/jpg;base64,"+imagenMuela;
                        String base64Image6 = imagenMuela.split(",")[1];
                        byte[] decodedString6 = Base64.decode(base64Image6, Base64.DEFAULT);
                        Bitmap decodedByte6 = BitmapFactory.decodeByteArray(decodedString6, 0, decodedString6.length);
                        imageMuelaE.setImageBitmap(decodedByte6);


                        servis = new Intent(Posterior.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Muela Posterior");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        break;
                    case TAKE_REMOLQUE:
                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });

                        Bitmap bitmapRemolque = BitmapFactory.decodeFile(mPath);
                        bitmapRemolque = foto.redimensiomarImagen(bitmapRemolque);

                        String imagenRemolque = foto.convertirImagenDano(bitmapRemolque);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Enchufe Remolque Posterior", 0, imagenRemolque, 0);
                        db.insertarValor(Integer.parseInt(id_inspeccion), 274, "Ok");
                        imagenRemolque = "data:image/jpg;base64,"+imagenRemolque;
                        String base64Image7 = imagenRemolque.split(",")[1];
                        byte[] decodedString7 = Base64.decode(base64Image7, Base64.DEFAULT);
                        Bitmap decodedByte7 = BitmapFactory.decodeByteArray(decodedString7, 0, decodedString7.length);
                        imageenChufeRemolque.setImageBitmap(decodedByte7);

                        servis = new Intent(Posterior.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Enchufe Remolque Posterior");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        break;

                    //accesorios faltantes

                    case TAKE_CREFRI:
                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });

                        Bitmap bitmapCrefrigerada = BitmapFactory.decodeFile(mPath);
                        bitmapCrefrigerada = foto.redimensiomarImagen(bitmapCrefrigerada);

                        String imagenCrefrigerada = foto.convertirImagenDano(bitmapCrefrigerada);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Camara refrigerada", 0, imagenCrefrigerada, 0);
                        db.insertarValor(Integer.parseInt(id_inspeccion), 331, "Ok");
                        imagenCrefrigerada = "data:image/jpg;base64,"+imagenCrefrigerada;
                        String base64Image8 = imagenCrefrigerada.split(",")[1];
                        byte[] decodedString8 = Base64.decode(base64Image8, Base64.DEFAULT);
                        Bitmap decodedByte8 = BitmapFactory.decodeByteArray(decodedString8, 0, decodedString8.length);
                        imageCamRePoE.setImageBitmap(decodedByte8);


                        servis = new Intent(Posterior.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Camara refrigerada");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        break;

                    case TAKE_EQFRIO:
                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });

                        Bitmap bitmapEquipoFrio = BitmapFactory.decodeFile(mPath);
                        bitmapEquipoFrio = foto.redimensiomarImagen(bitmapEquipoFrio);

                        String imagenEQuipoFrio = foto.convertirImagenDano(bitmapEquipoFrio);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Equipo frio", 0, imagenEQuipoFrio, 0);
                        db.insertarValor(Integer.parseInt(id_inspeccion), 346, "Ok");
                        imagenEQuipoFrio = "data:image/jpg;base64,"+imagenEQuipoFrio;
                        String base64Image9 = imagenEQuipoFrio.split(",")[1];
                        byte[] decodedString9 = Base64.decode(base64Image9, Base64.DEFAULT);
                        Bitmap decodedByte9 = BitmapFactory.decodeByteArray(decodedString9, 0, decodedString9.length);
                        imageEquipoE.setImageBitmap(decodedByte9);


                        servis = new Intent(Posterior.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Equipo frio");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        break;


                    case TAKE_CUBREPICK:
                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });

                        Bitmap bitmapCubrePick = BitmapFactory.decodeFile(mPath);
                        bitmapCubrePick = foto.redimensiomarImagen(bitmapCubrePick);
                        String imagenCubrepic = foto.convertirImagenDano(bitmapCubrePick);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Cubre pick up", 0, imagenCubrepic, 0);
                        db.insertarValor(Integer.parseInt(id_inspeccion), 305, "Ok");
                        imagenCubrepic = "data:image/jpg;base64,"+imagenCubrepic;
                        String base64Image10 = imagenCubrepic.split(",")[1];
                        byte[] decodedString10 = Base64.decode(base64Image10, Base64.DEFAULT);
                        Bitmap decodedByte10 = BitmapFactory.decodeByteArray(decodedString10, 0, decodedString10.length);
                        imageCubrePickPoE.setImageBitmap(decodedByte10);


                        servis = new Intent(Posterior.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Cubre pick up");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        break;


                    case TAKE_TAPARIGIDA:

                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });

                        Bitmap bitmapTaparig = BitmapFactory.decodeFile(mPath);
                        bitmapTaparig = foto.redimensiomarImagen(bitmapTaparig);

                        String imagenTaparig = foto.convertirImagenDano(bitmapTaparig);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Tapa rigida", 0, imagenTaparig, 0);
                        db.insertarValor(Integer.parseInt(id_inspeccion), 307, "Ok");
                        imagenTaparig = "data:image/jpg;base64,"+imagenTaparig;
                        String base64Image11 = imagenTaparig.split(",")[1];
                        byte[] decodedString11 = Base64.decode(base64Image11, Base64.DEFAULT);
                        Bitmap decodedByte11 = BitmapFactory.decodeByteArray(decodedString11, 0, decodedString11.length);
                        imageTapaRPoE.setImageBitmap(decodedByte11);


                        servis = new Intent(Posterior.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Tapa rigida");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        break;

                    case TAKE_LONACUBRE:

                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });

                        Bitmap bitmapLonacubre = BitmapFactory.decodeFile(mPath);
                        bitmapLonacubre = foto.redimensiomarImagen(bitmapLonacubre);

                        String imagenLonacubre = foto.convertirImagenDano(bitmapLonacubre);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Lona cubre", 0, imagenLonacubre, 0);
                        db.insertarValor(Integer.parseInt(id_inspeccion), 306, "Ok");
                        imagenLonacubre = "data:image/jpg;base64,"+imagenLonacubre;
                        String base64Image12 = imagenLonacubre.split(",")[1];
                        byte[] decodedString12 = Base64.decode(base64Image12, Base64.DEFAULT);
                        Bitmap decodedByte12 = BitmapFactory.decodeByteArray(decodedString12, 0, decodedString12.length);
                        imageLonaCPoE.setImageBitmap(decodedByte12);


                        servis = new Intent(Posterior.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Lona cubre");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        break;

                    case TAKE_HERRAMIENTA:

                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });

                        Bitmap bitmapHarramienta = BitmapFactory.decodeFile(mPath);
                        bitmapHarramienta = foto.redimensiomarImagen(bitmapHarramienta);

                        String imagenHerramienta = foto.convertirImagenDano(bitmapHarramienta);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "caja herramienta", 0, imagenHerramienta, 0);
                        db.insertarValor(Integer.parseInt(id_inspeccion), 310, "Ok");
                        imagenHerramienta = "data:image/jpg;base64,"+imagenHerramienta;
                        String base64Image13 = imagenHerramienta.split(",")[1];
                        byte[] decodedString13 = Base64.decode(base64Image13, Base64.DEFAULT);
                        Bitmap decodedByte13 = BitmapFactory.decodeByteArray(decodedString13, 0, decodedString13.length);
                        imageCajaHerrPoE.setImageBitmap(decodedByte13);


                        servis = new Intent(Posterior.this, TransferirFoto.class);
                        servis.putExtra("comentario", "caja herramienta");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        break;
                    case TAKE_SISTEMA_ARRASTRE:
                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });

                        Bitmap bitmapArrastre = BitmapFactory.decodeFile(mPath);
                        bitmapArrastre = foto.redimensiomarImagen(bitmapArrastre);

                        String imagenSistemaArrastre = foto.convertirImagenDano(bitmapArrastre);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Sistema Arrastre", 0, imagenSistemaArrastre, 0);
                        imagenSistemaArrastre   = "data:image/jpg;base64,"+imagenSistemaArrastre;
                        String base64ImageA = imagenSistemaArrastre.split(",")[1];
                        byte[] decodedStringA = Base64.decode(base64ImageA, Base64.DEFAULT);
                        Bitmap decodedByteA  = BitmapFactory.decodeByteArray(decodedStringA, 0, decodedStringA.length);
                        imageViewSistArrastre.setImageBitmap(decodedByteA);

                        servis = new Intent(Posterior.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Foto Sistema Arrastre");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                       int cantFotoA=db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Sistema Arrastre");
                        cantFotoA = cantFotoA + 1;
                        db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Foto Sistema Arrastre",cantFotoA);
                        contPostA.setText(String.valueOf(cantFotoA));

                        break;
                }
            }
        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }

    }



    private  void DesplegarCamposSeccionUno(final String id)    {

        if (btnPosteriorE.getVisibility()==View.VISIBLE)
        {
            btnPosteriorE.setVisibility(View.GONE);
            imageViewFotoPoE.setVisibility(View.GONE);
            imageViewFotoPoE.setImageBitmap(null);
            btnLogoLunetaE.setVisibility(View.GONE);
            imageLogoLunetaE.setVisibility(View.GONE);
            imageLogoLunetaE.setImageBitmap(null);
            btnFotoAdiocionalE.setVisibility(View.GONE);
            imageFotoAdicionalE.setVisibility(View.GONE);
            imageFotoAdicionalE.setImageBitmap(null);
            textCant1.setVisibility(View.GONE);
            contPost.setVisibility(View.GONE);
            textCant2.setVisibility(View.GONE);
            contPost2.setVisibility(View.GONE);
            textCant3.setVisibility(View.GONE);
            contPost3.setVisibility(View.GONE);

        }
        else
        {

            //seccion dos

            txtPieza.setVisibility(View.GONE);
            txtTipoDanoE.setVisibility(View.GONE);
            txtDeducibleE.setVisibility(View.GONE);
            spinnerPiezaPoE.setVisibility(View.GONE);
            spinnerDanoPoE.setVisibility(View.GONE);
            spinnerDeduciblePoE.setVisibility(View.GONE);
            btnFotoDanoE.setVisibility(View.GONE);
            mSetImage.setVisibility(View.GONE);
            mSetImage.setImageBitmap(null);
            textCantS.setVisibility(View.GONE);
            contPostS.setVisibility(View.GONE);

            //seccion tres
            sensoresPoE.setVisibility(View.GONE);
            imageSensores.setVisibility(View.GONE);
            imageSensores.setImageBitmap(null);
            camaraPoE.setVisibility(View.GONE);
            imageCameraPoE.setVisibility(View.GONE);
            imageCameraPoE.setImageBitmap(null);
            cocoPoE.setVisibility(View.GONE);
            //imageCocoPoE.setVisibility(View.GONE);
            //imageCocoPoE.setImageBitmap(null);
            muelaPoE.setVisibility(View.GONE);
            //imageMuelaE.setVisibility(View.GONE);
            //imageMuelaE.setImageBitmap(null);
            enchufeRemolque.setVisibility(View.GONE);
            //imageenChufeRemolque.setVisibility(View.GONE);
            //imageenChufeRemolque.setImageBitmap(null);
            btnSistArrastre.setVisibility(View.GONE);
            imageViewSistArrastre.setVisibility(View.GONE);
            imageViewSistArrastre.setImageBitmap(null);
            textArrastre.setVisibility(View.GONE);
            textCantA.setVisibility(View.GONE);
            contPostA.setVisibility(View.GONE);

            //Seccion tres mq
            equipoPoE.setVisibility(View.GONE);
            imageEquipoE.setImageBitmap(null);
            //camaraRefriPoE.setVisibility(View.GONE);
            //imageCamRePoE.setImageBitmap(null);
            cubrePickPoE.setVisibility(View.GONE);
            imageCubrePickPoE.setImageBitmap(null);
            tapaRiPoE.setVisibility(View.GONE);
            imageTapaRPoE.setImageBitmap(null);
            LonaCubrePoE.setVisibility(View.GONE);
            imageLonaCPoE.setImageBitmap(null);
            cajaHerrPoE.setVisibility(View.GONE);
            imageCajaHerrPoE.setImageBitmap(null);
            linea.setVisibility(View.GONE);

            String imagenPosterior = db.foto(Integer.parseInt(id),"Posterior");
            String imagenLogoLuneta = db.foto(Integer.parseInt(id),"Logo Luneta Posterior");
            String imagenAdicional = db.foto(Integer.parseInt(id),"Adicional Posterior");
            String imagenDanioPosterior = db.foto(Integer.parseInt(id),"Foto Danio Posterior");

            if(imagenPosterior.length()>=3 )
            {
                byte[] decodedString = Base64.decode(imagenPosterior, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageViewFotoPoE.setImageBitmap(decodedByte);



            }
            if(imagenLogoLuneta.length()>=3 ) {

                byte[] decodedString = Base64.decode(imagenLogoLuneta, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageLogoLunetaE.setImageBitmap(decodedByte);
            }
            if(imagenAdicional.length()>=3 )
            {
                byte[] decodedString = Base64.decode(imagenAdicional, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageFotoAdicionalE.setImageBitmap(decodedByte);
            }


            btnPosteriorE.setVisibility(View.VISIBLE);
            imageViewFotoPoE.setVisibility(View.VISIBLE);
            textCant1.setVisibility(View.VISIBLE);
            contPost.setVisibility(View.VISIBLE);
            textCant2.setVisibility(View.VISIBLE);
            contPost2.setVisibility(View.VISIBLE);
            textCant3.setVisibility(View.VISIBLE);
            contPost3.setVisibility(View.VISIBLE);




            btnLogoLunetaE.setVisibility(View.VISIBLE);
            imageLogoLunetaE.setVisibility(View.VISIBLE);

            btnFotoAdiocionalE.setVisibility(View.VISIBLE);
            imageFotoAdicionalE.setVisibility(View.VISIBLE);

        }
    }

    private  void desplegarCamposSeccionDos(String id)    {

        if (txtPieza.getVisibility() == View.VISIBLE) {

            txtPieza.setVisibility(View.GONE);
            txtTipoDanoE.setVisibility(View.GONE);
            txtDeducibleE.setVisibility(View.GONE);
            spinnerPiezaPoE.setVisibility(View.GONE);
            spinnerDanoPoE.setVisibility(View.GONE);
            spinnerDeduciblePoE.setVisibility(View.GONE);
            btnFotoDanoE.setVisibility(View.GONE);
            mSetImage.setVisibility(View.GONE);
            mSetImage.setImageBitmap(null);
            textCantS.setVisibility(View.GONE);
            contPostS.setVisibility(View.GONE);
        }
        else
        {
            //seccion uno
            btnPosteriorE.setVisibility(View.GONE);
            imageViewFotoPoE.setVisibility(View.GONE);
            imageViewFotoPoE.setImageBitmap(null);
            btnLogoLunetaE.setVisibility(View.GONE);
            imageLogoLunetaE.setVisibility(View.GONE);
            imageLogoLunetaE.setImageBitmap(null);
            btnFotoAdiocionalE.setVisibility(View.GONE);
            imageFotoAdicionalE.setVisibility(View.GONE);
            imageFotoAdicionalE.setImageBitmap(null);
            textCant1.setVisibility(View.GONE);
            contPost.setVisibility(View.GONE);
            textCant2.setVisibility(View.GONE);
            contPost2.setVisibility(View.GONE);
            textCant3.setVisibility(View.GONE);
            contPost3.setVisibility(View.GONE);

            //seccion tres
            sensoresPoE.setVisibility(View.GONE);
            imageSensores.setVisibility(View.GONE);
            imageSensores.setImageBitmap(null);
            camaraPoE.setVisibility(View.GONE);
            imageCameraPoE.setVisibility(View.GONE);
            imageCameraPoE.setImageBitmap(null);
            cocoPoE.setVisibility(View.GONE);
            //imageCocoPoE.setVisibility(View.GONE);
            //imageCocoPoE.setImageBitmap(null);
            muelaPoE.setVisibility(View.GONE);
            //imageMuelaE.setVisibility(View.GONE);
            //imageMuelaE.setImageBitmap(null);
            enchufeRemolque.setVisibility(View.GONE);
           //imageenChufeRemolque.setVisibility(View.GONE);
            //imageenChufeRemolque.setImageBitmap(null);
            textArrastre.setVisibility(View.GONE);
            btnSistArrastre.setVisibility(View.GONE);
            imageViewSistArrastre.setVisibility(View.GONE);
            imageViewSistArrastre.setImageBitmap(null);
            textCantA.setVisibility(View.GONE);
            contPostA.setVisibility(View.GONE);

            //Seccion tres mq
            equipoPoE.setVisibility(View.GONE);
            imageEquipoE.setImageBitmap(null);
            //camaraRefriPoE.setVisibility(View.GONE);
            //imageCamRePoE.setImageBitmap(null);
            cubrePickPoE.setVisibility(View.GONE);
            imageCubrePickPoE.setImageBitmap(null);
            tapaRiPoE.setVisibility(View.GONE);
            imageTapaRPoE.setImageBitmap(null);
            LonaCubrePoE.setVisibility(View.GONE);
            imageLonaCPoE.setImageBitmap(null);
            cajaHerrPoE.setVisibility(View.GONE);
            imageCajaHerrPoE.setImageBitmap(null);
            linea.setVisibility(View.GONE);

            String imagenDanoPosterior = db.foto(Integer.parseInt(id),db.comentarioFoto(Integer.parseInt(id),"posterior"));

           if(imagenDanoPosterior.length()>=3 )
            {
                byte[] decodedString = Base64.decode(imagenDanoPosterior, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                mSetImage.setImageBitmap(decodedByte);
            }

            ///////SPINNERPIEZA POSTERIOR
            spinnerPiezaPoE = (Spinner) findViewById(R.id.spinnerPiezaPoE);
            String listapieza[][] =db.listaPiezasPosterior();
            String[] listapiezaPosterior = new String[listapieza.length +1 ];
            listapiezaPosterior[0]="Seleccione";
            for (int i = 0; i < listapieza.length; i++) {
                listapiezaPosterior[i+1] = listapieza[i][0];
            }
            ArrayAdapter<String> adapterPosteior = new ArrayAdapter<String>(Posterior.this, android.R.layout.simple_spinner_item, listapiezaPosterior);
            adapterPosteior.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerPiezaPoE.setAdapter(adapterPosteior);
            spinnerPiezaPoE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position != 0) {
                        ///////SPINNERDAÑO POSTERIOR
                        spinnerDanoPoE = (Spinner) findViewById(R.id.spinnerDanoPoE);
                        txtTipoDanoE.setVisibility(View.VISIBLE);
                        spinnerDanoPoE.setVisibility(View.VISIBLE);

                        String listaDano[][] = db.listaDanoPosterior();
                        String[] listaDanoPosterior = new String[listaDano.length+1];
                        listaDanoPosterior[0]="Seleccione";
                        for (int i = 0; i < listaDano.length; i++) {
                            listaDanoPosterior[i+1] = listaDano[i][0];
                        }
                        ArrayAdapter<String> adapterDanoPosterior = new ArrayAdapter<String>(Posterior.this, android.R.layout.simple_spinner_item, listaDanoPosterior);
                        adapterDanoPosterior.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerDanoPoE.setAdapter(adapterDanoPosterior);
                        spinnerDanoPoE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if(i !=0) { // 6 -> faltante
                                    String[][] listadedu = db.listaDeduciblesPosterior(spinnerDanoPoE.getSelectedItem().toString());
                                    final Spinner spinnerDeduciblePoE = (Spinner) findViewById(R.id.spinnerDeduciblePoE);
                                    txtDeducibleE.setVisibility(View.VISIBLE);
                                    spinnerDeduciblePoE.setVisibility(View.VISIBLE);
                                    String[] spinnerDedu = new String[listadedu.length+1];
                                    spinnerDedu[0]="Seleccione";
                                    for (int ii = 0; ii < listadedu.length; ii++) {
                                        spinnerDedu[ii + 1] = listadedu[ii][0];
                                    }

                                    ArrayAdapter<String> adapterdedu = new ArrayAdapter<String>(Posterior.this, android.R.layout.simple_spinner_item, spinnerDedu);
                                    adapterdedu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinnerDeduciblePoE.setAdapter(adapterdedu);
                                    spinnerDeduciblePoE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                            btnFotoDanoE.setVisibility(View.VISIBLE);
                                            textCantS.setVisibility(View.VISIBLE);
                                            contPostS.setVisibility(View.VISIBLE);

                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {
                                        }
                                    });
                                }
                                /*}else if(i == 6){
                                    btnFotoDanoE.setVisibility(View.VISIBLE);
                                }*/
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



            txtPieza.setVisibility(View.VISIBLE);
            spinnerPiezaPoE.setVisibility(View.VISIBLE);
            mSetImage.setVisibility(View.VISIBLE);

        }
    }

    private void desplegarCamposSeccionTres(String id)    {
        if (sensoresPoE.getVisibility() == View.VISIBLE) {

            sensoresPoE.setVisibility(View.GONE);
            imageSensores.setVisibility(View.GONE);
            imageSensores.setImageBitmap(null);
            camaraPoE.setVisibility(View.GONE);
            imageCameraPoE.setVisibility(View.GONE);
            imageCameraPoE.setImageBitmap(null);
            cocoPoE.setVisibility(View.GONE);
            //imageCocoPoE.setVisibility(View.GONE);
            //imageCocoPoE.setImageBitmap(null);
            muelaPoE.setVisibility(View.GONE);
            //imageMuelaE.setVisibility(View.GONE);
            //imageMuelaE.setImageBitmap(null);
            enchufeRemolque.setVisibility(View.GONE);
            //imageenChufeRemolque.setVisibility(View.GONE);
            //imageenChufeRemolque.setImageBitmap(null);
            textArrastre.setVisibility(View.GONE);
            btnSistArrastre.setVisibility(View.GONE);
            imageViewSistArrastre.setVisibility(View.GONE);
            imageViewSistArrastre.setImageBitmap(null);
            textCantA.setVisibility(View.GONE);
            contPostA.setVisibility(View.GONE);
        }
        else
        {
            //seccion uno
            btnPosteriorE.setVisibility(View.GONE);
            imageViewFotoPoE.setVisibility(View.GONE);
            imageViewFotoPoE.setImageBitmap(null);
            btnLogoLunetaE.setVisibility(View.GONE);
            imageLogoLunetaE.setVisibility(View.GONE);
            imageLogoLunetaE.setImageBitmap(null);
            btnFotoAdiocionalE.setVisibility(View.GONE);
            imageFotoAdicionalE.setVisibility(View.GONE);
            imageFotoAdicionalE.setImageBitmap(null);
            textCant1.setVisibility(View.GONE);
            contPost.setVisibility(View.GONE);
            textCant2.setVisibility(View.GONE);
            contPost2.setVisibility(View.GONE);
            textCant3.setVisibility(View.GONE);
            contPost3.setVisibility(View.GONE);

            //seccion dos

            txtPieza.setVisibility(View.GONE);
            txtTipoDanoE.setVisibility(View.GONE);
            txtDeducibleE.setVisibility(View.GONE);
            spinnerPiezaPoE.setVisibility(View.GONE);
            spinnerDanoPoE.setVisibility(View.GONE);
            spinnerDeduciblePoE.setVisibility(View.GONE);
            btnFotoDanoE.setVisibility(View.GONE);
            mSetImage.setVisibility(View.GONE);
            mSetImage.setImageBitmap(null);
            textCantS.setVisibility(View.GONE);
            contPostS.setVisibility(View.GONE);

            //Seccion tres mq
            equipoPoE.setVisibility(View.GONE);
            imageEquipoE.setImageBitmap(null);
            //camaraRefriPoE.setVisibility(View.GONE);
            //imageCamRePoE.setImageBitmap(null);
            cubrePickPoE.setVisibility(View.GONE);
            imageCubrePickPoE.setImageBitmap(null);
            tapaRiPoE.setVisibility(View.GONE);
            imageTapaRPoE.setImageBitmap(null);
            LonaCubrePoE.setVisibility(View.GONE);
            imageLonaCPoE.setImageBitmap(null);
            cajaHerrPoE.setVisibility(View.GONE);
            imageCajaHerrPoE.setImageBitmap(null);
            linea.setVisibility(View.GONE);


            String imagenSensores = db.foto(Integer.parseInt(id),"Sensores Posteriores");
            String imagenCamara = db.foto(Integer.parseInt(id),"Camara Posterior");
            /*String imagenCoco = db.foto(Integer.parseInt(id),"Coco Posterior");
            String imagenMuela = db.foto(Integer.parseInt(id),"Muela Posterior");
            String imagenenChufeRemolqu = db.foto(Integer.parseInt(id),"Enchufe Remolque Posterior");*/
            String imagenSistemaArrastre = db.foto(Integer.parseInt(id),"Foto Sistema Arrastre");

            String imagenEquipoFrio = db.foto(Integer.parseInt(id),"Equipo frio");
            String imagenCamaraRefrigerada = db.foto(Integer.parseInt(id),"Camara refrigerada");
            String imagenCubrepickup = db.foto(Integer.parseInt(id),"Cubre pick up");
            String imagenTapaRigida = db.foto(Integer.parseInt(id),"Tapa rigida");
            String imagenLonaCubre = db.foto(Integer.parseInt(id),"Lona cubre");
            String imagenCajaHerramientas = db.foto(Integer.parseInt(id),"caja herramienta");



            if(imagenSensores.length()>=3 )
            {


                byte[] decodedString = Base64.decode(imagenSensores, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageSensores.setImageBitmap(decodedByte);
                sensoresPoE.setChecked(true);
            }
            if(imagenCamara.length()>=3 )
            {
                byte[] decodedString = Base64.decode(imagenCamara, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageCameraPoE.setImageBitmap(decodedByte);
                camaraPoE.setChecked(true);
            }

            if(imagenSistemaArrastre.length()>=3 )
            {
                byte[] decodedString = Base64.decode(imagenSistemaArrastre, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageViewSistArrastre.setImageBitmap(decodedByte);
                btnSistArrastre.setVisibility(View.VISIBLE);
                imageViewSistArrastre.setVisibility(View.VISIBLE);
                textCantA.setVisibility(View.VISIBLE);
                contPostA.setVisibility(View.VISIBLE);
               // cocoPoE.setChecked(true);
                //muelaPoE.setChecked(true);
                //enchufeRemolque.setChecked(true);

            }

            if(imagenCajaHerramientas.length()>=3)
            {
                byte[] decodedString = Base64.decode(imagenCajaHerramientas, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageCajaHerrPoE.setImageBitmap(decodedByte);
                cajaHerrPoE.setChecked(true);
            }
            if(imagenLonaCubre.length()>=3)
            {
                byte[] decodedString = Base64.decode(imagenLonaCubre, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageLonaCPoE.setImageBitmap(decodedByte);
                LonaCubrePoE.setChecked(true);
            }
            if(imagenTapaRigida.length()>=3)
            {
                byte[] decodedString = Base64.decode(imagenTapaRigida, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageTapaRPoE.setImageBitmap(decodedByte);
                tapaRiPoE.setChecked(true);
            }
            if(imagenCubrepickup.length()>=3)
            {
                byte[] decodedString = Base64.decode(imagenCubrepickup, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageCubrePickPoE.setImageBitmap(decodedByte);
                cubrePickPoE.setChecked(true);
            }
            /*if(imagenCamaraRefrigerada.length()>=3)
            {
                byte[] decodedString = Base64.decode(imagenCamaraRefrigerada, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageCamRePoE.setImageBitmap(decodedByte);
                camaraRefriPoE.setChecked(true);
            }*/
            if(imagenEquipoFrio.length()>=3)
            {
                byte[] decodedString = Base64.decode(imagenEquipoFrio, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageEquipoE.setImageBitmap(decodedByte);
                equipoPoE.setChecked(true);
            }

           /* if(imagenCoco.length()>=3 )
            {
                byte[] decodedString = Base64.decode(imagenCoco, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageCocoPoE.setImageBitmap(decodedByte);
                cocoPoE.setChecked(true);
            }

            if(imagenMuela.length()>=3)
            {
                byte[] decodedString = Base64.decode(imagenMuela, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageMuelaE.setImageBitmap(decodedByte);
                muelaPoE.setChecked(true);
            }
            if(imagenenChufeRemolqu.length()>=3)
            {
                byte[] decodedString = Base64.decode(imagenenChufeRemolqu, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageenChufeRemolque.setImageBitmap(decodedByte);
                enchufeRemolque.setChecked(true);
            }*/

            sensoresPoE.setVisibility(View.VISIBLE);
            imageSensores.setVisibility(View.VISIBLE);
            camaraPoE.setVisibility(View.VISIBLE);
            imageCameraPoE.setVisibility(View.VISIBLE);
            cocoPoE.setVisibility(View.VISIBLE);
           // imageCocoPoE.setVisibility(View.VISIBLE);
            muelaPoE.setVisibility(View.VISIBLE);
           // imageMuelaE.setVisibility(View.VISIBLE);
            enchufeRemolque.setVisibility(View.VISIBLE);
            //imageenChufeRemolque.setVisibility(View.VISIBLE);
            textArrastre.setVisibility(View.VISIBLE);
            imageViewSistArrastre.setVisibility(View.VISIBLE);

            equipoPoE.setVisibility(View.VISIBLE);
            imageEquipoE.setVisibility(View.VISIBLE);
            //camaraRefriPoE.setVisibility(View.VISIBLE);
            //imageCamRePoE.setVisibility(View.VISIBLE);
            cubrePickPoE.setVisibility(View.VISIBLE);
            imageCubrePickPoE.setVisibility(View.VISIBLE);
            tapaRiPoE.setVisibility(View.VISIBLE);
            imageTapaRPoE.setVisibility(View.VISIBLE);
            LonaCubrePoE.setVisibility(View.VISIBLE);
            imageLonaCPoE.setVisibility(View.VISIBLE);
            cajaHerrPoE.setVisibility(View.VISIBLE);
            imageCajaHerrPoE.setVisibility(View.VISIBLE);
            linea.setVisibility(View.VISIBLE);



        }

    }

   private void desplegarCamposSeccionTresMQ(String id){


        if (equipoPoE.getVisibility()==View.VISIBLE){

            equipoPoE.setVisibility(View.GONE);
            imageEquipoE.setImageBitmap(null);
            //camaraRefriPoE.setVisibility(View.GONE);
            //imageCamRePoE.setImageBitmap(null);
            cubrePickPoE.setVisibility(View.GONE);
            imageCubrePickPoE.setImageBitmap(null);
            tapaRiPoE.setVisibility(View.GONE);
            imageTapaRPoE.setImageBitmap(null);
            LonaCubrePoE.setVisibility(View.GONE);
            imageLonaCPoE.setImageBitmap(null);
            cajaHerrPoE.setVisibility(View.GONE);
            imageCajaHerrPoE.setImageBitmap(null);
            linea.setVisibility(View.GONE);

        }else {


            String imagenEquipoFrio = db.foto(Integer.parseInt(id),"Equipo frio");
            String imagenCamaraRefrigerada = db.foto(Integer.parseInt(id),"Camara refrigerada");
            String imagenCubrepickup = db.foto(Integer.parseInt(id),"Cubre pick up");
            String imagenTapaRigida = db.foto(Integer.parseInt(id),"Tapa rigida");
            String imagenLonaCubre = db.foto(Integer.parseInt(id),"Lona cubre");
            String imagenCajaHerramientas = db.foto(Integer.parseInt(id),"caja herramienta");


            if(imagenCajaHerramientas.length()>=3)
            {
                byte[] decodedString = Base64.decode(imagenCajaHerramientas, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageCajaHerrPoE.setImageBitmap(decodedByte);
                cajaHerrPoE.setChecked(true);
            }
            if(imagenLonaCubre.length()>=3)
            {
                byte[] decodedString = Base64.decode(imagenLonaCubre, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageLonaCPoE.setImageBitmap(decodedByte);
                LonaCubrePoE.setChecked(true);
            }
            if(imagenTapaRigida.length()>=3)
            {
                byte[] decodedString = Base64.decode(imagenTapaRigida, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageTapaRPoE.setImageBitmap(decodedByte);
                tapaRiPoE.setChecked(true);
            }
            if(imagenCubrepickup.length()>=3)
            {
                byte[] decodedString = Base64.decode(imagenCubrepickup, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageCubrePickPoE.setImageBitmap(decodedByte);
                cubrePickPoE.setChecked(true);
            }
           /* if(imagenCamaraRefrigerada.length()>=3)
            {
                byte[] decodedString = Base64.decode(imagenCamaraRefrigerada, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageCamRePoE.setImageBitmap(decodedByte);
                camaraRefriPoE.setChecked(true);
            }*/
            if(imagenEquipoFrio.length()>=3)
            {
                byte[] decodedString = Base64.decode(imagenEquipoFrio, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageEquipoE.setImageBitmap(decodedByte);
                equipoPoE.setChecked(true);
            }


            equipoPoE.setVisibility(View.VISIBLE);
            imageEquipoE.setVisibility(View.VISIBLE);
            //camaraRefriPoE.setVisibility(View.VISIBLE);
            //imageCamRePoE.setVisibility(View.VISIBLE);
            cubrePickPoE.setVisibility(View.VISIBLE);
            imageCubrePickPoE.setVisibility(View.VISIBLE);
            tapaRiPoE.setVisibility(View.VISIBLE);
            imageTapaRPoE.setVisibility(View.VISIBLE);
            LonaCubrePoE.setVisibility(View.VISIBLE);
            imageLonaCPoE.setVisibility(View.VISIBLE);
            cajaHerrPoE.setVisibility(View.VISIBLE);
            imageCajaHerrPoE.setVisibility(View.VISIBLE);
            linea.setVisibility(View.VISIBLE);


            //seccion uno
            btnPosteriorE.setVisibility(View.GONE);
            imageViewFotoPoE.setVisibility(View.GONE);
            imageViewFotoPoE.setImageBitmap(null);
            btnLogoLunetaE.setVisibility(View.GONE);
            imageLogoLunetaE.setVisibility(View.GONE);
            imageLogoLunetaE.setImageBitmap(null);
            btnFotoAdiocionalE.setVisibility(View.GONE);
            imageFotoAdicionalE.setVisibility(View.GONE);
            imageFotoAdicionalE.setImageBitmap(null);
            textCant1.setVisibility(View.GONE);
            contPost.setVisibility(View.GONE);
            textCant2.setVisibility(View.GONE);
            contPost2.setVisibility(View.GONE);
            textCant3.setVisibility(View.GONE);
            contPost3.setVisibility(View.GONE);

            //seccion dos

            txtPieza.setVisibility(View.GONE);
            txtTipoDanoE.setVisibility(View.GONE);
            txtDeducibleE.setVisibility(View.GONE);
            spinnerPiezaPoE.setVisibility(View.GONE);
            spinnerDanoPoE.setVisibility(View.GONE);
            spinnerDeduciblePoE.setVisibility(View.GONE);
            btnFotoDanoE.setVisibility(View.GONE);
            mSetImage.setVisibility(View.GONE);
            mSetImage.setImageBitmap(null);
            textCantS.setVisibility(View.GONE);
            contPostS.setVisibility(View.GONE);

            //seccion tres
            sensoresPoE.setVisibility(View.GONE);
            imageSensores.setVisibility(View.GONE);
            imageSensores.setImageBitmap(null);
            camaraPoE.setVisibility(View.GONE);
            imageCameraPoE.setVisibility(View.GONE);
            imageCameraPoE.setImageBitmap(null);
            cocoPoE.setVisibility(View.GONE);
            //imageCocoPoE.setVisibility(View.GONE);
            //imageCocoPoE.setImageBitmap(null);
            muelaPoE.setVisibility(View.GONE);
            //imageMuelaE.setVisibility(View.GONE);
            //imageMuelaE.setImageBitmap(null);
            enchufeRemolque.setVisibility(View.GONE);
            //imageenChufeRemolque.setVisibility(View.GONE);
            //imageenChufeRemolque.setImageBitmap(null);
            textArrastre.setVisibility(View.GONE);
            btnSistArrastre.setVisibility(View.GONE);
            imageViewSistArrastre.setVisibility(View.GONE);
            imageViewSistArrastre.setImageBitmap(null);
            textCantA.setVisibility(View.GONE);
            contPostA.setVisibility(View.GONE);


        }
    }




}
