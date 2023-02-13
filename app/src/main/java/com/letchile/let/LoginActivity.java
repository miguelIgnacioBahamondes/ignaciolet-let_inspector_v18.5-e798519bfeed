package com.letchile.let;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.Remoto.Data.LoginEnv;
import com.letchile.let.Remoto.Data.LoginResp;
import com.letchile.let.Remoto.InterfacePost;
import com.letchile.let.Servicios.ConexionInternet;
import com.letchile.let.Servicios.TransferirFoto;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class LoginActivity extends AppCompatActivity {

    ProgressDialog pDialog;
    DBprovider db;
    Boolean conexion = false;
    Button btnLogin;
    private final int MY_PERMISSIONS = 100;
    private LocationListener listener;
    private LocationManager locationManager;

    public LoginActivity(){
        db = new DBprovider(this);
    }

       //asd
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnlogin);

        if(mayRequestStoragePermission())
            btnLogin.setEnabled(true);
        else
            btnLogin.setEnabled(false);

        final EditText usuario = findViewById(R.id.usuarioM);
        final EditText password = findViewById(R.id.contrasenaM);

        final AlertDialog alert = null;
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );



            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(getString(R.string.URL_BASE))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            final InterfacePost servicio = retrofit.create(InterfacePost.class);


        pDialog= new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Autenticando...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);


        String usuarioAp = db.obtenerUsuario();
        if (usuarioAp.equals("")) {

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //comprobar si existe el usuario
                    conexion = new ConexionInternet(LoginActivity.this).isConnectingToInternet();
                    if (conexion) {

                        pDialog.show();

                        final LoginEnv loginEnv = new LoginEnv();

                        loginEnv.setUsr(usuario.getText().toString());
                        loginEnv.setPwd(password.getText().toString());

                        Call<LoginResp> loginRespCall = servicio.getAcceso(loginEnv.getUsr(), loginEnv.getPwd());

                        loginRespCall.enqueue(new Callback<LoginResp>() {
                            @Override
                            public void onResponse(Call<LoginResp> call, Response<LoginResp> response) {
                                int statusCode = response.code();
                                LoginResp loginResp = response.body();
                                Log.d("login", "onResponse " + statusCode);

                                if (loginResp.getMSJ().equals("3") || loginResp.getMSJ().equals("6")) {
                                    //Insertar usuario
                                    db.inserUsuario(loginEnv.getUsr(), loginEnv.getPwd(), Integer.parseInt(loginResp.getMSJ()));

                                    Calendar c = Calendar.getInstance();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String strDate = sdf.format(c.getTime());

                                    Log.i("FECHA:", strDate);

                                    //codigo gps

                                    listener = new LocationListener() {
                                        @Override

                                        public void onLocationChanged(Location location) {
                                            String latitude = String.valueOf(location.getLatitude());
                                            String longitud = String.valueOf(location.getLongitude());

                                            Log.d("latitude", latitude);
                                            Log.d("longitud", longitud);

                                        }

                                        @Override
                                        public void onStatusChanged(String s, int i, Bundle bundle) {

                                        }

                                        @Override
                                        public void onProviderEnabled(String s) {
                                            Log.d("GPS", "ENABLE");

                                        }

                                        @Override
                                        public void onProviderDisabled(String s) {
                                            Log.d("GPS", "DISABLE");
                                        }

                                    };




                                    // fin codigo gps

                                    /*try {
                                        File nuevaCarpeta = new File(Environment.getExternalStorageDirectory(), "Log_"+strDate);
                                        if (!nuevaCarpeta.exists()) {
                                            nuevaCarpeta.mkdir();
                                        }*/
                                        try {

                                            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "LogInspector_"+loginEnv.getUsr()+".txt");
                                            //File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "LogInspector.txt");

                                            file.createNewFile();

                                            String path= file.getAbsolutePath();

                                            FileOutputStream stream = new FileOutputStream(file);
                                            try {
                                                stream.write(("Inicia Sesión con el usuario: "+loginEnv.getUsr() + "\n\r").getBytes());

                                            } finally {
                                                stream.close();
                                            }
                                        } catch (Exception ex) {
                                            Log.e("Error", "ex: " + ex);
                                        }
                                   /* } catch (Exception e) {
                                        Log.e("Error", "e: " + e);
                                    }*/

                                    //iniciar aplicaciones
                                    Intent i = new Intent(LoginActivity.this, InsPendientesActivity.class);
                                    startActivity(i);
                                    finish();


                                } else {
                                    Toast.makeText(LoginActivity.this, "Usuario y/o contraseña incorrecta", Toast.LENGTH_SHORT).show();
                                }

                                pDialog.dismiss();
                            }

                            @Override
                            public void onFailure(Call<LoginResp> call, Throwable t) {
                                Log.e("login", "onFailure" + t.getMessage());
                                Toast.makeText(LoginActivity.this, "onFailure" + t.getMessage(), Toast.LENGTH_SHORT).show();
                                pDialog.dismiss();
                            }
                        });
                    } else {
                        Toast.makeText(LoginActivity.this, "No hay conexión", Toast.LENGTH_SHORT).show();
                    }
            }
        });

        }else{
            //si ya existe inicia automaticamente
            Intent i = new Intent(LoginActivity.this, InsPendientesActivity.class);
            startActivity(i);
        }
    }



    private boolean mayRequestStoragePermission() {

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        if((checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED))
            return true;

        requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
        // }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == MY_PERMISSIONS){
            if(grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(LoginActivity.this, "Permisos aceptados", Toast.LENGTH_SHORT).show();
                btnLogin.setEnabled(true);
            }
        }else{
            showExplanation();
        }
    }

    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Permisos denegados");
        builder.setMessage("Para usar las funciones de la app necesitas aceptar los permisos");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.show();
    }


}





















