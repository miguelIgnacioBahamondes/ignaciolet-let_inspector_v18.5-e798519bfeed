package com.letchile.let.Remoto.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by LET-CHILE on 20-03-2018.
 */

public class oiRangoHorario {

    @SerializedName("id_inspeccion")
    @Expose
    private String idInspeccion;
    @SerializedName("fechaR")
    @Expose
    private String fechaR;
    @SerializedName("horaIR")
    @Expose
    private String horaIR;
    @SerializedName("horaFR")
    @Expose
    private String horaFR;

    public String getIdInspeccion() {
        return idInspeccion;
    }

    public void setIdInspeccion(String idInspeccion) {
        this.idInspeccion = idInspeccion;
    }

    public String getFechaR() {
        return fechaR;
    }

    public void setFechaR(String fechaR) {
        this.fechaR = fechaR;
    }

    public String getHoraIR() {
        return horaIR;
    }

    public void setHoraIR(String horaIR) {
        this.horaIR = horaIR;
    }

    public String getHoraFR() {
        return horaFR;
    }

    public void setHoraFR(String horaFR) {
        this.horaFR = horaFR;
    }
}
