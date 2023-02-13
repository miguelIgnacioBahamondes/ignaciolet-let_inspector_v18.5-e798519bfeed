package com.letchile.let.Remoto;

import com.letchile.let.Remoto.Data.LoginResp;
import com.letchile.let.Remoto.Data.Resp_CambioRamo;
import com.letchile.let.Remoto.Data.Resp_FotoEnviada;
import com.letchile.let.Remoto.Data.Resp_versionapp;
import com.letchile.let.Remoto.Data.oiRangoHorario;
import com.letchile.let.Remoto.Data.resp_bd;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by LET-CHILE on 14-03-2018.
 */

public interface InterfacePost {


    //consultar para borrar BD
    @FormUrlEncoded
    @POST("cargamovil/gestDeleteBDApp")
    Call<resp_bd> getRespBD(@Field("usr") String usr, @Field("opcion") String opcion);


    //LOGIN
    @FormUrlEncoded
    @POST("login/verificaLogeo")
    Call<LoginResp> getAcceso(@Field("usr") String usr, @Field("pwd") String pwd);


    //CAMBIAR RAMO
    @FormUrlEncoded
    @POST("cargamovil/cambiaRamo")
    Call<Resp_CambioRamo> getRamo(@Field("id_inspeccion") String id_inspeccion, @Field("comboRamo") String ramo);


    //TRAER RANGO HORARIO
    @FormUrlEncoded
    @POST("cargamovil/oiRangoHorario")
    Call<oiRangoHorario> getRango(@Field("id_inspeccion")String id_inspeccion, @Field("usr") String usr);


    //ENVIAR FOTOS
    //@Headers("Content-Type: application/json")
    @FormUrlEncoded
    @POST("cargamovil/descargafotos64")
    //Call<Resp_FotoEnviada> getFotoEnv(@Body Env_FotoEnviada body);
    Call<Resp_FotoEnviada> getFotoEnv(@Field("id_inspeccion")String id_inspeccion,@Field("nombre_foto") String nombre_foto, @Field("archivo") String archivo,@Field("comentario") String comentario);
    /*Call<Resp_FotoEnviada> getFotoEnv(
            @Part("id_inspeccion")RequestBody id_inspeccion,
            @Part("nombre_foto")RequestBody nombre_foto,
            @Part("comentario")RequestBody comentario,
            @Part("archivo")RequestBody archivo);*/

    @FormUrlEncoded
    @POST("cargamovil/verificaVersionApp")
    Call<Resp_versionapp> getVersion(@Field("usr_inspector")String usr_inspector,@Field("version") String version);


}
