package com.letchile.let.Servicios;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.letchile.let.BD.DBprovider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by LETCHILE on 01/03/2018.
 */

public class TransferirInspeccion extends Service {

    DBprovider db;
    int id_inspeccion = 0;
    int estado_oi=0;
    int contador_oi=0;
    Boolean conn = false;
    private NotificationManager notificationManager;
    private static final int ID_NOTIFICACION = 1234;
    String usuario="", id_inspecc="" ;


    Calendar c = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String strDate = sdf.format(c.getTime());

    public TransferirInspeccion(){
        db = new DBprovider(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //INSTANCIAR LA NOTIFICACION
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId) {
        super.onStartCommand(intent, flags, startId);

    try {
        conn = new ConexionInternet(this).isConnectingToInternet();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getBaseContext())
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("LET CHILE SPA")
                .setContentText("Servicio de transferencia iniciada")
                .setWhen(System.currentTimeMillis());



        //lanzar notificacion de servicio activo
        notificationManager.notify(ID_NOTIFICACION, builder.build());

        usuario=db.obtenerUsuario();
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "LogInspector_"+usuario+".txt");
        if (conn) {

            transferirInspeccionCompleta tranf = new transferirInspeccionCompleta();
            Toast.makeText(this, "servicio activo" + conn, Toast.LENGTH_SHORT).show();
            //Consultar las inspecciones en estado dos y tres
            int insp_e2 = db.estadoInspecciones(2);
            int insp_e3 = db.estadoInspecciones(3);

            //preguntar por la primera inspeccion en estado 2 (por transmitir)
            if (insp_e2 > 0 && insp_e3 == 0) {

                //cambiar a estado para transmitir estado 3
               // Toast.makeText(this, "cambia estado 3 si hay alguna en estado 2", Toast.LENGTH_SHORT).show();
                db.cambiarEstadoInspeccion(insp_e2, 3);
                //estado_oi=db.estadoInspeccion(id_inspeccion);

                //Log.i("Status servicio", "xxxx" + estado_oi + insp_e2);

                JSONArray jsonArray = new JSONArray();
                String accesorios[][] = db.listaAccesoriosParaEnviar(insp_e2);
                for (int i = 0; i < accesorios.length; i++) {
                    try {

                        JSONObject valores = new JSONObject();
                        valores.put("idcampo", accesorios[i][0]);
                        valores.put("valor", accesorios[i][1]);
                        jsonArray.put(valores);


                    } catch (JSONException e15) {
                        Log.e("Error", "e15: " + e15);

                    }
                }

                //paso los datos a variables
                String variable1 = String.valueOf(insp_e2);
                String variable2 = jsonArray.toString();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    tranf.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, variable1, variable2);

                else
                    tranf.execute(variable1, variable2);


            } else if (insp_e2 == 0 && insp_e3 == 0) {
                //si no hay transmisión faltante o en desarrollo cerrar servicio
               //Toast.makeText(this, "no hay inspecciones por transmitir", Toast.LENGTH_SHORT).show();
                onDestroy();
            } else if(insp_e3>0){

                JSONArray jsonArray = new JSONArray();
                String accesorios[][] = db.listaAccesoriosParaEnviar(insp_e3);
                for (int i = 0; i < accesorios.length; i++) {
                    try {

                        JSONObject valores = new JSONObject();
                        valores.put("idcampo", accesorios[i][0]);
                        valores.put("valor", accesorios[i][1]);
                        jsonArray.put(valores);


                    } catch (JSONException e15) {
                        Log.e("Error", "e15: " + e15);

                    }
                }

                //paso los datos a variables
                String variable1 = String.valueOf(insp_e3);
                String variable2 = jsonArray.toString();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    tranf.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, variable1, variable2);

                else
                    tranf.execute(variable1, variable2);
            }

            try {

                String estConec="";

                if (conn.toString().equals("true"))
                {
                    estConec="Ok";
                }


                if (!file.exists()) {
                    file.mkdir();
                }

                OutputStreamWriter fout = null;

                fout = new FileWriter(file.getAbsoluteFile(), true);
                fout.write(insp_e2+" | Respuesta conexión a internet... "+ estConec +" | "+ strDate);
                fout.append("\n\r");
                fout.close();

            } catch (Exception ex) {
                Log.e("Error conec", "ex: " + ex);

            }


        } else {
            Toast.makeText(this, "No cuenta con internet para transmitir", Toast.LENGTH_SHORT).show();

            if (!file.exists()) {
                file.mkdir();
            }

            OutputStreamWriter fout = null;

            fout = new FileWriter(file.getAbsoluteFile(), true);
            fout.write("No cuenta con internet para transmitir | "+ strDate);
            fout.append("\n\r");
            fout.close();

            onDestroy();
        }
    }catch (Exception e){
        Log.e("Error",e.getMessage());
    }
        return START_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        notificationManager.cancel(ID_NOTIFICACION);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //en la tarea asincronica se cambia la inspección a transmitida en caso de
    public class transferirInspeccionCompleta extends AsyncTask<String,Void,String>{

        @Override

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            usuario=db.obtenerUsuario();
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "LogInspector_"+usuario+".txt");

            ////////////////////////////////ENVIA DATOS DE DIGITACIÓN///////////////////////////////////////////////////////////////////////////

            try{

                URL url = new URL("https://www.autoagenda.cl/movil/cargamovil/datosValor"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("id_inspeccion", strings[0]);
                postDataParams.put("dataV", strings[1]);
                Log.e("Parametros a pasar", postDataParams.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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

                Log.e("Status servicio", String.valueOf(responseCode));


                try {

                    if (!file.exists()) {
                        file.mkdir();
                    }

                    OutputStreamWriter fout = null;
                    fout = new FileWriter(file.getAbsoluteFile(), true);
                    fout.write(strings[0]+ " | Datos a transmitir... "+ strings[1] + "|"+postDataParams.toString()+" | "+strDate );
                    fout.append("\n\r");
                    fout.close();

                } catch (Exception ex1) {
                    Log.e("Error", "ex1: " + ex1);

                }


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

                    try {

                        if (!file.exists()) {
                            file.mkdir();
                        }

                        OutputStreamWriter fout = null;
                        fout = new FileWriter(file.getAbsoluteFile(), true);
                        fout.write(strings[0]+ " | Datos transmitidos... "+ sb.toString() +" | "+strDate );
                        fout.append("\n\r");
                        fout.close();

                        JSONObject respTrans = new JSONObject(sb.toString());
                        Log.e("respnacho", respTrans.getString("MSJ"));


                        if (respTrans.getString("MSJ").equals("Ok"))
                        {
                            db.insertRegistroTransDatos(Integer.parseInt(strings[0]),2);
                            Log.e("INSERT TRANS DATOS", "Ok");
                        }
                        else
                        {
                            db.insertRegistroTransDatos(Integer.parseInt(strings[0]),1);
                            Log.e("INSERT TRANS DATOS", "ERROR : "+respTrans.getString("MSJ"));
                        }

                    } catch (Exception ex2) {
                        Log.e("Error", "ex2: " + ex2);

                        db.insertRegistroTransDatos(Integer.parseInt(strings[0]),1);
                    }

                }else{

                    try {

                        if (!file.exists()) {
                            file.mkdir();
                        }

                        OutputStreamWriter fout = null;
                        fout = new FileWriter(file.getAbsoluteFile(), true);
                        fout.write(strings[0]+ " | Error de conexión a internet... "+ responseCode +" | "+strDate );
                        fout.append("\n\r");
                        fout.close();

                        db.insertRegistroTransDatos(Integer.parseInt(strings[0]),1);

                    } catch (Exception ex3) {
                        Log.e("Error", "ex3: " + ex3);

                        db.insertRegistroTransDatos(Integer.parseInt(strings[0]),1);

                    }
                    onDestroy(); // SIN INTERNET Y/O OTRO ERROR
                }

            }catch (Exception e){


                Log.e("Error datos",e.getMessage());

                db.cambiarEstadoInspeccion(Integer.parseInt(strings[0]),2);
                //estado_oi=db.estadoInspeccion(id_inspeccion);
                //Log.i("Status servicio", "vcvcv" +Integer.parseInt(strings[0])+ estado_oi );
                try {

                    if (!file.exists()) {
                        file.mkdir();
                    }

                    OutputStreamWriter fout = null;
                    fout = new FileWriter(file.getAbsoluteFile(), true);
                    fout.write(strings[0]+ " | Error servicio de datos... "+ e.getMessage() +" | "+strDate );
                    fout.append("\n\r");
                    fout.close();

                    db.insertRegistroTransDatos(Integer.parseInt(strings[0]),1);

                } catch (Exception ex4) {
                    Log.e("Error", "ex4: " + ex4);

                }
                onDestroy();
            }


            ///////////////////////////////////////ENVIA FOTOS////////////////////////////////////////////////////////////////////////////////////
           String fotos[][] = db.ListaDatosFotos(Integer.parseInt(strings[0]));
            //cuenta el total de fotos
            for(int i=0;i<fotos.length;i++){

                conn = new ConexionInternet(TransferirInspeccion.this).isConnectingToInternet();
                //pregunta si tienen internet por cada foto que pasa

                if(conn) {
                    try {

                        //Log.i("largoxx","largoxxx" + fotos.length + i);

                        URL url = new URL("https://www.autoagenda.cl/movil/cargamovil/descargafotos64"); // here is your URL path

                        JSONObject postDataParams = new JSONObject();
                        postDataParams.put("id_inspeccion", fotos[i][0]);
                        postDataParams.put("nombre_foto", fotos[i][1]);
                        postDataParams.put("archivo", fotos[i][3]);
                        postDataParams.put("comentario", fotos[i][2]);
                        Log.e("Parametos a pasar fotos", postDataParams.toString());
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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

                        Log.e("Status servicio", String.valueOf(responseCode));



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


                            try {
                                if (sb.toString().equals("Ok")) {
                                    //i++;
                                    //eliminar

                                   // db.cambiarEstadoFoto(Integer.parseInt(strings[0]), fotos[i][1].toString(), fotos[i][2].toString(), 2);
                                    db.cambiarEstadoFoto(Integer.parseInt(strings[0]),fotos[i][1],fotos[i][2],2);

                                    //Log.i("largozzz, estado","largo.. cambia estado" + fotos.length + i);
                                    try {

                                        if (!file.exists()) {
                                            file.mkdir();
                                        }

                                        OutputStreamWriter fout = null;
                                        fout = new FileWriter(file.getAbsoluteFile(), true);
                                        fout.write(strings[0]+ " | Fotos enviadas... "+ sb.toString() +"|"+fotos[i][1]+" | "+strDate );
                                        fout.append("\n\r");
                                        fout.close();

                                    } catch (Exception ex5) {
                                        Log.e("Error", "ex5: " + ex5);

                                    }

                                }else{
                                    db.cambiarEstadoInspeccion(Integer.parseInt(fotos[i][0]), 2);
                                    Log.e("Errror transmision", "Error en la transmisión");
                                    //Toast.makeText(TransferirInspeccion.this,"Error en la transmisión intente nuevamente",Toast.LENGTH_SHORT);
                                    try {

                                        if (!file.exists()) {
                                            file.mkdir();
                                        }

                                        OutputStreamWriter fout = null;
                                        fout = new FileWriter(file.getAbsoluteFile(), true);
                                        fout.write(strings[0]+ " | Error en envío de fotos... "+ sb.toString() +" | "+fotos[i][1]+" | "+strDate );
                                        fout.append("\n\r");
                                        fout.close();

                                    } catch (Exception ex6) {
                                        Log.e("Error", "ex6: " + ex6);

                                    }
                                    onDestroy();
                                }
                            } catch (Exception e) {
                                //sacar si esque se cae
                                //i++;
                                db.cambiarEstadoInspeccion(Integer.parseInt(fotos[i][0]), 2);
                                Log.e("Error transmision", e.getMessage());
                                //Toast.makeText(TransferirInspeccion.this,"Error en la transmisión intente nuevamente",Toast.LENGTH_SHORT);
                                try {

                                    if (!file.exists()) {
                                        file.mkdir();
                                    }

                                    OutputStreamWriter fout = null;
                                    fout = new FileWriter(file.getAbsoluteFile(), true);
                                    fout.write(strings[0]+ " | Error en envío de fotoss... "+ responseCode +" | "+fotos[i][1]+" | "+strDate );
                                    fout.append("\n\r");
                                    fout.close();

                                } catch (Exception ex7) {
                                    Log.e("Error", "ex7: " + ex7);

                                }
                                onDestroy();

                            }
                        } else {
                            db.cambiarEstadoInspeccion(Integer.parseInt(fotos[i][0]), 2);
                            try {

                                if (!file.exists()) {
                                    file.mkdir();
                                }

                                OutputStreamWriter fout = null;
                                fout = new FileWriter(file.getAbsoluteFile(), true);
                                fout.write(strings[0]+ " | Sin conexión servicio fotos... "+ responseCode +" | "+fotos[i][1]+" | "+strDate );
                                fout.append("\n\r");
                                fout.close();

                            } catch (Exception ex8) {
                                Log.e("Error", "ex8: " + ex8);

                            }
                            onDestroy(); // sin internet o otro error
                        }

                    } catch (Exception e9) {
                        //Log.e("Error fotos", e.getMessage());
                        Log.e("Error", "e9: " + e9);
                        try {

                            if (!file.exists()) {
                                file.mkdir();
                            }

                            OutputStreamWriter fout = null;
                            fout = new FileWriter(file.getAbsoluteFile(), true);
                            fout.write(strings[0]+ " | Error en servicio de fotoss...| "+ e9+" | " +fotos[i][1]+ " | "+strDate );
                            fout.append("\n\r");
                            fout.close();

                        } catch (Exception ex10) {
                            Log.e("Error", "ex10: " + ex10);

                        }

                    }
                }else{
                    //SI ESQUE ESTA TRANSMITIENDO LA FOTO Y QUEDA SIN INTERNET SACAR DEL SERVICIO Y DEJAR EN ESTADO 2
                    db.cambiarEstadoInspeccion(Integer.parseInt(fotos[i][0]), 2);

                    try {

                        if (!file.exists()) {
                            file.mkdir();
                        }

                        OutputStreamWriter fout = null;
                        fout = new FileWriter(file.getAbsoluteFile(), true);
                        fout.write(strings[0]+ " | Sin conexión a internet ...  | "+fotos[i][1]+" | "+strDate );
                        fout.append("\n\r");
                        fout.close();

                    } catch (Exception ex11) {
                        Log.e("Error", "ex11: " + ex11);

                    }
                    onDestroy();
                }

            }

            /////////////////////////////////////////////INGRESA FECHA DE INSPECCION/////////////////////////////////////////////////////////
            String[][] datosInspeccion=db.BuscaDatosInspeccion(strings[0]);
                try{

                    //Toast.makeText(TransferirInspeccion.this,"servicio fecha oi",Toast.LENGTH_SHORT);
                    URL url;
                    if(datosInspeccion[0][7].toString().equals("vl1")) {
                        url = new URL("https://www.autoagenda.cl/movil/cargamovil/ingresoFechaInspeccion"); // here is your URL path
                    }else {
                        url = new URL("https://www.autoagenda.cl/movil/cargamovil/ingresoFechaInspeccionVp"); // here is your URL path
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                    String fechaHoraFallida = sdf.format(new Date());

                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("id_inspeccion",strings[0]);
                    postDataParams.put("fecha_inspeccion", fechaHoraFallida);
                    postDataParams.put("usr", db.obtenerUsuario());
                    Log.e("Parametos a pasar fecha", postDataParams.toString());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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

                    Log.e("Status servicio", String.valueOf(responseCode));

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

                        try {

                            if (!file.exists()) {
                                file.mkdir();
                            }

                            OutputStreamWriter fout = null;
                            fout = new FileWriter(file.getAbsoluteFile(), true);
                            fout.write(strings[0]+ " | Fecha de inspección enviada... "+ sb.toString() +" | "+strDate );
                            fout.append("\n\r");
                            fout.close();

                            JSONObject respTransFecha = new JSONObject(sb.toString());

                            if (respTransFecha.getString("MSJ").equals("Ok"))
                            {
                                db.insertRegistroTransFecha(Integer.parseInt(strings[0]),2);
                            }
                            else
                            {
                                db.insertRegistroTransFecha(Integer.parseInt(strings[0]),1);
                            }


                        } catch (Exception ex12) {
                            Log.e("Error", "ex12: " + ex12);

                            db.insertRegistroTransFecha(Integer.parseInt(strings[0]),1);
                        }

                    }else{
                        try {

                            if (!file.exists()) {
                                file.mkdir();
                            }

                            OutputStreamWriter fout = null;
                            fout = new FileWriter(file.getAbsoluteFile(), true);
                            fout.write(strings[0]+ " | Error en servicio de fecha ... "+ responseCode +" | "+strDate );
                            fout.append("\n\r");
                            fout.close();

                            db.insertRegistroTransFecha(Integer.parseInt(strings[0]),1);

                        } catch (Exception ex13) {
                            Log.e("Error", "ex13: " + ex13);

                            db.insertRegistroTransFecha(Integer.parseInt(strings[0]),1);

                        }
                        onDestroy(); // sin internet o otro error

                    }

                }catch (Exception e){

                    try {

                        if (!file.exists()) {
                            file.mkdir();
                        }

                        OutputStreamWriter fout = null;
                        fout = new FileWriter(file.getAbsoluteFile(), true);
                        fout.write(strings[0]+ " | Sin conexión a internet(servicio fecha)... "+ e.getMessage() +" | "+strDate );
                        fout.append("\n\r");
                        fout.close();

                        db.insertRegistroTransFecha(Integer.parseInt(strings[0]),1);

                    } catch (Exception ex14) {
                        Log.e("Error", "ex14: " + ex14);

                        db.insertRegistroTransFecha(Integer.parseInt(strings[0]),1);

                    }
                    Log.e("Error fecha",e.getMessage());
                }

            return strings[0];
        }

        @Override
       protected void onPostExecute(String result) {

            usuario=db.obtenerUsuario();
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "LogInspector_"+usuario+".txt");

            //rescata la inspeccion que se estaba transmitiendo
           id_inspeccion = Integer.parseInt(result);



           //String[][] contInspeccion=db.BuscaDatosInspeccion(result);
           //contador_oi=Integer.parseInt(contInspeccion[0][16]);
            /*estado_oi=db.estadoInspeccion(id_inspeccion);
            Log.i("PASE POR ACA",result + estado_oi);*/





            //TRAE LISTA DE FOTOS PENDIENTES DE TRANSMITIR , ESTADO 1
            String fotos[][] = db.ListaDatosFotos(id_inspeccion);
            //Log.e("LISTA FOTOS let2", fotos.toString());
            //Log.e("LISTA FOTOS let22",Arrays.toString(fotos));



            String respTransDatos = db.ConsultarTransDatos(id_inspeccion);
            String respTransFecha = db.ConsultarTransFecha(id_inspeccion);
            String respTransGeo = db.ConsultarTransGeo(id_inspeccion);

            if (!file.exists()) {
                file.mkdir();
            }

            try {

                if(Arrays.toString(fotos).equals("[]") && respTransDatos.equals("Ok") && respTransFecha.equals("Ok") && respTransGeo.equals("Ok"))
                {
                    Log.e("RECUENTO TOTAL ","NO HAY NADA PENDIENTE");

                    if (!file.exists()) {
                        file.mkdir();
                    }

                    OutputStreamWriter fout = null;
                    fout = new FileWriter(file.getAbsoluteFile(), true);
                    fout.write(result+" | RESULTADO CONSULTA TRANSMISION: NO HAY NADA PENDIENTE | "+ strDate);
                    fout.append("\n\r");
                    fout.close();

                    //la cambia a estado 4
                    db.cambiarEstadoInspeccion(id_inspeccion,4);

                    //la borra de la base de datos porsiacaso
                    db.deleteInspeccion(id_inspeccion);

                }
                else
                {
                    Log.e("RECUENTO TOTAL ","SI INFORMACION PENDIENTE: fotos= "+Arrays.toString(fotos)+" - data= "+respTransDatos + " - fecha= "+respTransFecha + " - GEO= "+ respTransGeo);

                    if (!file.exists()) {
                        file.mkdir();
                    }

                    OutputStreamWriter fout = null;
                    fout = new FileWriter(file.getAbsoluteFile(), true);
                    fout.write(result+" | RESULTADO CONSULTA TRANSMISION: SI HAY INFORMACION PENDIENTE: fotos= "+Arrays.toString(fotos)+" - data= "+respTransDatos+" - fecha= "+respTransFecha+ " - GEO= "+respTransGeo+" | "+ strDate);
                    fout.append("\n\r");
                    fout.close();
                }

            }
            catch (Exception ex15){

                Log.e("RECUENTO TOTAL ","ERROR , SE ACTIVO TRY CATCH "+ex15);

            }



            new Handler().postDelayed(new Runnable() {
                    public void run() {

                        transferirInspeccionCompleta tranf = new transferirInspeccionCompleta();

                        //Consultar las inspecciones en estado dos y tres
                        int insp_e2 = db.estadoInspecciones(2);
                        int insp_e3 = db.estadoInspecciones(3);


                        //preguntar por la primera inspeccion en estado 2 (por transmitir)
                        if (insp_e2 > 0 && insp_e3 == 0) {

                           // contador_oi= contador_oi + 1 ;
                            //db.actualizaContador(id_inspeccion,contador_oi);

                            //Log.i("por acaaa","estado" + contador_oi + insp_e2 + insp_e3+ id_inspeccion);

                            //cambiar a estado para transmitir a 3
                            db.cambiarEstadoInspeccion(insp_e2, 3);

                            JSONArray jsonArray = new JSONArray();
                            String accesorios[][] = db.listaAccesoriosParaEnviar(insp_e2);
                            for (int i = 0; i < accesorios.length; i++) {
                                try {

                                    JSONObject valores = new JSONObject();
                                    valores.put("idcampo", accesorios[i][0]);
                                    valores.put("valor", accesorios[i][1]);
                                    jsonArray.put(valores);

                                } catch (JSONException e) {

                                    e.printStackTrace();

                                }
                            }

                            //paso los datos a variables
                            String variable1 = String.valueOf(insp_e2);
                            String variable2 = jsonArray.toString();


                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                tranf.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, variable1, variable2);
                            else
                                tranf.execute(variable1, variable2);

                        } else if (insp_e2 == 0 && insp_e3 == 0) {
                            //si no hay transmisión faltante o en desarrollo cerrar servicio
                            //Log.i("termina proceso","termina proceso" +estado_oi + insp_e2 + insp_e3);
                            File file =new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "LogInspector_"+usuario+".txt");

                            try {

                                if (!file.exists()) {
                                    file.mkdir();
                                }

                                OutputStreamWriter fout = null;
                                fout = new FileWriter(file.getAbsoluteFile(), true);
                                fout.write(id_inspeccion+ " | Transmisión completa ... Ok | "+strDate );
                                fout.append("\n\r");
                                fout.close();

                            } catch (Exception ex16) {
                                Log.e("Error", "ex16: " + ex16);

                            }
                            onDestroy();
                        }
                        else if(insp_e3>0)
                        {
                            JSONArray jsonArray = new JSONArray();
                            String accesorios[][] = db.listaAccesoriosParaEnviar(insp_e3);
                            for (int i = 0; i < accesorios.length; i++) {
                                try {

                                    JSONObject valores = new JSONObject();
                                    valores.put("idcampo", accesorios[i][0]);
                                    valores.put("valor", accesorios[i][1]);
                                    jsonArray.put(valores);

                                } catch (JSONException e) {

                                    e.printStackTrace();

                                }
                            }

                            //paso los datos a variables
                            String variable1 = String.valueOf(insp_e3);
                            String variable2 = jsonArray.toString();


                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                tranf.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, variable1, variable2);
                            else
                                tranf.execute(variable1, variable2);
                        }

                    }

                }, 30000);
        }

        //codigo original
        /* protected void onPostExecute(String result) {

            //rescata la inspeccion que se estaba transmitiendo
            id_inspeccion = Integer.parseInt(result);

            //la cambia a estado 4
            db.cambiarEstadoInspeccion(id_inspeccion,4);
            //la borra de la base de datos porsiacaso
            db.deleteInspeccion(id_inspeccion);



            new Handler().postDelayed(new Runnable() {
                    public void run() {

                        transferirInspeccionCompleta tranf = new transferirInspeccionCompleta();

                        //Consultar las inspecciones en estado dos y tres
                        int insp_e2 = db.estadoInspecciones(2);
                        int insp_e3 = db.estadoInspecciones(3);

                        //preguntar por la primera inspeccion en estado 2 (por transmitir)
                        if (insp_e2 > 0 && insp_e3 == 0) {

                            //cambiar a estado para transmitir
                            db.cambiarEstadoInspeccion(insp_e2, 3);


                            JSONArray jsonArray = new JSONArray();
                            String accesorios[][] = db.listaAccesoriosParaEnviar(insp_e2);
                            for (int i = 0; i < accesorios.length; i++) {
                                try {
                                    JSONObject valores = new JSONObject();
                                    valores.put("idcampo", accesorios[i][0]);
                                    valores.put("valor", accesorios[i][1]);
                                    jsonArray.put(valores);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            //paso los datos a variables
                            String variable1 = String.valueOf(insp_e2);
                            String variable2 = jsonArray.toString();

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                tranf.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, variable1, variable2);
                            else
                                tranf.execute(variable1, variable2);

                        } else if (insp_e2 == 0 && insp_e3 == 0) {
                            //si no hay transmisión faltante o en desarrollo cerrar servicio
                            onDestroy();
                        }

                    }

                }, 30000);
        }*/

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
}
