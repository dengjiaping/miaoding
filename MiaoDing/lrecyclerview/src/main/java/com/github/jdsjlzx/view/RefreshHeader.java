package com.github.jdsjlzx.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.jdsjlzx.R;
import com.github.jdsjlzx.interfaces.IRefreshHeader;

/**
 * Author：binge on 2017-07-07 18:41
 * Email：1993911441@qq.com
 * Describe：
 */
public class RefreshHeader extends LinearLayout implements IRefreshHeader{
    private LinearLayout mContainer;
    private int mState = STATE_NORMAL;
    public int mMeasuredHeight;
    public RefreshHeader(Context context) {
        super(context);
        initView();
    }

    public RefreshHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }


    private void initView(){
        // 初始情况，设置下拉刷新view高度为0
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);
        this.setLayoutParams(lp);
        this.setPadding(0, 0, 0, 0);

        mContainer = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.listview_header, null);
        addView(mContainer, new LayoutParams(LayoutParams.MATCH_PARENT, 0));
        setGravity(Gravity.BOTTOM);

        measure(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        mMeasuredHeight = getMeasuredHeight();
    }

    public void setState(int state) {
        if (state == mState) return ;

        if (state == STATE_REFRESHING) {	// 显示进度

            smoothScrollTo(mMeasuredHeight);
        } else if(state == STATE_DONE) {

        } else {	// 显示箭头图片

        }


        switch(state){
            case STATE_NORMAL:
                if (mState == STATE_RELEASE_TO_REFRESH) {

                }
                if (mState == STATE_REFRESHING) {

                }

                break;
            case STATE_RELEASE_TO_REFRESH:
                if (mState != STATE_RELEASE_TO_REFRESH) {

                }
                break;
            case STATE_REFRESHING:

                break;
            case STATE_DONE:

                break;
            default:
        }

        mState = state;
    }

    public void reset() {
        smoothScrollTo(0);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                setState(STATE_NORMAL);
            }
        }, 500);
    }

    private void smoothScrollTo(int destHeight) {
        ValueAnimator animator = ValueAnimator.ofInt(getVisibleHeight(), destHeight);
        animator.setDuration(300).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                setVisibleHeight((int) animation.getAnimatedValue());
            }
        });
        animator.start();
    }

    public void setVisibleHeight(int height) {
        if (height < 0) height = 0;
        LayoutParams lp = (LayoutParams) mContainer .getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }


    @Override
    public void onReset() {
        setState(STATE_NORMAL);
    }

    @Override
    public void onPrepare() {
        setState(STATE_RELEASE_TO_REFRESH);
    }

    @Override
    public void onRefreshing() {
        setState(STATE_REFRESHING);
    }

    @Override
    public void onMove(float offSet, float sumOffSet) {

    }

    @Override
    public boolean onRelease() {
        return false;
    }

    @Override
    public void refreshComplete() {

    }

    @Override
    public View getHeaderView() {
        return null;
    }

    @Override
    public int getVisibleHeight() {
        return 0;
    }
}
