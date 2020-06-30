package com.wushaohong.library.permissions;

import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class PermissionsRequestFragment extends Fragment {

    private PermissionsListener permissionsListener;

    public PermissionsRequestFragment() {
    }

    public void requestPermissions(PermissionsListener permissionsListener, String[] permissions) {

        this.permissionsListener = permissionsListener;
        requestPermissions(permissions, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        ArrayList<String> deniedArray = new ArrayList<>();

        for (int i = 0; i < grantResults.length; i++) {
            if (PackageManager.PERMISSION_DENIED == grantResults[i]) {
                deniedArray.add(permissions[i]);
            }
        }

        if (deniedArray.isEmpty() && permissionsListener != null) {
            permissionsListener.allow();
        } else if (!deniedArray.isEmpty() && permissionsListener != null) {
            // refuse回调拒绝的权限
            permissionsListener.refuse(deniedArray.toArray(new String[0]));
        }
    }
}
