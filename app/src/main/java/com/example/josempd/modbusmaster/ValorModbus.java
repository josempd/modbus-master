package com.example.josempd.modbusmaster;

/**
 * Created by Josempd on 15/04/2015.
 */
public class ValorModbus {

    //variables privadas
    int _id;
    int _RTU;
    int _idSensor;
    int _valor;

    // Constructor vacio
    public ValorModbus(){

    }
    // constructor
    public ValorModbus(int id, int RTU, int sensor, int valor){
        this._id = id;
        this._RTU = RTU;
        this._idSensor = sensor;
        this._valor = valor;
    }

    // constructor para valores sin ID
    public ValorModbus(int RTU, int sensor, int valor){
        this._RTU = RTU;
        this._idSensor = sensor;
        this._valor = valor;
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

    // obteniendo valor
    public int getValor(){
        return this._valor;
    }

    // colocando valor
    public void setValor(int valor){
        this._valor = valor;
    }
}
