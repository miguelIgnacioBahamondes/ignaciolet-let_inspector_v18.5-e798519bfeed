package com.letchile.let.Servicios;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.letchile.let.BD.DBprovider;
import com.letchile.let.R;
import com.letchile.let.Remoto.Data.Env_FotoEnviada;
import com.letchile.let.Remoto.Data.Resp_FotoEnviada;
import com.letchile.let.Remoto.InterfacePost;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Iterator;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TransferirFotoV2 extends Service {

    DBprovider db;
    Context context = this;
    String para1,para2,para3,para4;
    Boolean connec = false;

    public TransferirFotoV2(){
        db = new DBprovider(context);
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){


                    try {
                        String id_inspeccion = (String) intent.getExtras().get("id_inspeccion");
                        String comentario = (String) intent.getExtras().get("comentario");
                        //Trae datos de la foto
                        String datosFotos[][] = db.DatosFotos(Integer.parseInt(id_inspeccion), comentario);
                        para1 = datosFotos[0][0].toString(); //id_inspeccion
                        para2 = datosFotos[0][1].toString(); //nombre_foto
                        para3 = datosFotos[0][2].toString(); //comentario
                        para4 = datosFotos[0][3].toString(); //archivo

                        Gson gson = new GsonBuilder()
                                .setLenient()
                                .create();

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(getString(R.string.URL_BASE))
                                .addConverterFactory(GsonConverterFactory.create(gson))
                                .build();
                        final InterfacePost servicio = retrofit.create(InterfacePost.class);

                        connec = new ConexionInternet(this).isConnectingToInternet();

                        if (connec) {
                            //Toast.makeText(this, "Transferencia iniciada", Toast.LENGTH_SHORT).show();

                    final Call<Resp_FotoEnviada> respFotoEnviada = servicio.getFotoEnv(para1,para2,para4,para3);

                    respFotoEnviada.enqueue(new Callback<Resp_FotoEnviada>() {
                        @Override
                        public void onResponse(Call<Resp_FotoEnviada> call, Response<Resp_FotoEnviada> response) {
                            int statusCode = response.code();
                            Resp_FotoEnviada respFto = response.body();
                            Log.d("Foto", "onResponse " + statusCode);

                            if(respFto.getMSJ().equals("Ok")){
                                db.cambiarEstadoFoto(Integer.parseInt(para1.toString()), para2.toString(), para3.toString(), 2);
                                Toast.makeText(context,"Tranmisión finalizada",Toast.LENGTH_SHORT).show();
                                onDestroy();
                            }else{
                                Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show();
                                onDestroy();
                            }
                        }

                        @Override
                        public void onFailure(Call<Resp_FotoEnviada> call, Throwable t) {
                            db.cambiarEstadoFoto(Integer.parseInt(para1.toString()), para2.toString(), para3.toString(), 1);
                            t.getStackTrace();
                            Log.e("Error",t.getMessage());
                            Toast.makeText(context,"Error en la transmisión",Toast.LENGTH_SHORT).show();
                            onDestroy();
                        }
                    });



            } else {
                //CAMBIAR A ESTADO POR TRANSMITIR
                db.cambiarEstadoFoto(Integer.parseInt(id_inspeccion), para2.toString(), para3.toString(), 1);
                onDestroy();
            }
        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }


        return START_STICKY;
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
        Toast.makeText(this,"Tranmisión finalizada",Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
