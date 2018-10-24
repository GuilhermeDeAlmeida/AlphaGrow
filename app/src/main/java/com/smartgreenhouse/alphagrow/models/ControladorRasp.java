package com.smartgreenhouse.alphagrow.models;

public class ControladorRasp {

    private String id;
    private String temperatura;
    private String umidade;


    public String getTemperatura() {
        return temperatura;
    }
    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }
    public String getUmidade() {
        return umidade;
    }
    public void setUmidade(String umidade) {
        this.umidade = umidade;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

}
