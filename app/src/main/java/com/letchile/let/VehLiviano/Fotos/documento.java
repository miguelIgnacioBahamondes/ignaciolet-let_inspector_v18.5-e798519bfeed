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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.BuildConfig;
import com.letchile.let.Clases.PropiedadesFoto;
import com.letchile.let.R;
import com.letchile.let.Servicios.ConexionInternet;
import com.letchile.let.Servicios.TransferirFoto;
import com.letchile.let.VehLiviano.DatosAsegActivity;
import com.letchile.let.VehLiviano.SeccionActivity;
import com.letchile.let.VehLiviano.seccion2;
import com.letchile.let.VehPesado.Fotos.documentos_vp;
import com.letchile.let.detalleActivity;

import java.io.File;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class documento extends AppCompatActivity {

    DBprovider db;
    Boolean connec = false;
    PropiedadesFoto foto;
    private final int PHOTO_DOCUMENTO = 200;
    private final int PHOTO_DOCUMENTO_REVERSO = 300;
    private final int PHOTO_PAC = 400;
    private final int PHOTO_CARNET_AN = 500;
    private final int PHOTO_CARNET_RE = 600;
    //private final int PHOTO_CONVERTIBLE = 700;
    private final int PHOTO_ADICIONAL = 700;

    private Button btnVolverDocuE,btnVolverSecDocuE,btnDocumentoE,btnPACE,btnCarnetAnversoE,btnCarnetReversoE,btnConvertibleE,btnAdicionalDocumentoE,btnSeccionUnoDocumento,btnDocumentoR;
    private ImageView imageDocumentoE,imagePACE,imageCarnetAnversoE,imageCarnetReversoE,imageConvertibleE,imageAdicionalDocumentoE, imageDocumentoRr;
    private String mPath;
    private File ruta_sd;
    private String ruta = "", usuario;
    String nombreimagen = "";
    int correlativo=0;
    private TextView textCant4,contPost4, textCant5,contPost5, textCant6,contPost6, textCant7,contPost7, textCant8,contPost8, textCant9,contPost9, textCantR,contPostR;


    public documento(){db = new DBprovider(this);foto=new PropiedadesFoto(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documento);

        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion = bundle.getString("id_inspeccion");

        connec = new ConexionInternet(this).isConnectingToInternet();

        btnDocumentoE = findViewById(R.id.btnDocumentoE);
        imageDocumentoE = findViewById(R.id.imageDocumentoE);
        btnPACE = findViewById(R.id.btnPACE);
        imagePACE = findViewById(R.id.imagePACE);
        btnCarnetAnversoE = findViewById(R.id.btnCarnetAnversoE);
        imageCarnetAnversoE = findViewById(R.id.imageCarnetAnversoE);
        btnCarnetReversoE = findViewById(R.id.btnCarnetReversoE);
        imageCarnetReversoE = findViewById(R.id.imageCarnetReversoE);
       // btnConvertibleE = findViewById(R.id.btnConvertibleE);
       // imageConvertibleE = findViewById(R.id.imageConvertibleE);
        btnAdicionalDocumentoE = findViewById(R.id.btnAdicionalDocumentoE);
        imageAdicionalDocumentoE = findViewById(R.id.imageAdicionalDocumentoE);
        btnSeccionUnoDocumento = findViewById(R.id.btnSeccionUnoDocumento);
        btnDocumentoR = findViewById(R.id.btnDocumentoR);
        imageDocumentoRr = findViewById(R.id.imageDocumentoRr);
        usuario=db.obtenerUsuario();

        textCant4 = findViewById(R.id.textCant4);
        contPost4 = findViewById(R.id.contPost4);
        contPost4.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Documento Anverso")));

        textCantR = findViewById(R.id.textCantR);
        contPostR = findViewById(R.id.contPostR);
        contPostR.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Documento Reverso")));

        textCant5 = findViewById(R.id.textCant5);
        contPost5 = findViewById(R.id.contPost5);
        contPost5.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto_PAC")));

        textCant6 = findViewById(R.id.textCant6);
        contPost6 = findViewById(R.id.contPost6);
        contPost6.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Carnet Anverso")));

        textCant7 = findViewById(R.id.textCant7);
        contPost7 = findViewById(R.id.contPost7);
        contPost7.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Carnet Reversa")));

        /*textCant8 = findViewById(R.id.textCant8);
        contPost8 = findViewById(R.id.contPost8);
        contPost8.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Convertible")));*/

        textCant9 = findViewById(R.id.textCant9);
        contPost9 = findViewById(R.id.contPost9);
        contPost9.setText(String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Adicional Documento")));

        //Log.i("aaa","aaa"+ String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Documento")));


        btnDocumentoE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //SOLO EN LA PRIMERA FOTO DE POSTERIOR
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String fechaInspeccion = sdf.format(new Date());
                db.insertarValor(Integer.parseInt(id_inspeccion),360, fechaInspeccion);

                SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
                String horaInspeccion = sdf2.format(new Date());
                db.insertarValor(Integer.parseInt(id_inspeccion),361,horaInspeccion);

                Log.i("vv","vv" + fechaInspeccion);
                Log.i("vv","vv" + horaInspeccion);

                funcionCamara(id_inspeccion,"_Foto_Documento_Anverso.jpg",PHOTO_DOCUMENTO);
                //Toast.makeText(documento.this, "por acaaaa" , Toast.LENGTH_LONG).show();
            }
        });

        btnDocumentoR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log.i("ddd","ddd")
                funcionCamara(id_inspeccion,"_Foto_Documento_Reverso.jpg",PHOTO_DOCUMENTO_REVERSO);
                //Toast.makeText(documento.this, "por acaaaa" , Toast.LENGTH_LONG).show();
            }
        });
        btnPACE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcionCamara(id_inspeccion,"_Foto_PAC.jpg",PHOTO_PAC);
            }
        });
        btnCarnetAnversoE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                funcionCamara(id_inspeccion,"_Foto_Carnet_Anverso.jpg",PHOTO_CARNET_AN);
            }
        });
        btnCarnetReversoE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                funcionCamara(id_inspeccion,"_Foto_Carnet_Reverso.jpg",PHOTO_CARNET_RE);
            }
        });
       /* btnConvertibleE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                funcionCamara(id_inspeccion,"_Foto_Convertible.jpg",PHOTO_CONVERTIBLE);

            }
        });*/
        btnAdicionalDocumentoE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                funcionCamara(id_inspeccion,"_Foto_Adicional_Documento.jpg",PHOTO_ADICIONAL);

            }
        });
        btnSeccionUnoDocumento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desplegarSeccionUnoDocumento(id_inspeccion);
            }
        });

        btnVolverDocuE = findViewById(R.id.btnVolverDocuE);
        btnVolverDocuE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(documento.this,seccion2.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
                finish();
            }
        });

        btnVolverSecDocuE = findViewById(R.id.btnVolverSecDocuE);
        btnVolverSecDocuE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(documento.this, "por acaaaa" , Toast.LENGTH_LONG).show();
                String imageDocumento = db.foto(Integer.parseInt(id_inspeccion),"Foto Documento Anverso");
                String imageDocumentoR = db.foto(Integer.parseInt(id_inspeccion),"Foto Documento Reverso");
                String imagePAC = db.foto(Integer.parseInt(id_inspeccion),"Foto PAC");
                String imageCarnetAnverso = db.foto(Integer.parseInt(id_inspeccion),"Foto Carnet Anverso");
                String imageCarnetReverso = db.foto(Integer.parseInt(id_inspeccion),"Foto Carnet Reversa");
                //String imageConvertible = db.foto(Integer.parseInt(id_inspeccion),"Foto Convertible");

                try {
                    if (db.consultaPAC(Integer.parseInt(id_inspeccion)).toString().equals("S")) {

                        if(imageDocumento.length()<=3 || imagePAC.length()<=3 || imageCarnetAnverso.length()<=3 || imageCarnetReverso.length()<=3 )
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(documento.this);
                            builder.setCancelable(false);
                            builder.setTitle("LET Chile");
                            builder.setMessage(Html.fromHtml("<b>Debe Tomar Fotos Obligatorias</b><p><ul><li>- Foto Documento</li><p><li>- Foto PAC</li></p><p><li>- Foto Carnet Anverso</li></p>" +
                                    "<p><li>- Foto Reverso</li></p></ul></p>"));
                            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                        else{

                            Intent intent = new Intent(documento.this, Posterior.class);
                            intent.putExtra("id_inspeccion",id_inspeccion);
                            startActivity(intent);
                        }
                    }
                    else
                    {
                        if(imageDocumento.length()<=3 || imageDocumentoR.length()<=3)
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(documento.this);
                            builder.setCancelable(false);
                            builder.setTitle("LET Chile");
                            builder.setMessage(Html.fromHtml("<b>Debe Tomar Fotos Obligatorias</b><p><ul><li>- Foto Documento Anverso</li><p><li>- Foto Documento Anverso</li></ul></p>"));
                            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                        else{

                            Intent intent = new Intent(documento.this,Posterior.class);
                            intent.putExtra("id_inspeccion",id_inspeccion);
                            startActivity(intent);
                            finish();
                        }

                    }
                }catch(Exception e){
                    Log.e("Error pac",e.getMessage()+'-'+db.consultaPAC(Integer.parseInt(id_inspeccion)));
                }



            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion=bundle.getString("id_inspeccion");


        //Log.i("por  aca","por acca");
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(c.getTime());

        try {

            //CAMBIAR EL ESTADO DE LA INSPECCIÓN A INICIADA PARA PODER VALIDAR DESPUÉS
             db.cambiarEstadoInspeccion(Integer.parseInt(id_inspeccion), 1);
            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    case PHOTO_DOCUMENTO:
                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });


                        Log.i("foto doc","foto doc");
                        Bitmap bitmap = BitmapFactory.decodeFile(mPath);
                        bitmap = foto.redimensiomarImagen(bitmap);

                        String imagen = foto.convertirImagenDano(bitmap);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Documento Anverso", 0, imagen, 0);


                        imagen = "data:image/jpg;base64,"+imagen;
                        String base64Image1 = imagen.split(",")[1];
                        byte[] decodedString1 = Base64.decode(base64Image1, Base64.DEFAULT);
                        Bitmap decodedByte1 = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.length);
                        imageDocumentoE.setImageBitmap(decodedByte1);


                        Intent servis1 = new Intent(documento.this, TransferirFoto.class);
                        servis1.putExtra("comentario","Foto Documento Anverso");
                        servis1.putExtra("id_inspeccion",id_inspeccion);
                        startService(servis1);


                        int cantFoto4=db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Documento Anverso");
                        cantFoto4=cantFoto4 +1;
                        db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Foto Documento Anverso", cantFoto4);
                        contPost4.setText(String.valueOf(cantFoto4));


                      // Log.i("sss","sss"+db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Foto Documento",cantFoto4));
                       //Log.i("ddd","addaa"+ String.valueOf(db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Documento")));

                        // registra en log toma de foto
                        try {

                           File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "LogInspector_"+usuario+".txt");
                            //File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "LogInspector.txt");
                            if (!file.exists()) {
                                file.mkdir();
                            }

                            OutputStreamWriter fout = null;

                            fout = new FileWriter(file.getAbsoluteFile(), true);
                            fout.write(id_inspeccion+"| Toma Foto Documento |"+ strDate);
                            fout.append("\n");
                            fout.close();

                        } catch (Exception ex) {
                            Log.e("Error doc 1", "ex: " + ex);

                        }

                        break;
                    case PHOTO_DOCUMENTO_REVERSO:
                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });

                        Bitmap bitmapDocR = BitmapFactory.decodeFile(mPath);
                        bitmapDocR = foto.redimensiomarImagen(bitmapDocR);

                        String imagenDocR = foto.convertirImagenDano(bitmapDocR);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Documento Reverso", 0, imagenDocR, 0);

                        imagenDocR = "data:image/jpg;base64,"+imagenDocR;
                        String base64ImageR = imagenDocR.split(",")[1];
                        byte[] decodedStringR = Base64.decode(base64ImageR, Base64.DEFAULT);
                        Bitmap decodedByteR = BitmapFactory.decodeByteArray(decodedStringR, 0, decodedStringR.length);
                        imageDocumentoRr.setImageBitmap(decodedByteR);


                        Intent servisR = new Intent(documento.this, TransferirFoto.class);
                        servisR.putExtra("comentario","Foto Documento Reverso");
                        servisR.putExtra("id_inspeccion",id_inspeccion);
                        startService(servisR);

                        int cantFotoDocR=db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Documento Reverso");
                        cantFotoDocR= cantFotoDocR +1;
                        db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Foto Documento Reverso",cantFotoDocR);
                        contPostR.setText(String.valueOf(cantFotoDocR));

                        break;
                    case PHOTO_PAC:
                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });

                        Bitmap bitmapPAC = BitmapFactory.decodeFile(mPath);
                        bitmapPAC = foto.redimensiomarImagen(bitmapPAC);

                        String imagenPAC = foto.convertirImagenDano(bitmapPAC);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto PAC", 0, imagenPAC, 0);

                        imagenPAC = "data:image/jpg;base64,"+imagenPAC;
                        String base64Image2 = imagenPAC.split(",")[1];
                        byte[] decodedString2 = Base64.decode(base64Image2, Base64.DEFAULT);
                        Bitmap decodedByte2 = BitmapFactory.decodeByteArray(decodedString2, 0, decodedString2.length);
                        imagePACE.setImageBitmap(decodedByte2);


                            Intent servis2 = new Intent(documento.this, TransferirFoto.class);
                            servis2.putExtra("comentario","Foto PAC");
                            servis2.putExtra("id_inspeccion",id_inspeccion);
                            startService(servis2);

                        int cantFoto5=db.cantidadF(Integer.parseInt(id_inspeccion),"Foto PAC");
                        cantFoto5= cantFoto5 +1;
                        db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Foto PAC",cantFoto5);
                        contPost5.setText(String.valueOf(cantFoto5));

                        break;
                    case PHOTO_CARNET_AN:
                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });

                        Bitmap bitmapCarnetAn = BitmapFactory.decodeFile(mPath);
                        bitmapCarnetAn = foto.redimensiomarImagen(bitmapCarnetAn);

                        String imagenCarnetAn = foto.convertirImagenDano(bitmapCarnetAn);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Carnet Anverso", 0, imagenCarnetAn, 0);

                        imagenCarnetAn = "data:image/jpg;base64,"+imagenCarnetAn;
                        String base64Image3 = imagenCarnetAn.split(",")[1];
                        byte[] decodedString3 = Base64.decode(base64Image3, Base64.DEFAULT);
                        Bitmap decodedByte3 = BitmapFactory.decodeByteArray(decodedString3, 0, decodedString3.length);
                        imageCarnetAnversoE.setImageBitmap(decodedByte3);

                            Intent servis3 = new Intent(documento.this, TransferirFoto.class);
                            servis3.putExtra("comentario","Foto Carnet Anverso");
                            servis3.putExtra("id_inspeccion",id_inspeccion);
                            startService(servis3);

                        int cantFoto6=db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Carnet Anverso");
                        cantFoto6= cantFoto6 +1;
                        db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Foto Carnet Anverso",cantFoto6);
                        contPost6.setText(String.valueOf(cantFoto6));

                        break;
                    case PHOTO_CARNET_RE:
                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });

                        Bitmap bitmapCarnetRe = BitmapFactory.decodeFile(mPath);
                        bitmapCarnetRe = foto.redimensiomarImagen(bitmapCarnetRe);

                        String imagenCarnetRe = foto.convertirImagenDano(bitmapCarnetRe);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Carnet Reversa", 0, imagenCarnetRe, 0);

                        imagenCarnetRe = "data:image/jpg;base64,"+imagenCarnetRe;
                        String base64Image4 = imagenCarnetRe.split(",")[1];
                        byte[] decodedString4 = Base64.decode(base64Image4, Base64.DEFAULT);
                        Bitmap decodedByte4 = BitmapFactory.decodeByteArray(decodedString4, 0, decodedString4.length);
                        imageCarnetReversoE.setImageBitmap(decodedByte4);

                            Intent servis4 = new Intent(documento.this, TransferirFoto.class);
                            servis4.putExtra("comentario","Foto Carnet Reversa");
                            servis4.putExtra("id_inspeccion",id_inspeccion);
                            startService(servis4);

                            int cantFoto7=db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Carnet Reverso");
                            cantFoto7= cantFoto7 +1;
                            db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Foto Carnet Reverso",cantFoto7);
                            contPost7.setText(String.valueOf(cantFoto7));

                        break;
                    /*case  PHOTO_CONVERTIBLE:
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

                            Intent servis5 = new Intent(documento.this, TransferirFoto.class);
                            servis5.putExtra("comentario","Foto Convertible");
                            servis5.putExtra("id_inspeccion",id_inspeccion);
                            startService(servis5);

                        int cantFoto8=db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Convertible");
                        cantFoto8= cantFoto8 +1;
                        db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Foto Convertible",cantFoto8);
                        contPost8.setText(String.valueOf(cantFoto8));

                        break;*/
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
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Adicional Documento", 0, imagenAdicional, 0);


                        imagenAdicional = "data:image/jpg;base64,"+imagenAdicional;
                        String base64Image6 = imagenAdicional.split(",")[1];
                        byte[] decodedString6 = Base64.decode(base64Image6, Base64.DEFAULT);
                        Bitmap decodedByte6 = BitmapFactory.decodeByteArray(decodedString6, 0, decodedString6.length);
                        imageAdicionalDocumentoE.setImageBitmap(decodedByte6);


                            Intent servis6 = new Intent(documento.this, TransferirFoto.class);
                            servis6.putExtra("comentario","Foto Adicional Documento");
                            servis6.putExtra("id_inspeccion",id_inspeccion);
                            startService(servis6);

                        int cantFoto9=db.cantidadF(Integer.parseInt(id_inspeccion),"Foto Adicional Documento");
                        cantFoto9= cantFoto9 +1;
                        db.insertCantidadFoto(Integer.parseInt(id_inspeccion),"Foto Adicional Documento",cantFoto9);
                        contPost9.setText(String.valueOf(cantFoto9));

                        break;

                }
            }
        }catch (Exception e){
            Log.e("Error doc",e.getMessage());
            Toast.makeText(documento.this,"Porfavor vuelva a intentar tomar la foto",Toast.LENGTH_SHORT).show();
        }
    }

    public void funcionCamara(String id,String nombre_foto,int CodigoFoto) {
        String id_inspeccion = id;
        ruta_sd = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString() + '/' + id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();


        Log.i("ruta","ruta" + ruta_sd);
        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated) {

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion) + "_" + String.valueOf(correlativo) + nombre_foto;

            ruta = file.toString() + "/" + nombreimagen;
            mPath = ruta;

            File newFile = new File(mPath);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(documento.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            startActivityForResult(intent, CodigoFoto);
        }
    }

    private void desplegarSeccionUnoDocumento(String id)
    {
        if (btnPACE.getVisibility() == View.VISIBLE) {

           //Toast.makeText(documento.this, "por aca" , Toast.LENGTH_LONG).show();
            btnDocumentoE.setVisibility(View.GONE);
            imageDocumentoE.setVisibility(View.GONE);
            imageDocumentoE.setImageBitmap(null);
            btnPACE.setVisibility(View.GONE);
            imagePACE.setVisibility(View.GONE);
            imagePACE.setImageBitmap(null);
            btnCarnetAnversoE.setVisibility(View.GONE);
            imageCarnetAnversoE.setVisibility(View.GONE);
            imageCarnetAnversoE.setImageBitmap(null);
            btnCarnetReversoE.setVisibility(View.GONE);
            imageCarnetReversoE.setVisibility(View.GONE);
            imageCarnetReversoE.setImageBitmap(null);
            //btnConvertibleE.setVisibility(View.GONE);
            //imageConvertibleE.setVisibility(View.GONE);
            //imageConvertibleE.setImageBitmap(null);
            btnAdicionalDocumentoE.setVisibility(View.GONE);
            imageAdicionalDocumentoE.setVisibility(View.GONE);
            imageAdicionalDocumentoE.setImageBitmap(null);
            textCant4.setVisibility(View.GONE);
            contPost4.setVisibility(View.GONE);
            textCant5.setVisibility(View.GONE);
            contPost5.setVisibility(View.GONE);
            textCant6.setVisibility(View.GONE);
            contPost6.setVisibility(View.GONE);
            textCant7.setVisibility(View.GONE);
            contPost7.setVisibility(View.GONE);
            textCant8.setVisibility(View.GONE);
            contPost8.setVisibility(View.GONE);
            textCant9.setVisibility(View.GONE);
            contPost9.setVisibility(View.GONE);
            btnDocumentoR.setVisibility(View.GONE);
            imageDocumentoRr.setVisibility(View.GONE);
            textCantR.setVisibility(View.GONE);
            contPostR.setVisibility(View.GONE);

        }
        else
        {

            String imageDocumento = db.foto(Integer.parseInt(id),"Foto Documento Anverso");
            String imageDocumentoR = db.foto(Integer.parseInt(id),"Foto Documento Reverso");
            String imagePAC = db.foto(Integer.parseInt(id),"Foto PAC");
            String imageCarnetAnverso = db.foto(Integer.parseInt(id),"Foto Carnet Anverso");
            String imageCarnetReverso = db.foto(Integer.parseInt(id),"Foto Carnet Reversa");
            //String imageConvertible = db.foto(Integer.parseInt(id),"Foto Convertible");
            String imageAdicionalDocumento = db.foto(Integer.parseInt(id),"Foto Adicional Documento");

            if(imageDocumento.length()>3)
            {
                byte[] decodedString = Base64.decode(imageDocumento, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageDocumentoE.setImageBitmap(decodedByte);
            }

            if(imageDocumentoR.length()>3)
            {
                byte[] decodedString = Base64.decode(imageDocumentoR, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageDocumentoRr.setImageBitmap(decodedByte);
            }

            if(imagePAC.length()>3)
            {
                byte[] decodedString = Base64.decode(imagePAC, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imagePACE.setImageBitmap(decodedByte);
            }
            if(imageCarnetAnverso.length()>3)
            {
                byte[] decodedString = Base64.decode(imageCarnetAnverso, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageCarnetAnversoE.setImageBitmap(decodedByte);
            }
            if(imageCarnetReverso.length()>3)
            {
                byte[] decodedString = Base64.decode(imageCarnetReverso, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageCarnetReversoE.setImageBitmap(decodedByte);
            }
           /* if(imageConvertible.length()>3)
            {
                byte[] decodedString = Base64.decode(imageConvertible, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageConvertibleE.setImageBitmap(decodedByte);
            }*/
            if(imageAdicionalDocumento.length()>3)
            {
                byte[] decodedString = Base64.decode(imageAdicionalDocumento, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageAdicionalDocumentoE.setImageBitmap(decodedByte);
            }


            btnDocumentoE.setVisibility(View.VISIBLE);
            imageDocumentoE.setVisibility(View.VISIBLE);
            textCant4.setVisibility(View.VISIBLE);
            contPost4.setVisibility(View.VISIBLE);
            btnDocumentoR.setVisibility(View.VISIBLE);
            imageDocumentoRr.setVisibility(View.VISIBLE);
            textCantR.setVisibility(View.VISIBLE);
            contPostR.setVisibility(View.VISIBLE);

            try {
                if (db.consultaPAC(Integer.parseInt(id)).toString().equals("S")) {
                    btnPACE.setVisibility(View.VISIBLE);
                    imagePACE.setVisibility(View.VISIBLE);
                    btnCarnetAnversoE.setVisibility(View.VISIBLE);
                    imageCarnetAnversoE.setVisibility(View.VISIBLE);
                    btnCarnetReversoE.setVisibility(View.VISIBLE);
                    imageCarnetReversoE.setVisibility(View.VISIBLE);
                    textCant5.setVisibility(View.VISIBLE);
                    contPost5.setVisibility(View.VISIBLE);
                    textCant6.setVisibility(View.VISIBLE);
                    contPost6.setVisibility(View.VISIBLE);
                    textCant7.setVisibility(View.VISIBLE);
                    contPost7.setVisibility(View.VISIBLE);
                }
            }catch(Exception e){
                Log.e("Error pac",e.getMessage()+'-'+db.consultaPAC(Integer.parseInt(id)));
            }

            //btnConvertibleE.setVisibility(View.VISIBLE);
            //imageConvertibleE.setVisibility(View.VISIBLE);
            btnAdicionalDocumentoE.setVisibility(View.VISIBLE);
            imageAdicionalDocumentoE.setVisibility(View.VISIBLE);
            //textCant8.setVisibility(View.VISIBLE);
            //contPost8.setVisibility(View.VISIBLE);
            textCant9.setVisibility(View.VISIBLE);
            contPost9.setVisibility(View.VISIBLE);
            textCantR.setVisibility(View.VISIBLE);
            contPostR.setVisibility(View.VISIBLE);





        }

    }

}
