package com.letchile.let.Servicios;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.R;
import com.letchile.let.LoginActivity;
import com.letchile.let.VehLiviano.Fotos.frontal;
import com.letchile.let.VehLiviano.Fotos.lateralderecho;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Timer;


import javax.net.ssl.HttpsURLConnection;

/**
 * Created by LETCHILE on 20/02/2018.
 */

public class TransferirGeolocalizacion extends Service {

    DBprovider db;
    String para1,para2,para3,para4,para5,para6, usuario, id_inspecc="", coment="", estConec;
    Boolean connec = false;
    int contador_foto=0;

    Calendar c = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String strDate = sdf.format(c.getTime());




    public TransferirGeolocalizacion(){
        db = new DBprovider(this);
    }


    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        super.onStartCommand(intent,flags,startId);

        try {
            String id_inspeccion = (String) intent.getExtras().get("id_inspeccion");


            id_inspecc=id_inspeccion;


            //Trae datos de la foto
            String datosGeoloc[][] = db.DatosGeoloc(Integer.parseInt(id_inspeccion));
            para1 = datosGeoloc[0][0].toString();
            para2 = datosGeoloc[0][1].toString();
            para3 = datosGeoloc[0][2].toString();
            para4 = datosGeoloc[0][3].toString();
            para5 = datosGeoloc[0][4].toString();
            para6 = datosGeoloc[0][5].toString();

            connec = new ConexionInternet(this).isConnectingToInternet();
            usuario=db.obtenerUsuario();
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "LogInspector_"+usuario+".txt");

            if (connec) {// false o true
                //Toast.makeText(this, "Transferencia geo iniciada" + connec , Toast.LENGTH_SHORT).show();

                try {

                    //sb.toString().equals("Ok")

                    if (connec.toString().equals("true"))
                    {
                        estConec="Ok";
                    }


                    if (!file.exists()) {
                        file.mkdir();
                    }

                    OutputStreamWriter fout = null;

                    fout = new FileWriter(file.getAbsoluteFile(), true);
                    fout.write(para1+" | Respuesta conexión a internet... "+ estConec +" | "+ strDate);
                    fout.append("\n\r");
                    fout.close();

                } catch (Exception ex) {
                    Log.e("Error conec", "ex: " + ex);

                }

                transferirbackground transfer = new transferirbackground();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    transfer.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, para1.toString(), para2.toString(), para3.toString(), para4.toString(),para5.toString(),para6.toString());
                else
                    transfer.execute(para1.toString(), para2.toString(), para3.toString(), para4.toString(),para5.toString(),para6.toString());
            } else {


                try {


                    if (!file.exists()) {
                        file.mkdir();
                    }

                    OutputStreamWriter fout = null;

                    fout = new FileWriter(file.getAbsoluteFile(), true);
                    fout.write(para1+" | Sin conexión a internet... | "+para2+ " | "+ strDate);
                    fout.append("\n\r");
                    fout.close();

                } catch (Exception ex1) {
                    Log.e("Error conec", "ex1: " + ex1);

                }

            }
        }catch (Exception e){
            Log.e("Errorx",e.getMessage());
        }

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        /*String id_inspeccion = (String) inten.getExtras().get("id_inspeccion");
        String id_foto = (String) inten.getExtras().get("id_foto");*/
        //id_foto = (String) intent.getExtras().get("id_foto");

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //Toast.makeText(this,"Tranmisión finalizada",Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public class transferirbackground extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {


            try {

                URL url = new URL("https://www.autoagenda.cl/movil/cargamovil/datosGeo"); // here is your URL path


                JSONObject postDataParams = new JSONObject();
                postDataParams.put("id_inspeccion", strings[0]);
                postDataParams.put("usr", strings[1]);
                postDataParams.put("fecha", strings[2]);
                postDataParams.put("latitud", strings[3]);
                postDataParams.put("longitud", strings[4]);
                postDataParams.put("direccion", strings[5]);
                Log.e("Parametros geo", postDataParams.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                Log.e("paseeeee", postDataParams.toString());
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                usuario=db.obtenerUsuario();
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "LogInspector_"+usuario+".txt");

                int responseCode = conn.getResponseCode();

               /* try {

                    if (!file.exists()) {
                        file.mkdir();
                    }

                    OutputStreamWriter fout = null;
                    fout = new FileWriter(file.getAbsoluteFile(), true);
                    fout.write(strings[0]+" | Datos de foto a transferir... "+postDataParams.toString()+" | "+ strDate);
                    fout.append("\n\r");
                    fout.close();

                } catch (Exception ex4) {
                    Log.e("Error", "ex4: " + ex4);

                }*/

                Log.e("Status servicio" , String.valueOf(conn.getResponseCode()));

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

                    //CAMBIAR A ESTADO TRANSMITIDA
                    try {
                        if (sb.toString().equals("Ok")){


                            //graba log cambio estado de foto
                            try {

                                if (!file.exists()) {
                                    file.mkdir();
                                }

                                OutputStreamWriter fout = null;
                                fout = new FileWriter(file.getAbsoluteFile(), true);
                                fout.write(strings[0]+" | Datos geo enviado... "+sb.toString()+" | "+ strings[1]+" | "+ strDate);
                                fout.append("\n\r");
                                fout.close();

                            } catch (Exception ex2) {
                                Log.e("Error", "ex2: " + ex2);

                            }

                        }
                    }catch (Exception e){

                        //error en cambio de stado
                        Log.e("Error en envío de foto ",e.getMessage());
                        try {

                            if (!file.exists()) {
                                file.mkdir();
                            }

                            OutputStreamWriter fout = null;

                            fout = new FileWriter(file.getAbsoluteFile(), true);
                            fout.write(strings[0]+ " | Error en envío de foto  "+ e.getMessage()+" | "+strings[1]+ " | "+strDate );
                            fout.append("\n\r");
                            fout.close();

                        } catch (Exception ex3) {
                            Log.e("Error", "ex3: " + ex3);

                        }

                    }
                    return sb.toString();

                } else {

                    //Log.i("xxxxxx","xxxxx" + responseCode);
                    // error en conexión con el servidor
                    //return new String("false : " + responseCode);

                    try {

                        if (!file.exists()) {
                            file.mkdir();
                        }

                        OutputStreamWriter fout = null;
                        fout = new FileWriter(file.getAbsoluteFile(), true);
                        fout.write(strings[0]+ " | Error conexión al servicio foto... "+ responseCode +" | "+strings[1]+ " | "+strDate );
                        fout.append("\n\r");
                        fout.close();

                    } catch (Exception ex4) {
                        Log.e("Error", "ex4: " + ex4);

                    }


                }
            }
            catch(Exception e){

                try {

                    File file =new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "LogInspector_"+usuario+".txt");

                    if (!file.exists()) {
                        file.mkdir();
                    }

                    OutputStreamWriter fout = null;

                    fout = new FileWriter(file.getAbsoluteFile(), true);
                    fout.write(strings[0]+ " | Error en servicio de geo  "+ e.getMessage() +" | "+ strings[1]+ " | "+strDate );
                    fout.append("\n\r");
                    fout.close();

                } catch (Exception ex5) {
                    Log.e("Error", "ex5: " + ex5);

                }


            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {


            Log.i("PASE POR ACAxxxx","por acaxxxxx");
            //onDestroy();

           /* File file =new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "LogInspector_"+usuario+".txt");

            if (result.equals("Ok"))
            {

                try {

                    if (!file.exists()) {
                        file.mkdir();
                    }

                    // Log.e("Resultado  dddd" + coment + id_inspecc,result);

                    OutputStreamWriter fout = null;
                    fout = new FileWriter(file.getAbsoluteFile(), true);
                    fout.write(id_inspecc+ " | Resultado Transmisión... " +result+" | "+ coment + " | "+strDate );
                    fout.append("\n\r");
                    fout.close();

                } catch (Exception ex6) {
                    Log.e("Error", "ex6: " + ex6);

                }
                onDestroy();
            }
            else
            {

                try {

                    if (!file.exists()) {
                        file.mkdir();
                    }

                    OutputStreamWriter fout = null;

                    fout = new FileWriter(file.getAbsoluteFile(), true);
                    fout.write(id_inspecc+ "| Error en transmisión "+ result +" | "+coment+ "|"+strDate );
                    fout.append("\n\r");
                    fout.close();

                } catch (Exception ex7) {
                    Log.e("Error", "ex7: " + ex7);

                }
                onDestroy();

            }*/


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
}
