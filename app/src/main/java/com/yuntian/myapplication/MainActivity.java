package com.yuntian.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.yuntian.aoplib.annotation.DebugTrace;
import com.yuntian.aoplib.annotation.LogTime;

public class MainActivity extends AppCompatActivity {

    private TextView tv_aspect;
    private TextView tv_javassist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_aspect=findViewById(R.id.tv_aspect);
        tv_javassist=findViewById(R.id.tv_javassist);

        tv_aspect.setOnClickListener((v)->{ testAnnotatedMethod();});
        tv_javassist.setOnClickListener((v)->{ testJavassistMethod();});

    }

    @DebugTrace
    private void testAnnotatedMethod() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @LogTime
    private void testJavassistMethod() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
