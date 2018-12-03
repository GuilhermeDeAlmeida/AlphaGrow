package com.smartgreenhouse.alphagrow.models;

public class Autenticacao {
    private String id;
    private String token;
    private Boolean autenticado;

    public Boolean isAutenticado() {
        return autenticado;
    }

    public void setAutenticado(Boolean autenticado) {
        this.autenticado = autenticado;
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
}
