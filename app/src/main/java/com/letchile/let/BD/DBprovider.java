package com.letchile.let.BD;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;


public class DBprovider extends SQLiteOpenHelper {
    // Johana 03-02-2020
    //instanciar la base de datos
    private static final int VERSION_BASE_DATOS = 15;
    private static final String NOMBRE_BASE_DATOS = "datosbase";

    //crear tablas
    private static final String TABLA_USUARIO = "CREATE TABLE USUARIO " +
            " ( usr TEXT PRIMARY KEY, pwd TEXT, perfil int )";

    private static final String TABLA_INSPECCION = "CREATE TABLE INSPECCION " +
            "(id_inspeccion INTEGER, asegurado TEXT, apellidoPaterno TEXT, apellidoMaterno TEXT, rut TEXT, comuna_cita TEXT," +
            "marca TEXT, modelo TEXT, patente TEXT, ramo TEXT, fecha_cita DATE, hora TEXT, fecha_inspeccion TEXT," +
            "fecha_cierre DATETIME, fono INTEGER, comuna TEXT, direccion TEXT, cia TEXT, corredor TEXT, comentario TEXT," +
            "direccion_cita TEXT, enviado INTEGER, pac TEXT, estado INTEGER, email TEXT, contador_oi INTEGER, obd INTEGER)";

    private static final String TABLA_INSPECCIONFLLD ="CREATE TABLE INSPECCION_FALLIDA " +
            "(id_inspeccion INTEGER, fecha DATE, comentario TEXT, id_fallida INTEGER, fecha_cita DATE, hora_cita TEXT, activo INTEGER)";

    private static final String TABLA_FOTO = "CREATE TABLE FOTO " +
            "(id_inspeccion INTEGER,id_foto INTEGER, foto TEXT, comentario TEXT,enviado INTEGER,url BLOB, contF INTEGER)";

    private static final String TABLA_GEO = "CREATE TABLE GEOLOCALIZACION " +
            "(id_inspeccion INTEGER,usr TEXT, fecha TEXT, latitud TEXT,longitud TEXT, direccion TEXT)";

    private static final String LOG_FOTO_DANO = "CREATE TABLE LOG_FDANO " +
            "(id_fdano INTEGER, id_inspeccion INTEGER, comentario TEXT, ubicacion TEXT)";

    private static final String TABLA_FOTOFLLD ="CREATE TABLE FOTO_FALLIDA " +
            "(id_inspeccion INTEGER, foto TEXT, fecha DATE, id_fallida  INTEGER, fechaHoraFallida DATETIME, enviado INTEGER, url BLOB, comentario TEXT, motivoFallida INTEGER)";

    //tx.executeSql('CREATE TABLE IF NOT EXISTS foto_fallida (id_inspeccion INTEGER, foto TEXT, fecha DATE, id_fallida  INTEGER, fechaHoraFallida DATETIME, enviado INTEGER, url BLOB, comentario TEXT)');

    private static final String TABLA_HITO = "CREATE TABLE HITO" +
            "(id_hito INTEGER, glosa TEXT, orden INTEGER)";

    private static final String TABLA_CIA = "CREATE TABLE CIA" +
            "(id_cia INTEGER, cia TEXT)";

    private static final String TABLA_BITACORA = "CREATE TABLE BITACORA" +
            "(id_inspeccion INTEGER, id_hito INTEGER, glosa TEXT, fecha TEXT, enviado INTEGER)";

    private static final String TABLA_COMUNA = "CREATE TABLE COMUNA(id_region INTEGER, region TEXT, comuna TEXT)";


    private static final String TABLA_VALOR = "CREATE TABLE VALOR (idInspeccion INTEGER, idCampo INTEGER, valor TEXT, estado INTEGER)";

    private static final String TABLA_OI_EMAIL = "CREATE TABLE OI_EMAIL" +
            "(id_inspeccion INTEGER, email TEXT)";


    private static final String TABLA_PIEZA = "CREATE TABLE PIEZA(idDdeducible INTEGER, idCampo INTEGER, pieza TEXT, ubicacion TEXT)";
    private static final String TABLA_DANIOS = "CREATE TABLE DANIOS(idDano INTEGER, dano TEXT)";
    private static final String TABLA_DEDUCIBLES = "CREATE TABLE DEDUCIBLES (tipoDanio INTEGER, valorDeducible TEXT,  glosaDeducible TEXT)";
    private static final String TABLA_CANTIDAD_FOTOS ="CREATE TABLE CANTIDAD_FOTOS " +
            "( id_inspeccion INTEGER, foto TEXT, cantidad INTEGER)";


    // AGREGADO POR NACHO
    private static final String TABLA_ESTADO_TRANSMISION_DATOS = "CREATE TABLE ESTADO_TRANSMISION_DATOS (id_inspeccion INTEGER, estado INTEGER)";
    private static final String TABLA_ESTADO_TRANSMISION_FECHA = "CREATE TABLE ESTADO_TRANSMISION_FECHA (id_inspeccion INTEGER, estado INTEGER)";
    private static final String TABLA_ESTADO_TRANSMISION_GEO = "CREATE TABLE ESTADO_TRANSMISION_GEO (id_inspeccion INTEGER, estado INTEGER)";

    // FIN AGREGADO POR NACHO



    public DBprovider(Context context) {
        super(context, NOMBRE_BASE_DATOS, null, VERSION_BASE_DATOS);
    }


    //CUANDO SE EJECUTA POR PRIMERA VEZ, SE CREA LA BASE DE DATOS Y LAS TABLAS CORRESPONDIENTES
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLA_USUARIO);
        db.execSQL(TABLA_INSPECCION);
        db.execSQL(TABLA_INSPECCIONFLLD);
        db.execSQL(TABLA_FOTO);
        db.execSQL(TABLA_FOTOFLLD);
        db.execSQL(TABLA_VALOR);
        db.execSQL(TABLA_HITO);
        db.execSQL(TABLA_BITACORA);
        db.execSQL(TABLA_COMUNA);
        db.execSQL(TABLA_CIA);
        db.execSQL(TABLA_PIEZA);
        db.execSQL(TABLA_DANIOS);
        db.execSQL(TABLA_DEDUCIBLES);
        db.execSQL(LOG_FOTO_DANO);
        db.execSQL(TABLA_CANTIDAD_FOTOS);
        db.execSQL(TABLA_GEO);
        db.execSQL(TABLA_OI_EMAIL);
        db.execSQL(TABLA_ESTADO_TRANSMISION_DATOS);
        db.execSQL(TABLA_ESTADO_TRANSMISION_FECHA);
        db.execSQL(TABLA_ESTADO_TRANSMISION_GEO);



        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (463,386,'Parachoque', 'posterior')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (151,148,'Maleta Portalon','posterior')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (132,131,'Foco Izquierdo','posterior')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (112,111,'Foco Derecho','posterior')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (424,422,'Chapa Manilla','posterior')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (110,109,'Luneta','posterior')");

        db.execSQL("INSERT INTO DANIOS (idDano, dano) VALUES (1, 'Abolladura')");
        db.execSQL("INSERT INTO DANIOS (idDano, dano) VALUES (2, 'Rayadura')");
        db.execSQL("INSERT INTO DANIOS (idDano, dano) VALUES (3, 'Rotura')");
        db.execSQL("INSERT INTO DANIOS (idDano, dano) VALUES (4, 'Trizadura')");
        db.execSQL("INSERT INTO DANIOS (idDano, dano) VALUES (5, 'Corrosión')");
        db.execSQL("INSERT INTO DANIOS (idDano, dano) VALUES (6, 'Oxidación')");
        db.execSQL("INSERT INTO DANIOS (idDano, dano) VALUES (7, 'Faltante')");
        db.execSQL("INSERT INTO DANIOS (idDano, dano) VALUES (8, 'Desabolladura defectuosa')");
        db.execSQL("INSERT INTO DANIOS (idDano, dano) VALUES (9, 'Pintura deteriorada')");
        db.execSQL("INSERT INTO DANIOS (idDano, dano) VALUES (10, 'Sin pintura')");
        db.execSQL("INSERT INTO DANIOS (idDano, dano) VALUES (11, 'Saltadura')");
        db.execSQL("INSERT INTO DANIOS (idDano, dano) VALUES (12, 'Desgaste')");

        db.execSQL("INSERT INTO DEDUCIBLES (tipoDanio, valorDeducible, glosaDeducible) VALUES (1, '2.0', 'LEVE')");
        db.execSQL("INSERT INTO DEDUCIBLES (tipoDanio, valorDeducible, glosaDeducible) VALUES (1, 'EXCL','GRAVE')");
        db.execSQL("INSERT INTO DEDUCIBLES (tipoDanio, valorDeducible, glosaDeducible) VALUES (1, '2.5','MEDIANA')");
        db.execSQL("INSERT INTO DEDUCIBLES (tipoDanio, valorDeducible, glosaDeducible) VALUES (2, '1.5','LEVE')");
        db.execSQL("INSERT INTO DEDUCIBLES (tipoDanio, valorDeducible, glosaDeducible) VALUES (2, '2.0','MEDIANA')");
        db.execSQL("INSERT INTO DEDUCIBLES (tipoDanio, valorDeducible, glosaDeducible) VALUES (2, 'EXCL', 'GRAVE')");
        db.execSQL("INSERT INTO DEDUCIBLES (tipoDanio, valorDeducible, glosaDeducible) VALUES (3, 'EXCL', 'GRAVE')");
        db.execSQL("INSERT INTO DEDUCIBLES (tipoDanio, valorDeducible, glosaDeducible) VALUES (4, 'EXCL', 'GRAVE')");
        db.execSQL("INSERT INTO DEDUCIBLES (tipoDanio, valorDeducible, glosaDeducible) VALUES (5, 'EXCL', 'GRAVE')");
        db.execSQL("INSERT INTO DEDUCIBLES (tipoDanio, valorDeducible, glosaDeducible) VALUES (6, '2.5',	'LEVE')");
        db.execSQL("INSERT INTO DEDUCIBLES (tipoDanio, valorDeducible, glosaDeducible) VALUES (6, '3.0',	'MEDIANA')");
        db.execSQL("INSERT INTO DEDUCIBLES (tipoDanio, valorDeducible, glosaDeducible) VALUES (6, 'EXCL', 'GRAVE')");
        db.execSQL("INSERT INTO DEDUCIBLES (tipoDanio, valorDeducible, glosaDeducible) VALUES (7, 'EXCL', 'GRAVE')");
        db.execSQL("INSERT INTO DEDUCIBLES (tipoDanio, valorDeducible, glosaDeducible) VALUES (8, 'EXCL', 'GRAVE')");
        db.execSQL("INSERT INTO DEDUCIBLES (tipoDanio, valorDeducible, glosaDeducible) VALUES (9, '1.0',	'LEVE')");
        db.execSQL("INSERT INTO DEDUCIBLES (tipoDanio, valorDeducible, glosaDeducible) VALUES (9, '1.5',	'MEDIANA')");
        db.execSQL("INSERT INTO DEDUCIBLES (tipoDanio, valorDeducible, glosaDeducible) VALUES (9, '2.0',	'GRAVE')");
        db.execSQL("INSERT INTO DEDUCIBLES (tipoDanio, valorDeducible, glosaDeducible) VALUES (10, 'EXCL', 'GRAVE')");
        db.execSQL("INSERT INTO DEDUCIBLES (tipoDanio, valorDeducible, glosaDeducible) VALUES (11, '1.5', 'LEVE')");
        db.execSQL("INSERT INTO DEDUCIBLES (tipoDanio, valorDeducible, glosaDeducible) VALUES (11, '2.0', 'MEDIANA')");
        db.execSQL("INSERT INTO DEDUCIBLES (tipoDanio, valorDeducible, glosaDeducible) VALUES (11, '2.5', 'GRAVE')");
        db.execSQL("INSERT INTO DEDUCIBLES (tipoDanio, valorDeducible, glosaDeducible) VALUES (12, '1.0', 'LEVE')");
        db.execSQL("INSERT INTO DEDUCIBLES (tipoDanio, valorDeducible, glosaDeducible) VALUES (12, '1.5', 'MEDIANA')");
        db.execSQL("INSERT INTO DEDUCIBLES (tipoDanio, valorDeducible, glosaDeducible) VALUES (12, '2.0', 'GRAVE')");


        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (70,69,'Tapabarro','lateral_izquierdo')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (72,71,'Pilar Lateral DI','lateral_izquierdo')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (122,121,'Pilar Lateral TI','lateral_izquierdo')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (68,67,'Puerta Lateral DI','lateral_izquierdo')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (118,117,'Puerta Lateral TI','lateral_izquierdo')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (82,81,'Espejo','lateral_izquierdo')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (465,464,'Vidrio Aleta','lateral_izquierdo')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (80,79,'Vidrio Puerta Lateral DI','lateral_izquierdo')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (130,129,'Vidrio Puerta Lateral TI','lateral_izquierdo')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (90,89,'Manillas Exteriores Lateral DI','lateral_izquierdo')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (134,133,'Manillas Exteriores Laterales TI','lateral_izquierdo')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (92,91,'Manillas Interiores Laterales DI','lateral_izquierdo')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (136,135,'Manillas Interiores Laterales TI','lateral_izquierdo')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (76,75,'Molduras Laterales DI','lateral_izquierdo')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (126,125,'Molduras Laterales TI','lateral_izquierdo')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (78,77,'Zocalo Lateral DI','lateral_izquierdo')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (128,127,'Zocalo Lateral TI','lateral_izquierdo')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (74,73,'Costado','lateral_izquierdo')");

        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (96,95,'Tapabarro','lateral_derecho')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (30,29,'Pilar Lateral DD'	,'lateral_derecho')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (98,97,'Pilar Lateral TD','lateral_derecho')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (24,23,'Puerta Lateral DD','lateral_derecho')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (94,93,'Puerta Lateral TD', 'lateral_derecho')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (51,50,'Espejo','lateral_derecho')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (465,464,'Vidrio Aleta Lateral DD','lateral_derecho')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (45,44,'Vidrio Puerta Lateral DD','lateral_derecho')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (108,107,'Vidrio Puerta Lateral TD','lateral_derecho')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (63,62,'Manillas Exteriores Lateral DD','lateral_derecho')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (114,113,'Manillas Exteriores Lateral TD','lateral_derecho')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (66,65,'Manillas Interiores Lateral DD','lateral_derecho')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (116,115,'Manillas Interiores Lateral TD','lateral_derecho')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (36,35,'Molduras Lateral DD','lateral_derecho')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (102,101,'Molduras Lateral TD','lateral_derecho')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (39,38,'Zocalo Lateral DD','lateral_derecho')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (104,103,'Zocalo Lateral TD','lateral_derecho')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (33,32,'Costado','lateral_derecho')");

        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (482,481,'Parachoque','frontal')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (146,143,'Capot','frontal')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (48,47,'Parabrisa','frontal')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (156,153,'Mascara','frontal')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (804,802,'Optico Izquierdo','frontal')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (807,805,'Optico Derecho','frontal')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (810,809,'Neblinero Derecho','frontal')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (813,812,'Neblinero Izquierdo','frontal')");
       // db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (176,173,'Tapiz','frontal')");
        db.execSQL("INSERT INTO PIEZA (idDdeducible,idCampo, pieza, ubicacion) VALUES (770,769,'Techo','techo')");

    }

    //en caso de que se necesite actualizar la base de datos
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldDb, int newDb) {

        db.execSQL("DROP TABLE IF EXISTS USUARIO");
        db.execSQL("DROP TABLE IF EXISTS INSPECCION");
        db.execSQL("DROP TABLE IF EXISTS INSPECCION_FALLIDA");
        db.execSQL("DROP TABLE IF EXISTS FOTO");
        db.execSQL("DROP TABLE IF EXISTS LOG_FDANO");
        db.execSQL("DROP TABLE IF EXISTS FOTO_FALLIDA");
        db.execSQL("DROP TABLE IF EXISTS VALOR");
        db.execSQL("DROP TABLE IF EXISTS HITO");
        db.execSQL("DROP TABLE IF EXISTS BITACORA");
        db.execSQL("DROP TABLE IF EXISTS COMUNA");
        db.execSQL("DROP TABLE IF EXISTS PIEZA");
        db.execSQL("DROP TABLE IF EXISTS DANIOS");
        db.execSQL("DROP TABLE IF EXISTS DEDUCIBLES");
        db.execSQL("DROP TABLE IF EXISTS CANTIDAD_FOTOS");
        db.execSQL("DROP TABLE IF EXISTS ESTADO_TRANSMISION_DATOS");
        db.execSQL("DROP TABLE IF EXISTS ESTADO_TRANSMISION_FECHA");
        db.execSQL("DROP TABLE IF EXISTS ESTADO_TRANSMISION_GEO");
        // agregado por nacho
        db.execSQL("DROP TABLE IF EXISTS CIA");
        db.execSQL("DROP TABLE IF EXISTS LOG_FDANO");
        db.execSQL("DROP TABLE IF EXISTS GEOLOCALIZACION");
        db.execSQL("DROP TABLE IF EXISTS OI_EMAIL");
        // fin agregado por nacho

        //db.execSQL("DROP TABLE IF EXISTS DEDUCIBLES_CIAS");
        onCreate(db);
    }

    public String EliminarDatosTablaDB (){
        SQLiteDatabase db = getWritableDatabase();
        String respuesta = "";

        try {
           if (db != null) {
               db.execSQL("DELETE FROM INSPECCION");
               //db.execSQL("DELETE FROM USUARIO");
               db.execSQL("DELETE FROM INSPECCION_FALLIDA");
               db.execSQL("DELETE FROM FOTO");
               db.execSQL("DELETE FROM LOG_FDANO");
               db.execSQL("DELETE FROM FOTO_FALLIDA");
               db.execSQL("DELETE FROM VALOR");
               db.execSQL("DELETE FROM HITO");
               db.execSQL("DELETE FROM BITACORA");
               db.execSQL("DELETE FROM COMUNA");
               //db.execSQL("DELETE FROM PIEZA");
               //db.execSQL("DELETE FROM DANIOS");
               //db.execSQL("DELETE FROM DEDUCIBLES");
               db.execSQL("DELETE FROM CANTIDAD_FOTOS");
               db.execSQL("DELETE FROM ESTADO_TRANSMISION_DATOS");
               db.execSQL("DELETE FROM ESTADO_TRANSMISION_FECHA");
               db.execSQL("DELETE FROM ESTADO_TRANSMISION_GEO");
               //db.execSQL("DELETE FROM CIA");
               db.execSQL("DELETE FROM LOG_FDANO");
               db.execSQL("DELETE FROM GEOLOCALIZACION");
               db.execSQL("DELETE FROM OI_EMAIL");
           }
           db.close();
           respuesta ="Ok";

       }
       catch (Exception ex) {
           respuesta ="Error: "+ex.getMessage();
       }

        return respuesta;
    }


// AGREGADO POR NACHO
    //INSERTA RESPUESTA TRANSMISION DE DATOS DE LA OI
    public String insertRegistroTransDatos(int id_inspeccion, int estado) {
        String respuesta = "";

        //PARA LEER Y SACAR EL NÚMERO DE COLUMNAS
        SQLiteDatabase dbl = getReadableDatabase();
        Cursor ars = dbl.rawQuery("SELECT * FROM ESTADO_TRANSMISION_DATOS WHERE id_inspeccion =" + id_inspeccion , null);
        Integer numero = ars.getCount();


        //INSERTAR O ACTUALIZAR DEPEDIENDO SI EXISTE O NO EN LA TABLA
        SQLiteDatabase db = getWritableDatabase();
        if (ars.getCount() > 0) {
            db.execSQL("UPDATE ESTADO_TRANSMISION_DATOS SET estado = "+ estado +" WHERE id_inspeccion= " + id_inspeccion);
            respuesta = "Ok";
        } else {
            if (db != null) {
                db.execSQL("INSERT INTO ESTADO_TRANSMISION_DATOS (id_inspeccion,estado) VALUES (" + id_inspeccion + "," + estado + ")");
                respuesta = "Ok";
            }
        }
        return respuesta;
    }
    //INSERTA RESPUESTA TRANSMISION DE FECHA DE LA OI
    public String insertRegistroTransFecha(int id_inspeccion, int estado) {
        String respuesta = "";

        //PARA LEER Y SACAR EL NÚMERO DE COLUMNAS
        SQLiteDatabase dbl = getReadableDatabase();
        Cursor ars = dbl.rawQuery("SELECT * FROM ESTADO_TRANSMISION_FECHA WHERE id_inspeccion =" + id_inspeccion , null);
        Integer numero = ars.getCount();


        //INSERTAR O ACTUALIZAR DEPEDIENDO SI EXISTE O NO EN LA TABLA
        SQLiteDatabase db = getWritableDatabase();
        if (ars.getCount() > 0) {
            db.execSQL("UPDATE ESTADO_TRANSMISION_FECHA SET estado = "+ estado +" WHERE id_inspeccion=" + id_inspeccion);
            respuesta = "Ok";
        } else {
            if (db != null) {
                db.execSQL("INSERT INTO ESTADO_TRANSMISION_FECHA (id_inspeccion,estado) VALUES (" + id_inspeccion + "," + estado + ")");
                respuesta = "Ok";
            }
        }
        return respuesta;
    }

    //INSERTA RESPUESTA TRANSMISION DE GEOLOCALIZACION DE LA OI
    public String insertRegistroTransGeo(int id_inspeccion, int estado) {
        String respuesta = "";

        //PARA LEER Y SACAR EL NÚMERO DE COLUMNAS
        SQLiteDatabase dbl = getReadableDatabase();
        Cursor ars = dbl.rawQuery("SELECT * FROM ESTADO_TRANSMISION_GEO WHERE id_inspeccion =" + id_inspeccion , null);
        Integer numero = ars.getCount();


        //INSERTAR O ACTUALIZAR DEPEDIENDO SI EXISTE O NO EN LA TABLA
        SQLiteDatabase db = getWritableDatabase();
        if (ars.getCount() > 0) {
            db.execSQL("UPDATE ESTADO_TRANSMISION_GEO SET estado = "+ estado +" WHERE id_inspeccion=" + id_inspeccion);
            respuesta = "Ok";
        } else {
            if (db != null) {
                db.execSQL("INSERT INTO ESTADO_TRANSMISION_GEO (id_inspeccion,estado) VALUES (" + id_inspeccion + "," + estado + ")");
                respuesta = "Ok";
            }
        }
        return respuesta;
    }

    // CONSULTA EL ESTADO DE TRANSFERENCIA DE LOS DATOS PARA LA OI
    public String ConsultarTransDatos(int id_inspeccion) {
        String respuesta = "";
        SQLiteDatabase db = getReadableDatabase();
        Cursor aRS = db.rawQuery("SELECT estado FROM ESTADO_TRANSMISION_DATOS WHERE ID_INSPECCION = "+id_inspeccion + " AND estado = 1", null);
        if (aRS.getCount() > 0) {
            aRS.moveToFirst();
            respuesta = "Pendientes";
        }
        else
        {
            respuesta = "Ok";
        }
        aRS.close();
        db.close();
        return respuesta;
    }

    // CONSULTA EL ESTADO DE TRANSFERENCIA DE LA FECHA PARA LA OI
    public String ConsultarTransFecha(int id_inspeccion) {
        String respuesta = "";
        SQLiteDatabase db = getReadableDatabase();
        Cursor aRS = db.rawQuery("SELECT estado FROM ESTADO_TRANSMISION_FECHA WHERE ID_INSPECCION = "+id_inspeccion + " AND estado = 1", null);
        if (aRS.getCount() > 0) {
            aRS.moveToFirst();
            respuesta = "Pendientes";
        }
        else
        {
            respuesta = "Ok";
        }
        aRS.close();
        db.close();
        return respuesta;
    }

    // CONSULTA EL ESTADO DE TRANSFERENCIA DE LA GEO PARA LA OI
    public String ConsultarTransGeo(int id_inspeccion) {
        String respuesta = "";
        SQLiteDatabase db = getReadableDatabase();
        Cursor aRS = db.rawQuery("SELECT estado FROM ESTADO_TRANSMISION_GEO WHERE ID_INSPECCION = "+id_inspeccion + " AND estado = 1", null);
        if (aRS.getCount() > 0) {
            aRS.moveToFirst();
            respuesta = "Pendientes";
        }
        else
        {
            respuesta = "Ok";
        }
        aRS.close();
        db.close();
        return respuesta;
    }


// FIN AGREGADO NACHO

    //MÉTODOS PARA EL USUARIO
    public void inserUsuario(String usr, String pwd,int perfil){
        SQLiteDatabase db = getWritableDatabase();

        if(db != null)
        {
            db.execSQL("INSERT INTO USUARIO (usr,pwd,perfil) VALUES('"+usr+"','"+pwd+"',"+perfil+");");
        }
        db.close();
    }


    //verifica usuario
    public String obtenerUsuario() {
        String respuesta = "";
        SQLiteDatabase db = getReadableDatabase();
        Cursor aRS = db.rawQuery("SELECT usr FROM USUARIO", null);
        if (aRS.getCount() > 0) {
            aRS.moveToFirst();
            respuesta = aRS.getString(0);
        }
        aRS.close();
        db.close();
        return respuesta;
    }


    //INSERTA OI Y EMAIL
    public String insertaOiEmail(int id_inspeccion, String email) {
        String respuesta = "";
        ContentValues valores = new ContentValues();
        valores.put("id_inspeccion", id_inspeccion);
        valores.put("email", email);


        //PARA LEER Y SACAR EL NÚMERO DE COLUMNAS
        SQLiteDatabase dbl = getReadableDatabase();
        Cursor ars = dbl.rawQuery("SELECT * FROM OI_EMAIL WHERE id_inspeccion =" + id_inspeccion , null);
        Integer numero = ars.getCount();


       // respuesta = "Insertado" + numero;
        //INSERTAR O ACTUALIZAR DEPEDIENDO SI EXISTE O NO EN LA INSPECCIÓN
        SQLiteDatabase db = getWritableDatabase();
       if (ars.getCount() > 0) {
            db.execSQL("UPDATE OI_EMAIL SET id_inspeccion = " + id_inspeccion + ", email = '" + email + "' WHERE id_inspeccion=" + id_inspeccion);
            respuesta = "Actualizado" + valores ;
        } else {
            if (db != null) {
                db.execSQL("INSERT INTO OI_EMAIL (id_inspeccion,email) VALUES (" + id_inspeccion + ",'" + email + "')");
                respuesta = "Insertado" + valores;
            }
        }
        return respuesta;
    }

    //OBTENER DATOS DE OI EMAIL
    public String DatosOiEmail( String id_inspeccion) {
        String respuesta = "";
        String[][] aData = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor aRS = db.rawQuery("SELECT email FROM OI_EMAIL WHERE id_inspeccion =" + id_inspeccion , null);
        if (aRS.getCount() > 0) {
            aRS.moveToFirst();
            respuesta = aRS.getString(0);
        }
        aRS.close();
        db.close();
        return respuesta;
    }


    //INSERTA INSPECCIONES RESCATADAS DEL SERVIDOR
    public String insertaInspecciones(Integer id_inspeccion, String asegurado, String paternoAsegurado, String maternoAsegurado, String rut, String comunaAsegurado,
                                      String direccionAsegurado, Integer fono, String marca, String modelo, String direccionCita, String fechaCita, String horaCita,
                                      String comunaCita, String comentarioCita, String idRamo, String patente, String cia, String corredor, String pac, String email, Integer obd) {
        String resp = "";
        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            try {
                db.execSQL("INSERT INTO INSPECCION (id_inspeccion,asegurado,apellidoPaterno,apellidoMaterno,rut,comuna,direccion,fono,marca,modelo,direccion_cita,fecha_cita," +
                        "hora,comuna_cita,comentario,ramo,patente,cia,corredor,pac,enviado,estado, contador_oi, email,obd)" +
                        " VALUES(" + id_inspeccion + ",'" + asegurado + "','" + paternoAsegurado + "','" + maternoAsegurado + "','" + rut + "','" + comunaAsegurado + "','" + direccionAsegurado + "'," + fono + ",'" + marca + "','" + modelo + "'," +
                        "'" + direccionCita + "','" + fechaCita + "','" + horaCita + "','" + comunaCita + "','" + comentarioCita + "','" + idRamo + "','" + patente + "'," +
                        "'" + cia + "','" + corredor + "','" + pac + "'," + 0 + "," + 0 + "," + 0 + ", '"+email+"'," + obd +");");

                resp = "Ok";
            } catch (Exception e) {
                resp = "Error: " + e.getMessage();
            }
        } else {
            resp = "no hay base de datos";
        }
        db.close();
        return resp;
    }


    //lista de inspecciones que no esten enviadas
    public String[][] listaInspecciones() {
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        String[][] aData = null;
        Cursor aRS = db.rawQuery("SELECT * FROM INSPECCION WHERE estado in (0,1) ORDER BY fecha_cita asc,hora asc", null);

        if (aRS.getCount() > 0) {
            aData = new String[aRS.getCount()][];
            while (aRS.moveToNext()) {
                aData[count] = new String[8];
                aData[count][0] = Integer.toString(aRS.getInt(aRS.getColumnIndex("id_inspeccion")));
                aData[count][1] = aRS.getString(aRS.getColumnIndex("fecha_cita"));
                aData[count][2] = aRS.getString(aRS.getColumnIndex("hora"));
                aData[count][3] = aRS.getString(aRS.getColumnIndex("ramo"));
                aData[count][4] = aRS.getString(aRS.getColumnIndex("direccion"));
                aData[count][5] = aRS.getString(aRS.getColumnIndex("comuna"));
                aData[count][6] = aRS.getString(aRS.getColumnIndex("patente"));
                aData[count][7] = aRS.getString(aRS.getColumnIndex("enviado"));
                count++;
            }
        } else {
            aData = new String[0][];
        }
        aRS.close();
        db.close();
        return (aData);
    }

    //lista datos inspeccion x id_inspeccion
    public String[][] BuscaDatosInspeccion(String id_inspeccion) {
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        String[][] aData = null;

        Cursor aRs = db.rawQuery("SELECT * FROM INSPECCION where id_inspeccion=" + Integer.parseInt(id_inspeccion), null);
        if (aRs.getCount() > 0) {
            aData = new String[aRs.getCount()][];
            while (aRs.moveToNext()) {
                aData[count] = new String[17];
                aData[count][0] = Integer.toString(aRs.getInt(aRs.getColumnIndex("id_inspeccion")));
                aData[count][1] = aRs.getString(aRs.getColumnIndex("asegurado"));
                aData[count][2] = Integer.toString(aRs.getInt(aRs.getColumnIndex("fono")));
                aData[count][3] = aRs.getString(aRs.getColumnIndex("comentario"));
                aData[count][4] = aRs.getString(aRs.getColumnIndex("direccion"));
                aData[count][5] = aRs.getString(aRs.getColumnIndex("comuna"));
                aData[count][6] = aRs.getString(aRs.getColumnIndex("patente"));
                aData[count][7] = aRs.getString(aRs.getColumnIndex("ramo"));
                aData[count][8] = aRs.getString(aRs.getColumnIndex("apellidoPaterno"));
                aData[count][9] = aRs.getString(aRs.getColumnIndex("apellidoMaterno"));
                aData[count][10] = aRs.getString(aRs.getColumnIndex("rut"));
                aData[count][11] = aRs.getString(aRs.getColumnIndex("email"));
                aData[count][12] = aRs.getString(aRs.getColumnIndex("pac"));
                aData[count][13] = aRs.getString(aRs.getColumnIndex("marca"));
                aData[count][14] = aRs.getString(aRs.getColumnIndex("modelo"));
                aData[count][15] = aRs.getString(aRs.getColumnIndex("cia"));
                aData[count][16] = aRs.getString(aRs.getColumnIndex("contador_oi"));
                //aData[count][17] = aRs.getString(aRs.getColumnIndex("email"));
                count++;
            }
        } else {
            aData = new String[0][];
        }
        aRs.close();
        db.close();
        return (aData);
    }

    //borrar registros de la tabla inspeccion
    public void borrarInspeccion(int id_inspeccion) {
        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            db.execSQL("DELETE FROM INSPECCION WHERE id_inspeccion=" + id_inspeccion);
          /* db.execSQL("DELETE FROM GEOLOCALIZACION WHERE id_inspeccion=" + id_inspeccion);
            db.execSQL("DELETE FROM OI_EMAIL WHERE id_inspeccion=" + id_inspeccion);
            db.execSQL("DELETE FROM FOTO WHERE id_inspeccion=" + id_inspeccion);
            db.execSQL("DELETE FROM CANTIDAD_FOTOS WHERE id_inspeccion=" + id_inspeccion);*/

        }
        db.close();
    }


    //borrar registros de la tabla comuna
    public void borrarComunas() {
        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            db.execSQL("DELETE FROM COMUNA");
        }
        db.close();
    }



    //Insertar comunas
    public String insertarComuna(int id_region, String region, String comuna) {
        String resp = "";
        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            db.execSQL("INSERT INTO COMUNA (id_region,region,comuna) VALUES (" + id_region + ",'" + region + "','" + comuna + "')");
            resp = "Ok";
        } else {
            resp = "No";
        }
        db.close();
        return resp;
    }



    //lista total de comunas y regiones
    public Integer listatotalComunasRegiones() {
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();

        //db.rawQuery("SELECT COUNT(comuna) FROM COMUNA", null);
        long i = DatabaseUtils.queryNumEntries(db, "COMUNA");

        count = safeLongToInt(i);

        db.close();
        return count;
    }


    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }


    //SON MUCHOS REGIONES PARA DISTINTOS ID_COMUNA
    public String[][] listaRegiones() {
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        String[][] aData = null;
        Cursor aRS = db.rawQuery("SELECT DISTINCT region FROM COMUNA", null);

        Integer numer = aRS.getCount();
        if (aRS.getCount() > 0) {
            aData = new String[aRS.getCount()][];
            while (aRS.moveToNext()) {
                aData[count] = new String[3];
                aData[count][0] = aRS.getString(aRS.getColumnIndex("region"));
                count++;
            }
        } else {
            aData = new String[0][];
        }
        aRS.close();
        db.close();
        return (aData);
    }


    //SI SE ACTUALIZAN LAS COMUNAS (OSEA MÁS 1)SE BORRAN TODAS LAS COMUNAS Y SE VUELVE A CARGAR
    //LISTA TODAS LAS COMUNAS
    public String[][] listaComunas(String region) {
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        String[][] aData = null;
        Cursor aRS = db.rawQuery("SELECT DISTINCT comuna FROM COMUNA WHERE region = '" + region + "'", null);

        if (aRS.getCount() > 0) {
            aData = new String[aRS.getCount()][];
            while (aRS.moveToNext()) {
                aData[count] = new String[3];
                aData[count][0] = aRS.getString(aRS.getColumnIndex("comuna"));
                count++;
            }
        } else {
            aData = new String[0][];
        }
        aRS.close();
        db.close();
        return (aData);
    }




    public void actualizarAsegInspeccion(Integer id_inspeccion, String asegurado, String apellidoP, String apellidoM, String rut, String direccion,
                                         Integer fono, String email, String comuna) {
        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            db.execSQL("UPDATE INSPECCION SET asegurado = '" + asegurado + "', apellidoPaterno = '" + apellidoP + "', apellidoMaterno ='" + apellidoM + "'," +
                    "rut = '" + rut + "', direccion='" + direccion + "', fono=" + fono + ", email='" + email + "', comuna='" + comuna + "' WHERE id_inspeccion=" + id_inspeccion);
        }
        db.close();
    }

    //idInspeccion INTEGER, idCampo INTEGER, valor TEXT, estado INTEGER
    public String insertarValor(int id_inspeccion, int id_campo, String valor) {
        String respuesta = "";
        ContentValues valores = new ContentValues();
        valores.put("idInspeccion", id_inspeccion);
        valores.put("idCampo", id_campo);
        valores.put("valor", valor);
        valores.put("estado", 0);

        //PARA LEER Y SACAR EL NÚMERO DE COLUMNAS
        SQLiteDatabase dbl = getReadableDatabase();
        Cursor ars = dbl.rawQuery("SELECT * FROM VALOR WHERE idInspeccion =" + id_inspeccion + " and idCampo=" + id_campo, null);
        Integer numero = ars.getCount();

        //INSERTAR O ACTUALIZAR DEPEDIENDO SI EXISTE O NO EN LA INSPECCIÓN
        SQLiteDatabase db = getWritableDatabase();
        if (ars.getCount() > 0) {
            db.update("VALOR", valores, "idInspeccion=" + id_inspeccion + " and idCampo=" + id_campo, null);
            respuesta = "Actualizado";
        } else {
            if (db != null) {
                db.insert("VALOR", null, valores);
                respuesta = "Insertado";
            }
        }
        return respuesta;
    }


    //idInspeccion INTEGER, idCampo INTEGER, valor TEXT, estado INTEGER
    public String listaAccesorios(int id_inspeccion, int id_campo) {
        String respuesta = "";
        SQLiteDatabase dbl = getReadableDatabase();

        int count = 0;
        String[][] aData = null;

        Cursor ars = dbl.rawQuery("SELECT valor FROM VALOR where idInspeccion=" + id_inspeccion + " and idCampo=" + id_campo, null);
        Integer numero = ars.getCount();

        if (ars.getCount() > 0) {
            if (ars.getCount() > 0) {
                aData = new String[ars.getCount()][];
                while (ars.moveToNext()) {
                    aData[count] = new String[4];
                    aData[count][0] = ars.getString(ars.getColumnIndex("valor"));
                }
            }
        }
        return respuesta;
    }


    public String accesorio(int id_inspeccion, int id_campo) {
        String rsp = "";
        SQLiteDatabase db = getReadableDatabase();
        Cursor ars = db.rawQuery("SELECT valor FROM VALOR where idInspeccion=" + id_inspeccion + " and idCampo=" + id_campo, null);
        if (ars.moveToFirst()) {
            rsp = ars.getString(ars.getColumnIndex("valor"));
        }
        return rsp;
    }

    public String consultaPAC(int id_inspeccion) {
        String rsp = "";
        SQLiteDatabase db = getReadableDatabase();
        Cursor ars = db.rawQuery("SELECT pac FROM INSPECCION where id_inspeccion=" + id_inspeccion, null);
        if (ars.moveToFirst()) {
            rsp = ars.getString(ars.getColumnIndex("pac"));
        }
       return rsp;
    }

    //RAMO
    public void actualizaRamo(int id_inspeccion, String comboRamo) {

        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            db.execSQL("UPDATE INSPECCION SET ramo = '" + comboRamo + "' WHERE id_inspeccion=" + id_inspeccion);
        }
        db.close();
    }

    //ACTUALIZA CONTADOR
    public void actualizaContador(int id_inspeccion, int contador_oi) {

        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            db.execSQL("UPDATE INSPECCION SET contador_oi = '" + contador_oi + "' WHERE id_inspeccion=" + id_inspeccion);
        }
        db.close();
    }


    //FOTOS
    public String foto(int id_inspeccion, String comentario) {

        String rsp = "";
        SQLiteDatabase db = getReadableDatabase();
        Cursor ars = db.rawQuery("SELECT url FROM FOTO where id_inspeccion=" + id_inspeccion + " and comentario ='" + comentario +"' ORDER BY id_foto desc LIMIT 1", null);
        if (ars.moveToFirst()) {
            rsp = ars.getString(ars.getColumnIndex("url"));
        }
        return rsp;
    }






    //FOTOS
    public String fotoFallida(int id_inspeccion) {

        String rsp = "";
        SQLiteDatabase db = getReadableDatabase();
        Cursor ars = db.rawQuery("SELECT url FROM FOTO_FALLIDA where id_inspeccion=" + id_inspeccion + " ORDER BY foto desc LIMIT 1", null);
        if (ars.moveToFirst()) {
            rsp = ars.getString(ars.getColumnIndex("url"));
        }
        return rsp;
    }




    public String insertaFoto(int id_inspeccion, int id_foto, String imageName, String comentario, int estado, String url, int  contF) {
        String respuesta = "";
        ContentValues valores = new ContentValues();
        valores.put("id_inspeccion", id_inspeccion);
        valores.put("id_foto", id_foto);
        valores.put("foto", imageName);
        valores.put("comentario", comentario);
        valores.put("enviado", 1);
        valores.put("url", url);
        valores.put("contF", 0);

        //PARA LEER Y SACAR EL NÚMERO DE COLUMNAS
        SQLiteDatabase dbl = getReadableDatabase();
        Cursor ars = dbl.rawQuery("SELECT * FROM FOTO WHERE id_inspeccion =" + id_inspeccion + " and id_foto=" + id_foto, null);
        Integer numero = ars.getCount();

        //INSERTAR O ACTUALIZAR DEPEDIENDO SI EXISTE O NO EN LA INSPECCIÓN
        SQLiteDatabase db = getWritableDatabase();
        if (ars.getCount() > 0) {
            db.update("FOTO", valores, "id_inspeccion=" + id_inspeccion + " and id_foto=" + id_foto, null);
            respuesta = "Actualizado";
        } else {
            if (db != null) {
                db.insert("FOTO", null, valores);
                respuesta = "Insertado";
            }
        }
        return respuesta;
    }

    //Inserta geolocalizacion
    public String insertaGeolocalizacion(int id_inspeccion, String usr, String fecha, String latitud, String longitud, String direccion) {
        String respuesta = "";
        ContentValues valores = new ContentValues();
        valores.put("id_inspeccion", id_inspeccion);
        valores.put("usr", usr);
        valores.put("fecha", fecha);
        valores.put("latitud", latitud);
        valores.put("longitud", longitud);
        valores.put("direccion", direccion);

        //PARA LEER Y SACAR EL NÚMERO DE COLUMNAS
        SQLiteDatabase dbl = getReadableDatabase();
        Cursor ars = dbl.rawQuery("SELECT * FROM GEOLOCALIZACION WHERE id_inspeccion =" + id_inspeccion , null);
        Integer numero = ars.getCount();

        //respuesta="aqui" +numero;

        //INSERTAR O ACTUALIZAR DEPEDIENDO SI EXISTE O NO EN LA INSPECCIÓN
       SQLiteDatabase db = getWritableDatabase();
       /* if (numero > 0) {

            db.execSQL("UPDATE GEOLOCALIZACION SET id_inspeccion = " + id_inspeccion + ", usr = '" + usr + "', fecha ='" + fecha + "', latitud = '" + latitud + "', longitud = '" + longitud + "', direccion = '" + direccion+ "'   WHERE id_inspeccion=" + id_inspeccion );
            respuesta = "Actualizado " + numero;
        } else {
            if (db != null) {*/
                 db.execSQL("INSERT INTO GEOLOCALIZACION (id_inspeccion,usr,fecha,latitud,longitud,direccion) VALUES (" + id_inspeccion + ",'" + usr + "','" + fecha + "','" + latitud + "','" + longitud + "','" + direccion + "');");
                respuesta = "Insertado";
            //}
      // }
        return respuesta;
    }

    //inserta cantidad de fotos
    public String insertCantidadFoto(int id_inspeccion, String foto, int cantidad){

        String resp = "";
        ContentValues valores = new ContentValues();
        valores.put("id_inspeccion",id_inspeccion);
        valores.put("foto",foto);
        valores.put("cantidad",cantidad);


        SQLiteDatabase dbl = getReadableDatabase();
        Cursor ars = dbl.rawQuery("SELECT * FROM CANTIDAD_FOTOS WHERE id_inspeccion =" + id_inspeccion + " and foto='" + foto +"'", null);
        Integer numero = ars.getCount();

        SQLiteDatabase db = getWritableDatabase();
      if (numero > 0) {
          //resp = "www" +numero ;
          //db.update("CANTIDAD_FOTOS", valores, "id_inspeccion=" + id_inspeccion + " and foto ='" + foto +"' and cantidad=" + cantidad , null);
          db.execSQL("UPDATE CANTIDAD_FOTOS SET id_inspeccion = " + id_inspeccion + ", foto = '" + foto + "', cantidad =" + cantidad + " WHERE id_inspeccion=" + id_inspeccion + " and foto ='" + foto +"'");
          resp = "Actualizadooo"+ valores ;
        } else {
            //if (db != 0) {
           // db.insert("CANTIDAD_FOTOS","",valores);
            //resp ="INSERT INTO CANTIDAD_FOTOS (id_inspeccion,foto,cantidad) VALUES (" + id_inspeccion + ",'" + foto + "'," + cantidad + ");";
            db.execSQL("INSERT INTO CANTIDAD_FOTOS (id_inspeccion,foto,cantidad) VALUES (" + id_inspeccion + ",'" + foto + "'," + cantidad + ");");
            resp = "Insertado" + valores;
           // }
        }
        return resp;

    }

    public int cantidadF(int id_inspeccion, String foto) {

        int rsp = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor ars = db.rawQuery("SELECT cantidad FROM CANTIDAD_FOTOS where id_inspeccion=" + id_inspeccion + " and foto ='" + foto+"'", null);
        if (ars.moveToFirst()) {
            rsp = ars.getInt(ars.getColumnIndex("cantidad"));


        }
        return rsp;
    }


    //"(id_inspeccion INTEGER, foto TEXT, fecha DATE, id_fallida  INTEGER, fechaHoraFallida DATETIME, enviado INTEGER, url BLOB, comentario TEXT)";

    public String insertartFotoFallida(int id_inspeccion, String foto,String fecha,int id_fallida,String fechaHoraFallida,int enviado, String url, String comentario, int motivoFallida){
        String resp = "";
        ContentValues valores = new ContentValues();
        valores.put("id_inspeccion",id_inspeccion);
        valores.put("foto",foto);
        valores.put("fecha",fecha);
        valores.put("id_fallida",id_fallida);
        valores.put("fechaHoraFallida",fechaHoraFallida);
        valores.put("enviado",enviado);
        valores.put("url",url);
        valores.put("comentario",comentario);
        valores.put("motivoFallida",motivoFallida);

        SQLiteDatabase dbl = getReadableDatabase();
        Cursor ars = dbl.rawQuery("SELECT * FROM FOTO_FALLIDA WHERE id_inspeccion =" + id_inspeccion + " and foto='" + foto+ "'", null);
        Integer numero = ars.getCount();

        SQLiteDatabase db = getWritableDatabase();
        if (ars.getCount() > 0) {
            db.update("FOTO_FALLIDA", valores, "id_inspeccion=" + id_inspeccion + " and foto='" + foto +"'", null);
            resp = "Actualizado";
        } else {
            if (db != null) {
                db.insert("FOTO_FALLIDA", null, valores);
                resp = "Insertado";

            }
        }
        return resp;
    }


    //OBTENER DATOS DE LA FOTO
    public String[][] DatosFotos(int id_inspeccion, String comentario) {
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        String[][] aData = null;
        Cursor aRS = db.rawQuery("SELECT * FROM FOTO WHERE id_inspeccion=" + id_inspeccion + " and comentario='" + comentario + "' order by id_foto desc LIMIT 1", null);

        if (aRS.getCount() > 0) {
            aData = new String[aRS.getCount()][];
            while (aRS.moveToNext()) {
                aData[count] = new String[5];
                aData[count][0] = Integer.toString(aRS.getInt(aRS.getColumnIndex("id_inspeccion")));
                aData[count][1] = aRS.getString(aRS.getColumnIndex("foto"));
                aData[count][2] = aRS.getString(aRS.getColumnIndex("comentario"));
                aData[count][3] = aRS.getString(aRS.getColumnIndex("url"));
                aData[count][4] = aRS.getString(aRS.getColumnIndex("contF"));
                count++;
            }
        } else {
            aData = new String[0][];
        }
        aRS.close();
        db.close();
        return (aData);
    }

    //OBTENER DATOS GEO
    public String[][] DatosGeoloc(int id_inspeccion) {
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        String[][] aData = null;
        Cursor aRS = db.rawQuery("SELECT * FROM GEOLOCALIZACION WHERE id_inspeccion=" + id_inspeccion , null);

        if (aRS.getCount() > 0) {
            aData = new String[aRS.getCount()][];
            while (aRS.moveToNext()) {
                aData[count] = new String[6];
                aData[count][0] = Integer.toString(aRS.getInt(aRS.getColumnIndex("id_inspeccion")));
                aData[count][1] = aRS.getString(aRS.getColumnIndex("usr"));
                aData[count][2] = aRS.getString(aRS.getColumnIndex("fecha"));
                aData[count][3] = aRS.getString(aRS.getColumnIndex("latitud"));
                aData[count][4] = aRS.getString(aRS.getColumnIndex("longitud"));
                aData[count][5] = aRS.getString(aRS.getColumnIndex("direccion"));
                count++;
            }
        } else {
            aData = new String[0][];
        }
        aRS.close();
        db.close();
        return (aData);
    }

    public String[][] DatosFotosFallida(int id_inspeccion, String foto) {
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        String[][] aData = null;
        Cursor aRS = db.rawQuery("SELECT * FROM FOTO_FALLIDA WHERE id_inspeccion=" + id_inspeccion + " and foto='" + foto + "'", null);

        if (aRS.getCount() > 0) {
            aData = new String[aRS.getCount()][];
            while (aRS.moveToNext()) {
                aData[count] = new String[6];
                aData[count][0] = Integer.toString(aRS.getInt(aRS.getColumnIndex("id_inspeccion")));
                aData[count][1] = aRS.getString(aRS.getColumnIndex("foto"));
                aData[count][2] = aRS.getString(aRS.getColumnIndex("comentario"));
                aData[count][3] = aRS.getString(aRS.getColumnIndex("url"));
                aData[count][4] = aRS.getString(aRS.getColumnIndex("fechaHoraFallida"));
                aData[count][5] = aRS.getString(aRS.getColumnIndex("motivoFallida"));
                count++;
            }
        } else {
            aData = new String[0][];
        }
        aRS.close();
        db.close();
        return (aData);
    }

    /// LISTA PIEZAS POSTERIOR ELIAS
    public String[][] listaPiezasPosterior() {
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        String[][] aData = null;
        Cursor aRS = db.rawQuery("SELECT pieza FROM PIEZA WHERE ubicacion = 'posterior' ORDER BY pieza", null);

        if (aRS.getCount() > 0) {
            aData = new String[aRS.getCount()][];
            while (aRS.moveToNext()) {
                aData[count] = new String[8];
                aData[count][0] = aRS.getString(aRS.getColumnIndex("pieza"));
                count++;
            }
        } else {
            aData = new String[0][];
        }
        aRS.close();
        db.close();
        return (aData);
    }

    /// LISTA DAÑOS POSTERIOR ELIAS
    public String[][] listaDanoPosterior() {
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        String[][] aData = null;
        Cursor dano = db.rawQuery("SELECT dano FROM DANIOS ", null);

        if (dano.getCount() > 0) {
            aData = new String[dano.getCount()][];
            while (dano.moveToNext()) {
                aData[count] = new String[1];
                aData[count][0] = dano.getString(dano.getColumnIndex("dano"));
                count++;
            }
        } else {
            aData = new String[0][];
        }



        dano.close();
        db.close();
        return (aData);
    }

    /// LISTA DEDUCIBLES POSTERIOR ELIAS
    public String[][] listaDeduciblesPosterior(String dano) {
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        String[][] aData = null;
        Cursor aRS = db.rawQuery("SELECT DE.glosaDeducible FROM DEDUCIBLES as DE inner join DANIOS as D ON D.idDano = DE.tipoDanio  WHERE D.dano = '" + dano + "' ORDER BY valorDeducible", null);

        if (aRS.getCount() > 0) {
            aData = new String[aRS.getCount()][];
            while (aRS.moveToNext()) {
                aData[count] = new String[8];
                aData[count][0] = aRS.getString(aRS.getColumnIndex("glosaDeducible"));
                count++;
            }
        } else {
            aData = new String[0][];
        }
        aRS.close();
        db.close();
        return (aData);
    }


    public String[][] listaPiezasLateralDerecho() {
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        String[][] aData = null;
        Cursor aRS = db.rawQuery("SELECT pieza FROM PIEZA WHERE ubicacion = 'lateral_derecho' ORDER BY pieza", null);

        if (aRS.getCount() > 0) {
            aData = new String[aRS.getCount()][];
            while (aRS.moveToNext()) {
                aData[count] = new String[8];
                aData[count][0] = aRS.getString(aRS.getColumnIndex("pieza"));
                count++;
            }
        } else {
            aData = new String[0][];
        }
        aRS.close();
        db.close();
        return (aData);
    }

    public String[][] listaPiezasFrontal() {
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        String[][] aData = null;
        Cursor aRS = db.rawQuery("SELECT pieza FROM PIEZA WHERE ubicacion = 'frontal' ORDER BY pieza", null);

        if (aRS.getCount() > 0) {
            aData = new String[aRS.getCount()][];
            while (aRS.moveToNext()) {
                aData[count] = new String[8];
                aData[count][0] = aRS.getString(aRS.getColumnIndex("pieza"));
                count++;
            }
        } else {
            aData = new String[0][];
        }
        aRS.close();
        db.close();
        return (aData);
    }

    /// LISTA PIEZAS LATERAL IZQUIERDO ELIAS
    public String[][] listaPiezasLateralIzquierdo() {
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        String[][] aData = null;
        Cursor aRS = db.rawQuery("SELECT pieza FROM PIEZA WHERE ubicacion = 'lateral_izquierdo' ORDER BY pieza", null);

        if (aRS.getCount() > 0) {
            aData = new String[aRS.getCount()][];
            while (aRS.moveToNext()) {
                aData[count] = new String[8];
                aData[count][0] = aRS.getString(aRS.getColumnIndex("pieza"));
                count++;
            }
        } else {
            aData = new String[0][];
        }
        aRS.close();
        db.close();
        return (aData);
    }

    public String[][] listaPiezasTecho() {
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        String[][] aData = null;
        Cursor aRS = db.rawQuery("SELECT pieza FROM PIEZA WHERE ubicacion ='techo'", null);

        if (aRS.getCount() > 0) {
            aData = new String[aRS.getCount()][];
            while (aRS.moveToNext()) {
                aData[count] = new String[8];
                aData[count][0] = aRS.getString(aRS.getColumnIndex("pieza"));
                count++;
            }
        } else {
            aData = new String[0][];
        }
        aRS.close();
        db.close();
        return (aData);
    }




    //CAMBIARESTADOFOTO
    public String cambiarEstadoFoto(int id_inspeccion,String nombrefoto,String comentario, int enviado){
        String respuesta = "";
        ContentValues valores = new ContentValues();
        valores.put("enviado", enviado);

        //PARA LEER Y SACAR EL NÚMERO DE COLUMNAS
        SQLiteDatabase dbl = getReadableDatabase();
        Cursor ars = dbl.rawQuery("SELECT * FROM FOTO WHERE id_inspeccion=" + id_inspeccion + " and foto='"+nombrefoto+"' and comentario ='"+comentario+"'" , null);
        Integer numero = ars.getCount();

        //ACTUALIZAR
        SQLiteDatabase db = getWritableDatabase();
        if (ars.getCount() > 0) {
            db.update("FOTO", valores, "id_inspeccion=" + id_inspeccion + " and foto='"+nombrefoto+"' and comentario ='"+comentario+"'" , null);
            respuesta = "Actualizado";
        }
        return respuesta;
    }

    public String cambiarEstadoFotoFallida(int id_inspeccion,String nombrefoto, int enviado){
        String respuesta = "";
        ContentValues valores = new ContentValues();
        valores.put("enviado", enviado);

        //PARA LEER Y SACAR EL NÚMERO DE COLUMNAS
        SQLiteDatabase dbl = getReadableDatabase();
        Cursor ars = dbl.rawQuery("SELECT * FROM FOTO_FALLIDA WHERE id_inspeccion=" + id_inspeccion + " and foto='"+nombrefoto+"'" , null);
        Integer numero = ars.getCount();

        //ACTUALIZAR
        SQLiteDatabase db = getWritableDatabase();
        if (ars.getCount() > 0) {
            db.update("FOTO_FALLIDA", valores, "id_inspeccion=" + id_inspeccion + " and foto='"+nombrefoto+"'" , null);
            respuesta = "Actualizado";
        }
        return respuesta;
    }


    //OBTENER LISTA CON LOS DATOS DE LAS FOTOS
    public String[][] ListaDatosFotos(int id_inspeccion) {
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        String[][] aData = null;
        // + "and enviado="+1+""

            Cursor aRS = db.rawQuery("SELECT * FROM FOTO WHERE id_inspeccion=" + id_inspeccion + " and enviado = 1", null);



        if (aRS.getCount() > 0) {
            aData = new String[aRS.getCount()][];
            while (aRS.moveToNext()) {
                aData[count] = new String[4];
                aData[count][0] = Integer.toString(aRS.getInt(aRS.getColumnIndex("id_inspeccion")));
                aData[count][1] = aRS.getString(aRS.getColumnIndex("foto"));
                aData[count][2] = aRS.getString(aRS.getColumnIndex("comentario"));
                aData[count][3] = aRS.getString(aRS.getColumnIndex("url"));
                count++;
            }
        } else {
            aData = new String[0][];
        }
        aRS.close();
        db.close();
        return (aData);
    }

    //OBTENER LISTA CON LOS DATOS DE LAS FOTOS
    public String[][] ListaDatosFotosFallida(int id_inspeccion) {
        int count = 0;

        SQLiteDatabase db = getReadableDatabase();
        String[][] aData = null;
        // + "and enviado="+1+""

        Cursor aRS = db.rawQuery("SELECT * FROM FOTO_FALLIDA WHERE id_inspeccion=" + id_inspeccion + " and enviado = 1", null);

        if (aRS.getCount() > 0) {
            aData = new String[aRS.getCount()][];
            while (aRS.moveToNext()) {
                aData[count] = new String[6];
                aData[count][0] = Integer.toString(aRS.getInt(aRS.getColumnIndex("id_inspeccion")));
                aData[count][1] = aRS.getString(aRS.getColumnIndex("foto"));
                aData[count][2] = aRS.getString(aRS.getColumnIndex("comentario"));
                aData[count][3] = aRS.getString(aRS.getColumnIndex("url"));
                aData[count][4] = aRS.getString(aRS.getColumnIndex("fechaHoraFallida"));
                aData[count][5] = aRS.getString(aRS.getColumnIndex("motivoFallida"));
                count++;
            }

        } else {
            aData = new String[0][];
        }
        aRS.close();
        db.close();
        return (aData);
    }


    //perfil
    public String obtenerPerfil() {
        String respuesta = "";
        SQLiteDatabase db = getReadableDatabase();
        Cursor aRS = db.rawQuery("SELECT perfil FROM USUARIO", null);
        if (aRS.getCount() > 0) {
            aRS.moveToFirst();
            respuesta = aRS.getString(0);
        }
        aRS.close();
        db.close();
        return respuesta;
    }

    //email
    public String obtenerEmail(int id_inspeccion) {
        String respuesta = "";
        SQLiteDatabase db = getReadableDatabase();
        Cursor aRS = db.rawQuery("SELECT email FROM INSPECCION WHERE id_inspeccion = " +id_inspeccion, null);
        if (aRS.getCount() > 0) {
            aRS.moveToFirst();
            respuesta = aRS.getString(0);
        }
        aRS.close();
        db.close();
        return respuesta;
    }


    /// LISTA PIEZAS PARA ENVIAR
    public String[][] listaAccesoriosParaEnviar(int id_inspeccion) {
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        String[][] aData = null;

        Cursor aRS = db.rawQuery("SELECT * FROM VALOR WHERE idInspeccion = "+id_inspeccion+" and valor not like '%seleccione%'", null);

        if (aRS.getCount() > 0) {
            aData = new String[aRS.getCount()][];
            while (aRS.moveToNext()) {
                aData[count] = new String[2];
                aData[count][0] = aRS.getString(aRS.getColumnIndex("idCampo"));
                aData[count][1] = aRS.getString(aRS.getColumnIndex("valor"));
                count++;
            }
        } else {
            aData = new String[0][];
        }
        aRS.close();
        db.close();
        return (aData);
    }




    //cambiar estado inspeccion
    public String cambiarEstadoInspeccion(int id_inspeccion,int estado){
        String respuesta = "";
        ContentValues valores = new ContentValues();
        valores.put("estado", estado);

        //PARA LEER Y SACAR EL NÚMERO DE COLUMNAS
        SQLiteDatabase dbl = getReadableDatabase();
        Cursor ars = dbl.rawQuery("SELECT * FROM INSPECCION WHERE id_inspeccion=" + id_inspeccion, null);
        Integer numero = ars.getCount();

        //ACTUALIZAR
        SQLiteDatabase db = getWritableDatabase();
        if (ars.getCount() > 0) {
            db.update("INSPECCION", valores, "id_inspeccion=" + id_inspeccion, null);
            respuesta = "Actualizado";
        }
        return respuesta;
    }



    //Consulta estado de la inspección
    public int estadoInspecciones(int estado) {
        int rsp = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor ars = db.rawQuery("SELECT id_inspeccion FROM INSPECCION where estado = " + estado +" LIMIT 1", null);
        if (ars.moveToFirst()) {
            rsp = ars.getInt(ars.getColumnIndex("id_inspeccion"));
        }
        return rsp;
    }

    //Consulta estado de la inspección
    public int estadoInspeccion(int id_inspeccion) {
        int rsp = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor ars = db.rawQuery("SELECT estado FROM INSPECCION where id_inspeccion = " + id_inspeccion +" LIMIT 1", null);
        if (ars.moveToFirst()) {
            rsp = ars.getInt(ars.getColumnIndex("estado"));
        }
        return rsp;
    }


    //Valida fotos obligatorias para transmitir
    public int fotosObligatoriasTomadas(int id_inspeccion) {
        int rsp = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor ars = db.rawQuery("SELECT count(*) as cantidad FROM FOTO WHERE id_inspeccion="+id_inspeccion+"" , null);

        /*Cursor ars = db.rawQuery("SELECT count(*) as cantidad FROM FOTO WHERE id_inspeccion="+id_inspeccion+"" +
                " and comentario in ('Posterior','Logo Luneta Posterior','Foto Motor','Foto Chasis(VIN)','Foto Cuna Motor','Foto Llantas y Neumaticos','Foto Rueda de Respuesto Llantas y Neumaticos'," +
                "'Foto Lateral Izquierdo','Foto Lateral Derecho','Foto Panel desde Afuera Interior','Foto Panel desde Dentro Interior','Foto Radio Interior','Foto Kilometraje Interior'," +
                "'Foto Frontal','Logo Parabrisas Frontal','Foto Documento','Foto Convertible')", null);*/

        //2,3,62,63,61,30,31,22,9,39,40,41,42,13,14,65,69,72

        if (ars.moveToFirst()) {
            rsp = ars.getInt(ars.getColumnIndex("cantidad"));
        }
        return rsp;
    }

    //borrar de la base de datos todos los registros de esta
    public  void deleteInspeccion(int id_inspeccion){
        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            db.execSQL("DELETE FROM INSPECCION WHERE id_inspeccion=" + id_inspeccion);
            db.execSQL("DELETE FROM FOTO WHERE id_inspeccion=" + id_inspeccion);
            db.execSQL("DELETE FROM VALOR WHERE idInspeccion=" + id_inspeccion);
            db.execSQL("DELETE FROM LOG_FDANO WHERE id_inspeccion="+id_inspeccion);
            db.execSQL("DELETE FROM GEOLOCALIZACION WHERE id_inspeccion=" + id_inspeccion);
            db.execSQL("DELETE FROM OI_EMAIL WHERE id_inspeccion=" + id_inspeccion);
            db.execSQL("DELETE FROM CANTIDAD_FOTOS WHERE id_inspeccion=" + id_inspeccion);
        }
        db.close();
    }

    //borrar de la base de datos todos los registros de esta
    public  void deleteInspeccionFallida(int id_inspeccion){
        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            db.execSQL("DELETE FROM INSPECCION_FALLIDA WHERE id_inspeccion=" + id_inspeccion);
            db.execSQL("DELETE FROM FOTO_FALLIDA WHERE id_inspeccion=" + id_inspeccion);
            db.execSQL("DELETE FROM GEOLOCALIZACION WHERE id_inspeccion=" + id_inspeccion);
            db.execSQL("DELETE FROM OI_EMAIL WHERE id_inspeccion=" + id_inspeccion);
            db.execSQL("DELETE FROM CANTIDAD_FOTOS WHERE id_inspeccion=" + id_inspeccion);
        }
        db.close();
    }


    //INSERTA INSPECCIONES RESCATADAS DEL SERVIDOR
    //INSPECCION_FALLIDA " +
      //      "(id_inspeccion INTEGER, fecha DATE, comentario TEXT, id_fallida INTEGER, fecha_cita DATE, hora_cita TEXT, activo INTEGER)";
    public String insertaInspeccionesFallida(Integer id_inspeccion, String fecha, String comentario, int id_fallida, String fecha_cita, String hora_cita, int activo) {
        String resp = "";
        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            try {
                db.execSQL("INSERT INTO INSPECCION_FALLIDA (id_inspeccion,fecha,comentario,id_fallida,fecha_cita,hora_cita,activo) " +
                        "values ("+id_inspeccion+",'"+fecha+"','"+comentario+"',"+id_fallida+",'"+fecha_cita+"','"+hora_cita+"',"+activo+")",null);

                resp = "Ok";
            } catch (Exception e) {
                resp = "ErrorFallida: " + e.getMessage();
            }
        } else {
            resp = "no hay base de datos";
        }
        db.close();
        return resp;
    }

    //borrar registros de la tabla inspeccion
    public void borrarInspeccionFallida(int id_inspeccion) {
        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            db.execSQL("DELETE FROM INSPECCION_FALLIDA WHERE id_inspeccion=" + id_inspeccion);
            db.execSQL("DELETE FROM FOTO_FALLIDA WHERE id_inspeccion=" + id_inspeccion);
        }
        db.close();
    }




    public int correlativoFotos(int id_inspeccion){
        int rsp = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor ars = db.rawQuery("SELECT count(*) as cantidad FROM FOTO WHERE id_inspeccion="+id_inspeccion+"", null);

        if (ars.moveToFirst()) {
            rsp = ars.getInt(ars.getColumnIndex("cantidad")) + 1;
        }
        return rsp;
    }


    public int idFotoFallida(int id_inspeccion){
        int rsp = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor ars = db.rawQuery("SELECT id_fallida FROM INSPECCION_FALLIDA WHERE id_inspeccion="+id_inspeccion+"", null);

        if (ars.moveToFirst()) {
            rsp = ars.getInt(ars.getColumnIndex("id_fallida"));
        }
        return rsp;
    }



    public int correlativoFotosFallida(int id_inspeccion){
        int rsp = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor ars = db.rawQuery("SELECT count(*) as cantidad FROM FOTO_FALLIDA WHERE id_inspeccion="+id_inspeccion+"", null);

        if (ars.moveToFirst()) {
            rsp = ars.getInt(ars.getColumnIndex("cantidad"));
        }
        return rsp;
    }


    /*private static final String LOG_FOTO_DANO = "CREATE LOG_FDANO " + "(id_fdano INTEGER, id_inspeccion INTEGER, comentario TEXT, ubicacion TEXT)";**/

    //"(id_inspeccion INTEGER, foto TEXT, fecha DATE, id_fallida  INTEGER, fechaHoraFallida DATETIME, enviado INTEGER, url BLOB, comentario TEXT)";
    public String insertarComentarioFoto(int id_inspeccion, String comentario, String ubicacion){
        String resp = "";
        ContentValues valores = new ContentValues();

        SQLiteDatabase dbl = getReadableDatabase();
        Cursor ars = dbl.rawQuery("SELECT * FROM LOG_FDANO WHERE id_inspeccion =" + id_inspeccion +"", null);
        Integer numero = ars.getCount();

        valores.put("id_fdano",numero);
        valores.put("id_inspeccion",id_inspeccion);
        valores.put("comentario",comentario);
        valores.put("ubicacion",ubicacion);


        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            db.insert("LOG_FDANO", null, valores);
            resp = "Insertado";
        }
        return resp;
    }


    public String comentarioFoto(int id_inspeccion, String ubicacion){
        String rsp = "";
        SQLiteDatabase db = getReadableDatabase();
        Cursor ars = db.rawQuery("SELECT comentario FROM LOG_FDANO WHERE id_inspeccion="+id_inspeccion+" and ubicacion ='"+ubicacion+"' ORDER BY id_fdano desc LIMIT 1", null);

        if (ars.moveToFirst()) {
            rsp = ars.getString(ars.getColumnIndex("comentario"));
        }
        return rsp;
    }


    /// LISTA PIEZAS PARA ENVIAR
  public String[][] DeduciblePieza(String pieza, String ubicacion) {
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        String[][] aData = null;
        //idDdeducible,idCampo, pieza, ubicacion
        Cursor aRS = db.rawQuery("SELECT * FROM PIEZA WHERE pieza = '"+pieza+"' and ubicacion='"+ubicacion+"'", null);

        if (aRS.getCount() > 0) {
            aData = new String[aRS.getCount()][];
            while (aRS.moveToNext()) {
                aData[count] = new String[2];
                aData[count][0] = aRS.getString(aRS.getColumnIndex("idCampo"));
                aData[count][1] = aRS.getString(aRS.getColumnIndex("idDdeducible"));
                count++;
            }
        } else {
            aData = new String[0][];
        }
        aRS.close();
        db.close();
        return (aData);
    }



    //db.execSQL("INSERT INTO DANIOS (idDano, dano) VALUES (1, 'Abolladura')");

    public int obtenerDanio(String glosa){
        int rsp = 0;

        SQLiteDatabase db = getReadableDatabase();
        Cursor ars = db.rawQuery("select idDano from DANIOS where dano ='"+glosa+"'",null);

        if(ars.moveToFirst()){
            rsp = ars.getInt(ars.getColumnIndex("idDano"));
        }
        return rsp;
        //db.insertarValor(Integer.parseInt(id_inspeccion), Integer.parseInt(dañosDedu[0][0]), String.valueOf(db.obtenerDanio(spinnerDanoPoE.getSelectedItem().toString())));
    }


    //db.execSQL("INSERT INTO DEDUCIBLES (tipoDanio, valorDeducible, glosaDeducible) VALUES (1, '2.0', 'LEVE')");

    public String obtenerDeducible(int tipoDanio, String glosa){
        String rsp = "";

        SQLiteDatabase db = getReadableDatabase();
        //CAMBIAR A DEDUUCIBLES
        Cursor ars = db.rawQuery("select valorDeducible from DEDUCIBLES where tipoDanio ="+tipoDanio+"  and glosaDeducible='"+glosa+"'",null);

        if(ars.moveToFirst()){
            rsp = ars.getString(ars.getColumnIndex("valorDeducible"));
        }
        return rsp;
    }


    public String obtenerRegion(String comuna) {

        String rsp = "Seleccione";
        SQLiteDatabase db = getReadableDatabase();
        Cursor ars = db.rawQuery("SELECT DISTINCT region FROM COMUNA WHERE comuna = '" + comuna + "' LIMIT 1", null);

        if(ars.moveToFirst()){
            rsp = ars.getString(ars.getColumnIndex("region"));
        }

        return rsp;
    }

    public int DatosOiOBD( String id_inspeccion) {
        int respuesta = 0;
        String[][] aData = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor aRS = db.rawQuery("SELECT obd FROM inspeccion WHERE id_inspeccion =" + id_inspeccion , null);
        if (aRS.getCount() > 0) {
            aRS.moveToFirst();
            respuesta = aRS.getInt(0);
        }
        aRS.close();
        db.close();
        return respuesta;
    }

    /*public String Deducible(String glosa) {
        String rsp = "";
        SQLiteDatabase db = getReadableDatabase();
        Cursor ars = db.rawQuery("SELECT * FROM DEDUCIBLE WHERE glosaDeducible="+glosa+"' and tipoDanio='"+tipoD+"'", null);

        if (ars.moveToFirst()) {
            rsp = ars.getString(ars.getColumnIndex("cantidad"));
        }
        return rsp;
    }*/







}
