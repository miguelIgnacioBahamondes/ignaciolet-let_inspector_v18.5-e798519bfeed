package com.letchile.let.Clases;

import android.content.Context;

import com.letchile.let.BD.DBprovider;

/**
 * Created by LETCHILE on 27/02/2018.
 */

public class Validaciones {

    DBprovider db;

    public Validaciones(Context context){
        db = new DBprovider(context);
    }


    public boolean estadoCheck(int id_inspeccion, int id_campo){
        boolean ver = false;

        if(db.accesorio(id_inspeccion,id_campo).equals("Ok")){
            ver = true;
        }else {
            ver = false;
        }
        return  ver;
    }



    /*public void insertarDatos(int id_inspeccion,int valor_id,String texto){
        //db.insertarValor(Integer.parseInt(id_inspeccion),llenado.getInt("valor_id"),llenado.getString("texto"));
        if(texto.equals("") || texto.isEmpty()){
            db.insertarValor(id_inspeccion,valor_id,texto,2);
        }else{
            db.insertarValor(id_inspeccion,valor_id,texto,1);
        }
    }*/


}
