package com.teamap.mydemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.teamap.mydemo.Entity.GlobalVariables;

import java.io.File;


public class Tab1 extends Fragment {

    File file;
    Button btnImage;
    ImageView iv;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //file = new File(GlobalVariables.PATH + "/images/" + File.separator + "/abc.jpg");

        View v = inflater.inflate(R.layout.tab_1, container, false);
        /*iv = (ImageView) v.findViewById(R.id.ivTest);
        btnImage = (Button) v.findViewById(R.id.button);
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap myBitmap = BitmapFactory.decodeFile(file.getPath());
                iv.setImageBitmap(myBitmap);

            }
        });
*/

        return v;
    }

//
}