package com.yuntian.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tv_aspect;
    private TextView tv_javassist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_aspect=findViewById(R.id.tv_aspect);
        tv_javassist=findViewById(R.id.tv_javassist);

    }


}
