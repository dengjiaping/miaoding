package cn.cloudworkshop.miaoding.view;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Author：binge on 2017/3/10 10:59
 * Email：1993911441@qq.com
 * Describe：相机
 */
public class PreviewFrameLayout extends RelativeLayout {
    /** A callback to be invoked when the preview frame's size changes. */
    private Context context = null;


    private double mAspectRatio = 4.0 / 3.0;

    public PreviewFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setAspectRatio(double ratio) {
        if (ratio <= 0.0)
            throw new IllegalArgumentException();
        if (mAspectRatio != ratio) {
            mAspectRatio = ratio;
            requestLayout();
        }
    }


    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int previewWidth = MeasureSpec.getSize(widthSpec);
        int previewHeight = MeasureSpec.getSize(heightSpec);

        if (previewWidth < previewHeight) {
            int tmp = previewWidth;
            previewWidth = previewHeight;
            previewHeight = tmp;
        }

        if (previewWidth > previewHeight * mAspectRatio) {
            previewWidth = (int) (previewHeight * mAspectRatio + .5);
        }
        else {
            previewHeight = (int) (previewWidth / mAspectRatio + .5);
        }

        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            super.onMeasure(MeasureSpec.makeMeasureSpec(previewHeight, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(previewWidth, MeasureSpec.EXACTLY));
        } else {
            super.onMeasure(MeasureSpec.makeMeasureSpec(previewWidth, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(previewHeight, MeasureSpec.EXACTLY));
        }
    }
}

