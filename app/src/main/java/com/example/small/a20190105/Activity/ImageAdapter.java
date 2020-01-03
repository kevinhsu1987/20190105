package com.example.small.a20190105.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.small.a20190105.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends BaseAdapter  {

    private ViewGroup layout;
    private Context context;
    private List thumbIds;

    public ImageAdapter(Context context,List thumbIds) {
        super();
        this.context = context;
        this.thumbIds = thumbIds;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowview = inflater.inflate(R.layout.grid_item, parent, false);
        layout = rowview.findViewById(R.id.rl_item_photo);
        ImageView image = rowview.findViewById(R.id.image);
        TextView tv = rowview.findViewById(R.id.text);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float dd = dm.density;
        float px = 100 * dd;
        float screenWidth = dm.widthPixels;
        int newWidth = (int) (screenWidth-px)/2 ; // 一行顯示四個縮圖

        layout.setLayoutParams(new GridView.LayoutParams(650, 750));
        image.setImageBitmap(getDiskBitmap(getImagePathFromSD().get(position)));
        image.setScaleType(ImageView.ScaleType.FIT_XY);

        String filePath = "/storage/emulated/0/Pictures/Android_Class" ;
        File fileAll = new File(filePath);
        File[] files = fileAll.listFiles();
        File file1 = files[position];
        tv.setText(file1.getName());

        return rowview;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return thumbIds.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return thumbIds.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    public static Bitmap getDiskBitmap(String path) {
        Bitmap bitmap = null;
        if(TextUtils.isEmpty(path)) {
            return bitmap;
        }
        try {
            File file = new File(path);
            if (file.exists()) {
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inPreferredConfig = Bitmap.Config.RGB_565;
                bitmap = BitmapFactory.decodeFile(path, opt);
            }
        } catch (Exception e) {
        }
        return bitmap;
    }

    private List<String> getImagePathFromSD() {
        List<String> imagePathList = new ArrayList<String>();
        String filePath = "/storage/emulated/0/Pictures/Android_Class" ;
        File fileAll = new File(filePath);
        File[] files = fileAll.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (checkIsImageFile(file.getPath())) {
                imagePathList.add(file.getPath());
            }
        }
        return imagePathList;
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
