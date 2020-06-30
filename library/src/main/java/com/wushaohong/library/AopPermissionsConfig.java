package com.wushaohong.library;

import com.wushaohong.library.dialog.PermissionRefuseDialog;
import com.wushaohong.library.log.MyLog;

public class AopPermissionsConfig {

    private static PermissionRefuseDialog refuseDialog;

    public static void init() {
        MyLog.isLog = false;
    }

    public static PermissionRefuseDialog getRefuseDialog() {
        return refuseDialog;
    }

    public static void setRefuseDialog(PermissionRefuseDialog refuseDialog) {
        AopPermissionsConfig.refuseDialog = refuseDialog;
    }
}
