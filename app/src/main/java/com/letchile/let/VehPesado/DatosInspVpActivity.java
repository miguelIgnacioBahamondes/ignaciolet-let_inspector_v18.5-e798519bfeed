package com.letchile.let.VehPesado;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.BuildConfig;
import com.letchile.let.Clases.PropiedadesFoto;
import com.letchile.let.Clases.PropiedadesTexto;
import com.letchile.let.InsPendientesActivity;
import com.letchile.let.R;
import com.letchile.let.Servicios.ConexionInternet;
import com.letchile.let.Servicios.TransferirFoto;
import com.letchile.let.VehLiviano.DatosInspActivity;
import com.letchile.let.VehLiviano.SeccionActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatosInspVpActivity extends AppCompatActivity {

    DBprovider db;

    EditText dirIns,fechaInsp,mailJg,horaInsp;

    private File ruta_sdV;
    private String rutaV = "";
    Spinner tipoVehVp,region,comuna, comboRegion,comboComuna;
    PropiedadesFoto fotoV;
    Boolean connec = false;
    ImageView imagenComproV;
    Button btnFotocomprobanteV;
    EditText entrevistado;
    private String mPathV;
    String nombreimagenV = "",imagenComprobanteV,id_inspeccion;
    private final int PHOTO_COMPROBANTEV = 250;
    Context contextoV = this;
    int correlativo;
    /*FALTA CARGAR COMBOS DE REGION Y COMUNA*/

    public DatosInspVpActivity(){db = new DBprovider(this);fotoV = new PropiedadesFoto(this);};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_insp_vp);


        connec = new ConexionInternet(this).isConnectingToInternet();

        //Traspaso de datos
        Bundle bundle = getIntent().getExtras();
        id_inspeccion=bundle.getString("id_inspeccion");

       // imagenComproV = findViewById(R.id.imagenComproV);

        //direccion inspeccion
        dirIns = findViewById(R.id.dirIns);
        dirIns.setOnEditorActionListener(new PropiedadesTexto());
        dirIns.setText(db.accesorio(Integer.parseInt(id_inspeccion),733));

        //fecha inspeccion
        fechaInsp = findViewById(R.id.fechaInsp);
        fechaInsp.setOnEditorActionListener(new PropiedadesTexto());
        fechaInsp.setText(db.accesorio(Integer.parseInt(id_inspeccion),737));

        //hora inspeccion
        horaInsp = findViewById(R.id.horaInsp);
        horaInsp.setOnEditorActionListener(new PropiedadesTexto());
        horaInsp.setText(db.accesorio(Integer.parseInt(id_inspeccion),738));

        //entrevistado
        entrevistado = findViewById(R.id.entrevistado);
        entrevistado.setOnEditorActionListener(new PropiedadesTexto());
        entrevistado.setText(db.accesorio(Integer.parseInt(id_inspeccion),755));

        // cargar un combo inspeccion por
        tipoVehVp = findViewById(R.id.tipo_veh_vp);
        String[] arraytipo = getResources().getStringArray(R.array.insp);
        final List<String> arraytipolist = Arrays.asList(arraytipo);
        ArrayAdapter spinner_adapter = ArrayAdapter.createFromResource( this, R.array.insp , android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoVehVp.setAdapter(spinner_adapter);
        tipoVehVp.setSelection(arraytipolist.lastIndexOf("Nueva"));

        mailJg = findViewById(R.id.mailJg);

        //region
        //String regionInicial[][]=db.obtenerRegion(db.accesorio(Integer.parseInt(id_inspeccion),359).toString());
       /* String listaRegiones[][]=db.listaRegiones();
        comboRegion = findViewById(R.id.comboRegVep);
        String[] arraySpinner = new String[listaRegiones.length+1];
        arraySpinner[0]="Seleccione";
        for(int i=0;i<listaRegiones.length;i++)        {
            arraySpinner[i+1]=listaRegiones[i][0];
        }
        ArrayAdapter<String> adapterRegion = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapterRegion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comboRegion.setAdapter(adapterRegion);


        comboComuna = findViewById(R.id.comboComVpJg);
        comboRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Rescata el nombre del item elegido
                String regionSelected = comboRegion.getSelectedItem().toString();
                //Rescata la lista según el item elegido
                String listaComunas[][] = db.listaComunas(regionSelected);
                //Inicia el nuevo combo a llenar

                //Se crea una variable array para ser llenado
                String[] spinnerComuna = new String[listaComunas.length+1];
                spinnerComuna[0] = db.accesorio(Integer.parseInt(id_inspeccion),736).toString();
                for(int i=0;i<listaComunas.length;i++){
                    spinnerComuna[i+1] = listaComunas[i][0];
                }
                ArrayAdapter<String> adapterComuna = new ArrayAdapter<String>(DatosInspVpActivity.this,android.R.layout.simple_spinner_item,spinnerComuna);
                adapterComuna.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                comboComuna.setAdapter(adapterComuna);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/


        // AUTOCOMPLETAR REGION
        String regionInicialV=db.obtenerRegion(db.accesorio(Integer.parseInt(id_inspeccion),370).toString());
        final AutoCompleteTextView regionV = (AutoCompleteTextView) findViewById(R.id.comboRegVe);

        String listaRegiones[][]=db.listaRegiones();
        final String[] arraySpinner = new String[listaRegiones.length+1];
        arraySpinner[0]=regionInicialV;

        for(int i=0;i<listaRegiones.length;i++)
        {
            arraySpinner[i+1]=listaRegiones[i][0];


        }
        ArrayAdapter<String> adapterRegion = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapterRegion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regionV.setAdapter(adapterRegion);


        //valida region
        regionV.setValidator(new AutoCompleteTextView.Validator() {
            @Override
            public boolean isValid (CharSequence text){
                //some logic here returns true or false based on if the text is validated
                Arrays.sort(arraySpinner);

                if (Arrays.binarySearch(arraySpinner, text.toString()) > 0) {
                    return true;

                }

                return false;
            }

            @Override
            public CharSequence fixText (CharSequence invalidText){
                //If .isValid() returns false then the code comes here
                //do whatever way you want to fix in the users input and  return it
                return "Región inválida";
            }
        });

        final AutoCompleteTextView comunaV = (AutoCompleteTextView) findViewById(R.id.comboComVp);
        regionV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Rescata el nombre del item elegido
                String regionSelected = regionV.getText().toString();

                //Rescata la lista según el item elegido
                String listaComunas[][] = db.listaComunas(regionSelected);

                final String[] arrayComuna = new String[listaComunas.length+1];
                arrayComuna[0] = db.accesorio(Integer.parseInt(id_inspeccion),370).toString();
                for(int i=0;i<listaComunas.length;i++){
                    arrayComuna[i+1] = listaComunas[i][0];
                }
                ArrayAdapter<String> adapterComuna = new ArrayAdapter<String>(DatosInspVpActivity.this,android.R.layout.simple_spinner_item,arrayComuna);
                adapterComuna.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                comunaV.setAdapter(adapterComuna);

                //valida  comuna
                comunaV.setValidator(new AutoCompleteTextView.Validator() {
                    @Override
                    public boolean isValid (CharSequence text){
                        //some logic here returns true or false based on if the text is validated
                        Arrays.sort(arrayComuna);
                        if (Arrays.binarySearch(arrayComuna, text.toString()) > 0) {
                            return true;

                        }
                        else{

                            Toast.makeText(DatosInspVpActivity.this, "Debe ingresar comuna", Toast.LENGTH_SHORT).show();
                            return false;
                        }


                    }

                    @Override
                    public CharSequence fixText (CharSequence invalidText){
                        //If .isValid() returns false then the code comes here
                        //do whatever way you want to fix in the users input and  return it
                        return "Comuna inválida";
                    }
                });
            }

           /* @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }*/
        });


        //FOOTER
        /*btnFotocomprobanteV = findViewById(R.id.fotoComprobanteV);
        btnFotocomprobanteV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {openCamaraComprobanteV(Integer.parseInt(id_inspeccion));}
        });*/

        //image view
       /* imagenComprobanteV = db.foto(Integer.parseInt(id_inspeccion),"Foto Comprobante");

        if(imagenComprobanteV.length()>=3 )
        {
            byte[] decodedString = Base64.decode(imagenComprobanteV, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imagenComproV.setImageBitmap(decodedByte);
            imagenComproV.setVisibility(View.VISIBLE);
        }*/



        //Botón siguiente
        final Button btnSigInspJg = findViewById(R.id.btnSigInspJg);
        btnSigInspJg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {


                String regi = regionV.getText().toString();
                String comu = comunaV.getText().toString();
              /*  String getText=mailJg.getText().toString();

                String Expn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"

                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."

                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"

                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"

                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";*/

                if(regi.equals("") || comu.equals(""))
                {
                    Toast.makeText(DatosInspVpActivity.this, "Debe ingresar región y comuna", Toast.LENGTH_SHORT).show();

                }

                else
                {

                   // db.insertaOiEmail(Integer.parseInt(id_inspeccion),mailJg.getText().toString());

                    guardarDatos();
                    Intent intent = new Intent( DatosInspVpActivity.this, ObsVpActivity.class);
                    intent.putExtra("id_inspeccion",id_inspeccion);
                    startActivity(intent);

                }
            }
        });



        //Botón volver de secciones
        final Button btnVolverInspVpJg = findViewById(R.id.btnVolverInspVpJg);
        btnVolverInspVpJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarDatos();
                Intent intent = new Intent( DatosInspVpActivity.this, SeccionVpActivity.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
            }
        });


    }



    public void guardarDatos(){
        try{

            JSONObject valorNuevo = new JSONObject();
            valorNuevo.put("valor_id",364);
            valorNuevo.put("texto",tipoVehVp.getSelectedItem().toString());

            JSONObject valor93 = new JSONObject();
            valor93.put("valor_id",733);
            valor93.put("texto",dirIns.getText().toString());

            JSONObject valor94 = new JSONObject();
            valor94.put("valor_id",737);
            valor94.put("texto",fechaInsp.getText().toString());

            JSONObject valor95 = new JSONObject();
            valor95.put("valor_id",738);
            valor95.put("texto",horaInsp.getText().toString());

            JSONObject valor96 = new JSONObject();
            valor96.put("valor_id",755);
            valor96.put("texto",entrevistado.getText().toString());

            JSONObject valor97 = new JSONObject();
            valor97.put("valor_id",364);
            valor97.put("texto",tipoVehVp.getSelectedItem().toString());

            JSONObject datosValorVpCo = new JSONObject();
            datosValorVpCo.put("valor_id",736);
            datosValorVpCo.put("texto",comboComuna.getSelectedItem().toString());


            JSONArray jsonArray = new JSONArray();
            jsonArray.put(valorNuevo);
            jsonArray.put(valor93);
            jsonArray.put(valor94);
            jsonArray.put(valor95);
            jsonArray.put(valor96);
            jsonArray.put(valor97);
            jsonArray.put(datosValorVpCo);


            JSONObject llenado;
            if (!jsonArray.isNull(0)) {
                for(int i=0;i<jsonArray.length();i++){
                    llenado = new JSONObject(jsonArray.getString(i));

                    db.insertarValor(Integer.parseInt(id_inspeccion),llenado.getInt("valor_id"),llenado.getString("texto"));

                }
            }
        }catch (Exception e)
        {

            Toast.makeText(DatosInspVpActivity.this, "", Toast.LENGTH_SHORT);
        }
    }




    private void openCamaraComprobanteV(int id_inspeccion){
        ruta_sdV = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sdV.toString() + '/' + id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated) {

            correlativo = db.correlativoFotos(id_inspeccion);
            nombreimagenV = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Comprobante.jpg";

            rutaV = file.toString() + "/" + nombreimagenV;
            mPathV = rutaV;

            File newFile = new File(mPathV);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra("nombreImg",nombreimagenV);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(DatosInspVpActivity.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            startActivityForResult(intent, PHOTO_COMPROBANTEV);

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion=bundle.getString("id_inspeccion");

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_COMPROBANTEV:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPathV}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitmap = BitmapFactory.decodeFile(mPathV);
                    bitmap = fotoV.redimensiomarImagen(bitmap);
                    imagenComproV.setImageBitmap(bitmap);
                    imagenComproV.setVisibility(View.VISIBLE);
                    String imagen = fotoV.convertirImagenDano(bitmap);

                    db.insertaFoto(Integer.parseInt(id_inspeccion), correlativo, nombreimagenV, "Foto Comprobante", 0, imagen, 0);
                    break;
            }

            //TRANSFERIR FOTO

            Intent servis = new Intent(contextoV, TransferirFoto.class);
            servis.putExtra("comentario","Foto Comprobante");
            servis.putExtra("id_inspeccion",id_inspeccion);
            startService(servis);

        }
    }


}
