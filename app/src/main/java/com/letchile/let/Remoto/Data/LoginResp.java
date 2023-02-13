package com.letchile.let.Remoto.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by LET-CHILE on 14-03-2018.
 */

public class LoginResp {

    @SerializedName("MSJ")
    @Expose
    private String MSJ;

    public String getMSJ() {
        return MSJ;
    }



}
