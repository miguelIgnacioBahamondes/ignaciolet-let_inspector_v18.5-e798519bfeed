package com.letchile.let.Remoto.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Resp_FotoEnviada{

        @SerializedName("MSJ")
        @Expose
        private String MSJ;

        public String getMSJ() {
            return MSJ;
        }

}
