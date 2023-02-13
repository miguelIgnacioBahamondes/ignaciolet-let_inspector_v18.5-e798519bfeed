package com.letchile.let.VehLiviano;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;


import com.letchile.let.BD.DBprovider;
import com.letchile.let.BuildConfig;
import com.letchile.let.Clases.PropiedadesFoto;
import com.letchile.let.Clases.PropiedadesTexto;
import com.letchile.let.Clases.Validaciones;
import com.letchile.let.InsPendientesActivity;
import com.letchile.let.R;
import com.letchile.let.Servicios.ConexionInternet;
import com.letchile.let.Servicios.TransferirFoto;
import com.letchile.let.VehLiviano.Fotos.documento;
import com.letchile.let.VehPesado.DatosInspVpActivity;
import com.letchile.let.VehLiviano.seccion2;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class DatosInspActivity extends AppCompatActivity {

    private static String APP_DIRECTORY = "";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY ;
    private final int MY_PERMISSIONS = 100;

    DBprovider db;
    Button btnFotocomprobante,btbVolver,BtnPendientes,BtnGuardar,mOptionButton,btnConvertibleE;
    EditText usrInspector,direccionIns,fechaIns,horaIns,entrevistado,mailJg;
    Spinner region,comuna,spinnerInsp;
    private String mPath;
    private File ruta_sd;
    private String ruta = "";
    private final int PHOTO_COMPROBANTE = 250;
    private final int PHOTO_CONVERTIBLE=350;
    ImageView imagenCompro,imageConvertibleE;
    Boolean connec = false;
    PropiedadesFoto foto;
    Context contexto = this;
    String nombreimagen = "", imagenComprobante, imagenConvertible = "";
    private TextView textCant8,contPost8,textCantCom,contPostCom;
    JSONObject llenado;
    int correlativo = 0;
    String id_inspeccion;
    AutoCompleteTextView comunaa;



    public DatosInspActivity(){db = new DBprovider(this);foto = new PropiedadesFoto(this);};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_insp);



        connec = new ConexionInternet(this).isConnectingToInternet();

        //Traspaso de datos
        Bundle bundle = getIntent().getExtras();
        id_inspeccion=bundle.getString("id_inspeccion");


        //FOTO
        //imagenCompro = findViewById(R.id.imagenCompro);

        direccionIns =  findViewById(R.id.direccionInspe);
        direccionIns.setOnEditorActionListener(new PropiedadesTexto());
        direccionIns.setText(db.accesorio(Integer.parseInt(id_inspeccion),358));

        fechaIns =  findViewById(R.id.fechaInsp);
        fechaIns.setOnEditorActionListener(new PropiedadesTexto());
        fechaIns.setText(db.accesorio(Integer.parseInt(id_inspeccion),360));

        horaIns =  findViewById(R.id.horaInsp);
        horaIns.setOnEditorActionListener(new PropiedadesTexto());
        horaIns.setText(db.accesorio(Integer.parseInt(id_inspeccion),361));

        entrevistado =  findViewById(R.id.entrevistadoInsp);
        entrevistado.setOnEditorActionListener(new PropiedadesTexto());
        entrevistado.setText(db.accesorio(Integer.parseInt(id_inspeccion),755));


        // cargar un combo inspeccion por
        spinnerInsp = findViewById(R.id.spinnerInsp);
        String[] arraytipo = getResources().getStringArray(R.array.insp);
        final List<String> arraytipolist = Arrays.asList(arraytipo);
        ArrayAdapter spinner_adapter = ArrayAdapter.createFromResource( this, R.array.insp , android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerInsp.setAdapter(spinner_adapter);
        spinnerInsp.setSelection(arraytipolist.lastIndexOf("Nueva"));


        //region

       /* String regionInicial=db.obtenerRegion(db.accesorio(Integer.parseInt(id_inspeccion),359).toString());
        String listaRegiones[][]=db.listaRegiones();
        region = findViewById(R.id.regionSpinnerMQ);
        String[] arraySpinner = new String[listaRegiones.length+1];
        arraySpinner[0]=regionInicial;
        for(int i=0;i<listaRegiones.length;i++)        {
            arraySpinner[i+1]=listaRegiones[i][0];
        }
        ArrayAdapter<String> adapterRegion = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapterRegion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        region.setAdapter(adapterRegion);


        comuna = findViewById(R.id.comunaSpinner);
        region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                comuna.setAdapter(null);
                String regionSelected = region.getSelectedItem().toString();
                String listaComuna[][] = db.listaComunas(regionSelected);
                String[] spinnercomuna = new String[listaComuna.length+1];
                spinnercomuna[0] = db.accesorio(Integer.parseInt(id_inspeccion),359).toString();
                for(int i=0;i<listaComuna.length;i++){
                    spinnercomuna[i+1] = listaComuna[i][0];
                }
                ArrayAdapter<String> adapterComuna = new ArrayAdapter<String>(DatosInspActivity.this,android.R.layout.simple_spinner_item,spinnercomuna);
                adapterComuna.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                comuna.setAdapter(adapterComuna);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/


        //foto convertible
        btnConvertibleE = findViewById(R.id.btnConvertibleE);
        imageConvertibleE = findViewById(R.id.imageConvertibleE);

        textCant8 = findViewById(R.id.textCant8);
        contPost8 = findViewById(R.id.contPost8);
        contPost8.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Convertible")));

        imagenConvertible = db.foto(Integer.parseInt(id_inspeccion),"Foto Convertible");
        btnConvertibleE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                funcionCamara(id_inspeccion,"_Foto_Convertible.jpg",PHOTO_CONVERTIBLE);

            }
        });

        if(imagenConvertible.length()>=3 )
        {
            byte[] decodedString = Base64.decode(imagenConvertible, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageConvertibleE.setImageBitmap(decodedByte);
            imageConvertibleE.setVisibility(View.VISIBLE);
        }


        // AUTOCOMPLETAR REGION
        String regionInicial2=db.obtenerRegion(db.accesorio(Integer.parseInt(id_inspeccion),359).toString());
        final AutoCompleteTextView region = (AutoCompleteTextView) findViewById(R.id.regionSpinnerMQ);

        String listaRegiones2[][]=db.listaRegiones();
        final String[] arraySpinner2 = new String[listaRegiones2.length+1];
        arraySpinner2[0]=regionInicial2;

        for(int i=0;i<listaRegiones2.length;i++)
        {
            arraySpinner2[i+1]=listaRegiones2[i][0];


        }
        ArrayAdapter<String> adapterRegion2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner2);
        adapterRegion2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        region.setAdapter(adapterRegion2);

        //valida region
        region.setValidator(new AutoCompleteTextView.Validator() {
            @Override
            public boolean isValid (CharSequence text){
                //some logic here returns true or false based on if the text is validated
                Arrays.sort(arraySpinner2);

                if (Arrays.binarySearch(arraySpinner2, text.toString()) > 0) {
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


       comunaa = (AutoCompleteTextView) findViewById(R.id.comunaSpinner);
        region.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Rescata el nombre del item elegido
                String regionSelected = region.getText().toString();

                //Rescata la lista según el item elegido
                String listaComunas[][] = db.listaComunas(regionSelected);

                final String[] arrayComuna = new String[listaComunas.length+1];
                arrayComuna[0] = db.accesorio(Integer.parseInt(id_inspeccion),359).toString();
                for(int i=0;i<listaComunas.length;i++){
                    arrayComuna[i+1] = listaComunas[i][0];
                }
                ArrayAdapter<String> adapterComuna = new ArrayAdapter<String>(DatosInspActivity.this,android.R.layout.simple_spinner_item,arrayComuna);
                adapterComuna.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                comunaa.setAdapter(adapterComuna);

                //valida  comuna
                comunaa.setValidator(new AutoCompleteTextView.Validator() {
                    @Override
                    public boolean isValid (CharSequence text){
                        //some logic here returns true or false based on if the text is validated
                        Arrays.sort(arrayComuna);
                        if (Arrays.binarySearch(arrayComuna, text.toString()) > 0) {
                            return true;

                        }
                        else{

                            Toast.makeText(DatosInspActivity.this, "Debe ingresar comuna", Toast.LENGTH_SHORT).show();
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

        comunaa.setText(db.accesorio(Integer.parseInt(id_inspeccion),7).toString());

        //foto comprobante
       /* btnFotocomprobante = findViewById(R.id.fotoComprobante);
        btnFotocomprobante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {openCamaraComprobante(Integer.parseInt(id_inspeccion));}
        });*/

        //image view
       /* imagenComprobante = db.foto(Integer.parseInt(id_inspeccion),"Foto Comprobante");

        if(imagenComprobante.length()>=3 )
        {
            byte[] decodedString = Base64.decode(imagenComprobante, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imagenCompro.setImageBitmap(decodedByte);
            imagenCompro.setVisibility(View.VISIBLE);
        }*/


       // mailJg = findViewById(R.id.mailJg);




        //Botón volver de sección datos de inspeccion
        final Button btnSigInspJg = findViewById(R.id.btnSigInspJg);
        btnSigInspJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String regi = region.getText().toString();
                String comu = comunaa.getText().toString();
                String entrevi = entrevistado.getText().toString();

                //String getText=mailJg.getText().toString();

                /*String Expn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                                +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"

                                +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."

                                +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"

                                +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"

                                +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";*/

               if(regi.equals("") || comu.equals("") || entrevi.equals(""))
                {
                    Toast.makeText(DatosInspActivity.this,"Debe ingresar región, comuna y entrevistado." ,Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Toast.makeText(DatosInspActivity.this,"email valido" + getText.matches(Expn),Toast.LENGTH_SHORT).show();
                    //insertar en tabla oi email
                    //db.insertaOiEmail(Integer.parseInt(id_inspeccion),mailJg.getText().toString());

                    guardarDatos();
                    Intent intent = new Intent(DatosInspActivity.this, ObsActivity.class);
                    intent.putExtra("id_inspeccion", id_inspeccion);
                    startActivity(intent);

                    //Log.i("lalla","lalal" +  db.insertaOiEmail(Integer.parseInt(id_inspeccion),mailJg.getText().toString()));
                }


               /* if(imagenCompro.getVisibility()==View.GONE){
                    Toast.makeText(DatosInspActivity.this,"Falta la foto de comprobante",Toast.LENGTH_SHORT).show();
                }else{*/
              /* if(mailJg.getText().toString().length() == 0)
               {
                   Toast.makeText(DatosInspActivity.this,"Debe ingresar email",Toast.LENGTH_SHORT).show();
               }
               else
               {


               }*/
               // }

            }
        });

        //Botón volver pendiente
        final Button btnPenInspJg = findViewById(R.id.btnPenInspJg);
        btnPenInspJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarDatos();
                Intent intent = new Intent( DatosInspActivity.this, InsPendientesActivity.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
            }
        });

        //Botón volver de secciones
        final Button btnVolverInspJg = findViewById(R.id.btnVolverInspJg);
        btnVolverInspJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarDatos();
                Intent intent = new Intent( DatosInspActivity.this, seccion2.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
            }
        });



    }



    public void guardarDatos(){
        try {
            //LLENO EL JSON

            JSONObject datosValorIns = new JSONObject();
            datosValorIns.put("valor_id",1);
            datosValorIns.put("texto",spinnerInsp.getSelectedItem().toString());

            JSONObject datosValorA = new JSONObject();
            datosValorA.put("valor_id",358);
            datosValorA.put("texto",direccionIns.getText().toString());

            JSONObject datosValorB = new JSONObject();
            datosValorB.put("valor_id",360);
            datosValorB.put("texto",fechaIns.getText().toString());

            JSONObject datosValorC = new JSONObject();
            datosValorC.put("valor_id",361);
            datosValorC.put("texto",horaIns.getText().toString());

            JSONObject datosValorD = new JSONObject();
            datosValorD.put("valor_id",755);
            datosValorD.put("texto",entrevistado.getText().toString());

            JSONObject datosValorCo = new JSONObject();
            datosValorCo.put("valor_id",359);
            datosValorCo.put("texto",comunaa.getText().toString());

            JSONArray jsonArray = new JSONArray();

            jsonArray.put(datosValorIns);
            jsonArray.put(datosValorA);
            jsonArray.put(datosValorB);
            jsonArray.put(datosValorC);
            jsonArray.put(datosValorD);
            jsonArray.put(datosValorCo);


            //PREGUNTO SI ES NULO PARA INSERTAR LOS DATOS

            Log.e("largo json ", Integer.toString(jsonArray.length()));
            if (!jsonArray.isNull(0)) {
                for(int i=0;i<jsonArray.length();i++){

                    Log.e("valor ii ", Integer.toString(i));
                    llenado = new JSONObject(jsonArray.getString(i));
                    Log.e("valor json ", jsonArray.getString(i));

                    Log.e("INSERTA EN  CODIGO ", llenado.getString("valor_id"));


                    db.insertarValor(Integer.parseInt(id_inspeccion),llenado.getInt("valor_id"),llenado.getString("texto"));
                    //validaciones.insertarDatos(Integer.parseInt(id_inspeccion),llenado.getInt("valor_id"),llenado.getString("texto"));
                    Log.e("INSERTA EN  CODIGO ", db.insertarValor(Integer.parseInt(id_inspeccion),llenado.getInt("valor_id"),llenado.getString("texto")));



                }
            }

        }catch (Exception e)
        {
            Toast.makeText(DatosInspActivity.this,e.getMessage(),Toast.LENGTH_SHORT);
        }
    }

    private void openCamaraComprobante(int id_inspeccion){
        ruta_sd = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString() + '/' + id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated) {
            correlativo = db.correlativoFotos(id_inspeccion);
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Comprobante.jpg";
            ruta = file.toString() + "/" + nombreimagen;
            mPath = ruta;
            File newFile = new File(mPath);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra("nombreImg",nombreimagen);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(DatosInspActivity.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            startActivityForResult(intent, PHOTO_COMPROBANTE);
        }
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
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(DatosInspActivity.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            startActivityForResult(intent, CodigoFoto);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion=bundle.getString("id_inspeccion");

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_COMPROBANTE:
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
                    imagenCompro.setImageBitmap(bitmap);
                    imagenCompro.setVisibility(View.VISIBLE);
                    String imagen = foto.convertirImagenDano(bitmap);

                    db.insertaFoto(Integer.parseInt(id_inspeccion), correlativo, nombreimagen, "Foto Comprobante", 0, imagen,0);
                    break;
                case  PHOTO_CONVERTIBLE:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitmapConvertible = BitmapFactory.decodeFile(mPath);
                    bitmapConvertible = foto.redimensiomarImagen(bitmapConvertible);

                    String imagenConvetible = foto.convertirImagenDano(bitmapConvertible);
                    db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Convertible", 0, imagenConvetible,0);

                    imagenConvetible = "data:image/jpg;base64,"+imagenConvetible;
                    String base64Image5 = imagenConvetible.split(",")[1];
                    byte[] decodedString5 = Base64.decode(base64Image5, Base64.DEFAULT);
                    Bitmap decodedByte5 = BitmapFactory.decodeByteArray(decodedString5, 0, decodedString5.length);
                    imageConvertibleE.setImageBitmap(decodedByte5);

                    Intent servis5 = new Intent(DatosInspActivity.this, TransferirFoto.class);
                    servis5.putExtra("comentario","Foto Convertible");
                    servis5.putExtra("id_inspeccion",id_inspeccion);
                    startService(servis5);

                    int cantFoto8=db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Convertible");
                    cantFoto8= cantFoto8 +1;
                    db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Foto Convertible",cantFoto8);
                    contPost8.setText(String.valueOf(cantFoto8));

                    break;
            }
            //TRANSFERIR FOTO
            Intent servis = new Intent(contexto, TransferirFoto.class);
            servis.putExtra("comentario","Foto Convertible");
            servis.putExtra("id_inspeccion",id_inspeccion);
            startService(servis);

            int cantFotoCom=db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Convertible");
            cantFotoCom= cantFotoCom +1;
            db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Foto Convertible",cantFotoCom);
            contPostCom.setText(String.valueOf(cantFotoCom));
        }
    }







}
