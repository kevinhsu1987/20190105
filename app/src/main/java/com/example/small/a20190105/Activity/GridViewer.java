package com.example.small.a20190105.Activity;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import com.example.small.a20190105.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GridViewer extends AppCompatActivity {

    private GridView gridView;
    private List<Integer> id;  //存放圖片的路徑
    private ImageAdapter imageAdapter;  //用來顯示縮圖

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view);

        gridView = findViewById(R.id.gridView1);
        id = new ArrayList<Integer>();
        String filePath = Environment.getExternalStorageDirectory().toString() + File.separator
                + "Pictures/Android_Class";
        File fileAll = new File(filePath);
        File[] files = fileAll.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (checkIsImageFile(file.getPath())) {
                id.add(i);
            }
        }

        imageAdapter = new ImageAdapter(GridViewer.this,id);
        gridView.setAdapter(imageAdapter);
        imageAdapter.notifyDataSetChanged();
    }

    private boolean checkIsImageFile(String fName) {
        boolean isImageFile = false;
        String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
                fName.length()).toLowerCase();
        if (FileEnd.equals("jpg") || FileEnd.equals("png") || FileEnd.equals("gif")
                || FileEnd.equals("jpeg")|| FileEnd.equals("bmp") ) {
            isImageFile = true;
        } else {
            isImageFile = false;
        }
        return isImageFile;
    }


}
