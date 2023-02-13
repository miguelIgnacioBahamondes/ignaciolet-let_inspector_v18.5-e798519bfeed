package com.letchile.let.VehLiviano;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.Clases.PropiedadesTexto;
import com.letchile.let.InsPendientesActivity;
import com.letchile.let.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class NeuActivity extends AppCompatActivity {

    DBprovider db;

    EditText cantNeu,maNeu,moNeu,meNeu,cantNeu2,maNeu2,moNeu2,meNeu2,cantFierro,cantCromadas,cantAlea,maLlanta;
    Spinner estadoNeu, estadoLlanta;
    String id_inspeccion;

    public NeuActivity() {db = new DBprovider(this);}

    JSONObject obj = new JSONObject();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neu);

        Bundle bundle = getIntent().getExtras();
        id_inspeccion=bundle.getString("id_inspeccion");

        //cantidad de neumaticos
        cantNeu = findViewById(R.id.cantNeu);
        cantNeu.setOnEditorActionListener(new PropiedadesTexto());
        cantNeu.setText(db.accesorio(Integer.parseInt(id_inspeccion),188).toString());

        //marca de neumaticos
        maNeu = findViewById(R.id.maNeu);
        maNeu.setOnEditorActionListener(new PropiedadesTexto());
        maNeu.setText(db.accesorio(Integer.parseInt(id_inspeccion),189).toString());

        //modelo de neumaticos
        moNeu = findViewById(R.id.moNeu);
        moNeu.setOnEditorActionListener(new PropiedadesTexto());
        moNeu.setText(db.accesorio(Integer.parseInt(id_inspeccion),190).toString());

        //medida de neumaticos
        meNeu = findViewById(R.id.meNeu);
        meNeu.setOnEditorActionListener(new PropiedadesTexto());
        meNeu.setText(db.accesorio(Integer.parseInt(id_inspeccion),191).toString());

        //cantidad de neumaticos 2
        cantNeu2 = findViewById(R.id.cantNeu2);
        cantNeu2.setOnEditorActionListener(new PropiedadesTexto());
        cantNeu2.setText(db.accesorio(Integer.parseInt(id_inspeccion),193).toString());

        //marca de neumaticos 2
        maNeu2 = findViewById(R.id.maNeu2);
        maNeu2.setOnEditorActionListener(new PropiedadesTexto());
        maNeu2.setText(db.accesorio(Integer.parseInt(id_inspeccion),194).toString());

        //modelo de neumaticos 2
        moNeu2 = findViewById(R.id.moNeu2);
        moNeu2.setOnEditorActionListener(new PropiedadesTexto());
        moNeu2.setText(db.accesorio(Integer.parseInt(id_inspeccion),195).toString());

        //medida de neumaticos 2
        meNeu2 = findViewById(R.id.meNeu2);
        meNeu2.setOnEditorActionListener(new PropiedadesTexto());
        meNeu2.setText(db.accesorio(Integer.parseInt(id_inspeccion),196).toString());

        //cantidad fierro
        cantFierro = findViewById(R.id.cantFierro);
        cantFierro.setOnEditorActionListener(new PropiedadesTexto());
        cantFierro.setText(db.accesorio(Integer.parseInt(id_inspeccion),207).toString());

        //cantidad cromada
        cantCromadas = findViewById(R.id.cantCromadas);
        cantCromadas.setOnEditorActionListener(new PropiedadesTexto());
        cantCromadas.setText(db.accesorio(Integer.parseInt(id_inspeccion),774).toString());

        //cantidad aleacion
        cantAlea = findViewById(R.id.cantAlea);
        cantAlea.setOnEditorActionListener(new PropiedadesTexto());
        cantAlea.setText(db.accesorio(Integer.parseInt(id_inspeccion),775).toString());

        //marca llanta
        maLlanta = findViewById(R.id.maLLanta);
        maLlanta.setOnEditorActionListener(new PropiedadesTexto());
        maLlanta.setText(db.accesorio(Integer.parseInt(id_inspeccion),208).toString());

        // cargar un combo neumáticos
        estadoNeu = findViewById(R.id.spinNeu);
        String[] arrayEstado = getResources().getStringArray(R.array.estado_neumatico);
        final List<String> arrayEstadoList = Arrays.asList(arrayEstado);
        ArrayAdapter<String> spinner_adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayEstadoList);
        spinner_adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estadoNeu.setAdapter(spinner_adapter1);
        estadoNeu.setSelection(arrayEstadoList.lastIndexOf(db.accesorio(Integer.parseInt(id_inspeccion),206).toString()));


        // cargar un combo estado llantas
        estadoLlanta = findViewById(R.id.spinLlanta);
        String[] arrayEstado2 = getResources().getStringArray(R.array.estado_llanta);
        final List<String> arrayEstadoList2 = Arrays.asList(arrayEstado2);
        ArrayAdapter<String> spinner_adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayEstadoList2);
        estadoLlanta.setAdapter(spinner_adapter2);
        estadoLlanta.setSelection(arrayEstadoList2.lastIndexOf(db.accesorio(Integer.parseInt(id_inspeccion),776).toString()));






        //Botón guardar siguiente de sección Neumaticos
        final Button btnSigNeuJg = findViewById(R.id.btnSigNeuJg);
        btnSigNeuJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //guardarDatos();
                Intent intent = new Intent( NeuActivity.this, TechoActivity.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
            }
        });

        //Botón volver pendiente
        final Button btnVolverPenJg = (Button)findViewById(R.id.btnPenNeuJg);
        btnVolverPenJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // guardarDatos();
                Intent intent = new Intent( NeuActivity.this, InsPendientesActivity.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
            }
        });

        //Botón volver a secciones
        final Button btnVolverNeuJg = (Button)findViewById(R.id.btnVolverNeuJg);
        btnVolverNeuJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //guardarDatos();
                Intent intent = new Intent( NeuActivity.this, AudioActivity.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
            }
        });

    }

   public void guardarDatos(){
        try{
            JSONObject valor1 = new JSONObject();
            valor1.put("valor_id",188);
            valor1.put("texto",cantNeu.getText().toString());

            JSONObject valor2 = new JSONObject();
            valor2.put("valor_id",189);
            valor2.put("texto",maNeu.getText().toString());

            JSONObject valor3 = new JSONObject();
            valor3.put("valor_id",190);
            valor3.put("texto",moNeu.getText().toString());

            JSONObject valor4 = new JSONObject();
            valor4.put("valor_id",191);
            valor4.put("texto",meNeu.getText().toString());

            JSONObject valor5 = new JSONObject();
            valor5.put("valor_id",193);
            valor5.put("texto",cantNeu2.getText().toString());

            JSONObject valor6 = new JSONObject();
            valor6.put("valor_id",194);
            valor6.put("texto",maNeu2.getText().toString());

            JSONObject valor7 = new JSONObject();
            valor7.put("valor_id",195);
            valor7.put("texto",moNeu2.getText().toString());

            JSONObject valor8 = new JSONObject();
            valor8.put("valor_id",196);
            valor8.put("texto",meNeu2.getText().toString());

            JSONObject valor9 = new JSONObject();
            valor9.put("valor_id",207);
            valor9.put("texto",cantFierro.getText().toString());

            JSONObject valor10 = new JSONObject();
            valor10.put("valor_id",774);
            valor10.put("texto",cantCromadas.getText().toString());

            JSONObject valor11 = new JSONObject();
            valor11.put("valor_id",775);
            valor11.put("texto",cantAlea.getText().toString());

            JSONObject valor12 = new JSONObject();
            valor12.put("valor_id",208);
            valor12.put("texto",maLlanta.getText().toString());



            JSONObject valor79 = new JSONObject();
            valor79.put("valor_id",206);
            valor79.put("texto",estadoNeu.getSelectedItem().toString());

            JSONObject valor80 = new JSONObject();
            valor80.put("valor_id",776);
            valor80.put("texto",estadoLlanta.getSelectedItem().toString());



            JSONArray jsonArray = new JSONArray();


            jsonArray.put(valor1);
            jsonArray.put(valor2);
            jsonArray.put(valor3);
            jsonArray.put(valor4);
            jsonArray.put(valor5);
            jsonArray.put(valor6);
            jsonArray.put(valor7);
            jsonArray.put(valor8);
            jsonArray.put(valor9);
            jsonArray.put(valor10);
            jsonArray.put(valor11);
            jsonArray.put(valor12);
            jsonArray.put(valor79);
            jsonArray.put(valor80);

            JSONObject llenado;
            if (!jsonArray.isNull(0)) {
                for(int i=0;i<jsonArray.length();i++){
                    llenado = new JSONObject(jsonArray.getString(i));
                    db.insertarValor(Integer.parseInt(id_inspeccion),llenado.getInt("valor_id"),llenado.getString("texto"));
                }
            }

        }catch (Exception e)
        {

            Toast.makeText(NeuActivity.this,e.getMessage(),Toast.LENGTH_SHORT);
        }
    }
}
