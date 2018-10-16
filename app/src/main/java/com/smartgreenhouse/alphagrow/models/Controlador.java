package com.smartgreenhouse.alphagrow.models;

import com.google.gson.annotations.SerializedName;

public class Controlador {

    @SerializedName("nomeControlador")
    private String nomeControlador;

    @SerializedName("valor")
    private String valorControlador;

    @SerializedName("id")
    private String id;

    public String getNomeControlador() {
        return nomeControlador;
    }

    public void setNomeControlador(String nomeControlador) {
        this.nomeControlador = nomeControlador;
    }

    public String getValorControlador() {
        return valorControlador;
    }

    public void setValorControlador(String valorControlador) {
        this.valorControlador = valorControlador;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "\nControlador{" +
                "nomeControlador='" + nomeControlador + '\'' +
                ", valorControlador='" + valorControlador + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
