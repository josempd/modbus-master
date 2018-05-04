package com.example.josempd.modbusmaster;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class SettingsActivity extends ActionBarActivity {

    DatabaseHandler db = new DatabaseHandler(this);

    //Se declaran los EditText
    EditText DirIP;
    EditText Port;
    EditText SlaveID;
    EditText SensorID;
    EditText SensorNumber;
    EditText Tdm;
    //Se declara el Spinner de la funcion de modbus
    Spinner FuncModbus;
    //Se declaran los botones
    Button SaveButton;
    Button DeleteAllButton;

    //Se declaran las Shared Preferences
    SharedPreferences sharedpreferences;
    public static final String MisPreferencias = "MisPreferencias";
    public static final String IPK = "IPKey";
    public static final String PortK = "PortKey";
    public static final String SlaveK = "SlaveKey";
    public static final String SensorK = "SensorKey";
    public static final String CountK = "CountKey";
    public static final String ModbusK= "FuncKey";
    public static final String TiempoK = "TiempoKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Se asignan los EditText a variables de la activity
        DirIP = (EditText) findViewById(R.id.dirIP);
        Port = (EditText) findViewById(R.id.Port);
        SlaveID = (EditText) findViewById(R.id.SlaveID);
        SensorID = (EditText) findViewById(R.id.SensorID);
        SensorNumber = (EditText) findViewById(R.id.SensorNumber);

        Tdm = (EditText) findViewById(R.id.Tdm);

        SaveButton = (Button) findViewById(R.id.SaveButton);
        DeleteAllButton = (Button) findViewById(R.id.DeleteAllButton);

        //Se inicializa el spinner
        FuncModbus = (Spinner) findViewById(R.id.spinner);
        String []opciones={"01", "02", "03", "04"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opciones);
        FuncModbus.setAdapter(adapter);

        //Funcion del boton save
        SaveButton.setOnClickListener(buttonSaveOnClickListener);

        DeleteAllButton.setOnClickListener(buttonDeleteOnClickListener);

        //Se inicializan las preferencias
        sharedpreferences = getSharedPreferences(MisPreferencias, Context.MODE_PRIVATE);
    }

    View.OnClickListener buttonSaveOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View arg0){
            //Se verifica la validez de los parametros
            if (DirIP.getText().toString().matches("") ||
                    SensorNumber.getText().toString().matches("") ||
                    SlaveID.getText().toString().matches("") ||
                    Port.getText().toString().matches("") ||
                    SensorID.getText().toString().matches("") ||
                    SensorNumber.getText().toString().matches("0") ||
                    Tdm.getText().toString().matches("0") ||
                    Tdm.getText().toString().matches("") ||
                    SlaveID.getText().toString().matches("0")) {
                Toast.makeText(getBaseContext(), "Existen par\u00e1metros inv\u00e1lidos", Toast.LENGTH_SHORT).show();
            }
            else {
                //Se guardan los Strings en las preferencias.
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(IPK, DirIP.getText().toString());
                editor.putString(PortK, Port.getText().toString());
                editor.putString(SlaveK, SlaveID.getText().toString());
                editor.putString(SensorK, SensorID.getText().toString());
                editor.putString(CountK, SensorNumber.getText().toString());
                editor.putString(TiempoK, Tdm.getText().toString());
                editor.putString(ModbusK, FuncModbus.getSelectedItem().toString());
                editor.apply();
                //Se llama a la main activity
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                onNewIntent(i);
                startActivity(i);
            }
        }
    };

    View.OnClickListener buttonDeleteOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View arg0){
            db.clearValores();
            Toast.makeText(getBaseContext(), "Valores borrados", Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
