package com.mingyuechunqiu.agile.ui.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.mingyuechunqiu.agile.R;

/**
 * <pre>
 *      Project:    DoomsdayTaoistSanctuary
 *
 *      author:     xiyujie
 *      Github:     https://github.com/MingYueChunQiu
 *      Email:      xiyujieit@163.com
 *      Time:       2019-05-18 13:48
 *      Desc:       按下状态容器控件
 *                  继承自LinearLayout
 *      Version:    1.0
 * </pre>
 */
public class PressStateViewGroup extends LinearLayout {

    private static final int DEFAULT_ANIMATOR_DURATION = 200;//默认动画时长
    private static final float DEFAULT_SCALE_RATIO = 0.9f;//默认伸缩比例

    private int mAnimatorDuration = DEFAULT_ANIMATOR_DURATION;
    private float mScaleX = DEFAULT_SCALE_RATIO, mScaleY = DEFAULT_SCALE_RATIO;
    private AnimatorSet mEnlargeAnimatorSet, mNarrowAnimatorSet;

    public PressStateViewGroup(Context context) {
        this(context, null);
    }

    public PressStateViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PressStateViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PressStateViewGroup);
        mAnimatorDuration = a.getInt(R.styleable.PressStateViewGroup_psvg_animator_duration, DEFAULT_ANIMATOR_DURATION);
        mScaleX = a.getFloat(R.styleable.PressStateViewGroup_psvg_scale_x, DEFAULT_SCALE_RATIO);
        mScaleY = a.getFloat(R.styleable.PressStateViewGroup_psvg_scale_y, DEFAULT_SCALE_RATIO);
        a.recycle();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                showNarrowAnimator();
                setPressed(true);
                break;
            case MotionEvent.ACTION_UP:
                showEnlargeAnimator();
                setPressed(false);
                performClick();
                break;
            case MotionEvent.ACTION_CANCEL:
                showEnlargeAnimator();
                setPressed(false);
                break;
        }
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public int getAnimatorDuration() {
        return mAnimatorDuration;
    }

    public void setAnimatorDuration(int animatorDuration) {
        this.mAnimatorDuration = animatorDuration;
    }

    public float getAnimatorScaleX() {
        return mScaleX;
    }

    public void setAnimatorScaleX(float scaleX) {
        if (scaleX < 0 || scaleX > 1) {
            return;
        }
        if (mEnlargeAnimatorSet != null) {
            mEnlargeAnimatorSet.cancel();
            mEnlargeAnimatorSet = null;
        }
        this.mScaleX = scaleX;
    }

    public float getAnimatorScaleY() {
        return mScaleY;
    }

    public void setAnimatorScaleY(float scaleY) {
        if (scaleY < 0 || scaleY > 1) {
            return;
        }
        if (mNarrowAnimatorSet != null) {
            mNarrowAnimatorSet.cancel();
            mNarrowAnimatorSet = null;
        }
        this.mScaleY = scaleY;
    }

    /**
     * 显示放大动画
     */
    private void showEnlargeAnimator() {
        if (mNarrowAnimatorSet != null) {
            mNarrowAnimatorSet.cancel();
        }
        if (mEnlargeAnimatorSet == null) {
            mEnlargeAnimatorSet = new AnimatorSet();
            ObjectAnimator xAnimator = ObjectAnimator.ofFloat(this, "scaleX", mScaleX, 1.0f)
                    .setDuration(mAnimatorDuration);
            ObjectAnimator yAnimator = ObjectAnimator.ofFloat(this, "scaleY", mScaleY, 1.0f)
                    .setDuration(mAnimatorDuration);
            mEnlargeAnimatorSet.playTogether(xAnimator, yAnimator);
        } else {
            mEnlargeAnimatorSet.cancel();
        }
        mEnlargeAnimatorSet.start();
    }

    /**
     * 显示缩小动画
     */
    private void showNarrowAnimator() {
        if (mEnlargeAnimatorSet != null) {
            mEnlargeAnimatorSet.cancel();
        }
        if (mNarrowAnimatorSet == null) {
            mNarrowAnimatorSet = new AnimatorSet();
            ObjectAnimator xAnimator = ObjectAnimator.ofFloat(this, "scaleX", 1.0f, mScaleX)
                    .setDuration(mAnimatorDuration);
            ObjectAnimator yAnimator = ObjectAnimator.ofFloat(this, "scaleY", 1.0f, mScaleY)
                    .setDuration(mAnimatorDuration);
            mNarrowAnimatorSet.playTogether(xAnimator, yAnimator);
        } else {
            mNarrowAnimatorSet.cancel();
        }
        mNarrowAnimatorSet.start();
    }
}