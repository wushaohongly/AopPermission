package com.wushaohong.aoppermission;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wushaohong.library.AopPermissionsConfig;
import com.wushaohong.library.permissions.PermissionsCheck;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // BaseActivity / 或者 BaseFragment当中设置
        // 将自定义的Dialog，设置到 library中
        AopPermissionsConfig.setRefuseDialog(new MyRefuseDialog(this));
    }

    @PermissionsCheck({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE})
    public void click(View view) {
        Toast.makeText(this, "权限申请成功", Toast.LENGTH_LONG).show();
    }
}
