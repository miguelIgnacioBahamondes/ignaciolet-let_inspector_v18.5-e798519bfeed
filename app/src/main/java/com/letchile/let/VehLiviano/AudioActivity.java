package com.letchile.let.VehLiviano;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.Clases.PropiedadesTexto;
import com.letchile.let.Clases.Validaciones;
import com.letchile.let.InsPendientesActivity;
import com.letchile.let.R;
import com.letchile.let.VehLiviano.seccion2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class AudioActivity extends AppCompatActivity {

    DBprovider db;
    Spinner panel,tipoRadio,tweeter,ubicacion_w,amplificador1,amplificador2,lector,pantallaa,dvde;
    CheckBox eletricro,radiotransmisor,apernadoTeewter,apernadoWoofer,apernadoAmplifi1,apernadoAmplifi2,apernadoDvd,apernadoPantalla,apernadoDvdModular;
    String eletricros="",radiotransmisors="",apernadoTeewters="",apernadoWoofers="",apernadoAmplifi1s="",
            apernadoAmplifi2s="",apernadoDvds="",apernadoPantallas="",apernadoDvdModulars="";
    EditText marcaPanel,modeloPanel,parlanteCantidad,parlanteMarca,parlanteModelo,tweeterCantidad,tweeterMarca,tweetermodelo,
    woofercantidad,woofermarca,woofermodelo,ampli1Cantidad,ampli1marca,ampli1modelo,ampli2cantidad,ampli2marca,ampli2modelo,
    dvdCantidad,dvdMarca,dvdModelo,pantallaCantidad,pantallaMarca,pantallaModelo,dvdModularCantidad,dvdModularmarca,dvdModularmodelo;
    Button guardar,pendientes,secciones;
    JSONObject llenado;
    Validaciones validaciones;
    String id_inspeccion;

    public AudioActivity(){
        db = new DBprovider(this);validaciones=new Validaciones(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        Bundle bundle = getIntent().getExtras();
        id_inspeccion=bundle.getString("id_inspeccion");

        //Radio
        //panel
        panel = findViewById(R.id.spinner_panelR);
        String[] array = getResources().getStringArray(R.array.panel_radio);
        final List<String> arrayPanel = Arrays.asList(array);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,array);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        panel.setAdapter(spinnerAdapter);
        panel.setSelection(arrayPanel.indexOf(db.accesorio(Integer.parseInt(id_inspeccion),211)));
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



        //Parlantes
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

        //TWEETER
        apernadoTeewter = findViewById(R.id.tweeterApernado);
        if(db.accesorio(Integer.parseInt(id_inspeccion),239).equals("Ok")) {
            apernadoTeewter.setChecked(true);
            apernadoTeewters = "Ok";
        }else{
            apernadoTeewter.setChecked(false);
            apernadoTeewters = "";
        }
        apernadoTeewter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    apernadoTeewters = "Ok";
                }else{
                    apernadoTeewters = "";
                }
            }
        });
        tweeterCantidad = findViewById(R.id.cantidadTeewter);
        tweeterCantidad.setOnEditorActionListener(new PropiedadesTexto());
        tweeterCantidad.setText(db.accesorio(Integer.parseInt(id_inspeccion),240));

        tweeterMarca = findViewById(R.id.marcaTeewter);
        tweeterMarca.setOnEditorActionListener(new PropiedadesTexto());
        tweeterMarca.setText(db.accesorio(Integer.parseInt(id_inspeccion),241));

        tweetermodelo = findViewById(R.id.modeloTeewter);
        tweetermodelo.setOnEditorActionListener(new PropiedadesTexto());
        tweetermodelo.setText(db.accesorio(Integer.parseInt(id_inspeccion),242));

        tweeter = findViewById(R.id.spinner_tweeter);
        String[] ubitweeter = getResources().getStringArray(R.array.tweeter);
        final List<String> ubitweeterList = Arrays.asList(ubitweeter);
        ArrayAdapter<String>spinner_adapterTeewter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,ubitweeterList);
        spinner_adapterTeewter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tweeter.setAdapter(spinner_adapterTeewter);
        tweeter.setSelection(ubitweeterList.indexOf((db.accesorio(Integer.parseInt(id_inspeccion),243).toString())));

        //WOOFER
        apernadoWoofer = findViewById(R.id.checkWoofer);
        if(db.accesorio(Integer.parseInt(id_inspeccion),245).equals("Ok")){
            apernadoWoofer.setChecked(true);
            apernadoWoofers="Ok";
        }else{
            apernadoWoofer.setChecked(false);
            apernadoWoofers="";
        }
        apernadoWoofer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    apernadoWoofers="Ok";
                }else{
                    apernadoWoofers="";
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

        //Amplificador 1

        apernadoAmplifi1 = findViewById(R.id.amplifi1check);
        if(db.accesorio(Integer.parseInt(id_inspeccion),221).equals("Ok")){
            apernadoAmplifi1.setChecked(true);
            apernadoAmplifi1s = "Ok";
        }else{
            apernadoAmplifi1.setChecked(false);
            apernadoAmplifi1s = "";
        }
        apernadoAmplifi1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    apernadoAmplifi1s = "Ok";
                }else{
                    apernadoAmplifi1s = "";
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


        //amplificador 2
        apernadoAmplifi2 = findViewById(R.id.amplifi2check);
        if(db.accesorio(Integer.parseInt(id_inspeccion),227).equals("Ok")){
            apernadoAmplifi2.setChecked(true);
            apernadoAmplifi2s = "Ok";
        }else{
            apernadoAmplifi2.setChecked(false);
            apernadoAmplifi2s = "";
        }
        apernadoAmplifi2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    apernadoAmplifi2s = "Ok";
                }else{
                    apernadoAmplifi2s = "";
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


        //DVD
        apernadoDvd = findViewById(R.id.apernadoDvd);
        if(db.accesorio(Integer.parseInt(id_inspeccion),233).equals("Ok")){
            apernadoDvd.setChecked(true);
            apernadoDvds = "Ok";
        }else{
            apernadoDvd.setChecked(false);
            apernadoDvds = "";
        }
        apernadoDvd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    apernadoDvds = "Ok";
                }else{
                    apernadoDvds = "";
                }
            }
        });


        dvdCantidad = findViewById(R.id.dvdCantidad);
        dvdCantidad.setOnEditorActionListener(new PropiedadesTexto());
        dvdCantidad.setText(db.accesorio(Integer.parseInt(id_inspeccion),237));

        dvdMarca = findViewById(R.id.dvdMarca);
        dvdMarca.setOnEditorActionListener(new PropiedadesTexto());
        dvdMarca.setText(db.accesorio(Integer.parseInt(id_inspeccion),234));

        dvdModelo = findViewById(R.id.dvdModelo);
        dvdModelo.setOnEditorActionListener(new PropiedadesTexto());
        dvdModelo.setText(db.accesorio(Integer.parseInt(id_inspeccion),235));

        lector = findViewById(R.id.spinner_lector);
        String[] dvd = getResources().getStringArray(R.array.dvd);
        final List<String> dvdlist = Arrays.asList(dvd);
        ArrayAdapter<String> spinner_dvd = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dvdlist);
        spinner_dvd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lector.setAdapter(spinner_dvd);
        lector.setSelection(dvdlist.indexOf(db.accesorio(Integer.parseInt(id_inspeccion),236).toString()));

        //PANTALLA
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
                if(b){
                    apernadoPantallas = "Ok";
                }else{
                    apernadoPantallas = "";
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

        //dvd modular
        apernadoDvdModular = findViewById(R.id.apernadoDvdModular);
        if(db.accesorio(Integer.parseInt(id_inspeccion),757).equals("Ok")){
            apernadoDvdModular.setChecked(true);
            apernadoDvdModulars = "Ok";
        }else{
            apernadoDvdModular.setChecked(false);
            apernadoDvdModulars = "";
        }
        apernadoDvdModular.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    apernadoDvdModulars = "Ok";
                }else{
                    apernadoDvdModulars = "";
                }
            }
        });


        dvdModularCantidad = findViewById(R.id.dvdModularCantidad);
        dvdModularCantidad.setOnEditorActionListener(new PropiedadesTexto());
        dvdModularCantidad.setText(db.accesorio(Integer.parseInt(id_inspeccion),758));

        dvdModularmarca = findViewById(R.id.dvdModularmarca);
        dvdModularmarca.setOnEditorActionListener(new PropiedadesTexto());
        dvdModularmarca.setText(db.accesorio(Integer.parseInt(id_inspeccion),759));

        dvdModularmodelo = findViewById(R.id.dvdModularmodelo);
        dvdModularmodelo.setOnEditorActionListener(new PropiedadesTexto());
        dvdModularmodelo.setText(db.accesorio(Integer.parseInt(id_inspeccion),760));

        dvde = findViewById(R.id.spinner_dvd);
        String[] dvdmodular = getResources().getStringArray(R.array.dvd);
        final List<String> dvdmodulolist = Arrays.asList(dvdmodular);
        ArrayAdapter<String> spinner_dvdmodulo = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dvdmodulolist);
        spinner_dvdmodulo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dvde.setAdapter(spinner_dvdmodulo);
        dvde.setSelection(dvdmodulolist.indexOf(db.accesorio(Integer.parseInt(id_inspeccion),761).toString()));



        //Botón guardar
        guardar = findViewById(R.id.btnSigAuJg);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //guardarDatos();
                Intent i = new Intent(AudioActivity.this,DatosInspActivity.class);
                i.putExtra("id_inspeccion",id_inspeccion);
                startActivity(i);
            }
        });


        final Button btnVolverAuJg = (Button)findViewById(R.id.btnVolverAuJg);
        btnVolverAuJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // guardarDatos();
                Intent intent = new Intent( AudioActivity.this, seccion2.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
            }
        });

        final Button btnPenAuJg = (Button)findViewById(R.id.btnPenAuJg);
        btnPenAuJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // guardarDatos();
                Intent intent = new Intent( AudioActivity.this, InsPendientesActivity.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
            }
        });

    }

   public void guardarDatos(){
        try{
            //DVD MODULAR
            JSONObject valor44 = new JSONObject();
            valor44.put("valor_id",761);
            valor44.put("texto",dvde.getSelectedItem().toString());
            JSONObject valor43 = new JSONObject();
            valor43.put("valor_id",760);
            valor43.put("texto",dvdModularmodelo.getText().toString());
            JSONObject valor42 = new JSONObject();
            valor42.put("valor_id",759);
            valor42.put("texto",dvdModularmarca.getText().toString());
            JSONObject valor41 = new JSONObject();
            valor41.put("valor_id",758);
            valor41.put("texto",dvdModularCantidad.getText().toString());
            JSONObject valor40 = new JSONObject();
            valor40.put("valor_id",757);
            valor40.put("texto",apernadoDvdModulars);
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
            //DVD
            JSONObject valor34 = new JSONObject();
            valor34.put("valor_id",236);
            valor34.put("texto",lector.getSelectedItem().toString());
            JSONObject valor33 = new JSONObject();
            valor33.put("valor_id",235);
            valor33.put("texto",dvdModelo.getText().toString());
            JSONObject valor32 = new JSONObject();
            valor32.put("valor_id",234);
            valor32.put("texto",dvdMarca.getText().toString());
            JSONObject valor31 = new JSONObject();
            valor31.put("valor_id",237);
            valor31.put("texto",dvdCantidad.getText().toString());
            JSONObject valor30 = new JSONObject();
            valor30.put("valor_id",233);
            valor30.put("texto",apernadoDvds);
            //amplificador 2
            JSONObject valor29 = new JSONObject();
            valor29.put("valor_id",231);
            valor29.put("texto",amplificador2.getSelectedItem().toString());
            JSONObject valor28 = new JSONObject();
            valor28.put("valor_id",230);
            valor28.put("texto",ampli2modelo.getText());
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
            valor14.put("valor_id",243);
            valor14.put("texto",tweeter.getSelectedItem().toString());
            JSONObject valor13 = new JSONObject();
            valor13.put("valor_id",242);
            valor13.put("texto",tweetermodelo.getText().toString());
            JSONObject valor12 = new JSONObject();
            valor12.put("valor_id",241);
            valor12.put("texto",tweeterMarca.getText().toString());
            JSONObject valor11 = new JSONObject();
            valor11.put("valor_id",240);
            valor11.put("texto",tweeterCantidad.getText().toString());
            JSONObject valor10 = new JSONObject();
            valor10.put("valor_id",239);
            valor10.put("texto",apernadoTeewters);

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

            JSONArray datosvalores = new JSONArray();
            datosvalores.put(valor1);
            datosvalores.put(valor2);
            datosvalores.put(valor3);
            datosvalores.put(valor4);
            datosvalores.put(valor5);
            datosvalores.put(valor6);
            datosvalores.put(valor7);
            datosvalores.put(valor8);
            datosvalores.put(valor9);
            datosvalores.put(valor10);
            datosvalores.put(valor11);
            datosvalores.put(valor12);
            datosvalores.put(valor13);
            datosvalores.put(valor14);
            datosvalores.put(valor15);
            datosvalores.put(valor16);
            datosvalores.put(valor17);
            datosvalores.put(valor18);
            datosvalores.put(valor19);
            datosvalores.put(valor20);
            datosvalores.put(valor21);
            datosvalores.put(valor22);
            datosvalores.put(valor23);
            datosvalores.put(valor24);
            datosvalores.put(valor25);
            datosvalores.put(valor26);
            datosvalores.put(valor27);
            datosvalores.put(valor28);
            datosvalores.put(valor29);
            datosvalores.put(valor30);
            datosvalores.put(valor31);
            datosvalores.put(valor32);
            datosvalores.put(valor33);
            datosvalores.put(valor34);
            datosvalores.put(valor35);
            datosvalores.put(valor36);
            datosvalores.put(valor37);
            datosvalores.put(valor38);
            datosvalores.put(valor39);
            datosvalores.put(valor40);
            datosvalores.put(valor41);
            datosvalores.put(valor42);
            datosvalores.put(valor43);
            datosvalores.put(valor44);


            if(!datosvalores.isNull(0)){
                for(int i=0;i<datosvalores.length();i++){
                    llenado = new JSONObject(datosvalores.getString(i));
                    db.insertarValor(Integer.parseInt(id_inspeccion),llenado.getInt("valor_id"),llenado.getString("texto"));
                    //validaciones.insertarDatos(Integer.parseInt(id_inspeccion),llenado.getInt("valor_id"),llenado.getString("texto"));
                }
            }
        }catch (Exception e){
            Toast.makeText(AudioActivity.this,"Error conversión json",Toast.LENGTH_SHORT);
        }
    }
}
