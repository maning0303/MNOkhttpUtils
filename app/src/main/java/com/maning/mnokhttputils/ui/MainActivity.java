package com.maning.mnokhttputils.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.maning.library_httputils.OkhttpUtils;
import com.maning.mnokhttputils.http.HttpCommonCallback;
import com.maning.mnokhttputils.R;
import com.maning.mnokhttputils.model.GankModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 测试网络请求
     */
    private Button mBtnTest01;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("加载中...");
    }

    private void initView() {
        mBtnTest01 = (Button) findViewById(R.id.btn_test_01);
        mBtnTest01.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_test_01:
                //https://gank.io/api/data/Android/10/1
                OkhttpUtils
                        .with()
                        .get()
                        .url("https://gank.io/api/data/Android/100/1")
                        .execute(new HttpCommonCallback<GankModel>() {
                            @Override
                            public void onFailure(String errorCode, String errorMsg) {
                                Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSuccess(GankModel body) {
                                Log.i(">>>>>>>", "onSuccess1");
                                if (body != null) {
                                    Log.i("------", body.toString());
                                }
                            }

                            @Override
                            public void onSuccess(List<GankModel> bodys) {
                                Log.i(">>>>>>>", "onSuccess2");
                                if (bodys != null) {
                                    Toast.makeText(MainActivity.this, bodys.size() + "", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onStart() {
                                Log.i(">>>>>>>", "onStart");
                                progressDialog.show();
                            }

                            @Override
                            public void onFinish() {
                                Log.i(">>>>>>>", "onFinish");
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            }
                        });


                break;
        }
    }
}
