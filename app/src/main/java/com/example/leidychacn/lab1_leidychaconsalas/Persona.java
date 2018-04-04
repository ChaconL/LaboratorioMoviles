package com.example.leidychacn.lab1_leidychaconsalas;

import android.net.Uri;

/**
 * Created by Leidy Chacón on 4/4/2018.
 */

public class Persona {

    private String nombre;
    private  String profession;
    private  String sex;
    private Uri foto;

    public Persona(String nombre, String profession, String sex, Uri foto) {
        this.nombre = nombre;
        this.profession = profession;
        this.sex = sex;
        this.foto = foto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Uri getFoto() {
        return foto;
    }

    public void setFoto(Uri foto) {
        this.foto = foto;
    }
}
