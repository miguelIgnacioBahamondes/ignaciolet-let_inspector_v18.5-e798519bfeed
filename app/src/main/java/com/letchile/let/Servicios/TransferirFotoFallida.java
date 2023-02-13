package com.letchile.let.Servicios;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

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

/**
 * Created by LETCHILE on 07/03/2018.
 */

public class TransferirFotoFallida extends Service{
    DBprovider db;
    String para1,para2,para3,para4,para5,para6,id_inspecc="",usuario="", estConec="";
    Boolean connect = false;

    Calendar c = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String strDate = sdf.format(c.getTime());


    public TransferirFotoFallida(){
        db = new DBprovider(this);
    }

    public int onStartCommand(Intent intent,int flags,int startId){

        String id_inspeccion = (String) intent.getExtras().get("id_inspeccion");
        String nombreFoto = (String) intent.getExtras().get("nombreFoto");

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(c.getTime());

        id_inspecc=id_inspeccion;


        String datosFotos[][] = db.DatosFotosFallida(Integer.parseInt(id_inspeccion),nombreFoto);
        para1 = datosFotos[0][0].toString();
        para2 = datosFotos[0][1].toString();
        para3 = datosFotos[0][2].toString();
        para4 = datosFotos[0][3].toString();
        para5 = datosFotos[0][4].toString();
        para6 = datosFotos[0][5].toString();

        connect = new ConexionInternet(this).isConnectingToInternet();
        usuario=db.obtenerUsuario();

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "LogInspector_"+usuario+".txt");

        if(connect){
                //Toast.makeText(this,"Transferencia iniciada"+ para5.toString() ,Toast.LENGTH_SHORT).show();


            try {

                if (connect.toString().equals("true"))
                {
                    estConec="Ok";
                }

                if (!file.exists()) {
                    file.mkdir();
                }

                OutputStreamWriter fout = null;

                fout = new FileWriter(file.getAbsoluteFile(), true);
                fout.write(para1+" | Respuesta conexión a internet(foto fallida)... "+ estConec +" | "+ strDate);
                fout.append("\n\r");
                fout.close();

            } catch (Exception ex) {
                Log.e("Error conexión", "ex: " + ex );

            }

            transferirBackgroundFallida transfer = new transferirBackgroundFallida();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                transfer.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, para1.toString(), para2.toString(), para3.toString(), para4.toString(),para5.toString(),para6.toString());
            else
                transfer.execute(para1.toString(), para2.toString(), para3.toString(), para4.toString(),para5.toString(),para6.toString());

        }else{
            db.cambiarEstadoFotoFallida(Integer.parseInt(id_inspeccion),para2.toString(),1);
            try {


                if (!file.exists()) {
                    file.mkdir();
                }

                OutputStreamWriter fout = null;

                fout = new FileWriter(file.getAbsoluteFile(), true);
                fout.write(para1+" | Sin conexión a internet (oi fallida)... | "+datosFotos[0][1]+" | "+ strDate);
                fout.append("\n\r");
                fout.close();

            } catch (Exception ex1) {
                Log.e("Error conec", "ex1: " + ex1);

            }
            onDestroy();
            //cambiaEstado
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
        //Toast.makeText(this,"Tranmisión finalizada",Toast.LENGTH_SHORT).show();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public class transferirBackgroundFallida extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {

            usuario=db.obtenerUsuario();
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "LogInspector_"+usuario+".txt");

            try {
                URL url = new URL("https://www.autoagenda.cl/movil/cargamovil/descargaFotosFallida64"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("id_inspeccion", strings[0]);
                postDataParams.put("nombre_foto", strings[1]);
                postDataParams.put("comentFallida", strings[2]);
                postDataParams.put("fechaHoraFallida", strings[4]);
                postDataParams.put("usr", db.obtenerUsuario());
                postDataParams.put("motivoFallida", strings[5]);
                postDataParams.put("archivo", strings[3]);
                Log.e("Parametros a pasar foto", postDataParams.toString());
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


                    //CAMBIAR A ESTADO TRANSMITIDA
                    try {
                        if (sb.toString().equals("Ok")){
                            db.cambiarEstadoFotoFallida(Integer.parseInt(strings[0]),strings[1],2);
                            try {

                                if (!file.exists()) {
                                    file.mkdir();
                                }

                                OutputStreamWriter fout = null;
                                fout = new FileWriter(file.getAbsoluteFile(), true);
                                fout.write(strings[0]+" | Foto fallida  enviada... "+sb.toString()+" | "+ strings[1]+" | "+ strDate);
                                fout.append("\n\r");
                                fout.close();

                            } catch (Exception ex2) {
                                Log.e("Error", "ex2: " + ex2);

                            }
                        }
                    }catch (Exception ex3){
                        Log.e("Error cambio de estado", "" + ex3);

                        try {

                            if (!file.exists()) {
                                file.mkdir();
                            }

                            OutputStreamWriter fout = null;

                            fout = new FileWriter(file.getAbsoluteFile(), true);
                            fout.write(strings[0]+ " | Error en envío de foto fallida  "+ ex3+" | "+strings[1]+ " | "+strDate );
                            fout.append("\n\r");
                            fout.close();

                        } catch (Exception ex4) {
                            Log.e("Error", "ex4: " + ex4);

                        }
                    }
                    return sb.toString();

                } else {
                    //return new String("false : " + responseCode);
                    try {

                        if (!file.exists()) {
                            file.mkdir();
                        }

                        OutputStreamWriter fout = null;
                        fout = new FileWriter(file.getAbsoluteFile(), true);
                        fout.write(strings[0]+ " | Error conexión al servicio foto fallida... "+ responseCode +" | "+strings[1]+ " | "+strDate );
                        fout.append("\n\r");
                        fout.close();

                    } catch (Exception ex5) {
                        Log.e("Error", "ex5: " + ex5);

                    }

                }
            }
            catch(Exception e){
                //return new String("Exception: " + e.getMessage());
                try {

                    if (!file.exists()) {
                        file.mkdir();
                    }

                    OutputStreamWriter fout = null;
                    fout = new FileWriter(file.getAbsoluteFile(), true);
                    fout.write(strings[0]+ " | Error en servicio foto fallida... "+ e.getMessage() +" | "+strings[1]+ " | "+strDate );
                    fout.append("\n\r");
                    fout.close();

                } catch (Exception ex6) {
                    Log.e("Error", "ex6: " + ex6);

                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            File file =new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "LogInspector_"+usuario+".txt");

            if (result.equals("Ok"))
            {

                try {

                    if (!file.exists()) {
                        file.mkdir();
                    }

                    // Log.e("Resultado  dddd" + coment + id_inspecc,result);

                    OutputStreamWriter fout = null;
                    fout = new FileWriter(file.getAbsoluteFile(), true);
                    fout.write(id_inspecc+ " | Resultado Transmisión foto fallida... " +result+ " | "+strDate );
                    fout.append("\n\r");
                    fout.close();

                } catch (Exception ex7) {
                    Log.e("Error", "ex7: " + ex7);

                }
                onDestroy();
            }
            else {

                //Log.e("Resultado xxxx",result);
                try {

                    if (!file.exists()) {
                        file.mkdir();
                    }

                    OutputStreamWriter fout = null;

                    fout = new FileWriter(file.getAbsoluteFile(), true);
                    fout.write(id_inspecc + "| Error en transmisión " + result + "|" + strDate);
                    fout.append("\n\r");
                    fout.close();

                } catch (Exception ex8) {
                    Log.e("Error", "ex8: " + ex8);

                }

               // Log.e("Resultado Transmisión", result);
                onDestroy();
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
}
