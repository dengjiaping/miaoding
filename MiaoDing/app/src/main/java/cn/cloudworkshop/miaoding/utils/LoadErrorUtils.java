package cn.cloudworkshop.miaoding.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import cn.cloudworkshop.miaoding.R;

/**
 * Author：Libin on 2017-07-14 11:38
 * Email：1993911441@qq.com
 * Describe：网络加载失败弹窗
 */
public class LoadErrorUtils {

    private static Dialog dialog;

    //显示dialog的方法
    public static void showDialog(final Context context, final OnRefreshListener onRefreshListener) {
        if (context != null && !((Activity) context).isFinishing()) {
            dialog = new Dialog(context, R.style.MyDialog);//dialog样式
            View view = LayoutInflater.from(context).inflate(R.layout.layout_loading, null);
            dialog.setContentView(view);
//            dialog.setCancelable(false);
//            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            ToastUtils.showToast(context, "网络连接失败，请检查网络后点击刷新");
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissDialog();
                    onRefreshListener.onRefresh();
                }
            });

            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    onRefreshListener.onRefresh();
                }
            });

        }
    }

    /**
     * 隐藏
     */
    private static void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
    }

    /**
     * 点击刷新
     */
    public interface OnRefreshListener {
        void onRefresh();
    }

}
