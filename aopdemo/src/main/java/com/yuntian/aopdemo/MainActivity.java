package com.yuntian.aopdemo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.meituan.android.walle.WalleChannelReader;
import com.yuntian.aoplib.annotation.DebugTrace;
import com.yuntian.aoplib.annotation.LogTime;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";

    private TextView tv_aspect;
    private TextView tv_javassist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_aspect = findViewById(R.id.tv_aspect);
        tv_javassist = findViewById(R.id.tv_javassist);

        tv_aspect.setOnClickListener((v) -> {
            testAnnotatedMethod();
        });
        tv_javassist.setOnClickListener((v) -> {
            testJavassistMethod();
        });

        String channel = WalleChannelReader.getChannel(this.getApplicationContext());

        Log.d(TAG, "onCreate: " + channel);
        Log.d(TAG, "onCreate: " + isApkInDebug(this));
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

    //判断当前应用是否是debug状态
    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }


}
