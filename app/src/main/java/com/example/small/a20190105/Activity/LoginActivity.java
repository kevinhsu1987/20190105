package com.example.small.a20190105.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.small.a20190105.JsonPlaceHolderApi;
import com.example.small.a20190105.Model.Student;
import com.example.small.a20190105.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    EditText studentID;
    EditText password;
    Button register_btn;
    Button login_btn;
    Button exit_btn;

    String studentID_val;
    String password_val;
    String username_val;

    boolean flag=false ;
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    private final static SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private final static Date curDate =new Date(System.currentTimeMillis());
    private final static String str=formatter.format(curDate) ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findView();
    }
    public void findView(){
        studentID = findViewById(R.id.studentID);
        password = findViewById(R.id.password);
        register_btn = findViewById(R.id.register_btn);
        login_btn = findViewById(R.id.login_btn);
        exit_btn = findViewById(R.id.exit_btn);
    }
    public void exit_btn(View view){
        finish();
    }
    public void register_btn(View view){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }
    public void login_btn(View view) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(JsonPlaceHolderApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        getStudent();
    }

    private void getStudent() {


        studentID_val = studentID.getText().toString();
        password_val = password.getText().toString();

        Call<List<Student>> call = jsonPlaceHolderApi.getStudent();
        call.enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {

                List<Student> students = response.body();

                for (Student student : students) {
                    if (student.getStudentID().equals(studentID_val)) {

                        if (student.getPassword().equals(password_val)) {
                            username_val = student.getUsername();
                            flag = true;
                            break;
                        }
                    }
                }
                if (flag) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("studentID", studentID_val);
                    intent.putExtra("username",username_val);
                    intent.putExtra("datetime", str);

                    startActivity(intent);
                    flag=false;
                }
                else{
                    Toast.makeText(LoginActivity.this, "登入之敗，請重新登入", Toast.LENGTH_LONG).show();
                }
            }


            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();


            }
        });


    }






}
