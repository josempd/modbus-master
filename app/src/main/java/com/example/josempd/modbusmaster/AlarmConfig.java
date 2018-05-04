package com.example.josempd.modbusmaster;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AlarmConfig extends ActionBarActivity {

    DatabaseHandler db = new DatabaseHandler(this);

    EditText etRTU;
    EditText etSensor;
    EditText etValorminimo;
    EditText etValormaximo;
    EditText etMail;
    EditText etPhone;

    int intRTU;
    int intSensor;
    int intValormin;
    int intValormax;

    String strMail;
    String strPhone;

    TextView AlarmView;

    Button SaveAlarm;
    Button DeleteAlarms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_config);

        etRTU = (EditText) findViewById(R.id.etRTU);
        etSensor = (EditText) findViewById(R.id.etSensor);
        etValorminimo = (EditText) findViewById(R.id.etValormin);
        etValormaximo = (EditText) findViewById(R.id.etValormax);
        etMail = (EditText) findViewById(R.id.etMail);
        etPhone = (EditText) findViewById(R.id.etPhone);

        AlarmView = (TextView) findViewById(R.id.AlarmView);
        AlarmView.setMovementMethod(new ScrollingMovementMethod());

        SaveAlarm = (Button) findViewById(R.id.SaveAlarmButton);
        DeleteAlarms = (Button) findViewById(R.id.DeleteAlarmButton);

        SaveAlarm.setOnClickListener(SaveAlarmOnClickListener);
        DeleteAlarms.setOnClickListener(DeleteAlarmsOnClickListener);
    }

    View.OnClickListener SaveAlarmOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0){
            String alarm = "";
            if (etRTU.getText().toString().matches("") ||
                    etSensor.getText().toString().matches("") ||
                    etValorminimo.getText().toString().matches("") ||
                    etValormaximo.getText().toString().matches("") ||
                    etMail.getText().toString().matches("") ||
                    etPhone.getText().toString().matches("")) {
                Toast.makeText(getBaseContext(), "Existen par\u00e1metros inv\u00e1lidos", Toast.LENGTH_SHORT).show();
            }
            else {
                intRTU = Integer.parseInt(etRTU.getText().toString());
                intSensor = Integer.parseInt(etSensor.getText().toString());
                intValormin = Integer.parseInt(etValorminimo.getText().toString());
                intValormax = Integer.parseInt(etValormaximo.getText().toString());
                strMail = etMail.getText().toString();
                strPhone = etPhone.getText().toString();

                db.addAlarma(new AlarmasModbus(intRTU,
                        intSensor,
                        intValormin,
                        intValormax,
                        strMail,
                        strPhone));
                List<AlarmasModbus> Alarmas = db.getAllAlarmas();

                for (AlarmasModbus cn : Alarmas) {
                    alarm = alarm + "ID:" + cn.getID() + " ;RTU:" + cn.getRTU()
                            + " ;Sensor:" + cn.getSensor() + " ;Valormin:" + cn.getValormin()
                            + " ;Valormax:" + cn.getValormax() + " ;Mail:" + cn.getMail()
                            + " ;Cel:" + cn.getCel() + "\n";
                    AlarmView.setText(alarm);
                }
            }
        }
    };

    View.OnClickListener DeleteAlarmsOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View arg0){
        db.clearAlarms();
            List<AlarmasModbus> Alarmas = db.getAllAlarmas();

            for (AlarmasModbus cn : Alarmas) {
                String alarm = "Id: " + cn.getID() + " ,RTU: " + cn.getRTU()
                        + " ,Sensor:" + cn.getSensor() + " ,Valormin:" + cn.getValormin()
                        + " ,Valormax:" + cn.getValormax() + " ,Mail:" + cn.getMail()
                        + " ,Cel:" + cn.getCel();
                AlarmView.setText("");
                Toast.makeText(getBaseContext(), "Alarmas borradas", Toast.LENGTH_SHORT).show();
        }
    }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alarm_config, menu);
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
