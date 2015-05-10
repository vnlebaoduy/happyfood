package com.teamap.mydemo.Entity;

/**
 * Created by Gaming on 5/6/2015.
 */
public class tblAnh {
    private int PK_IDAnh;
    private String linkanh;
    private int anhcover;
    private int FK_IDQuan;

    public tblAnh() {
    }

    public tblAnh(int PK_IDAnh, String linkanh, int anhcover, int FK_IDQuan) {
        this.PK_IDAnh = PK_IDAnh;
        this.linkanh = linkanh;
        this.anhcover = anhcover;
        this.FK_IDQuan = FK_IDQuan;
    }

    public int getPK_IDAnh() {
        return PK_IDAnh;
    }

    public void setPK_IDAnh(int PK_IDAnh) {
        this.PK_IDAnh = PK_IDAnh;
    }

    public String getLinkanh() {
        return linkanh;
    }

    public void setLinkanh(String linkanh) {
        this.linkanh = linkanh;
    }

    public int getAnhcover() {
        return anhcover;
    }

    public void setAnhcover(int anhcover) {
        this.anhcover = anhcover;
    }

    public int getFK_IDQuan() {
        return FK_IDQuan;
    }

    public void setFK_IDQuan(int FK_IDQuan) {
        this.FK_IDQuan = FK_IDQuan;
    }
}
