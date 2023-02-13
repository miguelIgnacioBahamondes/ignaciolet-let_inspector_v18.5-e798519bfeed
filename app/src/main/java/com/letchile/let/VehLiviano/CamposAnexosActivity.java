package com.letchile.let.VehLiviano;

import com.letchile.let.BD.DBprovider;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.letchile.let.EmailActivity;
import com.letchile.let.InsPendientesActivity;
import com.letchile.let.R;
import com.letchile.let.Servicios.ConexionInternet;
import com.letchile.let.Clases.PropiedadesTexto;
import com.letchile.let.Clases.Validaciones;
import com.letchile.let.Servicios.TransferirInspeccion;
import com.letchile.let.Servicios.TransmitirOiEmail;
import com.letchile.let.VehLiviano.Fotos.Posterior;
import com.letchile.let.detalleActivity;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CamposAnexosActivity extends AppCompatActivity {

    DBprovider db;
    Boolean connec = false;
    String id_inspeccion;
    RadioButton  rs1,rs2, rs3, rs4, rs5, rs6, rs7, rs9, rn1, rn2, rn3, rn4, rn5, rn6, rn7, rn9;
    RadioGroup grupo1,grupo2,grupo3,grupo4,grupo5,grupo6,grupo7,grupo9;
    JSONObject llenado;
    int validacampos;
    Spinner spinner_tipoVe, spinner_pat_esp;


    public CamposAnexosActivity() { db = new DBprovider(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campos_anexos);
        ButterKnife.bind(this);
        connec = new ConexionInternet(this).isConnectingToInternet();

        Bundle bundle = getIntent().getExtras();
        id_inspeccion = bundle.getString("id_inspeccion");


        grupo1=(RadioGroup) findViewById(R.id.grupo1);
        rs1=(RadioButton)findViewById(R.id.rs1);
        rn1=(RadioButton)findViewById(R.id.rn1);


        /*grupo1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == R.id.rs1)
                {
                    if (db.accesorio(Integer.parseInt(id_inspeccion),815).toString().equals("Ok")==true) {
                        grupo1.check(R.id.rs1);
                        Log.i("aa","aaa" + db.accesorio(Integer.parseInt(id_inspeccion),815).toString().equals("Ok") );
                    }
                    else
                    {
                        Log.i("qq","qq" +db.accesorio(Integer.parseInt(id_inspeccion),815).toString().equals("Ok"));
                        rn1.setChecked(false);


                    }
                }
                else
                {
                    Log.i("oo","oo");
                    rn1.setChecked(true);
                }
            }
        });*/


        grupo2=(RadioGroup) findViewById(R.id.grupo2);
        rs2=(RadioButton)findViewById(R.id.rs2);
        rn2=(RadioButton)findViewById(R.id.rn2);

       /* grupo2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rs2)
                {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion),816).toString().equals("")) {
                        rs2.isChecked();
                    }
                    else
                    {
                        rn2.isChecked();

                    }
                }
                else
                {

                    rn2.isChecked();
                }
            }
        });*/

        grupo3=(RadioGroup) findViewById(R.id.grupo3);
        rs3=(RadioButton)findViewById(R.id.rs3);
        rn3=(RadioButton)findViewById(R.id.rn3);

        /*grupo3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rs3)
                {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion),817).toString().equals("")) {
                        rs3.isChecked();
                    }
                    else
                    {
                        rn3.isChecked();

                    }
                }
                else
                {

                    rn3.isChecked();
                }
            }
        });*/



        grupo4=(RadioGroup) findViewById(R.id.grupo4);
        rs4=(RadioButton)findViewById(R.id.rs4);
        rn4=(RadioButton)findViewById(R.id.rn4);

        /*grupo4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rs4)
                {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion),818).toString().equals("")) {
                        rs4.isChecked();
                    }
                    else
                    {
                        rn4.isChecked();

                    }
                }
                else
                {

                    rn4.isChecked();
                }
            }
        });*/

        grupo5=(RadioGroup) findViewById(R.id.grupo5);
        rs5=(RadioButton)findViewById(R.id.rs5);
        rn5=(RadioButton)findViewById(R.id.rn5);
        /*grupo5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rs5)
                {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion),819).toString().equals("")) {
                        rs5.isChecked();
                    }
                    else
                    {
                        rn5.isChecked();

                    }
                }
                else
                {

                    rn5.isChecked();
                }
            }
        });*/



        grupo6=(RadioGroup) findViewById(R.id.grupo6);
        rs6=(RadioButton)findViewById(R.id.rs6);
        rn6=(RadioButton)findViewById(R.id.rn6);
        /*grupo6.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rs6)
                {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion),820).toString().equals("")) {
                        rs6.isChecked();
                    }
                    else
                    {
                        rn6.isChecked();

                    }
                }
                else
                {

                    rn6.isChecked();
                }
            }
        });*/


        grupo7=(RadioGroup) findViewById(R.id.grupo7);
        rs7=(RadioButton)findViewById(R.id.rs7);
        rn7=(RadioButton)findViewById(R.id.rn7);
       /* grupo7.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rs7)
                {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion),821).toString().equals("")) {
                        rs7.isChecked();
                    }
                    else
                    {
                        rn7.isChecked();

                    }
                }
                else
                {

                    rn7.isChecked();
                }
            }
        });*/

        grupo9=(RadioGroup) findViewById(R.id.grupo9);
        rs9=(RadioButton)findViewById(R.id.rs9);
        rn9=(RadioButton)findViewById(R.id.rn9);
       /* grupo9.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rs9)
                {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion),824).toString().equals("")) {
                        rs9.isChecked();
                    }
                    else
                    {
                        rn9.isChecked();

                    }
                }
                else
                {

                    rn9.isChecked();
                }
            }
        });*/


        //Spinner
        //inicializo el spinner

        spinner_tipoVe = findViewById(R.id.spinner_tipoV);
        //traigo el array de datos
        String[] arraytipo = getResources().getStringArray(R.array.combo_tipo_veh);
        //creo una lista para insertar todos los datos
        final List<String> arraytipolist = Arrays.asList(arraytipo);
        //creo el adapter para llenar el spinner con la lista
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arraytipolist);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //se inserta el adapter
        spinner_tipoVe.setAdapter(spinner_adapter);
        //se selecciona el que está guardado en la base de datos
        spinner_tipoVe.setSelection(arraytipolist.indexOf(db.accesorio(Integer.parseInt(id_inspeccion),822).toString()));

        if (db.accesorio(Integer.parseInt(id_inspeccion),822).toString().equals("")) {
            spinner_tipoVe.setSelection(0);

            if(spinner_tipoVe.getSelectedItem().toString().equals("Seleccione..."))
            {
                spinner_tipoVe.getSelectedItem().toString().equals("");

            }


        }
        else
        {
            spinner_tipoVe.getSelectedItem().toString();

        }


        spinner_pat_esp = findViewById(R.id.spinner_patEsp);
        String[] arrayuso = getResources().getStringArray(R.array.combo_pat_esp);
        final List<String> arrayusolist = Arrays.asList(arrayuso);
        ArrayAdapter<String> spinner_adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayuso);
        spinner_adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_pat_esp.setAdapter(spinner_adapter2);
        spinner_pat_esp.setSelection(arrayusolist.indexOf(db.accesorio(Integer.parseInt(id_inspeccion),823).toString()));

        if (db.accesorio(Integer.parseInt(id_inspeccion),823).toString().equals("")) {
            spinner_pat_esp.setSelection(0);

            if(spinner_pat_esp.getSelectedItem().toString().equals("Seleccione..."))
            {
                spinner_pat_esp.getSelectedItem().toString().equals("");

            }


        }
        else
        {
            spinner_pat_esp.getSelectedItem().toString();

        }


        //Log.d("llegue pantalla anexo","dentro de la pantalla anexo");

        //VALIDAR QUETODO LO OBLIGATORIO ESTÉ LISTO
        //ir a clase
        Button btnFinalizar = findViewById(R.id.btnFinAnexos);
        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validacampos=0;

                //Log.d("clickl pantalla anerxo","dentro del click");
                //verificar nuevamente la conexión
                connec = new ConexionInternet(CamposAnexosActivity.this).isConnectingToInternet();
                /*android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CamposAnexosActivity.this);
                builder.setCancelable(false);
                builder.setMessage(Html.fromHtml("¿Seguro que desea finalizar la inspeccion <b>N°OI: " + id_inspeccion + "</b>?."));*/

               // builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    //@Override
                   // public void onClick(DialogInterface dialogInterface, int i) {

                        //int fotosTomadas = db.fotosObligatoriasTomadas(Integer.parseInt(id_inspeccion));


                        if (grupo1.getCheckedRadioButtonId() == -1) {

                            Toast.makeText(CamposAnexosActivity.this, "Debe seleccionar una opción en todos los anexos. " , Toast.LENGTH_LONG).show();
                            validacampos=1;
                        } else if (grupo2.getCheckedRadioButtonId() == -1) {
                            Toast.makeText(CamposAnexosActivity.this, "Debe seleccionar una opción en todos los anexos. ", Toast.LENGTH_LONG).show();
                            validacampos=1;
                        } else if (grupo3.getCheckedRadioButtonId() == -1) {
                            Toast.makeText(CamposAnexosActivity.this, "Debe seleccionar una opción en todos los anexos. ", Toast.LENGTH_LONG).show();
                            validacampos=1;
                        } else if (grupo4.getCheckedRadioButtonId() == -1) {
                            Toast.makeText(CamposAnexosActivity.this, "Debe seleccionar una opción en todos los anexos. ", Toast.LENGTH_LONG).show();
                            validacampos=1;
                        } else if (grupo5.getCheckedRadioButtonId() == -1) {
                            Toast.makeText(CamposAnexosActivity.this, "Debe seleccionar una opción en todos los anexos. ", Toast.LENGTH_LONG).show();
                            validacampos=1;
                        } else if (grupo6.getCheckedRadioButtonId() == -1) {
                            Toast.makeText(CamposAnexosActivity.this, "Debe seleccionar una opción en todos los anexos. ", Toast.LENGTH_LONG).show();
                            validacampos=1;

                        } else if (grupo7.getCheckedRadioButtonId() == -1) {
                            Toast.makeText(CamposAnexosActivity.this, "Debe seleccionar una opción en todos los anexos. ", Toast.LENGTH_LONG).show();
                            validacampos=1;

                        } else {

                            guardarDatos();
                            Intent seccion = new Intent(CamposAnexosActivity.this, EmailActivity.class);
                            seccion.putExtra("id_inspeccion",id_inspeccion);
                            startActivity(seccion);
                            finish();

                            //cambiar inspeccion a estado para transmitir
                           /* db.cambiarEstadoInspeccion(Integer.parseInt(id_inspeccion), 2);

                            if (connec) {
                                Intent servis = new Intent(CamposAnexosActivity.this, TransferirInspeccion.class);
                                startService(servis);

                                //Toast.makeText(CamposAnexosActivity.this, "Inspección " + id_inspeccion , Toast.LENGTH_LONG).show();
                            }
                            Intent seccion = new Intent(CamposAnexosActivity.this, InsPendientesActivity.class);
                            seccion.putExtra("id_inspeccion",id_inspeccion);
                            startActivity(seccion);
                            finish();

                            String email = db.DatosOiEmail(id_inspeccion);

                            Intent servis2 = new Intent(CamposAnexosActivity.this, TransmitirOiEmail.class);
                            servis2.putExtra("id_inspeccion", id_inspeccion);
                            servis2.putExtra("email", email);
                            startService(servis2);//fin codigo transmision*/
                            //Toast.makeText(CamposAnexosActivity.this, "transmitida" + email, Toast.LENGTH_LONG).show();
                     //   }

                            /*if (grupo8.getCheckedRadioButtonId() == -1) {
                            Toast.makeText(CamposAnexosActivity.this, "Debe seleccionar una opción en todos los anexos. ", Toast.LENGTH_LONG).show();
                            validacampos=1;

                        } else if (grupo9.getCheckedRadioButtonId() == -1) {
                            Toast.makeText(CamposAnexosActivity.this, "Debe seleccionar una opción en todos los anexos. ", Toast.LENGTH_LONG).show();
                            validacampos=1;

                        } else if (spinner_tipoVe.getSelectedItem().toString().equals("Seleccione...")){
                                Toast.makeText(getApplicationContext(), "Debe seleccionar tipo de vehículo", Toast.LENGTH_LONG).show();

                                  //Si el spinner no tiene nada seleccionado
                            }else{
                                    guardarDatos();
                                    //cambiar inspeccion a estado para transmitir
                                    db.cambiarEstadoInspeccion(Integer.parseInt(id_inspeccion), 2);

                                    if (connec) {
                                        Intent servis = new Intent(CamposAnexosActivity.this, TransferirInspeccion.class);
                                        startService(servis);

                                        //Toast.makeText(CamposAnexosActivity.this, "Inspección " + id_inspeccion , Toast.LENGTH_LONG).show();
                                    }
                                    Intent seccion = new Intent(CamposAnexosActivity.this, InsPendientesActivity.class);
                                    seccion.putExtra("id_inspeccion",id_inspeccion);
                                    startActivity(seccion);
                                    finish();
                                    //Toast.makeText(CamposAnexosActivity.this, "transmitida", Toast.LENGTH_LONG).show();
                            }*/



                    }
                /*}).setNegativeButton("Rechazar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(CamposAnexosActivity.this, "Inspección no transmitida", Toast.LENGTH_LONG).show();
                    }
                });*/

               /* android.app.AlertDialog alert = builder.create();
                alert.show();*/

            }

        });


    }

    //guarda datos
    public void guardarDatos() {
        try {

            RadioGroup contenedor1 = (RadioGroup) findViewById(R.id.grupo1);
            int radioButtonID1 = contenedor1.getCheckedRadioButtonId();
            RadioButton radioButton1 = (RadioButton) contenedor1.findViewById(radioButtonID1);
            String texto1 = (String) radioButton1.getText();


            RadioGroup contenedor2 = (RadioGroup) findViewById(R.id.grupo2);
            int radioButtonID2 = contenedor2.getCheckedRadioButtonId();
            RadioButton radioButton2 = (RadioButton) contenedor2.findViewById(radioButtonID2);
            String texto2 = (String) radioButton2.getText();

            RadioGroup contenedor3 = (RadioGroup) findViewById(R.id.grupo3);
            int radioButtonID3 = contenedor3.getCheckedRadioButtonId();
            RadioButton radioButton3 = (RadioButton) contenedor3.findViewById(radioButtonID3);
            String texto3 = (String) radioButton3.getText();

            RadioGroup contenedor4 = (RadioGroup) findViewById(R.id.grupo4);
            int radioButtonID4 = contenedor4.getCheckedRadioButtonId();
            RadioButton radioButton4 = (RadioButton) contenedor4.findViewById(radioButtonID4);
            String texto4 = (String) radioButton4.getText();

            RadioGroup contenedor5 = (RadioGroup) findViewById(R.id.grupo5);
            int radioButtonID5 = contenedor5.getCheckedRadioButtonId();
            RadioButton radioButton5 = (RadioButton) contenedor5.findViewById(radioButtonID5);
            String texto5 = (String) radioButton5.getText();

            RadioGroup contenedor6 = (RadioGroup) findViewById(R.id.grupo6);
            int radioButtonID6 = contenedor6.getCheckedRadioButtonId();
            RadioButton radioButton6 = (RadioButton) contenedor6.findViewById(radioButtonID6);
            String texto6 = (String) radioButton6.getText();

            RadioGroup contenedor7 = (RadioGroup) findViewById(R.id.grupo7);
            int radioButtonID7 = contenedor7.getCheckedRadioButtonId();
            RadioButton radioButton7 = (RadioButton) contenedor7.findViewById(radioButtonID7);
            String texto7 = (String) radioButton7.getText();

           /* RadioGroup contenedor8 = (RadioGroup) findViewById(R.id.grupo8);
            int radioButtonID8 = contenedor8.getCheckedRadioButtonId();
            RadioButton radioButton8 = (RadioButton) contenedor8.findViewById(radioButtonID8);
            String texto8 = (String) radioButton8.getText();*/

            RadioGroup contenedor9 = (RadioGroup) findViewById(R.id.grupo9);
            int radioButtonID9 = contenedor9.getCheckedRadioButtonId();
            RadioButton radioButton9 = (RadioButton) contenedor9.findViewById(radioButtonID9);
            String texto9 = (String) radioButton9.getText();


            if(texto1.equals("Si"))
            {
                texto1="Ok";
            }
            else
            {
                texto1="";
            }

            if(texto2.equals("Si"))
            {
                texto2="Ok";
            }
            else
            {
                texto2="";
            }

            if(texto3.equals("Si"))
            {
                texto3="Ok";
            }
            else
            {
                texto3="";
            }

            if(texto4.equals("Si"))
            {
                texto4="Ok";
            }
            else
            {
                texto4="";
            }


            if(texto5.equals("Si"))
            {
                texto5="Ok";
            }
            else
            {
                texto5="";
            }


            if(texto6.equals("Si"))
            {
                texto6="Ok";
            }
            else
            {
                texto6="";
            }


            if(texto7.equals("Si"))
            {
                texto7="Ok";
            }
            else
            {
                texto7="";
            }

            /*if(texto8.equals("Si"))
            {
                texto8="Ok";
            }
            else
            {
                texto8="";
            }*/

            JSONObject valoraa = new JSONObject();
            valoraa.put("valor_id",815);
            valoraa.put("texto",texto1);

            JSONObject valorab = new JSONObject();
            valorab.put("valor_id",816);
            valorab.put("texto",texto2);

            JSONObject valorac = new JSONObject();
            valorac.put("valor_id",817);
            valorac.put("texto",texto3);

            JSONObject valorad = new JSONObject();
            valorad.put("valor_id",818);
            valorad.put("texto",texto4);

            JSONObject valorae = new JSONObject();
            valorae.put("valor_id",819);
            valorae.put("texto",texto5);

            JSONObject valoraf = new JSONObject();
            valoraf.put("valor_id",820);
            valoraf.put("texto",texto6);

            JSONObject valorag = new JSONObject();
            valorag.put("valor_id",821);
            valorag.put("texto",texto7);

            JSONObject valorah = new JSONObject();
            valorah.put("valor_id",823);
            valorah.put("texto",spinner_pat_esp.getSelectedItem().toString());

            JSONObject valorai = new JSONObject();
            valorai.put("valor_id",824);
            valorai.put("texto",texto9);

            JSONObject valoraj = new JSONObject();
            valoraj.put("valor_id",822);
            valoraj.put("texto",spinner_tipoVe.getSelectedItem().toString());

            JSONArray datosvalores = new JSONArray();
            datosvalores.put(valoraa);
            datosvalores.put(valorab);
            datosvalores.put(valorac);
            datosvalores.put(valorad);
            datosvalores.put(valorae);
            datosvalores.put(valoraf);
            datosvalores.put(valorag);
            datosvalores.put(valorah);
            datosvalores.put(valorai);
            datosvalores.put(valoraj);



            Log.i("por  aca","por aca" + datosvalores.put(valoraj));
            //Toast.makeText(CamposAnexosActivity.this, "Inspecciónnnnnn " + spinner_tipoVe.getSelectedItem().toString(), Toast.LENGTH_LONG).show();

            if(!datosvalores.isNull(0)){
                for(int i=0;i<datosvalores.length();i++){
                    llenado = new JSONObject(datosvalores.getString(i));
                    db.insertarValor(Integer.parseInt(id_inspeccion),llenado.getInt("valor_id"),llenado.getString("texto"));


                }
            }

        }catch (Exception e){
            Toast.makeText(CamposAnexosActivity.this,"Error conversión json",Toast.LENGTH_SHORT);
        }

    }

    @OnClick(R.id.btnVolverAnex)
    public void DatosVolver(View view)
    {

        guardarDatos();
        Intent intent = new Intent(CamposAnexosActivity.this, seccion2.class);
        intent.putExtra("id_inspeccion", id_inspeccion);
        startActivity(intent);
    }

}




