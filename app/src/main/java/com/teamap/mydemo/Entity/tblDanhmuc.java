package com.teamap.mydemo.Entity;

/**
 * Created by Gaming on 4/28/2015.
 */
public class tblDanhmuc {
    private int PK_IDDanhmuc;
    private String tendanhmuc;


    public tblDanhmuc() {
    }

    public tblDanhmuc(int PK_IDDanhmuc, String tendanhmuc) {
        this.PK_IDDanhmuc = PK_IDDanhmuc;
        this.tendanhmuc = tendanhmuc;
    }

    public int getPK_IDDanhmuc() {
        return PK_IDDanhmuc;
    }

    public void setPK_IDDanhmuc(int PK_IDDanhmuc) {
        this.PK_IDDanhmuc = PK_IDDanhmuc;
    }

    public String getTendanhmuc() {
        return tendanhmuc;
    }

    public void setTendanhmuc(String tendanhmuc) {
        this.tendanhmuc = tendanhmuc;
    }
}
