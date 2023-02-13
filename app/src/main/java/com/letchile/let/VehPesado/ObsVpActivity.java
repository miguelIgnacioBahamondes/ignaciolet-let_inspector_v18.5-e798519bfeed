package com.letchile.let.VehPesado;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.Clases.PropiedadesTexto;
import com.letchile.let.R;
import com.letchile.let.Servicios.ConexionInternet;
import com.letchile.let.VehLiviano.DatosAsegActivity;
import com.letchile.let.VehLiviano.DatosVehActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class ObsVpActivity extends AppCompatActivity {

    DBprovider db;

    EditText obs1,obs2,obs3;
    Boolean connec = false;
    JSONObject llenado;
    String id_inspeccion;

    public ObsVpActivity(){
        db = new DBprovider(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obs_vp);

        connec = new ConexionInternet(this).isConnectingToInternet();

        //Traspaso de datos
        Bundle bundle = getIntent().getExtras();
        id_inspeccion=bundle.getString("id_inspeccion");

        //OBSERVACION 1
        obs1 = findViewById(R.id.obs1);
        obs1.setOnEditorActionListener(new PropiedadesTexto());
        obs1.setText(db.accesorio(Integer.parseInt(id_inspeccion),730).toString());

        //OBSERVACION 2
        obs2 = findViewById(R.id.obs2);
        obs2.setOnEditorActionListener(new PropiedadesTexto());
        obs2.setText(db.accesorio(Integer.parseInt(id_inspeccion),731).toString());

        //OBSERVACION 3
        obs3 = findViewById(R.id.obs3);
        obs3.setOnEditorActionListener(new PropiedadesTexto());
        obs3.setText(db.accesorio(Integer.parseInt(id_inspeccion),732).toString());

        //BOTON GUARDAR Y SIGUIENTE
        final Button btnSigObsVpJg = findViewById(R.id.btnSigObsVpJg);
        btnSigObsVpJg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                guardarDatos();
                Intent intent = new Intent( ObsVpActivity.this, SeccionVpActivity.class);//IR A PAGINA SECCIONES
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
            }
        });


        final Button btnVolverObsVpJg = findViewById(R.id.btnVolverObsVpJg);
        btnVolverObsVpJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarDatos();
                Intent intent = new Intent( ObsVpActivity.this, SeccionVpActivity.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
            }
        });
    }

    public void guardarDatos(){
        try{

            JSONObject valor98 = new JSONObject();
            valor98.put("valor_id",730);
            valor98.put("texto",obs1.getText().toString());

            JSONObject valor99 = new JSONObject();
            valor99.put("valor_id",731);
            valor99.put("texto",obs2.getText().toString());

            JSONObject valor100 = new JSONObject();
            valor100.put("valor_id",732);
            valor100.put("texto",obs3.getText().toString());



            JSONArray jsonArray = new JSONArray();
            jsonArray.put(valor98);
            jsonArray.put(valor99);
            jsonArray.put(valor100);



            if (!jsonArray.isNull(0)) {
                for(int i=0;i<jsonArray.length();i++){
                    llenado = new JSONObject(jsonArray.getString(i));
                    db.insertarValor(Integer.parseInt(id_inspeccion),llenado.getInt("valor_id"),llenado.getString("texto"));
                }
            }

        }catch (Exception e)
        {

            Toast.makeText(ObsVpActivity.this, "", Toast.LENGTH_SHORT);
        }
    }
}
