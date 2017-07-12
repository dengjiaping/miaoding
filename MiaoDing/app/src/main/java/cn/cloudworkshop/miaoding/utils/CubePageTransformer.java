package cn.cloudworkshop.miaoding.utils;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

/**
 * Author：binge on 2017-06-01 10:32
 * Email：1993911441@qq.com
 * Describe：
 */
public class CubePageTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(View view, float position) {
        if (position < -1) {
            view.setAlpha(0);
        } else if (position <= 0) {
            //从右向左滑动为当前View

            view.setAlpha(1);
            //设置旋转中心点；
            ViewHelper.setPivotX(view, view.getMeasuredWidth());
            ViewHelper.setPivotY(view, view.getMeasuredHeight() * 0.5f);

            //只在Y轴做旋转操作
            ViewHelper.setRotationY(view, 90f * position);
        } else if (position <= 1) {
            //从左向右滑动为当前View
            view.setAlpha(1);
            ViewHelper.setPivotX(view, 0);
            ViewHelper.setPivotY(view, view.getMeasuredHeight() * 0.5f);
            ViewHelper.setRotationY(view, 90f * position);
        }else {
            view.setAlpha(0);
        }
    }
}
