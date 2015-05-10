package com.teamap.mydemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.teamap.mydemo.DataBaseHandler.ConnectionDetector;
import com.teamap.mydemo.DataBaseHandler.DBController;
import com.teamap.mydemo.Entity.GlobalVariables;
import com.teamap.mydemo.Entity.tblAnh;
import com.teamap.mydemo.Entity.tblQuan;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ActionBarActivity {


    DBController controller = new DBController(this);
    HashMap<String, String> queryValues;
    ProgressDialog prgDialog;
    ConnectionDetector cd;
    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[] = {"Trang chủ", "Danh mục"};
    int Numboftabs = 2;
    File file = new File(GlobalVariables.PATH + "/images/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        cd = new ConnectionDetector(getApplicationContext());


        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Đang tải dữ liệu. Hãy đợi trong ít phút...");
        prgDialog.setCancelable(false);


        if (GlobalVariables.danhmucGList.size() == 0) {
            GlobalVariables.danhmucGList = controller.getAllDanhmuc();
//            Toast.makeText(getApplicationContext(), "Loaded Danh muc: "+ GlobalVariables.danhmucGList.size(),
//                    Toast.LENGTH_SHORT).show();
        }
        if (GlobalVariables.quanGList.size() == 0) {
            GlobalVariables.quanGList = controller.getAllQuan();
//            Toast.makeText(getApplicationContext(), "Loaded Quan: "+ GlobalVariables.quanGList.size(),
//                    Toast.LENGTH_SHORT).show();
        }
        if (GlobalVariables.anhGList.size() == 0) {
            GlobalVariables.anhGList = controller.getAllAnh();
//            Toast.makeText(getApplicationContext(), "Loaded Anh: "+ GlobalVariables.anhGList.size(),
//                    Toast.LENGTH_SHORT).show();
        }

        if (GlobalVariables.quanAllGList.size() == 0) {
            GlobalVariables.quanAllGList = controller.getListQuan();
//            Toast.makeText(getApplicationContext(), "Loaded Anh: "+ GlobalVariables.anhGList.size(),
//                    Toast.LENGTH_SHORT).show();
        }
        file.mkdirs();



        // Creating The Toolbar and setting it as the Toolbar for the activity

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.refresh) {
            // Transfer data from remote MySQL DB to SQLite on Android and perform Sync
            if (cd.isConnectingToInternet()) {
                syncSQLiteMySQLDB();
                long startTime = System.currentTimeMillis();
                for (tblAnh downImg : GlobalVariables.anhGList) {
                    DownloadFromUrl(GlobalVariables.PATH + "/images/" + downImg.getLinkanh(), GlobalVariables.SERVER_URL + "uploads/" + downImg.getLinkanh());
                    //Toast.makeText(getApplicationContext(),downImg.getLinkanh(),Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getApplicationContext(), "Quá trình cập nhật hoàn tất !", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Hãy kiểm tra kết nối Internet", Toast.LENGTH_SHORT).show();

            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void syncSQLiteMySQLDB() {
        // Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        // Http Request Params Object
        RequestParams params = new RequestParams();
        // Show ProgressBar
        prgDialog.show();
        // Make Http call to getusers.php
        client.post(GlobalVariables.SERVER_URL + "get_all_danhmuc.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                // Hide ProgressBar
                prgDialog.hide();
                // Update SQLite DB with response sent by getusers.php

                updateSQLiteDM(response);
            }

            // When error occured
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                // TODO Auto-generated method stub
                // Hide ProgressBar
                prgDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        client.post(GlobalVariables.SERVER_URL + "get_all_quan.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                // Hide ProgressBar
                prgDialog.hide();
                // Update SQLite DB with response sent by getusers.php
                updateSQLiteQuan(response);
            }

            // When error occured
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                // TODO Auto-generated method stub
                // Hide ProgressBar
                prgDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        client.post(GlobalVariables.SERVER_URL + "get_all_anh.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                // Hide ProgressBar
                prgDialog.hide();
                // Update SQLite DB with response sent by getusers.php
                updateSQLiteAnh(response);
            }

            // When error occured
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                // TODO Auto-generated method stub
                // Hide ProgressBar
                prgDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void updateSQLiteDM(String response) {
        ArrayList<HashMap<String, String>> usersynclist;
        usersynclist = new ArrayList<HashMap<String, String>>();
        // Create GSON object
        Gson gson = new GsonBuilder().create();
        try {
            // Extract JSON array from the response
            JSONArray arr = new JSONArray(response);
            System.out.println(arr.length());
            // If no of array elements is not zero
            controller.removeDM();
            if (arr.length() != 0) {
                // Loop through each array element, get JSON object which has userid and username
                for (int i = 0; i < arr.length(); i++) {
                    // Get JSON object
                    JSONObject obj = (JSONObject) arr.get(i);
                    System.out.println(obj.get("PK_IDDanhmuc"));
                    System.out.println(obj.get("tendanhmuc"));
                    queryValues = new HashMap<String, String>();
                    queryValues.put("PK_IDDanhmuc", obj.get("PK_IDDanhmuc").toString());
                    queryValues.put("tendanhmuc", obj.get("tendanhmuc").toString());
                    controller.insertDanhmuc(queryValues);

                }
                // Inform Remote MySQL DB about the completion of Sync activity by passing Sync status of Users
                //updateMySQLSyncSts(gson.toJson(usersynclist));
                // Reload the Main Activity
                reloadActivity();

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void updateSQLiteQuan(String response) {
//        List<tblQuan> quanList=new ArrayList<tblQuan>();
        // Create GSON object
        Gson gson = new GsonBuilder().create();
        try {
            // Extract JSON array from the response
            JSONArray arr = new JSONArray(response);
            System.out.println(arr.length());
            // If no of array elements is not zero
            if (arr.length() != 0) {
                // Loop through each array element, get JSON object which has userid and username
                tblQuan quans = new tblQuan();
                controller.removeQuan();
                for (int i = 0; i < arr.length(); i++) {
                    // Get JSON object
                    JSONObject obj = (JSONObject) arr.get(i);
                    System.out.println(obj.get("PK_IDQuan").toString());
                    System.out.println(obj.get("tenquan").toString());
                    quans.setPK_IDQuan(Integer.parseInt(obj.get("PK_IDQuan").toString()));
                    quans.setTenquan(obj.get("tenquan").toString());
                    quans.setDiachi(obj.get("diachi").toString());
                    quans.setToado(obj.get("toado").toString());
                    quans.setDaden(Integer.parseInt(obj.get("daden").toString()));
                    quans.setYeuthich(Integer.parseInt(obj.get("yeuthich").toString()));
                    quans.setMota(obj.get("mota").toString());
                    quans.setMonnoibat(obj.get("monnoibat").toString());
                    quans.setFK_IDDanhmuc(Integer.parseInt(obj.get("FK_IDDanhmuc").toString()));

                    controller.insertQuan(quans);

                }
                // Inform Remote MySQL DB about the completion of Sync activity by passing Sync status of Users
                //updateMySQLSyncSts(gson.toJson(usersynclist));
                // Reload the Main Activity

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void updateSQLiteAnh(String response) {
        Gson gson = new GsonBuilder().create();
        try {
            // Extract JSON array from the response
            JSONArray arr = new JSONArray(response);
            System.out.println(arr.length());
            // If no of array elements is not zero
            if (arr.length() != 0) {
                // Loop through each array element, get JSON object which has userid and username
                tblAnh anhs = new tblAnh();
                controller.removeAnh();
                for (int i = 0; i < arr.length(); i++) {
                    // Get JSON object
                    JSONObject obj = (JSONObject) arr.get(i);
                    anhs.setPK_IDAnh(Integer.parseInt(obj.get("PK_IDAnh").toString()));
                    anhs.setAnhcover(Integer.parseInt(obj.get("anhcover").toString()));
                    anhs.setLinkanh(obj.get("linkanh").toString());
                    anhs.setFK_IDQuan(Integer.parseInt(obj.get("FK_IDQuan").toString()));
                    controller.insertQuan(anhs);

                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    // Reload MainActivity
    public void reloadActivity() {
        Intent objIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(objIntent);
    }


    public void DownloadFromUrl(String fileName, String urlImage) {

        try {
            URL url = new URL(urlImage); //you can write here any link
            File file = new File(fileName);
            long startTime = System.currentTimeMillis();
            URLConnection ucon = url.openConnection();
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayBuffer baf = new ByteArrayBuffer(50);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baf.toByteArray());
            fos.close();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error:" + e, Toast.LENGTH_SHORT).show();
        }


    }
}
