package com.example.small.a20190105.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;

import com.example.small.a20190105.JsonPlaceHolderApi;
import com.example.small.a20190105.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    TextView tv;
    String studentID;
    String datetime;
    String username;
    JsonPlaceHolderApi jsonPlaceHolderApi;
    private ImageView img_image;
    private static Context mContext;

    private final String PERMISSION_WRITE_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";    //外部裝置權限
    private final String PREMISSION_CAMERA = "android.permission.CAMERA";                           //相機權限
    private final static SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss"); //日期格式
    private final static Date curDate = new Date(System.currentTimeMillis());                           //時間
    private final static String str = formatter.format(curDate);                                         //把時間放進日期格式
    private static final int REQUEST_IMAGE = 100;       //用於startActivityforResult的 Request code

    private static final int READ_BLOCK_SIZE = 100;
    private String fname = "note.txt";
    //GPS
    private TextView mTextView01, longitude_txt, latitude_txt;
    private boolean getService = false;     //是否已開啟定位服務
    private LocationManager lms;
    private Location location;
    private String bestProvider = LocationManager.GPS_PROVIDER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.textView);

        Intent intent = getIntent();
        studentID = intent.getStringExtra("studentID");
        username = intent.getStringExtra("username");
        datetime = intent.getStringExtra("datetime");

        String test = studentID + "\n" + datetime + "\n" + username;
        tv.setText(test);
        String content = username + ":" + datetime;
        String contentold = "";

        try {
            FileInputStream in = openFileInput(fname);
            InputStreamReader sr = new InputStreamReader(in);
            char[] buffer = new char[READ_BLOCK_SIZE];
            int count;
            while ((count = sr.read(buffer)) > 0) {
                String s = String.copyValueOf(buffer, 0, count);
                contentold += s;
                buffer = new char[READ_BLOCK_SIZE];
            }
            sr.close();     // 關閉串流
        } catch (IOException ex) {
            ex.printStackTrace();
        }


        try {
            // 開啟寫入檔案
            FileOutputStream out = openFileOutput(fname, MODE_PRIVATE);
            OutputStreamWriter sw = new OutputStreamWriter(out);
            sw.write(contentold + content + "\n");  // 將字串寫入串流
            sw.flush();     // 輸出串流資料
            sw.close();     // 關閉串流
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //GPS

        //mTextView01 = (TextView) findViewById(R.id.textView1);
        longitude_txt = (TextView) findViewById(R.id.longitude);
        latitude_txt = (TextView) findViewById(R.id.latitude);

        //取得系統定位服務
        LocationManager status = (LocationManager) (this.getSystemService(Context.LOCATION_SERVICE));
        if (status.isProviderEnabled(LocationManager.GPS_PROVIDER) || status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //如果GPS或網路定位開啟，呼叫locationServiceInitial()更新位置
            locationServiceInitial();
        } else {
            Toast.makeText(this, "請開啟定位服務", Toast.LENGTH_LONG).show();
            getService = true; //確認開啟定位服務
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)); //開啟設定頁面
        }


    }

    private void locationServiceInitial () {
      LocationManager lms = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE); //取得系統定位服務
        /* 做法一,由程式判斷用GPS_provider
        if (lms.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            location = lms.getLastKnownLocation(LocationManager.GPS_PROVIDER);  //使用GPS定位座標
        } else if (lms.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            location = lms.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); //使用GPS定位座標
         }
         //else {}*/
        //做法二,由Criteria物件判斷提供最準確的資訊
        Criteria criteria = new Criteria();  //資訊提供者選取標準
        bestProvider = lms.getBestProvider(criteria, true);    //選擇精準度最高的提供者
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = lms.getLastKnownLocation(bestProvider);

        getLocation(location);
    }
    private void getLocation(Location location) { //將定位資訊顯示在畫面中
        if (location != null) {
            Double longitude = location.getLongitude();   //取得經度
            Double latitude = location.getLatitude();     //取得緯度
            longitude_txt.setText(String.valueOf(longitude));
            latitude_txt.setText(String.valueOf(latitude));
        } else {
            Toast.makeText(this, "無法定位座標", Toast.LENGTH_LONG).show();
        }
    }

    public void exit_bt(View view) {
        finish();
    }

    public void search_bt(View view) {
        try {
            // 開啟讀取檔案
            FileInputStream in = openFileInput(fname);
            InputStreamReader sr = new InputStreamReader(in);
            char[] buffer = new char[READ_BLOCK_SIZE];
            String str = "";
            int count;
            // 讀取檔案內容
            while ((count = sr.read(buffer)) > 0) {
                String s = String.copyValueOf(buffer, 0, count);
                str += s;
                buffer = new char[READ_BLOCK_SIZE];
            }
            sr.close();     // 關閉串流
            Toast.makeText(this, "成功讀取檔案...",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, SearchResult.class);
            intent.putExtra("result", str);
            startActivity(intent);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void search_photo_bt(View view) {

        // 啟動內建相機程式
        if (!hasSDPermission()) {
            if (needCheckPermission()) {
                //如果須要檢查權限，由於這個步驟要等待使用者確認，
                //所以不能立即執行儲存的動作，
                //必須在 onRequestPermissionsResult 回應中才執行
                return;
            }
        } else if (!hasCamaraPermission()) {
            if (needCheckPermission()) {
                //如果須要檢查權限，由於這個步驟要等待使用者確認，
                //所以不能立即執行儲存的動作，
                //必須在 onRequestPermissionsResult 回應中才執行
                return;
            }
        } else if (hasCamaraPermission() && hasSDPermission()) {
            //確認權限都確認之後
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_IMAGE);
        }

    }

    private boolean needCheckPermission() {
        //MarshMallow(API-23)之後要在 Runtime 詢問權限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = {PERMISSION_WRITE_STORAGE, PREMISSION_CAMERA};
            int permsRequestCode = 200;
            requestPermissions(perms, permsRequestCode);
            return true;
        }
        return false;
    }

    private boolean hasSDPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return (ActivityCompat.checkSelfPermission(this, PERMISSION_WRITE_STORAGE) == PackageManager.PERMISSION_GRANTED);
        }
        return true;
    }

    private boolean hasCamaraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return (ActivityCompat.checkSelfPermission(this, PREMISSION_CAMERA) == PackageManager.PERMISSION_GRANTED);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        img_image = (ImageView) findViewById(R.id.img_image);
        if (requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK) {
            try {
                //拍照完回來做的事
                Bitmap userImage = (Bitmap) data.getExtras().get("data");
                File path = Environment.getExternalStoragePublicDirectory("/Pictures/Android_Class");
                OutputStream fOut = null;
                Toast.makeText(this, path.toString(), Toast.LENGTH_SHORT).show();
                img_image.setImageBitmap(userImage);
                File file = new File(path, studentID + str + ".jpg"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
                fOut = new FileOutputStream(file);
                userImage.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
                fOut.flush(); // Not really required
                fOut.close();

                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path.toString())));
                Log.d("TEST", "OK to save image" + path.toString());
            } catch (IOException e) {
                Log.d("TEST", "Failed to save image");
            }
        }
    }

    public void picture_bt(View view) {
        Intent intent = new Intent(this, GridViewer.class);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    //GPS

    public class GetLocation extends Activity implements LocationListener {



        @Override
        public void onLocationChanged(Location location) {  //當地點改變時
            // TODO 自動產生的方法 Stub
            getLocation(location);
        }

        @Override
        public void onProviderDisabled(String arg0) {//當GPS或網路定位功能關閉時
            // TODO 自動產生的方法 Stub
            Toast.makeText(this, "請開啟gps或3G網路", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderEnabled(String arg0) { //當GPS或網路定位功能開啟
            // TODO 自動產生的方法 Stub
        }

        @Override
        public void onStatusChanged(String arg0, int arg1, Bundle arg2) { //定位狀態改變
            // TODO 自動產生的方法 Stub
        }

        @Override
        protected void onResume() {
            // TODO Auto-generated method stub
            super.onResume();
            if (getService) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                lms.requestLocationUpdates(bestProvider, 1000, 1, this);
                //服務提供者、更新頻率60000毫秒=1分鐘、最短距離、地點改變時呼叫物件
            }
        }
        @Override
        protected void onPause() {
            // TODO Auto-generated method stub
            super.onPause();
            if(getService) {
                lms.removeUpdates(this);   //離開頁面時停止更新
            }
        }
    }






}
