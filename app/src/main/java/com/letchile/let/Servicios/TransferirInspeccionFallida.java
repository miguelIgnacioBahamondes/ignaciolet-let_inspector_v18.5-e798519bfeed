package com.letchile.let.Servicios;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
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
 * Created by LET-CHILE on 09-03-2018.
 */

public class TransferirInspeccionFallida extends Service {

    DBprovider db;
    int id_inspeccion = 0;
    boolean conn = false;
    private NotificationManager notificationManager;
    private static final int ID_NOTIFICACION = 4321;
    String usuario , id_inspecc="", coment="", estConec;
    Calendar c = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String strDate = sdf.format(c.getTime());


    public TransferirInspeccionFallida(){
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

        conn = new ConexionInternet(this).isConnectingToInternet();
        usuario=db.obtenerUsuario();
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "LogInspector_"+usuario+".txt");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getBaseContext())
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("LET CHILE SPA")
                .setContentText("Servicio de transferencia fallida iniciada")
                .setWhen(System.currentTimeMillis());

        String id_inspeccion = (String) intent.getExtras().get("id_inspeccion");

        notificationManager.notify(ID_NOTIFICACION,builder.build());

        if(conn){

            transferirInsFallida transfer = new transferirInsFallida();

            try {

               /* if (conn.toString().equals("true"))
                {
                    estConec="Ok";
                }*/


                if (!file.exists()) {
                    file.mkdir();
                }

                OutputStreamWriter fout = null;

                fout = new FileWriter(file.getAbsoluteFile(), true);
                fout.write(id_inspeccion +" | Respuesta conexión a internet (OI fallida)... Ok | "+ strDate );
                fout.append("\n\r");
                fout.close();

            } catch (Exception ex1) {
                Log.e("Error conec", "ex1: " + ex1);

            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                transfer.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,id_inspeccion);

            else
                transfer.execute(id_inspeccion);

        }else{
            Toast.makeText(this,"No cuenta con internet para transmitir",Toast.LENGTH_SHORT).show();
            try {

               /* if (conn.equals("true"))
                {
                    estConec="Ok";
                }*/


                if (!file.exists()) {
                    file.mkdir();
                }

                OutputStreamWriter fout = null;

                fout = new FileWriter(file.getAbsoluteFile(), true);
                fout.write(id_inspeccion +" | No cuenta con internet para transmitir... "+ conn +" | "+ strDate);
                fout.append("\n\r");
                fout.close();

            } catch (Exception ex2) {
                Log.e("Error conec", "ex2: " + ex2);

            }

            onDestroy();
        }

        return START_STICKY;
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class transferirInsFallida extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {


            //ENVIA FOTOS
            String fotos[][] = db.ListaDatosFotosFallida(Integer.parseInt(strings[0]));
            usuario=db.obtenerUsuario();
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "LogInspector_"+usuario+".txt");

            for(int i = 0;i < fotos.length; i++){
                try{


                    URL url = new URL("https://www.autoagenda.cl/movil/cargamovil/descargaFotosFallida64"); // here is your URL path

                    Log.i("pase por fallida","fallida");

                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("id_inspeccion", fotos[i][0]);
                    postDataParams.put("nombre_foto", fotos[i][1]);
                    postDataParams.put("archivo", fotos[i][3]);
                    postDataParams.put("comentFallida", fotos[i][2]);
                    postDataParams.put("fechaHoraFallida", fotos[i][4]);
                    postDataParams.put("motivoFallida", fotos[i][5]);
                    postDataParams.put("usr", db.obtenerUsuario());
                    Log.e("Param oi fallida", postDataParams.toString());
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
                    Log.i("asd","asad");

                    try {

                        if (!file.exists()) {
                            file.mkdir();
                        }

                        OutputStreamWriter fout = null;
                        fout = new FileWriter(file.getAbsoluteFile(), true);
                        fout.write(fotos[i][0]+" | Datos inspección fallida a transferir... |"+fotos[i][1]+" | " + strDate);
                        fout.append("\n\r");
                        fout.close();

                    } catch (Exception ex3) {
                        Log.e("Error", "ex3: " + ex3);

                    }



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
                            if (sb.toString().equals("Ok")){
                                //i++;
                                //eliminar
                                db.cambiarEstadoFotoFallida(Integer.parseInt(strings[0]),strings[1],2);
                                try {

                                    if (!file.exists()) {
                                        file.mkdir();
                                    }

                                    OutputStreamWriter fout = null;
                                    fout = new FileWriter(file.getAbsoluteFile(), true);
                                    fout.write(fotos[i][0]+" | Inspección fallida enviada... "+sb.toString()+" | "+ fotos[i][1]+" | "+ strDate);
                                    fout.append("\n\r");
                                    fout.close();

                                } catch (Exception ex3) {
                                    Log.e("Error", "ex3: " + ex3);

                                }
                            }
                        }catch (Exception e){
                            Log.e("Error transmision",e.getMessage());

                            try {

                                if (!file.exists()) {
                                    file.mkdir();
                                }

                                OutputStreamWriter fout = null;
                                fout = new FileWriter(file.getAbsoluteFile(), true);
                                fout.write(fotos[i][0]+" | Error en transmisión insp fallida... |"+ strDate);
                                fout.append("\n\r");
                                fout.close();

                            } catch (Exception ex5) {
                                Log.e("Error", "ex5: " + ex5);

                            }


                        }
                    }else{

                        try {

                            if (!file.exists()) {
                                file.mkdir();
                            }

                            OutputStreamWriter fout = null;
                            fout = new FileWriter(file.getAbsoluteFile(), true);
                            fout.write(fotos[i][0]+" | Sin internet insp fallida... "+ strDate);
                            fout.append("\n\r");
                            fout.close();

                        } catch (Exception ex6) {
                            Log.e("Error", "ex6: " + ex6);

                        }
                        onDestroy(); // sin internet o otro error
                        //ARREGLAR BUG
                        i=90;
                    }

                }catch (Exception ex7){
                    Log.e("Error","" + ex7);
                    try {

                        if (!file.exists()) {
                            file.mkdir();
                        }

                        OutputStreamWriter fout = null;
                        fout = new FileWriter(file.getAbsoluteFile(), true);
                        fout.write(fotos[i][0]+" | Error en servicio insp fallida... "+ex7+" | "+ strDate);
                        fout.append("\n\r");
                        fout.close();

                    } catch (Exception ex8) {
                        Log.e("Error", "ex8: " + ex8);

                    }

                }
            }
            return strings[0];
        }

        @Override
        protected void onPostExecute(String result) {

            int id_inspeccion = Integer.parseInt(result);

            File file =new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "LogInspector_"+usuario+".txt");


                try {

                    if (!file.exists()) {
                        file.mkdir();
                    }

                    // Log.e("Resultado  dddd" + coment + id_inspecc,result);

                    OutputStreamWriter fout = null;
                    fout = new FileWriter(file.getAbsoluteFile(), true);
                    fout.write(result+ " | Resultado Transmisión insp fallida... Ok | "+ strDate );
                    fout.append("\n\r");
                    fout.close();

                } catch (Exception ex9) {
                    Log.e("Error insp fallida", "ex9: " + ex9);

                }


            //preguntar estado foto e inspeccion
            db.deleteInspeccionFallida(id_inspeccion);

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        notificationManager.cancel(ID_NOTIFICACION);
    }

}
