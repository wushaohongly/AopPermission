package com.wushaohong.library.dialog;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;

/**
 * 权限拒绝申请对话框，抽象类
 */

public abstract class PermissionRefuseDialog extends Dialog {

    protected String[] permissions;

    public PermissionRefuseDialog(@NonNull Context context) {
        super(context);
    }

    public void setPermissions(String[] permissions) {
        this.permissions = permissions;
    }
}
