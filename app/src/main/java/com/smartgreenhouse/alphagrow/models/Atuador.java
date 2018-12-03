package com.smartgreenhouse.alphagrow.models;

public class Atuador {
    private String id;
    private String atuador;
    private boolean estado;
    private String token;

    public Atuador() {
    }

    public Atuador(String atuador, boolean estado) {
        this.atuador = atuador;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getAtuador() {
        return atuador;
    }
    public void setAtuador(String atuador) {
        this.atuador = atuador;
    }
    public boolean isEstado() {
        return estado;
    }
    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
