package com.letchile.let;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;



import com.letchile.let.BD.DBprovider;
import com.letchile.let.Clases.Inspeccion;
import com.letchile.let.Clases.Validaciones;
import com.letchile.let.Remoto.Data.LoginEnv;
import com.letchile.let.Remoto.Data.LoginResp;
import com.letchile.let.Remoto.Data.Resp_versionapp;
import com.letchile.let.Remoto.Data.resp_bd;
import com.letchile.let.Remoto.InterfacePost;
import com.letchile.let.Servicios.ConexionInternet;
import com.letchile.let.Servicios.TransferirInspeccion;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mauro on 08/01/2018.
 */

public class InsPendientesActivity extends AppCompatActivity{

    DBprovider db;
    SwipeRefreshLayout refres;
    String usr = "";
    ProgressDialog pDialog = null, pAutentic = null;
    Boolean connec = false;
    JSONObject jsonbb, jsonEstado,llenado;
    TableRow tr;
    Validaciones validaciones;
    TextView versionApp;
    Button btnBorrarBD;



    public InsPendientesActivity(){

        db=new DBprovider(this);
        validaciones = new Validaciones(this);
    }



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspecciones_pendientes);

        //VERSION DE LA APP
        versionApp = findViewById(R.id.versionApp);
        versionApp.setText(getString(R.string.version));


        //Comprobar la conexión a internet para cargar las inspecciones
        connec = new ConexionInternet(this).isConnectingToInternet();

        usr = db.obtenerUsuario();
        new SendPostRequest().execute(usr);

        //ejecutar el servicio si esque hay inspecciones pendientes
        int insp_e2 = db.estadoInspecciones(2);
        int insp_e3 = db.estadoInspecciones(3);

        if (insp_e2 > 0 && insp_e3 == 0) {
            if (connec) {
                Intent servis = new Intent(this, TransferirInspeccion.class);
                startService(servis);
            }
        }


        //refresca el layout
        refres = findViewById(R.id.swipeM);
        refres.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startActivity(getIntent());

                //comprobar nuevamente
                connec = new ConexionInternet(InsPendientesActivity.this).isConnectingToInternet();
                if(connec) {
                    usr = db.obtenerUsuario();
                    new SendPostRequest().execute(usr);
                }else{
                    Toast.makeText(InsPendientesActivity.this,"No hay conexión a internet",Toast.LENGTH_SHORT).show();
                }
                refres.setRefreshing(false);
            }
        });

        if(connec) {
            //mandar la versión que se está ocupando
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(getString(R.string.URL_BASE))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            final InterfacePost servicio = retrofit.create(InterfacePost.class);

            pAutentic = new ProgressDialog(InsPendientesActivity.this);
            pAutentic.setMessage("Autenticando versión...");
            pAutentic.setIndeterminate(false);
            pAutentic.setCancelable(false);

            pAutentic.show();


            Call<Resp_versionapp> respVersionCall = servicio.getVersion(usr,getString(R.string.version));
            respVersionCall.enqueue(new Callback<Resp_versionapp>() {
                @Override
                public void onResponse(Call<Resp_versionapp> call, Response<Resp_versionapp> response) {
                    int statusCode = response.code();
                    Resp_versionapp resp_versionapp = response.body();
                    Log.d("Version","onResponse"+statusCode);

                    pAutentic.dismiss();

                    if(resp_versionapp.getMSJ().equals("Version desactualizada")){

                        //Toast.makeText(InsPendientesActivity.this,"Desactualizada",Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder alertdialog = new AlertDialog.Builder(InsPendientesActivity.this);
                        alertdialog.setMessage("Debe actualizar la aplicación en Play store!!, recuerda terminar las transmisiones antes de actualizar");

                        alertdialog.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                final String appPackageName = getPackageName(); // obtiene el nombre de la app
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                }
                            }
                        });

                        alertdialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(InsPendientesActivity.this,"Actualización cancelada",Toast.LENGTH_SHORT).show();
                            }
                        });
                        alertdialog.show();
                    }
                }

                @Override
                public void onFailure(Call<Resp_versionapp> call, Throwable t) {
                    Log.e("Version", "onFailure" + t.getMessage());
                    Toast.makeText(InsPendientesActivity.this,"Error en la verificación",Toast.LENGTH_SHORT).show();
                    pAutentic.dismiss();
                }
            });
        }


        btnBorrarBD = findViewById(R.id.btnBorrarBD);
        btnBorrarBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String usr_inspector = db.obtenerUsuario();
                //Log.e("BOTON BORRAR BD", "pasa por acá al apretar el boton, usuario: "+usr_inspector);

                try {

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(getString(R.string.URL_BASE))
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    final InterfacePost servicio = retrofit.create(InterfacePost.class);

                    Call<resp_bd> resp_bd_call = servicio.getRespBD(usr_inspector,"0");

                    Log.e("LINK2", resp_bd_call.request().toString());

                    resp_bd_call.enqueue(new Callback<resp_bd>() {
                        @Override

                        public void onResponse(Call<resp_bd> call, Response<resp_bd> response) {
                            Log.e("PASEE", "PASEEEEE");
                            int statusCode = response.code();
                            resp_bd resp_bdd = response.body();
                            Log.d("respuesta call", "respu :" + statusCode);
                            Log.d("respuesta final", "res :" + resp_bdd.getMSJ());

                            String resultadoBD = "";
                            if(resp_bdd.getMSJ().equals("1"))
                            {
                               resultadoBD = db.EliminarDatosTablaDB();
                                Log.d("respuesta BD", resultadoBD);

                                Intent i = new Intent(InsPendientesActivity.this, InsPendientesActivity.class);
                                startActivity(i);
                            }
                            else
                            {
                                Toast.makeText(InsPendientesActivity.this, "No tiene autorización para borrar los datos de la APP", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<resp_bd> call, Throwable t) {
                            Log.e("PASE MAL", "MAAAAAL "+t.getMessage());
                        }
                    });

                } catch (Exception ex) {
                    Log.e("Error", "ex: " + ex);
                }
            }

        });
    }


    public class SendPostRequest extends AsyncTask<String, Integer, String> {

        protected void onPreExecute(){
            pDialog= new ProgressDialog(InsPendientesActivity.this);
            pDialog.setTitle("Datos LET");
            pDialog.setMessage("Descargando...");
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        protected String doInBackground(String... parametros) {
            publishProgress(0);
            connec = new ConexionInternet(InsPendientesActivity.this).isConnectingToInternet();
            try{
                if(connec)
                {
                    URL urll = new URL("https://www.autoagenda.cl/movil/cargamovil/cantidadComunasMovil");

                    HttpsURLConnection conns = (HttpsURLConnection)urll.openConnection();
                    conns.setReadTimeout(15000);
                    conns.setConnectTimeout(15000);
                    conns.setRequestMethod("POST");
                    conns.setDoInput(true);
                    conns.setDoOutput(true);

                    OutputStream osc = conns.getOutputStream();
                    BufferedWriter writerr = new BufferedWriter(new OutputStreamWriter(osc,"UTF-8"));
                    writerr.flush();
                    writerr.close();
                    osc.close();

                    int respon = conns.getResponseCode();
                    if(respon==HttpsURLConnection.HTTP_OK){
                        BufferedReader inne=new BufferedReader(new InputStreamReader(conns.getInputStream()));

                        StringBuffer sbb = new StringBuffer("");
                        String lnn = "";
                        while ((lnn=inne.readLine())!=null){
                            sbb.append(lnn);
                            break;
                        }
                        inne.close();
                        Integer total = db.listatotalComunasRegiones();
                        String json = sbb.toString();
                        try{
                            JSONObject obj = new JSONObject(json);
                            Integer numero = Integer.parseInt(obj.getString("MSJ"));
                            if(total.equals(numero)){
                                Log.e("Prueba","No se actualiza");
                            }else{
                                db.borrarComunas();
                                try{
                                    URL url2 = new URL("https://www.autoagenda.cl/movil/cargamovil/cargaComuna");
                                    HttpsURLConnection coni = (HttpsURLConnection) url2.openConnection();
                                    coni.setReadTimeout(15000 /* milliseconds */);
                                    coni.setConnectTimeout(15000 /* milliseconds */);
                                    coni.setRequestMethod("POST");
                                    coni.setDoInput(true);
                                    coni.setDoOutput(true);

                                    OutputStream osi = coni.getOutputStream();
                                    BufferedWriter writerrr = new BufferedWriter(new OutputStreamWriter(osi,"UTF-8"));

                                    writerrr.flush();
                                    writerrr.close();
                                    osi.close();

                                    int respone = coni.getResponseCode();
                                    if(respone == HttpsURLConnection.HTTP_OK){
                                        BufferedReader innn = new BufferedReader(new InputStreamReader(coni.getInputStream()));

                                        StringBuffer sbbb = new StringBuffer("");
                                        String linn = "";

                                        while ((linn=innn.readLine())!=null){
                                            sbbb.append(linn);
                                            break;
                                        }
                                        innn.close();

                                        String jsonn = sbbb.toString();
                                        try{
                                            JSONArray jsonjoha = new JSONArray(jsonn);
                                            if(!jsonjoha.isNull(0))
                                            {
                                                String result="";
                                                for(int i=0;i<jsonjoha.length();i++){
                                                    publishProgress(i+1);
                                                    JSONObject jsonnn = new JSONObject(jsonjoha.getString(i));
                                                    result = db.insertarComuna(jsonnn.getInt("id_region"),jsonnn.getString("region"),jsonnn.getString("comuna"));
                                                }
                                            }
                                        }catch (Exception e)
                                        {
                                            return e.getMessage();
                                        }
                                    }

                                }catch (Exception e){
                                    return  e.getMessage();
                                }
                            }
                        }catch (Exception e){
                            return e.getMessage();
                        }
                    }
                }
            }catch (Exception e){
                return e.getMessage();
            }



            String respuestaEstado = "";

            try {
                //pregunto sobre la conexión
                if (connec) {
                    String inspecciones[][] = db.listaInspecciones();
                    if (inspecciones.length > 0) {
                        for (int i = 0; i < inspecciones.length; i++) {
                            publishProgress(i+1);
                            URL url = new URL("https://www.autoagenda.cl/movil/cargamovil/estadoOI");
                            JSONObject postDataParams = new JSONObject();
                            postDataParams.put("usr", parametros[0]);
                            postDataParams.put("id_inspeccion", inspecciones[i][0]);
                            Log.e("Parametross", postDataParams.toString());

                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setReadTimeout(15000);
                            conn.setConnectTimeout(15000);
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
                            if (responseCode == HttpURLConnection.HTTP_OK) {
                                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                                StringBuffer sb = new StringBuffer("");
                                String line = "";

                                while ((line = in.readLine()) != null) {
                                    sb.append(line);
                                    break;
                                }

                                in.close();

                                respuestaEstado = sb.toString();
                                //Log.i("por aca","dd" +respuestaEstado);

                                jsonEstado = new JSONObject(respuestaEstado);
                                if(jsonEstado.getString("MSJ").equals("No"))
                                {
                                    db.borrarInspeccion(Integer.parseInt(inspecciones[i][0]));
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                return respuestaEstado = "Mensaje de error: " + e.getMessage();
            }



            try {
                if(connec) {
                    URL url = new URL("https://www.autoagenda.cl/movil/cargamovil/cargaInspecciones");

                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("usr_inspector", parametros[0]);
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

                        String json = sb.toString();
                        JSONArray jsonar = new JSONArray(json);
                        try {

                            //descarga inspecciones
                            if (!jsonar.isNull(0)) {
                                String result = "";
                                for (int i = 0; i < jsonar.length(); i++) {
                                    jsonbb = new JSONObject(jsonar.getString(i));
                                    db.borrarInspeccion(jsonbb.getInt("id_inspeccion"));
                                    result = db.insertaInspecciones(jsonbb.getInt("id_inspeccion"),jsonbb.getString("asegurado"),jsonbb.getString("paterno_asegurado"),
                                            jsonbb.getString("materno_asegurado"),jsonbb.getString("rut"),jsonbb.getString("comuna_asegurado"),jsonbb.getString("direccion_asegurado"),
                                            jsonbb.getInt("fono"),jsonbb.getString("marca"),jsonbb.getString("modelo"),jsonbb.getString("direccion_cita"),jsonbb.getString("fecha_cita"),
                                            jsonbb.getString("hora_cita"),jsonbb.getString("comuna_cita"),jsonbb.getString("comentario_cita"),jsonbb.getString("id_ramo"),
                                            jsonbb.getString("patente"),jsonbb.getString("cia"),jsonbb.getString("corredor"),jsonbb.getString("pac"), jsonbb.getString("email"),jsonbb.getInt("obd"));

                                    //[{,","","",","","pac":null,"motor":"","chasis":"","anio":"0"}
                                    if(jsonbb.getString("id_ramo").equals("vl1"))
                                    {
                                        //hacer jsons
                                        JSONObject valor1 = new JSONObject();
                                        valor1.put("valor_id",2);
                                        valor1.put("texto",jsonbb.getString("asegurado"));

                                        JSONObject valor2 = new JSONObject();
                                        valor2.put("valor_id",3);
                                        valor2.put("texto",jsonbb.getString("paterno_asegurado"));

                                        JSONObject valor3 = new JSONObject();
                                        valor3.put("valor_id",4);
                                        valor3.put("texto",jsonbb.getString("materno_asegurado"));

                                        JSONObject valor4 = new JSONObject();
                                        valor4.put("valor_id",5);
                                        valor4.put("texto",jsonbb.getString("rut"));

                                        JSONObject valor5 = new JSONObject();
                                        valor5.put("valor_id",7);
                                        valor5.put("texto",jsonbb.getString("comuna_asegurado"));

                                        JSONObject valor6 = new JSONObject();
                                        valor6.put("valor_id",6);
                                        valor6.put("texto",jsonbb.getInt("fono"));

                                        JSONObject valor9 = new JSONObject();
                                        valor9.put("valor_id",8);
                                        valor9.put("texto",jsonbb.getString("direccion_asegurado"));

                                        JSONObject valor10 = new JSONObject();
                                        valor10.put("valor_id",10);
                                        valor10.put("texto",jsonbb.getString("marca"));

                                        JSONObject valor11 = new JSONObject();
                                        valor11.put("valor_id",11);
                                        valor11.put("texto",jsonbb.getString("modelo"));

                                        //direccion_cita
                                        JSONObject valor12 = new JSONObject();
                                        valor12.put("valor_id",358);
                                        valor12.put("texto",jsonbb.getString("direccion_cita"));

                                        //FECHA CITA
                                        JSONObject valor13 = new JSONObject();
                                        valor13.put("valor_id",360);
                                        valor13.put("texto",jsonbb.getString("fecha_cita"));

                                        //HORA CITA
                                        JSONObject valor14 = new JSONObject();
                                        valor14.put("valor_id",361);
                                        valor14.put("texto",jsonbb.getString("hora_cita"));

                                        //comuna_cita
                                        JSONObject valor15 = new JSONObject();
                                        valor15.put("valor_id",359);
                                        valor15.put("texto",jsonbb.getString("comuna_cita"));

                                        /*JSONObject valor16 = new JSONObject();
                                        valor16.put("valor_id",301);
                                        valor16.put("texto",jsonbb.getString("comentario_cita"));*/

                                    /*JSONObject valor8 = new JSONObject();
                                    valor7.put("valor_id",);
                                    valor7.put("texto",jsonbb.getString("id_ramo"));*/

                                        JSONObject valor17 = new JSONObject();
                                        valor17.put("valor_id",363);
                                        valor17.put("texto",jsonbb.getString("patente"));

                                        //cia
                                    /*JSONObject valor18 = new JSONObject();
                                    valor18.put("valor_id",)*/

                                        JSONObject valor19 = new JSONObject();
                                        valor19.put("valor_id",9);
                                        valor19.put("texto",jsonbb.getString("corredor"));
                                        //pac
                                    /*JSONObject valor20 = new JSONObject();
                                    valor20.put()*/

                                        JSONObject valor21 = new JSONObject();
                                        valor21.put("valor_id",16);
                                        valor21.put("texto",jsonbb.getString("motor"));

                                        JSONObject valor22 = new JSONObject();
                                        valor22.put("valor_id",17);
                                        valor22.put("texto",jsonbb.getString("chasis"));

                                        JSONObject valor23 = new JSONObject();
                                        valor23.put("valor_id",13);
                                        valor23.put("texto",jsonbb.getString("anio"));


                                        JSONArray datosvalores = new JSONArray();
                                        datosvalores.put(valor1);
                                        datosvalores.put(valor2);
                                        datosvalores.put(valor3);
                                        datosvalores.put(valor4);
                                        datosvalores.put(valor5);
                                        datosvalores.put(valor6);
                                        //datosvalores.put(valor7);
                                        //datosvalores.put(valor8);
                                        datosvalores.put(valor9);
                                        datosvalores.put(valor10);
                                        datosvalores.put(valor11);
                                        datosvalores.put(valor12);
                                        datosvalores.put(valor13);
                                        datosvalores.put(valor14);
                                        datosvalores.put(valor15);
                                        //datosvalores.put(valor16);
                                        datosvalores.put(valor17);
                                        //datosvalores.put(valor18);
                                        datosvalores.put(valor19);
                                        //datosvalores.put(valor20);
                                        datosvalores.put(valor21);
                                        datosvalores.put(valor22);
                                        datosvalores.put(valor23);
                                        //datosvalores.put(valor24);

                                        //SE ACTUALIZAN LOS DATOS DE LAS INSPECCIONES

                                        if(!datosvalores.isNull(0)){
                                            for(int ii=0;ii<datosvalores.length();ii++){
                                                llenado = new JSONObject(datosvalores.getString(ii));
                                                db.insertarValor(jsonbb.getInt("id_inspeccion"),llenado.getInt("valor_id"),llenado.getString("texto"));
                                                //validaciones.insertarDatos(jsonbb.getInt("id_inspeccion"),llenado.getInt("valor_id"),llenado.getString("texto"));
                                            }
                                        }
                                    }
                                    else
                                    {

                                        JSONObject valor85 = new JSONObject();
                                        valor85.put("valor_id",365);
                                        valor85.put("texto",jsonbb.getString("asegurado"));

                                        JSONObject valor86 = new JSONObject();
                                        valor86.put("valor_id",366);
                                        valor86.put("texto",jsonbb.getString("paterno_asegurado"));

                                        JSONObject valor87 = new JSONObject();
                                        valor87.put("valor_id",367);
                                        valor87.put("texto",jsonbb.getString("materno_asegurado"));

                                        JSONObject valor88 = new JSONObject();
                                        valor88.put("valor_id",368);
                                        valor88.put("texto",jsonbb.getString("rut"));

                                        JSONObject valor89 = new JSONObject();
                                        valor89.put("valor_id",371);
                                        valor89.put("texto",jsonbb.getString("direccion_asegurado"));

                                        JSONObject valor90 = new JSONObject();
                                        valor90.put("valor_id",369);
                                        valor90.put("texto",jsonbb.getInt("fono"));


                                        JSONObject datosValorVpCo = new JSONObject();
                                        datosValorVpCo.put("valor_id",370);
                                        datosValorVpCo.put("texto",jsonbb.getString("comuna_asegurado"));

                                        JSONArray datosvalores = new JSONArray();
                                        datosvalores.put(valor85);
                                        datosvalores.put(valor86);
                                        datosvalores.put(valor88);
                                        datosvalores.put(valor89);
                                        datosvalores.put(valor90);
                                        datosvalores.put(datosValorVpCo);

                                        //SE ACTUALIZAN LOS DATOS DE LAS INSPECCIONES

                                        if(!datosvalores.isNull(0)){
                                            for(int ii=0;ii<datosvalores.length();ii++){
                                                llenado = new JSONObject(datosvalores.getString(ii));
                                                db.insertarValor(jsonbb.getInt("id_inspeccion"),llenado.getInt("valor_id"),llenado.getString("texto"));
                                                //validaciones.insertarDatos(jsonbb.getInt("id_inspeccion"),llenado.getInt("valor_id"),llenado.getString("texto"));
                                            }
                                        }
                                    }




                                    //falta hacer filtro para descargar inspeccion = poner estado
                                    if(result.equals("Ok"))
                                    {


                                        URL url2 = new URL("https://www.autoagenda.cl/movil/cargamovil/descargaInspeccionMovil");

                                        JSONObject postData = new JSONObject();
                                        postData.put("usr_inspector",parametros[0]);
                                        postData.put("id_inspeccion",Integer.toString(jsonbb.getInt("id_inspeccion")));
                                        Log.e("Parametros de descarga",postData.toString());

                                        HttpURLConnection cone = (HttpURLConnection)url2.openConnection();
                                        cone.setReadTimeout(15000);
                                        cone.setConnectTimeout(15000);
                                        cone.setRequestMethod("POST");
                                        cone.setDoInput(true);
                                        cone.setDoOutput(true);

                                        OutputStream oss = cone.getOutputStream();
                                        BufferedWriter writerr = new BufferedWriter(new OutputStreamWriter(oss,"UTF-8"));
                                        writerr.write(getPostDataString(postData));

                                        writerr.flush();
                                        writerr.close();
                                        oss.close();

                                        int respon = cone.getResponseCode();
                                        if(respon == HttpURLConnection.HTTP_OK){
                                            BufferedReader ini = new BufferedReader(new InputStreamReader(cone.getInputStream()));

                                            StringBuffer sbb = new StringBuffer("");
                                            String linee = "";

                                            while ((linee = ini.readLine())!=null){
                                                sbb.append(linee);
                                                break;
                                            }

                                            ini.close();

                                            String jsonDesc = sbb.toString();
                                            try{
                                                JSONObject jsonDesca = new JSONObject(jsonDesc);
                                                if(jsonDesca.getString("MSJ").equals("Ok")){
                                                    Log.e("LET", "No se borra de la lista");
                                                }else{
                                                    db.borrarInspeccion(jsonbb.getInt("id_inspeccion"));
                                                }
                                            }catch (Exception e) {
                                                Log.e("LET", "No se pudo convertir: \"" + json + "\"");
                                            }


                                        }
                                    }
                                }
                            } else {
                                return "No hay inspecciones";
                            }

                        } catch (Throwable t) {
                            Log.e("LET", "Could not parse malformed JSON: \"" + json + "\"");
                        }

                        return sb.toString();

                    } else {
                        return new String("false : " + responseCode);
                    }
                }
            } catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
            return respuestaEstado;


        }


        @Override
        protected void onProgressUpdate(Integer... progreso)
        {
            pDialog.setProgress(progreso[0]);
        }



        private void llenarTabla(){
            Inspeccion ins;
            ArrayList<Inspeccion> lista = new ArrayList<>();

            String valores[][] = db.listaInspecciones();

            for(int i=0;i<valores.length;i++){
                ins = new Inspeccion();
                ins.setId_inspeccion(Integer.parseInt(valores[i][0]));
                ins.setPatente(db.accesorio(Integer.parseInt(valores[i][0]),363).toString());
                ins.setFecha(valores[i][1]);
                ins.setHora(valores[i][2]);
                lista.add(ins);
            }

            insp = new String[lista.size()][4];

            for(int i = 0;i<lista.size();i++){
                Inspeccion in = lista.get(i);

                insp[i][0]=String.valueOf(in.getId_inspeccion());
                insp[i][1]=in.getPatente();
                insp[i][2]=in.getFecha();
                insp[i][3]=in.getHora();
            }
        }


        String[] head = {"OI","PATENTE","FECHA","CITA"};
        String[][] insp;
        @Override
        protected void onPostExecute(String result) {


                pDialog.dismiss();

                TableColumnWeightModel columnModel = new TableColumnWeightModel(4);
                columnModel.setColumnWeight(1, 1);
                columnModel.setColumnWeight(2, 1);
                columnModel.setColumnWeight(3, 1);
                columnModel.setColumnWeight(4, 5);

                final TableView<String[]> tb = (TableView<String[]>) findViewById(R.id.tableView);
                tb.setColumnModel(columnModel);
                tb.setHeaderBackgroundColor(Color.parseColor("WHITE"));
                tb.setHeaderElevation(10);
                llenarTabla();

                tb.setHeaderAdapter(new SimpleTableHeaderAdapter(InsPendientesActivity.this, head));

                tb.setDataAdapter(new SimpleTableDataAdapter(InsPendientesActivity.this, insp));


                tb.addDataClickListener(new TableDataClickListener<String[]>() {
                    @Override
                    public void onDataClicked(int rowIndex, String[] clickedData) {
                        //Toast.makeText(InsPendientesActivity.this,((String[])clickedData)[0],Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(InsPendientesActivity.this, FotoGeolocalizacion.class);
                        i.putExtra("id_inspeccion", ((String[]) clickedData)[0]);
                        i.putExtra("fecha_cita", ((String[]) clickedData)[2]);
                        i.putExtra("hora_cita", ((String[]) clickedData)[3]);

                        int inspeccionElegida = Integer.parseInt(((String[]) clickedData)[0]);

                        try {
                            //VALIDAR QUE LA INSPECCION SELECCIONADA NO SE ESTÉ TRANSMITIENDO
                            if (db.estadoInspeccion(inspeccionElegida) != 2 && db.estadoInspeccion(inspeccionElegida) != 3) {
                                //VALIDAR QUE NO HAYA QUEDADO UNA INSPECCIÓN SIN TRANSMITIR
                                int inspeccionPendiente = db.estadoInspecciones(1);
                                if (inspeccionPendiente > 0 && inspeccionPendiente != inspeccionElegida) {
                                    Toast.makeText(InsPendientesActivity.this, "Debe transmitir la Inspección n°" + String.valueOf(inspeccionPendiente), Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(i);
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(InsPendientesActivity.this, "Inspección eliminada por termino de su transmisión", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }

}
