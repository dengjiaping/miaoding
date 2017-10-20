package cn.cloudworkshop.miaoding.utils;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import android.provider.Settings;
import android.support.v4.content.ContextCompat;

import cn.cloudworkshop.miaoding.R;

/**
 * Author：Libin on 2016/11/7 10:31
 * Email：1993911441@qq.com
 * Describe：运行时权限
 */
public class PermissionUtils {
    private Context mContext;

    public PermissionUtils(Context context) {
        this.mContext = context;
    }

    /**
     * 判断权限
     */
    public boolean judgePermissions(String[] permissions) {
        for (String permission : permissions) {
            if (deniedPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否缺少权限
     * PackageManager.PERMISSION_GRANTED 授予权限
     * PackageManager.PERMISSION_DENIED 缺少权限
     */
    private boolean deniedPermission(String permission) {
        return ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 提示对话框
     */
    public void showPermissionDialog(String msg) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext,
                R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        dialog.setTitle("帮助");
        dialog.setMessage("当前应用缺少" + msg + "权限，请点击\"设置\" - \"权限管理\"，打开所需权限。");
        //为“确定”按钮注册监听事件
        dialog.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 启动应用的设置
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + mContext.getPackageName()));
                mContext.startActivity(intent);
            }
        });
        //为“取消”按钮注册监听事件
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 根据实际情况编写相应代码。
                dialog.dismiss();
            }
        });
        dialog.create();
        dialog.show();

    }

}
