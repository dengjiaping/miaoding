package cn.cloudworkshop.miaoding.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import cn.cloudworkshop.miaoding.R;

/**
 * Author：binge on 2017/1/2 13:28
 * Email：1993911441@qq.com
 * Describe：圆环进度条
 */
public class RingProgressBar extends View {
    private Paint paint;
    //画圆所在的距形区域
    private RectF rectF;
    //圆环颜色
    private int ringColor;
    //圆环宽度
    private float ringWidth;
    //进度条颜色
    private int progressColor;
    //字体颜色
    private int textColor;
    //字体大小
    private float textSize;
    //副标题大小
    private float subTextSize;
    //最大进度
    private int max;
    //当前进度
    private int progress;

    public RingProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        paint = new Paint();
        rectF = new RectF();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RingProgressBar);
        ringColor = typedArray.getColor(R.styleable.RingProgressBar_ringColor, 0xffaaaaaa);
        ringWidth = typedArray.getDimension(R.styleable.RingProgressBar_ringWidth, 5);
        progressColor = typedArray.getColor(R.styleable.RingProgressBar_progressColor, 0xffea3a37);
        textColor = typedArray.getColor(R.styleable.RingProgressBar_textColor, 0xffaaaaaa);
        textSize = typedArray.getDimension(R.styleable.RingProgressBar_textSize, 20);
        subTextSize = typedArray.getDimension(R.styleable.RingProgressBar_subTextSize, 14);
        max = typedArray.getInteger(R.styleable.RingProgressBar_max, 30000);
        progress = typedArray.getInteger(R.styleable.RingProgressBar_progress, 0);
        typedArray.recycle();
    }

    public RingProgressBar(Context context) {
        this(context, null);
    }

    public RingProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        //画最外层圆环
        paint.setColor(ringColor);
        paint.setStrokeWidth(ringWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        rectF = new RectF(ringWidth / 2, ringWidth / 2, getWidth() - ringWidth / 2, getWidth() - ringWidth / 2);
        canvas.drawArc(rectF, 150, 240, false, paint);

        //画进度条
        paint.setColor(progressColor);
        canvas.drawArc(rectF, 150, ((float) progress / max) * 240, false, paint);

        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        float textWidth = paint.measureText(progress + "");
        canvas.drawText(progress + "", getWidth() / 2 - textWidth / 2, getWidth() / 5 * 2, paint);

        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(subTextSize);
        float subWidth = paint.measureText("成长值");
        canvas.drawText("成长值", getWidth() / 2 - subWidth / 2, getWidth() / 9 * 5, paint);


        paint.setColor(0xff2e2e2e);
        paint.setTextSize(18);
        for (int i = 0; i < 8; i++) {
            canvas.save();
            canvas.rotate(30 * i, getWidth() / 2, getWidth() / 2);

//            canvas.drawLine(getMetrics() / 20,
//                    Float.parseFloat(String.valueOf(getMetrics() / 2 + getMetrics() / 20 * 19*Math.tan(15 * Math.PI / 180))),
//                    getMetrics() / 14,
//                    Float.parseFloat(String.valueOf(getMetrics() / 2 + getMetrics() / 14 * 13*Math.tan(15 * Math.PI / 180))), paint);

//            canvas.drawText(4 * i + "K", Float.parseFloat(String.valueOf(getMetrics() / 2
//                            - getMetrics() / 5 * 2 * Math.cos(15 * Math.PI / 180))),
//                    Float.parseFloat(String.valueOf(getMetrics() / 2 + getMetrics() / 5 * 2
//                            * Math.sin(15 * Math.PI / 180))), paint);
            canvas.drawLine(getWidth() / 20, getWidth() / 2, getWidth() / 14, getWidth() / 2, paint);
            canvas.restore();

        }

        canvas.save();
        canvas.rotate(-30, getWidth() / 2, getWidth() / 2);
        canvas.drawLine(getWidth() / 20, getWidth() / 2, getWidth() / 14, getWidth() / 2, paint);
        canvas.restore();

        for (int i = 0; i < 70; i++) {
            canvas.save();
            canvas.rotate(3 * i, getWidth() / 2, getWidth() / 2);
//            canvas.drawLine(getMetrics() / 20,
//                    Float.parseFloat(String.valueOf(getMetrics() / 2 + getMetrics() / 20 * 19 * Math.tan(30 * Math.PI / 180))),
//                    getMetrics() / 16,
//                    Float.parseFloat(String.valueOf(getMetrics() / 2 + getMetrics() / 16 * 15 * Math.tan(30 * Math.PI / 180))), paint);
            canvas.drawLine(getWidth() / 20, getWidth() / 2, getWidth() / 16, getWidth() / 2, paint);
            canvas.restore();
        }

        for (int i = 0; i < 10; i++) {
            canvas.save();
            canvas.rotate(-3 * i, getWidth() / 2, getWidth() / 2);
            canvas.drawLine(getWidth() / 20, getWidth() / 2, getWidth() / 16, getWidth() / 2, paint);
            canvas.restore();
        }
    }

    public int getRingColor() {
        return ringColor;
    }

    public void setRingColor(int ringColor) {
        this.ringColor = ringColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getProgressColor() {
        return progressColor;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getRingWidth() {
        return ringWidth;
    }

    public void setRingWidth(float ringWidth) {
        this.ringWidth = ringWidth;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        if (progress > 0 && progress <= max) {
            this.progress = progress;
            postInvalidate();
        }

    }
}
