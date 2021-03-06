package com.mingyuechunqiu.agile.ui.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;

import com.mingyuechunqiu.agile.R;

/**
 * <pre>
 *     author : xyj
 *     e-mail : yujie.xi@ehailuo.com
 *     time   : 2018/06/25
 *     desc   : 带密码隐藏功能和清除按钮的输入框
 *              继承自AppCompatEditText
 *     version: 1.0
 * </pre>
 */
public class PasswordClearEditText extends AppCompatEditText {

    private static final int DEFAULT_EYE_OPEN_RES_ID = R.drawable.agile_eye_open;
    private static final int DEFAULT_EYE_CLOSED_RES_ID = R.drawable.agile_eye_closed;
    private static final int DEFAULT_CLEAR_RES_ID = R.drawable.agile_close;
    //图标按钮的间距、宽度、距离右边边距
    private final int DEFAULT_BUTTON_PADDING =
            getResources().getDimensionPixelSize(R.dimen.dp_5);
    private final int DEFAULT_BUTTON_WIDTH =
            getResources().getDimensionPixelSize(R.dimen.dp_20);
    private final int DEFAULT_BUTTON_RIGHT_MARGIN = getResources().getDimensionPixelSize(R.dimen.dp_5);
    private static final int ANIMATOR_TIME = 200;//动画时间

    private boolean isPasswordVisible, isClearVisible;//密码隐藏、清除图标是否可见
    private int mEyeOpenResId, mEyeClosedResId, mClearResId;//图片资源id
    private Bitmap mBpEyeOpen, mBpEyeClosed, mBpClear;
    private int mBtnPadding, mTextPadding;//防止文字与图标重叠
    private int mBtnWidth;
    private int mBtnRightMargin;//最右边按钮距离边框距离
    private boolean isEyeOpen, isClearShown;//记录当前是否眼睛睁开，清除按钮是否可见，眼睛按钮是否可见
    private boolean isAutoHide;//标记按钮是否自动消失

    //出现和消失动画
    private ValueAnimator mGoneAnimator;
    private ValueAnimator mVisibleAnimator;
    private Paint mPaint;
    private OnClickIconButtonListener mListener;

    public PasswordClearEditText(Context context) {
        this(context, null);
    }

    public PasswordClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PasswordClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //设置右内边距, 防止清除按钮和文字重叠
        setPadding(getPaddingLeft(), getPaddingTop(), mTextPadding, getPaddingBottom());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawButtons(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //如果点击的时候，里面已经有文字了，则显示清除图标
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!isClearShown && length() > 0) {
                isClearShown = true;
                startVisibleAnimator();
            }
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            performClick();
            boolean eyeTouched =
                    (getWidth() - mBtnPadding - mBtnRightMargin - mBtnWidth < event.getX())
                            && (event.getX() < getWidth() - mBtnPadding - mBtnRightMargin)
                            && isFocused();
            boolean clearTouched =
                    (getWidth() - mBtnPadding * 3 - mBtnRightMargin - mBtnWidth * 2 < event.getX())
                            && (event.getX() < getWidth() - mBtnPadding * 3 - mBtnRightMargin - mBtnWidth)
                            && isFocused();

            //如果眼睛图标不可见，则让一键清除图标采用眼睛图标的位置判断
            if (!isPasswordVisible) {
                clearTouched = eyeTouched;
                eyeTouched = false;
            }

            if (eyeTouched) {
                if (isEyeOpen) {
                    hidePassword();
                    if (mListener != null) {
                        mListener.onClickEyeOpen();
                    }
                } else {
                    isEyeOpen = true;
                    setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    if (getText() != null) {
                        setSelection(getText().length());
                    }
                    if (mListener != null) {
                        mListener.onClickEyeClose();
                    }
                }
                invalidate();
                return true;
            } else if (clearTouched) {
                setText("");
                if (mListener != null) {
                    mListener.onClickClear();
                }
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (text.length() > 0 && isFocused()) {
            if (!isClearShown) {
                isClearShown = true;
                startVisibleAnimator();
            }
        } else {
            if (isClearShown) {
                isClearShown = false;
                startGoneAnimator();
            }
        }
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (!isAutoHide) {
            return;
        }
        if (focused) {
            if (!isClearShown) {
                isClearShown = true;
                startVisibleAnimator();
            }
        } else {
            if (isClearShown) {
                isClearShown = false;
                startGoneAnimator();
            }
        }
    }

    public OnClickIconButtonListener getOnClickIconButtonListener() {
        return mListener;
    }

    public void setOnClickIconButtonListener(OnClickIconButtonListener listener) {
        mListener = listener;
    }

    /**
     * 当密码显示按钮允许显示时，可以控制隐藏密码
     */
    public void hidePassword() {
        if (!isPasswordVisible) {
            return;
        }
        isEyeOpen = false;
        setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
        if (getText() != null) {
            setSelection(getText().length());
        }
    }

    /**
     * 初始化相关资源
     *
     * @param context 上下文
     * @param attrs   属性
     */
    private void init(@NonNull Context context, AttributeSet attrs) {
        //抗锯齿和位图滤波
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PasswordClearEditText);
        isPasswordVisible = a.getBoolean(R.styleable.PasswordClearEditText_pcet_password_is_visible, true);
        isClearVisible = a.getBoolean(R.styleable.PasswordClearEditText_pcet_clear_btn_is_visible, false);
        mEyeOpenResId = a.getResourceId(R.styleable.PasswordClearEditText_pcet_eye_open_drawable, DEFAULT_EYE_OPEN_RES_ID);
        mEyeClosedResId = a.getResourceId(R.styleable.PasswordClearEditText_pcet_eye_close_drawable, DEFAULT_EYE_CLOSED_RES_ID);
        mClearResId = a.getResourceId(R.styleable.PasswordClearEditText_pcet_clear_btn_drawable, DEFAULT_CLEAR_RES_ID);
        mBtnPadding = a.getDimensionPixelSize(R.styleable.PasswordClearEditText_pcet_button_padding, DEFAULT_BUTTON_PADDING);
        mBtnWidth = a.getDimensionPixelSize(R.styleable.PasswordClearEditText_pcet_button_width, DEFAULT_BUTTON_WIDTH);
        mBtnRightMargin = a.getDimensionPixelSize(R.styleable.PasswordClearEditText_pcet_button_right_margin, DEFAULT_BUTTON_RIGHT_MARGIN);
        if (isPasswordVisible) {
            mBpEyeOpen = BitmapFactory.decodeResource(getResources(), mEyeOpenResId);
            mBpEyeClosed = BitmapFactory.decodeResource(getResources(), mEyeClosedResId);
        }
        if (isClearVisible) {
            mBpClear = BitmapFactory.decodeResource(getResources(), mClearResId);
        }
        isAutoHide = a.getBoolean(R.styleable.PasswordClearEditText_pcet_btn_auto_hide, true);
        a.recycle();
        //给文字设置一个padding，避免文字和按钮重叠了
        mTextPadding = mBtnPadding * 4 + mBtnWidth * 2;
        if (!isPasswordVisible) {
            mTextPadding = mBtnPadding * 2 + mBtnWidth;
        }
        //按钮出现和消失的动画
        mGoneAnimator = ValueAnimator.ofFloat(1f, 0f).setDuration(ANIMATOR_TIME);
        mVisibleAnimator = ValueAnimator.ofFloat(0f, 1f).setDuration(ANIMATOR_TIME);
        //默认密码是隐藏状态
        hidePassword();
    }

    private void drawButtons(Canvas canvas) {
        //绘制按钮显示与消失效果
        if (isClearShown) {
            if (mVisibleAnimator.isRunning()) {
                float scale = (float) mVisibleAnimator.getAnimatedValue();
                drawButton(canvas, scale, false);
                drawButton(canvas, scale, true);
            } else {
                drawButton(canvas, 1, false);
                drawButton(canvas, 1, true);
            }
        } else {
            if (mGoneAnimator.isRunning()) {
                float scale = (float) mGoneAnimator.getAnimatedValue();
                drawButton(canvas, scale, false);
                drawButton(canvas, scale, true);
            }
        }
        invalidate();
    }

    /**
     * 绘制图标按钮
     *
     * @param canvas      画布
     * @param scale       根据动画进度显示绘制效果
     * @param isDrawClear 是绘制一键清除图标还是眼睛图标
     */
    private void drawButton(Canvas canvas, float scale, boolean isDrawClear) {
        if (isDrawClear) {
            if (!isClearVisible) {
                return;
            }
        } else {
            if (!isPasswordVisible) {
                return;
            }
        }
        int right = (int) (getWidth() + getScrollX() - mBtnPadding - mBtnRightMargin -
                mBtnWidth * (1f - scale) / 2);
        int left = (int) (getWidth() + getScrollX() - mBtnPadding - mBtnRightMargin -
                mBtnWidth * (scale + (1f - scale) / 2f));
        //根据眼睛图标是否可见，来决定是否调整一键清除图标的位置
        if (isDrawClear && isPasswordVisible) {
            right = (int) (getWidth() + getScrollX() - mBtnPadding * 3 - mBtnRightMargin -
                    mBtnWidth - mBtnWidth * (1f - scale) / 2f);
            //宽度 * scale + (宽度- 宽度* scale)/2 = 宽度 * scale + 宽度* （1- scale） /2
            // = 宽度* (scale + (1 - scale)/2)
            left = (int) (getWidth() + getScrollX() - mBtnPadding * 3 - mBtnWidth - mBtnRightMargin -
                    mBtnWidth * (scale + (1f - scale) / 2f));
        }
        int top = (int) ((getHeight() - mBtnWidth * scale) / 2);
        int bottom = (int) (top + mBtnWidth * scale);
        Rect rect = new Rect(left, top, right, bottom);
        if (isDrawClear) {
            canvas.drawBitmap(mBpClear, null, rect, mPaint);
        } else {
            if (isEyeOpen) {
                canvas.drawBitmap(mBpEyeOpen, null, rect, mPaint);
            } else {
                canvas.drawBitmap(mBpEyeClosed, null, rect, mPaint);
            }
        }
    }

    // 清除按钮出现时的动画效果
    private void startVisibleAnimator() {
        endAllAnimator();
        mVisibleAnimator.start();
        invalidate();
    }

    // 清除按钮消失时的动画效果
    private void startGoneAnimator() {
        endAllAnimator();
        mGoneAnimator.start();
        invalidate();
    }

    // 结束所有动画
    private void endAllAnimator() {
        mGoneAnimator.end();
        mVisibleAnimator.end();
    }

    /**
     * 监听按钮下拉收回的点击事件
     */
    public interface OnClickIconButtonListener {

        /**
         * 当点击睁开眼睛按钮时回调
         */
        void onClickEyeOpen();

        /**
         * 当点击闭上眼睛按钮时回调
         */
        void onClickEyeClose();

        /**
         * 当点击清除按钮时回调
         */
        void onClickClear();
    }
}
