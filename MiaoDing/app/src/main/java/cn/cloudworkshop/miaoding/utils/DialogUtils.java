package cn.cloudworkshop.miaoding.utils;

import android.app.Dialog;
import android.content.Context;

import cn.cloudworkshop.miaoding.R;

/**
 * Author：binge on 2017-07-14 11:38
 * Email：1993911441@qq.com
 * Describe：
 */
public class DialogUtils {

    private static Dialog dialog;

    //显示dialog的方法
    public static void showDialog(Context context) {
        dialog = new Dialog(context, R.style.MyDialog);//dialog样式
        dialog.setContentView(R.layout.layout_loading);//dialog布局文件
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);//点击外部不允许关闭dialog

    }

    /**
     * 隐藏
     */
    public static void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog.cancel();
        }
    }
}
