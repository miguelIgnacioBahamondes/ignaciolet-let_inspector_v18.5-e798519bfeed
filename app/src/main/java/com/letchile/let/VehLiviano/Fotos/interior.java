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
import com.letchile.let.VehLiviano.AudioActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class interior extends AppCompatActivity {

    DBprovider db;
    Boolean connec = false;
    private final int PHOTO_PANEL = 200;
    private final int PHOTO_PANEL_DENTRO = 300;
    private final int PHOTO_RADIO = 400;
    private final int PHOTO_KILOMETRAJE = 500;
    private final int PHOTO_ADICIONAL = 600;
   // private final int PHOTO_CHECK_ENGINE = 700;
    //private final int PHOTO_LUZ_TESTIGO = 800;
    //private final int PHOTO_CONTROL = 900;
    private final int PHOTO_BLUETOOH = 1000;
    private final int PHOTO_TAPIZ = 1100;
    private final int PHOTO_BUTACA = 1200;
    private final int PHOTO_CORTA_CORRIENTE = 1300;
    private final int PHOTO_ALZA_VIDRIO_DE = 1400;
    private final int PHOTO_ALZA_VIDRIO_TR = 1500;
    private final int PHOTO_RETROVISOR = 1600;
    private final int PHOTO_PARLANTES = 1700;
    private final int PHOTO_TWEETER = 1800;
    private final int PHOTO_AMPL1 = 1900;
    private final int PHOTO_AMPL2 = 2000;
    private final int PHOTO_WOOFER = 2100;
    private final int PHOTO_PANTALLA = 2200;
    private final int PHOTO_GPS = 2300;
    private final int PHOTO_LECTOR = 2400;
    private final int PHOTO_MODULAR = 2500;
    private final int PHOTO_OBD = 2600;
    private final int PHOTO_LLAVES = 2700;
    private final int PHOTO_PARABRISAS = 2800;
    private Button btnVolverInteriorE,btnVolverSecInteriorE,btnSiguienteInteriorE,btnPanelFueraInteE,btnPanelDentroInteE,btnRadioInteriorE,btnKilometrajeE,btnAdicionalInteriorE,btnOBDE,btnLLavesE,btnSeccionUnoInterior, btnSeccionTresInterior, btnSeccionTresMQ,btnSeccionOBD,btnLogoParaFrontalE;
    private ImageView imagePanelFueraInteE,imagePanelDentroInteE,imageRadioInteriorE,imageKilometrajeE,imageAdicionalInteriorE,imageLuzCheckEngineE,imageluzTestigoAirE,
            imageControlCruceE,imageBluetoothE,imageTapizCueroE,imageButacaElectE,imageCortaCorriE,imageAlzavidrioDeE,imageAlzavidrioTrE,imageRetroElectE,imageParlantesE,
            imageTweeterE,imageAmplifiUnoE,imageAmplifiDosE,imageWooferE,imagePantallaDvdE,imageGpsE,imageLectorDvdE,imageModularDvd,imageOBDE,imageLLavesE,imageLogoParaE;
    private CheckBox luzCheckEngineE,luzTestigoAirE,controlCruceE,bluetoothE,tapizCueroE,butacaElectE,cortaCorriE,alzavidrioDeE,alzavidrioTrE,retroElectE,parlantesE,tweeterE,
            amplifiUnoE,amplifiDosE,wooferE,pantallaDvdE,gpsE, eletricro,radiotransmisor,apernadoTeewter,apernadoWoofer,apernadoAmplifi1,apernadoAmplifi2,apernadoDvd,apernadoPantalla,apernadoDvdModular,
            anteElectCheck,radioTransCheck,tweeterApernado, checkWoofe,amplifi1check,amplifi2check,checkWoofer, luzTestigoABS ;
    private TextView txtAlzavidrioE,textCant19,contPost19,textCant20,contPost20,textCant21,contPost21,textCant22,contPost22,textCant23,contPost23,contPost24,textCant24,contPost25,textCant25,
            textView36, textView37,textView38,textView42,textView43,textView44,textView45, textView46,textView47,textView48,textView50,textView51,textView52,textView53,textView49,
            textView54,textView55,textView56,textView57,textView58,textView63,textView64,textView65,textView66,textView59,textView67,textView68,textView69,textView70,textView60,textView71,textView72,
            textView73,textView74,textView61,textView75,textView76,textView77,textView78,textView62,textView79,textView80,textView81,textView82,textView39,textViewK,textCant14,contPost14;
    private String mPath;
    private File ruta_sd;
    private String ruta = "";
    private Spinner panel,tipoRadio,tweeter,ubicacion_w,amplificador1,amplificador2,lector,pantallaa,dvde,spinner_panelR,spinner_ubic_w,spinner_amplif1,spinner_amplif2,spinner_lector,
            spinner_pantalla,spinner_dvd;
    private EditText marcaPanel,modeloPanel,parlanteCantidad,parlanteMarca,parlanteModelo,tweeterCantidad,tweeterMarca,tweetermodelo,
            woofercantidad,woofermarca,woofermodelo,ampli1Cantidad,ampli1marca,ampli1modelo,ampli2cantidad,ampli2marca,ampli2modelo,
            dvdCantidad,dvdMarca,dvdModelo,pantallaCantidad,pantallaMarca,pantallaModelo,dvdModularCantidad,dvdModularmarca,dvdModularmodelo, cantidadParlante,cantidadTeewter,
            marcaTeewter,modeloTeewter,cantidadWoofer,cantidadampli1,cantidadampli2,amplimarca2,amplimodelo2,Ekilometraje;
    String nombreimagen = "", eletricros="",radiotransmisors="",apernadoTeewters="",apernadoWoofers="",apernadoAmplifi1s="",
            apernadoAmplifi2s="",apernadoDvds="",apernadoPantallas="",apernadoDvdModulars="", id_inspeccion, parlantesEs="";
    Validaciones validaciones;
    View linea;
    JSONObject llenado;


    int correlativo = 0;

    PropiedadesFoto foto;

    public interior(){db = new DBprovider(this);foto=new PropiedadesFoto(this); validaciones = new Validaciones(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interior);

        Bundle bundle = getIntent().getExtras();
        id_inspeccion = bundle.getString("id_inspeccion");

        connec = new ConexionInternet(this).isConnectingToInternet();

        btnPanelFueraInteE = findViewById(R.id.btnPanelFueraInteE);
        imagePanelFueraInteE = findViewById(R.id.imagePanelFueraInteE);
        btnPanelDentroInteE = findViewById(R.id.btnPanelDentroInteE);
        imagePanelDentroInteE = findViewById(R.id.imagePanelDentroInteE);
        btnRadioInteriorE = findViewById(R.id.btnRadioInteriorE);
        imageRadioInteriorE = findViewById(R.id.imageRadioInteriorE);
        btnKilometrajeE = findViewById(R.id.btnKilometrajeE);
        imageKilometrajeE = findViewById(R.id.imageKilometrajeE);
        btnOBDE = findViewById(R.id.btnOBDE);
        imageOBDE = findViewById(R.id.imageOBDE);
        btnLLavesE = findViewById(R.id.btnLLavesE);
        imageLLavesE = findViewById(R.id.imageLLavesE);
        textViewK = findViewById(R.id.textViewK);
        btnLogoParaFrontalE = findViewById(R.id.btnLogoParaFrontalE);
        imageLogoParaE = findViewById(R.id.imageLogoParaE);


        Ekilometraje = findViewById(R.id.Ekilometraje);
        Ekilometraje.setOnEditorActionListener(new PropiedadesTexto());
        Ekilometraje.setText(db.accesorio(Integer.parseInt(id_inspeccion),296).toString());





        btnAdicionalInteriorE = findViewById(R.id.btnAdicionalInteriorE);
        imageAdicionalInteriorE  = findViewById(R.id.imageAdicionalInteriorE);
        luzCheckEngineE = findViewById(R.id.luzCheckEngineE);
        //imageLuzCheckEngineE = findViewById(R.id.imageLuzCheckEngineE);
        luzTestigoAirE = findViewById(R.id.luzTestigoAirE);
        //imageluzTestigoAirE = findViewById(R.id.imageluzTestigoAirE);
        luzTestigoABS = findViewById(R.id.luzTestigoABS);
        controlCruceE = findViewById(R.id.controlCruceE);
        //imageControlCruceE = findViewById(R.id.imageControlCruceE);
        bluetoothE = findViewById(R.id.bluetoothE);
        linea = findViewById(R.id.linea);
        imageBluetoothE = findViewById(R.id.imageBluetoothE);
        tapizCueroE = findViewById(R.id.tapizCueroE);
        imageTapizCueroE = findViewById(R.id.imageTapizCueroE);
        butacaElectE = findViewById(R.id.butacaElectE);
        imageButacaElectE = findViewById(R.id.imageButacaElectE);
        cortaCorriE = findViewById(R.id.cortaCorriE);
        imageCortaCorriE = findViewById(R.id.imageCortaCorriE);
        alzavidrioDeE = findViewById(R.id.alzavidrioDeE);
        imageAlzavidrioDeE = findViewById(R.id.imageAlzavidrioDeE);
       // alzavidrioTrE = findViewById(R.id.alzavidrioTrE);
       // imageAlzavidrioTrE = findViewById(R.id.imageAlzavidrioTrE);
        retroElectE = findViewById(R.id.retroElectE);
        imageRetroElectE = findViewById(R.id.imageRetroElectE);
        parlantesE = findViewById(R.id.parlantesE);
        imageParlantesE = findViewById(R.id.imageParlantesE);
        apernadoTeewter = findViewById(R.id.tweeterApernado);
        imageTweeterE = findViewById(R.id.imageTweeterE);
        apernadoAmplifi1 = findViewById(R.id.amplifi1check);
        imageAmplifiUnoE = findViewById(R.id.imageAmplifiUnoE);
        amplifiDosE  = findViewById(R.id.amplifiDosE);
        imageAmplifiDosE = findViewById(R.id.imageAmplifiDosE);
        apernadoWoofer = findViewById(R.id.checkWoofer);
        imageWooferE = findViewById(R.id.imageWooferE);
        imagePantallaDvdE = findViewById(R.id.imagePantallaDvdE);
        gpsE = findViewById(R.id.gpsE);
        imageGpsE = findViewById(R.id.imageGpsE);
        btnSeccionUnoInterior = findViewById(R.id.btnSeccionUnoInterior);
        btnSeccionTresInterior = findViewById(R.id.btnSeccionTresInterior);
        btnSeccionOBD = findViewById(R.id.btnSeccionOBD);
        int obd = db.DatosOiOBD(id_inspeccion);
        if(obd == 1)
        {
            btnSeccionOBD.setVisibility(View.VISIBLE);
        }
        //txtAlzavidrioE = findViewById(R.id.txtAlzavidrioE);
        btnSeccionTresMQ = findViewById(R.id.btnSeccionTresInteriorMQ);
        textView43 = findViewById(R.id.textView43);
        textView44 = findViewById(R.id.textView44);
        textView45 = findViewById(R.id.textView45);
        textView46 = findViewById(R.id.textView46);
        textView47 = findViewById(R.id.textView47);
        textView48 = findViewById(R.id.textView48);
        textView50 = findViewById(R.id.textView50);
        cantidadParlante = findViewById(R.id.cantidadParlante);
        parlanteMarca = findViewById(R.id.parlanteMarca);
        parlanteModelo = findViewById(R.id.parlanteModelo);
        anteElectCheck = findViewById(R.id.anteElectCheck);
        radioTransCheck = findViewById(R.id.radioTransCheck);





        textView51 = findViewById(R.id.textView51);

        textView52 = findViewById(R.id.textView52);

        //marca
        marcaTeewter = findViewById(R.id.marcaTeewter);
        marcaTeewter.setOnEditorActionListener(new PropiedadesTexto());
        marcaTeewter.setText(db.accesorio(Integer.parseInt(id_inspeccion),241));
        //modelo
        modeloTeewter = findViewById(R.id.modeloTeewter);
        modeloTeewter.setOnEditorActionListener(new PropiedadesTexto());
        modeloTeewter.setText(db.accesorio(Integer.parseInt(id_inspeccion),242));
        //cantidad
        cantidadTeewter = findViewById(R.id.cantidadTeewter);
        cantidadTeewter.setOnEditorActionListener(new PropiedadesTexto());
        cantidadTeewter.setText(db.accesorio(Integer.parseInt(id_inspeccion),240));

        textView53 = findViewById(R.id.textView53);
        textView49 = findViewById(R.id.textView49);
        //checkWoofer = findViewById(R.id.checkWoofer);
        textView54 = findViewById(R.id.textView54);
        cantidadWoofer = findViewById(R.id.cantidadWoofer);
        textView55 = findViewById(R.id.textView55);
        woofermarca = findViewById(R.id.wooferMarca);
        textView56 = findViewById(R.id.textView56);
        textView58 = findViewById(R.id.textView58);
        //amplifi1check = findViewById(R.id.amplifi1check);
        textView63 = findViewById(R.id.textView63);
        cantidadampli1 = findViewById(R.id.cantidadampli1);
        textView64 = findViewById(R.id.textView64);
        ampli1marca = findViewById(R.id.ampli1marca);
        textView65 = findViewById(R.id.textView65);
        ampli1modelo = findViewById(R.id.ampli1Modelo);
        textView66 = findViewById(R.id.textView66);
        spinner_amplif1 = findViewById(R.id.spinner_amplif1);
        textView59 = findViewById(R.id.textView59);
        //amplifi2check = findViewById(R.id.amplifi2check);
        textView67 = findViewById(R.id.textView67);
        textView57 = findViewById(R.id.textView57);
        cantidadampli2 = findViewById(R.id.cantidadampli2);
        textView68 = findViewById(R.id.textView68);
        amplimarca2 = findViewById(R.id.amplimarca2);
        textView69 = findViewById(R.id.textView69);
        amplimodelo2 = findViewById(R.id.amplimodelo2);
        textView70 = findViewById(R.id.textView70);
        //textView36 = findViewById(R.id.textView36);
        textView37 = findViewById(R.id.textView37);
        //spinner_panelR = findViewById(R.id.spinner_panelR);
       textView38 = findViewById(R.id.textView38);
        marcaPanel = findViewById(R.id.marcaPanel);
        textView39 = findViewById(R.id.textView39);
        modeloPanel = findViewById(R.id.modeloPanel);
        textView42 = findViewById(R.id.textView42);
        woofermodelo = findViewById(R.id.wooferModelo);
        spinner_amplif2 = findViewById(R.id.spinner_amplif2);
        spinner_ubic_w = findViewById(R.id.spinner_ubic_w);
        textView61 = findViewById(R.id.textView61);
        apernadoPantalla = findViewById(R.id.apernadoPantalla);
        textView75 = findViewById(R.id.textView75);
        pantallaCantidad = findViewById(R.id.pantallaCantidad);
        textView76 = findViewById(R.id.textView76);
        pantallaMarca = findViewById(R.id.pantallaMarca);
        textView77 = findViewById(R.id.textView77);
        pantallaModelo = findViewById(R.id.pantallaModelo);
        textView78 = findViewById(R.id.textView78);
        spinner_pantalla = findViewById(R.id.spinner_pantalla);
        tweeter = findViewById(R.id.spinner_tweeter);



        //panel
        panel = findViewById(R.id.spinner_panelR);
        String[] array = getResources().getStringArray(R.array.panel_radio);
        final List<String> arrayPanel = Arrays.asList(array);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayPanel);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        panel.setAdapter(spinnerAdapter);
        panel.setSelection(arrayPanel.indexOf(db.accesorio(Integer.parseInt(id_inspeccion),211)));

        if (db.accesorio(Integer.parseInt(id_inspeccion),211).toString().equals("")) {
            panel.setSelection(0);

            if(panel.getSelectedItem().toString().equals("Seleccione..."))
            {
                panel.getSelectedItem().toString().equals("");

            }


        }
        else
        {
            panel.getSelectedItem().toString();

        }


        //marca
        marcaPanel = findViewById(R.id.marcaPanel);
        marcaPanel.setOnEditorActionListener(new PropiedadesTexto());
        marcaPanel.setText(db.accesorio(Integer.parseInt(id_inspeccion),213));
        //modelo
        modeloPanel = findViewById(R.id.modeloPanel);
        modeloPanel.setOnEditorActionListener(new PropiedadesTexto());
        modeloPanel.setText(db.accesorio(Integer.parseInt(id_inspeccion),214));
        //tipo de panel
        tipoRadio = findViewById(R.id.spinner_tipoRadio);
        String[] array2 = getResources().getStringArray(R.array.tipo_radio);
        final List<String> arrayPanel2 = Arrays.asList(array2);
        ArrayAdapter<String> spinner_adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayPanel2);
        spinner_adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoRadio.setAdapter(spinner_adapter2);
        tipoRadio.setSelection(arrayPanel2.indexOf(db.accesorio(Integer.parseInt(id_inspeccion),212).toString()));

        if (db.accesorio(Integer.parseInt(id_inspeccion),212).toString().equals("")) {
            tipoRadio.setSelection(0);

            if(tipoRadio.getSelectedItem().toString().equals("Seleccione..."))
            {
                tipoRadio.getSelectedItem().toString().equals("");

            }


        }
        else
        {
            tipoRadio.getSelectedItem().toString();

        }


        textCant19 = findViewById(R.id.textCant19);
        contPost19 = findViewById(R.id.contPost19);
        contPost19.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Panel desde Afuera Interior")));

        textCant20 = findViewById(R.id.textCant20);
        contPost20 = findViewById(R.id.contPost20);
        contPost20.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Panel desde Dentro Interior")));

        textCant21 = findViewById(R.id.textCant21);
        contPost21 = findViewById(R.id.contPost21);
        contPost21.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Radio Interior")));

        textCant22 = findViewById(R.id.textCant22);
        contPost22 = findViewById(R.id.contPost22);
        contPost22.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Kilometraje Interior")));

        textCant23 = findViewById(R.id.textCant23);
        contPost23 = findViewById(R.id.contPost23);
        //contPost23.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Adicional Interior")));
        contPost23.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto panel herramientas")));

        textCant24 = findViewById(R.id.textCant24);
        contPost24 = findViewById(R.id.contPost24);
        contPost24.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto obd")));

        textCant25 = findViewById(R.id.textCant25);
        contPost25 = findViewById(R.id.contPost25);
        contPost25.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto llaves")));

        textCant14 = findViewById(R.id.textCant14);
        contPost14 = findViewById(R.id.contPost14);
        contPost14.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"parabrisas interior")));

        btnPanelFueraInteE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcionCamara(id_inspeccion,"_Foto_Panel_desde_afuera_Interior.jpg",PHOTO_PANEL);
            }
        });
        btnPanelDentroInteE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcionCamara(id_inspeccion,"_Foto_Panel_desde_dentro_Interior.jpg",PHOTO_PANEL_DENTRO);
                //openCamaraPanelDentro(id_inspeccion);
            }
        });
        btnRadioInteriorE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcionCamara(id_inspeccion,"_Foto_Radio_Interior.jpg",PHOTO_RADIO);
                //openCamaraRadioInterior(id_inspeccion);
            }
        });
        btnKilometrajeE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcionCamara(id_inspeccion,"_Foto_Kilometraje_Interior.jpg",PHOTO_KILOMETRAJE);
                //openCamaraKilometraje(id_inspeccion);
            }
        });
        btnAdicionalInteriorE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcionCamara(id_inspeccion,"_Foto_panel_herramienta.jpg",PHOTO_ADICIONAL);
                //openCamaraAdicionalInterior(id_inspeccion);
            }
        });
        btnOBDE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcionCamara(id_inspeccion,"_Foto_obd.jpg",PHOTO_OBD);
            }
        });

        btnLLavesE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcionCamara(id_inspeccion,"_Foto_llaves.jpg",PHOTO_LLAVES);
            }
        });
        btnSeccionUnoInterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {DesplegarCamposSeccionUno(id_inspeccion);  }
        });
        btnSeccionTresInterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {desplegarCamposSeccionTres(id_inspeccion); luzCheckEngineE.requestFocus(); }
        });

        btnSeccionTresMQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                desplegarCamposSeccionTresMQ(id_inspeccion);
                //textView36.requestFocus();
            }
        });

        btnSeccionOBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {DesplegarCamposSeccionOBD(id_inspeccion);  }

        });
        btnLogoParaFrontalE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcionCamara(id_inspeccion,"_Foto_Parabrisas.jpg",PHOTO_PARABRISAS);
            }
        });



        luzCheckEngineE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),791));
        luzCheckEngineE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 791).toString().equals("Ok")) {
                       // funcionCamara(id_inspeccion,"_Foto_Luz_Check_Engine_Interior.jpg",PHOTO_CHECK_ENGINE);
                        //openCamaraLuzCheckEngine(id_inspeccion);
                        db.insertarValor(Integer.parseInt(id_inspeccion),791,"Ok");
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),791,"");
                    //imageLuzCheckEngineE.setImageBitmap(null);
                }
            }
        });

        luzTestigoAirE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),792));
        luzTestigoAirE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 792).toString().equals("Ok")) {
                        //funcionCamara(id_inspeccion,"_Foto_Luz_Testigo_Airbags_Interior.jpg",PHOTO_LUZ_TESTIGO);
                        //openCamaraLuzTestigo(id_inspeccion);
                        db.insertarValor(Integer.parseInt(id_inspeccion),792,"Ok");
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),792,"");
                    //imageluzTestigoAirE.setImageBitmap(null);
                }
            }
        });

        luzTestigoABS.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),968));
        luzTestigoABS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 968).toString().equals("Ok")) {

                        db.insertarValor(Integer.parseInt(id_inspeccion),968,"Ok");
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),968,"");

                }
            }
        });

        controlCruceE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),291));
        controlCruceE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 291).toString().equals("Ok")) {
                        //funcionCamara(id_inspeccion,"_Foto_Control_Crucero_Interior.jpg",PHOTO_CONTROL);
                        //openCamaraControlCrucero(id_inspeccion);
                        db.insertarValor(Integer.parseInt(id_inspeccion),291,"Ok");
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),291,"");
                   // imageControlCruceE.setImageBitmap(null);
                }
            }
        });

        bluetoothE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),339));
        bluetoothE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 339).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_Bluetooh_Interior.jpg",PHOTO_BLUETOOH);
                        //openCamaraBluetooh(id_inspeccion);
                        db.insertarValor(Integer.parseInt(id_inspeccion),339,"Ok");
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),339,"");
                    imageBluetoothE.setImageBitmap(null);
                }
            }
        });

        tapizCueroE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),333));
        tapizCueroE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 333).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_Tapiz_Cuero_Interior.jpg",PHOTO_TAPIZ);
                        //openCamaraTapizCuero(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),333,"");
                    imageTapizCueroE.setImageBitmap(null);
                }

            }
        });

        butacaElectE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),336));
        butacaElectE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 336).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_Butaca_Interior.jpg",PHOTO_BUTACA);
                        //openCamaraButaca(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),336,"");
                    imageButacaElectE.setImageBitmap(null);
                }
            }
        });

        cortaCorriE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),344));
        cortaCorriE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 344).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_Corta_Corriente_Interior.jpg",PHOTO_CORTA_CORRIENTE);
                        //openCamaraCortaCorriente(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),344,"");
                    imageCortaCorriE.setImageBitmap(null);
                }
            }
        });

        alzavidrioDeE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),266));
        alzavidrioDeE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 266).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_Alza_Vidrio_Delantero_Interior.jpg",PHOTO_ALZA_VIDRIO_DE);
                        //openCamaraAlzaVidrioDelantero(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),266,"");
                    imageAlzavidrioDeE.setImageBitmap(null);
                }
            }
        });

       /* alzavidrioTrE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),267));
        alzavidrioTrE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 267).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_Alza_Vidrio_Tracero_Interior.jpg",PHOTO_ALZA_VIDRIO_TR);
                        //openCamaraAlzaVidrioTracero(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),267,"");
                    imageAlzavidrioTrE.setImageBitmap(null);
                }
            }
        });*/

        retroElectE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),256));
        retroElectE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 256).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_Retrovisor_Electrico_Interior.jpg",PHOTO_RETROVISOR);
                        //openCamaraRetrovisorElectrico(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),256,"");
                    imageRetroElectE.setImageBitmap(null);
                }
            }
        });



       /* parlantesE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),271));
        parlantesE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 271).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_Parlantes_Interior.jpg",PHOTO_PARLANTES);
                        //openCamaraParlantesInterior(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),271,"");
                    imageParlantesE.setImageBitmap(null);
                }
            }
        });*/

        parlantesE = findViewById(R.id.parlantesE);
        if(db.accesorio(Integer.parseInt(id_inspeccion),271).equals("Ok")) {
            parlantesE.setChecked(true);
            parlantesEs = "Ok";

        }else{

            parlantesE.setChecked(false);
            parlantesEs = "";
        }
        parlantesE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),271));
        parlantesE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    parlantesEs = "Ok";
                        if (!db.accesorio(Integer.parseInt(id_inspeccion), 271).toString().equals("Ok")) {

                            funcionCamara(id_inspeccion,"_Foto_Parlantes_Interior.jpg",PHOTO_PARLANTES);

                        }

                }else{
                    parlantesEs = "";
                    imageParlantesE.setImageBitmap(null);
                }
            }
        });
        //cantidad
        parlanteCantidad = findViewById(R.id.cantidadParlante);
        parlanteCantidad.setOnEditorActionListener(new PropiedadesTexto());
        parlanteCantidad.setText(db.accesorio(Integer.parseInt(id_inspeccion),216));
        //Marca
        parlanteMarca = findViewById(R.id.parlanteMarca);
        parlanteMarca.setOnEditorActionListener(new PropiedadesTexto());
        parlanteMarca.setText(db.accesorio(Integer.parseInt(id_inspeccion),217));
        //modelo
        parlanteModelo = findViewById(R.id.parlanteModelo);
        parlanteModelo.setOnEditorActionListener(new PropiedadesTexto());
        parlanteModelo.setText(db.accesorio(Integer.parseInt(id_inspeccion),323));

        //ANTENA
        //electrico
        eletricro = findViewById(R.id.anteElectCheck);
        if(db.accesorio(Integer.parseInt(id_inspeccion),219).equals("Ok"))
        {
            eletricro.setChecked(true);
            eletricros = "Ok";
        }else{
            eletricro.setChecked(false);
            eletricros = "";
        }eletricro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    eletricros="Ok";
                }else{
                    eletricros="";
                }
            }
        });

        //Radiotransmisor
        radiotransmisor = findViewById(R.id.radioTransCheck);
        if(db.accesorio(Integer.parseInt(id_inspeccion),797).equals("Ok")){
            radiotransmisor.setChecked(true);
            radiotransmisors="Ok";
        }else{
            radiotransmisor.setChecked(false);
            radiotransmisors="";
        }
        radiotransmisor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    radiotransmisors="Ok";
                }else{
                    radiotransmisors="";
                }
            }
        });

     /*   tweeterE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),239));
        tweeterE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 239).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_Tweeter_Interior.jpg",PHOTO_TWEETER);
                        //openCamaraTweeter(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),239,"");
                    imageTweeterE.setImageBitmap(null);
                }


            }
        });*/

     apernadoTeewter = findViewById(R.id.tweeterApernado);
        if(db.accesorio(Integer.parseInt(id_inspeccion),239).equals("Ok")) {
            apernadoTeewter.setChecked(true);
            apernadoTeewters = "Ok";

        }else{

            apernadoTeewter.setChecked(false);
            apernadoTeewters = "";
        }
        apernadoTeewter.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),239));
        apernadoTeewter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    apernadoTeewters = "Ok";

                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 239).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_Tweeter_Interior.jpg",PHOTO_TWEETER);
                    }
                }else{
                    apernadoTeewters = "";
                    imageTweeterE.setImageBitmap(null);
                }
            }
        });




        String[] ubitweeter = getResources().getStringArray(R.array.tweeter);
        final List<String> ubitweeterList = Arrays.asList(ubitweeter);
        ArrayAdapter<String>spinner_adapterTeewter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,ubitweeterList);
        spinner_adapterTeewter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tweeter.setAdapter(spinner_adapterTeewter);
        tweeter.setSelection(ubitweeterList.indexOf((db.accesorio(Integer.parseInt(id_inspeccion),243).toString())));

        if (db.accesorio(Integer.parseInt(id_inspeccion),243).toString().equals("")) {
            tweeter.setSelection(0);

            if(tweeter.getSelectedItem().toString().equals("Seleccione..."))
            {
                tweeter.getSelectedItem().toString().equals("");

            }


        }
        else
        {
            tweeter.getSelectedItem().toString();

        }


        //WOOFER
       /* wooferE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),245));
        wooferE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 245).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_Woofer_Interior.jpg",PHOTO_WOOFER);
                        //openCamaraWoofer(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),245,"");
                    imageWooferE.setImageBitmap(null);
                }
            }
        });*/


       apernadoWoofer = findViewById(R.id.checkWoofer);
        if(db.accesorio(Integer.parseInt(id_inspeccion),245).equals("Ok")){
            apernadoWoofer.setChecked(true);
            apernadoWoofers="Ok";

        }else{
            apernadoWoofer.setChecked(false);
            apernadoWoofers="";
        }
        apernadoWoofer.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),245));
        apernadoWoofer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    apernadoWoofers="Ok";

                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 245).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_Woofer_Interior.jpg",PHOTO_WOOFER);
                    }
                }else{
                    apernadoWoofers="";
                    imageWooferE.setImageBitmap(null);
                }
            }
        });

        woofercantidad = findViewById(R.id.cantidadWoofer);
        woofercantidad.setOnEditorActionListener(new PropiedadesTexto());
        woofercantidad.setText(db.accesorio(Integer.parseInt(id_inspeccion),246));

        woofermarca = findViewById(R.id.wooferMarca);
        woofermarca.setOnEditorActionListener(new PropiedadesTexto());
        woofermarca.setText(db.accesorio(Integer.parseInt(id_inspeccion),247));

        woofermodelo = findViewById(R.id.wooferModelo);
        woofermodelo.setOnEditorActionListener(new PropiedadesTexto());
        woofermodelo.setText(db.accesorio(Integer.parseInt(id_inspeccion),248));

        ubicacion_w = findViewById(R.id.spinner_ubic_w);
        String[] wooferUbi = getResources().getStringArray(R.array.woofer);
        final List<String> arraywooferUbi = Arrays.asList(wooferUbi);
        ArrayAdapter<String> spinner_adapterWoofer = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,arraywooferUbi);
        spinner_adapterWoofer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ubicacion_w.setAdapter(spinner_adapterWoofer);
        ubicacion_w.setSelection(arraywooferUbi.indexOf(db.accesorio(Integer.parseInt(id_inspeccion),249).toString()));

        if (db.accesorio(Integer.parseInt(id_inspeccion),249).toString().equals("")) {
            ubicacion_w.setSelection(0);

            if(ubicacion_w.getSelectedItem().toString().equals("Seleccione..."))
            {
                ubicacion_w.getSelectedItem().toString().equals("");

            }
        }
        else
        {
            ubicacion_w.getSelectedItem().toString();

        }



        //Amplificador 1
        apernadoAmplifi1 = findViewById(R.id.amplifi1check);
        if(db.accesorio(Integer.parseInt(id_inspeccion),221).equals("Ok")){
            apernadoAmplifi1.setChecked(true);
            apernadoAmplifi1s = "Ok";

        }else{
            apernadoAmplifi1.setChecked(false);
            apernadoAmplifi1s = "";
        }
        apernadoAmplifi1.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),221));
        apernadoAmplifi1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    apernadoAmplifi1s = "Ok";

                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 221).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_Amplificador_Uno_Interior.jpg",PHOTO_AMPL1);
                    }
                }else{
                    apernadoAmplifi1s = "";
                    imageAmplifiUnoE.setImageBitmap(null);
                }
            }
        });

        ampli1Cantidad = findViewById(R.id.cantidadampli1);
        ampli1Cantidad.setOnEditorActionListener(new PropiedadesTexto());
        ampli1Cantidad.setText(db.accesorio(Integer.parseInt(id_inspeccion),222));

        ampli1marca = findViewById(R.id.ampli1marca);
        ampli1marca.setOnEditorActionListener(new PropiedadesTexto());
        ampli1marca.setText(db.accesorio(Integer.parseInt(id_inspeccion),223));

        ampli1modelo = findViewById(R.id.ampli1Modelo);
        ampli1modelo.setOnEditorActionListener(new PropiedadesTexto());
        ampli1modelo.setText(db.accesorio(Integer.parseInt(id_inspeccion),224));

        amplificador1 = findViewById(R.id.spinner_amplif1);
        String[] ubiampli1 = getResources().getStringArray(R.array.amplif1);
        final List<String> ubiampli1list = Arrays.asList(ubiampli1);
        ArrayAdapter<String> spinner_ubiampli1 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,ubiampli1list);
        spinner_ubiampli1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        amplificador1.setAdapter(spinner_ubiampli1);
        amplificador1.setSelection(ubiampli1list.indexOf(db.accesorio(Integer.parseInt(id_inspeccion),225).toString()));

        if (db.accesorio(Integer.parseInt(id_inspeccion),225).toString().equals("")) {
            amplificador1.setSelection(0);

            if(amplificador1.getSelectedItem().toString().equals("Seleccione..."))
            {
                amplificador1.getSelectedItem().toString().equals("");

            }
        }
        else
        {
            amplificador1.getSelectedItem().toString();

        }

        /*amplifiUnoE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),221));
        amplifiUnoE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 221).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_Amplificador_Uno_Interior.jpg",PHOTO_AMPL1);
                    }
                }else{
                    Log.i("ccxx","cxcx");
                    db.insertarValor(Integer.parseInt(id_inspeccion),221,"");
                    imageAmplifiUnoE.setImageBitmap(null);
                }


            }
        });*/

        //amplificador 2
       apernadoAmplifi2 = findViewById(R.id.amplifi2check);
        if(db.accesorio(Integer.parseInt(id_inspeccion),227).equals("Ok")){
            apernadoAmplifi2.setChecked(true);
            apernadoAmplifi2s = "Ok";

        }else{
            apernadoAmplifi2.setChecked(false);
            apernadoAmplifi2s = "";
        }
        apernadoAmplifi2.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),227));
        apernadoAmplifi2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    apernadoAmplifi2s = "Ok";

                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 227).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_Amplificador_Dos_Interior.jpg",PHOTO_AMPL2);
                    }
                }else{
                    apernadoAmplifi2s = "";
                    imageAmplifiDosE.setImageBitmap(null);
                }
            }
        });


        ampli2cantidad = findViewById(R.id.cantidadampli2);
        ampli2cantidad.setOnEditorActionListener(new PropiedadesTexto());
        ampli2cantidad.setText(db.accesorio(Integer.parseInt(id_inspeccion),228));

        ampli2marca = findViewById(R.id.amplimarca2);
        ampli2marca.setOnEditorActionListener(new PropiedadesTexto());
        ampli2marca.setText(db.accesorio(Integer.parseInt(id_inspeccion),229));

        ampli2modelo = findViewById(R.id.amplimodelo2);
        ampli2modelo.setOnEditorActionListener(new PropiedadesTexto());
        ampli2modelo.setText(db.accesorio(Integer.parseInt(id_inspeccion),230));

        amplificador2 = findViewById(R.id.spinner_amplif2);
        String[] ubiampli2 = getResources().getStringArray(R.array.amplif2);
        final List<String> ubiampli2list = Arrays.asList(ubiampli2);
        ArrayAdapter<String> spinner_ubiampli2 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,ubiampli2list);
        spinner_ubiampli2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        amplificador2.setAdapter(spinner_ubiampli2);
        amplificador2.setSelection(ubiampli2list.indexOf(db.accesorio(Integer.parseInt(id_inspeccion),231).toString()));

        if (db.accesorio(Integer.parseInt(id_inspeccion),231).toString().equals("")) {
            amplificador2.setSelection(0);

            if(amplificador2.getSelectedItem().toString().equals("Seleccione..."))
            {
                amplificador2.getSelectedItem().toString().equals("");

            }
        }
        else
        {
            amplificador2.getSelectedItem().toString();

        }


       /* amplifiDosE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),227));
        amplifiDosE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 227).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_Amplificador_Dos_Interior.jpg",PHOTO_AMPL2);
                        //openCamaraAmplificadorDos(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),227,"");
                    imageAmplifiDosE.setImageBitmap(null);
                }
            }
        });*/


        //DVD

       /* apernadoDvd.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),233));
        apernadoDvd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 233).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_Lector_Dvd.jpg",PHOTO_LECTOR);
                        //openCamaraPantallaDvd(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),233,"");
                    imageLectorDvdE.setImageBitmap(null);
                }
            }
        });*/

        //dvd modular

        //DVD

       /* apernadoDvdModular.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),757));
        apernadoDvdModular.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 757).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_Modular_Dvd.jpg",PHOTO_MODULAR);
                        //openCamaraPantallaDvd(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),757,"");
                    imageModularDvd.setImageBitmap(null);
                }
            }
        });*/


        /*pantallaDvdE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),251));
        pantallaDvdE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 251).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_Pantalla_Dvd_Interior.jpg",PHOTO_PANTALLA);
                        //openCamaraPantallaDvd(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),251,"");
                    imagePantallaDvdE.setImageBitmap(null);
                }
            }
        });*/
        apernadoPantalla.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),251));
        apernadoPantalla = findViewById(R.id.apernadoPantalla);
        if(db.accesorio(Integer.parseInt(id_inspeccion),251).equals("Ok")){
            apernadoPantalla.setChecked(true);
            apernadoPantallas = "Ok";
        }else{
            apernadoPantalla.setChecked(false);
            apernadoPantallas = "";
        }
        apernadoPantalla.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    apernadoPantallas = "Ok";
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 251).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_Pantalla_Dvd_Interior.jpg",PHOTO_PANTALLA);
                        //openCamaraPantallaDvd(id_inspeccion);
                    }
                }else{
                    apernadoPantallas = "";
                    imagePantallaDvdE.setImageBitmap(null);
                }
            }
        });


        pantallaCantidad = findViewById(R.id.pantallaCantidad);
        pantallaCantidad.setOnEditorActionListener(new PropiedadesTexto());
        pantallaCantidad.setText(db.accesorio(Integer.parseInt(id_inspeccion),255));

        pantallaMarca = findViewById(R.id.pantallaMarca);
        pantallaMarca.setOnEditorActionListener(new PropiedadesTexto());
        pantallaMarca.setText(db.accesorio(Integer.parseInt(id_inspeccion),252));

        pantallaModelo = findViewById(R.id.pantallaModelo);
        pantallaModelo.setOnEditorActionListener(new PropiedadesTexto());
        pantallaModelo.setText(db.accesorio(Integer.parseInt(id_inspeccion),253));

        pantallaa = findViewById(R.id.spinner_pantalla);
        String[] pantalla = getResources().getStringArray(R.array.pantalla);
        final List<String> pantallalist = Arrays.asList(pantalla);
        ArrayAdapter<String> spinner_pantalla = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,pantallalist);
        spinner_pantalla.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pantallaa.setAdapter(spinner_pantalla);
        pantallaa.setSelection(pantallalist.indexOf(db.accesorio(Integer.parseInt(id_inspeccion),254).toString()));

        if (db.accesorio(Integer.parseInt(id_inspeccion),254).toString().equals("")) {
            pantallaa.setSelection(0);

            if(pantallaa.getSelectedItem().toString().equals("Seleccione..."))
            {
                pantallaa.getSelectedItem().toString().equals("");

            }
        }
        else
        {
            pantallaa.getSelectedItem().toString();

        }




       /* dvdCantidad = findViewById(R.id.dvdCantidad);
        dvdCantidad.setOnEditorActionListener(new PropiedadesTexto());
        dvdCantidad.setText(db.accesorio(Integer.parseInt(id_inspeccion),237));

        dvdMarca = findViewById(R.id.dvdMarca);
        dvdMarca.setOnEditorActionListener(new PropiedadesTexto());
        dvdMarca.setText(db.accesorio(Integer.parseInt(id_inspeccion),234));

        dvdModelo = findViewById(R.id.dvdModelo);
        dvdModelo.setOnEditorActionListener(new PropiedadesTexto());
        dvdModelo.setText(db.accesorio(Integer.parseInt(id_inspeccion),235));*/

        /*lector = findViewById(R.id.spinner_lector);
        String[] dvd = getResources().getStringArray(R.array.dvd);
        final List<String> dvdlist = Arrays.asList(dvd);
        ArrayAdapter<String> spinner_dvd = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dvdlist);
        spinner_dvd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lector.setAdapter(spinner_dvd);
        lector.setSelection(dvdlist.indexOf(db.accesorio(Integer.parseInt(id_inspeccion),236).toString()));*/




       /* dvdModularCantidad = findViewById(R.id.dvdModularCantidad);
        dvdModularCantidad.setOnEditorActionListener(new PropiedadesTexto());
        dvdModularCantidad.setText(db.accesorio(Integer.parseInt(id_inspeccion),758));

        dvdModularmarca = findViewById(R.id.dvdModularmarca);
        dvdModularmarca.setOnEditorActionListener(new PropiedadesTexto());
        dvdModularmarca.setText(db.accesorio(Integer.parseInt(id_inspeccion),759));

        dvdModularmodelo = findViewById(R.id.dvdModularmodelo);
        dvdModularmodelo.setOnEditorActionListener(new PropiedadesTexto());
        dvdModularmodelo.setText(db.accesorio(Integer.parseInt(id_inspeccion),760));*/

        /*dvde = findViewById(R.id.spinner_dvd);
        String[] dvdmodular = getResources().getStringArray(R.array.dvd);
        final List<String> dvdmodulolist = Arrays.asList(dvdmodular);
        ArrayAdapter<String> spinner_dvdmodulo = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dvdmodulolist);
        spinner_dvdmodulo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dvde.setAdapter(spinner_dvdmodulo);
        dvde.setSelection(dvdmodulolist.indexOf(db.accesorio(Integer.parseInt(id_inspeccion),761).toString()));*/



        gpsE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),299));
        gpsE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 299).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_Gps_Interior.jpg",PHOTO_GPS);
                        //openCamaraGps(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),299,"");
                    imageGpsE.setImageBitmap(null);
                }
            }
        });


        btnVolverInteriorE = (Button)findViewById(R.id.btnVolverInteriorE);
        btnVolverInteriorE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarDatos();
                Intent intent = new Intent(interior.this, vl_techo.class);
                intent.putExtra("id_inspeccion", id_inspeccion);
                startActivity(intent);
                finish();
            }
        });
        btnSiguienteInteriorE = (Button)findViewById(R.id.btnSiguienteInteriorE);
        btnSiguienteInteriorE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                String imagePanelFueraInte = db.foto(Integer.parseInt(id_inspeccion), "Foto Panel desde Afuera Interior");
                String imagePanelDentroInte = db.foto(Integer.parseInt(id_inspeccion), "Foto Panel desde Dentro Interior");
                String imageRadioInterior = db.foto(Integer.parseInt(id_inspeccion), "Foto Radio Interior");
                String imageKilometraje = db.foto(Integer.parseInt(id_inspeccion), "Foto Kilometraje Interior");
                String imageAdicionalInterior = db.foto(Integer.parseInt(id_inspeccion),"Foto panel herramientas");
                String imageOBDE = db.foto(Integer.parseInt(id_inspeccion),"Foto OBD");
                String imageLLavesE = db.foto(Integer.parseInt(id_inspeccion),"Foto llaves");
                String marcaRadio = marcaPanel.getText().toString();
                String kilomet = Ekilometraje.getText().toString();
                String imageLogoPara = db.foto(Integer.parseInt(id_inspeccion),"Parabrisas Interior");

                if (imagePanelFueraInte.length()<3 && imagePanelDentroInte.length()<3 && imageRadioInterior.length()<3 && imageKilometraje.length()<3 && imageAdicionalInterior.length()<3 && obd == 0 ) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(interior.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe Tomar Fotos Obligatorias</b><p><ul><li>- Foto Panel desde Afuera</li><p><li>- Foto Panel desde Adent.</li></p>" +
                            "<p><li>- Foto Radio</li></p><p><li>- Foto Kilomentraje</li></p><p><li>- Foto panel herramientas</li></p></ul></p>"));
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else if (imagePanelFueraInte.length()<3 && imagePanelDentroInte.length()<3 && imageRadioInterior.length()<3 && imageKilometraje.length()<3 && imageAdicionalInterior.length()<3 && imageOBDE.length()<3 && imageLLavesE.length()<3  && imageLogoPara.length()<3&& obd == 1 ) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(interior.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe Tomar Fotos Obligatorias</b><p><ul><li>- Foto Panel desde Afuera</li><p><li>- Foto Panel desde Adent.</li></p>" +
                            "<p><li>- Foto Radio</li></p><p><li>- Foto Kilomentraje</li></p><p><li>- Foto panel herramientas</li></p></ul></p> <p><li>- Foto obd</li></p></ul></p>" +
                            "<p><li>- Foto llaves</li></p></ul></p>"));
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
               else  if( imageLogoPara.length()<4 )
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(interior.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe Tomar Fotos Obligatorias</b><p><ul><li>- Foto interior</li><p><li>- Foto Interior Parabrisas</li></p></ul></p>"));
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else  if (imagePanelFueraInte.length()<3 ) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(interior.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe Tomar Fotos Obligatorias</b><p><ul><li>- Foto Panel desde Afuera</li></ul></p>"));
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else if ( imagePanelDentroInte.length()<3  ) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(interior.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe Tomar Fotos Obligatorias</b><p><ul><li>- Foto Panel desde Adent</li></ul></p>"));
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else  if (imageRadioInterior.length()<3 ) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(interior.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe Tomar Fotos Obligatorias</b><p><ul><li>- Foto Radio</li></ul></p>"));
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else  if (imageKilometraje.length()<3 ) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(interior.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe Tomar Fotos Obligatorias</b><p><ul><li>- Foto Kilomentraje</li></ul></p>"));
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else  if (imageOBDE.length()<3 && obd == 1 ) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(interior.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe Tomar Fotos Obligatorias</b><p><ul><li>- Foto OBD</li></ul></p>"));
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else  if (imageLLavesE.length()<3 && obd == 1 ) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(interior.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe Tomar Fotos Obligatorias</b><p><ul><li>- Foto LLaves</li></ul></p>"));
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else if(marcaRadio.equals("") && kilomet.equals("")){

                    AlertDialog.Builder builder = new AlertDialog.Builder(interior.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe ingresar datos obligatorios </b><p><ul><li>- Marca radio</li><p><li>Kilometraje</li></ul></p>"));
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else if(marcaRadio.equals("")){

                    AlertDialog.Builder builder = new AlertDialog.Builder(interior.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe ingresar datos obligatorios </b><p><ul><li>- Marca radio</li></ul></p>"));
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else if(kilomet.equals("")){

                    AlertDialog.Builder builder = new AlertDialog.Builder(interior.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe ingresar datos obligatorios </b><p><ul><li>- Kilometraje</li></ul></p>"));
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else
                {
                    guardarDatos();
                    Intent intent = new Intent(interior.this, motor.class);
                    intent.putExtra("id_inspeccion", id_inspeccion);
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

        Log.i("slslls","sss"+nombre_foto);

        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated) {

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion) + "_" + String.valueOf(correlativo) + nombre_foto;

            ruta = file.toString() + "/" + nombreimagen;
            mPath = ruta;

            File newFile = new File(mPath);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(interior.this,
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
            case PHOTO_PANEL:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapPanel = BitmapFactory.decodeFile(mPath);
                bitmapPanel = foto.redimensiomarImagen(bitmapPanel);

                String imagenPanel = foto.convertirImagenDano(bitmapPanel);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Panel desde Afuera Interior", 0, imagenPanel, 0);
                imagenPanel = "data:image/jpg;base64,"+imagenPanel;
                String base64Image1 = imagenPanel.split(",")[1];
                byte[] decodedString1 = Base64.decode(base64Image1, Base64.DEFAULT);
                Bitmap decodedByte1 = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.length);
                imagePanelFueraInteE.setImageBitmap(decodedByte1);


                Intent servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Panel desde Afuera Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);


                int cantFoto19=db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Panel desde Afuera Interior");
                cantFoto19= cantFoto19 +1;
                db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Foto Panel desde Afuera Interior",cantFoto19);
                contPost19.setText(String.valueOf(cantFoto19));

                break;

            case PHOTO_PANEL_DENTRO:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapPanelDentro = BitmapFactory.decodeFile(mPath);
                bitmapPanelDentro = foto.redimensiomarImagen(bitmapPanelDentro);

                String imagenPanelDentro = foto.convertirImagenDano(bitmapPanelDentro);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Panel desde Dentro Interior", 0, imagenPanelDentro, 0);
                imagenPanelDentro = "data:image/jpg;base64,"+imagenPanelDentro;
                String base64Image2 = imagenPanelDentro.split(",")[1];
                byte[] decodedString2 = Base64.decode(base64Image2, Base64.DEFAULT);
                Bitmap decodedByte2 = BitmapFactory.decodeByteArray(decodedString2, 0, decodedString2.length);
                imagePanelDentroInteE.setImageBitmap(decodedByte2);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Panel desde Dentro Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);


                int cantFoto20=db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Panel desde Dentro Interior");
                cantFoto20= cantFoto20 +1;
                db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Foto Panel desde Dentro Interior",cantFoto20);
                contPost20.setText(String.valueOf(cantFoto20));

                break;
            case PHOTO_PARABRISAS:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapParabrisas = BitmapFactory.decodeFile(mPath);
                bitmapParabrisas = foto.redimensiomarImagen(bitmapParabrisas);

                String imagenParabrisas = foto.convertirImagenDano(bitmapParabrisas);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Parabrisas Interior", 0, imagenParabrisas, 0);
                imagenParabrisas = "data:image/jpg;base64,"+imagenParabrisas;
                String base64Image26 = imagenParabrisas.split(",")[1];
                byte[] decodedString26 = Base64.decode(base64Image26, Base64.DEFAULT);
                Bitmap decodedByte26 = BitmapFactory.decodeByteArray(decodedString26, 0, decodedString26.length);
                imageLogoParaE.setImageBitmap(decodedByte26);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Logo Parabrisas Frontal");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                int cantFoto14=db.cantidadF(Integer.parseInt(id_inspeccion),"Parabrisas Interior");
                cantFoto14= cantFoto14 +1;
                db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Parabrisas Interior",cantFoto14);
                contPost14.setText(String.valueOf(cantFoto14));

                break;
            case PHOTO_RADIO:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapRadio = BitmapFactory.decodeFile(mPath);
                bitmapRadio = foto.redimensiomarImagen(bitmapRadio);

                String imagenRadio = foto.convertirImagenDano(bitmapRadio);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Radio Interior", 0, imagenRadio, 0);
                imagenRadio = "data:image/jpg;base64,"+imagenRadio;
                String base64Image3 = imagenRadio.split(",")[1];
                byte[] decodedString3 = Base64.decode(base64Image3, Base64.DEFAULT);
                Bitmap decodedByte3 = BitmapFactory.decodeByteArray(decodedString3, 0, decodedString3.length);
                imageRadioInteriorE.setImageBitmap(decodedByte3);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Radio Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);


                int cantFoto21=db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Radio Interior");
                cantFoto21= cantFoto21 +1;
                db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Foto Radio Interior",cantFoto21);
                contPost21.setText(String.valueOf(cantFoto21));

                break;
            case PHOTO_KILOMETRAJE:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapKilometraje = BitmapFactory.decodeFile(mPath);
                bitmapKilometraje = foto.redimensiomarImagen(bitmapKilometraje);

                String imagenKilometraje = foto.convertirImagenDano(bitmapKilometraje);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Kilometraje Interior", 0, imagenKilometraje, 0);
                imagenKilometraje = "data:image/jpg;base64,"+imagenKilometraje;
                String base64Image4 = imagenKilometraje.split(",")[1];
                byte[] decodedString4 = Base64.decode(base64Image4, Base64.DEFAULT);
                Bitmap decodedByte4 = BitmapFactory.decodeByteArray(decodedString4, 0, decodedString4.length);
                imageKilometrajeE.setImageBitmap(decodedByte4);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Kilometraje Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);


                int cantFoto22=db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Kilometraje Interior");
                cantFoto22= cantFoto22 +1;
                db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Foto Kilometraje Interior",cantFoto22);
                contPost22.setText(String.valueOf(cantFoto22));

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

                Bitmap bitmapAdicional = BitmapFactory.decodeFile(mPath);
                bitmapAdicional = foto.redimensiomarImagen(bitmapAdicional);

                String imagenAdicional = foto.convertirImagenDano(bitmapAdicional);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto panel herramientas", 0, imagenAdicional, 0);
                imagenAdicional = "data:image/jpg;base64,"+imagenAdicional;
                String base64Image5 = imagenAdicional.split(",")[1];
                byte[] decodedString5 = Base64.decode(base64Image5, Base64.DEFAULT);
                Bitmap decodedByte5 = BitmapFactory.decodeByteArray(decodedString5, 0, decodedString5.length);
                imageAdicionalInteriorE.setImageBitmap(decodedByte5);


                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto panel herramientas");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                int cantFoto23=db.cantidadF(Integer.parseInt(id_inspeccion),"Foto panel herramientas");
                cantFoto23= cantFoto23 +1;
                db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Foto panel herramientas",cantFoto23);
                contPost23.setText(String.valueOf(cantFoto23));

                break;
            case PHOTO_OBD:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapOBD = BitmapFactory.decodeFile(mPath);
                bitmapOBD = foto.redimensiomarImagen(bitmapOBD);

                String imagenOBD = foto.convertirImagenDano(bitmapOBD);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto OBD", 0, imagenOBD, 0);
                imagenOBD = "data:image/jpg;base64,"+imagenOBD;
                String base64Image23 = imagenOBD.split(",")[1];
                byte[] decodedString23 = Base64.decode(base64Image23, Base64.DEFAULT);
                Bitmap decodedByte23 = BitmapFactory.decodeByteArray(decodedString23, 0, decodedString23.length);
                imageOBDE.setImageBitmap(decodedByte23);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto obd");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);


                int cantFoto24=db.cantidadF(Integer.parseInt(id_inspeccion),"Foto obd");
                cantFoto24= cantFoto24 +1;
                db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Foto obd",cantFoto24);
                contPost24.setText(String.valueOf(cantFoto24));

                break;

            case PHOTO_LLAVES:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapllaves = BitmapFactory.decodeFile(mPath);
                bitmapllaves = foto.redimensiomarImagen(bitmapllaves);

                String imagenllaves = foto.convertirImagenDano(bitmapllaves);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto llaves", 0, imagenllaves, 0);
                imagenllaves = "data:image/jpg;base64,"+imagenllaves;
                String base64Image24 = imagenllaves.split(",")[1];
                byte[] decodedString24 = Base64.decode(base64Image24, Base64.DEFAULT);
                Bitmap decodedByte24 = BitmapFactory.decodeByteArray(decodedString24, 0, decodedString24.length);
                imageLLavesE.setImageBitmap(decodedByte24);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto llaves");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);


                int cantFoto25=db.cantidadF(Integer.parseInt(id_inspeccion),"Foto llaves");
                cantFoto25= cantFoto25 +1;
                db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Foto llaves",cantFoto25);
                contPost25.setText(String.valueOf(cantFoto25));

                break;
            /*case PHOTO_CHECK_ENGINE:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapCheck = BitmapFactory.decodeFile(mPath);
                bitmapCheck = foto.redimensiomarImagen(bitmapCheck);

                String imagenCheck = foto.convertirImagenDano(bitmapCheck);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Check Engine Interior", 0, imagenCheck, 0);
                db.insertarValor(Integer.parseInt(id_inspeccion), 791, "Ok");
                imagenCheck = "data:image/jpg;base64,"+imagenCheck;
                String base64Image6 = imagenCheck.split(",")[1];
                byte[] decodedString6 = Base64.decode(base64Image6, Base64.DEFAULT);
                Bitmap decodedByte6 = BitmapFactory.decodeByteArray(decodedString6, 0, decodedString6.length);
                imageLuzCheckEngineE.setImageBitmap(decodedByte6);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Check Engine Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;
            case PHOTO_LUZ_TESTIGO:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapLuzTestigo = BitmapFactory.decodeFile(mPath);
                bitmapLuzTestigo = foto.redimensiomarImagen(bitmapLuzTestigo);

                String imagenLuzTestigo = foto.convertirImagenDano(bitmapLuzTestigo);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Luz Testigo  Airbags Interior", 0, imagenLuzTestigo, 0);
                db.insertarValor(Integer.parseInt(id_inspeccion), 792, "Ok");
                imagenLuzTestigo = "data:image/jpg;base64,"+imagenLuzTestigo;
                String base64Image7 = imagenLuzTestigo.split(",")[1];
                byte[] decodedString7 = Base64.decode(base64Image7, Base64.DEFAULT);
                Bitmap decodedByte7 = BitmapFactory.decodeByteArray(decodedString7, 0, decodedString7.length);
                imageluzTestigoAirE.setImageBitmap(decodedByte7);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Luz Testigo  Airbags Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;
            case PHOTO_CONTROL:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapControl = BitmapFactory.decodeFile(mPath);
                bitmapControl = foto.redimensiomarImagen(bitmapControl);

                String imagenControl = foto.convertirImagenDano(bitmapControl);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Control Crucero Interior", 0, imagenControl, 0);
                db.insertarValor(Integer.parseInt(id_inspeccion), 291, "Ok");

                imagenControl = "data:image/jpg;base64,"+imagenControl;
                String base64Image8 = imagenControl.split(",")[1];
                byte[] decodedString8 = Base64.decode(base64Image8, Base64.DEFAULT);
                Bitmap decodedByte8 = BitmapFactory.decodeByteArray(decodedString8, 0, decodedString8.length);
                imageControlCruceE.setImageBitmap(decodedByte8);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Control Crucero Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;*/
            case PHOTO_BLUETOOH:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapBluetooh = BitmapFactory.decodeFile(mPath);
                bitmapBluetooh = foto.redimensiomarImagen(bitmapBluetooh);

                String imagenBluetooh = foto.convertirImagenDano(bitmapBluetooh);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Bluetooh Interior", 0, imagenBluetooh, 0);
                db.insertarValor(Integer.parseInt(id_inspeccion), 339, "Ok");
                imagenBluetooh = "data:image/jpg;base64,"+imagenBluetooh;
                String base64Image9 = imagenBluetooh.split(",")[1];
                byte[] decodedString9 = Base64.decode(base64Image9, Base64.DEFAULT);
                Bitmap decodedByte9 = BitmapFactory.decodeByteArray(decodedString9, 0, decodedString9.length);
                imageBluetoothE.setImageBitmap(decodedByte9);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Bluetooh Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;
            case PHOTO_TAPIZ:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapTapiz = BitmapFactory.decodeFile(mPath);
                bitmapTapiz = foto.redimensiomarImagen(bitmapTapiz);

                String imagenTapiz = foto.convertirImagenDano(bitmapTapiz);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Tapiz de Cuero Interior", 0, imagenTapiz, 0);
                db.insertarValor(Integer.parseInt(id_inspeccion), 333, "Ok");
                imagenTapiz = "data:image/jpg;base64,"+imagenTapiz;
                String base64Image10 = imagenTapiz.split(",")[1];
                byte[] decodedString10 = Base64.decode(base64Image10, Base64.DEFAULT);
                Bitmap decodedByte10 = BitmapFactory.decodeByteArray(decodedString10, 0, decodedString10.length);
                imageTapizCueroE.setImageBitmap(decodedByte10);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Tapiz de Cuero Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;
            case PHOTO_BUTACA:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapButaca = BitmapFactory.decodeFile(mPath);
                bitmapButaca = foto.redimensiomarImagen(bitmapButaca);

                String imagenButaca = foto.convertirImagenDano(bitmapButaca);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Butaca Eléctrica Interior", 0, imagenButaca, 0);
                db.insertarValor(Integer.parseInt(id_inspeccion), 336, "Ok");
                imagenButaca = "data:image/jpg;base64,"+imagenButaca;
                String base64Image11 = imagenButaca.split(",")[1];
                byte[] decodedString11 = Base64.decode(base64Image11, Base64.DEFAULT);
                Bitmap decodedByte11 = BitmapFactory.decodeByteArray(decodedString11, 0, decodedString11.length);
                imageButacaElectE.setImageBitmap(decodedByte11);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Butaca Eléctrica Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;
            case PHOTO_CORTA_CORRIENTE:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapCorta = BitmapFactory.decodeFile(mPath);
                bitmapCorta = foto.redimensiomarImagen(bitmapCorta);

                String imagenCorta = foto.convertirImagenDano(bitmapCorta);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Corta Corriente Interior", 0, imagenCorta, 0);
                db.insertarValor(Integer.parseInt(id_inspeccion), 344, "Ok");
                imagenCorta = "data:image/jpg;base64,"+imagenCorta;
                String base64Image12 = imagenCorta.split(",")[1];
                byte[] decodedString12 = Base64.decode(base64Image12, Base64.DEFAULT);
                Bitmap decodedByte12 = BitmapFactory.decodeByteArray(decodedString12, 0, decodedString12.length);
                imageCortaCorriE.setImageBitmap(decodedByte12);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Corta Corriente Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;
            case PHOTO_ALZA_VIDRIO_DE:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapAlzaVidrioDe = BitmapFactory.decodeFile(mPath);
                bitmapAlzaVidrioDe = foto.redimensiomarImagen(bitmapAlzaVidrioDe);

                String imagenAlzaVidrioDe = foto.convertirImagenDano(bitmapAlzaVidrioDe);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Alza Vidrio Delantero Interior", 0, imagenAlzaVidrioDe, 0);
                db.insertarValor(Integer.parseInt(id_inspeccion), 266, "Ok");

                imagenAlzaVidrioDe = "data:image/jpg;base64,"+imagenAlzaVidrioDe;
                String base64Image13 = imagenAlzaVidrioDe.split(",")[1];
                byte[] decodedString13 = Base64.decode(base64Image13, Base64.DEFAULT);
                Bitmap decodedByte13 = BitmapFactory.decodeByteArray(decodedString13, 0, decodedString13.length);
                imageAlzavidrioDeE.setImageBitmap(decodedByte13);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Alza Vidrio Delantero Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;
            /*case PHOTO_ALZA_VIDRIO_TR:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapAlzaVidrioTr = BitmapFactory.decodeFile(mPath);
                bitmapAlzaVidrioTr = foto.redimensiomarImagen(bitmapAlzaVidrioTr);

                String imagenAlzaVidrioTr = foto.convertirImagenDano(bitmapAlzaVidrioTr);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Alza Vidrio Trasero Interior", 0, imagenAlzaVidrioTr, 0);
                db.insertarValor(Integer.parseInt(id_inspeccion), 267, "Ok");

                imagenAlzaVidrioTr = "data:image/jpg;base64,"+imagenAlzaVidrioTr;
                String base64Image14 = imagenAlzaVidrioTr.split(",")[1];
                byte[] decodedString14 = Base64.decode(base64Image14, Base64.DEFAULT);
                Bitmap decodedByte14 = BitmapFactory.decodeByteArray(decodedString14, 0, decodedString14.length);
                imageAlzavidrioTrE.setImageBitmap(decodedByte14);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Alza Vidrio Trasero Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;*/
            case PHOTO_RETROVISOR:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapRetrovisor = BitmapFactory.decodeFile(mPath);
                bitmapRetrovisor = foto.redimensiomarImagen(bitmapRetrovisor);

                String imagenRetrovisor = foto.convertirImagenDano(bitmapRetrovisor);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Retrovisor Eléctrico Interior", 0, imagenRetrovisor, 0);
                db.insertarValor(Integer.parseInt(id_inspeccion), 256, "Ok");

                imagenRetrovisor = "data:image/jpg;base64,"+imagenRetrovisor;
                String base64Image15 = imagenRetrovisor.split(",")[1];
                byte[] decodedString15 = Base64.decode(base64Image15, Base64.DEFAULT);
                Bitmap decodedByte15 = BitmapFactory.decodeByteArray(decodedString15, 0, decodedString15.length);
                imageRetroElectE.setImageBitmap(decodedByte15);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Retrovisor Eléctrico Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;
            case PHOTO_PARLANTES:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapParlantes = BitmapFactory.decodeFile(mPath);
                bitmapParlantes = foto.redimensiomarImagen(bitmapParlantes);

                String imagenParlantes = foto.convertirImagenDano(bitmapParlantes);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Parlantes Interior", 0, imagenParlantes, 0);
                //db.insertarValor(Integer.parseInt(id_inspeccion), 271, "Ok");

                imagenParlantes = "data:image/jpg;base64,"+imagenParlantes;
                String base64Image16 = imagenParlantes.split(",")[1];
                byte[] decodedString16 = Base64.decode(base64Image16, Base64.DEFAULT);
                Bitmap decodedByte16 = BitmapFactory.decodeByteArray(decodedString16, 0, decodedString16.length);
                imageParlantesE.setImageBitmap(decodedByte16);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Parlantes Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;
            case PHOTO_TWEETER:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapTweeter = BitmapFactory.decodeFile(mPath);
                bitmapTweeter = foto.redimensiomarImagen(bitmapTweeter);

                String ImagenTweeter = foto.convertirImagenDano(bitmapTweeter);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Tweeter Interior", 0, ImagenTweeter, 0);
               // db.insertarValor(Integer.parseInt(id_inspeccion), 239, "Ok");

                ImagenTweeter = "data:image/jpg;base64,"+ImagenTweeter;
                String base64Image17 = ImagenTweeter.split(",")[1];
                byte[] decodedString17 = Base64.decode(base64Image17, Base64.DEFAULT);
                Bitmap decodedByte17 = BitmapFactory.decodeByteArray(decodedString17, 0, decodedString17.length);
                imageTweeterE.setImageBitmap(decodedByte17);


                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Tweeter Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;
            case PHOTO_AMPL1:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapAmp1 = BitmapFactory.decodeFile(mPath);
                bitmapAmp1 = foto.redimensiomarImagen(bitmapAmp1);

                String ImagenAmp1 = foto.convertirImagenDano(bitmapAmp1);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Amplificador Uno Interior", 0, ImagenAmp1, 0);
               // db.insertarValor(Integer.parseInt(id_inspeccion), 221, "Ok");

                ImagenAmp1 = "data:image/jpg;base64,"+ImagenAmp1;
                String base64Image18 = ImagenAmp1.split(",")[1];
                byte[] decodedString18 = Base64.decode(base64Image18, Base64.DEFAULT);
                Bitmap decodedByte18 = BitmapFactory.decodeByteArray(decodedString18, 0, decodedString18.length);
                imageAmplifiUnoE.setImageBitmap(decodedByte18);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Amplificador Uno Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;
            case PHOTO_AMPL2:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapAmp2 = BitmapFactory.decodeFile(mPath);
                bitmapAmp2 = foto.redimensiomarImagen(bitmapAmp2);

                String ImagenAmp2 = foto.convertirImagenDano(bitmapAmp2);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Amplificador Dos Interior", 0, ImagenAmp2, 0);
                //db.insertarValor(Integer.parseInt(id_inspeccion), 227, "Ok");

                ImagenAmp2 = "data:image/jpg;base64,"+ImagenAmp2;
                String base64Image19 = ImagenAmp2.split(",")[1];
                byte[] decodedString19 = Base64.decode(base64Image19, Base64.DEFAULT);
                Bitmap decodedByte19 = BitmapFactory.decodeByteArray(decodedString19, 0, decodedString19.length);
                imageAmplifiDosE.setImageBitmap(decodedByte19);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Amplificador Dos Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;
            case PHOTO_WOOFER:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapWoofer = BitmapFactory.decodeFile(mPath);
                bitmapWoofer = foto.redimensiomarImagen(bitmapWoofer);

                String ImagenWoofer = foto.convertirImagenDano(bitmapWoofer);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Woofer Interior", 0, ImagenWoofer, 0);
                //db.insertarValor(Integer.parseInt(id_inspeccion), 245, "Ok");

                ImagenWoofer = "data:image/jpg;base64,"+ImagenWoofer;
                String base64Image20 = ImagenWoofer.split(",")[1];
                byte[] decodedString20 = Base64.decode(base64Image20, Base64.DEFAULT);
                Bitmap decodedByte20 = BitmapFactory.decodeByteArray(decodedString20, 0, decodedString20.length);
                imageWooferE.setImageBitmap(decodedByte20);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Woofer Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;
            case PHOTO_PANTALLA:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapPantalla = BitmapFactory.decodeFile(mPath);
                bitmapPantalla = foto.redimensiomarImagen(bitmapPantalla);

                String ImagenPantalla = foto.convertirImagenDano(bitmapPantalla);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Pantalla DVD Interior", 0, ImagenPantalla, 0);
                db.insertarValor(Integer.parseInt(id_inspeccion), 251, "Ok");

                ImagenPantalla = "data:image/jpg;base64,"+ImagenPantalla;
                String base64Image21 = ImagenPantalla.split(",")[1];
                byte[] decodedString21 = Base64.decode(base64Image21, Base64.DEFAULT);
                Bitmap decodedByte21 = BitmapFactory.decodeByteArray(decodedString21, 0, decodedString21.length);
                imagePantallaDvdE.setImageBitmap(decodedByte21);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Pantalla DVD Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;
            case PHOTO_GPS:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapGps = BitmapFactory.decodeFile(mPath);
                bitmapGps = foto.redimensiomarImagen(bitmapGps);

                String ImagenGps = foto.convertirImagenDano(bitmapGps);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto GPS Interior", 0, ImagenGps, 0);
                db.insertarValor(Integer.parseInt(id_inspeccion), 299, "Ok");

                ImagenGps = "data:image/jpg;base64,"+ImagenGps;
                String base64Image22 = ImagenGps.split(",")[1];
                byte[] decodedString22 = Base64.decode(base64Image22, Base64.DEFAULT);
                Bitmap decodedByte22 = BitmapFactory.decodeByteArray(decodedString22, 0, decodedString22.length);
                imageGpsE.setImageBitmap(decodedByte22);


                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto GPS Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;
           /* case PHOTO_LECTOR:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });
                Log.i("xxx","xxx");

                Bitmap bitmapLector = BitmapFactory.decodeFile(mPath);
                bitmapLector = foto.redimensiomarImagen(bitmapLector);

                String ImagenLector = foto.convertirImagenDano(bitmapLector);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Lector DVD", 0, ImagenLector, 0);
                db.insertarValor(Integer.parseInt(id_inspeccion), 233, "Ok");

                ImagenLector = "data:image/jpg;base64,"+ImagenLector;
                String base64ImageL = ImagenLector.split(",")[1];
                byte[] decodedStringL = Base64.decode(base64ImageL, Base64.DEFAULT);
                Bitmap decodedByteL = BitmapFactory.decodeByteArray(decodedStringL, 0, decodedStringL.length);
                imageLectorDvdE.setImageBitmap(decodedByteL);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Lector DVD");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;*/
           /* case PHOTO_MODULAR:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapModular = BitmapFactory.decodeFile(mPath);
                bitmapModular = foto.redimensiomarImagen(bitmapModular);

                String ImagenModular = foto.convertirImagenDano(bitmapModular);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Modular DVD", 0, ImagenModular, 0);
                db.insertarValor(Integer.parseInt(id_inspeccion), 757, "Ok");

                ImagenModular = "data:image/jpg;base64,"+ImagenModular;
                String base64ImageM = ImagenModular.split(",")[1];
                byte[] decodedStringM = Base64.decode(base64ImageM, Base64.DEFAULT);
                Bitmap decodedByteM = BitmapFactory.decodeByteArray(decodedStringM, 0, decodedStringM.length);
                imageModularDvd.setImageBitmap(decodedByteM);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Modular DVD");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;*/
        }
    }
}catch(Exception e){
    Log.e("Error",e.getMessage());
    Toast.makeText(interior.this,"Porfavor vuelva a intentar tomar la foto",Toast.LENGTH_SHORT).show();
}
    }


    private  void DesplegarCamposSeccionUno(String id)    {

        if (btnPanelFueraInteE.getVisibility()==View.VISIBLE)
        {
            btnPanelFueraInteE.setVisibility(View.GONE);
            imagePanelFueraInteE.setVisibility(View.GONE);
            imagePanelFueraInteE.setImageBitmap(null);
            btnPanelDentroInteE.setVisibility(View.GONE);
            imagePanelDentroInteE.setVisibility(View.GONE);
            btnLogoParaFrontalE.setVisibility(View.GONE);
            imagePanelDentroInteE.setImageBitmap(null);
            btnRadioInteriorE.setVisibility(View.GONE);
            imageRadioInteriorE.setVisibility(View.GONE);
            imageRadioInteriorE.setImageBitmap(null);
            imageLogoParaE.setVisibility(View.GONE);
            imageLogoParaE.setImageBitmap(null);
            textView38.setVisibility(View.GONE);
            marcaPanel.setVisibility(View.GONE);
            textView42.setVisibility(View.GONE);
            textView37.setVisibility(View.GONE);
            panel.setVisibility(View.GONE);
            tipoRadio.setVisibility(View.GONE);
            textView39.setVisibility(View.GONE);
            modeloPanel.setVisibility(View.GONE);
            btnKilometrajeE.setVisibility(View.GONE);
            imageKilometrajeE.setVisibility(View.GONE);
            imageKilometrajeE.setImageBitmap(null);
            textViewK.setVisibility(View.GONE);
            Ekilometraje.setVisibility(View.GONE);
            btnAdicionalInteriorE.setVisibility(View.GONE);
            imageAdicionalInteriorE.setVisibility(View.GONE);
            imageAdicionalInteriorE.setImageBitmap(null);
            luzCheckEngineE.setVisibility(View.GONE);
            luzTestigoAirE.setVisibility(View.GONE);
            luzTestigoABS.setVisibility(View.GONE);
            textCant14.setVisibility(View.GONE);
            contPost14.setVisibility(View.GONE);
            textCant19.setVisibility(View.GONE);
            contPost19.setVisibility(View.GONE);
            textCant20.setVisibility(View.GONE);
            contPost20.setVisibility(View.GONE);
            textCant21.setVisibility(View.GONE);
            contPost21.setVisibility(View.GONE);
            textCant22.setVisibility(View.GONE);
            contPost22.setVisibility(View.GONE);
            textCant23.setVisibility(View.GONE);
            contPost23.setVisibility(View.GONE);
            textCant14.setVisibility(View.GONE);
            contPost14.setVisibility(View.GONE);



        }
        else
        {

            //seccion tres
            controlCruceE.setVisibility(View.GONE);
            bluetoothE.setVisibility(View.GONE);
            linea.setVisibility(View.GONE);
            imageBluetoothE.setVisibility(View.GONE);
            imageBluetoothE.setImageBitmap(null);
            tapizCueroE.setVisibility(View.GONE);
            imageTapizCueroE.setVisibility(View.GONE);
            imageTapizCueroE.setImageBitmap(null);
            butacaElectE.setVisibility(View.GONE);
            imageButacaElectE.setVisibility(View.GONE);
            imageButacaElectE.setImageBitmap(null);
            cortaCorriE.setVisibility(View.GONE);
            imageCortaCorriE.setVisibility(View.GONE);
            imageCortaCorriE.setImageBitmap(null);
            alzavidrioDeE.setVisibility(View.GONE);
            imageAlzavidrioDeE.setVisibility(View.GONE);
            imageAlzavidrioDeE.setImageBitmap(null);
            retroElectE.setVisibility(View.GONE);
            imageRetroElectE.setVisibility(View.GONE);
            imageRetroElectE.setImageBitmap(null);
            parlantesE.setVisibility(View.GONE);
            imageParlantesE.setVisibility(View.GONE);
            imageParlantesE.setImageBitmap(null);
            cantidadParlante.setVisibility(View.GONE);
            textView43.setVisibility(View.GONE);
            textView44.setVisibility(View.GONE);
            textView45.setVisibility(View.GONE);
            textView46.setVisibility(View.GONE);
            textView47.setVisibility(View.GONE);
            textView48.setVisibility(View.GONE);
            textView50.setVisibility(View.GONE);
            cantidadTeewter.setVisibility(View.GONE);
            textView51.setVisibility(View.GONE);
            marcaTeewter.setVisibility(View.GONE);
            textView52.setVisibility(View.GONE);
            modeloTeewter.setVisibility(View.GONE);
            textView53.setVisibility(View.GONE);
            tweeter.setVisibility(View.GONE);
            textView49.setVisibility(View.GONE);
            textView54.setVisibility(View.GONE);
            cantidadWoofer.setVisibility(View.GONE);
            textView55.setVisibility(View.GONE);
            woofermarca.setVisibility(View.GONE);
            textView56.setVisibility(View.GONE);
            textView58.setVisibility(View.GONE);
            textView63.setVisibility(View.GONE);
            cantidadampli1.setVisibility(View.GONE);
            textView64.setVisibility(View.GONE);
            ampli1marca.setVisibility(View.GONE);
            textView65.setVisibility(View.GONE);
            ampli1modelo.setVisibility(View.GONE);
            parlanteMarca.setVisibility(View.GONE);
            parlanteModelo.setVisibility(View.GONE);
            anteElectCheck.setVisibility(View.GONE);
            radioTransCheck.setVisibility(View.GONE);
            imageTweeterE.setVisibility(View.GONE);
            imageTweeterE.setImageBitmap(null);
            apernadoAmplifi1.setVisibility(View.GONE);
            imageAmplifiUnoE.setVisibility(View.GONE);
            imageAmplifiUnoE.setImageBitmap(null);
            apernadoAmplifi2.setVisibility(View.GONE);
            parlantesE.setVisibility(View.GONE);
            imageAmplifiDosE.setVisibility(View.GONE);
            imageAmplifiDosE.setImageBitmap(null);
            apernadoWoofer.setVisibility(View.GONE);
            imageWooferE.setVisibility(View.GONE);
            imageWooferE.setImageBitmap(null);
            apernadoPantalla.setVisibility(View.GONE);
            imagePantallaDvdE.setVisibility(View.GONE);
            imagePantallaDvdE.setImageBitmap(null);
            imageGpsE.setVisibility(View.GONE);
            imageGpsE.setImageBitmap(null);
            gpsE.setVisibility(View.GONE);
            woofermodelo.setVisibility(View.GONE);
            textView57.setVisibility(View.GONE);
            spinner_ubic_w.setVisibility(View.GONE);
            textView58.setVisibility(View.GONE);
            textView63.setVisibility(View.GONE);
            cantidadampli1.setVisibility(View.GONE);
            textView64.setVisibility(View.GONE);
            ampli1marca.setVisibility(View.GONE);
            textView65.setVisibility(View.GONE);
            ampli1modelo.setVisibility(View.GONE);
            parlanteMarca.setVisibility(View.GONE);
            textView66.setVisibility(View.GONE);
            spinner_amplif1.setVisibility(View.GONE);
            spinner_amplif2.setVisibility(View.GONE);
            textView59.setVisibility(View.GONE);
            textView67.setVisibility(View.GONE);
            textView57.setVisibility(View.GONE);
            cantidadampli2.setVisibility(View.GONE);
            textView68.setVisibility(View.GONE);
            amplimarca2.setVisibility(View.GONE);
            textView69.setVisibility(View.GONE);
            amplimodelo2.setVisibility(View.GONE);
            //textView36.setVisibility(View.GONE);
            //textView37.setVisibility(View.GONE);
           // spinner_panelR.setVisibility(View.GONE);
            //.setVisibility(View.GONE);
            //tipoRadio.setVisibility(View.GONE);
            textView61.setVisibility(View.GONE);
            textView75.setVisibility(View.GONE);
            pantallaCantidad.setVisibility(View.GONE);
            textView76.setVisibility(View.GONE);
            pantallaMarca.setVisibility(View.GONE);
            textView77.setVisibility(View.GONE);
            pantallaModelo.setVisibility(View.GONE);
            textView78.setVisibility(View.GONE);
            spinner_pantalla.setVisibility(View.GONE);
            apernadoTeewter.setVisibility(View.GONE);



            String imagePanelFueraInte = db.foto(Integer.parseInt(id),"Foto Panel desde Afuera Interior");
            String imagePanelDentroInte = db.foto(Integer.parseInt(id),"Foto Panel desde Dentro Interior");
            String imageRadioInterior = db.foto(Integer.parseInt(id),"Foto Radio Interior");
            String imageKilometraje = db.foto(Integer.parseInt(id),"Foto Kilometraje Interior");
            String imageAdicionalInterior = db.foto(Integer.parseInt(id),"Foto panel herramientas");
            String imageLogoPara = db.foto(Integer.parseInt(id)," Foto Parabrisas Interior");

            if(imagePanelFueraInte.length()>3)
            {
                byte[] decodedString = Base64.decode(imagePanelFueraInte, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imagePanelFueraInteE.setImageBitmap(decodedByte);
            }
            if(imagePanelDentroInte.length()>3)
            {
                byte[] decodedString = Base64.decode(imagePanelDentroInte, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imagePanelDentroInteE.setImageBitmap(decodedByte);
            }
            if(imageRadioInterior.length()>3)
            {
                byte[] decodedString = Base64.decode(imageRadioInterior, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageRadioInteriorE.setImageBitmap(decodedByte);
            }
            if(imageKilometraje.length()>3)
            {
                byte[] decodedString = Base64.decode(imageKilometraje, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageKilometrajeE.setImageBitmap(decodedByte);
            }
            if(imageAdicionalInterior.length()>3)
            {
                byte[] decodedString = Base64.decode(imageAdicionalInterior, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageAdicionalInteriorE.setImageBitmap(decodedByte);
            }
            if(imageLogoPara.length()>=3 )
            {
                byte[] decodedString = Base64.decode(imageLogoPara, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageLogoParaE.setImageBitmap(decodedByte);
            }


            btnPanelFueraInteE.setVisibility(View.VISIBLE);
            imagePanelFueraInteE.setVisibility(View.VISIBLE);
            btnPanelDentroInteE.setVisibility(View.VISIBLE);
            imagePanelDentroInteE.setVisibility(View.VISIBLE);
            imageLogoParaE.setVisibility(View.VISIBLE);
            btnLogoParaFrontalE.setVisibility(View.VISIBLE);
            btnRadioInteriorE.setVisibility(View.VISIBLE);
            imageRadioInteriorE.setVisibility(View.VISIBLE);
            textView38.setVisibility(View.VISIBLE);
            marcaPanel.setVisibility(View.VISIBLE);
            textView42.setVisibility(View.VISIBLE);
            textView37.setVisibility(View.VISIBLE);
            panel.setVisibility(View.VISIBLE);
            tipoRadio.setVisibility(View.VISIBLE);
            textView39.setVisibility(View.VISIBLE);
            modeloPanel.setVisibility(View.VISIBLE);
            btnKilometrajeE.setVisibility(View.VISIBLE);
            imageKilometrajeE.setVisibility(View.VISIBLE);
            textViewK.setVisibility(View.VISIBLE);
            Ekilometraje.setVisibility(View.VISIBLE);
            btnAdicionalInteriorE.setVisibility(View.VISIBLE);
            imageAdicionalInteriorE.setVisibility(View.VISIBLE);
            luzCheckEngineE.setVisibility(View.VISIBLE);
            luzTestigoAirE.setVisibility(View.VISIBLE);
            luzTestigoABS.setVisibility(View.VISIBLE);
            textCant19.setVisibility(View.VISIBLE);
            contPost19.setVisibility(View.VISIBLE);
            textCant20.setVisibility(View.VISIBLE);
            contPost20.setVisibility(View.VISIBLE);
            textCant21.setVisibility(View.VISIBLE);
            contPost21.setVisibility(View.VISIBLE);
            textCant22.setVisibility(View.VISIBLE);
            contPost22.setVisibility(View.VISIBLE);
            textCant23.setVisibility(View.VISIBLE);
            contPost23.setVisibility(View.VISIBLE);
            textCant14.setVisibility(View.VISIBLE);
            contPost14.setVisibility(View.VISIBLE);

        }
    }
    private void desplegarCamposSeccionTres(String id)    {
        if (controlCruceE.getVisibility() == View.VISIBLE) {

            //luzCheckEngineE.setVisibility(View.GONE);
            //imageLuzCheckEngineE.setVisibility(View.GONE);
            //imageLuzCheckEngineE.setImageBitmap(null);
            //luzTestigoAirE.setVisibility(View.GONE);
            //imageluzTestigoAirE.setVisibility(View.GONE);
            //imageluzTestigoAirE.setImageBitmap(null);
            controlCruceE.setVisibility(View.GONE);
           // imageControlCruceE.setVisibility(View.GONE);
            //imageControlCruceE.setImageBitmap(null);
            bluetoothE.setVisibility(View.GONE);
            linea.setVisibility(View.GONE);
            imageBluetoothE.setVisibility(View.GONE);
            imageBluetoothE.setImageBitmap(null);
            tapizCueroE.setVisibility(View.GONE);
            imageTapizCueroE.setVisibility(View.GONE);
            imageTapizCueroE.setImageBitmap(null);
            butacaElectE.setVisibility(View.GONE);
            imageButacaElectE.setVisibility(View.GONE);
            imageButacaElectE.setImageBitmap(null);
            cortaCorriE.setVisibility(View.GONE);
            imageCortaCorriE.setVisibility(View.GONE);
            imageCortaCorriE.setImageBitmap(null);
            alzavidrioDeE.setVisibility(View.GONE);
            imageAlzavidrioDeE.setVisibility(View.GONE);
            imageAlzavidrioDeE.setImageBitmap(null);
            //alzavidrioTrE.setVisibility(View.GONE);
            //imageAlzavidrioTrE.setVisibility(View.GONE);
            //imageAlzavidrioTrE.setImageBitmap(null);
           // txtAlzavidrioE.setVisibility(View.GONE);
            retroElectE.setVisibility(View.GONE);
            imageRetroElectE.setImageBitmap(null);
            imageRetroElectE.setVisibility(View.GONE);
            gpsE.setVisibility(View.GONE);
            imageGpsE.setVisibility(View.GONE);
            imageGpsE.setImageBitmap(null);

        }
        else
        {
            //seccion uno
            btnPanelFueraInteE.setVisibility(View.GONE);
            imagePanelFueraInteE.setVisibility(View.GONE);
            imagePanelFueraInteE.setImageBitmap(null);
            btnPanelDentroInteE.setVisibility(View.GONE);
            imagePanelDentroInteE.setVisibility(View.GONE);
            imagePanelDentroInteE.setImageBitmap(null);
            imageLogoParaE.setVisibility(View.GONE);
            imageLogoParaE.setImageBitmap(null);
            btnLogoParaFrontalE.setVisibility(View.GONE);
            btnRadioInteriorE.setVisibility(View.GONE);
            imageRadioInteriorE.setVisibility(View.GONE);
            imageRadioInteriorE.setImageBitmap(null);
            textView38.setVisibility(View.GONE);
            marcaPanel.setVisibility(View.GONE);
            textView42.setVisibility(View.GONE);
            textView37.setVisibility(View.GONE);
            panel.setVisibility(View.GONE);
            tipoRadio.setVisibility(View.GONE);
            textView39.setVisibility(View.GONE);
            modeloPanel.setVisibility(View.GONE);
            btnKilometrajeE.setVisibility(View.GONE);
            imageKilometrajeE.setVisibility(View.GONE);
            imageKilometrajeE.setImageBitmap(null);
            textViewK.setVisibility(View.GONE);
            Ekilometraje.setVisibility(View.GONE);
            btnAdicionalInteriorE.setVisibility(View.GONE);
            imageAdicionalInteriorE.setVisibility(View.GONE);
            imageAdicionalInteriorE.setImageBitmap(null);
            luzCheckEngineE.setVisibility(View.GONE);
            luzTestigoAirE.setVisibility(View.GONE);
            luzTestigoABS.setVisibility(View.GONE);
            textCant19.setVisibility(View.GONE);
            contPost19.setVisibility(View.GONE);
            textCant20.setVisibility(View.GONE);
            contPost20.setVisibility(View.GONE);
            textCant21.setVisibility(View.GONE);
            contPost21.setVisibility(View.GONE);
            textCant22.setVisibility(View.GONE);
            contPost22.setVisibility(View.GONE);
            textCant23.setVisibility(View.GONE);
            contPost23.setVisibility(View.GONE);
            textCant14.setVisibility(View.VISIBLE);
            contPost14.setVisibility(View.VISIBLE);


            imageRetroElectE.setImageBitmap(null);
            parlantesE.setVisibility(View.GONE);
            imageParlantesE.setVisibility(View.GONE);
            imageParlantesE.setImageBitmap(null);
            textView43.setVisibility(View.GONE);
            textView44.setVisibility(View.GONE);
            textView45.setVisibility(View.GONE);
            textView46.setVisibility(View.GONE);
            textView47.setVisibility(View.GONE);
            textView48.setVisibility(View.GONE);
            textView50.setVisibility(View.GONE);
            cantidadParlante.setVisibility(View.GONE);
            parlanteMarca.setVisibility(View.GONE);
            parlanteModelo.setVisibility(View.GONE);
            anteElectCheck.setVisibility(View.GONE);
            radioTransCheck.setVisibility(View.GONE);
            cantidadTeewter.setVisibility(View.GONE);
            textView51.setVisibility(View.GONE);
            cantidadTeewter.setVisibility(View.GONE);
            marcaTeewter.setVisibility(View.GONE);
            textView52.setVisibility(View.GONE);
            modeloTeewter.setVisibility(View.GONE);
            textView53.setVisibility(View.GONE);
            tweeter.setVisibility(View.GONE);
            textView49.setVisibility(View.GONE);
            //checkWoofer.setVisibility(View.GONE);
            textView54.setVisibility(View.GONE);
            cantidadWoofer.setVisibility(View.GONE);
            textView55.setVisibility(View.GONE);
            woofermarca.setVisibility(View.GONE);
            textView56.setVisibility(View.GONE);
            woofermodelo.setVisibility(View.GONE);
            textView58.setVisibility(View.GONE);
            //amplifi1check.setVisibility(View.GONE);
            textView63.setVisibility(View.GONE);
            cantidadampli1.setVisibility(View.GONE);
            textView64.setVisibility(View.GONE);
            ampli1marca.setVisibility(View.GONE);
            textView65.setVisibility(View.GONE);
            textView66.setVisibility(View.GONE);
            spinner_amplif1.setVisibility(View.GONE);
            textView59.setVisibility(View.GONE);
            //amplifi2check.setVisibility(View.GONE);
            textView67.setVisibility(View.GONE);
            cantidadampli2.setVisibility(View.GONE);
            textView68.setVisibility(View.GONE);
            amplimarca2.setVisibility(View.GONE);
            textView69.setVisibility(View.GONE);
            amplimodelo2.setVisibility(View.GONE);
            textView70.setVisibility(View.GONE);
            ampli1modelo.setVisibility(View.GONE);
            apernadoTeewter.setVisibility(View.GONE);
            imageTweeterE.setVisibility(View.GONE);
            imageTweeterE.setImageBitmap(null);
            apernadoAmplifi1.setVisibility(View.GONE);
            imageAmplifiUnoE.setVisibility(View.GONE);
            imageAmplifiUnoE.setImageBitmap(null);
            apernadoAmplifi2.setVisibility(View.GONE);
            imageAmplifiDosE.setVisibility(View.GONE);
            imageAmplifiDosE.setImageBitmap(null);
            apernadoWoofer.setVisibility(View.GONE);
            imageWooferE.setVisibility(View.GONE);
            imageWooferE.setImageBitmap(null);
            apernadoPantalla.setVisibility(View.GONE);
            imagePantallaDvdE.setVisibility(View.GONE);
            imagePantallaDvdE.setImageBitmap(null);
            textView57.setVisibility(View.GONE);
           // textView36.setVisibility(View.GONE);
            //textView36.setVisibility(View.GONE);
            //textView37.setVisibility(View.GONE);
            //spinner_panelR.setVisibility(View.GONE);
            //.setVisibility(View.GONE);
            //tipoRadio.setVisibility(View.GONE);
            spinner_amplif2.setVisibility(View.GONE);
            spinner_ubic_w.setVisibility(View.GONE);
            textView61.setVisibility(View.GONE);
            apernadoPantalla.setVisibility(View.GONE);
            textView75.setVisibility(View.GONE);
            pantallaCantidad.setVisibility(View.GONE);
            textView76.setVisibility(View.GONE);
            pantallaMarca.setVisibility(View.GONE);
            textView77.setVisibility(View.GONE);
            pantallaModelo.setVisibility(View.GONE);
            textView78.setVisibility(View.GONE);
            spinner_pantalla.setVisibility(View.GONE);



            String imageRetroElect = db.foto(Integer.parseInt(id), "Foto Retrovisor Eléctrico Interior");
            //String imageLuzCheckEngine = db.foto(Integer.parseInt(id),"Foto Check Engine Interior");
           // String imageluzTestigoAir = db.foto(Integer.parseInt(id),"Foto Luz Testigo  Airbags Interior");
            //String imageControlCruce = db.foto(Integer.parseInt(id),"Foto Control Crucero Interior");
            String imageBluetooth = db.foto(Integer.parseInt(id),"Foto Bluetooh Interior");
            String imageTapizCuero = db.foto(Integer.parseInt(id),"Foto Tapiz de Cuero Interior");
            String imageButacaElect = db.foto(Integer.parseInt(id),"Foto Butaca Eléctrica Interior");
            String imageCortaCorri = db.foto(Integer.parseInt(id),"Foto Corta Corriente Interior");
            String imageAlzavidrioDe = db.foto(Integer.parseInt(id),"Foto Alza Vidrio Delantero Interior");
           // String imageAlzavidrioTr = db.foto(Integer.parseInt(id),"Foto Alza Vidrio Trasero Interior");
            String imageGps = db.foto(Integer.parseInt(id), "Foto GPS Interior");



            if (imageRetroElect.length() > 3) {
                byte[] decodedString = Base64.decode(imageRetroElect, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageRetroElectE.setImageBitmap(decodedByte);
                retroElectE.setChecked(true);
            }

            if (imageGps.length() > 3) {
                byte[] decodedString = Base64.decode(imageGps, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageGpsE.setImageBitmap(decodedByte);
                gpsE.setChecked(true);
            }


           /* if(imageLuzCheckEngine.length()>3)
            {
                byte[] decodedString = Base64.decode(imageLuzCheckEngine, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageLuzCheckEngineE.setImageBitmap(decodedByte);
                luzCheckEngineE.setChecked(true);
            }
            if(imageluzTestigoAir.length()>3)
            {
                byte[] decodedString = Base64.decode(imageluzTestigoAir, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageluzTestigoAirE.setImageBitmap(decodedByte);
                luzTestigoAirE.setChecked(true);
            }
            if(imageControlCruce.length()>3)
            {
                byte[] decodedString = Base64.decode(imageControlCruce, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageControlCruceE.setImageBitmap(decodedByte);
                controlCruceE.setChecked(true);
            }*/
            if(imageBluetooth.length()>3)
            {
                byte[] decodedString = Base64.decode(imageBluetooth, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageBluetoothE.setImageBitmap(decodedByte);
                bluetoothE.setChecked(true);
            }
            if(imageTapizCuero.length()>3)
            {
                byte[] decodedString = Base64.decode(imageTapizCuero, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageTapizCueroE.setImageBitmap(decodedByte);
                tapizCueroE.setChecked(true);
            }
            if(imageButacaElect.length()>3)
            {
                byte[] decodedString = Base64.decode(imageButacaElect, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageButacaElectE.setImageBitmap(decodedByte);
                butacaElectE.setChecked(true);
            }
            if(imageCortaCorri.length()>3)
            {
                byte[] decodedString = Base64.decode(imageCortaCorri, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageCortaCorriE.setImageBitmap(decodedByte);
                cortaCorriE.setChecked(true);
            }
            if(imageAlzavidrioDe.length()>3)
            {
                byte[] decodedString = Base64.decode(imageAlzavidrioDe, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageAlzavidrioDeE.setImageBitmap(decodedByte);
                alzavidrioDeE.setChecked(true);
            }
            /*if(imageAlzavidrioTr.length()>3)
            {
                byte[] decodedString = Base64.decode(imageAlzavidrioTr, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageAlzavidrioTrE.setImageBitmap(decodedByte);
                alzavidrioTrE.setChecked(true);
            }*/



            retroElectE.setVisibility(View.VISIBLE);
            imageRetroElectE.setVisibility(View.VISIBLE);
            //luzCheckEngineE.setVisibility(View.VISIBLE);
            linea.setVisibility(View.VISIBLE);
            //imageLuzCheckEngineE.setVisibility(View.VISIBLE);
            //luzTestigoAirE.setVisibility(View.VISIBLE);
            //imageluzTestigoAirE.setVisibility(View.VISIBLE);
            controlCruceE.setVisibility(View.VISIBLE);
            //imageControlCruceE.setVisibility(View.VISIBLE);
            bluetoothE.setVisibility(View.VISIBLE);
            linea.setVisibility(View.VISIBLE);
            imageBluetoothE.setVisibility(View.VISIBLE);
            tapizCueroE.setVisibility(View.VISIBLE);
            imageTapizCueroE.setVisibility(View.VISIBLE);
            butacaElectE.setVisibility(View.VISIBLE);
            imageButacaElectE.setVisibility(View.VISIBLE);
            cortaCorriE.setVisibility(View.VISIBLE);
            imageCortaCorriE.setVisibility(View.VISIBLE);
            alzavidrioDeE.setVisibility(View.VISIBLE);
            imageAlzavidrioDeE.setVisibility(View.VISIBLE);
            gpsE.setVisibility(View.VISIBLE);
            imageGpsE.setVisibility(View.VISIBLE);
            imageGpsE.setImageBitmap(null);
            //alzavidrioTrE.setVisibility(View.VISIBLE);
            //imageAlzavidrioTrE.setVisibility(View.VISIBLE);
            //txtAlzavidrioE.setVisibility(View.VISIBLE);


        }

    }
    private void desplegarCamposSeccionTresMQ(String id){
        if (textView44.getVisibility() == View.VISIBLE) {
            imageRetroElectE.setImageBitmap(null);
            parlantesE.setVisibility(View.GONE);
            imageParlantesE.setVisibility(View.GONE);
            imageParlantesE.setImageBitmap(null);
            textView43.setVisibility(View.GONE);
            textView44.setVisibility(View.GONE);
            textView45.setVisibility(View.GONE);
            textView46.setVisibility(View.GONE);
            textView47.setVisibility(View.GONE);
            textView48.setVisibility(View.GONE);
            textView50.setVisibility(View.GONE);
            cantidadParlante.setVisibility(View.GONE);
            parlanteMarca.setVisibility(View.GONE);
            parlanteModelo.setVisibility(View.GONE);
            anteElectCheck.setVisibility(View.GONE);
            radioTransCheck.setVisibility(View.GONE);
            apernadoTeewter.setVisibility(View.GONE);
            cantidadTeewter.setVisibility(View.GONE);
            marcaTeewter.setVisibility(View.GONE);
            textView52.setVisibility(View.GONE);
            modeloTeewter.setVisibility(View.GONE);
            textView53.setVisibility(View.GONE);
            tweeter.setVisibility(View.GONE);
            textView49.setVisibility(View.GONE);
            apernadoAmplifi1.setVisibility(View.GONE);
            apernadoAmplifi2.setVisibility(View.GONE);
            textView54.setVisibility(View.GONE);
            cantidadWoofer.setVisibility(View.GONE);
            textView55.setVisibility(View.GONE);
            woofermarca.setVisibility(View.GONE);
            textView56.setVisibility(View.GONE);
            textView58.setVisibility(View.GONE);
            //amplifi1check.setVisibility(View.GONE);
            textView63.setVisibility(View.GONE);
            cantidadampli1.setVisibility(View.GONE);
            textView64.setVisibility(View.GONE);
            ampli1marca.setVisibility(View.GONE);
            textView65.setVisibility(View.GONE);
            textView66.setVisibility(View.GONE);
            spinner_amplif1.setVisibility(View.GONE);
            textView59.setVisibility(View.GONE);
            //amplifi2check.setVisibility(View.GONE);
            textView67.setVisibility(View.GONE);
            textView57.setVisibility(View.GONE);
            cantidadampli2.setVisibility(View.GONE);
            textView68.setVisibility(View.GONE);
            amplimarca2.setVisibility(View.GONE);
            textView69.setVisibility(View.GONE);
            amplimodelo2.setVisibility(View.GONE);
            textView70.setVisibility(View.GONE);
            ampli1modelo.setVisibility(View.GONE);
            textView51.setVisibility(View.GONE);
            apernadoTeewter.setVisibility(View.GONE);
            imageTweeterE.setVisibility(View.GONE);
            imageTweeterE.setImageBitmap(null);
            imageAmplifiUnoE.setVisibility(View.GONE);
            imageAmplifiUnoE.setImageBitmap(null);
            imageAmplifiDosE.setVisibility(View.GONE);
            imageAmplifiDosE.setImageBitmap(null);
            apernadoWoofer.setVisibility(View.GONE);
            imageWooferE.setVisibility(View.GONE);
            imageWooferE.setImageBitmap(null);
            imagePantallaDvdE.setVisibility(View.GONE);
            imagePantallaDvdE.setImageBitmap(null);
            //.setVisibility(View.GONE);
            //textView37.setVisibility(View.GONE);
            //spinner_panelR.setVisibility(View.GONE);
            //textView42.setVisibility(View.GONE);
            //tipoRadio.setVisibility(View.GONE);
            woofermodelo.setVisibility(View.GONE);
            spinner_amplif2.setVisibility(View.GONE);
            spinner_ubic_w.setVisibility(View.GONE);
            textView61.setVisibility(View.GONE);
            apernadoPantalla.setVisibility(View.GONE);
            textView75.setVisibility(View.GONE);
            pantallaCantidad.setVisibility(View.GONE);
            textView76.setVisibility(View.GONE);
            pantallaMarca.setVisibility(View.GONE);
            textView77.setVisibility(View.GONE);
            pantallaModelo.setVisibility(View.GONE);
            textView78.setVisibility(View.GONE);
            spinner_pantalla.setVisibility(View.GONE);


        }
        else {
            //seccion uno
            btnPanelFueraInteE.setVisibility(View.GONE);
            imagePanelFueraInteE.setVisibility(View.GONE);
            imagePanelFueraInteE.setImageBitmap(null);
            btnPanelDentroInteE.setVisibility(View.GONE);
            imagePanelDentroInteE.setVisibility(View.GONE);
            imagePanelDentroInteE.setImageBitmap(null);
            imageLogoParaE.setVisibility(View.GONE);
            imageLogoParaE.setImageBitmap(null);
            btnLogoParaFrontalE.setVisibility(View.GONE);
            btnRadioInteriorE.setVisibility(View.GONE);
            imageRadioInteriorE.setVisibility(View.GONE);
            imageRadioInteriorE.setImageBitmap(null);
            textView38.setVisibility(View.GONE);
            marcaPanel.setVisibility(View.GONE);
            textView42.setVisibility(View.GONE);
            textView37.setVisibility(View.GONE);
            panel.setVisibility(View.GONE);
            tipoRadio.setVisibility(View.GONE);
            textView39.setVisibility(View.GONE);
            modeloPanel.setVisibility(View.GONE);
            btnKilometrajeE.setVisibility(View.GONE);
            imageKilometrajeE.setVisibility(View.GONE);
            imageKilometrajeE.setImageBitmap(null);
            btnAdicionalInteriorE.setVisibility(View.GONE);
            imageAdicionalInteriorE.setVisibility(View.GONE);
            imageAdicionalInteriorE.setImageBitmap(null);
            luzCheckEngineE.setVisibility(View.GONE);
            luzTestigoAirE.setVisibility(View.GONE);
            luzTestigoABS.setVisibility(View.GONE);
            textCant19.setVisibility(View.GONE);
            contPost19.setVisibility(View.GONE);
            textCant20.setVisibility(View.GONE);
            contPost20.setVisibility(View.GONE);
            textCant21.setVisibility(View.GONE);
            contPost21.setVisibility(View.GONE);
            textCant22.setVisibility(View.GONE);
            contPost22.setVisibility(View.GONE);
            textCant23.setVisibility(View.GONE);
            contPost23.setVisibility(View.GONE);
            textCant14.setVisibility(View.VISIBLE);
            contPost14.setVisibility(View.VISIBLE);

            //luzCheckEngineE.setVisibility(View.GONE);
            //imageLuzCheckEngineE.setVisibility(View.GONE);
            //imageLuzCheckEngineE.setImageBitmap(null);
            //luzTestigoAirE.setVisibility(View.GONE);
            //imageluzTestigoAirE.setVisibility(View.GONE);
            //imageluzTestigoAirE.setImageBitmap(null);
            controlCruceE.setVisibility(View.GONE);
            //imageControlCruceE.setVisibility(View.GONE);
            //imageControlCruceE.setImageBitmap(null);
            bluetoothE.setVisibility(View.GONE);
            linea.setVisibility(View.GONE);
            imageBluetoothE.setVisibility(View.GONE);
            imageBluetoothE.setImageBitmap(null);
            tapizCueroE.setVisibility(View.GONE);
            imageTapizCueroE.setVisibility(View.GONE);
            imageTapizCueroE.setImageBitmap(null);
            butacaElectE.setVisibility(View.GONE);
            imageButacaElectE.setVisibility(View.GONE);
            imageButacaElectE.setImageBitmap(null);
            cortaCorriE.setVisibility(View.GONE);
            imageCortaCorriE.setVisibility(View.GONE);
            imageCortaCorriE.setImageBitmap(null);
            alzavidrioDeE.setVisibility(View.GONE);
            imageAlzavidrioDeE.setVisibility(View.GONE);
            imageAlzavidrioDeE.setImageBitmap(null);
            //alzavidrioTrE.setVisibility(View.GONE);
            //imageAlzavidrioTrE.setVisibility(View.GONE);
            //imageAlzavidrioTrE.setImageBitmap(null);
            //txtAlzavidrioE.setVisibility(View.GONE);
            retroElectE.setVisibility(View.GONE);
            imageRetroElectE.setImageBitmap(null);
            imageRetroElectE.setVisibility(View.GONE);
            gpsE.setVisibility(View.GONE);
            imageGpsE.setVisibility(View.GONE);
            imageGpsE.setImageBitmap(null);


            String imageParlantes = db.foto(Integer.parseInt(id), "Foto Parlantes Interior");
            String imageTweeter = db.foto(Integer.parseInt(id), "Foto Tweeter Interior");
            String imageAmplifiUno = db.foto(Integer.parseInt(id), "Foto Amplificador Uno Interior");
            String imageAmplifiDos = db.foto(Integer.parseInt(id), "Foto Amplificador Dos Interior");
            String imageWoofer = db.foto(Integer.parseInt(id), "Foto Woofer Interior");
            String imagePantallaDvd = db.foto(Integer.parseInt(id), "Foto Pantalla DVD Interior");
            //String imageLectorDvd = db.foto(Integer.parseInt(id), "Foto Lector DVD");
           // String imageModular = db.foto(Integer.parseInt(id), "Foto Modular DVD");
           // String imageGps = db.foto(Integer.parseInt(id), "Foto GPS Interior");




            if (imageParlantes.length() > 3) {
                byte[] decodedString = Base64.decode(imageParlantes, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageParlantesE.setImageBitmap(decodedByte);
                parlantesE.setChecked(true);
            }
            if (imageTweeter.length() > 3) {
                byte[] decodedString = Base64.decode(imageTweeter, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageTweeterE.setImageBitmap(decodedByte);
                apernadoTeewter.setChecked(true);
            }
            if (imageAmplifiUno.length() > 3) {
                byte[] decodedString = Base64.decode(imageAmplifiUno, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageAmplifiUnoE.setImageBitmap(decodedByte);
                apernadoAmplifi1.setChecked(true);
            }
            if (imageAmplifiDos.length() > 3) {
                byte[] decodedString = Base64.decode(imageAmplifiDos, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageAmplifiDosE.setImageBitmap(decodedByte);
                apernadoAmplifi2.setChecked(true);
            }
            if (imageWoofer.length() > 3) {
                byte[] decodedString = Base64.decode(imageWoofer, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageWooferE.setImageBitmap(decodedByte);
                apernadoWoofer.setChecked(true);
            }
            if (imagePantallaDvd.length() > 3) {
                byte[] decodedString = Base64.decode(imagePantallaDvd, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imagePantallaDvdE.setImageBitmap(decodedByte);
                apernadoPantalla.setChecked(true);
            }

            /*if (imageLectorDvd.length() > 3) {
                byte[] decodedString = Base64.decode(imageLectorDvd, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageLectorDvdE.setImageBitmap(decodedByte);
                apernadoDvd.setChecked(true);
            }*/

           /* if (imageModular.length() > 3) {
                byte[] decodedString = Base64.decode(imageModular, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageModularDvd.setImageBitmap(decodedByte);
                apernadoDvdModular.setChecked(true);
            }*/



            parlantesE.setVisibility(View.VISIBLE);
            imageParlantesE.setVisibility(View.VISIBLE);
            textView43.setVisibility(View.VISIBLE);
            textView44.setVisibility(View.VISIBLE);
            textView45.setVisibility(View.VISIBLE);
            textView46.setVisibility(View.VISIBLE);
            textView47.setVisibility(View.VISIBLE);
            textView48.setVisibility(View.VISIBLE);
            textView50.setVisibility(View.VISIBLE);
            cantidadParlante.setVisibility(View.VISIBLE);
            parlanteMarca.setVisibility(View.VISIBLE);
            parlanteModelo.setVisibility(View.VISIBLE);
            anteElectCheck.setVisibility(View.VISIBLE);
            radioTransCheck.setVisibility(View.VISIBLE);
            //tweeterApernado.setVisibility(View.VISIBLE);
            cantidadTeewter.setVisibility(View.VISIBLE);
            marcaTeewter.setVisibility(View.VISIBLE);
            textView52.setVisibility(View.VISIBLE);
            modeloTeewter.setVisibility(View.VISIBLE);
            textView53.setVisibility(View.VISIBLE);
            tweeter.setVisibility(View.VISIBLE);
            textView49.setVisibility(View.VISIBLE);
            //checkWoofer.setVisibility(View.VISIBLE);
            textView54.setVisibility(View.VISIBLE);
            cantidadWoofer.setVisibility(View.VISIBLE);
            textView55.setVisibility(View.VISIBLE);
            woofermarca.setVisibility(View.VISIBLE);
            textView56.setVisibility(View.VISIBLE);
            textView58.setVisibility(View.VISIBLE);
            //amplifi1check.setVisibility(View.VISIBLE);
            textView63.setVisibility(View.VISIBLE);
            cantidadampli1.setVisibility(View.VISIBLE);
            textView64.setVisibility(View.VISIBLE);
            ampli1marca.setVisibility(View.VISIBLE);
            textView65.setVisibility(View.VISIBLE);
            textView66.setVisibility(View.VISIBLE);
            spinner_amplif1.setVisibility(View.VISIBLE);
            spinner_amplif2.setVisibility(View.VISIBLE);
            textView59.setVisibility(View.VISIBLE);
            textView67.setVisibility(View.VISIBLE);
            cantidadampli2.setVisibility(View.VISIBLE);
            textView68.setVisibility(View.VISIBLE);
            amplimarca2.setVisibility(View.VISIBLE);
            textView69.setVisibility(View.VISIBLE);
            amplimodelo2.setVisibility(View.VISIBLE);
            textView70.setVisibility(View.VISIBLE);
            ampli1modelo.setVisibility(View.VISIBLE);
            textView51.setVisibility(View.VISIBLE);
            apernadoTeewter.setVisibility(View.VISIBLE);
            imageTweeterE.setVisibility(View.VISIBLE);
            apernadoAmplifi1.setVisibility(View.VISIBLE);
            imageAmplifiUnoE.setVisibility(View.VISIBLE);
            apernadoAmplifi2.setVisibility(View.VISIBLE);
            imageAmplifiDosE.setVisibility(View.VISIBLE);
            apernadoWoofer.setVisibility(View.VISIBLE);
            imageWooferE.setVisibility(View.VISIBLE);
            apernadoPantalla.setVisibility(View.VISIBLE);
            imagePantallaDvdE.setVisibility(View.VISIBLE);
            textView57.setVisibility(View.VISIBLE);
            //textView36.setVisibility(View.VISIBLE);
            //textView37.setVisibility(View.VISIBLE);
            //spinner_panelR.setVisibility(View.VISIBLE);
            //textView42.setVisibility(View.VISIBLE);
            //tipoRadio.setVisibility(View.VISIBLE);
            woofermodelo.setVisibility(View.VISIBLE);
            spinner_ubic_w.setVisibility(View.VISIBLE);
            textView61.setVisibility(View.VISIBLE);
            apernadoPantalla.setVisibility(View.VISIBLE);
            textView75.setVisibility(View.VISIBLE);
            pantallaCantidad.setVisibility(View.VISIBLE);
            textView76.setVisibility(View.VISIBLE);
            pantallaMarca.setVisibility(View.VISIBLE);
            textView77.setVisibility(View.VISIBLE);
            pantallaModelo.setVisibility(View.VISIBLE);
            textView78.setVisibility(View.VISIBLE);
            spinner_pantalla.setVisibility(View.VISIBLE);


        }
    }

    private  void DesplegarCamposSeccionOBD(String id)    {


        if (btnPanelFueraInteE.getVisibility()==View.VISIBLE)
        {


            imageOBDE.setVisibility(View.GONE);
            imageOBDE.setImageBitmap(null);
            imageLLavesE.setVisibility(View.GONE);
            imageLLavesE.setImageBitmap(null);


            textCant24.setVisibility(View.GONE);
            contPost24.setVisibility(View.GONE);
            textCant25.setVisibility(View.GONE);
            contPost25.setVisibility(View.GONE);

        }
        else
        {

            //seccion tres

            imageOBDE.setVisibility(View.GONE);
            imageOBDE.setImageBitmap(null);
            imageLLavesE.setVisibility(View.GONE);
            imageLLavesE.setImageBitmap(null);


            String imageOBD = db.foto(Integer.parseInt(id),"Foto obd");
            String imageLLaves = db.foto(Integer.parseInt(id),"Foto llaves");
            //String imagePanelDentroInte = db.foto(Integer.parseInt(id),"Foto Panel desde Dentro Interior");

            if(imageOBD.length()>3)
            {
                byte[] decodedString = Base64.decode(imageOBD, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageOBDE.setImageBitmap(decodedByte);
            }

            if(imageLLaves.length()>3)
            {
                byte[] decodedString = Base64.decode(imageLLaves, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageLLavesE.setImageBitmap(decodedByte);
            }


            btnOBDE.setVisibility(View.VISIBLE);
            imageOBDE.setVisibility(View.VISIBLE);
            btnLLavesE.setVisibility(View.VISIBLE);
            imageLLavesE.setVisibility(View.VISIBLE);

            textCant24.setVisibility(View.VISIBLE);
            contPost24.setVisibility(View.VISIBLE);
            textCant25.setVisibility(View.VISIBLE);
            contPost25.setVisibility(View.VISIBLE);


        }
    }

    public void guardarDatos(){
        try{

            //PANTALLA
            JSONObject valor39 = new JSONObject();
            valor39.put("valor_id",254);
            valor39.put("texto",pantallaa.getSelectedItem().toString());
            JSONObject valor38 = new JSONObject();
            valor38.put("valor_id",253);
            valor38.put("texto",pantallaModelo.getText().toString());
            JSONObject valor37 = new JSONObject();
            valor37.put("valor_id",252);
            valor37.put("texto",pantallaMarca.getText().toString());
            JSONObject valor36 = new JSONObject();
            valor36.put("valor_id",255);
            valor36.put("texto",pantallaCantidad.getText().toString());
            JSONObject valor35 = new JSONObject();
            valor35.put("valor_id",251);
            valor35.put("texto",apernadoPantallas);

           JSONObject valor29 = new JSONObject();
            valor29.put("valor_id",231);
            valor29.put("texto",amplificador2.getSelectedItem().toString());
            JSONObject valor28 = new JSONObject();
            valor28.put("valor_id",230);
            valor28.put("texto",ampli2modelo.getText().toString());
            JSONObject valor27 = new JSONObject();
            valor27.put("valor_id",229);
            valor27.put("texto",ampli2marca.getText().toString());
            JSONObject valor26 = new JSONObject();
            valor26.put("valor_id",228);
            valor26.put("texto",ampli2cantidad.getText().toString());
            JSONObject valor25 = new JSONObject();
            valor25.put("valor_id",227);
            valor25.put("texto",apernadoAmplifi2s);
            //Amplificador 1
           JSONObject valor24 = new JSONObject();
            valor24.put("valor_id",225);
            valor24.put("texto",amplificador1.getSelectedItem().toString());
            JSONObject valor23 = new JSONObject();
            valor23.put("valor_id",224);
            valor23.put("texto",ampli1modelo.getText().toString());
            JSONObject valor22 = new JSONObject();
            valor22.put("valor_id",223);
            valor22.put("texto",ampli1marca.getText().toString());
            JSONObject valor21 = new JSONObject();
            valor21.put("valor_id",222);
            valor21.put("texto",ampli1Cantidad.getText().toString());
            JSONObject valor20 = new JSONObject();
            valor20.put("valor_id",221);
            valor20.put("texto",apernadoAmplifi1s);
            //WOOFER
            JSONObject valor19 = new JSONObject();
            valor19.put("valor_id",249);
            valor19.put("texto",ubicacion_w.getSelectedItem().toString());
            JSONObject valor18 = new JSONObject();
            valor18.put("valor_id",248);
            valor18.put("texto",woofermodelo.getText().toString());
            JSONObject valor17 = new JSONObject();
            valor17.put("valor_id",247);
            valor17.put("texto",woofermarca.getText().toString());
            JSONObject valor16 = new JSONObject();
            valor16.put("valor_id",246);
            valor16.put("texto",woofercantidad.getText().toString());
            JSONObject valor15 = new JSONObject();
            valor15.put("valor_id",245);
            valor15.put("texto",apernadoWoofers);
            //TWEETER
            JSONObject valor14 = new JSONObject();
            valor14.put("valor_id",239);
            valor14.put("texto",apernadoTeewters);
            JSONObject valor13 = new JSONObject();
            valor13.put("valor_id",242);
            valor13.put("texto",modeloTeewter.getText().toString());
            JSONObject valor12 = new JSONObject();
            valor12.put("valor_id",241);
            valor12.put("texto",marcaTeewter.getText().toString());
            JSONObject valor11 = new JSONObject();
            valor11.put("valor_id",240);
            valor11.put("texto",cantidadTeewter.getText().toString());
            JSONObject valor10 = new JSONObject();
            valor10.put("valor_id",243);
            valor10.put("texto",tweeter.getSelectedItem().toString());

            //ANTENA
            JSONObject valor9 = new JSONObject();
            valor9.put("valor_id",797);
            valor9.put("texto",radiotransmisors);
            JSONObject valor8 = new JSONObject();
            valor8.put("valor_id",219);
            valor8.put("texto",eletricros);
            //PANEL
           JSONObject valor1 = new JSONObject();
            valor1.put("valor_id",211);
            valor1.put("texto",panel.getSelectedItem().toString());

            JSONObject valor2 = new JSONObject();
            valor2.put("valor_id",213);
            valor2.put("texto",marcaPanel.getText().toString());
           JSONObject valor3 = new JSONObject();
            valor3.put("valor_id",214);
            valor3.put("texto",modeloPanel.getText().toString());

            JSONObject valor4 = new JSONObject();
            valor4.put("valor_id",212);
            valor4.put("texto",tipoRadio.getSelectedItem().toString());
            //Parlante
           JSONObject valor5 = new JSONObject();
            valor5.put("valor_id",216);
            valor5.put("texto",parlanteCantidad.getText().toString());
            JSONObject valor6 = new JSONObject();
            valor6.put("valor_id",217);
            valor6.put("texto",parlanteMarca.getText().toString());
            JSONObject valor7 = new JSONObject();
            valor7.put("valor_id",323);
            valor7.put("texto",parlanteModelo.getText().toString());

            JSONObject valorK = new JSONObject();
            valorK.put("valor_id",296);
            valorK.put("texto",Ekilometraje.getText().toString());


            JSONArray jsonArray = new JSONArray();
            jsonArray.put(valor39);
            jsonArray.put(valor38);
            jsonArray.put(valor37);
            jsonArray.put(valor36);
            jsonArray.put(valor35);
            jsonArray.put(valor29);
            jsonArray.put(valor28);
            jsonArray.put(valor27);
            jsonArray.put(valor26);
            jsonArray.put(valor25);
            jsonArray.put(valor24);
            jsonArray.put(valor23);
            jsonArray.put(valor22);
            jsonArray.put(valor21);
            jsonArray.put(valor20);
            jsonArray.put(valor19);
            jsonArray.put(valor18);
            jsonArray.put(valor17);
            jsonArray.put(valor16);
            jsonArray.put(valor15);
            jsonArray.put(valor14);
            jsonArray.put(valor13);
            jsonArray.put(valor12);
            jsonArray.put(valor11);
            jsonArray.put(valor10);
            jsonArray.put(valor9);
            jsonArray.put(valor8);
            jsonArray.put(valor1);
            jsonArray.put(valor2);
            jsonArray.put(valor3);
            jsonArray.put(valor4);
            jsonArray.put(valor5);
            jsonArray.put(valor6);
            jsonArray.put(valor7);
            jsonArray.put(valorK);



            Log.i("por  aca","por aca" + jsonArray.put(valorK));
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
                    Log.e("INSERTA EN  CODIGO ", db.insertarValor(Integer.parseInt(id_inspeccion),llenado.getInt("valor_id"),llenado.getString("texto")));


                }
            }



        }catch (Exception e)
        {

            Toast.makeText(interior.this,e.getMessage(),Toast.LENGTH_SHORT);
        }
    }

}
