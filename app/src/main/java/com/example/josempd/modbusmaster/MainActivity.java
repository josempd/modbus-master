package com.example.josempd.modbusmaster;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.PendingIntent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.msg.ReadCoilsRequest;
import com.serotonin.modbus4j.msg.ReadCoilsResponse;
import com.serotonin.modbus4j.msg.ReadDiscreteInputsRequest;
import com.serotonin.modbus4j.msg.ReadDiscreteInputsResponse;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersRequest;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersResponse;
import com.serotonin.modbus4j.msg.ReadInputRegistersRequest;
import com.serotonin.modbus4j.msg.ReadInputRegistersResponse;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity {
    //Variable para manejo de la base de datos
    DatabaseHandler db = new DatabaseHandler(this);

    //Declaracion de los TextView de la actividad
    TextView ParamView;
    TextView ValoresView;
    TextView TVInicio;
    TextView tvAlarmas;

    //String globales para escribir los TextView:
    String ParamText = "";
    String ValoresText = "";

    //String globales para las SharedPreferences:
    String IPK;
    String PortK;
    String SlaveK;
    String SensorK;
    String CountK;
    String ModbusK;
    String Tdm;

    //Declaracion del boton toggle
    ToggleButton ONOFF;

    //Variable global, booleana para saber si se envio una alarma
    boolean AlarmSent;


    //Strings que muestran la fecha y hora actual.
    static Date d = new Date();
    static CharSequence Fecha  = android.text.format.DateFormat.format
            ("EEE, dd MMM, HH:mm:ss", d.getTime());

    //Creacion del timer para realizar el ciclo de mediciones
    static Timer timer = new Timer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParamView = (TextView) findViewById(R.id.tvParam);
        ValoresView = (TextView) findViewById(R.id.tvValores);
        TVInicio = (TextView) findViewById(R.id.tvInicio);
        tvAlarmas = (TextView) findViewById(R.id.tvAlarmas);
        ValoresView.setMovementMethod(new ScrollingMovementMethod());

        ONOFF = (ToggleButton) findViewById(R.id.ConexButton);

        AlarmSent = false;

        SharedPreferences MisPreferencias = getSharedPreferences("MisPreferencias",
                Context.MODE_PRIVATE);
        IPK = MisPreferencias.getString("IPKey", "0.0.0.0");
        PortK = MisPreferencias.getString("PortKey", "0000");
        SlaveK = MisPreferencias.getString("SlaveKey", "1");
        SensorK = MisPreferencias.getString("SensorKey", "1");
        CountK = MisPreferencias.getString("CountKey", "1");
        ModbusK = MisPreferencias.getString("FuncKey", "04");
        Tdm = MisPreferencias.getString("TiempoKey", "100");

            ParamText = ParamText + "Los par\u00e1metros son:" + "\n" +
                    "IP:" + IPK + "\n" +
                    "Puerto:" + PortK + "\n" +
                    "ID del esclavo:" + SlaveK + "\n" +
                    "primera direcci\u00f3n:" + SensorK + "\n" +
                    "N\u00famero de direcciones:" + CountK + "\n" +
                    "Funci\u00f3n de Modbus:" + ModbusK + "\n" +
                    "Tiempo de muestreo: " + Tdm + " seg";
        ParamView.setText(ParamText);
    }

    public void onToggleClicked(View view) {
        //Si el boton se enciende, entonces se cumple la condicion
        boolean on = ((ToggleButton) view).isChecked();
        if (on) {
            //Se coloca el inicio de la medicion, con la hora
            //en el formato previamente realizado
            TVInicio.setText("Medici\u00f3n iniciada en: " + Fecha);
            //Se crea un Handler
            final Handler handler = new Handler();
            //Se inicializa el timer creado previamente
            timer = new Timer();
                //Se crea una tarea para el timer
                TimerTask doAsynchronousTask = new TimerTask() {
                    //Dentro de la tarea del timer, se corre el
                    //Handler iniciado previamente
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //Aca se llama la clase de Modbus
                                //pasandole los parametros
                                try {
                                        ModbusTask myClientTask = new ModbusTask
                                            (IPK,
                                            Integer.parseInt(PortK),
                                            Integer.parseInt(SlaveK),
                                            Integer.parseInt(SensorK),
                                            Integer.parseInt(CountK),
                                            Integer.parseInt(ModbusK));
                                    myClientTask.execute();
                                } catch (Exception e ) {
                                    e.printStackTrace();
                                    ValoresView.setText(e.toString());
                                }
                            }
                        });
                    }
                };
                //En este segmento se configura el timer
                //0 ms de retardo, Cada Tdm*1000ms
                timer.schedule(doAsynchronousTask, 0,
                        Integer.parseInt(Tdm)*1000);
        } else {
            //Si se vuelve a tocar el boton toggle
            //Se cancela el timer
            //Se reemplaza el texto de modbus
            //Se limpian los valores de alarma
            //La actividad pasa a un estado de espera por
            //presionar de nuevo el boton
            timer.cancel();
            ValoresView.setText("no se est\u00e1 midiendo actualmente.");
            tvAlarmas.setText("");
            AlarmSent = false;
        //}
    }
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            ONOFF.setChecked(false);
            timer.cancel();
            ValoresView.setText("no se est\u00e1 midiendo actualmente.");
            startActivity(i);
            return true;
        }

        if (id == R.id.alarm_settings) {
            Intent i = new Intent(this, AlarmConfig.class);
            ONOFF.setChecked(false);
            timer.cancel();
            ValoresView.setText("no se est\u00e1 midiendo actualmente.");
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Enviar SMS a destinatario
    public void sendSMS(String phoneNumber, String message)
    {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //Luego de enviar el mensaje.
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(),
                                "Mensaje de alarma enviado.",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(),
                                "Falla generica al enviar SMS",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(),
                                "Sin servicio, sms no enviado.",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(),
                                "Mensaje no enviado, receptor no disponible",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(),
                                "Antena no disponible, mensaje no enviado.",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //Cuando el mensaje fue entregado
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(),
                                "Mensaje de alarma entregado",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(),
                                "Mensaje de alarma ennviado, pero no entregado",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }

    public void sendMail(String mail, String message){
        String Mail = mail;
        String Message = message;
        Mail m = new Mail("alarmasmodbus@gmail.com", "AlarmaModbus");
        String[] toArr = {Mail};
        m.setTo(toArr);
        m.setFrom("alarmasmodbus@gmail.com");
        m.setSubject("Alerta");
        m.setBody(Message);
        try {
            m.send();
        } catch(Exception e) {
            Log.e("MailApp", "No se pudo enviar el E-mail", e);
        }
    }

    public class ModbusTask extends AsyncTask<Void, String, String> {

        String Resp;
        String IPMod;
        int PortMod;
        int slaveId;
        int RefMod;
        int CountMod;
        int Func;
        //Arreglos para manipular valores de Modbus
        private int[] ModbusArray;
        private int[] SensorArray;
        private boolean[] ArregloModbus01;
        private boolean[] ArregloModbus02;
        private short[] ArregloModbus03;
        private short[] ArregloModbus04;

        ModbusTask(String addr, int port, int ID, int ref, int count, int func) {
            IPMod = addr;
            PortMod = port;
            slaveId = ID;
            RefMod = ref;
            CountMod = count;
            Func = func;
        }

        @Override
        protected String doInBackground(Void... arg0) {

            ModbusFactory factory = new ModbusFactory();
            IpParameters params = new IpParameters();//Se crea la variable que
            // maneja los parametros para el maestro de modbus
            params.setHost(IPMod);//Se asigna el IP del esclavo
            params.setPort(PortMod);//Se asigna el puerto del esclavo
            params.setEncapsulated(true);//Se asigna el valor True a la encapsulacion del
            // mensaje dentro de una trama RTU
            ModbusMaster master = factory.createTcpMaster(params, true); //Se crea un maestro de
            // Modbus segun los parametros y se mantiene abierto con 'keepalive = true'
            master.setTimeout(500);//Se coloca un timeout de 500ms para enviar la trama
            master.setRetries(2);//Se colocan 2 reintentos por fallo en la conexion

            try {
                    master.init(); //Se inicializa el maestro de ModBus
                    switch (Func) {
                        case 01:// Se genera la peticion de la funcion 01
                            ReadCoilsRequest request01 = new ReadCoilsRequest(slaveId,
                                    RefMod, CountMod);
                            // Se genera la respuesta de la funcion 01
                            ReadCoilsResponse response01 = (ReadCoilsResponse) master.send(request01);
                            if (response01.isException()) {
                                Resp = "Exception response: " +
                                        "message=" + response01.getExceptionMessage();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ValoresView.setText(Resp);
                                    }
                                });
                            } else {
                                ArregloModbus01 = response01.getBooleanData();
                                int i = 0;
                                ModbusArray = new int[CountMod];
                                while (i < CountMod) {
                                    ModbusArray[i] = ArregloModbus01[i] ? 1 : 0;
                                    i++;
                                }
                            }
                        case 02:// Se genera la peticion de la funcion 02
                            ReadDiscreteInputsRequest request02 =
                                    new ReadDiscreteInputsRequest(slaveId, RefMod, CountMod);
                            // Se genera la respuesta de la funcion 02
                            ReadDiscreteInputsResponse response02 =
                                    (ReadDiscreteInputsResponse) master.send(request02);
                            if (response02.isException()) {
                                Resp = "Exception response: message=" +
                                        response02.getExceptionMessage();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ValoresView.setText(Resp);
                                    }
                                });
                            } else {
                                ArregloModbus02 = response02.getBooleanData();
                                int j = 0;
                                ModbusArray = new int[CountMod];
                                while (j < CountMod) {
                                    ModbusArray[j] = ArregloModbus02[j] ? 1 : 0;
                                    j++;
                                }
                            }
                        case 03:// Se genera la peticion de la funcion 03
                            ReadHoldingRegistersRequest request03 =
                                    new ReadHoldingRegistersRequest(slaveId, RefMod, CountMod);
                            // Se genera la respuesta de la funcion 03
                            ReadHoldingRegistersResponse response03 =
                                    (ReadHoldingRegistersResponse) master.send(request03);

                            if (response03.isException()) {
                                Resp = "Exception response: message=" +
                                        response03.getExceptionMessage();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ValoresView.setText(Resp);
                                    }
                                });
                            } else {
                                ArregloModbus03 = response03.getShortData();
                                int k = 0;
                                ModbusArray = new int[CountMod];
                                while (k < CountMod) {
                                    ModbusArray[k] = (int) ArregloModbus03[k];
                                    k++;
                                }
                            }
                        case 04:// Se genera la peticion de la funcion 04
                            ReadInputRegistersRequest request04 =
                                    new ReadInputRegistersRequest(slaveId, RefMod, CountMod);
                            // Se genera la respuesta de la funcion 04
                            ReadInputRegistersResponse response04 =
                                    (ReadInputRegistersResponse) master.send(request04);
                            if (response04.isException()) {
                                Resp = "Exception response: message=" + response04.getExceptionMessage();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ValoresView.setText(Resp);
                                    }
                                });
                            } else {
                                ArregloModbus04 = response04.getShortData();
                                int l = 0;
                                ModbusArray = new int[CountMod];
                                while (l < CountMod) {
                                    ModbusArray[l] = (int) ArregloModbus04[l];
                                    l++;
                                }
                            }

                            break;
                    }
                //Se inicializa el texto de respuesta
                ValoresText = "";
                //Inicia un ciclo while para escrbir los valores en la base de datos
                int i = 0;
                SensorArray = new int[Integer.parseInt(CountK)];
                while (i < Integer.parseInt(CountK)) {
                    SensorArray[i] = i + 1;
                    Log.d("Insertar datos: ", "Insertando ..");
                    //Se agregan los valores en la base de datos
                    db.addValor(new ValorModbus(1, SensorArray[i], ModbusArray[i]));
                    db.close();
                    //Se escriben en un String que luego actualiza la pantalla principal
                    ValoresText = ValoresText + "ID: " + db.LastValor() + ";RTU: " +
                            "1" + ";Sensor:" + SensorArray[i] + ";Valor: " +
                            ModbusArray[i] + "  \n";
                    //Se extraen los valores de rango de las alarmas
                    //Segun la remota y el sensor actuales en el ciclo
                    String rangomin = db.ValorminValue(1, SensorArray[i]);
                    String rangomax = db.ValormaxValue(1, SensorArray[i]);
                    //Existe una condicion para funcionar si no hay alarmas
                    if(!rangomin.matches("") || !rangomax.matches("" )){
                        int rangominint = Integer.parseInt(rangomin);
                        int rangomaxint = Integer.parseInt(rangomax);
                        String rango1 = rangomin;
                        String rango2 = rangomax;
                        Log.d("Modbus", rango1);
                        Log.d("Modbus", rango2);
                        //Si hay alarmas, comparar el sensor actual del ciclo
                        //Con los valores de alarma
                        if ((ModbusArray[i] > rangomaxint || ModbusArray[i] < rangominint) &&
                                !AlarmSent) {
                            //Si hay una alarma y no se ha enviado una antes
                            //Escribir los mensajes de alarma
                            String MailAlarma = db.MailValue(1, SensorArray[i]);
                            String CelAlarma = db.CelValue(1, SensorArray[i]);
                            String MensajeAlarmaMail = "ALERTA: Valor del sensor " +
                                    String.valueOf(SensorArray[i] +
                                        " fuera de rango aceptable:" + "\n" + ValoresText);
                            String MensajeAlarmaCel = "ALERTA: Valor del sensor " +
                                    String.valueOf(SensorArray[i] +
                                        " fuera de rango aceptable");
                            //Enviar SMS y correo electronico de alarma
                            sendSMS(CelAlarma, MensajeAlarmaCel);
                            sendMail(MailAlarma, MensajeAlarmaMail);
                            //Se coloca verdadera la variable de 'alarma enviada'
                            AlarmSent = true;
                        }
                    }
                    i++;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ValoresView.setText(ValoresText);
                        //Si se envio una alarma, activar texto de alarmas
                        if(AlarmSent){
                            tvAlarmas.setText("ALARMA ENVIADA. Para reactivar " +
                                    "las alarmas debe reiniciar la medici\u00f3n");
                        }
                    }
                });
            } catch (ModbusInitException | ModbusTransportException e) {
                e.printStackTrace();
                Resp = "Error: " + e.toString();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ValoresView.setText(Resp);
                    }
                });
            } catch(java.lang.IllegalStateException e){
                e.printStackTrace();
                Resp = "Error: " + e.toString();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ValoresView.setText(Resp);
                    }            });
            }catch(java.lang.RuntimeException e){
                e.printStackTrace();
                Resp = "Error: " + e.toString();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ValoresView.setText(Resp);
                    }            });
            }
            finally {
                //Se destruye el maestro de modbus
                //Que se inicializa en el proximo ciclo
                master.destroy();
            }
            return ValoresText;
        }
        @Override
        //Se actualiza la pantalla principal con los valores guardados previamente
        protected void onProgressUpdate(String... values) {
            ValoresView.setText(ValoresText);
        }
    }
}
