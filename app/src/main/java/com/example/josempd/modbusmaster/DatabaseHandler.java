package com.example.josempd.modbusmaster;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Josempd on 15/04/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // Todas las variables Static
    // Version de la base de datos
    private static final int DATABASE_VERSION = 1;

    // Nombre de la base de datos
    private static final String DATABASE_NAME = "MonitoreoModbus";

    // Nombre de las tablas
    private static final String TABLE_VALORES = "valores";
    private static final String TABLE_ALARMAS = "alarmas";

    // Nombre de las columnas comunes entre tablas
    private static final String KEY_ID = "id";
    private static final String KEY_RTU = "RTU";
    private static final String KEY_SENSOR = "Sensor";

    //Columnas exclusivas de la tabla Valores
    private static final String KEY_VALOR = "Valor";

    //Columnas exclusivas de la tabla Alarmas
    private static final String KEY_VALORMIN = "Valormin";
    private static final String KEY_VALORMAX = "Valormax";
    private static final String KEY_MAIL = "Mail";
    private static final String KEY_CEL = "Cel";

    // Creando las tablas
    //Creando la tabla de valores
    private static final String CREATE_TABLE_VALORES = "CREATE TABLE "
            + TABLE_VALORES + "(" + KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_RTU + " INTEGER," + KEY_SENSOR + " INTEGER," + KEY_VALOR +
            " INTEGER" + ")";

    //Creando la tabla de alarmas
    private static final String CREATE_TABLE_ALARMAS = "CREATE TABLE "
            + TABLE_ALARMAS + "(" + KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_RTU + " INTEGER," + KEY_SENSOR + " INTEGER," + KEY_VALORMIN +
            " INTEGER," + KEY_VALORMAX + " INTEGER," + KEY_MAIL + " TEXT," +
            KEY_CEL + " TEXT" + ")";

    //Constructor de la clase
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Crear tablas con las string declaradas previamente
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE_VALORES);
        db.execSQL(CREATE_TABLE_ALARMAS);
    }

    // Updateando las tablas
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Si existe una tabla vieja, abandonarla
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VALORES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARMAS);

        // Crear de nuevo la tabla
        onCreate(db);
    }

    /**
     * Operaciones CRUD(Create, Read, Update, Delete)
     */

    // Agregando nuevo valor
    void addValor(ValorModbus Valor) {
        //Se llama la base de datos
        SQLiteDatabase db = this.getWritableDatabase();
        //Se guardan los valores mediante contentvalues
        ContentValues values = new ContentValues();
        values.put(KEY_RTU, Valor.getRTU());
        values.put(KEY_SENSOR, Valor.getSensor());
        values.put(KEY_VALOR, Valor.getValor());

        // Insertando fila
        db.insert(TABLE_VALORES, null, values);
        db.close(); // se cierra la conexion de la db
    }

    // Obteniendo un solo valor
    //ValorModbus getValor(int id) {
      //  SQLiteDatabase db = this.getReadableDatabase();
        //Se crea el cursor para moverse a traves de la tabla
        //Cursor cursor = db.query(TABLE_VALORES, new String[] { KEY_ID,
          //            KEY_RTU, KEY_SENSOR,  KEY_VALOR}, KEY_ID + "=?",
            //    new String[] { String.valueOf(id) }, null, null, null, null);
        //if (cursor != null)
          //  cursor.moveToFirst();
        //Se hace un nuevo valor con la info de la tabla
        //moviendonos a traves del cursor
        //ValorModbus Valor = new ValorModbus(Integer.parseInt(cursor.getString(0)),
          //      Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(2)),
            //    Integer.parseInt(cursor.getString(3)));
        // Return del valor
        //return Valor;
   // }

    //Obteniendo el ultimo valor introducido:
    public String LastValor(){
        //Uso de el cursor en una consulta para devolver el valor
        String selectQuery= "SELECT * FROM " + TABLE_VALORES+" ORDER BY " + KEY_ID +
                " DESC LIMIT 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String str = "";
        if(cursor.moveToFirst())
            str  =  cursor.getString( cursor.getColumnIndex(KEY_ID) );
        cursor.close();
        return str;
    }

    //Obteniendo el Valormin de alarma segun valor de RTU y sensor
    public String ValorminValue(int rtu, int sensor){
        String selectQuery= "SELECT " + "*" + " FROM " + TABLE_ALARMAS+" WHERE " +
                KEY_RTU + " =? AND " + KEY_SENSOR + " =?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] {String.valueOf(rtu), String.valueOf(sensor)});
        String intv = "";
        if(cursor.moveToFirst())
            intv =  cursor.getString(cursor.getColumnIndex(KEY_VALORMIN));
        cursor.close();
        return intv;
    }

    //Obteniendo el Valormax de alarma segun valor de RTU y sensor
    public String ValormaxValue(int rtu, int sensor){
        String selectQuery= "SELECT " + "*" + " FROM " + TABLE_ALARMAS+" WHERE " +
                KEY_RTU + " =? AND " + KEY_SENSOR + " =?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] {String.valueOf(rtu), String.valueOf(sensor)});
        String intv = "";
        if(cursor.moveToFirst())
            intv =  cursor.getString( cursor.getColumnIndex(KEY_VALORMAX));
        cursor.close();
        return intv;
    }

    //Obteniendo el mail de alarma segun valor de RTU y sensor
    public String MailValue(int rtu, int sensor){
        String selectQuery= "SELECT " + "*" + " FROM " + TABLE_ALARMAS+" WHERE " +
                KEY_RTU + " =? AND " + KEY_SENSOR + " =?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] {String.valueOf(rtu), String.valueOf(sensor)});
        String intmail = "";
        if(cursor.moveToFirst())
            intmail =  cursor.getString(cursor.getColumnIndex(KEY_MAIL));
        cursor.close();
        return intmail;
    }

    //Obteniendo el Celular de alarma segun valor de RTU y sensor
    public String CelValue(int rtu, int sensor){
        String selectQuery= "SELECT " + "*" + " FROM " + TABLE_ALARMAS+" WHERE " +
                KEY_RTU + " =? AND " + KEY_SENSOR + " =?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] {String.valueOf(rtu), String.valueOf(sensor)});
        String intcel = "";
        if(cursor.moveToFirst())
            intcel =  cursor.getString(cursor.getColumnIndex(KEY_CEL));
        cursor.close();
        return intcel;
    }

    //Borrando un valor
    //public void ClearValor(ValorModbus Valor){
    //    SQLiteDatabase db = this.getWritableDatabase();

        //db.delete(TABLE_VALORES, KEY_ID + " = ?",
         //       new String[]{ String.valueOf(Valor.getID())});
    //}

    // Obteniendo todos los valores
    public List<ValorModbus> getAllValores() {
        List<ValorModbus> ValorList = new ArrayList<ValorModbus>();
        // Consultando todos
        String selectQuery = "SELECT  * FROM " + TABLE_VALORES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // loop a traves de todas las filas
        if (cursor.moveToFirst()) {
            do {
                ValorModbus valorModbus = new ValorModbus();
                valorModbus.setID(Integer.parseInt(cursor.getString(0)));
                valorModbus.setRTU(Integer.parseInt(cursor.getString(1)));
                valorModbus.setSensor(Integer.parseInt(cursor.getString(2)));
                valorModbus.setValor(Integer.parseInt(cursor.getString(3)));

                // Anadiendo valores a la lista
                ValorList.add(valorModbus);
            } while (cursor.moveToNext());
        }

        // Return de la lista
        return ValorList;
    }

    // Conteo de valores obtenidos
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_VALORES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return del count
        return cursor.getCount();
    }

    //Creando un valor de alarma
    // Agregando nuevo valor
    void addAlarma(AlarmasModbus Alarma) {
        SQLiteDatabase db = this.getWritableDatabase();
        //Uso de ContentValues para pasar los valores
        //De la clase creada a la base de datos
        ContentValues values = new ContentValues();
        values.put(KEY_RTU, Alarma.getRTU());
        values.put(KEY_SENSOR, Alarma.getSensor());
        values.put(KEY_VALORMIN, Alarma.getValormin());
        values.put(KEY_VALORMAX, Alarma.getValormax());
        values.put(KEY_MAIL, Alarma.getMail());
        values.put(KEY_CEL, Alarma.getCel());

        // Insertando fila
        db.insert(TABLE_ALARMAS, null, values);
        db.close(); // Closing database connection
    }

    //Borrando un valor de alarma
    void clearAlarms(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ALARMAS, null, null);
    }

    //Borrando los valores medidos
    void clearValores(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_VALORES, null, null);
    }

    //Escrbiendo todos los valores de alarmas
    public List<AlarmasModbus> getAllAlarmas() {
        List<AlarmasModbus> ValorList = new ArrayList<AlarmasModbus>();
        // Consultando todos
        String selectQuery = "SELECT  * FROM " + TABLE_ALARMAS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // loop a traves de todas las filas
        if (cursor.moveToFirst()) {
            do {
                AlarmasModbus AlarmasModbus = new AlarmasModbus();
                AlarmasModbus.setID(Integer.parseInt(cursor.getString(0)));
                AlarmasModbus.setRTU(Integer.parseInt(cursor.getString(1)));
                AlarmasModbus.setSensor(Integer.parseInt(cursor.getString(2)));
                AlarmasModbus.setValormin(Integer.parseInt(cursor.getString(3)));
                AlarmasModbus.setValormax(Integer.parseInt(cursor.getString(4)));
                AlarmasModbus.setMail(cursor.getString(5));
                AlarmasModbus.setCel(cursor.getString(6));

                // Anadiendo valores a la lista
                ValorList.add(AlarmasModbus);
            } while (cursor.moveToNext());
        }

        // Return de la lista
        return ValorList;
    }
}
