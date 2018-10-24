package com.smartgreenhouse.alphagrow.models;

import java.util.List;

public class Cultivo {
    private String id;
    private String nomeCultivo;
    //Será no cultivo que os ciclos estarão atrelados
    private List<Ciclo> ciclos;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getNomeCultivo() {
        return nomeCultivo;
    }
    public void setNomeCultivo(String nomeCultivo) {
        this.nomeCultivo = nomeCultivo;
    }
    public List<Ciclo> getCiclos() {
        return ciclos;
    }
    public void setCiclos(List<Ciclo> ciclos) {
        this.ciclos = ciclos;
    }
}
