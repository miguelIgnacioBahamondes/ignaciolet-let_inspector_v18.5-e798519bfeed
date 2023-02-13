package com.letchile.let.VehLiviano;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.Clases.PropiedadesTexto;
import com.letchile.let.InsPendientesActivity;
import com.letchile.let.R;
import com.letchile.let.VehLiviano.seccion2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;


public class TechoActivity extends AppCompatActivity {

    DBprovider db;

    EditText maParrilla,maBarras,maPorta, maPortaSki;
    String cParrilla="",cBarras="",cportaE="",cPortaSk="";
    CheckBox checkParrilla,checkBarras,checkPortaE,checkPortaSk;
    JSONObject llenado;
    String id_inspeccion;

    public TechoActivity() {db = new DBprovider(this);}

    JSONObject obj = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_techo);


        Bundle bundle = getIntent().getExtras();
        id_inspeccion=bundle.getString("id_inspeccion");

        //marca parrilla
        maParrilla = findViewById(R.id.maParrilla);
        maParrilla.setOnEditorActionListener(new PropiedadesTexto());
        maParrilla.setText(db.accesorio(Integer.parseInt(id_inspeccion),309).toString());

        //marca barras porta equipaje
        maBarras = findViewById(R.id.maBarras);
        maBarras.setOnEditorActionListener(new PropiedadesTexto());
        maBarras.setText(db.accesorio(Integer.parseInt(id_inspeccion),773).toString());

        //marca porta equipaje
        maPorta = findViewById(R.id.maPorta);
        maPorta.setOnEditorActionListener(new PropiedadesTexto());
        maPorta.setText(db.accesorio(Integer.parseInt(id_inspeccion),772).toString());

        //porta ski
        maPortaSki = findViewById(R.id.maPortaSki);
        maPortaSki.setOnEditorActionListener(new PropiedadesTexto());
        maPortaSki.setText(db.accesorio(Integer.parseInt(id_inspeccion),771).toString());


       //check parrilla
        checkParrilla = findViewById(R.id.cParrilla);
        if(db.accesorio(Integer.parseInt(id_inspeccion),293).toString().equals("Ok"))
        {
            checkParrilla.setChecked(true);
            cParrilla = "Ok";
        }else{
            checkParrilla.setChecked(false);
            cParrilla = "";
        }
        checkParrilla.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    cParrilla = "Ok";
                }else{
                    cParrilla = "";
                }
            }
        });


        //check Barras
        checkBarras = findViewById(R.id.cBarras);
        if(db.accesorio(Integer.parseInt(id_inspeccion),325).toString().equals("Ok"))
        {
            checkBarras.setChecked(true);
            cBarras = "Ok";
        }else{
            checkBarras.setChecked(false);
            cBarras = "";
        }
        checkBarras.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    cBarras = "Ok";
                }else{
                    cBarras = "";
                }

            }
        });

        //check porta equipaje
        checkPortaE = findViewById(R.id.cportaE);
        if(db.accesorio(Integer.parseInt(id_inspeccion),355).toString().equals("Ok"))
        {
            checkPortaE.setChecked(true);
            cportaE = "Ok";
        }else{
            checkPortaE.setChecked(false);
            cportaE = "";
        }
        checkPortaE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    cportaE = "Ok";
                }else{
                    cportaE = "";
                }

            }
        });

        //check porta ski
        checkPortaSk = findViewById(R.id.cPortaSk);
        if(db.accesorio(Integer.parseInt(id_inspeccion),327).toString().equals("Ok"))
        {
            checkPortaSk.setChecked(true);
            cPortaSk = "Ok";
        }else{
            checkPortaSk.setChecked(false);
            cPortaSk = "";
        }
        checkPortaSk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    cPortaSk = "Ok";
                }else{
                    cPortaSk = "";
                }

            }
        });


        //Botón guardar siguiente de sección Techo
        final Button btnSigTecJg = findViewById(R.id.btnSigTecJg);
        btnSigTecJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                guardarDatos();
                Intent intent = new Intent( TechoActivity.this, DatosInspActivity.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
            }
        });

        //Botón volver pendiente
        final Button btnPenTecJg = findViewById(R.id.btnPenTecJg);
        btnPenTecJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                guardarDatos();
                Intent intent = new Intent( TechoActivity.this, seccion2.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
            }
        });

        //Botón volver de sección
        final Button btnVolverTecJg = findViewById(R.id.btnVolverTecJg);
        btnVolverTecJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                guardarDatos();
                //Intent intent = new Intent( TechoActivity.this, AudioActivity.class);
                Intent intent = new Intent( TechoActivity.this, seccion2.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
            }
        });
    }

    public void guardarDatos(){
        try{


            JSONObject valor75 = new JSONObject();
            valor75.put("valor_id",309);
            valor75.put("texto",maParrilla.getText().toString());

            JSONObject valor76 = new JSONObject();
            valor76.put("valor_id",773);
            valor76.put("texto",maBarras.getText().toString());

            JSONObject valor77 = new JSONObject();
            valor77.put("valor_id",772);
            valor77.put("texto",maPorta.getText().toString());

            JSONObject valor78 = new JSONObject();
            valor78.put("valor_id",771);
            valor78.put("texto",maPortaSki.getText().toString());

            JSONObject valor81 = new JSONObject();
            valor81.put("valor_id",293);
            valor81.put("texto",cParrilla);

            JSONObject valor82 = new JSONObject();
            valor82.put("valor_id",325);
            valor82.put("texto",cBarras);

            JSONObject valor83 = new JSONObject();
            valor83.put("valor_id",355);
            valor83.put("texto",cportaE);

            JSONObject valor84 = new JSONObject();
            valor84.put("valor_id",327);
            valor84.put("texto",cPortaSk);

            JSONArray jsonArray = new JSONArray();
            jsonArray.put(valor75);
            jsonArray.put(valor76);
            jsonArray.put(valor77);
            jsonArray.put(valor78);
            jsonArray.put(valor81);
            jsonArray.put(valor82);
            jsonArray.put(valor83);
            jsonArray.put(valor84);


            if (!jsonArray.isNull(0)) {
                for(int i=0;i<jsonArray.length();i++){
                    llenado = new JSONObject(jsonArray.getString(i));
                    db.insertarValor(Integer.parseInt(id_inspeccion),llenado.getInt("valor_id"),llenado.getString("texto"));
                }
            }

        }catch (Exception e)
        {

            Toast.makeText(TechoActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
        }
    }
}
