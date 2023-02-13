package com.letchile.let;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.Fallida.Fallida;
import com.letchile.let.Remoto.Data.Resp_CambioRamo;
//import com.letchile.let.Remoto.Data.oiRangoHorario;
import com.letchile.let.Remoto.InterfacePost;
import com.letchile.let.Servicios.ConexionInternet;
import com.letchile.let.VehLiviano.seccion2;
import com.letchile.let.VehPesado.SeccionVpActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class detalleActivity extends AppCompatActivity {

    DBprovider db;
    TextView n_oi,pantete,asegurado,direccion,comentario,fono,ramo,pac,marca,modelo,compañia,corredor;
    Button btnInspeccion, btnAddhito, btnVolver,btnFallida,btnCambiarRamo;
    Boolean conexion1 = false;
    ProgressDialog pDialog;
    long minutosDiferencia;
    long minutosAntes;
    long minutosDespues;
    JSONObject jsonInspe;
    String fecha_cita,hora_cita,comboRamoo;
    Spinner comboRamo;
    boolean rangoHorario = false, notificarFallida = false;


    public detalleActivity(){
        db = new DBprovider(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        conexion1 = new ConexionInternet(this).isConnectingToInternet();

        final String perfil = db.obtenerPerfil();

        final AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);

        //variable id_inspeccion
        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion=bundle.getString("id_inspeccion");
        fecha_cita=bundle.getString("fecha_cita");
        hora_cita=bundle.getString("hora_cita");
        final String fecha_total = fecha_cita+'T'+hora_cita;

        final Calendar c= Calendar.getInstance();
        //DateFormat df = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        //String date = df.format(Calendar.getInstance().getTime());

        new rangoHorario().execute(id_inspeccion,db.obtenerUsuario());
        //new notificarFallida().execute(id_inspeccion,db.obtenerUsuario());


        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm");
        try {

            Date fechaCita = format.parse(fecha_total);
            Calendar Datecita = Calendar.getInstance();
            Datecita.setTime(fechaCita);

            long msDiff = Calendar.getInstance().getTimeInMillis()-Datecita.getTimeInMillis() ;
            minutosDiferencia = TimeUnit.MILLISECONDS.toMinutes(msDiff);

            //Toast.makeText(this,"diferencia: "+minutosDiferencia,Toast.LENGTH_SHORT).show();

        } catch (Exception exception) {
            Log.e("DIDN'T WORK", "exception " + exception);
        }


        String[][] datosInspeccion=db.BuscaDatosInspeccion(id_inspeccion);

        compañia = findViewById(R.id.compañiaMQ);
        compañia.setText(datosInspeccion[0][15].toString());

        corredor = findViewById(R.id.corredorMQ);
        corredor.setText(db.accesorio(Integer.parseInt(id_inspeccion),9).toString());

        n_oi = findViewById(R.id.n_oim);
        n_oi.setText(id_inspeccion);

        pac = findViewById(R.id.pac);
        if(datosInspeccion[0][12].toString().equals("S")) {
            pac.setText("Si");
        }else if(datosInspeccion[0][12].toString().equals("") || datosInspeccion[0][12].isEmpty()){
            pac.setText("No");
        }else{
            pac.setText("");
        }

        asegurado = findViewById(R.id.aseguradoM);
        asegurado.setText(datosInspeccion[0][1]);

        pantete = findViewById(R.id.patenteM);
        pantete.setText(db.accesorio(Integer.parseInt(id_inspeccion),363).toString());

        direccion = findViewById(R.id.direccionM);
        direccion.setText(datosInspeccion[0][4]);

        comentario = findViewById(R.id.comentarioM);
        comentario.setText(datosInspeccion[0][3]);

        fono = findViewById(R.id.telefonoM);
        fono.setText(datosInspeccion[0][2]);
        Button btnLlamar = findViewById(R.id.llamarContacto);
        btnLlamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int REQUEST_PHONE_CALL = 1;
                Intent callIntent2 = new Intent(Intent.ACTION_CALL);
                callIntent2.setData(Uri.parse("tel:"+fono.getText().toString()));

                if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(ContextCompat.checkSelfPermission(detalleActivity.this,Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(detalleActivity.this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                    }else{
                        startActivity(callIntent2);
                    }
                }
            }
        });




        marca = findViewById(R.id.MarcaMQ);
        marca.setText(datosInspeccion[0][13]);

        modelo = findViewById(R.id.modeloMQ);
        modelo.setText(datosInspeccion[0][14]);

        ramo = findViewById(R.id.tipoVehiculoM);
        if(datosInspeccion[0][7].toString().equals("vl1")) {
            ramo.setText("Vehículo liviano");
        }else if(datosInspeccion[0][7].toString().equals("vp1")){
            ramo.setText("Vehículo pesado");
        }else{
            ramo.setText("");
        }

        // cargar ramo
        comboRamo = findViewById(R.id.comboRamo);
        String[] arraytipo = getResources().getStringArray(R.array.tipo_ramo);
        final List<String> arraytipolist = Arrays.asList(arraytipo);
        ArrayAdapter<String> spinner_adapter1 = new ArrayAdapter<String>(detalleActivity.this,android.R.layout.simple_list_item_1,arraytipolist);
        spinner_adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comboRamo.setAdapter(spinner_adapter1);

        //instanciar retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.URL_BASE))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final InterfacePost servicio = retrofit.create(InterfacePost.class);

        //cambiar ramo
        pDialog= new ProgressDialog(detalleActivity.this);
        pDialog.setMessage("Cambiando de ramo...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        btnCambiarRamo = findViewById(R.id.ramo);
        btnCambiarRamo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialog.show();
                String cRamo = comboRamo.getSelectedItem().toString();
                comboRamoo = "";
                if(cRamo.equals("Vehículo liviano"))
                {
                    comboRamoo = "vl1";
                }
                else{
                    comboRamoo = "vp1";
                }

                Call<Resp_CambioRamo> respuestaRamo = servicio.getRamo(id_inspeccion,comboRamoo);

                respuestaRamo.enqueue(new Callback<Resp_CambioRamo>() {
                    @Override
                    public void onResponse(Call<Resp_CambioRamo> call, Response<Resp_CambioRamo> response) {
                        int statusCode = response.code();
                        Resp_CambioRamo rcr = response.body();
                        Log.d("Cambio ramo", "onResponse " + statusCode);

                        if(rcr.getMSJ().equals("Ok")){
                            db.actualizaRamo(Integer.parseInt(id_inspeccion),comboRamoo);
                            if (comboRamoo.equals("vl1")) {
                                ramo.setText("Vehículo liviano");
                                Toast.makeText(detalleActivity.this,"El vehículo es liviano",Toast.LENGTH_SHORT).show();
                            } else if (comboRamoo.toString().equals("vp1")) {
                                ramo.setText("Vehículo pesado");
                                Toast.makeText(detalleActivity.this,"El vehículo es pesado",Toast.LENGTH_SHORT).show();
                            }
                        }
                        pDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<Resp_CambioRamo> call, Throwable t) {
                        Toast.makeText(detalleActivity.this,"Operación fallida",Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                    }
                });
            }
        });

        //Inspeccionar
        btnInspeccion = findViewById(R.id.btnInspeccionarM);
        btnInspeccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(detalleActivity.this);
                builder.setCancelable(false);
                builder.setMessage(Html.fromHtml("¿Seguro que desea realizar la inspeccion <b>N°OI: "+id_inspeccion+"</b>?. <b>Recuerde que una vez comenzada la inspección</b>" +
                        " <b><font color='#FF0000'>NO</font> podrá volver atrás.</b>"));

                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //new rangoHorario().execute(id_inspeccion,db.obtenerUsuario());
                        //Log.d("RAMO: ",ramo.getText().toString());
                        if(ramo.getText().toString().equals("Vehículo liviano")) {
                            Intent inn = new Intent(detalleActivity.this,seccion2.class);
                            inn.putExtra("id_inspeccion",n_oi.getText().toString());
                            startActivity(inn);
                        }else if(ramo.getText().toString().equals("Vehículo pesado")){
                           // Toast.makeText(detalleActivity.this, "Pantalla vehículo pesado", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent( detalleActivity.this, SeccionVpActivity.class);
                            intent.putExtra("id_inspeccion",n_oi.getText().toString());
                            startActivity(intent);
                        }
                    }
                });

                builder.setNegativeButton("Rechazar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(detalleActivity.this, "Inspección rechazada", Toast.LENGTH_LONG).show();
                    }
                });
                android.app.AlertDialog alert = builder.create();
                alert.show();
            }
        });

        //Agregar hito

        //Volver al layout anterior
        btnVolver = findViewById(R.id.btnVolverA);
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent in = new Intent(detalleActivity.this,InsPendientesActivity.class);
               in.putExtra("id_inspeccion",id_inspeccion);
               startActivity(in);
               finish();


            }
        });

        //Botón declarar Fallida  btnFallida
        btnFallida = findViewById(R.id.btnFallida);
        btnFallida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(detalleActivity.this);
                builder.setCancelable(false);
                builder.setMessage(Html.fromHtml("¿Desea declarar fallida la inspeccion <b>N°: "+id_inspeccion+"</b>?."));
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //pregunto perfil, si es 3 validar horario de fallida, 6 sin validaciones


                        String notificaFall = String.valueOf(notificarFallida);

                        if(perfil.equals("3"))
                        {
                           // Toast.makeText(detalleActivity.this, "faliida " + perfil  , Toast.LENGTH_LONG).show();

                            new notificarFallida().execute(n_oi.getText().toString(), db.obtenerUsuario());
                        }
                        else {

                            new notificarFallida().execute(n_oi.getText().toString(), db.obtenerUsuario());

                        }



                        /*if(rangoHorario) {
                            if(minutosAntes <=30 || minutosDespues >=-30){
                                new notificarFallida().execute(n_oi.getText().toString(), db.obtenerUsuario());
                            }
                        }else {
                            if (perfil.equals("3")) {
                                if (minutosDiferencia <= 30 && minutosDiferencia >= -30) {
                                    new notificarFallida().execute(n_oi.getText().toString(), db.obtenerUsuario());
                                } else {
                                    Toast.makeText(detalleActivity.this, "Fuera de rango de horario", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                new notificarFallida().execute(n_oi.getText().toString(), db.obtenerUsuario());
                            }
                        }*/

                    }
                });

                builder.setNegativeButton("Rechazar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(detalleActivity.this, "Inspección no fallida", Toast.LENGTH_LONG).show();
                    }
                });
                android.app.AlertDialog alert = builder.create();
                alert.show();
            }
        });



        //Eliminar oi
        Button btnEliminar = findViewById(R.id.eliminarOIMQ);
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(detalleActivity.this);
                builder.setCancelable(false);
                builder.setMessage(Html.fromHtml("¿Desea borrar la inspeccion <b>N°: "+id_inspeccion+"</b>?."));

                builder.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.deleteInspeccion(Integer.parseInt(id_inspeccion));
                        startActivity(new Intent(detalleActivity.this,InsPendientesActivity.class));
                        finish();
                    }
                });

                builder.setNegativeButton("Dejar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(detalleActivity.this, "Inspección no borrada del sistema", Toast.LENGTH_LONG).show();
                    }
                });
                android.app.AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }

    public class notificarFallida extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){
            //pDialog= new ProgressDialog(detalleActivity.this);
            //pDialog.setIndeterminate(false);
            //pDialog.setCancelable(false);
            //pDialog.show();

        }

        protected String doInBackground(String... param) {


            conexion1 = new ConexionInternet(detalleActivity.this).isConnectingToInternet();
            try {
                if (conexion1) {
                    URL url = new URL("https://www.autoagenda.cl/movil/cargamovil/fotoFallida"); // here is your URL path

                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("id_inspeccion", param[0]);
                    postDataParams.put("usr", param[1]);
                    Log.e("params", postDataParams.toString());

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(15000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(postDataParams));

                    writer.flush();
                    writer.close();
                    os.close();

                    int responseCode = conn.getResponseCode();

                    if (responseCode == HttpsURLConnection.HTTP_OK) {

                        BufferedReader in = new BufferedReader(new
                                InputStreamReader(
                                conn.getInputStream()));

                        StringBuffer sb = new StringBuffer("");
                        String line = "";

                        while ((line = in.readLine()) != null) {

                            sb.append(line);

                            break;
                        }

                         in.close();

                        //se llena la inspeccion fallida
                        String json = sb.toString();
                        JSONArray jsonar = new JSONArray(json);
                        try{
                            if(!jsonar.isNull(0)){
                                for (int i = 0; i < jsonar.length(); i++) {
                                    jsonInspe = new JSONObject(jsonar.getString(i));
                                    //if(jsonInspe.getInt("id_inspeccion")==Integer.parseInt(param[0])) {
                                        db.borrarInspeccionFallida(jsonInspe.getInt("id_inspeccion"));
                                        db.insertaInspeccionesFallida(jsonInspe.getInt("id_inspeccion"), jsonInspe.getString("fechaFallida"), jsonInspe.getString("comentarioFallida"),
                                                jsonInspe.getInt("idFallida"), jsonInspe.getString("fechaCita"), jsonInspe.getString("horaCita"), jsonInspe.getInt("activo"));

                                        //notificarFallida=true;

                                   // }

                                }



                            }

                        }catch (Exception e){
                            Log.e("Error al convertir json", e.getMessage());
                        }
                        //String con el json de respuesta
                        return sb.toString();

                    } else {
                        return new String("false : " + responseCode);
                    }
                } else {
                    return "No hay conexión";
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {



            /*if(db.obtenerPerfil().equals("3")) {

                if (result.equals("[]")) {
                    //Log.i("aaaa","aaaa" + result);
                    Toast.makeText(detalleActivity.this, "Debe contactarse con coordinación", Toast.LENGTH_LONG).show();
                    pDialog = new ProgressDialog(detalleActivity.this);
                    //pDialog.setIndeterminate(false);
                    //pDialog.setCancelable(false);
                    //pDialog.show();
                    pDialog.dismiss();

                } else {
                    //Log.i("bbbb","bbbb" +result);
                    Intent in = new Intent(detalleActivity.this, Fallida.class);
                    in.putExtra("id_inspeccion", n_oi.getText().toString());
                    in.putExtra("fecha_cita", fecha_cita);
                    in.putExtra("hora_cita", fecha_cita);

                    startActivity(in);

                }
            }
            else*/
            //{
                Intent in = new Intent(detalleActivity.this, Fallida.class);
                in.putExtra("id_inspeccion", n_oi.getText().toString());
                in.putExtra("fecha_cita", fecha_cita);
                in.putExtra("hora_cita", fecha_cita);

                startActivity(in);

            //}


          /*  Intent in = new Intent(detalleActivity.this,Fallida.class);
            in.putExtra("id_inspeccion",n_oi.getText().toString());
            in.putExtra("fecha_cita",fecha_cita);
            in.putExtra("hora_cita",fecha_cita);

            startActivity(in);*/
        }
    }

    public class rangoHorario extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            conexion1 = new ConexionInternet(detalleActivity.this).isConnectingToInternet();
            try {
                if (conexion1) {
                    URL url = new URL("https://www.autoagenda.cl/movil/cargamovil/oiRangoHorario"); // here is your URL path

                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("id_inspeccion", strings[0]);
                    postDataParams.put("usr", strings[1]);
                    Log.e("params", postDataParams.toString());

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(15000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(postDataParams));

                    writer.flush();
                    writer.close();
                    os.close();

                    int responseCode = conn.getResponseCode();

                    if (responseCode == HttpsURLConnection.HTTP_OK) {

                        BufferedReader in = new BufferedReader(new
                                InputStreamReader(
                                conn.getInputStream()));

                        StringBuffer sb = new StringBuffer("");
                        String line = "";

                        while ((line = in.readLine()) != null) {

                            sb.append(line);
                            break;
                        }

                        in.close();

                        //se llena la inspeccion fallida
                        String json = sb.toString();
                        JSONArray jsonar = new JSONArray(json);

                        try {
                            if (!jsonar.isNull(0)) {
                                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");

                                for(int i=0;i<jsonar.length();i++){
                                    jsonInspe = new JSONObject(jsonar.getString(i));
                                    if(jsonInspe.getInt("id_inspeccion")==Integer.parseInt(strings[0])){


                                        String fInicio = (jsonInspe.getString("fechaR"));
                                        String HInicio = (jsonInspe.getString("horaIR"));
                                        final String fecha_inicio_total = fInicio+'T'+HInicio;
                                        SimpleDateFormat formatoInicio = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                                        Date fechaInicioTotal = formatoInicio.parse(fecha_inicio_total);

                                        String fTermino = (jsonInspe.getString("fechaR"));
                                        String HTermino = (jsonInspe.getString("horaFR"));
                                        final String fecha_Termino_total = fTermino+'T'+HTermino;
                                        SimpleDateFormat formatoTermino = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                                        Date fechaTerminoTotal = formatoTermino.parse(fecha_Termino_total);

                                        Calendar DateFechaInicio = Calendar.getInstance();
                                        DateFechaInicio.setTime(fechaInicioTotal);

                                        Calendar DateFechaFinal = Calendar.getInstance();
                                        DateFechaFinal.setTime(fechaTerminoTotal);

                                        minutosAntes = Calendar.getInstance().getTimeInMillis()-DateFechaInicio.getTimeInMillis();
                                        minutosDespues = DateFechaFinal.getTimeInMillis()-Calendar.getInstance().getTimeInMillis();

                                        minutosAntes = TimeUnit.MILLISECONDS.toMinutes(minutosAntes);
                                        minutosDespues =  TimeUnit.MILLISECONDS.toMinutes(minutosDespues);

                                        System.out.println("Minutos antes: "+minutosAntes);
                                        System.out.println("Minutos después: "+minutosDespues);

                                        rangoHorario = true;
                                    }
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        return sb.toString();

                    } else {
                        return new String("false : " + responseCode);
                    }
                } else {
                    return "No hay conexión";
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return result.toString();


    }
}
