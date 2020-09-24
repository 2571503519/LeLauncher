package com.jacky.lelauncher;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class SpeedDashBoardView extends View {

    private int mRadius; // 扇形半径
    private int mStartAngle = 150; // 起始角度
    private int mSweepAngle = 240; // 扇形扫过的角度
    private int mMinSpeed = 0;
    private int mMaxSpeed = 100;
    private String mHeaderText = "KM/H";
    private int mVelocity = mMinSpeed;
    private int mStrokeWidth; // 画笔宽度
    private int mLength; // 刻度长度

    private int mPadding;
    private float mCenterX, mCenterY; // 圆心坐标
    private Paint mPaint;
    private RectF mOuterArc; // 外部扇形区域
    private RectF mInnerArc; // 内部扇形区域
    private Rect mRectText; // 速度区域
    private int mStartColor; // 渐变色起始色
    private int mEndColor; // 渐变色结束色



    public SpeedDashBoardView(Context context) {
        this(context, null);
    }

    public SpeedDashBoardView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpeedDashBoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mStrokeWidth = dp2px(3);
        mLength = dp2px(8) + mStrokeWidth;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.SQUARE);

        mOuterArc = new RectF();
        mInnerArc = new RectF();
        mRectText = new Rect();

        mStartColor = ContextCompat.getColor(getContext(), R.color.color_green);
        mEndColor = ContextCompat.getColor(getContext(), R.color.color_blue);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mPadding = Math.max(
                Math.max(getPaddingBottom(), getPaddingRight()),
                Math.max(getPaddingTop(), getPaddingLeft())
        );
        setPadding(mPadding, mPadding, mPadding, mPadding);
        int width = resolveSize(dp2px(260), widthMeasureSpec);
        mRadius = (width - mPadding * 2 - mStrokeWidth * 2) / 2;

        // 由起始角度确定的高度
        float[] startPoint = getCoordinatePoint(mRadius, mStartAngle);
        // 由结束角度确定的高度
        float[] endPoint = getCoordinatePoint(mRadius, mStartAngle + mSweepAngle);
        int height = (int) Math.max(startPoint[1] + mRadius + mStrokeWidth * 2,
                endPoint[1] + mRadius + mStrokeWidth * 2);
        setMeasuredDimension(width, height + getPaddingTop() + getPaddingBottom());

        mCenterX = mCenterY = getMeasuredWidth() / 2f;
        mInnerArc.set(
                getPaddingLeft() + mStrokeWidth,
                getPaddingTop() + mStrokeWidth,
                getMeasuredWidth() - getPaddingRight() - mStrokeWidth,
                getMeasuredWidth() - getPaddingBottom() - mStrokeWidth
        );

        mPaint.setTextSize(sp2px(16));
        mPaint.getTextBounds("0", 0, "0".length(), mRectText);
        mOuterArc.set(
                getPaddingLeft() + mRectText.height() + dp2px(30),
                getPaddingTop() + mRectText.height() + dp2px(30),
                getMeasuredWidth() - getPaddingRight() - mRectText.height() - dp2px(30),
                getMeasuredWidth() - getPaddingBottom() - mRectText.height() - dp2px(30)
        );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(ContextCompat.getColor(getContext(), R.color.color_dark));
        /**
         * 画圆弧
         */
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_light));
        canvas.drawArc(mInnerArc, mStartAngle, mSweepAngle, false, mPaint);
        /**
         * 画刻度
         * 画好起始角度的一条刻度后通过canvas绕着原点旋转来画剩下的长刻度
         */
        double cos = Math.cos(Math.toRadians(mStartAngle - 180));
        double sin = Math.sin(Math.toRadians(mStartAngle - 180));
        float x0 = (float) (mPadding + mStrokeWidth + mRadius * (1 - cos));
        float y0 = (float) (mPadding + mStrokeWidth + mRadius * (1 - sin));
        float x1 = (float) (mPadding + mStrokeWidth + mRadius - (mRadius - mLength) * cos);
        float y1 = (float) (mPadding + mStrokeWidth + mRadius - (mRadius - mLength) * sin);

        canvas.save();
        canvas.drawLine(x0, y0, x1, y1, mPaint);
        float angle = mSweepAngle * 1f / 60;
        for (int i = 0; i < 60; i++) {
            canvas.rotate(angle, mCenterX, mCenterY);
            canvas.drawLine(x0, y0, x1, y1, mPaint);
        }
        canvas.restore();

        /**
         * 画表头
         * 没有表头就不画
         */
        if (!TextUtils.isEmpty(mHeaderText)) {
            mPaint.setTextSize(sp2px(16));
            mPaint.setTextAlign(Paint.Align.CENTER);
            mPaint.getTextBounds(mHeaderText, 0, mHeaderText.length(), mRectText);
            canvas.drawText(mHeaderText, mCenterX, mCenterY - mRectText.height() * 3, mPaint);
        }

        /**
         * 画实时度数值
         */
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        mPaint.setStrokeWidth(dp2px(2));
        int xOffset = dp2px(22);
        if (mVelocity >= 100) {
            drawDigitalTube(canvas, mVelocity / 100, -xOffset);
            drawDigitalTube(canvas, (mVelocity - 100) / 10, 0);
            drawDigitalTube(canvas, mVelocity % 100 % 10, xOffset);
        } else if (mVelocity >= 10) {
            drawDigitalTube(canvas, -1, -xOffset);
            drawDigitalTube(canvas, mVelocity / 10, 0);
            drawDigitalTube(canvas, mVelocity % 10, xOffset);
        } else {
            drawDigitalTube(canvas, -1, -xOffset);
            drawDigitalTube(canvas, -1, 0);
            drawDigitalTube(canvas, mVelocity, xOffset);
        }

    }

    /**
     *       1
     *        ——
     *     2 |  | 3
     *        —— 4
     *     5 |  | 6
     *        ——
     *         7
     * 数码管样式
     */

    private void drawDigitalTube(Canvas canvas, int num, int xOffset) {
        float x = mCenterX + xOffset;
        float y = mCenterY + dp2px(40);
        int lx = dp2px(5);
        int ly = dp2px(10);
        int gap = dp2px(2);

        // 1
        mPaint.setAlpha(num == -1 || num == 1 || num == 4 ? 25 : 255);
        canvas.drawLine(x - lx, y, x + lx, y, mPaint);
        // 2
        mPaint.setAlpha(num == -1 || num == 1 || num == 2 || num == 3 || num == 7 ? 25 : 255);
        canvas.drawLine(x - lx - gap, y + gap, x - lx - gap, y + gap + ly, mPaint);
        // 3
        mPaint.setAlpha(num == -1 || num == 5 || num == 6 ? 25 : 255);
        canvas.drawLine(x + lx + gap, y + gap, x + lx + gap, y + gap + ly, mPaint);
        // 4
        mPaint.setAlpha(num == -1 || num == 0 || num == 1 || num == 7 ? 25 : 255);
        canvas.drawLine(x - lx, y + gap * 2 + ly, x + lx, y + gap * 2 + ly, mPaint);
        // 5
        mPaint.setAlpha(num == -1 || num == 1 || num == 3 || num == 4 || num == 5 || num == 7
                || num == 9 ? 25 : 255);
        canvas.drawLine(x - lx - gap, y + gap * 3 + ly,
                x - lx - gap, y + gap * 3 + ly * 2, mPaint);
        // 6
        mPaint.setAlpha(num == -1 || num == 2 ? 25 : 255);
        canvas.drawLine(x + lx + gap, y + gap * 3 + ly,
                x + lx + gap, y + gap * 3 + ly * 2, mPaint);
        // 7
        mPaint.setAlpha(num == -1 || num == 1 || num == 4 || num == 7 ? 25 : 255);
        canvas.drawLine(x - lx, y + gap * 4 + ly * 2, x + lx, y + gap * 4 + ly * 2, mPaint);
    }

    public float[] getCoordinatePoint(int radius, float angle) {
        float[] point = new float[2];

        double arcAngle = Math.toRadians(angle); //将角度转换为弧度
        if (angle < 90) {
            point[0] = (float) (mCenterX + Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY + Math.sin(arcAngle) * radius);
        } else if (angle == 90) {
            point[0] = mCenterX;
            point[1] = mCenterY + radius;
        } else if (angle > 90 && angle < 180) {
            arcAngle = Math.PI * (180 - angle) / 180.0;
            point[0] = (float) (mCenterX - Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY + Math.sin(arcAngle) * radius);
        } else if (angle == 180) {
            point[0] = mCenterX - radius;
            point[1] = mCenterY;
        } else if (angle > 180 && angle < 270) {
            arcAngle = Math.PI * (angle - 180) / 180.0;
            point[0] = (float) (mCenterX - Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY - Math.sin(arcAngle) * radius);
        } else if (angle == 270) {
            point[0] = mCenterX;
            point[1] = mCenterY - radius;
        } else {
            arcAngle = Math.PI * (360 - angle) / 180.0;
            point[0] = (float) (mCenterX + Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY - Math.sin(arcAngle) * radius);
        }

        return point;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }

    private int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                Resources.getSystem().getDisplayMetrics());
    }


}
