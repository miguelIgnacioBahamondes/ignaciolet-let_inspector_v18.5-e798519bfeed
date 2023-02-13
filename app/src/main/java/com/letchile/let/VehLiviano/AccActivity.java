package com.letchile.let.VehLiviano;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.Clases.PropiedadesTexto;
import com.letchile.let.Clases.Validaciones;
import com.letchile.let.InsPendientesActivity;
import com.letchile.let.R;
import com.letchile.let.VehLiviano.seccion2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class AccActivity extends AppCompatActivity {

    DBprovider db;

    EditText kilometraje,alarma,cAirb,cupula;
    CheckBox ChekcSetHerr,CheckLlaveRueda,CheckGata,CheckExtint,checkSpo,checkAleron,checkLimLu,checkLogo1,checkCubre,
            checkRuresp,checkFaltante,checkBAnt,checkPert,checkBaliza,checkCupula,checkCajaH,checkAc,checkClim,checkAlr,checkCierreC,
            checkCint,checkFreno,cAirbag,checkCapota,checkBel,checkMold,checkFal;
    String ChekcSetHerrs,CheckLlaveRuedas,CheckGatas,CheckExtints,checkSpoS,checkAlerons,checkLimLus,checkLogo1s,checkCubres,
            checkRuresps,checkFaltantes,checkBAnts,checkPerts,checkBalizas,checkCupulas,checkCajaHs,checkAcs,checkClims,checkAlrs,checkCierreCs,
            checkCints,checkFrenos,checkAirgs="",checkCapotas,checkBels,checkMolds,checkFals;
    Spinner tipoRueda,ubiRueda,ubicAnti;

    String id_inspeccion;

    JSONObject llenado;
    Validaciones validaciones;


    public AccActivity(){
        db = new DBprovider(this);
        validaciones = new Validaciones(this);
    }

    JSONObject obj = new JSONObject();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc);

        Bundle bundle = getIntent().getExtras();
        id_inspeccion = bundle.getString("id_inspeccion");

        //check faldones
        /*checkFal = findViewById(R.id.checkFal);
        if(db.accesorio(Integer.parseInt(id_inspeccion),259).toString().equals("Ok"))
        {
            checkFal.setChecked(true);
            checkFals = "Ok";
        }else{
            checkFal.setChecked(false);
            checkFals = "";
        }
        checkFal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    checkFals = "Ok";
                }else{
                    checkFals = "";
                }
            }
        });*/

        //check molduras
        checkMold = findViewById(R.id.checkMoldu);
        if(db.accesorio(Integer.parseInt(id_inspeccion),283).toString().equals("Ok"))
        {
            checkMold.setChecked(true);
            checkMolds = "Ok";
        }else{
            checkMold.setChecked(false);
            checkMolds = "";
        }
        checkMold.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    checkMolds = "Ok";
                }else{
                    checkMolds = "";
                }
            }
        });

        /*check butaca electrica
        checkBel = findViewById(R.id.checkBel);
        if(db.accesorio(Integer.parseInt(id_inspeccion),336).toString().equals("Ok"))
        {
            checkBel.setChecked(true);
            checkBels = "Ok";
        }else{
            checkBel.setChecked(false);
            checkBels = "";
        }
        checkBel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    checkBels = "Ok";
                }else{
                    checkBels = "";
                }
            }
        });*/

        //check capota electrica
        /*checkCapota = findViewById(R.id.checkCapota);
        if(db.accesorio(Integer.parseInt(id_inspeccion),343).toString().equals("Ok"))
        {
            checkCapota.setChecked(true);
            checkCapotas = "Ok";
        }else{
            checkCapota.setChecked(false);
            checkCapotas = "";
        }
        checkCapota.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    checkCapotas = "Ok";
                }else{
                    checkCapotas = "";
                }
            }
        });*/

        //Cantidad de airbag  282--cantAirb
        cAirb = findViewById(R.id.cantAirb);
        cAirb.setOnEditorActionListener(new PropiedadesTexto());
        cAirb.setText(db.accesorio(Integer.parseInt(id_inspeccion),282).toString());
       // cAirb.setInputType(InputType.TYPE_NULL);

        //check airbags
        cAirbag = findViewById(R.id.checkAirg);
        if(db.accesorio(Integer.parseInt(id_inspeccion),281).toString().equals("Ok"))
        {
            cAirbag.setChecked(true);
            checkAirgs = "Ok";
            cAirb.setEnabled(true);


        }else{
            cAirbag.setChecked(false);
            checkAirgs = "";
            cAirb.setEnabled(false);
        }
        cAirbag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    checkAirgs = "Ok";
                    Log.e("pase por ab", checkAirgs);
                    cAirb.setEnabled(true);
                }else{
                    checkAirgs = "";
                    cAirb.setEnabled(false);
                }
            }
        });

        /*//check alarma
        checkAlr = findViewById(R.id.checkAlr);
        if(db.accesorio(Integer.parseInt(id_inspeccion),279).toString().equals("Ok"))
        {
            checkAlr.setChecked(true);
            checkAlrs = "Ok";
            alarma.setInputType(InputType.TYPE_CLASS_TEXT);
            alarma.setEnabled(true);
        }else{
            checkAlr.setChecked(false);
            checkAlrs = "";
            alarma.setEnabled(false);
        }
        checkAlr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    checkAlrs = "Ok";
                    alarma.setInputType(InputType.TYPE_CLASS_TEXT);
                    alarma.setEnabled(true);
                }else{
                    checkAlrs = "";
                    alarma.setText("");
                    alarma.setInputType(InputType.TYPE_NULL);
                    alarma.setEnabled(false);
                }
            }
        });*/


        //check frenos abs
        checkFreno = findViewById(R.id.checkFreno);
        if(db.accesorio(Integer.parseInt(id_inspeccion),318).toString().equals("Ok"))
        {
            checkFreno.setChecked(true);
            checkFrenos = "Ok";
        }else{
            checkFreno.setChecked(false);
            checkFrenos = "";
        }
        checkFreno.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    checkFrenos = "Ok";
                }else{
                    checkFrenos = "";
                }
            }
        });

        //check cinturones retractiles
        checkCint = findViewById(R.id.checkCint);
        if(db.accesorio(Integer.parseInt(id_inspeccion),324).toString().equals("Ok"))
        {
            checkCint.setChecked(true);
            checkCints = "Ok";
        }else{
            checkCint.setChecked(true);
            checkCints = "Ok";
        }
        checkCint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    checkCints = "Ok";
                }else{
                    checkCints = "";
                }
            }
        });

        //check cierre centralizado
        checkCierreC = findViewById(R.id.checkCierreC);
        if(db.accesorio(Integer.parseInt(id_inspeccion),292).toString().equals("Ok"))
        {
            checkCierreC.setChecked(true);
            checkCierreCs = "Ok";
        }else{
            checkCierreC.setChecked(false);
            checkCierreCs = "";
        }
        checkCierreC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    checkCierreCs = "Ok";
                }else{
                    checkCierreCs = "";
                }
            }
        });

        //Marca Alarma
        alarma = findViewById(R.id.EAlarma);
        alarma.setOnEditorActionListener(new PropiedadesTexto());
        alarma.setText(db.accesorio(Integer.parseInt(id_inspeccion),552).toString());
       // alarma.setInputType(InputType.TYPE_NULL);
        //check alarma
        checkAlr = findViewById(R.id.checkAlr);
        if(db.accesorio(Integer.parseInt(id_inspeccion),279).toString().equals("Ok"))
        {
            checkAlr.setChecked(true);
            checkAlrs = "Ok";
            //alarma.setInputType(InputType.TYPE_CLASS_TEXT);
            alarma.setEnabled(true);
        }else{
            checkAlr.setChecked(false);
            checkAlrs = "";
            alarma.setEnabled(false);
        }
        checkAlr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    checkAlrs = "Ok";
                   // alarma.setInputType(InputType.TYPE_CLASS_TEXT);
                    alarma.setEnabled(true);
                }else{
                    checkAlrs = "";
                    alarma.setText("");
                   // alarma.setInputType(InputType.TYPE_NULL);
                    alarma.setEnabled(false);
                }
            }
        });

        //check climatizador
        checkClim = findViewById(R.id.checkClim);
        if(db.accesorio(Integer.parseInt(id_inspeccion),340).toString().equals("Ok"))
        {
            checkClim.setChecked(true);
            checkClims = "Ok";
        }else{
            checkClim.setChecked(false);
            checkClims = "";
        }
        checkClim.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    checkClims = "Ok";
                }else{
                    checkClims = "";
                }
            }
        });

        //check A/C
        checkAc = findViewById(R.id.checkAc);
        if(db.accesorio(Integer.parseInt(id_inspeccion),277).toString().equals("Ok"))
        {
            checkAc.setChecked(true);
            checkAcs = "Ok";
        }else{
            checkAc.setChecked(false);
            checkAcs = "";
        }
        checkAc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    checkAcs = "Ok";
                }else{
                    checkAcs = "";
                }
            }
        });

        //check caja herramientas
        checkCajaH = findViewById(R.id.checkCajaH);
        if(db.accesorio(Integer.parseInt(id_inspeccion),310).toString().equals("Ok"))
        {
            checkCajaH.setChecked(true);
            checkCajaHs = "Ok";
        }else{
            checkCajaH.setChecked(false);
            checkCajaHs = "";
        }
        checkCajaH.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    checkCajaHs = "Ok";
                }else{
                    checkCajaHs = "";
                }
            }
        });


        //marca cúpula
        cupula = findViewById(R.id.mCupula);
        cupula.setOnEditorActionListener(new PropiedadesTexto());
        cupula.setText(db.accesorio(Integer.parseInt(id_inspeccion),762).toString());

        //check cupula
        checkCupula = findViewById(R.id.checkCupula);
        if(db.accesorio(Integer.parseInt(id_inspeccion),329).toString().equals("Ok"))
        {
            checkCupula.setChecked(true);
            checkCupulas = "Ok";
            cupula.setEnabled(true);
        }else{
            checkCupula.setChecked(false);
            checkCupulas = "";
            cupula.setEnabled(false);
        }
        checkCupula.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    checkCupulas = "Ok";
                    cupula.setEnabled(true);
                }else{
                    checkCupulas = "";
                    cupula.setEnabled(false);
                }
            }
        });


        //check baliza
        checkBaliza = findViewById(R.id.checkBaliza);
        if(db.accesorio(Integer.parseInt(id_inspeccion),801).toString().equals("Ok"))
        {
            checkBaliza.setChecked(true);
            checkBalizas = "Ok";
        }else{
            checkBaliza.setChecked(false);
            checkBalizas = "";
        }
        checkBaliza.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    checkBalizas = "Ok";
                }else{
                    checkBalizas = "";
                }
            }
        });

        //check pertiga
        checkPert = findViewById(R.id.checkPert);
        if(db.accesorio(Integer.parseInt(id_inspeccion),311).toString().equals("Ok"))
        {
            checkPert.setChecked(true);
            checkPerts = "Ok";
        }else{
            checkPert.setChecked(false);
            checkPerts = "";
        }
        checkPert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    checkPerts = "Ok";
                }else{
                    checkPerts = "";
                }
            }
        });

        //check barra antivuelco
        checkBAnt = findViewById(R.id.checkBAnt);
        if(db.accesorio(Integer.parseInt(id_inspeccion),308).toString().equals("Ok"))
        {
            checkBAnt.setChecked(true);
            checkBAnts = "Ok";
        }else{
            checkBAnt.setChecked(false);
            checkBAnts = "";
        }
        checkBAnt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    checkBAnts = "Ok";
                }else{
                    checkBAnts = "";
                }
            }
        });


        //check faltante
        /*checkFaltante = findViewById(R.id.checkFaltante);
        if(db.accesorio(Integer.parseInt(id_inspeccion),814).toString().equals("Ok"))
        {
            checkFaltante.setChecked(true);
            checkFaltantes = "Ok";
        }else{
            checkFaltante.setChecked(false);
            checkFaltantes = "";
        }
        checkFaltante.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    checkFaltantes = "Ok";
                }else{
                    checkFaltantes = "";
                }
            }
        });*/

        //check Rueda repuesto
       /* checkRuresp = findViewById(R.id.checkRuresp);
        if(db.accesorio(Integer.parseInt(id_inspeccion),289).toString().equals("Ok"))
        {
            checkRuresp.setChecked(true);
            checkRuresps = "Ok";
        }else{
            checkRuresp.setChecked(false);
            checkRuresps = "";
        }
        checkRuresp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    checkRuresps = "Ok";
                }else{
                    checkRuresps = "";
                }
            }
        });*/

        //check cubre maleta
        checkCubre = findViewById(R.id.checkCubre);
        if(db.accesorio(Integer.parseInt(id_inspeccion),322).toString().equals("Ok"))
        {
            checkCubre.setChecked(true);
            checkCubres = "Ok";
        }else{
            checkCubre.setChecked(false);
            checkCubres = "";
        }
        checkCubre.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    checkCubres = "Ok";
                }else{
                    checkCubres = "";
                }
            }
        });


        //check LOGO
        checkLogo1 = findViewById(R.id.checkLogo1);
        if(db.accesorio(Integer.parseInt(id_inspeccion),261).toString().equals("Ok"))
        {
            checkLogo1.setChecked(true);
            checkLogo1s = "Ok";
        }else{
            checkLogo1.setChecked(false);
            checkLogo1s = "";
        }
        checkLogo1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    checkLogo1s = "Ok";
                }else{
                    checkLogo1s = "";
                }
            }
        });


        //check Limpia luneta
        checkLimLu = findViewById(R.id.checkLimLu);
        if(db.accesorio(Integer.parseInt(id_inspeccion),269).toString().equals("Ok"))
        {
            checkLimLu.setChecked(true);
            checkLimLus = "Ok";
        }else{
            checkLimLu.setChecked(false);
            checkLimLus = "";
        }
        checkLimLu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    checkLimLus = "Ok";
                }else{
                    checkLimLus = "";
                }
            }
        });

        //Check aleron
        checkAleron = findViewById(R.id.checkAleron);
        if(db.accesorio(Integer.parseInt(id_inspeccion),270).toString().equals("Ok"))
        {
            checkAleron.setChecked(true);
            checkAlerons = "Ok";
        }else{
            checkAleron.setChecked(false);
            checkAlerons = "";
        }
        checkAleron.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    checkAlerons = "Ok";
                }else{
                    checkAlerons = "";
                }
            }
        });

        //CHECK SPOILERS
        checkSpo = findViewById(R.id.checkSpo);
        if(db.accesorio(Integer.parseInt(id_inspeccion),285).toString().equals("Ok"))
        {
            checkSpo.setChecked(true);
            checkSpoS = "Ok";
        }else{
            checkSpo.setChecked(false);
            checkSpoS = "";
        }
        checkSpo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    checkSpoS = "Ok";
                }else{
                    checkSpoS = "";
                }
            }
        });

        //checks set herramientas
        ChekcSetHerr = findViewById(R.id.checkSHerr);
        if(db.accesorio(Integer.parseInt(id_inspeccion),319).toString().equals("Ok"))
        {
            ChekcSetHerr.setChecked(true);
            ChekcSetHerrs = "Ok";
        }else{
            ChekcSetHerr.setChecked(true);
            ChekcSetHerrs = "Ok";
        }
        ChekcSetHerr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    ChekcSetHerrs = "Ok";
                }else{
                    ChekcSetHerrs = "";
                }
            }
        });

        // check llave rueda
        CheckLlaveRueda = findViewById(R.id.checkLRueda);
        if(db.accesorio(Integer.parseInt(id_inspeccion),320).toString().equals("Ok"))
        {
            CheckLlaveRueda.setChecked(true);
            CheckLlaveRuedas = "Ok";
        }else{
            CheckLlaveRueda.setChecked(true);
            CheckLlaveRuedas = "Ok";
        }
        CheckLlaveRueda.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    CheckLlaveRuedas = "Ok";
                }else{
                    CheckLlaveRuedas = "";
                }
            }
        });

        // check gata
        CheckGata = findViewById(R.id.checkGata);
        if(db.accesorio(Integer.parseInt(id_inspeccion),321).toString().equals("Ok"))
        {
            CheckGata.setChecked(true);
            CheckGatas = "Ok";
        }else{
            CheckGata.setChecked(true);
            CheckGatas = "Ok";
        }
        CheckGata.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    CheckGatas = "Ok";
                }else{
                    CheckGatas = "";
                }
            }
        });

        //check extintor
        CheckExtint = findViewById(R.id.checkExt);
        if(db.accesorio(Integer.parseInt(id_inspeccion),483).toString().equals("Ok"))
        {
            CheckExtint.setChecked(true);
            CheckExtints = "Ok";
        }else{
            CheckExtint.setChecked(true);
            CheckExtints = "Ok";
        }
        CheckExtint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    CheckExtints = "Ok";
                }else{
                    CheckExtints = "";
                }
            }
        });

        //kilometraje
      /*  kilometraje = findViewById(R.id.Ekilometraje);
        kilometraje.setOnEditorActionListener(new PropiedadesTexto());
        kilometraje.setText(db.accesorio(Integer.parseInt(id_inspeccion),296).toString());*/






        // cargar un combo tipo rueda repuesto
       /* tipoRueda = findViewById(R.id.tipoRueda);
        String[] arraytipo = getResources().getStringArray(R.array.tipo_rueda);
        final List<String> arraytipolist = Arrays.asList(arraytipo);
        ArrayAdapter<String> spinner_adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arraytipolist);
        spinner_adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoRueda.setAdapter(spinner_adapter1);
        tipoRueda.setSelection(arraytipolist.lastIndexOf(db.accesorio(Integer.parseInt(id_inspeccion),753).toString()));*/

        // cargar un combo ubicacion rueda repuesto
        /*ubiRueda = findViewById(R.id.ubiRueda);
        String[] arraytipo2 = getResources().getStringArray(R.array.ubicacion_rueda);
        final List<String> arraytipolist2 = Arrays.asList(arraytipo2);
        ArrayAdapter<String> spinner_adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arraytipolist2);
        spinner_adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ubiRueda.setAdapter(spinner_adapter2);
        ubiRueda.setSelection(arraytipolist2.lastIndexOf(db.accesorio(Integer.parseInt(id_inspeccion),290).toString()));*/

        // cargar un combo ubicacion barra antivuelco
        ubicAnti = findViewById(R.id.ubicAnti);
        String[] arraytipo3 = getResources().getStringArray(R.array.ubic_anti);
        final List<String> arraytipolist3 = Arrays.asList(arraytipo3);
        ArrayAdapter<String> spinner_adapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arraytipolist3);
        spinner_adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ubicAnti.setAdapter(spinner_adapter3);
        ubicAnti.setSelection(arraytipolist3.lastIndexOf(db.accesorio(Integer.parseInt(id_inspeccion),312).toString()));


        //Botón guardar siguiente de sección accesorio
        final Button btnSigAccJg = findViewById(R.id.btnSigAccJg);
        btnSigAccJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarDatos();
                Intent intent = new Intent( AccActivity.this, DatosInspActivity.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
            }
        });

        //Botón volver a Secciones
        final Button btnVolverAccJg = (Button)findViewById(R.id.btnVolverAccJg);
        btnVolverAccJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {guardarDatos();

                Intent intent = new Intent( AccActivity.this, seccion2.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
            }
        });

        //Botón volver a pendiente
        final Button btnPenAccJg = (Button)findViewById(R.id.btnPenAccJg);
        btnPenAccJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarDatos();
                Intent intent = new Intent( AccActivity.this, InsPendientesActivity.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
            }
        });

    }

    public void guardarDatos(){
        try{

            //edit text
            //kilometraje
           /* JSONObject valor63 = new JSONObject();
            valor63.put("valor_id",296);
            valor63.put("texto",kilometraje.getText().toString());*/

            //Marca alarma
            JSONObject valor64 = new JSONObject();
            valor64.put("valor_id",552);
            valor64.put("texto",alarma.getText().toString());

            //cantidad de airbag
            JSONObject valor65 = new JSONObject();
            valor65.put("valor_id",282);
            valor65.put("texto",cAirb.getText().toString());

            //marca cupula
            JSONObject valor66 = new JSONObject();
            valor66.put("valor_id",762);
            valor66.put("texto",cupula.getText().toString());

           /* JSONObject valor75 = new JSONObject();
            valor75.put("valor_id",753);
            valor75.put("texto",tipoRueda.getSelectedItem().toString());

            JSONObject valor76 = new JSONObject();
            valor76.put("valor_id",290);
            valor76.put("texto",ubiRueda.getSelectedItem().toString());*/

            JSONObject valor77 = new JSONObject();
            valor77.put("valor_id",312);
            valor77.put("texto",ubicAnti.getSelectedItem().toString());

            JSONObject valor78 = new JSONObject();
            valor78.put("valor_id",319);
            valor78.put("texto",ChekcSetHerrs);

            JSONObject valor79 = new JSONObject();
            valor79.put(getString(R.string.valor_id),320);
            valor79.put(getString(R.string.texto),CheckLlaveRuedas);

            JSONObject valor80 = new JSONObject();
            valor80.put(getString(R.string.valor_id),321);
            valor80.put(getString(R.string.texto),CheckGatas);

            JSONObject valor81 = new JSONObject();
            valor81.put(getString(R.string.valor_id),483);
            valor81.put(getString(R.string.texto),CheckExtints);

            JSONObject valor82 = new JSONObject();
            valor82.put(getString(R.string.valor_id),285);
            valor82.put(getString(R.string.texto),checkSpoS);

            JSONObject valor83 = new JSONObject();
            valor83.put(getString(R.string.valor_id),270);
            valor83.put(getString(R.string.texto),checkAlerons);

            JSONObject valor84 = new JSONObject();
            valor84.put(getString(R.string.valor_id),269);
            valor84.put(getString(R.string.texto),checkLimLus);

            JSONObject valor85 = new JSONObject();
            valor85.put(getString(R.string.valor_id),261);
            valor85.put(getString(R.string.texto),checkLogo1s);

            JSONObject valor86 = new JSONObject();
            valor86.put(getString(R.string.valor_id),322);
            valor86.put(getString(R.string.texto),checkCubres);

            /*JSONObject valor87 = new JSONObject();
            valor87.put(getString(R.string.valor_id),289);
            valor87.put(getString(R.string.texto),checkRuresps);

            JSONObject valor88 = new JSONObject();
            valor88.put(getString(R.string.valor_id),814);
            valor88.put(getString(R.string.texto),checkFaltantes);*/

            JSONObject valor89 = new JSONObject();
            valor89.put(getString(R.string.valor_id),311);
            valor89.put(getString(R.string.texto),checkBAnts);

            JSONObject valor90 = new JSONObject();
            valor90.put(getString(R.string.valor_id),308);
            valor90.put(getString(R.string.texto),checkBAnts);

            JSONObject valor91 = new JSONObject();
            valor91.put(getString(R.string.valor_id),801);
            valor91.put(getString(R.string.texto),checkBalizas);

            JSONObject valor92 = new JSONObject();
            valor92.put(getString(R.string.valor_id),329);
            valor92.put(getString(R.string.texto),checkCupulas);

            JSONObject valor93 = new JSONObject();
            valor93.put(getString(R.string.valor_id),310);
            valor93.put(getString(R.string.texto),checkCajaHs);

            JSONObject valor94 = new JSONObject();
            valor94.put(getString(R.string.valor_id),277);
            valor94.put(getString(R.string.texto),checkAcs);

            JSONObject valor95 = new JSONObject();
            valor95.put(getString(R.string.valor_id),340);
            valor95.put(getString(R.string.texto),checkClims);

            JSONObject valor96 = new JSONObject();
            valor96.put(getString(R.string.valor_id),279);
            valor96.put(getString(R.string.texto),checkAlrs);

            JSONObject valor97 = new JSONObject();
            valor97.put(getString(R.string.valor_id),292);
            valor97.put(getString(R.string.texto),checkCierreCs);

            JSONObject valor98 = new JSONObject();
            valor98.put(getString(R.string.valor_id),324);
            valor98.put(getString(R.string.texto),checkCints);

            JSONObject valor99 = new JSONObject();
            valor99.put(getString(R.string.valor_id),318);
            valor99.put(getString(R.string.texto),checkFrenos);

            JSONObject valor100 = new JSONObject();
            valor100.put(getString(R.string.valor_id),281);
            valor100.put(getString(R.string.texto),checkAirgs);

            Log.e("pase json ", getString(R.string.valor_id));


         /*   JSONObject valor101 = new JSONObject();
            valor101.put(getString(R.string.valor_id),343);
            valor101.put(getString(R.string.texto),checkCapotas);*/

         /* JSONObject valor102 = new JSONObject();
            valor102.put(getString(R.string.valor_id),336);
            valor102.put(getString(R.string.texto),checkBels);*/

            JSONObject valor103 = new JSONObject();
            valor103.put(getString(R.string.valor_id),283);
            valor103.put(getString(R.string.texto),checkMolds);

            /*JSONObject valor104 = new JSONObject();
            valor104.put(getString(R.string.valor_id),259);
            valor104.put(getString(R.string.texto),checkFals);*/

            //UNIR TODOS LOS ACCESORIOS EN UN JSONARRAY
            JSONArray jsonArray = new JSONArray();
            //jsonArray.put(valor104);
            jsonArray.put(valor103);
            //jsonArray.put(valor101);
            // jsonArray.put(valor102);

            jsonArray.put(valor100);
            jsonArray.put(valor99);
            jsonArray.put(valor98);
            jsonArray.put(valor97);
            jsonArray.put(valor96);
            jsonArray.put(valor95);
            jsonArray.put(valor94);
            jsonArray.put(valor93);
            jsonArray.put(valor92);
            jsonArray.put(valor91);
            jsonArray.put(valor90);
            jsonArray.put(valor89);
            //jsonArray.put(valor88);
            //jsonArray.put(valor87);
            jsonArray.put(valor86);
            jsonArray.put(valor85);
            jsonArray.put(valor84);
            jsonArray.put(valor83);
            jsonArray.put(valor82);
            jsonArray.put(valor81);
            jsonArray.put(valor80);
            jsonArray.put(valor79);
            jsonArray.put(valor78);
            //jsonArray.put(valor63);
            jsonArray.put(valor64);
            jsonArray.put(valor65);
            jsonArray.put(valor66);
            //jsonArray.put(valor75);
            //jsonArray.put(valor76);
            jsonArray.put(valor77);


            Log.e("largo json ", Integer.toString(jsonArray.length()));
            if (!jsonArray.isNull(0)) {
                for(int i=0;i<jsonArray.length();i++){

                    Log.e("valor ii ", Integer.toString(i));
                    llenado = new JSONObject(jsonArray.getString(i));
                    Log.e("valor json ", jsonArray.getString(i));
                    Log.e("valor ii ", Integer.toString(i));


                    Log.e("INSERTA EN  CODIGO ", llenado.getString("valor_id"));


                    db.insertarValor(Integer.parseInt(id_inspeccion),llenado.getInt("valor_id"),llenado.getString("texto"));
                    //validaciones.insertarDatos(Integer.parseInt(id_inspeccion),llenado.getInt("valor_id"),llenado.getString("texto"));
                    Log.e("INSERTA EN  CODIGO ", db.insertarValor(Integer.parseInt(id_inspeccion),llenado.getInt("valor_id"),llenado.getString("texto")));


                }
            }

        }catch (Exception e)
        {
            Toast.makeText(AccActivity.this,e.getMessage(),Toast.LENGTH_SHORT);
        }
    }
}