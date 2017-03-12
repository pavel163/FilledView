package com.ebr163.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ergashev on 12.03.17.
 */

public class FillableView extends View {

    public static final int START_LEFT = 0;
    public static final int START_TOP = 1;
    public static final int START_RIGHT = 2;
    public static final int START_BOTTOM = 3;

    private int fillColor = Color.WHITE;
    private int startPosition = START_LEFT;

    private final Path croppedProgressPath = new Path();
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float percent = 0.1F;
    private int width;
    private int height;

    private final Region region = new Region();

    public FillableView(Context context) {
        super(context);
    }

    public FillableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public FillableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FillableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(attrs);
    }

    private void initAttrs(@Nullable AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.FillableView,
                0, 0);

        try {
            fillColor = a.getColor(R.styleable.FillableView_fill_color, Color.WHITE);
            startPosition = a.getInteger(R.styleable.FillableView_start_position, 0);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        computeCroppedProgressPath();
        setMeasuredDimension(width, height);
    }

    private void computeCroppedProgressPath() {
        if (startPosition == START_RIGHT) {
            region.set((int) (width * (1F - percent)), 0, width, height);
        } else if (startPosition == START_LEFT) {
            region.set(0, 0, (int) (width * percent), height);
        } else if (startPosition == START_TOP) {
            region.set(0, 0, width, (int) (height * percent));
        } else if (startPosition == START_BOTTOM) {
            region.set(0, (int) (height * (1F - percent)), width, height);
        }
        croppedProgressPath.rewind();
        region.getBoundaryPath(croppedProgressPath);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(fillColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(croppedProgressPath, paint);
    }

    public void setProgress(float percent) {
        if (percent >= 0f && percent <= 1F) {
            this.percent = percent;
            computeCroppedProgressPath();
            invalidate();
        }
    }
}
