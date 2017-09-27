package cn.cloudworkshop.miaoding.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;

import cn.cloudworkshop.miaoding.R;

/**
 * Author：binge on 2017/9/25 17:27
 * Email：1993911441@qq.com
 * Describe：
 */
public class MyShadowLayout extends RelativeLayout {

    public static final int ALL = 0x1111;
    public static final int LEFT = 0x0001;
    public static final int TOP = 0x0010;
    public static final int RIGHT = 0x0100;
    public static final int BOTTOM = 0x1000;

    private static final int DEFAULT_COLOR = Color.parseColor("#a1a1a1");

    private float mShadowRadius = 0;

    private int mShadowColor = DEFAULT_COLOR;

    private float mLayoutRadius = 0;

    private float mShadowDX = 0;

    private float mShadowDY = 0;

    private int mShadowPosition = ALL;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private RectF mRectF = new RectF();

    private int mBackgroundColor = Color.parseColor("#f1f1f1");

    public MyShadowLayout(Context context) {
        this(context, null);
    }

    public MyShadowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyShadowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        float radius = mShadowRadius + dpToPx(5);
        float rectLeft = 0;
        float rectTop = 0;
        float rectRight = this.getWidth();
        float rectBottom = this.getHeight();

        if (((mShadowPosition & LEFT) == LEFT)) {
            rectLeft = radius;
        }

        if (((mShadowPosition & TOP) == TOP)) {
            rectTop = radius;
        }

        if (((mShadowPosition & RIGHT) == RIGHT)) {
            rectRight = this.getWidth() - radius;
        }

        if (((mShadowPosition & BOTTOM) == BOTTOM)) {
            rectBottom = this.getHeight() - radius;
        }

        mRectF.left = rectLeft;
        mRectF.top = rectTop;
        mRectF.right = rectRight;
        mRectF.bottom = rectBottom;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mLayoutRadius > 0) {
            canvas.drawRoundRect(mRectF, mLayoutRadius, mLayoutRadius, mPaint);
        } else {
            canvas.drawRect(mRectF, mPaint);
        }
    }

    private void initAttrs(AttributeSet attrs) {
        //关闭硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        //调用 onDraw
        this.setWillNotDraw(false);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MyShadowLayout);
        if (typedArray != null) {
            mShadowColor = typedArray.getColor(R.styleable.MyShadowLayout_shadowColor, DEFAULT_COLOR);
            mShadowPosition = typedArray.getInt(R.styleable.MyShadowLayout_shadowPosition, ALL);
            mShadowRadius = typedArray.getDimension(R.styleable.MyShadowLayout_shadowRadius, dpToPx(0));
            mShadowDX = typedArray.getDimension(R.styleable.MyShadowLayout_shadowDX, dpToPx(0));
            mShadowDY = typedArray.getDimension(R.styleable.MyShadowLayout_shadowDY, dpToPx(0));
            mLayoutRadius = typedArray.getDimension(R.styleable.MyShadowLayout_layoutRadius, dpToPx(0));
            mBackgroundColor = typedArray.getColor(R.styleable.MyShadowLayout_backgroundColor, Color.WHITE);
            typedArray.recycle();
        }
        initPaint();
    }

    private void initPaint() {
        mPaint.setAntiAlias(true);
        mPaint.setColor(mBackgroundColor);
        mPaint.setShadowLayer(mShadowRadius, mShadowDX, mShadowDY, mShadowColor);
    }

    private float dpToPx(float dp) {
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        float scale = dm.density;
        return (dp * scale + 0.5f);
    }
}