package com.teamap.mydemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
import com.teamap.mydemo.DataBaseHandler.imageOnServer;
import com.teamap.mydemo.Entity.GlobalVariables;
import com.teamap.mydemo.Entity.tblQuan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends ActionBarActivity {


    DBController controller = new DBController(this);
    HashMap<String, String> queryValues;
    ProgressDialog prgDialog;
    ConnectionDetector cd;
    File mediaStorageDir;
    tblQuan quan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cd=new ConnectionDetector(getApplicationContext());
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Đang tải dữ liệu. Hãy đợi trong ít phút...");
        prgDialog.setCancelable(false);


        // Xử lý thông tin file ảnh
        File file;
        file = getFileFromUri("file://" + mediaStorageDir.getPath() + "/" + quan.getAnhdaidien());

        if (file == null && cd.isConnectingToInternet()) {
            try {
                imageOnServer.downloadFileFromServer(quan.getAnhdaidien());
                file = getFileFromUri("file://" + mediaStorageDir.getPath() + "/" + quan.getAnhdaidien());
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
				/*Toast.makeText(getApplicationContext(), e.getMessage(),
						Toast.LENGTH_LONG).show();*/
            } catch (IOException e) {
                // TODO Auto-generated catch block
				/*Toast.makeText(getApplicationContext(), e.getMessage(),
						Toast.LENGTH_LONG).show();*/
            }
        }

//        if (file != null) { // Nếu file khác null thì sẽ được Decode thành file
//            // Bitmap và hiển thị
//            Bitmap bm = decodeSampledBitmapFromFile(file, 500, 500);
//            anh.setImageBitmap(bm);
//        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        if (GlobalVariables.danhmucGList.size()==0){
            GlobalVariables.danhmucGList=controller.getAllDanhmuc();
            Toast.makeText(getApplicationContext(), "Loaded Danh muc",
            Toast.LENGTH_LONG).show();
        }

        if (GlobalVariables.quanGList.size()==0){
            GlobalVariables.quanGList=controller.getAllQuan();
            Toast.makeText(getApplicationContext(), "Loaded Quan",
                    Toast.LENGTH_LONG).show();
        }
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
                syncSQLiteMySQLDBDM();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Hãy kiểm tra kết nối Internet",Toast.LENGTH_SHORT).show();

            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void syncSQLiteMySQLDBDM() {
        // Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        // Http Request Params Object
        RequestParams params = new RequestParams();
        // Show ProgressBar
        prgDialog.show();
        // Make Http call to getusers.php
        client.post(GlobalVariables.SERVER_URL+"get_all_danhmuc.php", params, new AsyncHttpResponseHandler() {
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

        client.post(GlobalVariables.SERVER_URL+"get_all_quan.php", params, new AsyncHttpResponseHandler() {
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
                tblQuan quans=new tblQuan();
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
                    quans.setAnhdaidien(obj.get("anhdaidien").toString());
                    quans.setMota(obj.get("mota").toString());
                    quans.setMonnoibat(obj.get("monnoibat").toString());
                    quans.setFK_IDDanhmuc(Integer.parseInt(obj.get("FK_IDDanhmuc").toString()));

                    controller.insertQuan(quans);

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

    // Reload MainActivity
    public void reloadActivity() {
        Intent objIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(objIntent);
    }



    private Bitmap decodeSampledBitmapFromFile(File file, int reqWidth,
                                               int reqHeight) {
        // TODO Auto-generated method stub
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }

    private File getFileFromUri(String imgUri) {
        // TODO Auto-generated method stub

        try {
            URI uri = URI.create(imgUri);
            File file = new File(uri);
            if (file != null) {
                if (file.canRead()) {
                    return file;
                }
            }
        } catch (Exception e) {
            return null;
        }

        return null;
    }

    /** Calculate the scaling factor */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public String getImageName() { // Xử lý chuỗi Uril của ảnh để lấy ra tên ảnh
        String imageName = "";
        File file = getFileFromUri(quan.getAnhdaidien());
        if (file != null) {
            String temp[] = quan.getAnhdaidien().split("/");
            imageName = temp[temp.length - 1];
        } else {
            imageName = quan.getAnhdaidien();
        }
        return imageName;
}
}
