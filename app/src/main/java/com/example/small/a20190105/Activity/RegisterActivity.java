package com.example.small.a20190105.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.small.a20190105.JsonPlaceHolderApi;
import com.example.small.a20190105.Model.Student;
import com.example.small.a20190105.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    EditText username;
    EditText studentID;
    EditText email;
    EditText password;

    String username_val;
    String studentID_val;
    String email_val;
    String password_val;


    Button register;
    Button cancel;
    private JsonPlaceHolderApi jsonPlaceHolderApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        finView();
    }
    public void finView(){
        username = (EditText)findViewById(R.id.username);
        studentID = (EditText)findViewById(R.id.studentID);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);

        register = (Button) findViewById(R.id.register_btn);
        cancel = (Button) findViewById(R.id.cancel_btn);


    }
    public void cancel_btn_click(View view){
        finish();
    }
    public void register_btn_click(View view){
        username_val = username.getText().toString();
        studentID_val = studentID.getText().toString();
        email_val = email.getText().toString();
        password_val = password.getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(JsonPlaceHolderApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        createpost();


    }
    private void createpost(){
        Student student = new Student(username_val,studentID_val,email_val,password_val);
        Call<Student> call = jsonPlaceHolderApi.PostStudent(student);
        call.enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                if(!response.isSuccessful()){
                    return;
                }
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {

            }
        });
    }


}
