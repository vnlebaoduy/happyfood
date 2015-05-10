package com.teamap.mydemo.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gaming on 5/5/2015.
 */
public class GlobalVariables {
    public static final String SERVER_URL = "http://192.168.0.101/syncDB/";
    public static final String PATH = "/data/data/com.teamap.mydemo/";
    public static List<tblQuan> quanGList = new ArrayList<tblQuan>();
    public static List<tblDanhmuc> danhmucGList=new ArrayList<tblDanhmuc>();
    public static List<tblAnh> anhGList=new ArrayList<tblAnh>();
    public static List<tblQuan> quanAllGList=new ArrayList<tblQuan>();
}
