package com.victorsaico.practicarealm.activities.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by JARVIS on 18/12/2017.
 */

public class Publicacion extends RealmObject {
    @PrimaryKey
    private int id;

    private String titulo;
    private String fecha;
    private Integer imagen;


    public Publicacion(int id, String titulo, String fecha, Integer imagen) {
        this.id = id;
        this.titulo = titulo;
        this.fecha = fecha;
        this.imagen = imagen;
    }
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Integer getImagen() {
        return imagen;
    }

    public void setImagen(Integer imagen) {
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return "Publicacion{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", fecha='" + fecha + '\'' +
                ", imagen=" + imagen +
                '}';
    }
}
