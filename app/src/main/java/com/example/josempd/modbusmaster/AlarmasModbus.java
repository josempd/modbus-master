package com.example.josempd.modbusmaster;

/**
 * Created by Josempd on 23/04/2015.
 */
public class AlarmasModbus {

    //variables privadas
    int _id;
    int _RTU;
    int _idSensor;
    int _valormin;
    int _valormax;
    String _email;
    String _cel;

    // Constructor vacio
    public AlarmasModbus(){

    }
    // constructor
    public AlarmasModbus(int id, int RTU, int sensor, int valormin, int valormax,
                         String email, String cel){
        this._id = id;
        this._RTU = RTU;
        this._idSensor = sensor;
        this._valormin = valormin;
        this._valormax = valormax;
        this._email = email;
        this._cel = cel;

    }

    // constructor para valores de alarma sin ID
    public AlarmasModbus(int RTU, int sensor, int valormin, int valormax,
                         String email, String cel){
        this._RTU = RTU;
        this._idSensor = sensor;
        this._valormin = valormin;
        this._valormax = valormax;
        this._email = email;
        this._cel = cel;

    }
    // obteniendo ID
    public int getID(){
        return this._id;
    }

    // colocando ID
    public void setID(int id){
        this._id = id;
    }

    // obteniendo RTU
    public int getRTU(){
        return this._RTU;
    }

    // colocando RTU
    public void setRTU(int RTU){
        this._RTU = RTU;
    }

    // obteniendo Sensor
    public int getSensor(){
        return this._idSensor;
    }

    // colocando Sensor
    public void setSensor(int sensor){
        this._idSensor = sensor;
    }

    // obteniendo valormin
    public int getValormin(){
        return this._valormin;
    }

    // colocando valormin
    public void setValormin(int valor){
        this._valormin = valor;
    }

    // obteniendo valormax
    public int getValormax(){
        return this._valormax;
    }

    // colocando valormax
    public void setValormax(int valor){
        this._valormax = valor;
    }

    // obteniendo Mail
    public String getMail(){
        return this._email;
    }

    // colocando Mail
    public void setMail(String mail){ this._email = mail;    }

    // obteniendo Celular
    public String getCel(){
        return this._cel;
    }

    // colocando Celular
    public void setCel(String cel){ this._cel = cel;    }
}