package cn.cloudworkshop.miaoding.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.Toast;


/**
 * Author：binge on 2017-04-26 13:16
 * Email：1993911441@qq.com
 * Describe：Toast显示图片
 */
public class MyToast {
    public static void showToast(Context context, int imgId) {
        //new一个toast传入要显示的activity的上下文
        Toast toast = new Toast(context);
        //显示的时间
        toast.setDuration(Toast.LENGTH_SHORT);
        //显示的位置
        toast.setGravity(Gravity.CENTER, 0, 0);
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(imgId);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        toast.setView(imageView);
        toast.show();
    }

}
