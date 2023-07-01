package com.letchile.let.Servicios;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.letchile.let.BD.DBprovider;

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
import java.util.Calendar;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class AgregarHito extends Service {

    DBprovider db;
    String usuario, id_inspecc="", comentarioX="", estConec;
    Boolean connec = false;
    Calendar c = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String strDate = sdf.format(c.getTime());

    public AgregarHito() {
        db = new DBprovider(this);
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        super.onStartCommand(intent,flags,startId);

        try {
            String id_inspeccion = (String) intent.getExtras().get("id_inspeccion");
            String comentario = (String) intent.getExtras().get("comentario");

            id_inspecc=id_inspeccion;
            comentarioX=comentario;

            connec = new ConexionInternet(this).isConnectingToInternet();
            usuario=db.obtenerUsuario();
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "LogInspector_"+usuario+".txt");

            if (connec) {// false o true
                try {
                    if (connec.toString().equals("true"))
                    {
                        estConec="Ok";
                    }

                    if (!file.exists()) {
                        file.mkdir();
                    }

                    OutputStreamWriter fout = null;

                    fout = new FileWriter(file.getAbsoluteFile(), true);
                    fout.write(id_inspecc+" | Respuesta conexión a internet... "+ estConec +" | "+ strDate);
                    fout.append("\n\r");
                    fout.close();
                } catch (Exception ex) {
                    Log.e("Error conec", "ex: " + ex);
                }

                transHitoOi transfer = new transHitoOi();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    transfer.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, id_inspecc.toString(), comentarioX);
                else
                    transfer.execute(id_inspecc.toString(), comentarioX);
            } else {
                try {
                    if (!file.exists()) {
                        file.mkdir();
                    }

                    OutputStreamWriter fout = null;

                    fout = new FileWriter(file.getAbsoluteFile(), true);
                    fout.write(id_inspecc+" | Sin conexión a internet... | "+usuario+ " | "+ strDate);
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public class transHitoOi extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            int estadoOI = db.estadoInspeccion(Integer.parseInt(id_inspecc));
            Log.e("Parametros agregarHito", "estado: "+String.valueOf(estadoOI));

            try {
                URL url = new URL("https://www.autoagenda.cl/movil/cargamovil/ingresaHitoActualizacion"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("id_inspeccion", id_inspecc);
                postDataParams.put("comentario", comentarioX);
                postDataParams.put("usr_inspector", usuario);

                Log.e("Parametros agregarHito", postDataParams.toString());

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

                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "LogInspector_" + usuario + ".txt");

                int responseCode = conn.getResponseCode();
                Log.e("Status servicio", String.valueOf(conn.getResponseCode()));

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    db.cambiarEstadoInspeccion(Integer.parseInt(id_inspecc),99);
                    estadoOI = db.estadoInspeccion(Integer.parseInt(id_inspecc));
                    Log.e("Parametros agregarHito", "estado HTTP OK: "+String.valueOf(estadoOI));

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
                        if (sb.toString().equals("Ok")) {
                            //graba log cambio estado de foto
                            try {
                                if (!file.exists()) {
                                    file.mkdir();
                                }

                                OutputStreamWriter fout = null;
                                fout = new FileWriter(file.getAbsoluteFile(), true);
                                fout.write(id_inspecc + " | Datos agregarHito enviado... " + sb.toString() + " | " + id_inspecc + " | " + strDate);
                                fout.append("\n\r");
                                fout.close();
                            } catch (Exception ex2) {
                                Log.e("Error", "agregarHito inspeccion ex2: " + ex2);
                            }
                        }
                    } catch (Exception e) {
                        //error en cambio de stado
                        Log.e("Error envío agregarHito", e.getMessage());
                        try {
                            if (!file.exists()) {
                                file.mkdir();
                            }

                            OutputStreamWriter fout = null;

                            fout = new FileWriter(file.getAbsoluteFile(), true);
                            fout.write(strings[0] + " | Error en envío de agregarHito  " + e.getMessage() + " | " + strings[1] + " | " + strDate);
                            fout.append("\n\r");
                            fout.close();
                        } catch (Exception ex3) {
                            Log.e("Error", "ex3: " + ex3);
                        }
                    }
                    return sb.toString();
                } else {
                    try {
                        if (!file.exists()) {
                            file.mkdir();
                        }

                        estadoOI = db.estadoInspeccion(Integer.parseInt(id_inspecc));
                        Log.e("Parametros agregarHito", "estado HTTP ERROR: "+String.valueOf(estadoOI));

                        OutputStreamWriter fout = null;
                        fout = new FileWriter(file.getAbsoluteFile(), true);
                        fout.write(strings[0] + " | Error conexión al servicio agregarHito... " + responseCode + " | " + strings[1] + " | " + strDate);
                        fout.append("\n\r");
                        fout.close();
                    } catch (Exception ex4) {
                        Log.e("Error", "servicio agregarHito ex4: " + ex4);
                    }
                }
            } catch (Exception e) {
                try {
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "LogInspector_" + usuario + ".txt");

                    if (!file.exists()) {
                        file.mkdir();
                    }

                    OutputStreamWriter fout = null;

                    fout = new FileWriter(file.getAbsoluteFile(), true);
                    fout.write(strings[0] + " | Error en servicio agregarHito " + e.getMessage() + " | " + strings[1] + " | " + strDate);
                    fout.append("\n\r");
                    fout.close();
                } catch (Exception ex5) {
                    Log.e("Error", "agregarHito ex5: " + ex5);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            onDestroy();
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