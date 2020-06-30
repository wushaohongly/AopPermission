package com.wushaohong.library.permissions;

/**
 * 权限回调
 * 不考虑不再提示的情况
 */

public interface PermissionsListener {

    void allow();
    void refuse(String[] permissions);

}
