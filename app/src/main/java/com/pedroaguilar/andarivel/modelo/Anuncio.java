package com.pedroaguilar.andarivel.modelo;

/**
 * Proyect: Andarivel
 * From: com.pedroaguilar.andarivel.modelo
 * Create by Pedro Aguilar Fernández on 15/12/2022 at 19:36
 * More info: linkedin.com/in/pedro-aguilar-fernández-167753140
 * All rights reserved 2022
 **/
public class Anuncio {
    private String id = null;
    private String title = null;
    private String descripcion = null;
    private String imgUrl = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}