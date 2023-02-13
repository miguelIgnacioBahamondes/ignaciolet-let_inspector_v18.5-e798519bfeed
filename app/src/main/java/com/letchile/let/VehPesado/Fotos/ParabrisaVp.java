package com.letchile.let.VehPesado.Fotos;

        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.media.MediaScannerConnection;
        import android.net.ConnectivityManager;
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
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.letchile.let.BD.DBprovider;
        import com.letchile.let.BuildConfig;
        import com.letchile.let.Clases.PropiedadesFoto;
        import com.letchile.let.R;
        import com.letchile.let.Servicios.TransferirFoto;
        import com.letchile.let.VehLiviano.seccion2;

        import java.io.File;

        import butterknife.ButterKnife;

public class ParabrisaVp extends AppCompatActivity {

    private final int TAKE_PARABRISA_VP = 100, TAKE_ADIC_PARABRISA=200;
    String id_inspeccion,ruta,mPath,nombreimagen,tipoVeh;
    private File ruta_sd;
    int correlativo = 0,cantFotoP=0,cantFotoPa=0;
    private TextView textCantP,contPostP, textCantPa,contPostPa;
    private Button btnParaVp, btnFotoAdiocionalParaVp, btnVolverParabrisa,btnSiguientePara, btnSeccionUnoParaVp;
    private ImageView imageParaVp, imageFotoAdicionalParaVp;
    DBprovider db;
    PropiedadesFoto foto;
    Intent servis;;

    public ParabrisaVp(){
        db = new DBprovider(this);
        foto = new PropiedadesFoto(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parabrisa_vp);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();

        id_inspeccion = bundle.getString("id_inspeccion");
        tipoVeh = bundle.getString("tipoVeh");

        textCantP = findViewById(R.id.textCantP);
        contPostP = findViewById(R.id.contPostP);
        contPostP.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Parabrisa")));

        textCantPa = findViewById(R.id.textCantPa);
        contPostPa = findViewById(R.id.contPostPa);
        contPostPa.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Adicional Parabrisa")));

        imageParaVp = findViewById(R.id.imageParaVp);
        imageFotoAdicionalParaVp = findViewById(R.id.imageFotoAdicionalParaVp);
        btnSeccionUnoParaVp = findViewById(R.id.btnSeccionUnoParaVp);



        btnParaVp = findViewById(R.id.btnParaVp);
        btnParaVp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                funcionCamara(id_inspeccion,"_Foto_parabrisa_vp.jpg",TAKE_PARABRISA_VP);

            }
        });


        btnFotoAdiocionalParaVp = findViewById(R.id.btnFotoAdiocionalParaVp);
        btnFotoAdiocionalParaVp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                funcionCamara(id_inspeccion,"_Foto_adicional_parabrisa_vp.jpg",TAKE_ADIC_PARABRISA);

            }
        });

        btnSeccionUnoParaVp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                desplegarSeccionUnoDocumento(id_inspeccion);
            }
        });


        //botón volver
        btnVolverParabrisa = findViewById(R.id.btnVolverParabrisa);
        btnVolverParabrisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ParabrisaVp.this,MotorVp.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                intent.putExtra("tipoVeh", tipoVeh);
                startActivity(intent);
                finish();
            }
        });

        //botón siguiente
        btnSiguientePara = findViewById(R.id.btnSiguientePara);
        btnSiguientePara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String imagePara = db.foto(Integer.parseInt(id_inspeccion),"Parabrisa");

                if(imagePara.length()>=3) {

                    Intent intent = new Intent(ParabrisaVp.this, seccion2.class);// foto parabrisa
                    intent.putExtra("id_inspeccion",id_inspeccion);
                    intent.putExtra("tipoVeh", tipoVeh);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(ParabrisaVp.this,"Faltan fotos obligatorias por tomar",Toast.LENGTH_SHORT).show();
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
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(ParabrisaVp.this,
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
                case TAKE_PARABRISA_VP:

                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitPara = BitmapFactory.decodeFile(mPath);
                    bitPara = foto.redimensiomarImagen(bitPara);
                    imageParaVp.setImageBitmap(bitPara);
                    String imagenPara = foto.convertirImagenDano(bitPara);
                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, "Parabrisa",0,imagenPara, 0);


                    servis = new Intent(ParabrisaVp.this, TransferirFoto.class);
                    servis.putExtra("comentario","Parabrisa");
                    servis.putExtra("id_inspeccion",id_inspeccion);
                    startService(servis);


                    int cantFotoP=db.cantidadF(Integer.parseInt(id_inspeccion),"Parabrisa");
                    cantFotoP = cantFotoP +1;
                    db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Parabrisa",cantFotoP);
                    contPostP.setText(String.valueOf(cantFotoP));

                    break;

                case TAKE_ADIC_PARABRISA:

                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitAdicPara = BitmapFactory.decodeFile(mPath);
                    bitAdicPara = foto.redimensiomarImagen(bitAdicPara);
                    imageFotoAdicionalParaVp.setImageBitmap(bitAdicPara);
                    String imagenAdicPara = foto.convertirImagenDano(bitAdicPara);
                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, "Foto Adicional Parabrisa",0,imagenAdicPara, 0);

                    servis = new Intent(ParabrisaVp.this, TransferirFoto.class);
                    servis.putExtra("comentario","Foto Adicional Parabrisa");
                    servis.putExtra("id_inspeccion",id_inspeccion);
                    startService(servis);

                    int cantFotoPa=db.cantidadF(Integer.parseInt(id_inspeccion),"Adicional Parabrisa");
                    cantFotoPa= cantFotoPa +1;
                    db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Adicional Parabrisa",cantFotoPa);
                    contPostPa.setText(String.valueOf(cantFotoPa));

                    break;

            }
        }
    }

    private void desplegarSeccionUnoDocumento(String id)
    {
        if(btnParaVp.getVisibility()==View.VISIBLE){
            btnParaVp.setVisibility(View.GONE);
            imageParaVp.setVisibility(View.GONE);
            imageParaVp.setImageBitmap(null);
            btnFotoAdiocionalParaVp.setVisibility(View.GONE);
            imageFotoAdicionalParaVp.setVisibility(View.GONE);
            imageFotoAdicionalParaVp.setImageBitmap(null);
            textCantP.setVisibility(View.GONE);
            textCantPa.setVisibility(View.GONE);
            contPostP.setVisibility(View.GONE);
            contPostPa.setVisibility(View.GONE);




        }else{
            btnParaVp.setVisibility(View.VISIBLE);
            imageParaVp.setVisibility(View.VISIBLE);
            imageParaVp.setImageBitmap(null);
            btnFotoAdiocionalParaVp.setVisibility(View.VISIBLE);
            imageFotoAdicionalParaVp.setVisibility(View.VISIBLE);
            imageFotoAdicionalParaVp.setImageBitmap(null);
            textCantP.setVisibility(View.VISIBLE);
            textCantPa.setVisibility(View.VISIBLE);
            contPostP.setVisibility(View.VISIBLE);
            contPostPa.setVisibility(View.VISIBLE);


            String imagPara = db.foto(Integer.parseInt(id_inspeccion),"Parabrisa");
            String imagAdicPara = db.foto(Integer.parseInt(id_inspeccion),"Foto Adicional Parabrisa");




            if(imagPara.length()>=3){
                byte[] decodeString = Base64.decode(imagPara,Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodeString,0,decodeString.length);
                imageParaVp.setImageBitmap(decodedByte);
            }
            if(imagAdicPara.length()>=3){
                byte[] decodeString = Base64.decode(imagAdicPara,Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodeString,0,decodeString.length);
                imageFotoAdicionalParaVp.setImageBitmap(decodedByte);
            }



        }
    }
}
