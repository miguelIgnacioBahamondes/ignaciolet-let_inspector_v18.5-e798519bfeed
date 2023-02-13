/**
 * Created by Johana on 01/2018.
 */

package com.letchile.let.VehPesado;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.Clases.PropiedadesTexto;
import com.letchile.let.Clases.Validaciones;
import com.letchile.let.R;
import com.letchile.let.VehLiviano.DatosAsegActivity;
import com.letchile.let.VehLiviano.DatosVehActivity;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

public class DatosAsegVpActivity extends AppCompatActivity {

    DBprovider db;

    EditText nomVpJg,patVpJg,matVpJg,rutVpJg,dirJg,mailVpJg,fonoJg,celular;
    ProgressDialog pDialog;
    String [][] datosInspeccion;
    JSONObject llenadoV;
    JSONArray arrayValor;
    Validaciones validaciones;
    Spinner comboRegion,comboComuna;
    String id_inspeccion;


    public DatosAsegVpActivity() {
        db = new DBprovider(this);validaciones=new Validaciones(this);    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_aseg_vp);

        Bundle bundle = getIntent().getExtras();
        id_inspeccion=bundle.getString("id_inspeccion");

        //nombre
        nomVpJg = findViewById(R.id.nomVpJg);
        nomVpJg.setOnEditorActionListener(new PropiedadesTexto());
        nomVpJg.setText(db.accesorio(Integer.parseInt(id_inspeccion),365).toString());

        //Apellido paterno
        patVpJg = findViewById(R.id.patVpJg);
        patVpJg.setOnEditorActionListener(new PropiedadesTexto());
        patVpJg.setText(db.accesorio(Integer.parseInt(id_inspeccion),366).toString());

        //Apellido materno
        matVpJg = findViewById(R.id.matVpJg);
        matVpJg.setOnEditorActionListener(new PropiedadesTexto());
        matVpJg.setText(db.accesorio(Integer.parseInt(id_inspeccion),367).toString());

        //rut
        rutVpJg = findViewById(R.id.rutVpJg);
        rutVpJg.setOnEditorActionListener(new PropiedadesTexto());
        rutVpJg.setText(db.accesorio(Integer.parseInt(id_inspeccion),368).toString());

        //direccion
        dirJg = findViewById(R.id.dirJg);
        dirJg.setOnEditorActionListener(new PropiedadesTexto());
        dirJg.setText(db.accesorio(Integer.parseInt(id_inspeccion),371).toString());

        //fono
        fonoJg = findViewById(R.id.fonoJg);
        fonoJg.setOnEditorActionListener(new PropiedadesTexto());
        fonoJg.setText(db.accesorio(Integer.parseInt(id_inspeccion),369).toString());

        //celular
        celular = findViewById(R.id.celular);
        celular.setOnEditorActionListener(new PropiedadesTexto());
        celular.setText(db.accesorio(Integer.parseInt(id_inspeccion),533).toString());

        //mail
        /*mailVpJg = findViewById(R.id.mailVpJg);
        mailVpJg.setOnEditorActionListener(new PropiedadesTexto());
        mailVpJg.setText(db.accesorio(Integer.parseInt(id_inspeccion),532).toString());*/


        /*String regionInicial=db.obtenerRegion(db.accesorio(Integer.parseInt(id_inspeccion),370).toString());
        String listaRegiones[][]=db.listaRegiones();
        comboRegion = findViewById(R.id.comboRegVpJg);
        String[] arraySpinner = new String[listaRegiones.length+1];
        arraySpinner[0]=regionInicial;
        for(int i=0;i<listaRegiones.length;i++)        {
            arraySpinner[i+1]=listaRegiones[i][0];
        }
        ArrayAdapter<String> adapterRegion = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapterRegion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comboRegion.setAdapter(adapterRegion);


        comboComuna = findViewById(R.id.comboComVpJg);
        comboRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Rescata el nombre del item elegido
                String regionSelected = comboRegion.getSelectedItem().toString();
                //Rescata la lista según el item elegido
                String listaComunas[][] = db.listaComunas(regionSelected);
                //Inicia el nuevo combo a llenar

                //Se crea una variable array para ser llenado
                String[] spinnerComuna = new String[listaComunas.length+1];
                spinnerComuna[0] = db.accesorio(Integer.parseInt(id_inspeccion),370).toString();
                for(int i=0;i<listaComunas.length;i++){
                    spinnerComuna[i+1] = listaComunas[i][0];
                }
                ArrayAdapter<String> adapterComuna = new ArrayAdapter<String>(DatosAsegVpActivity.this,android.R.layout.simple_spinner_item,spinnerComuna);
                adapterComuna.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                comboComuna.setAdapter(adapterComuna);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/

        // AUTOCOMPLETAR REGION
        String regionInicial=db.obtenerRegion(db.accesorio(Integer.parseInt(id_inspeccion),370).toString());
        final AutoCompleteTextView regionV = (AutoCompleteTextView) findViewById(R.id.comboRegVpJg);

        String listaRegiones[][]=db.listaRegiones();
        final String[] arraySpinner = new String[listaRegiones.length+1];
        arraySpinner[0]=regionInicial;

        for(int i=0;i<listaRegiones.length;i++)
        {
            arraySpinner[i+1]=listaRegiones[i][0];


        }
        ArrayAdapter<String> adapterRegion = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapterRegion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regionV.setAdapter(adapterRegion);


        //valida region
        regionV.setValidator(new AutoCompleteTextView.Validator() {
            @Override
            public boolean isValid (CharSequence text){
                //some logic here returns true or false based on if the text is validated
                Arrays.sort(arraySpinner);

                if (Arrays.binarySearch(arraySpinner, text.toString()) > 0) {
                    return true;

                }

                return false;
            }

            @Override
            public CharSequence fixText (CharSequence invalidText){
                //If .isValid() returns false then the code comes here
                //do whatever way you want to fix in the users input and  return it
                return "Región inválida";
            }
        });

        final AutoCompleteTextView comunaV = (AutoCompleteTextView) findViewById(R.id.comboComVpJg);
        regionV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Rescata el nombre del item elegido
                String regionSelected = regionV.getText().toString();

                //Rescata la lista según el item elegido
                String listaComunas[][] = db.listaComunas(regionSelected);

                final String[] arrayComuna = new String[listaComunas.length+1];
                arrayComuna[0] = db.accesorio(Integer.parseInt(id_inspeccion),370).toString();
                for(int i=0;i<listaComunas.length;i++){
                    arrayComuna[i+1] = listaComunas[i][0];
                }
                ArrayAdapter<String> adapterComuna = new ArrayAdapter<String>(DatosAsegVpActivity.this,android.R.layout.simple_spinner_item,arrayComuna);
                adapterComuna.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                comunaV.setAdapter(adapterComuna);

                //valida  comuna
                comunaV.setValidator(new AutoCompleteTextView.Validator() {
                    @Override
                    public boolean isValid (CharSequence text){
                        //some logic here returns true or false based on if the text is validated
                        Arrays.sort(arrayComuna);
                        if (Arrays.binarySearch(arrayComuna, text.toString()) > 0) {
                            return true;

                        }
                        else{

                            Toast.makeText(DatosAsegVpActivity.this, "Debe ingresar comuna", Toast.LENGTH_SHORT).show();
                            return false;
                        }


                    }

                    @Override
                    public CharSequence fixText (CharSequence invalidText){
                        //If .isValid() returns false then the code comes here
                        //do whatever way you want to fix in the users input and  return it
                        return "Comuna inválida";
                    }
                });
            }

           /* @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }*/
        });


        //boton siguiente
        final Button btnSigAsegVpJg = findViewById(R.id.btnSigAsegVpJg);
        btnSigAsegVpJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String regi = regionV.getText().toString();
                String comu = comunaV.getText().toString();

                if(regi.equals("") || comu.equals(""))
                {
                    Toast.makeText(DatosAsegVpActivity.this, "Debe ingresar región y comuna", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    guardarDatos();
                    Intent intent = new Intent( DatosAsegVpActivity.this, DatosInspVpActivity.class);
                    intent.putExtra("id_inspeccion",id_inspeccion);
                    startActivity(intent);

                }
            }
        });

        //Botón volver de sección pesado
        final Button btnVolverAsegVpJg = (Button)findViewById(R.id.btnVolverAsegVpJg);
        btnVolverAsegVpJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                guardarDatos();
                Intent intent = new Intent( DatosAsegVpActivity.this, SeccionVpActivity.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
            }
        });
    }

    public void guardarDatos(){
        try{
            JSONObject valor85 = new JSONObject();
            valor85.put("valor_id",365);
            valor85.put("texto",nomVpJg.getText().toString());

            JSONObject valor86 = new JSONObject();
            valor86.put("valor_id",366);
            valor86.put("texto",patVpJg.getText().toString());

            JSONObject valor87 = new JSONObject();
            valor87.put("valor_id",367);
            valor87.put("texto",matVpJg.getText().toString());

            JSONObject valor88 = new JSONObject();
            valor88.put("valor_id",368);
            valor88.put("texto",rutVpJg.getText().toString());

            JSONObject valor89 = new JSONObject();
            valor89.put("valor_id",371);
            valor89.put("texto",dirJg.getText().toString());

            JSONObject valor90 = new JSONObject();
            valor90.put("valor_id",369);
            valor90.put("texto",fonoJg.getText().toString());

            JSONObject valor91 = new JSONObject();
            valor91.put("valor_id",533);
            valor91.put("texto",celular.getText().toString());


           /* JSONObject valor92 = new JSONObject();
            valor92.put("valor_id",532);
            valor92.put("texto",mailVpJg.getText().toString());*/

            JSONObject datosValorVpCo = new JSONObject();
            datosValorVpCo.put("valor_id",370);
            datosValorVpCo.put("texto",comboComuna.getSelectedItem().toString());

            JSONArray jsonArray = new JSONArray();
            jsonArray.put(valor85);
            jsonArray.put(valor86);
            jsonArray.put(valor87);
            jsonArray.put(valor88);
            jsonArray.put(valor89);
            jsonArray.put(valor90);
            jsonArray.put(valor91);
           // jsonArray.put(valor92);
            jsonArray.put(datosValorVpCo);

            JSONObject llenadoV;
            if (!jsonArray.isNull(0)) {
                for(int i=0;i<jsonArray.length();i++){
                    llenadoV = new JSONObject(jsonArray.getString(i));
                    db.insertarValor(Integer.parseInt(id_inspeccion),llenadoV.getInt("valor_id"),llenadoV.getString("texto"));
                }
            }
        }catch (Exception e)
        {
            Toast.makeText(DatosAsegVpActivity.this, "", Toast.LENGTH_SHORT).show();
        }
    }


}
