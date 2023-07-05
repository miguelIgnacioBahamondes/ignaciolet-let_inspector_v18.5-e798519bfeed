package com.letchile.let;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.text.Html;
import android.Manifest;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.Geocoder;
import android.location.Address;
import android.widget.Toast;
import android.widget.EditText;
import android.view.View.OnFocusChangeListener;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.Clases.Localizacion;
import com.letchile.let.Clases.PropiedadesFoto;
import com.letchile.let.Servicios.AgendarInspeccion;
import com.letchile.let.Servicios.ConexionInternet;
import com.letchile.let.Servicios.TransferirFoto;
import com.letchile.let.Servicios.TransferirGeolocalizacion;
import com.letchile.let.Servicios.TransmitirOiEmail;
import com.letchile.let.VehLiviano.DatosAsegActivity;
import com.letchile.let.VehLiviano.seccion2;


import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Calendar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.io.IOException;

public class AgendarActivity extends AppCompatActivity {

    DBprovider db;
    Boolean connec = false;
    String id_inspeccion, mensajeError, fechaseleccionada;
    PropiedadesFoto foto;
    AutoCompleteTextView comunaa;
    EditText direcion, txtComentarioAgenda;
    /*
        private static String APP_DIRECTORY = "";
        private static String MEDIA_DIRECTORY = APP_DIRECTORY ;
        private File ruta_fa;
        private String ruta = "";
        private String mPath;
        Boolean connec = false;
        Button btnVolverInspJg;
        Context contexto = this;
        int correlativo = 0;
        String nombreimagen = "",perfil;
        TextView  fono;
    */
    public AgendarActivity(){db = new DBprovider(this);foto = new PropiedadesFoto(this);};
@Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_oi);

        connec = new ConexionInternet(this).isConnectingToInternet();

        //Traspaso de datos
        Bundle bundle = getIntent().getExtras();
        id_inspeccion=bundle.getString("id_inspeccion");

        direcion = findViewById(R.id.direccionAgenda);
        txtComentarioAgenda = findViewById(R.id.comentarioAgenda);

        // AUTOCOMPLETAR REGION
        String regionInicial2=db.obtenerRegion(db.accesorio(Integer.parseInt(id_inspeccion),359).toString());
        final AutoCompleteTextView region = (AutoCompleteTextView) findViewById(R.id.regionSpinnerMQ);

        String listaRegiones2[][]=db.listaRegiones();
        final String[] arraySpinner2 = new String[listaRegiones2.length+1];
        arraySpinner2[0]=regionInicial2;

        for(int i=0;i<listaRegiones2.length;i++)
        {
            arraySpinner2[i+1]=listaRegiones2[i][0];
        }
        ArrayAdapter<String> adapterRegion2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner2);
        adapterRegion2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        region.setAdapter(adapterRegion2);

        //valida region
        region.setValidator(new AutoCompleteTextView.Validator() {
            @Override
            public boolean isValid (CharSequence text){
                //some logic here returns true or false based on if the text is validated
                Arrays.sort(arraySpinner2);

                if (Arrays.binarySearch(arraySpinner2, text.toString()) > 0) {
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

    comunaa = (AutoCompleteTextView) findViewById(R.id.comunaSpinner);
    region.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Rescata el nombre del item elegido
            String regionSelected = region.getText().toString();

            //Rescata la lista según el item elegido
            String listaComunas[][] = db.listaComunas(regionSelected);

            final String[] arrayComuna = new String[listaComunas.length+1];
            arrayComuna[0] = db.accesorio(Integer.parseInt(id_inspeccion),359).toString();
            for(int i=0;i<listaComunas.length;i++){
                arrayComuna[i+1] = listaComunas[i][0];
            }
            ArrayAdapter<String> adapterComuna = new ArrayAdapter<String>(AgendarActivity.this,android.R.layout.simple_spinner_item,arrayComuna);
            adapterComuna.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            comunaa.setAdapter(adapterComuna);

            //valida  comuna
            comunaa.setValidator(new AutoCompleteTextView.Validator() {
                @Override
                public boolean isValid (CharSequence text){
                    //some logic here returns true or false based on if the text is validated
                    Arrays.sort(arrayComuna);
                    if (Arrays.binarySearch(arrayComuna, text.toString()) > 0) {
                        return true;
                    }
                    else{
                        Toast.makeText(AgendarActivity.this, "Debe ingresar comuna", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
                @Override
                public CharSequence fixText (CharSequence invalidText){
                    //If .isValid() returns false then the code comes here
                    //do whatever way you want to fix in the users input and  return it
                    Toast.makeText(AgendarActivity.this, "Debe ingresar comuna", Toast.LENGTH_LONG).show();
                    return invalidText.toString();
                }
            });
        }

    });

    comunaa.setText(db.accesorio(Integer.parseInt(id_inspeccion),7).toString());


    CalendarView calendarioAgenda = (CalendarView) findViewById(R.id.calendarViewAgendar);

    Date currenTime = Calendar.getInstance().getTime();
    calendarioAgenda.setMinDate(currenTime.getTime());

    long selectedDate = calendarioAgenda.getDate();
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    String dateString = formatter.format(new Date(selectedDate));
    Log.i("fecha INICIADA =>", dateString);
    fechaseleccionada = dateString;

    calendarioAgenda.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
        @Override
        public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String selectedDates = sdf.format(new Date(year-1900,month,dayOfMonth));
            Log.i("calendario =>",selectedDates);
            fechaseleccionada = selectedDates;
            //dateTest.setText(selectedDates);
        }
    });

    EditText txthoraInicio = (EditText) findViewById(R.id.horaInicio);
    txthoraInicio.setOnFocusChangeListener(new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                // code to execute when EditText loses focus
                if(txthoraInicio.getText().toString().trim().length()<1) {
                    txthoraInicio.setText("00");
                }

                if(Integer.parseInt(txthoraInicio.getText().toString())<0 || Integer.parseInt(txthoraInicio.getText().toString())>23 || txthoraInicio.getText().toString().trim().length()<2 ){
                    //Log.i("FOCUS","ERROR: " + txthoraInicio.getText().toString() + "DEBE SER ENTRE 0 Y 23" );
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        txthoraInicio.setTextColor(Color.parseColor("#EF2525"));
                        txthoraInicio.setBackgroundTintList((ContextCompat.getColorStateList(AgendarActivity.this,R.color.colorTintAgendaError)));
                        Toast.makeText(AgendarActivity.this, "Ingrese una hora correcta", Toast.LENGTH_SHORT).show();
                    }else{
                        txthoraInicio.setTextColor(Color.parseColor("#202020"));
                    }
                }else{
                    txthoraInicio.setTextColor(Color.parseColor("#202020"));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        txthoraInicio.setBackgroundTintList((ContextCompat.getColorStateList(AgendarActivity.this,R.color.colorTintAgendaDefault)));
                    }
                }
            }
        }
    });

    EditText txtMinutosInicio = (EditText) findViewById(R.id.MinutosInicio);
    txtMinutosInicio.setOnFocusChangeListener(new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                if(txtMinutosInicio.getText().toString().trim().length()<1) {
                    txtMinutosInicio.setText("00");
                }
                // code to execute when EditText loses focus
                if(Integer.parseInt(txtMinutosInicio.getText().toString())<0 || Integer.parseInt(txtMinutosInicio.getText().toString())>59 || txtMinutosInicio.getText().toString().trim().length()<2 ){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        txtMinutosInicio.setTextColor(Color.parseColor("#EF2525"));
                        txtMinutosInicio.setBackgroundTintList((ContextCompat.getColorStateList(AgendarActivity.this,R.color.colorTintAgendaError)));
                        Toast.makeText(AgendarActivity.this, "Formato de minutos incorrecto", Toast.LENGTH_SHORT).show();
                    }else{
                        txtMinutosInicio.setTextColor(Color.parseColor("#202020"));
                    }
                }else{
                    txtMinutosInicio.setTextColor(Color.parseColor("#202020"));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        txtMinutosInicio.setBackgroundTintList((ContextCompat.getColorStateList(AgendarActivity.this,R.color.colorTintAgendaDefault)));
                    }
                }
            }
        }
    });
    EditText txthoraFin = (EditText) findViewById(R.id.horaFin);
    txthoraFin.setOnFocusChangeListener(new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                if(txthoraFin.getText().toString().trim().length()<1) {
                    txthoraFin.setText("00");
                }
                // code to execute when EditText loses focus
                if(Integer.parseInt(txthoraFin.getText().toString())<0 || Integer.parseInt(txthoraFin.getText().toString())>23 || txthoraFin.getText().toString().trim().length()<2 ){
                    //Log.i("FOCUS","ERROR: " + txthoraInicio.getText().toString() + "DEBE SER ENTRE 0 Y 23" );
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        txthoraFin.setTextColor(Color.parseColor("#EF2525"));
                        txthoraFin.setBackgroundTintList((ContextCompat.getColorStateList(AgendarActivity.this,R.color.colorTintAgendaError)));
                        Toast.makeText(AgendarActivity.this, "Ingrese una hora correcta", Toast.LENGTH_SHORT).show();
                    }else{
                        txthoraFin.setTextColor(Color.parseColor("#202020"));
                    }
                }else{
                    txthoraFin.setTextColor(Color.parseColor("#202020"));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        txthoraFin.setBackgroundTintList((ContextCompat.getColorStateList(AgendarActivity.this,R.color.colorTintAgendaDefault)));
                    }
                }
            }
        }
    });

    EditText txtMinutosFin = (EditText) findViewById(R.id.MinutosFin);
    txtMinutosFin.setOnFocusChangeListener(new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                if(txtMinutosFin.getText().toString().trim().length()<1) {
                    txtMinutosFin.setText("00");
                }
                // code to execute when EditText loses focus
                if(Integer.parseInt(txtMinutosFin.getText().toString())<0 || Integer.parseInt(txtMinutosFin.getText().toString())>59 || txtMinutosFin.getText().toString().trim().length()<2 ){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        txtMinutosFin.setTextColor(Color.parseColor("#EF2525"));
                        txtMinutosFin.setBackgroundTintList((ContextCompat.getColorStateList(AgendarActivity.this,R.color.colorTintAgendaError)));
                        Toast.makeText(AgendarActivity.this, "Formato de minutos incorrecto", Toast.LENGTH_SHORT).show();
                    }else{
                        txtMinutosFin.setTextColor(Color.parseColor("#202020"));
                    }
                }else{
                    txtMinutosFin.setTextColor(Color.parseColor("#202020"));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        txtMinutosFin.setBackgroundTintList((ContextCompat.getColorStateList(AgendarActivity.this,R.color.colorTintAgendaDefault)));
                    }
                }
            }
        }
    });

    //Botón volver a foto geolocalizacion
    final Button btnVolverInspJg = findViewById(R.id.btnVolverInspJg);
    btnVolverInspJg.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent intent = new Intent( AgendarActivity.this, FotoGeolocalizacion.class);
            intent.putExtra("id_inspeccion",id_inspeccion);
            startActivity(intent);
        }
    });

    //Botón Agendar
    final Button btnAgendar = findViewById(R.id.btnAgendarInspeccion);
    btnAgendar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mensajeError = "";

            if(direcion.getText().toString().trim().equals("")){
                mensajeError = "- Debe ingresar una dirección. <br/>";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    direcion.setBackgroundTintList((ContextCompat.getColorStateList(AgendarActivity.this,R.color.colorTintAgendaError)));
                }
            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    direcion.setBackgroundTintList((ContextCompat.getColorStateList(AgendarActivity.this,R.color.colorTintAgendaDefault)));
                }
            }

            if(region.getText().toString().trim().equals("")){
                mensajeError = mensajeError+"- Debe ingresar una Región. <br/>";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    region.setBackgroundTintList((ContextCompat.getColorStateList(AgendarActivity.this,R.color.colorTintAgendaError)));
                }
            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    region.setBackgroundTintList((ContextCompat.getColorStateList(AgendarActivity.this,R.color.colorTintAgendaDefault)));
                }
            }

            if(comunaa.getText().toString().trim().equals("")){
                mensajeError = mensajeError+"- Debe ingresar una comuna. <br/>";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    comunaa.setBackgroundTintList((ContextCompat.getColorStateList(AgendarActivity.this,R.color.colorTintAgendaError)));
                }
            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    comunaa.setBackgroundTintList((ContextCompat.getColorStateList(AgendarActivity.this,R.color.colorTintAgendaDefault)));
                }
            }


            /*calendarioAgenda.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                    fechaseleccionada = String.valueOf(dayOfMonth)+"/"+String.valueOf(month)+"/"+String.valueOf(year);
                    Log.i("fecha SELECCIONADA =>", fechaseleccionada);
                }
            });*/

            if(calendarioAgenda.toString().equals("")){
                mensajeError = mensajeError+"- Debe seleccionar una fecha. <br/>";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    calendarioAgenda.setBackgroundTintList((ContextCompat.getColorStateList(AgendarActivity.this,R.color.colorTintAgendaError)));
                }
            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    calendarioAgenda.setBackgroundTintList((ContextCompat.getColorStateList(AgendarActivity.this,R.color.colorTintAgendaDefault)));
                }
            }

            if(txthoraInicio.getText().toString().trim().equals("") || txtMinutosInicio.getText().toString().trim().equals("") || txthoraInicio.getText().toString().trim().length()<2 || txtMinutosInicio.getText().toString().trim().length()<2){
                mensajeError = mensajeError+"- Debe ingresar horario inicio de la cita. <br/>";
                txthoraInicio.requestFocus();
                txtMinutosInicio.requestFocus();
                txtComentarioAgenda.requestFocus();
            }
            if(txthoraFin.getText().toString().trim().equals("") || txtMinutosFin.getText().toString().trim().equals("") || txthoraFin.getText().toString().trim().length()<2 || txtMinutosFin.getText().toString().trim().length()<2){
                mensajeError = mensajeError+"- Debe ingresar horario fin de la cita. <br/>";
                txthoraFin.requestFocus();
                txtMinutosFin.requestFocus();
                txtComentarioAgenda.requestFocus();
            }

            if(txtComentarioAgenda.getText().toString().trim().equals("")){
                mensajeError = mensajeError+"- Debe ingresar un comentario. <br/>";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    txtComentarioAgenda.setBackgroundTintList((ContextCompat.getColorStateList(AgendarActivity.this,R.color.colorTintAgendaError)));
                }
            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    txtComentarioAgenda.setBackgroundTintList((ContextCompat.getColorStateList(AgendarActivity.this,R.color.colorTintAgendaDefault)));
                }
            }
            //Log.i("texto final =>", mensajeError);

            if(mensajeError.equals("")){

                int estadoOI = db.estadoInspeccion(Integer.parseInt(id_inspeccion));
                Log.e("Parametros agendamiento", "ESTADO 1° "+String.valueOf(estadoOI));

                //Log.i("ESTADO BTN", "TODO OK!");
                Intent servis2 = new Intent(AgendarActivity.this, AgendarInspeccion.class);
                servis2.putExtra("id_inspeccion", id_inspeccion);
                servis2.putExtra("direccion", direcion.getText().toString());
                servis2.putExtra("comuna", comunaa.getText().toString());
                servis2.putExtra("comentario", txtComentarioAgenda.getText().toString());
                servis2.putExtra("fecha_cita", fechaseleccionada);
                servis2.putExtra("hora_inicio", txthoraInicio.getText().toString());
                servis2.putExtra("minutos_inicio", txtMinutosInicio.getText().toString());
                servis2.putExtra("hora_fin", txthoraFin.getText().toString());
                servis2.putExtra("minutos_fin", txtMinutosFin.getText().toString());
                startService(servis2);

                Log.e("Parametros agendamiento", "ESTADO 2° "+String.valueOf(estadoOI));

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    /*se hace un "sleep" para que alcance a gatillzarse el servicio y actualizacion de estado, antes de volver a consultar y verificar que todo esta OK*/
                    @Override
                    public void run() {
                        int estadoOI2 = db.estadoInspeccion(Integer.parseInt(id_inspeccion));
                        Log.e("Parametros agendamiento", "ESTADO 3° "+String.valueOf(estadoOI2));

                        if(estadoOI2==4) {
                            /*cuando el estado es 4 osea si se gatillo el servicio de crear la cita, reiniciamos el estado de la oi a 0 como estaba en un comienzo.*/
                            db.cambiarEstadoInspeccion(Integer.parseInt(id_inspeccion),0);
                            int estadoOI3 = db.estadoInspeccion(Integer.parseInt(id_inspeccion));
                            Log.e("Parametros agendamiento", "ESTADO 4° "+String.valueOf(estadoOI3));

                            /*REDIRIGE AL MENU PRINCIPAL DONDE ESTAN LAS OI PENDIENTES...*/
                            Intent seccion = new Intent(AgendarActivity.this, InsPendientesActivity.class);
                            seccion.putExtra("id_inspeccion",id_inspeccion);
                            startActivity(seccion);
                            finish();
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(AgendarActivity.this);
                            builder.setCancelable(false);
                            builder.setTitle("LET Chile");
                            builder.setMessage(Html.fromHtml("Error al intentar agendar inspección, si el problema persiste contactarse con soporte."));
                            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }
                }, 2000);

            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(AgendarActivity.this);
                builder.setCancelable(false);
                builder.setTitle("LET Chile");
                builder.setMessage(Html.fromHtml(mensajeError));
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    });






    }


}
