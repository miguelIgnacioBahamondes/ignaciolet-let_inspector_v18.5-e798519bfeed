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
        import com.letchile.let.VehLiviano.Fotos.documento;
        import com.letchile.let.VehLiviano.seccion2;

        import java.io.File;

        import butterknife.BindView;
        import butterknife.ButterKnife;
        import butterknife.OnClick;

public class MotorVp extends AppCompatActivity {



    private final int TAKE_MOTOR_VP = 100, TAKE_ADIC_MOTOR=200;
    String id_inspeccion,ruta,mPath,nombreimagen,tipoVeh;
    private File ruta_sd;
    int correlativo = 0,cantFotoM=0,cantFotoMo=0;
    private TextView textCantM,contPostM, textCantMo,contPostMo;
    private Button btnMotorVp, btnFotoAdiocionalMotorVp, btnVolverMotor,btnSiguientePvpMQ, btnSeccionUnoMotorVp;
    private ImageView imageMotorVp, imageFotoAdicionalMotorVp;
    DBprovider db;
    PropiedadesFoto foto;
    Intent servis;;

    public MotorVp(){
        db = new DBprovider(this);
        foto = new PropiedadesFoto(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motor_vp);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();

        id_inspeccion = bundle.getString("id_inspeccion");
        tipoVeh = bundle.getString("tipoVeh");

        textCantM = findViewById(R.id.textCantM);
        contPostM = findViewById(R.id.contPostM);
        contPostM.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Motor")));

        textCantMo = findViewById(R.id.textCantMo);
        contPostMo = findViewById(R.id.contPostMo);
        contPostMo.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Adicional Motor")));

        imageMotorVp = findViewById(R.id.imageMotorVp);
        imageFotoAdicionalMotorVp = findViewById(R.id.imageFotoAdicionalMotorVp);
        btnSeccionUnoMotorVp = findViewById(R.id.btnSeccionUnoMotorVp);



        btnMotorVp = findViewById(R.id.btnMotorVp);
        btnMotorVp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                funcionCamara(id_inspeccion,"_Foto_motor_vp.jpg",TAKE_MOTOR_VP);

            }
        });


        btnFotoAdiocionalMotorVp = findViewById(R.id.btnFotoAdiocionalMotorVp);
        btnFotoAdiocionalMotorVp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                funcionCamara(id_inspeccion,"_Foto_adicional_motor_vp.jpg",TAKE_ADIC_MOTOR);

            }
        });

        btnSeccionUnoMotorVp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                desplegarSeccionUnoDocumento(id_inspeccion);
            }
        });



        //botón volver
        btnVolverMotor = findViewById(R.id.btnVolverMotor);
        btnVolverMotor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MotorVp.this,interior_vp.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                intent.putExtra("tipoVeh", tipoVeh);
                startActivity(intent);
                finish();
            }
        });

        //botón siguiente
        btnSiguientePvpMQ = findViewById(R.id.btnSiguientePvpMQ);
        btnSiguientePvpMQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String imageMotor = db.foto(Integer.parseInt(id_inspeccion),"Motor");

                if(imageMotor.length()>=3) {

                    Intent intent = new Intent(MotorVp.this, ParabrisaVp.class);// foto parabrisa
                    intent.putExtra("id_inspeccion",id_inspeccion);
                    intent.putExtra("tipoVeh", tipoVeh);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(MotorVp.this,"Faltan fotos obligatorias por tomar",Toast.LENGTH_SHORT).show();
                }

            }
        });




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
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(MotorVp.this,
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
                case TAKE_MOTOR_VP:

                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitMotor = BitmapFactory.decodeFile(mPath);
                    bitMotor = foto.redimensiomarImagen(bitMotor);
                    imageMotorVp.setImageBitmap(bitMotor);
                    String imagenMotor = foto.convertirImagenDano(bitMotor);
                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, "Motor",0,imagenMotor, 0);


                    servis = new Intent(MotorVp.this, TransferirFoto.class);
                    servis.putExtra("comentario","Motor");
                    servis.putExtra("id_inspeccion",id_inspeccion);
                    startService(servis);


                    int cantFotoM=db.cantidadF(Integer.parseInt(id_inspeccion),"Motor");
                    cantFotoM = cantFotoM +1;
                    db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Motor",cantFotoM);
                    contPostM.setText(String.valueOf(cantFotoM));

                    break;

                case TAKE_ADIC_MOTOR:

                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitAdicMotor = BitmapFactory.decodeFile(mPath);
                    bitAdicMotor = foto.redimensiomarImagen(bitAdicMotor);
                    imageFotoAdicionalMotorVp.setImageBitmap(bitAdicMotor);
                    String imagenAdicMotor = foto.convertirImagenDano(bitAdicMotor);
                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, "Foto Adicional Motor",0,imagenAdicMotor, 0);

                    servis = new Intent(MotorVp.this, TransferirFoto.class);
                    servis.putExtra("comentario","Foto Adicional Motor");
                    servis.putExtra("id_inspeccion",id_inspeccion);
                    startService(servis);

                    int cantFotoMo=db.cantidadF(Integer.parseInt(id_inspeccion),"Adicional Motor");
                    cantFotoMo= cantFotoMo +1;
                    db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Adicional Motor",cantFotoMo);
                    contPostMo.setText(String.valueOf(cantFotoMo));

                    break;

            }
        }
    }

    private void desplegarSeccionUnoDocumento(String id)
    {
        if(btnMotorVp.getVisibility()==View.VISIBLE){
            btnMotorVp.setVisibility(View.GONE);
            imageMotorVp.setVisibility(View.GONE);
            imageMotorVp.setImageBitmap(null);
            btnFotoAdiocionalMotorVp.setVisibility(View.GONE);
            imageFotoAdicionalMotorVp.setVisibility(View.GONE);
            imageFotoAdicionalMotorVp.setImageBitmap(null);
            textCantM.setVisibility(View.GONE);
            textCantMo.setVisibility(View.GONE);
            contPostM.setVisibility(View.GONE);
            contPostMo.setVisibility(View.GONE);




        }else{
            btnMotorVp.setVisibility(View.VISIBLE);
            imageMotorVp.setVisibility(View.VISIBLE);
            imageMotorVp.setImageBitmap(null);
            btnFotoAdiocionalMotorVp.setVisibility(View.VISIBLE);
            imageFotoAdicionalMotorVp.setVisibility(View.VISIBLE);
            imageFotoAdicionalMotorVp.setImageBitmap(null);
            textCantM.setVisibility(View.VISIBLE);
            textCantMo.setVisibility(View.VISIBLE);
            contPostM.setVisibility(View.VISIBLE);
            contPostMo.setVisibility(View.VISIBLE);


            String imagMotor = db.foto(Integer.parseInt(id_inspeccion),"Motor");
            String imagAdicMotor = db.foto(Integer.parseInt(id_inspeccion),"Foto Adicional Motor");




            if(imagMotor.length()>=3){
                byte[] decodeString = Base64.decode(imagMotor,Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodeString,0,decodeString.length);
                imageMotorVp.setImageBitmap(decodedByte);
            }
            if(imagAdicMotor.length()>=3){
                byte[] decodeString = Base64.decode(imagAdicMotor,Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodeString,0,decodeString.length);
                imageFotoAdicionalMotorVp.setImageBitmap(decodedByte);
            }



        }
    }
}
