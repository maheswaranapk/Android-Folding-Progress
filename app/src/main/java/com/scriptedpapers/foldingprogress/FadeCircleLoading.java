package com.scriptedpapers.foldingprogress;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;

import java.util.ArrayList;

/**
 * Created by mahes on 4/1/16.
 */
public class FadeCircleLoading extends View {

    AnimatorSet set;

    Paint frontPaint;
    Paint bgPaint;

    float vTop;
    float vBottom;

    int dimension = 0;

    int ANIM_DURATION = 1000;

    boolean mIsHalfDone;
    float previousvTop;

    int YELLOW = 0xFFFFEB3B;
    int GREEN = 0xFF33691E;

    int BLUE = 0xFF5E35B1;
    int RED = 0xFFFF3D00;

    int[] FRONT_COLOR = {YELLOW, BLUE, GREEN, RED};
    int[] BG_COLOR = {BLUE, GREEN, RED, YELLOW};

    int mode;

    int HORIZONTAL = 1;
    int VERTICAL = 2;

    int directionFactor = 180;

    Animator outerAnimator;
    Animator innerAnimator;


    public FadeCircleLoading(Context context) {
        super(context);
    }

    public FadeCircleLoading(Context context, AttributeSet attrs) {
        super(context, attrs);
        onCreate();
    }

    public FadeCircleLoading(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreate();
    }

    void onCreate() {

        directionFactor = 0;

        mode = VERTICAL;

        bgPaint = new Paint();
        bgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        bgPaint.setColor(0xFF4caf50);
        bgPaint.setAntiAlias(true);

        frontPaint = new Paint();
        frontPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        frontPaint.setColor(0xFFffeb3b);
        frontPaint.setAntiAlias(true);

    }

    void reset() {

        vTop = 0;
        vBottom = 0;
        mIsHalfDone = false;
        previousvTop = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mode == HORIZONTAL) {

            RectF innerRectF = new RectF(0, 0 + vTop, dimension, dimension + vBottom);
            RectF rect2 = new RectF(0, 0, dimension, dimension);

            canvas.drawArc(rect2, 0 + directionFactor, 360, false, bgPaint);

            canvas.drawArc(rect2, 0 + directionFactor, 180, false, frontPaint);

            if (mIsHalfDone)
                canvas.drawArc(innerRectF, 0 + directionFactor, 180, false, bgPaint);
            else
                canvas.drawArc(innerRectF, 180 + directionFactor, 180, false, frontPaint);
        } else if (mode == VERTICAL) {


            RectF innerRectF = new RectF(0 + vTop, 0, dimension + vBottom, dimension);
            RectF rect2 = new RectF(0, 0, dimension, dimension);

            canvas.drawArc(rect2, 90 + directionFactor, 360, false, bgPaint);

            canvas.drawArc(rect2, 90 + directionFactor, 180, false, frontPaint);

            if (mIsHalfDone)
                canvas.drawArc(innerRectF, 90 + directionFactor, 180, false, bgPaint);
            else
                canvas.drawArc(innerRectF, 270 + directionFactor, 180, false, frontPaint);

        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (w < h)
            dimension = w;
        else
            dimension = h;

        invalidate();
    }

    public void startAnimation() {
        if (set == null) {

            set = new AnimatorSet();
            ArrayList<Animator> viewAnimList = new ArrayList<Animator>();

            invalidate();

            for (int i = 0; i < 4; i++) {

                final int finalI = i;

                outerAnimator = ObjectAnimator.ofFloat(FadeCircleLoading.this, "vTop",
                        0, dimension / 2, 0);
                outerAnimator.setInterpolator(new AccelerateInterpolator());
                outerAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                        reset();

                        bgPaint.setColor(BG_COLOR[finalI]);
                        frontPaint.setColor(FRONT_COLOR[finalI]);

                        if (finalI == 0) {

                            mode = HORIZONTAL;
                            directionFactor = 0;
                        } else if (finalI == 1) {

                            mode = VERTICAL;
                            directionFactor = 180;
                        } else if (finalI == 2) {

                            mode = HORIZONTAL;
                            directionFactor = 180;
                        } else if (finalI == 3) {

                            mode = VERTICAL;
                            directionFactor = 0;
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
                outerAnimator.setStartDelay(i * ANIM_DURATION);
                outerAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                outerAnimator.setDuration(ANIM_DURATION);

                innerAnimator = ObjectAnimator.ofFloat(FadeCircleLoading.this, "vBottom",
                        0, -dimension / 2, 0);
                innerAnimator.setInterpolator(new AccelerateInterpolator());
                innerAnimator.setDuration(ANIM_DURATION);
                innerAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                innerAnimator.setStartDelay(i * ANIM_DURATION);

                viewAnimList.add(outerAnimator);
                viewAnimList.add(innerAnimator);

            }

            set.playTogether(viewAnimList);

            set.addListener(new AnimatorListenerAdapter() {

                private boolean mCanceled;

                @Override
                public void onAnimationStart(Animator animation) {
                    mCanceled = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    mCanceled = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (!mCanceled) {
                        animation.start();
                    }
                }
            });

            set.start();
        }
    }

    public void setVBottom(float vBottom) {
        this.vBottom = vBottom;
        invalidate();
    }

    public void setVTop(float vTop) {

        if (previousvTop > vTop)
            mIsHalfDone = true;

        previousvTop = vTop;

        this.vTop = vTop;
        invalidate();
    }
}
