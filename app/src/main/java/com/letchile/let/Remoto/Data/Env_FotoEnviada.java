package com.letchile.let.Remoto.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Env_FotoEnviada {

    @SerializedName("id_inspeccion")
    @Expose
    private String idInspeccion;
    @SerializedName("nombre_foto")
    @Expose
    private String nombreFoto;
    @SerializedName("comentario")
    @Expose
    private String comentario;
    @SerializedName("archivo")
    @Expose
    private String archivo;

    public Env_FotoEnviada(String id_inspeccion,String nombre_foto,String comentario, String archiv){
        this.idInspeccion = id_inspeccion;
        this.nombreFoto = nombre_foto;
        this.comentario = comentario;
        this.archivo = archiv;
    }

    public String getIdInspeccion() {
        return idInspeccion;
    }

    public void setIdInspeccion(String idInspeccion) {
        this.idInspeccion = idInspeccion;
    }

    public String getNombreFoto() {
        return nombreFoto;
    }

    public void setNombreFoto(String nombreFoto) {
        this.nombreFoto = nombreFoto;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

}