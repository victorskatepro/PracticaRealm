package com.victorsaico.practicarealm.activities.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by JARVIS on 18/12/2017.
 */

public class Usuario extends RealmObject {
    @PrimaryKey
    private int id;

    private String nombre;
    private String correo;
    private int telefono;
    private String nacionalidad;
    private String empresa;
    private String contrasena;
    private String imagenprofile;

    private RealmList<Publicacion> publicacions;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }



    public RealmList<Publicacion> getPublicacions() {
        return publicacions;
    }

    public void setPublicacions(RealmList<Publicacion> publicacions) {
        this.publicacions = publicacions;
    }

    public String getImagenprofile() {
        return imagenprofile;
    }

    public void setImagenprofile(String imagenprofile) {
        this.imagenprofile = imagenprofile;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", telefono=" + telefono +
                ", nacionalidad='" + nacionalidad + '\'' +
                ", empresa='" + empresa + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", imagenprofile='" + imagenprofile + '\'' +
                ", publicacions=" + publicacions +
                '}';
    }
}
