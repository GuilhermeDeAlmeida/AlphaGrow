package com.smartgreenhouse.alphagrow.models;

public class Autenticacao {
    private String id;

    public Boolean isAutenticado() {
        return autenticado;
    }

    public void setAutenticado(Boolean autenticado) {
        this.autenticado = autenticado;
    }

    private Boolean autenticado;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
