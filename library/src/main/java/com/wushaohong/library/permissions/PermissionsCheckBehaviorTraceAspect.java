package com.wushaohong.library.permissions;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.wushaohong.library.AopPermissionsConfig;
import com.wushaohong.library.dialog.PermissionRefuseDialog;
import com.wushaohong.library.log.MyLog;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.ArrayList;

@Aspect
public class PermissionsCheckBehaviorTraceAspect {

    private static final String POINTCUT_METHOD =
            "execution(@com.wushaohong.library.permissions.PermissionsCheck * *(..))";

    private static final String FRAGMENT_TAG = "PermissionsRequestFragment";

    /**
     * 切入点
     */
    @Pointcut(POINTCUT_METHOD)
    public void methodAnnottatedWithBehaviorTrace() {
    }

    /**
     * 切入面
     */
//    @Before("methodAnnottatedWithBehaviorTrace()")
    @Around("methodAnnottatedWithBehaviorTrace()")
    public void weaveJoinPoint(final ProceedingJoinPoint joinPoint) throws Throwable {

        // 获取注解内容
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        PermissionsCheck annotation = method.getAnnotation(PermissionsCheck.class);
        String[] permissions = annotation.value();

        Object object = joinPoint.getThis();

        FragmentActivity activity = null;
        // Fragment
        if (object instanceof Fragment) {
            activity = ((Fragment) object).getActivity();
        } else if (object instanceof Activity) {
            // Activity
            activity = (FragmentActivity) object;
        }

        // 检查 FragmentActivity
        if (activity == null) {
            return;
        }

        // 检查权限
        ArrayList<String> afterCheckPermissions = new ArrayList<>();

        for (String permission : permissions) {
            if (PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(activity, permission)) {
                afterCheckPermissions.add(permission);
            }
        }

        if (afterCheckPermissions.isEmpty()) {
            joinPoint.proceed();
            return;
        }

        // 权限拒绝对话框
        final PermissionRefuseDialog permissionRefuseDialog = AopPermissionsConfig.getRefuseDialog();

        // 启动申请权限 Fragment
        getFragment(activity).requestPermissions(new PermissionsListener() {
            @Override
            public void allow() {
                //
                MyLog.e("全部允许");
                try {
                    joinPoint.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            @Override
            public void refuse(String[] permissions) {
                // 打印
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < permissions.length; i++) {
                    String p = permissions[i];
                    stringBuilder.append(p);
                    if (i != permissions.length - 1) {
                        stringBuilder.append("\n");
                    }
                }
                MyLog.e("拒绝权限：" + stringBuilder.toString());

                if (permissionRefuseDialog != null) {
                    permissionRefuseDialog.setPermissions(permissions);
                    permissionRefuseDialog.show();
                }
            }
        }, afterCheckPermissions.toArray(new String[0]));

    }

    /**
     * 获取 Fragment
     * @param activity 任意 Activity
     * @return PermissionsRequestFragment
     */
    private PermissionsRequestFragment getFragment(FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment existedFragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG);
        if (existedFragment != null) {
            return (PermissionsRequestFragment) existedFragment;
        } else {
            PermissionsRequestFragment invisibleFragment = new PermissionsRequestFragment();
            fragmentManager.beginTransaction().add(invisibleFragment, FRAGMENT_TAG).commitNow();
            return invisibleFragment;
        }
    }
}
