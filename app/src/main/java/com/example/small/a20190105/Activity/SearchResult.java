package com.example.small.a20190105.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.small.a20190105.R;

public class SearchResult extends AppCompatActivity {

    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Intent intent = getIntent();
        result =  intent.getStringExtra("result");

        TextView output = findViewById(R.id.result);
        output.setText(result);
    }

    public void search_exit_bt(View view) {
        finish();
    }
}
