package com.victorsaico.practicarealm.activities.models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by JARVIS on 18/12/2017.
 */

public class Publicacion extends RealmObject {
    @PrimaryKey
    private int id;

    private String titulo;
    private Date fecha;
    private String imagen;


public Publicacion()
{

}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return "Publicacion{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", fecha=" + fecha +
                ", imagen='" + imagen + '\'' +
                '}';
    }
}
