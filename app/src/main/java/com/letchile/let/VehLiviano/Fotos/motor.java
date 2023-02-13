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
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.BuildConfig;
import com.letchile.let.Clases.PropiedadesFoto;
import com.letchile.let.R;
import com.letchile.let.Servicios.ConexionInternet;
import com.letchile.let.Servicios.TransferirFoto;
import com.letchile.let.VehLiviano.DatosVehActivity;
import com.letchile.let.VehLiviano.seccion2;
import com.letchile.let.Clases.PropiedadesTexto;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class motor extends AppCompatActivity {

    DBprovider db;
    Boolean connec = false;
    private final int PHOTO_CUNA = 200;
    private final int PHOTO_MOTOR = 300;
    private final int PHOTO_CHASIS = 400;
    private final int PHOTO_ADICIONAL = 500;

    private Button btnVolverMotorE,btnVolverSecMotorE,btnSiguienteMotorE,cunaMotorE,motorE,chasisVinE,adicionalE,btnSeccionUnoMotor;
    private ImageView imageCunaMotorE,imageMotorE,imageChasisVinE,imageAdicionalE;
    private String mPath;
    private File ruta_sd;
    private String ruta = "", id_inspeccion="";
    String nombreimagen = "";
    private TextView  textCant24,contPost24,textCant25,contPost25,textCant26,contPost26,textCant27,contPost27,textView11,textViewCha;
    private EditText nChasis;
    private Spinner placaAdult;
    int correlativo = 0;

    JSONObject llenado;

    PropiedadesFoto foto;

    public motor(){db = new DBprovider(this);foto=new PropiedadesFoto(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motor);

        Bundle bundle = getIntent().getExtras();
        id_inspeccion = bundle.getString("id_inspeccion");

        connec = new ConexionInternet(this).isConnectingToInternet();

        cunaMotorE = findViewById(R.id.cunaMotorE);
        imageCunaMotorE = findViewById(R.id.imageCunaMotorE);
        motorE = findViewById(R.id.motorE);
        imageMotorE = findViewById(R.id.imageMotorE);
        chasisVinE = findViewById(R.id.chasisVinE);
        imageChasisVinE = findViewById(R.id.imageChasisVinE);
        adicionalE = findViewById(R.id.adicionalE);
        imageAdicionalE = findViewById(R.id.imageAdicionalE);
        btnSeccionUnoMotor = findViewById(R.id.btnSeccionUnoMotor);

        textCant24 = findViewById(R.id.textCant24);
        contPost24 = findViewById(R.id.contPost24);
        contPost24.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Cuna Motor")));

        textCant25 = findViewById(R.id.textCant25);
        contPost25 = findViewById(R.id.contPost25);
        contPost25.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Motor")));

        textCant26 = findViewById(R.id.textCant26);
        contPost26 = findViewById(R.id.contPost26);
        contPost26.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Chasis(VIN)")));

        textCant27 = findViewById(R.id.textCant27);
        contPost27 = findViewById(R.id.contPost27);
        contPost27.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Adicional Motor")));

        cunaMotorE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {funcionCamara(id_inspeccion,"_Foto_Cuna_Motor.jpg",PHOTO_CUNA);
            }
        });
        motorE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcionCamara(id_inspeccion,"_Foto_Motor.jpg",PHOTO_MOTOR);
            }
        });
        chasisVinE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {funcionCamara(id_inspeccion,"_Foto_Chasis_Motor.jpg",PHOTO_CHASIS);
            }
        });
        adicionalE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {funcionCamara(id_inspeccion,"_Foto_Adicional_Motor.jpg",PHOTO_ADICIONAL);
            }
        });

        textView11 = findViewById(R.id.textView11);

        nChasis = findViewById(R.id.nChasisM);
        nChasis.setOnEditorActionListener(new PropiedadesTexto());
        nChasis.setText(db.accesorio(Integer.parseInt(id_inspeccion),17).toString());

        textViewCha = findViewById(R.id.textViewCha);

        placaAdult = findViewById(R.id.spinner_placAdult);
        String[] arrayPlaca = getResources().getStringArray(R.array.combo_placaAdul);
        final List<String> arrayPlacaList = Arrays.asList(arrayPlaca);
        ArrayAdapter<String> spinner_adapter4 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayPlacaList);
        spinner_adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        placaAdult.setAdapter(spinner_adapter4);
        placaAdult.setSelection(arrayPlacaList.indexOf(db.accesorio(Integer.parseInt(id_inspeccion),790).toString()));

        if (db.accesorio(Integer.parseInt(id_inspeccion),790).toString().equals("")) {
            placaAdult.setSelection(0);

            if(placaAdult.getSelectedItem().toString().equals("Seleccione..."))
            {
                placaAdult.getSelectedItem().toString().equals("");

            }


        }
        else
        {
            placaAdult.getSelectedItem().toString();

        }




        btnSeccionUnoMotor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {desplegarSeccionUnoMotor(id_inspeccion);
            }
        });

        btnVolverMotorE = findViewById(R.id.btnVolverMotorE);
        btnVolverMotorE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(motor.this, interior.class);
                intent.putExtra("id_inspeccion", id_inspeccion);
                startActivity(intent);
                finish();

                guardarDatos();
            }
        });

        btnSiguienteMotorE = findViewById(R.id.btnSiguienteMotorE);
        btnSiguienteMotorE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imageMotor = db.foto(Integer.parseInt(id_inspeccion),"Foto Motor");
                String imageChasisVin = db.foto(Integer.parseInt(id_inspeccion),"Foto Chasis(VIN)");
                String imageCunaMotor = db.foto(Integer.parseInt(id_inspeccion),"Foto Cuna Motor");

                guardarDatos();
                 if (nChasis.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(motor.this, "Debe digitar chasis de vehículo.",
                            Toast.LENGTH_SHORT).show();

                }else if(nChasis.length()!=17)
                {

                    Toast.makeText(motor.this, "N° Chasis debe  contener 17 caracteres.",
                            Toast.LENGTH_SHORT).show();

                }

                else if(imageCunaMotor.length()<=3 && imageChasisVin.length()<=3 && imageMotor.length()<=3 )  {
                    AlertDialog.Builder builder = new AlertDialog.Builder(motor.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe Tomar Fotos Obligatorias</b><p><ul><li>- Foto capot abierto</li><p><li>- Foto Motor</li></p>" +
                            "<p><li>- Foto Chavis(VIN)</li></p></ul></p>"));
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }

                else if(imageCunaMotor.length()<=3 )  {
                    AlertDialog.Builder builder = new AlertDialog.Builder(motor.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe Tomar Fotos Obligatorias</b><p><ul><li>- Foto capot abierto</li></ul></p>"));
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else  if( imageChasisVin.length()<=3  )  {
                    AlertDialog.Builder builder = new AlertDialog.Builder(motor.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe Tomar Fotos Obligatorias</b><p><ul><li>- Foto Chavis(VIN)</li></ul></p>"));
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else if(imageMotor.length()<=3 )  {
                    AlertDialog.Builder builder = new AlertDialog.Builder(motor.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe Tomar Fotos Obligatorias</b><p><ul><li>- Foto motor</li></ul></p>"));
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
                    Intent intent = new Intent(motor.this, seccion2.class);
                    intent.putExtra("id_inspeccion", id_inspeccion);
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
            intent.putExtra(MediaStore.EXTRA_OUTPUT,  FileProvider.getUriForFile(motor.this,
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
                    case PHOTO_CUNA:
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
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Cuna Motor", 0, imagen, 0);

                        imagen = "data:image/jpg;base64,"+imagen;
                        String base64Image1 = imagen.split(",")[1];
                        byte[] decodedString1 = Base64.decode(base64Image1, Base64.DEFAULT);
                        Bitmap decodedByte1 = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.length);
                        imageCunaMotorE.setImageBitmap(decodedByte1);

                        Intent servis = new Intent(motor.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Foto Cuna Motor");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);


                        int cantFoto24=db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Cuna Motor");
                        cantFoto24= cantFoto24 +1;
                        db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Foto Cuna Motor",cantFoto24);
                        contPost24.setText(String.valueOf(cantFoto24));

                        break;
                    case PHOTO_MOTOR:
                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });

                        Bitmap bitmapMotor = BitmapFactory.decodeFile(mPath);
                        bitmapMotor = foto.redimensiomarImagen(bitmapMotor);

                        String imagenMotor = foto.convertirImagenDano(bitmapMotor);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Motor", 0, imagenMotor, 0);

                        imagenMotor = "data:image/jpg;base64,"+imagenMotor;
                        String base64Image2 = imagenMotor.split(",")[1];
                        byte[] decodedString2 = Base64.decode(base64Image2, Base64.DEFAULT);
                        Bitmap decodedByte2 = BitmapFactory.decodeByteArray(decodedString2, 0, decodedString2.length);
                        imageMotorE.setImageBitmap(decodedByte2);

                        servis = new Intent(motor.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Foto Motor");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        int cantFoto25=db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Motor");
                        cantFoto25= cantFoto25 +1;
                        db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Foto Motor",cantFoto25);
                        contPost25.setText(String.valueOf(cantFoto25));

                        break;
                    case PHOTO_CHASIS:
                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });

                        Bitmap bitmapChasis = BitmapFactory.decodeFile(mPath);
                        bitmapChasis = foto.redimensiomarImagen(bitmapChasis);

                        String imagenChasis = foto.convertirImagenDano(bitmapChasis);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Chasis(VIN)", 0, imagenChasis, 0);

                        imagenChasis = "data:image/jpg;base64,"+imagenChasis;
                        String base64Image3 = imagenChasis.split(",")[1];
                        byte[] decodedString3 = Base64.decode(base64Image3, Base64.DEFAULT);
                        Bitmap decodedByte3 = BitmapFactory.decodeByteArray(decodedString3, 0, decodedString3.length);
                        imageChasisVinE.setImageBitmap(decodedByte3);

                        servis = new Intent(motor.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Foto Chasis(VIN)");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        int cantFoto26=db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Chasis(VIN)");
                        cantFoto26= cantFoto26 +1;
                        db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Foto Chasis(VIN)",cantFoto26);
                        contPost26.setText(String.valueOf(cantFoto26));

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
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Adicional Motor", 0, imagenAdicional, 0);

                        imagenAdicional = "data:image/jpg;base64,"+imagenAdicional;
                        String base64Image4 = imagenAdicional.split(",")[1];
                        byte[] decodedString4 = Base64.decode(base64Image4, Base64.DEFAULT);
                        Bitmap decodedByte4 = BitmapFactory.decodeByteArray(decodedString4, 0, decodedString4.length);
                        imageAdicionalE.setImageBitmap(decodedByte4);

                        servis = new Intent(motor.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Foto Adicional Motor");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);


                        int cantFoto27=db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Adicional Motor");
                        cantFoto27= cantFoto27 +1;
                        db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Foto Adicional Motor",cantFoto27);
                        contPost27.setText(String.valueOf(cantFoto27));

                        break;
                }
            }
        }catch (Exception e){
            Log.e("Error",e.getMessage());
            Toast.makeText(motor.this,"Por favor intente tomar nuevamente la fotografía",Toast.LENGTH_SHORT).show();
        }
    }

    private void desplegarSeccionUnoMotor(String id)    {

        if (cunaMotorE.getVisibility() == View.VISIBLE) {

            cunaMotorE.setVisibility(View.GONE);
            imageCunaMotorE.setVisibility(View.GONE);
            imageCunaMotorE.setImageBitmap(null);
            motorE.setVisibility(View.GONE);
            imageMotorE.setVisibility(View.GONE);
            imageMotorE.setImageBitmap(null);
            chasisVinE.setVisibility(View.GONE);
            imageChasisVinE.setVisibility(View.GONE);
            imageChasisVinE.setImageBitmap(null);
            textView11.setVisibility(View.GONE);
            textViewCha.setVisibility(View.GONE);
            nChasis.setVisibility(View.GONE);
            placaAdult.setVisibility(View.GONE);
            adicionalE.setVisibility(View.GONE);
            imageAdicionalE.setVisibility(View.GONE);
            imageAdicionalE.setImageBitmap(null);
            textCant24.setVisibility(View.GONE);
            contPost24.setVisibility(View.GONE);
            textCant25.setVisibility(View.GONE);
            contPost25.setVisibility(View.GONE);
            textCant26.setVisibility(View.GONE);
            contPost26.setVisibility(View.GONE);
            textCant27.setVisibility(View.GONE);
            contPost27.setVisibility(View.GONE);

        }
        else
        {

            String imageCunaMotor = db.foto(Integer.parseInt(id),"Foto Cuna Motor");
            String imageMotor = db.foto(Integer.parseInt(id),"Foto Motor");
            String imageChasisVin = db.foto(Integer.parseInt(id),"Foto Chasis(VIN)");
            String imageAdicional = db.foto(Integer.parseInt(id),"Foto Adicional Motor");

            if(imageCunaMotor.length()>3)
            {
                byte[] decodedString = Base64.decode(imageCunaMotor, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageCunaMotorE.setImageBitmap(decodedByte);

            }
            if(imageMotor.length()>3)
            {
                byte[] decodedString = Base64.decode(imageMotor, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageMotorE.setImageBitmap(decodedByte);
            }
            if(imageChasisVin.length()>3)
            {
                byte[] decodedString = Base64.decode(imageChasisVin, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageChasisVinE.setImageBitmap(decodedByte);
            }
            if(imageAdicional.length()>3)
            {
                byte[] decodedString = Base64.decode(imageAdicional, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageAdicionalE.setImageBitmap(decodedByte);
            }


            cunaMotorE.setVisibility(View.VISIBLE);
            imageCunaMotorE.setVisibility(View.VISIBLE);
            motorE.setVisibility(View.VISIBLE);
            imageMotorE.setVisibility(View.VISIBLE);
            chasisVinE.setVisibility(View.VISIBLE);
            imageChasisVinE.setVisibility(View.VISIBLE);
            textView11.setVisibility(View.VISIBLE);
            textViewCha.setVisibility(View.VISIBLE);
            nChasis.setVisibility(View.VISIBLE);
            placaAdult.setVisibility(View.VISIBLE);
            adicionalE.setVisibility(View.VISIBLE);
            imageAdicionalE.setVisibility(View.VISIBLE);
            textCant24.setVisibility(View.VISIBLE);
            contPost24.setVisibility(View.VISIBLE);
            textCant25.setVisibility(View.VISIBLE);
            contPost25.setVisibility(View.VISIBLE);
            textCant26.setVisibility(View.VISIBLE);
            contPost26.setVisibility(View.VISIBLE);
            textCant27.setVisibility(View.VISIBLE);
            contPost27.setVisibility(View.VISIBLE);




        }

    }

    public void guardarDatos() {
        try {

            JSONObject datosvalor12 = new JSONObject();
            datosvalor12.put("valor_id", 17);
            datosvalor12.put("texto", nChasis.getText().toString());
            JSONObject datosvalor17 = new JSONObject();
            datosvalor17.put("valor_id", 790);
            datosvalor17.put("texto", placaAdult.getSelectedItem().toString());

            JSONArray jsonArray = new JSONArray();
            jsonArray.put(datosvalor12);
            jsonArray.put(datosvalor17);

            Log.i("por  aca","por aca" + jsonArray.put(datosvalor12));
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

        } catch (Exception e) {
            Toast.makeText(motor.this, "Error en los datos", Toast.LENGTH_SHORT);
        }
    }
}
