package com.teamap.mydemo.Entity;

/**
 * Created by Gaming on 4/28/2015.
 */
public class tblQuan {
    private int PK_IDQuan;
    private String tenquan;
    private String diachi;
    private String toado;
    private int daden;
    private int yeuthich;
    private String mota;
    private String monnoibat;
    private int FK_IDDanhmuc;
    private String anhdaidien;

    public String getAnhdaidien() {
        return anhdaidien;
    }

    public void setAnhdaidien(String anhdaidien) {
        this.anhdaidien = anhdaidien;
    }

    public int getPK_IDQuan() {
        return PK_IDQuan;
    }

    public void setPK_IDQuan(int PK_IDQuan) {
        this.PK_IDQuan = PK_IDQuan;
    }

    public String getTenquan() {
        return tenquan;
    }

    public void setTenquan(String tenquan) {
        this.tenquan = tenquan;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getToado() {
        return toado;
    }

    public void setToado(String toado) {
        this.toado = toado;
    }

    public int getDaden() {
        return daden;
    }

    public void setDaden(int daden) {
        this.daden = daden;
    }

    public int getYeuthich() {
        return yeuthich;
    }

    public void setYeuthich(int yeuthich) {
        this.yeuthich = yeuthich;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getMonnoibat() {
        return monnoibat;
    }

    public void setMonnoibat(String monnoibat) {
        this.monnoibat = monnoibat;
    }

    public int getFK_IDDanhmuc() {
        return FK_IDDanhmuc;
    }

    public void setFK_IDDanhmuc(int FK_IDDanhmuc) {
        this.FK_IDDanhmuc = FK_IDDanhmuc;
    }

    public tblQuan(int PK_IDQuan, String tenquan, String diachi, String toado, int daden, int yeuthich, String mota, String monnoibat, int FK_IDDanhmuc, String anhdaidien) {
        this.PK_IDQuan = PK_IDQuan;
        this.tenquan = tenquan;
        this.diachi = diachi;
        this.toado = toado;
        this.daden = daden;
        this.yeuthich = yeuthich;
        this.mota = mota;
        this.monnoibat = monnoibat;
        this.FK_IDDanhmuc = FK_IDDanhmuc;
        this.anhdaidien = anhdaidien;
    }

    public tblQuan() {
    }
}
