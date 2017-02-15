package com.rinno.apumanque.models;

/**
 * Created by simaski on 03-02-17.
 */

public class Message {
    private String idGrupo;
    private String idPantalla;
    private String server;
    private String cast;

    public Message(String idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(String idGrupo) {
        this.idGrupo = idGrupo;
    }


}