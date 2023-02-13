package com.letchile.let.VehLiviano;

import android.content.Context;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
import com.letchile.let.Servicios.TransferirFoto;
import com.letchile.let.VehLiviano.Fotos.Posterior;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatosVehActivity extends AppCompatActivity {

    private static String APP_DIRECTORY = "";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY ;
    private final int TAKE_GAS = 200;
    private final int TAKE_ELECTRICO = 300;
    Spinner tipo_veh,uso_veh,transmision, placaAdult,Tpasajero,Ptotal;
    EditText patente,marca,modelo,subModelo,color,anio,nPuertas,nMotor,nChasis;
    CheckBox bencina,Diesel,gasLicuado,electrico,placaAdulterada,cuatroxcuatro,iimportDirec;
    JSONObject llenado;
    DBprovider db;
    String id_inspeccion,nombre_foto = "", nombreimagen = "",ruta = "",mPath;
    Validaciones validaciones;
    ImageView imageGas,imageElec;
    PropiedadesFoto foto;
    int correlativo = 0;
    private File ruta_sd;


    //variables con una s alfinal para diferenciar de los checkbox
    String bencinas="",Diesels="",gasLicuados="",electricos="",placaAdulteradas="",cuatroxcuatros="",iimportDirecs="";

    public DatosVehActivity(){
        db = new DBprovider(this);foto=new PropiedadesFoto(this);validaciones = new Validaciones(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_veh);

        Bundle bundle = getIntent().getExtras();
        id_inspeccion=bundle.getString("id_inspeccion");


        //Spinner
        //inicializo el spinner
        tipo_veh = findViewById(R.id.spinner_tipo_veh);
        //traigo el array de datos
        String[] arraytipo = getResources().getStringArray(R.array.tipo_veh);
        //creo una lista para insertar todos los datos
        final List<String> arraytipolist = Arrays.asList(arraytipo);
        //creo el adapter para llenar el spinner con la lista
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arraytipolist);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //se inserta el adapter
        tipo_veh.setAdapter(spinner_adapter);
        //se selecciona el que está guardado en la base de datos
        tipo_veh.setSelection(arraytipolist.indexOf(db.accesorio(Integer.parseInt(id_inspeccion),18).toString()));



        uso_veh = findViewById(R.id.spinner_uso_veh);
        String[] arrayuso = getResources().getStringArray(R.array.uso_veh);
        final List<String> arrayusolist = Arrays.asList(arrayuso);
        ArrayAdapter<String> spinner_adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayuso);
        spinner_adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        uso_veh.setAdapter(spinner_adapter2);
        uso_veh.setSelection(arrayusolist.indexOf(db.accesorio(Integer.parseInt(id_inspeccion),20).toString()));

        transmision = findViewById(R.id.spinner_trans);
        String[] arrayTrans = getResources().getStringArray(R.array.trans);
        final  List<String> arrayTransList = Arrays.asList(arrayTrans);
        ArrayAdapter<String> spinner_adapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayTransList);
        spinner_adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transmision.setAdapter(spinner_adapter3);
        transmision.setSelection(arrayTransList.indexOf(db.accesorio(Integer.parseInt(id_inspeccion),550).toString()));

        Tpasajero = findViewById(R.id.spinner_Tpasajero);
        String[] arrayTpasajero = getResources().getStringArray(R.array.Tpasajero);
        final  List<String> arrayTpasajeroList = Arrays.asList(arrayTpasajero);
        ArrayAdapter<String> spinner_adapter6 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayTpasajeroList);
        spinner_adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Tpasajero.setAdapter(spinner_adapter6);
        Tpasajero.setSelection(arrayTpasajeroList.indexOf(db.accesorio(Integer.parseInt(id_inspeccion),966).toString()));

        Ptotal = findViewById(R.id.spinner_Ptotal);
        String[] arrayPtotal = getResources().getStringArray(R.array.Ptotal);
        final  List<String> arrayPtotalList = Arrays.asList(arrayPtotal);
        ArrayAdapter<String> spinner_adapter5 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayPtotalList);
        spinner_adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Ptotal.setAdapter(spinner_adapter5);
        Ptotal.setSelection(arrayPtotalList.indexOf(db.accesorio(Integer.parseInt(id_inspeccion),967).toString()));

       /* placaAdult = findViewById(R.id.spinner_placAdult);
        String[] arrayPlaca = getResources().getStringArray(R.array.combo_placaAdul);
        final  List<String> arrayPlacaList = Arrays.asList(arrayPlaca);
        ArrayAdapter<String> spinner_adapter4 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayPlacaList);
        spinner_adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        placaAdult.setAdapter(spinner_adapter4);
        placaAdult.setSelection(arrayPlacaList.indexOf(db.accesorio(Integer.parseInt(id_inspeccion),790).toString()));*/

        //Edit Text
        patente = findViewById(R.id.patenteM);
        patente.setOnEditorActionListener(new PropiedadesTexto());
        patente.setText(db.accesorio(Integer.parseInt(id_inspeccion),363).toString());


        marca = findViewById(R.id.marcaM);
        marca.setOnEditorActionListener(new PropiedadesTexto());
        marca.setText(db.accesorio(Integer.parseInt(id_inspeccion),10).toString());

        modelo =findViewById(R.id.modeloM);
        modelo.setOnEditorActionListener(new PropiedadesTexto());
        modelo.setText(db.accesorio(Integer.parseInt(id_inspeccion),11).toString());

        subModelo = findViewById(R.id.subModelo);
        subModelo.setOnEditorActionListener(new PropiedadesTexto());
        subModelo.setText(db.accesorio(Integer.parseInt(id_inspeccion),12).toString());

        color = findViewById(R.id.colorM);
        color.setOnEditorActionListener(new PropiedadesTexto());
        color.setText(db.accesorio(Integer.parseInt(id_inspeccion),14).toString());

        anio = findViewById(R.id.AnioM);
        anio.setOnEditorActionListener(new PropiedadesTexto());
        anio.setText(db.accesorio(Integer.parseInt(id_inspeccion),13).toString());

        nPuertas = findViewById(R.id.nPuertasM);
        nPuertas.setOnEditorActionListener(new PropiedadesTexto());
        nPuertas.setText(db.accesorio(Integer.parseInt(id_inspeccion),15).toString());

        nMotor = findViewById(R.id.nMotorM);
        nMotor.setOnEditorActionListener(new PropiedadesTexto());
        nMotor.setText(db.accesorio(Integer.parseInt(id_inspeccion),16).toString());

        /*nChasis = findViewById(R.id.nChasisM);
        nChasis.setOnEditorActionListener(new PropiedadesTexto());
        nChasis.setText(db.accesorio(Integer.parseInt(id_inspeccion),17).toString());*/

        gasLicuado = findViewById(R.id.checkGasL);
        imageGas = findViewById(R.id.imageGas);

        electrico = findViewById(R.id.checkEl);
        imageElec = findViewById(R.id.imageElec);

        //Checkbox 1
        bencina = findViewById(R.id.checkBencina);
        //db.accesorio(Integer.parseInt(id_inspeccion),385).toString();//(()).equals("Ok");
        if((db.accesorio(Integer.parseInt(id_inspeccion),385).toString()).equals("Ok"))
        {
            bencina.setChecked(true);
            bencinas = "Ok";
        }else{
            bencina.setChecked(false);
            bencinas = "";
        }
        bencina.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean estaCheck) {
                if(estaCheck){

                    bencinas = "Ok";

                    Diesel.setChecked(false);
                    gasLicuado.setChecked(false);
                    electrico.setChecked(false);
                }else{
                    bencinas = "";
                }
            }
        });


        Diesel = findViewById(R.id.checkDiesel);
        if((db.accesorio(Integer.parseInt(id_inspeccion),21)).equals("Ok"))
        {
            Diesel.setChecked(true);
            Diesels = "Ok";
        }else{
            Diesel.setChecked(false);
            Diesels = "";
        }
        Diesel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean estaCheck) {
                if(estaCheck){

                    Diesels = "Ok";

                    bencina.setChecked(false);
                    gasLicuado.setChecked(false);
                    electrico.setChecked(false);
                }else{
                    Diesels = "";
                }
            }
        });


        gasLicuado.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),551));
        gasLicuado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String imagenGasL = db.foto(Integer.parseInt(id_inspeccion),"Gas Licuado");
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 551).toString().equals("Ok")) {


                        funcionCamara(id_inspeccion,"_Foto_Gas_Licuado.jpg",TAKE_GAS);

                        byte[] decodedString = Base64.decode(imagenGasL, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imageGas.setImageBitmap(decodedByte);

                        gasLicuado.setChecked(true);
                        gasLicuados = "Ok";

                        bencina.setChecked(false);
                        Diesel.setChecked(false);
                        electrico.setChecked(false);
                        imageElec.setImageBitmap(null);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),551,"");

                    imageGas.setImageBitmap(null);
                    imageElec.setImageBitmap(null);
                    gasLicuado.setChecked(false);
                    gasLicuados = "";
                }
            }
        });

        electrico.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),549));
        electrico.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 549).toString().equals("Ok")) {
                        funcionCamara(id_inspeccion,"_Foto_Electrico.jpg",TAKE_ELECTRICO);
                        Log.i("bbb","bbb");
                        electrico.setChecked(true);
                        electricos = "Ok";

                        bencina.setChecked(false);
                        Diesel.setChecked(false);
                        gasLicuado.setChecked(false);
                        imageGas.setImageBitmap(null);

                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),549,"");
                    Log.i("zzz","zzz");
                    imageElec.setImageBitmap(null);
                    imageGas.setImageBitmap(null);
                    electrico.setChecked(false);
                    electricos = "";
                }
            }
        });

        if((db.accesorio(Integer.parseInt(id_inspeccion),549)).equals("Ok"))
        {
            String imagenElectrico = db.foto(Integer.parseInt(id_inspeccion),"Combustion electrica");
            if(imagenElectrico.length()>=3 ) {

                if (electrico.isChecked()) {
                    byte[] decodedStringE = Base64.decode(imagenElectrico, Base64.DEFAULT);
                    Bitmap decodedByteE = BitmapFactory.decodeByteArray(decodedStringE, 0, decodedStringE.length);
                    imageElec.setImageBitmap(decodedByteE);

                } else {
                    imageElec.setImageBitmap(null);

                }
            }
        }else{

            electrico.setChecked(false);
            electricos = "";
            imageElec.setImageBitmap(null);
        }

       if((db.accesorio(Integer.parseInt(id_inspeccion),551)).equals("Ok"))
        {
            String imagenGasL = db.foto(Integer.parseInt(id_inspeccion),"Gas Licuado");
            if(imagenGasL.length()>=3 ) {



                byte[] decodedString = Base64.decode(imagenGasL, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageGas.setImageBitmap(decodedByte);

            }
        }else{
            gasLicuado.setChecked(false);
            gasLicuados = "";
            imageGas.setImageBitmap(null);
        }

       /* gasLicuado = findViewById(R.id.checkGasL);
        if((db.accesorio(Integer.parseInt(id_inspeccion),551)).equals("Ok"))
        {
            gasLicuado.setChecked(true);
            gasLicuados = "Ok";
        }else{
            gasLicuado.setChecked(false);
            gasLicuados = "";
        }
        gasLicuado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean estaCheck) {
                if(estaCheck){

                    gasLicuados = "Ok";

                    bencina.setChecked(false);
                    Diesel.setChecked(false);
                    electrico.setChecked(false);
                }else{

                    gasLicuados = "";

                }
            }
        });*/


        //electrico = findViewById(R.id.checkEl);
       /* if((db.accesorio(Integer.parseInt(id_inspeccion),549)).equals("Ok"))
        {
            Log.i("ppp", "ppp");
            electrico.setChecked(true);
            electricos = "Ok";
        }else{
            electrico.setChecked(false);
            electricos = "";
        }*/
        /*electrico.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean estaCheck) {
                if(estaCheck){

                    electricos = "Ok";

                    bencina.setChecked(false);
                    Diesel.setChecked(false);
                    gasLicuado.setChecked(false);
                }else{
                    electricos = "";
                }
            }
        });*/


        //Checkbox 2
        /*placaAdulterada = findViewById(R.id.checkPlaca);
        if((db.accesorio(Integer.parseInt(id_inspeccion),790)).equals("Ok"))
        {
            placaAdulterada.setChecked(true);
            placaAdulteradas="Ok";
        }else{
            placaAdulterada.setChecked(false);
            placaAdulteradas="";
        }
        placaAdulterada.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean estaCheck) {
                if(estaCheck){
                    placaAdulteradas="Ok";
                }else{
                    placaAdulteradas="";
                }
            }
        });*/

        cuatroxcuatro = findViewById(R.id.check4x4);
        if((db.accesorio(Integer.parseInt(id_inspeccion),19)).equals("Ok"))
        {
            cuatroxcuatro.setChecked(true);
            cuatroxcuatros = "Ok";
        }else{
            cuatroxcuatro.setChecked(false);
            cuatroxcuatros = "";
        }
        cuatroxcuatro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean estaCheck) {
                if(estaCheck){
                    cuatroxcuatros = "Ok";
                }else{
                    cuatroxcuatros = "";
                }
            }
        });

        iimportDirec = findViewById(R.id.checkImpDir);
        if((db.accesorio(Integer.parseInt(id_inspeccion),357)).equals("Ok"))
        {
            iimportDirec.setChecked(true);
            iimportDirecs = "Ok";
        }else{
            iimportDirec.setChecked(false);
            iimportDirecs = "";
        }
        iimportDirec.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean estaCheck) {
                if(estaCheck){
                    iimportDirecs = "Ok";
                }else{
                    iimportDirecs = "";
                }
            }
        });



        //botón guardar y siguiente de datos de vehículos
        final Button btnGuardar1JG = findViewById(R.id.btnSigVehJg);
        btnGuardar1JG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*codigo de validacion de datos obligatorios*/

                /*if (nChasis.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(DatosVehActivity.this, "Debe digitar chasis de vehículo.",
                            Toast.LENGTH_SHORT).show();

                }else if(nChasis.length()!=17)
                {

                    Toast.makeText(DatosVehActivity.this, "N° Chasis debe  contener 17 caracteres.",
                            Toast.LENGTH_SHORT).show();

                }*/
                if (anio.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(DatosVehActivity.this, "Debe digitar año de vehículo.",
                            Toast.LENGTH_SHORT).show();
                }
                else if (Tpasajero.getSelectedItem().toString().equalsIgnoreCase("Seleccione..."))
                {
                    Toast.makeText(DatosVehActivity.this, "Debe responder pregunta ¿El vehículo es o será usado como trasporte de pasajeros?.",
                            Toast.LENGTH_SHORT).show();
                }
                else if (Ptotal.getSelectedItem().toString().equalsIgnoreCase("Seleccione..."))
                {
                    Toast.makeText(DatosVehActivity.this, "Debe responder pregunta ¿El vehículo tiene pérdida total o ha sido comprado en remate?",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    guardarDatos();
                    Intent intent = new Intent( DatosVehActivity.this, AccActivity.class);
                    intent.putExtra("id_inspeccion",id_inspeccion);
                    startActivity(intent);
                }
                /*fin*/
            }
        });


        final Button btnVolverSecc = (Button)findViewById(R.id.btnVolverVehJg);
        btnVolverSecc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarDatos();
                Intent intent = new Intent( DatosVehActivity.this, seccion2.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
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
            intent.putExtra(MediaStore.EXTRA_OUTPUT,  FileProvider.getUriForFile(DatosVehActivity.this,
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
            db.cambiarEstadoInspeccion(Integer.parseInt(id_inspeccion), 1);

            if (resultCode == RESULT_OK) {
                switch (requestCode) {

                    case TAKE_GAS:

                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });

                        Bitmap bitmapGas = BitmapFactory.decodeFile(mPath);
                        bitmapGas = foto.redimensiomarImagen(bitmapGas);
                        String imagenGas = foto.convertirImagenDano(bitmapGas);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Gas Licuado", 0, imagenGas, 0);
                        db.insertarValor(Integer.parseInt(id_inspeccion), 551, "Ok");
                        imagenGas = "data:image/jpg;base64,"+imagenGas;
                        String base64Image3 = imagenGas.split(",")[1];
                        byte[] decodedString3 = Base64.decode(base64Image3, Base64.DEFAULT);
                        Bitmap decodedByte3  = BitmapFactory.decodeByteArray(decodedString3, 0, decodedString3.length);
                        imageGas.setImageBitmap(decodedByte3);


                        Intent servis = new Intent(DatosVehActivity.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Gas Licuado");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        break;

                    case TAKE_ELECTRICO:

                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });

                        Bitmap bitmapElec = BitmapFactory.decodeFile(mPath);
                        bitmapElec = foto.redimensiomarImagen(bitmapElec);
                        String imagenElec = foto.convertirImagenDano(bitmapElec);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Combustion electrica", 0, imagenElec, 0);
                        db.insertarValor(Integer.parseInt(id_inspeccion), 549, "Ok");
                        imagenElec = "data:image/jpg;base64,"+imagenElec;
                        String base64ImageE = imagenElec.split(",")[1];
                        byte[] decodedStringE = Base64.decode(base64ImageE, Base64.DEFAULT);
                        Bitmap decodedByteE  = BitmapFactory.decodeByteArray(decodedStringE, 0, decodedStringE.length);
                        imageElec.setImageBitmap(decodedByteE);


                        Intent servisE = new Intent(DatosVehActivity.this, TransferirFoto.class);
                        servisE.putExtra("comentario", "Combustion electrica");
                        servisE.putExtra("id_inspeccion", id_inspeccion);
                        startService(servisE);

                        break;

                }
            }
        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }

    }


    public void guardarDatos(){
        try{
            JSONObject datosvalor1 = new JSONObject();
            datosvalor1.put("valor_id",18);
            datosvalor1.put("texto",tipo_veh.getSelectedItem().toString());

            JSONObject datosvalor2 = new JSONObject();
            datosvalor2.put("valor_id",20);
            datosvalor2.put("texto",uso_veh.getSelectedItem().toString());

            JSONObject datosvalor3 = new JSONObject();
            datosvalor3.put("valor_id",550);
            datosvalor3.put("texto",transmision.getSelectedItem().toString());

            JSONObject datosvalor4 = new JSONObject();
            datosvalor4.put("valor_id",363);
            datosvalor4.put("texto",patente.getText().toString());

            JSONObject datosvalor5 = new JSONObject();
            datosvalor5.put("valor_id",10);
            datosvalor5.put("texto",marca.getText().toString());

            JSONObject datosvalor6 = new JSONObject();
            datosvalor6.put("valor_id",11);
            datosvalor6.put("texto",modelo.getText().toString());

            JSONObject datosvalor7 = new JSONObject();
            datosvalor7.put("valor_id",12);
            datosvalor7.put("texto",subModelo.getText().toString());

            JSONObject datosvalor8 = new JSONObject();
            datosvalor8.put("valor_id",14);
            datosvalor8.put("texto",color.getText().toString());

            JSONObject datosvalor9 = new JSONObject();
            datosvalor9.put("valor_id",13);
            datosvalor9.put("texto",anio.getText().toString());

            JSONObject datosvalor10 = new JSONObject();
            datosvalor10.put("valor_id",15);
            datosvalor10.put("texto",nPuertas.getText().toString());

            JSONObject datosvalor11 = new JSONObject();
            datosvalor11.put("valor_id",16);
            datosvalor11.put("texto",nMotor.getText().toString());

            /*JSONObject datosvalor12 = new JSONObject();
            datosvalor12.put("valor_id",17);
            datosvalor12.put("texto",nChasis.getText().toString());*/
            /*JSONObject datosvalor17 = new JSONObject();
            datosvalor17.put("valor_id",790);
            datosvalor17.put("texto",placaAdult.getSelectedItem().toString());*/


            JSONObject datosvalor13 = new JSONObject();
            datosvalor13.put("valor_id",385);
            datosvalor13.put("texto",bencinas);

            JSONObject datosvalor14 = new JSONObject();
            datosvalor14.put("valor_id",21);
            datosvalor14.put("texto",Diesels);

            JSONObject datosvalor15 = new JSONObject();
            datosvalor15.put("valor_id",551);
            datosvalor15.put("texto",gasLicuados);

            JSONObject datosvalor16 = new JSONObject();
            datosvalor16.put("valor_id",549);
            datosvalor16.put("texto",electricos);

            JSONObject datosvalor18 = new JSONObject();
            datosvalor18.put("valor_id",19);
            datosvalor18.put("texto",cuatroxcuatros);

            JSONObject datosvalor19 = new JSONObject();
            datosvalor19.put("valor_id",357);
            datosvalor19.put("texto",iimportDirecs);

            JSONObject datosvalor20 = new JSONObject();
            datosvalor20.put("valor_id",966);
            datosvalor20.put("texto",Tpasajero.getSelectedItem().toString());

            JSONObject datosvalor21 = new JSONObject();
            datosvalor21.put("valor_id",967);
            datosvalor21.put("texto",Ptotal.getSelectedItem().toString());


            JSONArray datosvalores = new JSONArray();
            datosvalores.put(datosvalor1);
            datosvalores.put(datosvalor2);
            datosvalores.put(datosvalor3);
            datosvalores.put(datosvalor4);
            datosvalores.put(datosvalor5);
            datosvalores.put(datosvalor6);
            datosvalores.put(datosvalor7);
            datosvalores.put(datosvalor8);
            datosvalores.put(datosvalor9);
            datosvalores.put(datosvalor10);
            datosvalores.put(datosvalor11);
           // datosvalores.put(datosvalor12);
            datosvalores.put(datosvalor13);
            datosvalores.put(datosvalor14);
            datosvalores.put(datosvalor15);
            datosvalores.put(datosvalor16);
            //datosvalores.put(datosvalor17);
            datosvalores.put(datosvalor18);
            datosvalores.put(datosvalor19);
            datosvalores.put(datosvalor20);
            datosvalores.put(datosvalor21);

            if(!datosvalores.isNull(0)){
                for(int i=0;i<datosvalores.length();i++){
                    llenado = new JSONObject(datosvalores.getString(i));
                    db.insertarValor(Integer.parseInt(id_inspeccion),llenado.getInt("valor_id"),llenado.getString("texto"));
                }
            }

        }catch (Exception e){
            Toast.makeText(DatosVehActivity.this,"Error en los datos",Toast.LENGTH_SHORT);
        }
    }
}
