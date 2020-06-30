package com.wushaohong.aoppermission;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.wushaohong.library.dialog.PermissionRefuseDialog;

public class MyRefuseDialog extends PermissionRefuseDialog {

    private TextView tvContent;
    private Button btCancel;
    private Button btOpen;

    public MyRefuseDialog(@NonNull Context context) {
        super(context);

        setContentView(R.layout.layout_my_refuse_dialog);

        tvContent = findViewById(R.id.tv_content_permission_dialog);
        btCancel = findViewById(R.id.bt_cancel_permission_dialog);
        btOpen = findViewById(R.id.bt_open_permission_dialog);

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        setCancelable(true);
    }

    @Override
    public void setPermissions(String[] permissions) {
        // 父类赋值 permissions
        super.setPermissions(permissions);

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < permissions.length; i++) {
            String p = permissions[i];
            stringBuilder.append(p);
            if (i != permissions.length - 1) {
                stringBuilder.append("\n");
            }
        }

        tvContent.setText(stringBuilder.toString());
    }
}
